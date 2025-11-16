package com.kalinchak.smart_parking_system.util;

import lombok.experimental.UtilityClass;

import java.time.Duration;
import java.time.LocalDateTime;

@UtilityClass
public class TimeFormatUtils {

    public static String formatDuration(LocalDateTime entryTime, LocalDateTime exitTime) {

        if (entryTime == null || exitTime == null) {
            throw new IllegalArgumentException("entryTime and exitTime must not be null");
        }

        if (exitTime.isBefore(entryTime)) {
            throw new IllegalArgumentException("exitTime cannot be before entryTime");
        }

        Duration duration = Duration.between(entryTime, exitTime);

        long seconds = duration.getSeconds();

        long days = seconds / 86_400;
        seconds %= 86_400;

        long hours = seconds / 3600;
        seconds %= 3600;

        long minutes = seconds / 60;
        seconds %= 60;

        StringBuilder sb = new StringBuilder();

        if (days > 0) {
            sb.append(days).append(days == 1 ? " day " : " days ");
        }
        if (hours > 0) {
            sb.append(hours).append(hours == 1 ? " hour " : " hours ");
        }
        if (minutes > 0) {
            sb.append(minutes).append(minutes == 1 ? " minute " : " minutes ");
        }
        if (seconds > 0 || sb.isEmpty()) {
            sb.append(seconds).append(seconds == 1 ? " second" : " seconds");
        }

        return sb.toString().trim();
    }
}
