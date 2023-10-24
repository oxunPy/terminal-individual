package com.example.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRateDto {
    private Long id;

    private LocalDateTime invDate;

    private String mainCurrency;

    private BigDecimal mainCurrencyVal;

    private String toCurrency;

    private BigDecimal toCurrencyVal;
}
