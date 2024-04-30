package com.darzee.shankh.utils;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TimeUtils {

    public static LocalDateTime getWeekStartTime(LocalDateTime time) {
        LocalDateTime localTime = time;
        while (localTime.getDayOfWeek() != DayOfWeek.SUNDAY) {
            localTime = localTime.minusDays(1);
        }
        return localTime.withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    public static LocalDateTime convertISTToUTC(LocalDateTime istLocalDateTime) {
        ZoneId istZoneId = ZoneId.of("Asia/Kolkata");
        LocalDateTime utcDateTime = istLocalDateTime.atZone(istZoneId).withZoneSameInstant(ZoneId.of("UTC"))
                .toLocalDateTime();
        return utcDateTime;

    }

    public static LocalDateTime convertUTCToIST(LocalDateTime utcLocalDateTime) {
        ZoneId utcZoneId = ZoneId.of("UTC");
        LocalDateTime istDateTime = utcLocalDateTime.atZone(utcZoneId).withZoneSameInstant(ZoneId.of("Asia/Kolkata"))
                .toLocalDateTime();
        return istDateTime;

    }

    public static String getISOFormatDateTime(LocalDateTime dateTime) {
        if (dateTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            return dateTime.format(formatter);
        } else {
            return null;
        }
    }
}
