package com.example.rest.dto.dtoProjection;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ReportByDateProjection {
    Integer getInvoice_item_id();

    LocalDate getDate_of_sell();

    BigDecimal getProduct_quantity();

    BigDecimal getProduct_price();

    Integer getType_of_operation();

    String getProduct_name();

    Integer getPrice_currency();


}
