package top.spray.common.data;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SprayDateTimeUtil {
    public static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static String getCurrentTime() {
        return getStandardFormatTime(new Date());
    }

    public static String getCurrentTime(String format) {
        return formatTime(format, new Date());
    }

    public static String getStandardFormatTime(Date date) {
        return formatTime(STANDARD_FORMAT, date);
    }
    public static String getFormatTime(String format, Date date) {
        return formatTime(format, date);
    }

    private static String formatTime(String format, Date date) {
        if (format == null || format.trim().isEmpty()) {
            format = STANDARD_FORMAT;
        }
        return new SimpleDateFormat(format).format(date);
    }

}
