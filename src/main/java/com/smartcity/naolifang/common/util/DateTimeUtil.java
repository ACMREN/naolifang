package com.smartcity.naolifang.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private final static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String localDateTimeToString(LocalDateTime localDateTime) {
        return dtf.format(localDateTime);
    }

    public static LocalDateTime stringToLocalDateTime(String dateTime) {
        return LocalDateTime.parse(dateTime, dtf);
    }

    public static void main(String[] args) {
        String date = "2021-03-10 15:12:09";
        System.out.println(stringToLocalDateTime(date));
    }
}
