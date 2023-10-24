package com.example.rest.telegramBot.utils;

import com.example.rest.telegramBot.calendar.CalendarMarkup;
import com.example.rest.telegramBot.calendar.Utils;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class CalendarMarkupUtils {

    public static final String FROM_DATE = "from_date_";
    public static final String TO_DATE = "to_date_";

    public InlineKeyboardMarkup convertToFromDateCalendarMarkup(InlineKeyboardMarkup calendarMarkup) {
        // change some button names as from date
        Integer nextYearIndex = getButtonListIndexByValue(calendarMarkup.getKeyboard().get(0),
                CalendarMarkup.NEXT_YEAR);
        Integer prevYearIndex = getButtonListIndexByValue(calendarMarkup.getKeyboard().get(0),
                CalendarMarkup.PREV_YEAR);
        Integer currentYearIndex = getButtonListIndexByValue(calendarMarkup.getKeyboard().get(0),
                CalendarMarkup.CURRENT_YEAR);

        Integer nextMonthIndex = getButtonListIndexByValue(calendarMarkup.getKeyboard().get(1),
                CalendarMarkup.NEXT_MONTH);
        Integer prevMonthIndex = getButtonListIndexByValue(calendarMarkup.getKeyboard().get(1),
                CalendarMarkup.PREV_MONTH);
        Integer currentMonthIndex = getButtonListIndexByValue(calendarMarkup.getKeyboard().get(1),
                CalendarMarkup.CURRENT_MONTH);

        calendarMarkup.getKeyboard().get(0).get(nextYearIndex).setCallbackData(FROM_DATE + CalendarMarkup.NEXT_YEAR);
        calendarMarkup.getKeyboard().get(0).get(prevYearIndex).setCallbackData(FROM_DATE + CalendarMarkup.PREV_YEAR);
        calendarMarkup.getKeyboard().get(0).get(currentYearIndex).setCallbackData(FROM_DATE + CalendarMarkup.CURRENT_YEAR);

        calendarMarkup.getKeyboard().get(1).get(nextMonthIndex).setCallbackData(FROM_DATE + CalendarMarkup.NEXT_MONTH);
        calendarMarkup.getKeyboard().get(1).get(prevMonthIndex).setCallbackData(FROM_DATE + CalendarMarkup.PREV_MONTH);
        calendarMarkup.getKeyboard().get(1).get(currentMonthIndex).setCallbackData(FROM_DATE + CalendarMarkup.CURRENT_MONTH);

        for(List<InlineKeyboardButton> row : calendarMarkup.getKeyboard()){
            for(InlineKeyboardButton button : row){
                if(Utils.parseInt(button.getCallbackData()) != 0)
                    button.setCallbackData(FROM_DATE  + button.getText());
            }
        }

        // FROM DATE LABEL
        InlineKeyboardButton fromDateLabelButton = new InlineKeyboardButton();
        fromDateLabelButton.setText("с даты:");
        fromDateLabelButton.setCallbackData("FROM DATE");

        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(fromDateLabelButton);

        calendarMarkup.getKeyboard().add(0, row);

        return calendarMarkup;
    }

    public InlineKeyboardMarkup convertToToDateCalendarMarkup(InlineKeyboardMarkup calendarMarkup) {
        // change some button name as to date
        Integer nextYearIndex = getButtonListIndexByValue(calendarMarkup.getKeyboard().get(0),
                CalendarMarkup.NEXT_YEAR);
        Integer prevYearIndex = getButtonListIndexByValue(calendarMarkup.getKeyboard().get(0),
                CalendarMarkup.PREV_YEAR);
        Integer nextMonthIndex = getButtonListIndexByValue(calendarMarkup.getKeyboard().get(1),
                CalendarMarkup.NEXT_MONTH);
        Integer prevMonthIndex = getButtonListIndexByValue(calendarMarkup.getKeyboard().get(1),
                CalendarMarkup.PREV_MONTH);
        Integer currentYearIndex = getButtonListIndexByValue(calendarMarkup.getKeyboard().get(0),
                CalendarMarkup.CURRENT_YEAR);
        Integer currentMonthIndex = getButtonListIndexByValue(calendarMarkup.getKeyboard().get(1),
                CalendarMarkup.CURRENT_MONTH);

        calendarMarkup.getKeyboard().get(0).get(nextYearIndex).setCallbackData(TO_DATE + CalendarMarkup.NEXT_YEAR);
        calendarMarkup.getKeyboard().get(0).get(prevYearIndex).setCallbackData(TO_DATE + CalendarMarkup.PREV_YEAR);
        calendarMarkup.getKeyboard().get(0).get(currentYearIndex).setCallbackData(TO_DATE + CalendarMarkup.CURRENT_YEAR);

        calendarMarkup.getKeyboard().get(1).get(nextMonthIndex).setCallbackData(TO_DATE + CalendarMarkup.NEXT_MONTH);
        calendarMarkup.getKeyboard().get(1).get(prevMonthIndex).setCallbackData(TO_DATE + CalendarMarkup.PREV_MONTH);
        calendarMarkup.getKeyboard().get(1).get(currentMonthIndex).setCallbackData(TO_DATE + CalendarMarkup.CURRENT_MONTH);

        for(List<InlineKeyboardButton> row : calendarMarkup.getKeyboard()){
            for(InlineKeyboardButton button : row){
                if(Utils.parseInt(button.getCallbackData()) != 0)
                    button.setCallbackData(TO_DATE + button.getText());
            }
        }

        // TO DATE LABEL
        InlineKeyboardButton toDateLabelButton = new InlineKeyboardButton();
        toDateLabelButton.setText("до даты:");
        toDateLabelButton.setCallbackData("TO DATE");

        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(toDateLabelButton);

        calendarMarkup.getKeyboard().add(0, row);

        return calendarMarkup;
    }

    public Integer getButtonListIndexByValue(List<InlineKeyboardButton> list, String buttonText) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getCallbackData().equals(buttonText)) {
                return i;
            }
        }
        return -1;
    }

    public InlineKeyboardMarkup getFromDateToDateCalendar() {

        InlineKeyboardMarkup doubleCalendarMarkup = new InlineKeyboardMarkup();

        // CALENDARS
        InlineKeyboardMarkup fromDateCalendar = convertToFromDateCalendarMarkup(new CalendarMarkup()
                .getCalendarInstanceWithoutSubmit());
        InlineKeyboardMarkup toDateCalendar = convertToToDateCalendarMarkup(new CalendarMarkup()
                .getCalendarInstance());

        List<List<InlineKeyboardButton>> doubleCalendarKeyboard = new ArrayList<>();
        doubleCalendarKeyboard.addAll(fromDateCalendar.getKeyboard());
        doubleCalendarKeyboard.addAll(toDateCalendar.getKeyboard());

        doubleCalendarMarkup.setKeyboard(doubleCalendarKeyboard);
        return doubleCalendarMarkup;
    }


    public LocalDate getFromDateFromCalendar(InlineKeyboardMarkup replyMarkup) {
        Integer year = LocalDate.now().getYear();
        Integer month = LocalDate.now().getMonthValue();
        Integer day = LocalDate.now().getDayOfMonth();

        for (List<InlineKeyboardButton> row : replyMarkup.getKeyboard()) {
            for (InlineKeyboardButton btn : row) {
                if (btn.getCallbackData().equals(FROM_DATE + CalendarMarkup.CURRENT_YEAR)) {
                    year = Integer.parseInt(btn.getText());
                } else if (btn.getCallbackData().equals(FROM_DATE + CalendarMarkup.CURRENT_MONTH)) {
                    month = Month.valueOf(btn.getText()).getValue();
                } else if (btn.getText().contains(CalendarMarkup.ACTIVE_FROM_DATE_SIGN)) {
                    day = Integer.parseInt(btn.getText().split(" ")[0]);
                }
            }
        }
        return LocalDate.of(year, month, day);
    }

    public LocalDate getToDateFromCalendar(InlineKeyboardMarkup replyMarkup) {
        Integer year = LocalDate.now().getYear();
        Integer month = LocalDate.now().getMonthValue();
        Integer day = LocalDate.now().getDayOfMonth();

        for (List<InlineKeyboardButton> row : replyMarkup.getKeyboard()) {
            for (InlineKeyboardButton btn : row) {
                if (btn.getCallbackData().equals(TO_DATE + CalendarMarkup.CURRENT_YEAR)) {
                    year = Integer.parseInt(btn.getText());
                } else if (btn.getCallbackData().equals(TO_DATE + CalendarMarkup.CURRENT_MONTH)) {
                    month = Month.valueOf(btn.getText()).getValue();
                } else if (btn.getText().contains(CalendarMarkup.ACTIVE_TO_DATE_SIGN)) {
                    day = Integer.parseInt(btn.getText().split(" ")[0]);
                }
            }
        }
        return LocalDate.of(year, month, day);
    }

    public InlineKeyboardButton getButtonByCallbackQuery(List<List<InlineKeyboardButton>> buttonGrid,String callBackQueryName){
        for(List<InlineKeyboardButton> row : buttonGrid){
            for(InlineKeyboardButton button : row){
                if(button.getCallbackData().equals(callBackQueryName)){
                    return button;
                }
            }
        }
        return null;
    }

    public InlineKeyboardMarkup cloneDateToDateCalendar(InlineKeyboardMarkup calendar){
        InlineKeyboardMarkup dateToDateMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        for(List<InlineKeyboardButton> keyboardRow : calendar.getKeyboard()){
            for(InlineKeyboardButton button : keyboardRow){
                InlineKeyboardButton clonedButton = new InlineKeyboardButton();
                clonedButton.setText(button.getText());
                clonedButton.setCallbackData(button.getCallbackData());
                row.add(clonedButton);
            }
            keyboard.add(row);
            row = new ArrayList<>();
        }
        dateToDateMarkup.setKeyboard(keyboard);
        return dateToDateMarkup;
    }

    public void updateDaysFromDate(InlineKeyboardMarkup calendar, int year, int monthVal){
        int daysInMonth = Utils.getNumberOfDaysInMonth(year, monthVal - 1);

        int lastFromDateDayRow = getLastRowFromDateFromDateToDateCalendar(calendar);

        List<InlineKeyboardButton> lastRow = calendar.getKeyboard().get(lastFromDateDayRow);
        int lastDay = Integer.parseInt(lastRow.get(lastRow.size() - 1).getText());
        if(lastDay < daysInMonth){
            if(lastDay == 28){
                lastRow = new ArrayList<>();
                calendar.getKeyboard().add(lastFromDateDayRow + 1, lastRow);
            }
            while(lastDay++ < daysInMonth){
                InlineKeyboardButton newButton = new InlineKeyboardButton();
                newButton.setText(String.valueOf(lastDay));
                newButton.setCallbackData(FROM_DATE + lastDay);
                lastRow.add(newButton);
            }
        }
        else if(lastDay > daysInMonth){
            while(lastDay-- > daysInMonth){
                lastRow.remove(lastRow.size() - 1);
            }
        }

    }

    public void updateDaysToDate(InlineKeyboardMarkup calendar, int year, int monthVal){
        int daysInMonth = Utils.getNumberOfDaysInMonth(year, monthVal - 1);
        int lastToDateDayRow = getLastRowToDateFromDateToDateCalendar(calendar);

        List<InlineKeyboardButton> lastRow = calendar.getKeyboard().get(lastToDateDayRow);
        int lastDay = Integer.parseInt(lastRow.get(lastRow.size() - 1).getText());
        if(lastDay < daysInMonth){
            if(lastDay == 28){
                lastRow = new ArrayList<>();
                calendar.getKeyboard().add(lastToDateDayRow + 1, lastRow);
            }
            while(lastDay++ < daysInMonth){
                InlineKeyboardButton newButton = new InlineKeyboardButton();
                newButton.setText(String.valueOf(lastDay));
                newButton.setCallbackData(TO_DATE + lastDay);
                lastRow.add(newButton);
            }
        }
        else if(lastDay > daysInMonth){
            while(lastDay-- > daysInMonth){
                lastRow.remove(lastRow.size() - 1);
            }
        }

    }

    private int getLastRowFromDateFromDateToDateCalendar(InlineKeyboardMarkup calendar){
        List<List<InlineKeyboardButton>> keyboard = calendar.getKeyboard();
        int row;

        for(row = 0; row < keyboard.size(); row++){
            String callBackData = keyboard.get(row).get(0).getCallbackData();           // row's first button callBackData
            if(callBackData.startsWith(FROM_DATE) && hasMonthDayNumber(callBackData)){
                while(row < keyboard.size() && callBackData.startsWith(FROM_DATE) && hasMonthDayNumber(callBackData)){
                    row++;
                    callBackData = keyboard.get(row).get(0).getCallbackData();
                }
                break;
            }
        }
        return row - 1;
    }
    private int getLastRowToDateFromDateToDateCalendar(InlineKeyboardMarkup calendar){
        return calendar.getKeyboard().size() - 2;
    }



    public boolean hasMonthDayNumber(String text){
        for(int i = 1; i < 31; i++){
            if(text.contains(String.valueOf(i))) return true;
        }
        return false;
    }

    public InlineKeyboardButton getActiveDay(InlineKeyboardMarkup markup, String activeSign){
        for(List<InlineKeyboardButton> row : markup.getKeyboard()){
            for(InlineKeyboardButton button : row){
                if(button.getText().contains(activeSign)){
                    return button;
                }
            }
        }
        return null;
    }

}
