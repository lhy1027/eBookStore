package com.ebookstore.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewReq {
    @NotNull(message = "订单明细不能为空")
    private Long orderItemId;
    @NotNull(message = "图书不能为空")
    private Long bookId;
    @Min(value = 1, message = "评分最小值为1")
    @Max(value = 5, message = "评分最大值为5")
    private Integer rating;
    private String content;
}
