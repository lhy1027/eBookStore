package com.ebookstore.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebookstore.entity.OrderItem;
import com.ebookstore.mapper.OrderItemMapper;
import com.ebookstore.service.interf.IOrderItemService;
import org.springframework.stereotype.Service;

@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements IOrderItemService {
}
