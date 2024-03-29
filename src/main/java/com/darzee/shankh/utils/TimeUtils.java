package com.darzee.shankh.utils;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class TimeUtils {

    public static LocalDateTime getWeekStartTime(LocalDateTime time) {
        LocalDateTime localTime = time;
        while (localTime.getDayOfWeek() != DayOfWeek.SUNDAY) {
            localTime = localTime.minusDays(1);
        }
        return localTime.withHour(0).withMinute(0).withSecond(0).withNano(0);
    }
}
