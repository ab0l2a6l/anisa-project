package org.example.model.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.example.exception.NotFoundCoupon;
import org.example.model.entity.Coupon;
import org.example.model.repository.CouponRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
    private final CouponRepository couponRepository;

    @Override
    public Coupon createCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    @Override
    @CircuitBreaker(name = "mm", fallbackMethod = "findByCodeFallBack")
    public Coupon findByCode(String code) throws NotFoundCoupon {
        Optional<Coupon> coupon = couponRepository.findByCode(code);
        if (coupon.isEmpty()) {
            throw new NotFoundCoupon("not found coupon");
        }
        return coupon.get();
    }

    public Coupon findByCodeFallBack(Throwable throwable) {
        return null;
    }
}
