package com.ebookstore.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BookResp {
    private Long id;
    private String bookNo;
    private String isbn;
    private String name;
    private String author;
    private String publisher;
    private LocalDate publishDate;
    private Long categoryId;
    private String categoryName;
    private String coverUrl;
    private String description;
    private String detail;
    private BigDecimal price;
    private BigDecimal salePrice;
    private Integer stock;
    private Integer status;
    private Integer recommendStatus;
    private Integer salesCount;
    private Double avgRating;
    private Integer reviewCount;
}
