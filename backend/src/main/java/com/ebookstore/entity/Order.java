package com.ebookstore.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("orders")
public class Order {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String orderNo;
    @TableField("member_id")
    private Long userId;
    @TableField(exist = false)
    private Long addressId;
    @TableField("total_amount")
    private BigDecimal totalAmount;
    @TableField(exist = false)
    private BigDecimal discountAmount;
    @TableField(exist = false)
    private BigDecimal payAmount;
    @TableField("status")
    private String orderStatus;
    @TableField("logistics_status")
    private String logisticsStatus;
    private LocalDateTime payTime;
    private LocalDateTime shipTime;
    private LocalDateTime receiptTime;
    @TableField(exist = false)
    private LocalDateTime cancelTime;
    private LocalDateTime createTime;
    @TableField(exist = false)
    private LocalDateTime updateTime;
}
