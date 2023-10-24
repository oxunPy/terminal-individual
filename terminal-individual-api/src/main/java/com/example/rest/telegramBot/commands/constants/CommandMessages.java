package com.example.rest.telegramBot.commands.constants;

import com.example.rest.telegramBot.commands.constants.core.Message;
import org.springframework.stereotype.Component;

import java.util.HashMap;

import static com.example.rest.telegramBot.commands.constants.core.Commands.*;

@Component
public class CommandMessages extends Message<String> {
    private CommandMessages() {
        mapMessages = new HashMap<>();
        initTextMap();
    }

    public void initTextMap() {
        mapMessages.put(UPDATE, "Пользователь создан с активным статусом!!!");
        mapMessages.put(START, "Пожалуйста, поделитесь контактом");
        mapMessages.put(GET_PAYMENT_CASH, "Оплата наличными, выберите валюту");
        mapMessages.put(GET_PAYMENT_BANK, "Оплата безналичными, выберите валюту");
        mapMessages.put(GET_RECEIPT_CASH, "Получить наличные, выберите валюту");
        mapMessages.put(GET_RECEIPT_BANK, "Получить безналичные, выберите валюту");
        mapMessages.put(GET_TOTAL_BALANCE_CLIENT, "Клиент общего баланса, выберите валюту");
        mapMessages.put(GET_TOTAL_RETURNED_AMOUNT_FROM_CLIENT, "Сумма возвратных товаров, выберите валюту");
        mapMessages.put(WHOLE_OPERATION, "Все операция\uD83D\uDDD3");
    }
}
