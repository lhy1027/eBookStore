package com.ebookstore.service.interf;

import com.ebookstore.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ebookstore.dto.request.OrderSubmitReq;

public interface IOrderService extends IService<Order> {
    Order submitOrder(OrderSubmitReq req, Long userId);
    void cancelOrder(Long orderId, Long userId);
    void payOrder(Long orderId, Long userId);
    void confirmReceipt(Long orderId, Long userId);
    void refundOrder(Long orderId, Long userId);
}
