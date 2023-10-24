package com.example.rest.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateDeserializer {
    private final SimpleDateFormat simpleDateFormat;
    public final String format1 = "yyyy-MM-dd HH:mm:ss";

    public DateDeserializer() {
        this.simpleDateFormat = new SimpleDateFormat(format1);
    }

    public LocalDateTime getDateFromStr(String date) {
        Date resultDate = new Date();
        try{
            resultDate = simpleDateFormat.parse(date);
        }
        catch(ParseException e){
            e.printStackTrace();
        }
        LocalDateTime ldTime =LocalDateTime.of( resultDate.getYear(),
                                                resultDate.getMonth(),
                                                resultDate.getDate(),
                                                resultDate.getHours(),
                                                resultDate.getMinutes(),
                                                resultDate.getSeconds());
        return ldTime;
    }
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public LocalDateTime convertToLocalDatetime(String dateValue) {
        return (dateValue == null ? null : LocalDateTime.parse(dateValue, formatter));
    }
}
