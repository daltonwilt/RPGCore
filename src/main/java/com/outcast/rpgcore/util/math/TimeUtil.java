package com.outcast.rpgcore.util.math;

import java.time.Duration;

public class TimeUtil {

    public static final long MILLISECONDS = 1000;
    public static final int TICKS = 20;

    //  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    //  Methods for time conversion in milliseconds, seconds, and ticks.
    //  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

    public static int msToTicks(long ms) { return (int) Math.round((double)ms / TICKS); }

    public static long ticksToMs(int ticks) {
        return Math.round(ticks * TICKS);
    }

    public static long msToSeconds(long ms) {
        return Math.round((double)ms / MILLISECONDS);
    }

    public static long secondToMs(long seconds) {
        return Math.round(seconds * MILLISECONDS);
    }

    public static long ticksToSeconds(int ticks) {
        return Math.round((double)ticks / TICKS);
    }

    public static long secondToTicks(int ticks) {
        return Math.round(ticks * TICKS);
    }

    //  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    //  Formatting Duration type to string and vice versa.
    //  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

    public static String durationToString(Duration duration) {
        String s = "";

        if(duration != null) {
            long days = duration.toDays();
            duration = duration.minusDays(days);
            long hours = duration.toHours();
            duration = duration.minusHours(hours);
            long minutes = duration.toMinutes();
            duration = duration.minusMinutes(minutes);
            long seconds = duration.getSeconds();
            duration = duration.minusSeconds(seconds);
            long millis = duration.toMillis();
            s = (days == 0 ? "" : days + "d") +
                    (hours == 0 ? "" : hours + "h") +
                    (minutes == 0 ? "" : minutes + "m") +
                    (seconds == 0 ? "" : seconds + "s") +
                    (seconds == 0 ? "" : millis + "M");
        }

        return s;
    }

    public static Duration stringToDuration(String dString) {
        if(dString == null || dString.isEmpty())
            return null;

        String delimiter = "(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)";
        String[] tokens = dString.split(delimiter);
        Duration d = Duration.ZERO;

        for (int i = 1; i < tokens.length; i++) {
            if (tokens[i - 1].matches("\\d+")) {
                long amount = Long.parseLong(tokens[i - 1]);
                switch (tokens[i]) {
                    case "S":
                        d = d.plusMillis(amount);
                        break;
                    case "s":
                        d = d.plusSeconds(amount);
                        break;
                    case "m":
                        d = d.plusMinutes(amount);
                        break;
                    case "h":
                        d = d.plusHours(amount);
                        break;
                    case "d":
                        d = d.plusDays(amount);
                        break;
                }
            }

        }

        return d;
    }

}