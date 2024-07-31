package org.example.discount;


import org.springframework.stereotype.Component;

@Component
public class CouponClientFallBack implements CouponClient {
    @Override
    public CouponDTO findByCode(String code) {
        return new CouponDTO();
    }
}
