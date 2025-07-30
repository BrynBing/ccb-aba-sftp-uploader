package com.ccb.aba.sftp.uploader.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    private static final DateTimeFormatter YYYYMMDD = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter TOUCH_TIMESTAMP = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

    public static String today() {
        return LocalDate.now().format(YYYYMMDD);
    }

    public static LocalDateTime parseTouchTimestamp(String touchTime) {
        return LocalDateTime.parse(touchTime, TOUCH_TIMESTAMP);
    }

    public static String buildTouchTimestamp(String yyyymmdd, int hour) {
        return yyyymmdd + String.format("%02d", hour) + "00";
    }

    public static LocalDate parseDate(String yyyymmdd) {
        return LocalDate.parse(yyyymmdd, YYYYMMDD);
    }

    public static boolean isInDateRange(LocalDateTime fileTime, LocalDateTime start, LocalDateTime end) {
        return (fileTime.isEqual(start) || fileTime.isAfter(start)) && fileTime.isBefore(end);
    }
}
