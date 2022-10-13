package com.outcast.rpgcore.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.apache.commons.lang3.time.DurationFormatUtils;

public class TextUtil {

    public static TextComponent formatDuration(long duration) {
        String format = "H'h' m'm' s.S's'";

        if (duration < 60000) {
            format = "s's'";
        }

        if (duration >= 60000 && duration < 3600000) {
            format = "m'm'";
        }

        if (duration >= 3600000 && duration < 86400000) {
            format = "H'h' m'm'";
        }

        if (duration >= 86400000) {
            format = "d'd' H'h'";
        }

        String formatted = DurationFormatUtils.formatDuration(duration, format, false);
        // Remove the third digit after the decimal
        formatted = new StringBuilder(formatted).deleteCharAt(formatted.length() - 2).toString();

        return Component.text(formatted);
    }

}
