package me.nereo.multi_image_selector.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {
    public static String timeFormat(long timeMillis, String pattern) {
        return new SimpleDateFormat(pattern, Locale.CHINA).format(new Date(timeMillis));
    }

    public static String formatPhotoDate(long time) {
        return timeFormat(time, "yyyy-MM-dd");
    }

    public static String formatPhotoDate(String path) {
        File file = new File(path);
        if (file.exists()) {
            return formatPhotoDate(file.lastModified());
        }
        return "1970-01-01";
    }
}
