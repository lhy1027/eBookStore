package com.ebookstore.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("message")
public class Message {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String content;
    private String reply;
    private LocalDateTime replyTime;
    private Integer status;
    private LocalDateTime createTime;

    @TableField(exist = false)
    private String username;
}
