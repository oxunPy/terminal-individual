package com.example.rest.dto.dtoProjection;

import java.math.BigDecimal;
import java.util.Date;

public interface InvoiceProjection {
    Long getId();

    Long getClientId();

    Long getWarehouseId();

    String getWarehouse();

    String getPrintableName();

    Date getDate();

    Integer getType();

    String getInfo();

    Long getItemCount();

    BigDecimal getTotal();
}
