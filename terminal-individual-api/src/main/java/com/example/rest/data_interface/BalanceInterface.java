package com.example.rest.data_interface;

import java.math.BigDecimal;

public interface BalanceInterface {
    BigDecimal getDebit();

    BigDecimal getCredit();
}
