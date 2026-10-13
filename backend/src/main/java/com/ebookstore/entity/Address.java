package com.ebookstore.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("address")
public class Address {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String receiverName;
    private String phone;
    private String region;            // 省市区
    private String detailAddress;     // 详细地址
    private Integer isDefault;        // 1默认 0非默认
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
