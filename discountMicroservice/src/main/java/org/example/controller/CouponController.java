package org.example.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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
    @CircuitBreaker(name = "myClient", fallbackMethod = "findByCodeFallBack")
    public ResponseEntity<CouponResponse> findByCode(@PathVariable("code") String code) throws NotFoundCoupon {
        return ResponseEntity.ok(mapper.map(couponService.findByCode(code), CouponResponse.class));
    }

    public ResponseEntity<CouponResponse> findByCodeFallBack(String code, Throwable throwable) {
        return ResponseEntity.ok(new CouponResponse());
    }
}
