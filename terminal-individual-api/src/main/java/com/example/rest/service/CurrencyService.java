package com.example.rest.service;

import com.example.rest.dto.ExchangeRateDto;

public interface CurrencyService {

    ExchangeRateDto getLastExchangeRate();

}
