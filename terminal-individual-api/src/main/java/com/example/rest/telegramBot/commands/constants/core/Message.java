package com.example.rest.telegramBot.commands.constants.core;

import java.util.HashMap;

public abstract class Message<T> {
    protected HashMap<T, String> mapMessages;
    protected abstract void initTextMap();

    public String getMessage(T command) {
        return mapMessages.getOrDefault(command, null);
    }
}