package com.example.rest.telegramBot.calendar;

import java.time.Month;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Utils {
    public static Month getPrevMonth(String month){
        if(Month.JANUARY.toString().equals(month)){
            return Month.DECEMBER;
        }
        Integer monthVal = getMonthValue(month);
        return Month.of(monthVal - 1);
    }

    public static Month getNextMonth(String month){
        if(Month.DECEMBER.toString().equals(month)){
            return Month.JANUARY;
        }
        Integer monthVal = getMonthValue(month);
        return Month.of(monthVal + 1);
    }

    public static Integer getMonthValue(String month){
        Integer monthVal = 1;

        for(Month m : Month.values()){
            if(m.toString().equals(month)) break;
            monthVal++;
        }
        return monthVal;
    }

    public static int getNumberOfDaysInMonth(int year, int month) {
        // Create a calendar object and set year and month
        Calendar mycal = new GregorianCalendar(year, month, 1);
        int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return daysInMonth;
    }

    public static Integer parseInt(String number){
        try{
            return Integer.valueOf(number);
        }catch(NumberFormatException e){
            return 0;
        }
    }
}
