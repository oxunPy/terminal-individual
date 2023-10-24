package com.example.rest.telegramBot.commands;

import com.example.rest.common.BotState;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

import static com.example.rest.telegramBot.commands.constants.core.Commands.*;


public class CommandMarkups {
    public static ReplyKeyboardMarkup getAdditionalFunctionsMenuKeyBoard() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);
        markup.setOneTimeKeyboard(true);
        markup.setSelective(true);

        List<KeyboardRow> rows = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();

        KeyboardButton paymentCashButton = new KeyboardButton();
        paymentCashButton.setText(GET_PAYMENT_CASH);
        row1.add(paymentCashButton);

        KeyboardButton paymentBankButton = new KeyboardButton();
        paymentBankButton.setText(GET_PAYMENT_BANK);
        row1.add(paymentBankButton);

        KeyboardButton receiptCashButton = new KeyboardButton();
        receiptCashButton.setText(GET_RECEIPT_CASH);
        row1.add(receiptCashButton);

        KeyboardButton receiptBankButton = new KeyboardButton();
        receiptBankButton.setText(GET_RECEIPT_BANK);
        row2.add(receiptBankButton);

        KeyboardButton returnedAmountButton = new KeyboardButton();
        returnedAmountButton.setText(GET_TOTAL_RETURNED_AMOUNT_FROM_CLIENT);
        row2.add(returnedAmountButton);

        KeyboardButton totalBalanceButton = new KeyboardButton();
        totalBalanceButton.setText(GET_TOTAL_BALANCE_CLIENT);
        row2.add(totalBalanceButton);

        rows.add(row1);
        rows.add(row2);
        rows.add(row3);

        markup.setKeyboard(rows);
        return markup;
    }

    public static ReplyKeyboardMarkup getShareContactKeyBoard() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);
        markup.setOneTimeKeyboard(false);
        markup.setSelective(true);
        List<KeyboardRow> rows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        KeyboardButton buttonAskContact = new KeyboardButton();
        buttonAskContact.setRequestContact(true);
        buttonAskContact.setText("Пожалуйста, поделитесь контактом");
        row.add(buttonAskContact);
        rows.add(row);
        markup.setKeyboard(rows);
        return markup;
    }

    public static ReplyKeyboardMarkup getWholeOperationKeyboard() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setOneTimeKeyboard(true);
        markup.setSelective(true);
        markup.setResizeKeyboard(true);

        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow firstRow = new KeyboardRow();

        KeyboardButton wholeOperationButton = new KeyboardButton();
        wholeOperationButton.setText(WHOLE_OPERATION);

        ReplyKeyboardMarkup additionalFunctionsMarkup = getAdditionalFunctionsMenuKeyBoard();

        firstRow.add(wholeOperationButton);
        rows.add(firstRow);
        rows.addAll(additionalFunctionsMarkup.getKeyboard());
        markup.setKeyboard(rows);
        return markup;
    }

    public static ReplyKeyboardMarkup getCurrencyKeyboardMarkup() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setOneTimeKeyboard(true);
        markup.setSelective(true);
        markup.setResizeKeyboard(true);

        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();

        KeyboardButton uzsButton = new KeyboardButton();
        uzsButton.setText(UZBEK_CURRENCY);

        KeyboardButton usdButton = new KeyboardButton();
        usdButton.setText(AMERICAN_CURRENCY);

        KeyboardButton backButton = new KeyboardButton();
        backButton.setText(BACK);

        row1.add(uzsButton);
        row1.add(usdButton);
        row2.add(backButton);
        rows.add(row1);
        rows.add(row2);

        markup.setKeyboard(rows);
        return markup;
    }

    public static ReplyKeyboardMarkup getDateMarkup() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setOneTimeKeyboard(true);
        markup.setSelective(true);
        markup.setResizeKeyboard(true);

        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        KeyboardRow head = new KeyboardRow();

        KeyboardButton searchByNameProductBtn = new KeyboardButton();
        searchByNameProductBtn.setText("Поиск по продукту❓");
        head.add(searchByNameProductBtn);
        rows.add(head);

        KeyboardButton todayOperationButton = new KeyboardButton();
        todayOperationButton.setText(TODAY_OPERATION);

        KeyboardButton thisWeekendButton = new KeyboardButton();
        thisWeekendButton.setText(THIS_WEEKEND);

        KeyboardButton thisMonthButton = new KeyboardButton();
        thisMonthButton.setText(THIS_MONTH);

        KeyboardButton otherDate = new KeyboardButton();
        otherDate.setText(OTHER_DATE);

        KeyboardButton backButton = new KeyboardButton();
        backButton.setText(BACK);

        row1.add(todayOperationButton);
        row1.add(thisWeekendButton);
        row2.add(thisMonthButton);
        row2.add(otherDate);
        row3.add(backButton);

        rows.add(row1);
        rows.add(row2);
        rows.add(row3);
        markup.setKeyboard(rows);
        return markup;
    }
    public static ReplyKeyboardMarkup getCalendarMarkup(){
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setSelective(true);
        markup.setResizeKeyboard(true);
        markup.setOneTimeKeyboard(true);

        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        KeyboardButton backBtn = new KeyboardButton(BACK);
        row.add(backBtn);
        rows.add(row);
        markup.setKeyboard(rows);
        return markup;
    }

    public static ReplyKeyboardMarkup getUpdateMarkup(){
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setSelective(true);
        markup.setResizeKeyboard(true);
        markup.setOneTimeKeyboard(true);

        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        KeyboardButton updateBtn = new KeyboardButton(UPDATE);
        row.add(updateBtn);
        rows.add(row);
        markup.setKeyboard(rows);
        return markup;
    }

    public static ReplyKeyboardMarkup getMenuMarkupBy(BotState botState){
        switch(botState){
            case START_STATE:
                return getWholeOperationKeyboard();
            case CURRENCY_STATE:
                return getCurrencyKeyboardMarkup();
            case DATE_STATE:
                return getDateMarkup();
            case CALENDAR_STATE:
                return getCalendarMarkup();
        }
        return getWholeOperationKeyboard();
    }
}
