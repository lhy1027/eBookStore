package com.ebookstore.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("review")
public class Review {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    @TableField(exist = false)
    private String username;
    @TableField(exist = false)
    private String bookName;
    private Long bookId;
    private Long orderItemId;
    private Integer rating;           // 1~5
    private String content;
    private Integer status;           // 正常/删除
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
