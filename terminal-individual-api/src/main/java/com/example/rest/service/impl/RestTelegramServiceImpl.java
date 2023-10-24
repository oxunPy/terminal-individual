package com.example.rest.service.impl;

import com.example.rest.common.Currency;
import com.example.rest.data_interface.BalanceInterface;
import com.example.rest.data_interface.UserInterface;
import com.example.rest.model.BotUserModel;
import com.example.rest.model.ClientBalance;
import com.example.rest.repository.RestTelegramRepository;
import com.example.rest.service.RestTelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestTelegramServiceImpl implements RestTelegramService {

    @Autowired
    private final RestTelegramRepository restTemplateRepository;

    @Transactional(readOnly = true)
    public BigDecimal getReceiptCash(Currency currency, LocalDate fromDate, LocalDate toDate) {
        if (currency == null) {
            throw new NullPointerException("currency is null");
        }

        if (fromDate != null && toDate != null) {
            Optional<BigDecimal> result = restTemplateRepository.getReceiptCash(currency.ordinal() + 1, fromDate, toDate);
            if (result.isPresent()) return formatBigDecimal(result.get(), currency.ordinal() == 0 ? 3 : 0);
        }
        return BigDecimal.ZERO;
    }

    @Transactional(readOnly = true)
    public BigDecimal getReceiptBank(Currency currency, LocalDate fromDate, LocalDate toDate) {
        if (currency == null) {
            throw new NullPointerException("currency is null");
        }
        if (fromDate != null && toDate != null) {
            Optional<BigDecimal> result = restTemplateRepository.getReceiptBank(currency.ordinal() + 1, fromDate, toDate);
            if (result.isPresent()) return formatBigDecimal(result.get(), currency.ordinal() == 0 ? 3 : 0);
        }
        return BigDecimal.ZERO;
    }

    @Transactional(readOnly = true)
    public BigDecimal getPaymentCash(Currency currency, LocalDate fromDate, LocalDate toDate) {
        if (currency == null) {
            throw new NullPointerException("currency is null");
        }
        if (fromDate != null && toDate != null) {
            Optional<BigDecimal> result = restTemplateRepository.getPaymentCash(currency.ordinal() + 1, fromDate, toDate);
            if (result.isPresent()) return formatBigDecimal(result.get(), currency.ordinal() == 0 ? 3 : 0);
        }
        return BigDecimal.ZERO;
    }

    @Transactional(readOnly = true)
    public BigDecimal getPaymentBank(Currency currency, LocalDate fromDate, LocalDate toDate) {
        if (currency == null) {
            throw new NullPointerException("currency is null");
        }
        if (fromDate != null && toDate != null) {
            Optional<BigDecimal> result = restTemplateRepository.getPaymentBank(currency.ordinal() + 1, fromDate, toDate);
            if (result.isPresent()) return formatBigDecimal(result.get(), currency.ordinal() == 0 ? 3 : 0);
        }
        return BigDecimal.ZERO;
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalReturnedAmountFromClient(Currency currency, LocalDate fromDate, LocalDate toDate) {
        if (currency == null) {
            throw new NullPointerException("currency is null");
        }
        if (fromDate != null && toDate != null) {
            Optional<BigDecimal> result = restTemplateRepository.getTotalReturnedAmountFromClient(currency.ordinal() + 1, fromDate, toDate);
            if (result.isPresent()) return formatBigDecimal(result.get(), currency.ordinal() == 0 ? 3 : 0);
        }
        return BigDecimal.ZERO;
    }

    @Transactional(readOnly = true)
    public ClientBalance getTotalBalanceClient(Currency currency, LocalDate toDate) {
        if (currency == null) {
            throw new NullPointerException("currency is null");
        }
        ClientBalance clientBalance = new ClientBalance(BigDecimal.valueOf(0.00), BigDecimal.valueOf(0.00));
        if (toDate != null) {
            Optional<BalanceInterface> result = restTemplateRepository.getTotalBalanceClient(currency.ordinal() + 1, toDate);
            if (result.isPresent()) {
                clientBalance.setCredit(result.get().getCredit() != null ? formatBigDecimal(result.get().getCredit(), currency.ordinal() == 0 ? 3 : 0)
                        : formatBigDecimal(BigDecimal.valueOf(0.00), currency.ordinal() == 0 ? 3 : 0));
                clientBalance.setDebit(result.get().getDebit() != null ? formatBigDecimal(result.get().getDebit(), currency.ordinal() == 0 ? 3 : 0)
                        : formatBigDecimal(BigDecimal.valueOf(0.00), currency.ordinal() == 0 ? 3 : 0));
            }
        }
        return clientBalance;
    }

    @Transactional
    public BotUserModel saveOrEditBotUser(BotUserModel bum) {
        restTemplateRepository.save(bum.getFirstName(), bum.getLastName(), bum.getCommand(), bum.getChatId(), String.valueOf(bum.getBotState()), bum.getContact(), String.valueOf(bum.getCurrency()));
        return bum;
    }

    @Override
    public boolean userExists(Long chatId) {
        return restTemplateRepository.existsUserByChatId(chatId);
    }

    @Override
    public BotUserModel getBotUser(Long chatId) {
        Optional<UserInterface> optUserInterface = restTemplateRepository.getBotUserByChatId(chatId);
        if(!optUserInterface.isPresent()) return null;
        return BotUserModel.builder()
                .id(optUserInterface.get().getId())
                .chatId(optUserInterface.get().getChatId())
                .botState(optUserInterface.get().getBotState())
                .firstName(optUserInterface.get().getFirstName())
                .lastName(optUserInterface.get().getLastName())
                .userName(optUserInterface.get().getUserName())
                .contact(optUserInterface.get().getContact())
                .command(optUserInterface.get().getCommand())
                .currency(optUserInterface.get().getCurrency())
                .status(optUserInterface.get().getStatus())
                .build();
    }
    @Transactional
    public boolean activateUser(Long chatId){
        return restTemplateRepository.activateUser(chatId) > 0;
    }

    private double withBigDecimal(double value, int places) {
        BigDecimal bigDecimal = new BigDecimal(value);
        bigDecimal = bigDecimal.setScale(places, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    private BigDecimal formatBigDecimal(BigDecimal value, int places) {
        value = value.setScale(places, RoundingMode.HALF_UP);
        return value;
    }


}
