package com.ebookstore.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class PurchaseReq {
    @NotNull(message = "图书不能为空")
    private Long bookId;
    @NotNull(message = "数量不能为空")
    private Integer quantity;
    @NotNull(message = "单价不能为空")
    private BigDecimal price;
    private String supplier;
    private String operator;
}
