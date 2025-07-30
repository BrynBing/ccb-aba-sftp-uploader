package com.ccb.aba.sftp.uploader.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

public final class DateUtil {
    private static final DateTimeFormatter YYYYMMDD_STRICT = DateTimeFormatter.ofPattern("uuuuMMdd").withResolverStyle(ResolverStyle.STRICT);



    private DateUtil() {}

    public static ZoneId zone() {
        return ZoneId.systemDefault();
    }

    public static ZonedDateTime startBoundary(LocalDate startDate) {
        return startDate.atTime(7, 0, 0).atZone(zone());
    }

    public static ZonedDateTime endBoundary(LocalDate endDate) {
        ZoneId z = zone();
        LocalTime now = LocalTime.now(z);
        LocalTime endMinute = LocalTime.of(now.getHour(), now.getMinute(), 0);
        return endDate.atTime(endMinute).atZone(z);
    }

    public static boolean isInDateRange(Instant fileInstant,
                                         ZonedDateTime start,
                                         ZonedDateTime end) {
        Instant s = start.toInstant();
        Instant e = end.toInstant();
        return fileInstant.isAfter(s) && !fileInstant.isAfter(e);
    }

    public static LocalDate parseDate(String s) {
        if (s == null || !s.matches("\\d{8}")) {
            throw new IllegalArgumentException("Date must be yyyymmdd (8 digits). Got: " + s);
        }
        return LocalDate.parse(s, YYYYMMDD_STRICT);
    }
}
