package org.example.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class CouponRequest {
    private String code;
    private BigDecimal discount;
    private LocalDate expiryDate;
}
