package com.smartcity.naolifang.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private final static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final static DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final static DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");


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

    public static String iso8601ToString(String time) {
        LocalDateTime localDateTime = LocalDateTime.parse(time, dtf2);
        return localDateTimeToString(localDateTime);
    }

    public static String stringToIso8601(String time) {
        LocalDateTime ldt = LocalDateTime.parse(time, dtf);
        ZoneOffset zoneOffset = ZoneOffset.of("+08:00");
        OffsetDateTime offsetDateTime = OffsetDateTime.of(ldt, zoneOffset);
        return offsetDateTime.format(dtf2);
    }

    public static void main(String[] args) {
        String date = "2021-03-10 15:12:09";
        System.out.println(stringToIso8601(date));
    }
}
