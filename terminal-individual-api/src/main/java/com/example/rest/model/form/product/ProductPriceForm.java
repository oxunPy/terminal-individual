package com.example.rest.model.form.product;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductPriceForm {
    private Long id;

    private String date;

    private String standardCost;

    private Long currencyId;

    private String currency;
}
