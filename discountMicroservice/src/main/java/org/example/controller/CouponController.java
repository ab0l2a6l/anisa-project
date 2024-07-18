package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.CouponRequest;
import org.example.dto.CouponResponse;
import org.example.exception.NotFoundCoupon;
import org.example.model.entity.Coupon;
import org.example.model.service.CouponService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupon")
public class CouponController {
    private final CouponService couponService;
    private final ModelMapper mapper;
    @PostMapping("/save")
    public ResponseEntity<CouponResponse> createCoupon(@RequestBody CouponRequest coupon) {
        return ResponseEntity.ok(mapper.map(couponService.createCoupon(mapper.map(coupon, Coupon.class)), CouponResponse.class));
    }

    @GetMapping("/find/{code}")
    public ResponseEntity<CouponResponse> findByCode(@PathVariable("code") String code) throws NotFoundCoupon {
        return ResponseEntity.ok(mapper.map(couponService.findByCode(code), CouponResponse.class));
    }
}
