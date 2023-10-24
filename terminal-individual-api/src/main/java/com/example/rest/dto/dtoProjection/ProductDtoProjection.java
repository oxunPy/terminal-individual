package com.example.rest.dto.dtoProjection;

import java.math.BigDecimal;

public interface ProductDtoProjection {

    Long getId();

    String getProduct_name();

    BigDecimal getRate();

    String getCurrency();

    String getGroup_name();

    String getBarcode();
}
