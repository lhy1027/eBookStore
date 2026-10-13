package com.ebookstore.service.interf;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebookstore.entity.Coupon;
import java.util.List;

public interface ICouponService extends IService<Coupon> {
    List<Coupon> getAvailableCoupons();
}
