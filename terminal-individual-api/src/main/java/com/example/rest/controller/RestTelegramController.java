package com.example.rest.controller;

import com.example.rest.model.BotUserModel;
import com.example.rest.common.DateRangeAndCurrency;
import com.example.rest.model.ClientBalance;
import com.example.rest.service.impl.RestTelegramServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/bot/api")
@RequiredArgsConstructor
public class RestTelegramController{

    private final RestTelegramServiceImpl restTelegramService;

    @Operation(hidden = true)
    @PostMapping("/save-bot-user")
    public BotUserModel saveBotUser(@RequestBody BotUserModel bum){
        return restTelegramService.saveOrEditBotUser(bum);
    }

    @PostMapping("/get-receipt-cash")
    @Operation(hidden = true)
    public BigDecimal getReceiptCash(@RequestBody DateRangeAndCurrency dateRangeAndCurrency){
        return restTelegramService.getReceiptCash(dateRangeAndCurrency.getCurrency(), dateRangeAndCurrency.getFromDate(), dateRangeAndCurrency.getToDate());
    }
    @PostMapping("/get-receipt-bank")
    @Operation(hidden = true)
    public BigDecimal getReceiptBank(@RequestBody DateRangeAndCurrency dateRangeAndCurrency){
        return restTelegramService.getReceiptBank(dateRangeAndCurrency.getCurrency(), dateRangeAndCurrency.getFromDate(), dateRangeAndCurrency.getToDate());
    }
    @PostMapping("/get-payment-cash")
    @Operation(hidden = true)
    public BigDecimal getPaymentCash(@RequestBody DateRangeAndCurrency dateRangeAndCurrency){
        return restTelegramService.getPaymentCash(dateRangeAndCurrency.getCurrency(), dateRangeAndCurrency.getFromDate(), dateRangeAndCurrency.getToDate());
    }
    @PostMapping("/get-payment-bank")
    @Operation(hidden = true)
    public BigDecimal getPaymentBank(@RequestBody DateRangeAndCurrency dateRangeAndCurrency){
        return restTelegramService.getPaymentBank(dateRangeAndCurrency.getCurrency(), dateRangeAndCurrency.getFromDate(), dateRangeAndCurrency.getToDate());
    }
    @PostMapping("/get-total-returned-amount-from-client")
    @Operation(hidden = true)
    public BigDecimal getTotalReturnedAmount(@RequestBody DateRangeAndCurrency dateRangeAndCurrency){
        return restTelegramService.getTotalReturnedAmountFromClient(dateRangeAndCurrency.getCurrency(), dateRangeAndCurrency.getFromDate(), dateRangeAndCurrency.getToDate());
    }
    @Operation(hidden = true)
    @PostMapping("/get-total-balance-client")
    public ClientBalance getTotalBalance(@RequestBody DateRangeAndCurrency dateRangeAndCurrency){
        return restTelegramService.getTotalBalanceClient(dateRangeAndCurrency.getCurrency(), dateRangeAndCurrency.getToDate());
    }
    @Operation(hidden = true)
    @GetMapping("/user-exists/{chatId}")
    public boolean userExists(@PathVariable("chatId") Long chatId){
        return restTelegramService.userExists(chatId);
    }
    @Operation(hidden = true)
    @GetMapping("/get-user/{chatId}")
    public ResponseEntity<BotUserModel> getBotUser(@PathVariable("chatId") Long chatId){
        return ResponseEntity.ok().body(restTelegramService.getBotUser(chatId)) ;
    }
    @Operation(hidden = true)
    @GetMapping("/activate/{chatId}")
    public boolean activateBotUser(@PathVariable("chatId") Long chatId){
        return restTelegramService.activateUser(chatId);
    }
}
