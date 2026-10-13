package com.ebookstore.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebookstore.dto.request.OrderSubmitReq;
import com.ebookstore.entity.*;
import com.ebookstore.exception.BusinessException;
import com.ebookstore.mapper.OrderItemMapper;
import com.ebookstore.mapper.OrderMapper;
import com.ebookstore.service.interf.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    private final IBookService bookService;
    private final ICartService cartService;
    private final IAddressService addressService;
    private final OrderItemMapper orderItemMapper;
    private final ICouponService couponService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order submitOrder(OrderSubmitReq req, Long userId) {
        // 1. 校验地址
        Address address = addressService.getById(req.getAddressId());
        if (address == null || !address.getUserId().equals(userId)) {
            throw new BusinessException("地址无效");
        }
        // 2. 获取购物车选中的图书
        List<CartItem> cartItems = cartService.getSelectedItems(userId, req.getSelectedBookIds());
        if (cartItems.isEmpty()) {
            throw new BusinessException("购物车为空");
        }
        // 3. 校验库存和上架状态，计算总价
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            Book book = bookService.getById(item.getBookId());
            if (book == null || book.getStatus() != 1) {
                throw new BusinessException("图书已下架：" + book.getName());
            }
            if (book.getStock() < item.getQuantity()) {
                throw new BusinessException("库存不足：" + book.getName());
            }
            total = total.add(book.getSalePrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        // 4. 应用优惠券
        BigDecimal discountAmount = BigDecimal.ZERO;
        if (req.getCouponId() != null) {
            Coupon coupon = couponService.getById(req.getCouponId());
            if (coupon != null && coupon.getStatus() == 1) {
                LocalDateTime now = LocalDateTime.now();
                if (now.isAfter(coupon.getExpireStart()) && now.isBefore(coupon.getExpireEnd())) {
                    if (total.compareTo(coupon.getThreshold()) >= 0) {
                        // 根据优惠券类型计算优惠金额
                        if ("满减".equals(coupon.getType())) {
                            discountAmount = coupon.getDiscount();
                        } else {
                            // 固定金额优惠券
                            discountAmount = coupon.getDiscount();
                        }
                    }
                }
            }
        }
        BigDecimal payAmount = total.subtract(discountAmount);
        if (payAmount.compareTo(BigDecimal.ZERO) < 0) {
            payAmount = BigDecimal.ZERO;
        }
        // 5. 生成订单
        Order order = new Order();
        order.setOrderNo(UUID.randomUUID().toString().replace("-", "").substring(0, 20));
        order.setUserId(userId);
        order.setAddressId(req.getAddressId());
        order.setTotalAmount(total);
        order.setDiscountAmount(discountAmount);
        order.setPayAmount(payAmount);
        order.setOrderStatus("待付款");
        order.setLogisticsStatus("未发货");
        order.setCreateTime(LocalDateTime.now());
        save(order);

        // 6. 生成订单明细并扣减库存
        for (CartItem item : cartItems) {
            Book book = bookService.getById(item.getBookId());
            OrderItem oi = new OrderItem();
            oi.setOrderId(order.getId());
            oi.setBookId(book.getId());
            oi.setBookName(book.getName());
            oi.setPrice(book.getSalePrice());
            oi.setQuantity(item.getQuantity());
            oi.setSubtotal(book.getSalePrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            oi.setReviewed(0);
            orderItemMapper.insert(oi);
            // 扣减库存
            book.setStock(book.getStock() - item.getQuantity());
            bookService.updateById(book);
        }
        // 7. 清空购物车已选图书
        cartService.removeSelected(userId, req.getSelectedBookIds());
        return order;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(Long orderId, Long userId) {
        Order order = getById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BusinessException("订单不存在");
        }
        if (!"待付款".equals(order.getOrderStatus()) && !"待发货".equals(order.getOrderStatus())) {
            throw new BusinessException("只能取消待支付或待发货订单");
        }
        // 恢复库存
        List<OrderItem> items = orderItemMapper.selectList(
                new QueryWrapper<OrderItem>().eq("order_id", orderId));
        for (OrderItem item : items) {
            Book book = bookService.getById(item.getBookId());
            if (book != null) {
                book.setStock(book.getStock() + item.getQuantity());
                bookService.updateById(book);
            }
        }
        order.setOrderStatus("已取消");
        order.setCancelTime(LocalDateTime.now());
        updateById(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void payOrder(Long orderId, Long userId) {
        Order order = getById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BusinessException("订单不存在");
        }
        if (!"待付款".equals(order.getOrderStatus())) {
            throw new BusinessException("订单状态不正确");
        }
        order.setOrderStatus("待发货");
        order.setLogisticsStatus("未发货");
        order.setPayTime(LocalDateTime.now());
        updateById(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmReceipt(Long orderId, Long userId) {
        Order order = getById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BusinessException("订单不存在");
        }
        if (!"已发货".equals(order.getOrderStatus()) && !"已签收".equals(order.getOrderStatus())) {
            throw new BusinessException("订单状态不正确");
        }
        order.setOrderStatus("已签收");
        order.setLogisticsStatus("已签收");
        order.setReceiptTime(LocalDateTime.now());
        updateById(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refundOrder(Long orderId, Long userId) {
        Order order = getById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BusinessException("订单不存在");
        }
        if (!"已发货".equals(order.getOrderStatus()) && !"已签收".equals(order.getOrderStatus())) {
            throw new BusinessException("只能对已发货或已签收订单申请退款");
        }
        order.setOrderStatus("退款申请中");
        updateById(order);
    }
}
