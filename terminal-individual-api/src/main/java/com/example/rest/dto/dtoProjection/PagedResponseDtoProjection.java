package com.example.rest.dto.dtoProjection;

import java.math.BigDecimal;

public interface PagedResponseDtoProjection {

    Long getCount();

    BigDecimal getSum();

}
