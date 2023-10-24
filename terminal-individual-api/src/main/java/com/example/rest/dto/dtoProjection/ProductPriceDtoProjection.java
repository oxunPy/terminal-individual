package com.example.rest.dto.dtoProjection;

import java.math.BigDecimal;

public interface ProductPriceDtoProjection {
    Long getId();

    String getDate();

    BigDecimal getRate();

    String getCurrency();
}
