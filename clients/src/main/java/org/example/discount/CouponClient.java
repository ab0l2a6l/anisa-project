package org.example.discount;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "DISCOUNT", fallback = CouponClientFallBack.class)
public interface CouponClient {
    @GetMapping("/api/v1/coupon/find/{code}")
    CouponDTO findByCode(@PathVariable("code") String code);
}
