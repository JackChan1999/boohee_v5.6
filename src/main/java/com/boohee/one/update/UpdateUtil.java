package com.boohee.one.update;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.boohee.utils.FileUtil;

import java.io.File;

public class UpdateUtil {
    public static final String UPDATE_DIR = "update";

    public static void installApk(Context context, File file) {
        if (file != null && file.exists()) {
            Intent intent = getInstallIntent(file);
            if (intent != null) {
                context.startActivity(intent);
            }
        }
    }

    public static Intent getInstallIntent(File file) {
        if (file == null || !file.exists()) {
            return null;
        }
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(268435456);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        return intent;
    }

    public static File getUpdateDir(Context context) {
        return FileUtil.getFileDirectory(context, UPDATE_DIR, true);
    }
}
