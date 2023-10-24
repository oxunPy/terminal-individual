package com.example.rest.utils;

import com.example.rest.constant.CurrencyCode;
import com.example.rest.dto.ExchangeRateDto;

import java.math.BigDecimal;

public class CurrencyConverter {

    public static BigDecimal getRateUZS(ExchangeRateDto exchangeRateDto, BigDecimal productRate){
        if(!exchangeRateDto.getMainCurrency().equals(CurrencyCode.UZS.name())){
            BigDecimal uzsRate = productRate.multiply(exchangeRateDto.getToCurrencyVal())
                                            .divide(exchangeRateDto.getMainCurrencyVal());
            return uzsRate;
        }
        return productRate;
    }

}
