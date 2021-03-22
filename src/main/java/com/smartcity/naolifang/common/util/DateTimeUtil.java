package com.smartcity.naolifang.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private final static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final static DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public static String localDateTimeToString(LocalDateTime localDateTime) {
        return dtf.format(localDateTime);
    }

    public static LocalDateTime stringToLocalDateTime(String dateTime) {
        return LocalDateTime.parse(dateTime, dtf);
    }

    public static String localDateToString(LocalDate localDate) {
        return dtf1.format(localDate);
    }

    public static LocalDate stringToLocalDate(String date) {
        return LocalDate.parse(date, dtf1);
    }

    public static String stringToHivikisionTime(String time) {
        time = time.replace(" ", "T");
        return time += "+08:00";
    }

    public static String hikivisionTimeToStr(String time) {
        time = time.replace("T", " ");
        time = time.replace("+08:00", "");
        return time;
    }

    public static void main(String[] args) {
        String date = "2021-03-10 15:12:09";
        System.out.println(stringToHivikisionTime(date));
    }
}
