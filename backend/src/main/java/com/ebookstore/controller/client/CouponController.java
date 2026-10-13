package com.ebookstore.controller.client;

import com.ebookstore.entity.Coupon;
import com.ebookstore.service.interf.ICouponService;
import com.ebookstore.vo.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final ICouponService couponService;

    @GetMapping("/available")
    @PreAuthorize("isAuthenticated()")
    public Result<List<Coupon>> available() {
        return Result.success(couponService.getAvailableCoupons());
    }
}