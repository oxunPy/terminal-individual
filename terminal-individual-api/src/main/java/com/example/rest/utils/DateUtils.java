package com.example.rest.utils;

import lombok.Data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Data
public class DateUtils {
    private static String PATTERN_1 = "yyyy-MM-dd hh:mm:ss";
    private static String PATTERN_2 = "dd-MM-yyyy";
    private static String PATTERN_3 = "yyyy-MM-dd";

    public static String dateToStringFormat1(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(PATTERN_1);
        return dateFormat.format(date);
    }

    public static String dateToStringFormat2(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(PATTERN_2);
        return dateFormat.format(date);
    }

    public static Date stringToDateFormat1(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_1);
        Date convertedCurrentDate = null;
        try {
            convertedCurrentDate = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedCurrentDate;
    }

    public static LocalDate stringToLocalDateFormat(String dateStr) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(PATTERN_3);
        return LocalDate.parse(dateStr, dateTimeFormatter);
    }
}
