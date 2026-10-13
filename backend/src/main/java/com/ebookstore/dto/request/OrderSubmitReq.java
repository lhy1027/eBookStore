package com.ebookstore.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class OrderSubmitReq {
    @NotNull(message = "地址不能为空")
    private Long addressId;
    private Long couponId;
    @NotNull(message = "请选择图书")
    private List<Long> selectedBookIds;
}
