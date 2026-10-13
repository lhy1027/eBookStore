package com.ebookstore.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@TableName("order_item")
public class OrderItem {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long orderId;
    private Long bookId;
    private String bookName;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subtotal;
    private Integer reviewed;         // 0未评价，1已评价
}
