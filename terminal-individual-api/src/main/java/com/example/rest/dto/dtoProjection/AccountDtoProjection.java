package com.example.rest.dto.dtoProjection;

import java.math.BigDecimal;

public interface AccountDtoProjection {

    //TODO need to search annotation for fields

    Integer getId();

    String getFirst_name();

    String getLast_name();

    String getPrintable_name();

    String getPhone();

    BigDecimal getOpening_balance();

}
