package com.example.rest.telegramBot.handler.impl;

import com.example.rest.common.Currency;
import com.example.rest.model.BotUserModel;
import com.example.rest.service.RestTelegramService;
import com.example.rest.telegramBot.calendar.CalendarMarkup;
import com.example.rest.telegramBot.commands.CommandMarkups;
import com.example.rest.telegramBot.commands.constants.core.Commands;
import com.example.rest.telegramBot.commands.operation.Operations;
import com.example.rest.telegramBot.handler.Handler;
import com.example.rest.telegramBot.service.TelegramService;
import com.example.rest.telegramBot.utils.CalendarMarkupUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.time.Month;

@Component
@RequiredArgsConstructor
public class CallBackQueryHandler implements Handler<CallbackQuery> {
    private final TelegramService telegramService;
    private final Operations operations;
    private final RestTelegramService restTelegramService;
    @Override
    public void handleMessage(CallbackQuery callbackQuery) throws TelegramApiException {

        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
        editMessageReplyMarkup.setChatId(callbackQuery.getMessage().getChatId().toString());
        editMessageReplyMarkup.setMessageId(callbackQuery.getMessage().getMessageId());
        editMessageReplyMarkup.setInlineMessageId(callbackQuery.getInlineMessageId());
        CalendarMarkupUtils calendarUtils = new CalendarMarkupUtils();
        BotUserModel bum = restTelegramService.getBotUser(callbackQuery.getMessage().getChatId());

        if (callbackQuery.getData().equals(CalendarMarkup.SUBMIT_DATE)) {

            LocalDate fromDate = calendarUtils.getFromDateFromCalendar(callbackQuery.getMessage().getReplyMarkup());
            LocalDate toDate = calendarUtils.getToDateFromCalendar(callbackQuery.getMessage().getReplyMarkup());
            String command = bum.getCommand();
            Currency currency = bum.getCurrency();

            if(command == null){
                telegramService.executeMessage(SendMessage.builder()
                        .text("Servis tanlanmadi!!!")
                        .chatId(callbackQuery.getMessage().getChatId().toString())
                        .replyMarkup(CommandMarkups.getWholeOperationKeyboard())
                        .build());
                return;
            }

            if (command.equals(Commands.WHOLE_OPERATION)) {
                telegramService.executeMessage(SendMessage.builder()
                        .text(operations.getWholePeriodOperations(currency, fromDate, toDate))
                        .chatId(callbackQuery.getMessage().getChatId().toString())
                        .replyMarkup(CommandMarkups.getDateMarkup())
                        .build());
            } else {
                telegramService.executeMessage(SendMessage.builder()
                        .text(operations.getPeriodOperation(currency, fromDate, toDate, command))
                        .chatId(callbackQuery.getMessage().getChatId().toString())
                        .replyMarkup(CommandMarkups.getDateMarkup())
                        .build());
            }

        } else if (callbackQuery.getData().equals(CalendarMarkupUtils.FROM_DATE + CalendarMarkup.PREV_YEAR)) {

            InlineKeyboardMarkup dateToDateCalendar = new CalendarMarkupUtils().cloneDateToDateCalendar(callbackQuery.getMessage().getReplyMarkup());
            editMessageReplyMarkup.setReplyMarkup(dateToDateCalendar);
            InlineKeyboardButton currentYearButton = calendarUtils.getButtonByCallbackQuery(dateToDateCalendar.getKeyboard(),
                    CalendarMarkupUtils.FROM_DATE + CalendarMarkup.CURRENT_YEAR);
            currentYearButton.setText(String.valueOf(Integer.parseInt(currentYearButton.getText()) - 1));
            telegramService.executeEditMessageReplyMarkup(editMessageReplyMarkup);

        } else if (callbackQuery.getData().equals(CalendarMarkupUtils.FROM_DATE + CalendarMarkup.NEXT_YEAR)) {

            InlineKeyboardMarkup dateToDateCalendar = calendarUtils.cloneDateToDateCalendar(callbackQuery.getMessage().getReplyMarkup());
            editMessageReplyMarkup.setReplyMarkup(dateToDateCalendar);
            InlineKeyboardButton currentYearButton = new CalendarMarkupUtils().getButtonByCallbackQuery(dateToDateCalendar.getKeyboard(),
                    CalendarMarkupUtils.FROM_DATE + CalendarMarkup.CURRENT_YEAR);
            currentYearButton.setText(String.valueOf(Integer.parseInt(currentYearButton.getText()) + 1));
            telegramService.executeEditMessageReplyMarkup(editMessageReplyMarkup);

        } else if (callbackQuery.getData().equals(CalendarMarkupUtils.FROM_DATE + CalendarMarkup.PREV_MONTH)) {

            InlineKeyboardMarkup dateToDateCalendar = calendarUtils.cloneDateToDateCalendar(callbackQuery.getMessage().getReplyMarkup());
            editMessageReplyMarkup.setReplyMarkup(dateToDateCalendar);
            InlineKeyboardButton currentMonthButton = calendarUtils.getButtonByCallbackQuery(dateToDateCalendar.getKeyboard(),
                    CalendarMarkupUtils.FROM_DATE + CalendarMarkup.CURRENT_MONTH);

            int monthVal = Month.valueOf(currentMonthButton.getText()).getValue();
            int year = Integer.parseInt(calendarUtils.getButtonByCallbackQuery(dateToDateCalendar.getKeyboard(), CalendarMarkupUtils.FROM_DATE + CalendarMarkup.CURRENT_YEAR).getText());
            calendarUtils.updateDaysFromDate(dateToDateCalendar, year, monthVal == 1 ? 12 : monthVal - 1);

            currentMonthButton.setText(String.valueOf(monthVal == 1 ? Month.DECEMBER : Month.of(monthVal - 1)));
            telegramService.executeEditMessageReplyMarkup(editMessageReplyMarkup);

        } else if (callbackQuery.getData().equals(CalendarMarkupUtils.FROM_DATE + CalendarMarkup.NEXT_MONTH)) {

            InlineKeyboardMarkup dateToDateCalendar = calendarUtils.cloneDateToDateCalendar(callbackQuery.getMessage().getReplyMarkup());
            editMessageReplyMarkup.setReplyMarkup(dateToDateCalendar);
            InlineKeyboardButton currentMonthButton = calendarUtils.getButtonByCallbackQuery(dateToDateCalendar.getKeyboard(),
                    CalendarMarkupUtils.FROM_DATE + CalendarMarkup.CURRENT_MONTH);

            int monthVal = Month.valueOf(currentMonthButton.getText()).getValue();
            int year = Integer.parseInt(calendarUtils.getButtonByCallbackQuery(dateToDateCalendar.getKeyboard(), CalendarMarkupUtils.FROM_DATE + CalendarMarkup.CURRENT_YEAR).getText());
            calendarUtils.updateDaysFromDate(dateToDateCalendar, year, monthVal == 12 ? 1 : monthVal + 1);

            currentMonthButton.setText(String.valueOf(monthVal == 12 ? Month.JANUARY : Month.of(monthVal + 1)));
            telegramService.executeEditMessageReplyMarkup(editMessageReplyMarkup);

        } else if (callbackQuery.getData().equals(CalendarMarkupUtils.TO_DATE + CalendarMarkup.PREV_YEAR)) {

            InlineKeyboardMarkup dateToDateCalendar = calendarUtils.cloneDateToDateCalendar(callbackQuery.getMessage().getReplyMarkup());
            editMessageReplyMarkup.setReplyMarkup(dateToDateCalendar);
            InlineKeyboardButton currentYearButton = calendarUtils.getButtonByCallbackQuery(dateToDateCalendar.getKeyboard(),
                    CalendarMarkupUtils.TO_DATE + CalendarMarkup.CURRENT_YEAR);
            currentYearButton.setText(String.valueOf(Integer.parseInt(currentYearButton.getText()) - 1));
            telegramService.executeEditMessageReplyMarkup(editMessageReplyMarkup);

        } else if (callbackQuery.getData().equals(CalendarMarkupUtils.TO_DATE + CalendarMarkup.NEXT_YEAR)) {

            InlineKeyboardMarkup dateToDateCalendar = calendarUtils.cloneDateToDateCalendar(callbackQuery.getMessage().getReplyMarkup());
            editMessageReplyMarkup.setReplyMarkup(dateToDateCalendar);
            InlineKeyboardButton currentYearButton = calendarUtils.getButtonByCallbackQuery(dateToDateCalendar.getKeyboard(),
                    CalendarMarkupUtils.TO_DATE + CalendarMarkup.CURRENT_YEAR);
            currentYearButton.setText(String.valueOf(Integer.parseInt(currentYearButton.getText()) + 1));
            telegramService.executeEditMessageReplyMarkup(editMessageReplyMarkup);

        } else if (callbackQuery.getData().equals(CalendarMarkupUtils.TO_DATE + CalendarMarkup.PREV_MONTH)) {

            InlineKeyboardMarkup dateToDateCalendar = calendarUtils.cloneDateToDateCalendar(callbackQuery.getMessage().getReplyMarkup());
            editMessageReplyMarkup.setReplyMarkup(dateToDateCalendar);
            InlineKeyboardButton currentMonthButton = calendarUtils.getButtonByCallbackQuery(dateToDateCalendar.getKeyboard(),
                    CalendarMarkupUtils.TO_DATE + CalendarMarkup.CURRENT_MONTH);

            int monthVal = Month.valueOf(currentMonthButton.getText()).getValue();
            InlineKeyboardButton yearButton = calendarUtils.getButtonByCallbackQuery(dateToDateCalendar.getKeyboard(), CalendarMarkupUtils.TO_DATE + CalendarMarkup.CURRENT_YEAR);
            int year = yearButton == null ? LocalDate.now().getYear() : Integer.parseInt(yearButton.getText());
            calendarUtils.updateDaysToDate(dateToDateCalendar, year, monthVal == 1 ? 12 : monthVal - 1);

            currentMonthButton.setText(String.valueOf(monthVal == 1 ? Month.DECEMBER : Month.of(monthVal - 1)));
            telegramService.executeEditMessageReplyMarkup(editMessageReplyMarkup);

        } else if (callbackQuery.getData().equals(CalendarMarkupUtils.TO_DATE + CalendarMarkup.NEXT_MONTH)) {

            InlineKeyboardMarkup dateToDateCalendar = calendarUtils.cloneDateToDateCalendar(callbackQuery.getMessage().getReplyMarkup());
            editMessageReplyMarkup.setReplyMarkup(dateToDateCalendar);
            InlineKeyboardButton currentMonthButton = calendarUtils.getButtonByCallbackQuery(dateToDateCalendar.getKeyboard(),
                    CalendarMarkupUtils.TO_DATE + CalendarMarkup.CURRENT_MONTH);

            int monthVal = Month.valueOf(currentMonthButton.getText()).getValue();
            InlineKeyboardButton yearButton = calendarUtils.getButtonByCallbackQuery(dateToDateCalendar.getKeyboard(), CalendarMarkupUtils.TO_DATE + CalendarMarkup.CURRENT_YEAR);
            int year = yearButton == null ? LocalDate.now().getYear() : Integer.parseInt(yearButton.getText());
            calendarUtils.updateDaysToDate(dateToDateCalendar, year, monthVal == 12 ? 1 : monthVal + 1);

            currentMonthButton.setText(String.valueOf(monthVal == 12 ? Month.JANUARY : Month.of(monthVal + 1)));
            telegramService.executeEditMessageReplyMarkup(editMessageReplyMarkup);
        }
        //pressed from date day
        else if (callbackQuery.getData().startsWith(CalendarMarkupUtils.FROM_DATE) && calendarUtils.hasMonthDayNumber(callbackQuery.getData())) {

            InlineKeyboardMarkup dateToDateCalendar = calendarUtils.cloneDateToDateCalendar(callbackQuery.getMessage().getReplyMarkup());
            editMessageReplyMarkup.setReplyMarkup(dateToDateCalendar);

            InlineKeyboardButton oldActiveButton = calendarUtils.getActiveDay(dateToDateCalendar, CalendarMarkup.ACTIVE_FROM_DATE_SIGN);
            if (oldActiveButton != null) oldActiveButton.setText(oldActiveButton.getText().split(" ")[0]);

            InlineKeyboardButton activeDay = calendarUtils.getButtonByCallbackQuery(dateToDateCalendar.getKeyboard(), callbackQuery.getData());
            activeDay.setText(activeDay.getText() + " " + CalendarMarkup.ACTIVE_FROM_DATE_SIGN);

            telegramService.executeEditMessageReplyMarkup(editMessageReplyMarkup);
        }
        // pressed to date day
        else if (callbackQuery.getData().startsWith(CalendarMarkupUtils.TO_DATE) && calendarUtils.hasMonthDayNumber(callbackQuery.getData())) {
            InlineKeyboardMarkup dateToDateCalendar = calendarUtils.cloneDateToDateCalendar(callbackQuery.getMessage().getReplyMarkup());
            editMessageReplyMarkup.setReplyMarkup(dateToDateCalendar);

            InlineKeyboardButton oldActiveButton = calendarUtils.getActiveDay(dateToDateCalendar, CalendarMarkup.ACTIVE_TO_DATE_SIGN);
            if (oldActiveButton != null) oldActiveButton.setText(oldActiveButton.getText().split(" ")[0]);

            InlineKeyboardButton activeDay = calendarUtils.getButtonByCallbackQuery(dateToDateCalendar.getKeyboard(), callbackQuery.getData());
            activeDay.setText(activeDay.getText() + " " + CalendarMarkup.ACTIVE_TO_DATE_SIGN);

            telegramService.executeEditMessageReplyMarkup(editMessageReplyMarkup);
        }


    }
}
