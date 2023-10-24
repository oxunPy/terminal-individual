package com.example.rest.dto.dtoProjection;

import java.math.BigDecimal;

public interface InvoiceItemDtoProjection {

    Long getId();

    Long getProduct_id();

    String getProduct_name();

    String getCurrencyCode();

    BigDecimal getQuantity();

    BigDecimal getSelling_rate();

    BigDecimal getTotal();

}
