package com.example.rest.data_interface;

import com.example.rest.common.BotState;
import com.example.rest.common.Currency;
import com.example.rest.constant.Status;

public interface UserInterface {
    public Long getId();

    public Status getStatus();

    public BotState getBotState();

    public Long getChatId();

    public String getFirstName();
    public String getLastName();

    public String getUserName();

    public String getCommand();

    public String getContact();

    public Currency getCurrency();
}
