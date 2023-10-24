package com.example.rest.dto.dtoProjection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ExchangeRateDtoProjection {

    Long getId();

    LocalDateTime getInv_date();

    String getMain_currency();

    BigDecimal getMain_currency_val();

    String getTo_currency();

    BigDecimal getTo_currency_val();
}
