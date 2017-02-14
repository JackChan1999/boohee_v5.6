package me.nereo.multi_image_selector.utils;

import android.content.Context;
import android.os.Environment;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileUtils {
    public static File createTmpFile(Context context) {
        if (Environment.getExternalStorageState().equals("mounted")) {
            return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), ("multi_image_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date()) + "") + ".jpg");
        }
        return new File(context.getCacheDir(), ("multi_image_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date()) + "") + ".jpg");
    }
}
