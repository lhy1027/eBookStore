package com.ebookstore.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("book")
public class Book {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String bookNo;          // 鍥句功缂栧彿
    private String isbn;
    private String name;
    private String author;
    private String publisher;
    private LocalDate publishDate;
    private Long categoryId;        // 浜岀骇鍒嗙被ID
    private String coverUrl;
    private String description;     // 绠€锟?
    private String detail;          // 璇︽儏
    private BigDecimal price;       // 瀹氫环
    private BigDecimal salePrice;   // 閿€鍞环锛堜細鍛樹环锟?
    private BigDecimal costPrice;   // 杩涗环
    private Integer stock;
    private Integer stockWarning;   // 棰勮闃堝€硷紝榛樿5
    private Integer status;         // 0涓嬫灦 1涓婃灦
    private Integer recommendStatus;// 0涓嶆帹锟?1鎺ㄨ崘
    private Integer salesCount;     // 閿€閲忥紙鍙啑浣欙級
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
