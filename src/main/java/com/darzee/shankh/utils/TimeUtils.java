package com.darzee.shankh.utils;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

public class TimeUtils {

    public static LocalDateTime convertISTToUTC(LocalDateTime istLocalDateTime) {
        ZoneId istZoneId = ZoneId.of("Asia/Kolkata");
        LocalDateTime utcDateTime = istLocalDateTime.atZone(istZoneId).withZoneSameInstant(ZoneId.of("UTC"))
                .toLocalDateTime();
        return utcDateTime;

    }


    public static LocalDateTime convertSystemTimeZoneToUTC(LocalDateTime istLocalDateTime) {
        ZoneId systemZoneId = ZoneId.systemDefault();
        LocalDateTime utcDateTime = istLocalDateTime.atZone(systemZoneId).withZoneSameInstant(ZoneId.of("UTC"))
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

    public static LocalDate getNextSunday(LocalDate inputDate) {
        return inputDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
    }

    public static LocalDateTime getTimeInDBTimeZone(LocalDateTime inputTime, ZoneId clientZone) {
        ZoneId targetZone = ZoneId.of("UTC");
        ZonedDateTime clientZonedDateTime = ZonedDateTime.of(inputTime, clientZone);
        return clientZonedDateTime.withZoneSameInstant(targetZone).toLocalDateTime();
    }

    public static LocalDate getWeekStartDate(Timestamp inputDateTime) {
        LocalDate inputDate = inputDateTime.toLocalDateTime().toLocalDate();
        LocalDate monthStartDate = inputDate.withDayOfMonth(1);
        LocalDate nextSundayDate = TimeUtils.getNextSunday(inputDate);
        LocalDate previousMonday = nextSundayDate.minusDays(6);
        return monthStartDate.isAfter(previousMonday) ? monthStartDate : previousMonday;
    }

    public static LocalDate getWeekStartDate(LocalDate inputDate) {
        LocalDate monthStartDate = inputDate.withDayOfMonth(1);
        LocalDate nextSundayDate = TimeUtils.getNextSunday(inputDate);
        LocalDate previousMonday = nextSundayDate.minusDays(6);
        return monthStartDate.isAfter(previousMonday) ? monthStartDate : previousMonday;
    }

    public static LocalDateTime getWeekStartDateTime(LocalDateTime inputTime) {
        LocalDate weekStartDate = getWeekStartDate(Timestamp.valueOf(inputTime));
        return weekStartDate.atStartOfDay();
    }
}
