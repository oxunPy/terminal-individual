package com.example.rest.telegramBot.calendar;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CalendarMarkup {

    public static final String NEXT_YEAR = "next_year";
    public static final String PREV_YEAR = "prev_year";
    public static final String NEXT_MONTH = "next_month";
    public static final String PREV_MONTH = "prev_month";
    public static final String SUBMIT = "подтвердить";
    public static final String SUBMIT_DATE = "submit_date";
    public static final String CURRENT_YEAR = "current_year";
    public static final String CURRENT_MONTH = "current_month";
    public static final String ACTIVE_FROM_DATE_SIGN = /*"\u221A"*/"\u2734";
    public static final String ACTIVE_TO_DATE_SIGN = /*"\u1F5F8"*/"\u2733";

    public InlineKeyboardMarkup getCalendarInstance() {
        return generateCalendar(LocalDate.now(), true);
    }

    public InlineKeyboardMarkup getCalendarInstanceWithoutSubmit(){
        return generateCalendar(LocalDate.now(), false);
    }

    private InlineKeyboardMarkup generateCalendar(LocalDate date, boolean withSubmit) {
        InlineKeyboardMarkup markup = generateMonthKeyboard(date);
        List<List<InlineKeyboardButton>> keyboard = markup.getKeyboard();
        keyboard.addAll(generateDaysKeyboard(date, withSubmit));
        return markup;
    }

    public List<List<InlineKeyboardButton>> generateDaysKeyboard(LocalDate date, boolean withSubmit) {
        int numberOfDays = Utils.getNumberOfDaysInMonth(date.getYear(), date.getMonthValue() - 1);

        List<InlineKeyboardButton> row = new ArrayList<>();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        for (int day = 1; day <= numberOfDays; day++) {
            if (day % 7 == 0) {
                InlineKeyboardButton lastDayButton = new InlineKeyboardButton();
                lastDayButton.setText(String.valueOf(day));
                lastDayButton.setCallbackData(String.valueOf(day));
                lastDayButton.setPay(false);
                row.add(lastDayButton);
                keyboard.add(row);
                row = new ArrayList<>();
            } else {
                InlineKeyboardButton dayButton = new InlineKeyboardButton();
                dayButton.setText(String.valueOf(day));
                dayButton.setCallbackData(String.valueOf(day));
                dayButton.setPay(false);
                row.add(dayButton);
            }
        }
        if (!row.isEmpty()) keyboard.add(row);

        if(withSubmit) {
            InlineKeyboardButton submit = new InlineKeyboardButton();
            submit.setText(SUBMIT);
            submit.setCallbackData(SUBMIT_DATE);
            row = new ArrayList<>();
            row.add(submit);
            keyboard.add(row);
        }


        return keyboard;
    }

    private InlineKeyboardMarkup generateYearKeyboard(Integer year) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton prevYear = new InlineKeyboardButton("<<");
        prevYear.setCallbackData(PREV_YEAR);
        row.add(prevYear);            // previous year

        InlineKeyboardButton currentYear = new InlineKeyboardButton(String.valueOf(year));
        currentYear.setCallbackData(CURRENT_YEAR);
        row.add(currentYear);            // previous year

        InlineKeyboardButton nextYear = new InlineKeyboardButton(">>");
        nextYear.setCallbackData(NEXT_YEAR);
        row.add(nextYear);            // previous year

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(row);
        markup.setKeyboard(keyboard);
        return markup;
    }

    public InlineKeyboardMarkup generateMonthKeyboard(LocalDate date) {
        InlineKeyboardMarkup markup = generateYearKeyboard(date.getYear());
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton prevMonth = new InlineKeyboardButton("<<");
        prevMonth.setCallbackData(PREV_MONTH);
        row.add(prevMonth);            // previous year

        InlineKeyboardButton currentMonth = new InlineKeyboardButton(String.valueOf(date.getMonth()));
        currentMonth.setCallbackData(CURRENT_MONTH);
        row.add(currentMonth);            // previous year

        InlineKeyboardButton nextMonth = new InlineKeyboardButton(">>");
        nextMonth.setCallbackData(NEXT_MONTH);
        row.add(nextMonth);            // previous year

        List<List<InlineKeyboardButton>> keyboard = markup.getKeyboard();
        keyboard.add(row);
        return markup;
    }


}
