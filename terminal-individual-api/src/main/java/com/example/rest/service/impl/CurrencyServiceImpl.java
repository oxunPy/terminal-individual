package com.example.rest.service.impl;

import com.example.rest.dto.ExchangeRateDto;
import com.example.rest.dto.dtoProjection.ExchangeRateDtoProjection;
import com.example.rest.repository.CurrencyRepository;
import com.example.rest.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;

    @Override
    public ExchangeRateDto getLastExchangeRate() {
        Optional<ExchangeRateDtoProjection> lastExchangeRate = currencyRepository.getLastExchangeRate();
        ExchangeRateDto dto = new ExchangeRateDto();
        dto.setToCurrencyVal(BigDecimal.valueOf(1));
        dto.setMainCurrencyVal(BigDecimal.valueOf(1));
        if (lastExchangeRate.isPresent()) {
            dto.setId(lastExchangeRate.get().getId());
            dto.setInvDate(lastExchangeRate.get().getInv_date());
            dto.setMainCurrency(lastExchangeRate.get().getMain_currency());
            dto.setToCurrency(lastExchangeRate.get().getTo_currency());
            dto.setMainCurrencyVal(lastExchangeRate.get().getMain_currency_val());
            dto.setToCurrencyVal(lastExchangeRate.get().getTo_currency_val());
        }
        return dto;
    }
}
