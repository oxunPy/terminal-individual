package com.example.rest.dto.dtoProjection;

import java.math.BigDecimal;

public interface InvoiceDtoProjection {

    Long getId();

    Long getAccount_id();

    Long getWarehouseId();

    String getWarehouse();

    String getAccount_printable_name();

    String getDate();

    Integer getType();

    Integer getStatus();

    Long getCount_of_items();

    BigDecimal getTotal();

    String getInfo();

}
