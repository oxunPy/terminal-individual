package com.example.rest.telegramBot.commands.constants;

import com.example.rest.telegramBot.commands.constants.core.Message;
import org.springframework.stereotype.Component;

import java.util.HashMap;

import static com.example.rest.telegramBot.commands.constants.core.TextCode.*;

@Component
public class TextCodeMessages extends Message<Integer> {
    private TextCodeMessages() {
        mapMessages = new HashMap<>();
        initTextMap();
    }

    @Override
    protected void initTextMap() {
        mapMessages.put(ERROR_CREATING_USER, "Произошла ошибка при создании пользователя\uD83D\uDE14❗\uFE0F");
        mapMessages.put(SUCCESS_CREATING_USER, "Пользователь успешно создан✔\uFE0F");
        mapMessages.put(SEND_TO_ADMIN_REQUEST, "Отправьте запрос администратору на разрешение");
        mapMessages.put(INTERNAL_SERVER_ERROR, "Внутренняя ошибка сервера❗\uFE0F");
        mapMessages.put(SELECT_OTHER_DATE, "Выберите другую дату");
        mapMessages.put(OTHER_DATE, "Другая дата");
        mapMessages.put(UZS, "UZS\uD83C\uDDFA\uD83C\uDDFF");
        mapMessages.put(USD, "USD\uD83C\uDDFA\uD83C\uDDF8");
        mapMessages.put(SUCCESS_CREATED_USER_WITH_CONTACT, "Пользователь успешно создан.\nВаш аккаунт неактивен, обратитесь к администратору!");
    }
}
