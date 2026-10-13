package com.ebookstore.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("coupon")
public class Coupon {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String type;              // 满减/固定减
    private BigDecimal threshold;     // 使用门槛
    private BigDecimal discount;      // 优惠金额
    private LocalDateTime expireStart;
    private LocalDateTime expireEnd;
    private Integer stock;            // 发放数量
    private Integer status;           // 1启用 0禁用
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
