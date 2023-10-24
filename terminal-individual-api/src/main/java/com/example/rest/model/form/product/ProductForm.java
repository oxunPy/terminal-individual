package com.example.rest.model.form.product;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ProductForm {

    private Long id;

    private String productName;

    private BigDecimal rate; // UZSdagi qiymat

    private BigDecimal originalRate; // original qiymat (UZS/USD)

    private String currency; // original qiymatdagi valyuta (UZS/USD)

    private String groupName;

    private String barcode;
}
