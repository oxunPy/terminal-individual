package com.example.rest.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductDto {

    private Long id;

    private String productName;

    private BigDecimal rate; // UZSdagi qiymat

    private BigDecimal originalRate; // original qiymat (UZS/USD)

    private String currency; // original qiymatdagi valyuta (UZS/USD)

    private String groupName;
}
