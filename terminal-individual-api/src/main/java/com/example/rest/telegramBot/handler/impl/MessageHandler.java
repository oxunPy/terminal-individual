package com.example.rest.telegramBot.handler.impl;

import com.example.rest.telegramBot.commands.GeneralCommandHandler;
import com.example.rest.telegramBot.handler.Handler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@RequiredArgsConstructor
public class MessageHandler implements Handler<Message> {

    private final GeneralCommandHandler generalCommandHandler;
    private final ContactHandler contactHandler;

    public void handleMessage(Message message) throws TelegramApiException {
        if(message.hasText()){
            generalCommandHandler.executeCommand(message, message.getText());
        }
        if(message.hasContact()){
            contactHandler.handleMessage(message);
        }
    }
}
