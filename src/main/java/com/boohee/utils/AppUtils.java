package com.boohee.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.boohee.database.OnePreference;
import com.boohee.model.ModelName;
import com.boohee.one.R;
import com.boohee.one.ui.BrowserActivity;
import com.boohee.utility.BuilderIntent;
import com.boohee.utility.Config;
import com.zxinsight.MagicWindowSDK;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class AppUtils {
    public static final String  BOOHEE_PACKAGE_NAME = "com.boohee.one";
    public static final String  FOOD_PACKAGE_NAME   = "com.boohee.food";
    public static final String  LIGHT_PACKAGE_NAME  = "com.boohee.light";
    public static final String  MODEL_PACKAGE_NAME  = "com.boohee.secret";
    public static final String  WECHAT_PACKAGE_NAME = "com.tencent.mm";
    public static       boolean isAppActive         = false;

    public static void launchApp(Context context, String pkg) {
        if (isAppInstalled(context, pkg)) {
            context.startActivity(context.getPackageManager().getLaunchIntentForPackage(pkg));
        } else {
            goToMarket(context, pkg);
        }
    }

    public static void launchBoohee(Context context) {
        if (isAppInstalled(context, "com.boohee.one")) {
            context.startActivity(context.getPackageManager().getLaunchIntentForPackage("com" +
                    ".boohee.one"));
        } else {
            goToMarket(context, "com.boohee.one");
        }
    }

    public static void launchLight(Context context) {
        if (isAppInstalled(context, LIGHT_PACKAGE_NAME)) {
            context.startActivity(context.getPackageManager().getLaunchIntentForPackage
                    (LIGHT_PACKAGE_NAME));
        } else {
            new BuilderIntent(context, BrowserActivity.class).putExtra("url", "http://shop.boohee" +
                    ".com/store/pages/lightcalory_android").putExtra("title", "轻卡减肥")
                    .startActivity();
        }
    }

    public static void launchFood(Context context) {
        if (isAppInstalled(context, FOOD_PACKAGE_NAME)) {
            context.startActivity(context.getPackageManager().getLaunchIntentForPackage
                    (FOOD_PACKAGE_NAME));
        } else {
            new BuilderIntent(context, BrowserActivity.class).putExtra("url", "http://shop.boohee" +
                    ".com/store/pages/foodlibrary_android").putExtra("title", "食物图书馆")
                    .startActivity();
        }
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void goToMarket(Context context, String packageName) {
        try {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse
                    ("market://details?id=" + packageName)));
        } catch (ActivityNotFoundException e) {
            Helper.showToast((int) R.string.xt);
        }
    }

    public static void launchOrDownloadApp(Context context, String packageName) {
        if (isAppInstalled(context, packageName)) {
            context.startActivity(context.getPackageManager().getLaunchIntentForPackage
                    (packageName));
        } else {
            goToMarket(context, packageName);
        }
    }

    public static String getChannelFromMetaInf(Context context) {
        IOException e;
        String[] split;
        Throwable th;
        String ret = "";
        ZipFile zipfile = null;
        try {
            ZipFile zipfile2 = new ZipFile(context.getApplicationInfo().sourceDir);
            try {
                Enumeration<?> entries = zipfile2.entries();
                while (entries.hasMoreElements()) {
                    String entryName = ((ZipEntry) entries.nextElement()).getName();
                    if (entryName.startsWith("META-INF/channel")) {
                        ret = entryName;
                        break;
                    }
                }
                if (zipfile2 != null) {
                    try {
                        zipfile2.close();
                        zipfile = zipfile2;
                    } catch (IOException e2) {
                        e2.printStackTrace();
                        zipfile = zipfile2;
                    }
                }
            } catch (IOException e3) {
                e2 = e3;
                zipfile = zipfile2;
                try {
                    e2.printStackTrace();
                    if (zipfile != null) {
                        try {
                            zipfile.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                        }
                    }
                    split = ret.split("_");
                    if (split != null) {
                    }
                    return "";
                } catch (Throwable th2) {
                    th = th2;
                    if (zipfile != null) {
                        try {
                            zipfile.close();
                        } catch (IOException e222) {
                            e222.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                zipfile = zipfile2;
                if (zipfile != null) {
                    zipfile.close();
                }
                throw th;
            }
        } catch (IOException e4) {
            e222 = e4;
            e222.printStackTrace();
            if (zipfile != null) {
                zipfile.close();
            }
            split = ret.split("_");
            if (split != null) {
            }
            return "";
        }
        split = ret.split("_");
        if (split != null || split.length < 2) {
            return "";
        }
        return ret.substring(split[0].length() + 1);
    }

    public static String getChannel(Context context) {
        String channel = "";
        try {
            channel = MagicWindowSDK.getMLink().getLastChannelForMLink();
            if (!TextUtils.isEmpty(channel)) {
                return URLEncoder.encode(channel, "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        int currentVersionCode = Config.getVersionCode();
        Helper.showLog("当前的VersionCode:" + currentVersionCode);
        int versionCodeSaved = OnePreference.getVersionCode();
        Helper.showLog("缓存的VersionCode:" + versionCodeSaved);
        if (currentVersionCode == versionCodeSaved) {
            channel = OnePreference.getChannel();
            Helper.showLog("缓存的channel:" + channel);
        }
        if (TextUtils.isEmpty(channel)) {
            channel = getChannelFromMetaInf(context);
            Helper.showLog("MetaInf的channel:" + channel);
            OnePreference.setChannel(channel);
            OnePreference.updateVersionCode();
        }
        return TextUtils.isEmpty(channel) ? SDcard.BOOHEE_DIR : channel;
    }

    public static boolean isForeground(Context context) {
        List<RunningTaskInfo> tasks = ((ActivityManager) context.getSystemService(ModelName
                .ACTIVITY)).getRunningTasks(1);
        if (tasks.isEmpty() || !((RunningTaskInfo) tasks.get(0)).topActivity.getPackageName()
                .equals(context.getPackageName())) {
            return false;
        }
        return true;
    }
}
