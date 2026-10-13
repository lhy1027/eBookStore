package com.ebookstore.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebookstore.entity.Coupon;
import com.ebookstore.mapper.CouponMapper;
import com.ebookstore.service.interf.ICouponService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements ICouponService {

    @Override
    public List<Coupon> getAvailableCoupons() {
        LocalDateTime now = LocalDateTime.now();
        return list(new QueryWrapper<Coupon>()
                .eq("status", 1)
                .le("expire_start", now)
                .ge("expire_end", now));
    }
}
