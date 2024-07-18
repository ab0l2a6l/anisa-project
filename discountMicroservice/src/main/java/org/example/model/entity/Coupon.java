package org.example.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String code;

    @NonNull
    private BigDecimal discount;

    @NonNull
    @Column(name = "expiry_date")
    @JsonProperty("date")
    private LocalDate expiryDate;
}
