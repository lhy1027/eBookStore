package com.ebookstore.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("purchase")
public class Purchase {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long bookId;
    private Integer quantity;
    private BigDecimal purchasePrice;
    private String supplier;
    @TableField("operator_id")
    private String operator;
    private LocalDateTime createTime;

    @TableField(exist = false)
    private String bookName;
    @TableField(exist = false)
    private BigDecimal price;
}
