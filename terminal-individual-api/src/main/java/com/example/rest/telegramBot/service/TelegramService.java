package com.example.rest.telegramBot.service;

import com.example.rest.telegramBot.botConfig.MyTelegramPollingBot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@RequiredArgsConstructor
@Service
public class TelegramService {

    private final MyTelegramPollingBot telegramLongPollingBot;

    public void executeMessage(SendMessage message) throws TelegramApiException {
        telegramLongPollingBot.execute(message);
    }

    public void executeDocument(SendDocument document) throws TelegramApiException {
        telegramLongPollingBot.execute(document);
    }

    public void executePhoto(SendPhoto photo) throws TelegramApiException {
        telegramLongPollingBot.execute(photo);
    }

    public void executeEditMessageReplyMarkup(EditMessageReplyMarkup editMarkup) throws TelegramApiException {
        telegramLongPollingBot.execute(editMarkup);
    }
}
