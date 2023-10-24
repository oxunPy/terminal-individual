package com.example.rest.telegramBot.handler.impl;

import com.example.rest.common.BotState;
import com.example.rest.common.Currency;
import com.example.rest.constant.Status;
import com.example.rest.model.BotUserModel;
import com.example.rest.service.RestTelegramService;
import com.example.rest.telegramBot.commands.CommandMarkups;
import com.example.rest.telegramBot.commands.constants.TextCodeMessages;
import com.example.rest.telegramBot.commands.constants.core.TextCode;
import com.example.rest.telegramBot.handler.Handler;
import com.example.rest.telegramBot.service.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ContactHandler implements Handler<Message> {
    private final TelegramService telegramService;
    private final RestTelegramService restTelegramService;

    @Resource
    private TextCodeMessages textCodeMessages;

    @Override
    public void handleMessage(Message message) throws TelegramApiException {
        BotUserModel botUserModel = BotUserModel.builder()
                .contact(message.getContact().getPhoneNumber())
                .userName(message.getFrom().getUserName())
                .firstName(message.getFrom().getFirstName())
                .lastName(message.getFrom().getLastName())
                .chatId(message.getChatId())
                .botState(BotState.START_STATE)
                .createdDate(Date.from(Instant.now()))
                .status(Status.PASSIVE)
                .currency(Currency.USD)
                .synced(false)
                .build();

        BotUserModel savedBotUser = restTelegramService.saveOrEditBotUser(botUserModel);

        telegramService.executeMessage(SendMessage.builder()
                .text(textCodeMessages.getMessage(TextCode.SUCCESS_CREATED_USER_WITH_CONTACT))
                .chatId(message.getChatId().toString())
                .replyMarkup(CommandMarkups.getUpdateMarkup())
                .build());
    }

}

