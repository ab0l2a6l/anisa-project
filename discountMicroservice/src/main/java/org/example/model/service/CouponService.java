package org.example.model.service;

import org.example.exception.NotFoundCoupon;
import org.example.model.entity.Coupon;
import org.springframework.stereotype.Service;


public interface CouponService {
    Coupon createCoupon(Coupon coupon);

    Coupon findByCode(String code) throws NotFoundCoupon;
}
