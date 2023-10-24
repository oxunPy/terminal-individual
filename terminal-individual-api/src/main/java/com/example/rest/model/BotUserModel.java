package com.example.rest.model;

import com.example.rest.common.BotState;
import com.example.rest.common.Currency;
import com.example.rest.constant.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BotUserModel {
    public BotUserModel(Long chatId, String userName, String firstName, String lastName) {
        this.chatId = chatId;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    private Long id;
    private Long chatId;
    private String userName;
    private String firstName;
    private String lastName;

    private String contact;

    private Status status;

    private String command;

    private BotState botState;

    private Currency currency;

    private boolean synced = false;

    private Date createdDate;

    private Date updatedDate;
}
