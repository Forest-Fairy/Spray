package top.spray.core.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {
    public static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final SimpleDateFormat STANDARD_FORMATTER = new SimpleDateFormat(STANDARD_FORMAT);
    public static String getCurrentTime() {
        return STANDARD_FORMATTER.format(new Date());
    }

    public static String getCurrentTime(String format) {
        if (STANDARD_FORMAT.equals(format) || format == null || format.trim().isEmpty()) {
            return getCurrentTime();
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }

    public static String getStandardFormatTime(Date date) {
        return STANDARD_FORMATTER.format(date);
    }
    public static String getStandardFormatTime(String format, Date date) {
        if (STANDARD_FORMAT.equals(format) || format == null || format.trim().isEmpty()) {
            return getStandardFormatTime(date);
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

}
