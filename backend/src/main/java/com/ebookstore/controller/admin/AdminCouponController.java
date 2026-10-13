package com.ebookstore.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ebookstore.annotation.Log;
import com.ebookstore.entity.Coupon;
import com.ebookstore.service.interf.ICouponService;
import com.ebookstore.vo.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/coupons")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminCouponController {

    private final ICouponService couponService;

    @GetMapping
    public Result<IPage<Coupon>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(couponService.page(new Page<>(page, size),
                new QueryWrapper<Coupon>().orderByDesc("create_time")));
    }

    @PostMapping
    @Log("创建优惠券")
    public Result<String> add(@RequestBody Coupon coupon) {
        couponService.save(coupon);
        return Result.success("创建成功");
    }

    @PutMapping("/{id}")
    @Log("修改优惠券")
    public Result<String> update(@PathVariable Long id, @RequestBody Coupon coupon) {
        coupon.setId(id);
        couponService.updateById(coupon);
        return Result.success("修改成功");
    }

    @DeleteMapping("/{id}")
    @Log("删除优惠券")
    public Result<String> delete(@PathVariable Long id) {
        couponService.removeById(id);
        return Result.success("删除成功");
    }
}
