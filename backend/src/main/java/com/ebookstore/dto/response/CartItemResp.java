package com.ebookstore.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CartItemResp {
    private Long id;
    private Long bookId;
    private String bookName;
    private String coverUrl;
    private String author;
    private BigDecimal price;
    private BigDecimal salePrice;
    private Integer quantity;
    private BigDecimal subtotal;
}