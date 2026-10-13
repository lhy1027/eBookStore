package com.ebookstore.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ebookstore.annotation.Log;
import com.ebookstore.entity.Book;
import com.ebookstore.entity.Order;
import com.ebookstore.entity.OrderItem;
import com.ebookstore.mapper.BookMapper;
import com.ebookstore.mapper.OrderItemMapper;
import com.ebookstore.mapper.OrderMapper;
import com.ebookstore.vo.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminOrderController {

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final BookMapper bookMapper;

    @GetMapping
    public Result<IPage<Order>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) String orderStatus,
            @RequestParam(required = false) String memberName) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        if (orderNo != null && !orderNo.isEmpty()) {
            wrapper.like("order_no", orderNo);
        }
        if (orderStatus != null && !orderStatus.isEmpty()) {
            wrapper.eq("status", orderStatus);
        }
        if (memberName != null && !memberName.isEmpty()) {
            wrapper.inSql("member_id", "SELECT id FROM user WHERE username LIKE '%" + memberName + "%'");
        }
        wrapper.orderByDesc("create_time");
        return Result.success(orderMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @PutMapping("/{id}/pay")
    @Log("确认收款")
    public Result<String> pay(@PathVariable Long id) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            return Result.error(400, "订单不存在");
        }
        order.setOrderStatus("待发货");
        order.setLogisticsStatus("未发货");
        order.setPayTime(LocalDateTime.now());
        orderMapper.updateById(order);
        return Result.success("确认收款成功");
    }

    @PutMapping("/{id}/ship")
    @Log("发货")
    public Result<String> ship(@PathVariable Long id) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            return Result.error(400, "订单不存在");
        }
        order.setOrderStatus("已发货");
        order.setLogisticsStatus("已出库");
        order.setShipTime(LocalDateTime.now());
        orderMapper.updateById(order);
        return Result.success("发货成功");
    }

    @PutMapping("/{id}/logistics")
    @Log("更新物流状态")
    public Result<String> updateLogistics(@PathVariable Long id, @RequestBody Map<String, String> params) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            return Result.error(400, "订单不存在");
        }
        String status = params.get("logisticsStatus");
        if (status == null || status.isEmpty()) {
            return Result.error(400, "物流状态不能为空");
        }
        order.setLogisticsStatus(status);
        orderMapper.updateById(order);
        return Result.success("物流状态已更新");
    }

    @PutMapping("/{id}/status")
    @Log("更新订单状态")
    public Result<String> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> params) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            return Result.error(400, "订单不存在");
        }
        order.setOrderStatus(params.get("orderStatus"));
        orderMapper.updateById(order);
        return Result.success("更新成功");
    }

    @PutMapping("/{id}/cancel")
    @Log("取消订单")
    public Result<String> cancel(@PathVariable Long id) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            return Result.error(400, "订单不存在");
        }
        // 恢复库存
        List<OrderItem> items = orderItemMapper.selectList(
                new QueryWrapper<OrderItem>().eq("order_id", id));
        for (OrderItem item : items) {
            Book book = bookMapper.selectById(item.getBookId());
            if (book != null) {
                book.setStock(book.getStock() + item.getQuantity());
                bookMapper.updateById(book);
            }
        }
        order.setOrderStatus("已取消");
        order.setCancelTime(LocalDateTime.now());
        orderMapper.updateById(order);
        return Result.success("已取消");
    }

    @PutMapping("/{id}/refund-approve")
    @Log("同意退款")
    public Result<String> refundApprove(@PathVariable Long id) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            return Result.error(400, "订单不存在");
        }
        order.setOrderStatus("已退款");
        orderMapper.updateById(order);
        return Result.success("退款已处理");
    }

    @PutMapping("/{id}/refund-reject")
    @Log("拒绝退款")
    public Result<String> refundReject(@PathVariable Long id) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            return Result.error(400, "订单不存在");
        }
        order.setOrderStatus("已发货");
        orderMapper.updateById(order);
        return Result.success("已拒绝退款");
    }
}