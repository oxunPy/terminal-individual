package com.example.rest.service;
import com.example.rest.common.Currency;
import com.example.rest.model.BotUserModel;
import com.example.rest.model.ClientBalance;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface RestTelegramService {
    BigDecimal getReceiptCash(Currency currency, LocalDate fromDate, LocalDate toDate);

    BigDecimal getReceiptBank(Currency currency, LocalDate fromDate, LocalDate toDate);

    BigDecimal getPaymentCash(Currency currency, LocalDate fromDate, LocalDate toDate);

    BigDecimal getPaymentBank(Currency currency, LocalDate fromDate, LocalDate toDate);

    BigDecimal getTotalReturnedAmountFromClient(Currency currency, LocalDate fromDate, LocalDate toDate);

    ClientBalance getTotalBalanceClient(Currency currency, LocalDate toDate);

    BotUserModel saveOrEditBotUser(BotUserModel bum);

    boolean userExists(Long chatId);

    BotUserModel getBotUser(Long chatId);

    boolean activateUser(Long chatId);
}
