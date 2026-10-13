package com.ebookstore.controller.client;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ebookstore.entity.Order;
import com.ebookstore.entity.OrderItem;
import com.ebookstore.dto.request.OrderSubmitReq;
import com.ebookstore.mapper.OrderItemMapper;
import com.ebookstore.service.interf.IOrderService;
import com.ebookstore.util.SecurityUtil;
import com.ebookstore.vo.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client/orders")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;
    private final OrderItemMapper orderItemMapper;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public Result<IPage<Order>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String orderStatus) {
        Long userId = SecurityUtil.getCurrentUserId();
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("member_id", userId);
        if (orderStatus != null && !orderStatus.isEmpty()) {
            wrapper.eq("status", orderStatus);
        }
        wrapper.orderByDesc("create_time");
        return Result.success(orderService.page(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public Result<Order> detail(@PathVariable Long id) {
        Long userId = SecurityUtil.getCurrentUserId();
        Order order = orderService.getById(id);
        if (order == null || !order.getUserId().equals(userId)) {
            return Result.error(400, "订单不存在");
        }
        return Result.success(order);
    }

    @GetMapping("/{id}/items")
    @PreAuthorize("isAuthenticated()")
    public Result<List<OrderItem>> items(@PathVariable Long id) {
        Long userId = SecurityUtil.getCurrentUserId();
        Order order = orderService.getById(id);
        if (order == null || !order.getUserId().equals(userId)) {
            return Result.error(400, "订单不存在");
        }
        List<OrderItem> items = orderItemMapper.selectList(
                new QueryWrapper<OrderItem>().eq("order_id", id));
        return Result.success(items);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public Result<Order> submit(@Valid @RequestBody OrderSubmitReq req) {
        Long userId = SecurityUtil.getCurrentUserId();
        return Result.success(orderService.submitOrder(req, userId));
    }

    @PostMapping("/{id}/pay")
    @PreAuthorize("isAuthenticated()")
    public Result<String> pay(@PathVariable Long id) {
        Long userId = SecurityUtil.getCurrentUserId();
        orderService.payOrder(id, userId);
        return Result.success("支付成功");
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("isAuthenticated()")
    public Result<String> cancel(@PathVariable Long id) {
        Long userId = SecurityUtil.getCurrentUserId();
        orderService.cancelOrder(id, userId);
        return Result.success("已取消订单");
    }

    @PostMapping("/{id}/confirm")
    @PreAuthorize("isAuthenticated()")
    public Result<String> confirm(@PathVariable Long id) {
        Long userId = SecurityUtil.getCurrentUserId();
        orderService.confirmReceipt(id, userId);
        return Result.success("确认收货成功");
    }

    @PostMapping("/{id}/refund")
    @PreAuthorize("isAuthenticated()")
    public Result<String> refund(@PathVariable Long id) {
        Long userId = SecurityUtil.getCurrentUserId();
        orderService.refundOrder(id, userId);
        return Result.success("退款申请已提交");
    }
}