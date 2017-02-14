package com.boohee.widgets;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;

import java.util.List;

public class Util {
    public static  String  Log_Tag                  = "Hori";
    private static boolean isSDCardAvailable        = false;
    private static boolean isSDCardReceiverRegister = false;
    private static boolean isSDCardWriteable        = false;
    public static  sdcardListener    mSdcardListener;
    private static Context           saveContext;
    private static BroadcastReceiver sdCardReceiver;

    public static class DensityUtil {
        public static int dip2px(Context context, float dpValue) {
            return (int) ((dpValue * context.getResources().getDisplayMetrics().density) + 0.5f);
        }

        public static int px2dip(Context context, float pxValue) {
            return (int) ((pxValue / context.getResources().getDisplayMetrics().density) + 0.5f);
        }
    }

    public interface sdcardListener {
        void onReceiver(boolean z, boolean z2);
    }

    public static void v(String msg) {
        Log.v(Log_Tag, msg);
    }

    public static void e(String msg) {
        Log.e(Log_Tag, msg);
    }

    public static void toast(Context ctx, String msg, boolean isShort) {
        Toast.makeText(ctx, msg, isShort ? 0 : 1).show();
    }

    public static void toast(Context ctx, int msgid, boolean isShort) {
        Toast.makeText(ctx, msgid, isShort ? 0 : 1).show();
    }

    public static boolean isNull(Object o) {
        return o == null;
    }

    public static boolean isNull(List<?> list) {
        return list == null || list.size() == 0;
    }

    public static boolean isNull(String str) {
        return TextUtils.isEmpty(str);
    }

    public static int isInArray(int[] src, int key) {
        if (isNull((Object) src)) {
            return -1;
        }
        for (int i = 0; i < src.length; i++) {
            if (src[i] == key) {
                return i;
            }
        }
        return -1;
    }

    public static int isInList(List<? extends Object> src, Object key) {
        if (isNull((List) src)) {
            return -1;
        }
        if (isNull(key)) {
            return -1;
        }
        int i = 0;
        while (i < src.size()) {
            if (!isNull(src.get(i)) && src.get(i).equals(key)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static boolean isFlagContain(int sourceFlag, int compareFlag) {
        return (sourceFlag & compareFlag) == compareFlag;
    }

    public static void statueBarVisible(Activity active, int visible) {
        if (visible == 0) {
            active.getWindow().setFlags(2048, 2048);
        } else {
            active.getWindow().clearFlags(2048);
        }
    }

    public static boolean sdcardIsOnline() {
        return "mounted".equals(Environment.getExternalStorageState());
    }

    private static void sdCardUpdateState(Context context) {
        String sdCardState = Environment.getExternalStorageState();
        if ("mounted".equals(sdCardState)) {
            isSDCardWriteable = true;
            isSDCardAvailable = true;
        } else if ("mounted_ro".equals(sdCardState)) {
            isSDCardAvailable = true;
            isSDCardWriteable = false;
        } else {
            isSDCardWriteable = false;
            isSDCardAvailable = false;
        }
    }

    public static synchronized void sdCardStartListener(Context context, sdcardListener lis) {
        synchronized (Util.class) {
            if (!(saveContext == null || saveContext == context)) {
                sdCardStopListener(saveContext);
            }
            mSdcardListener = lis;
            saveContext = context;
            sdCardReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    Util.sdCardUpdateState(context);
                    if (Util.mSdcardListener != null) {
                        Util.mSdcardListener.onReceiver(Util.isSDCardAvailable, Util
                                .isSDCardWriteable);
                    }
                }
            };
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.intent.action.MEDIA_UNMOUNTED");
            filter.addAction("android.intent.action.MEDIA_MOUNTED");
            filter.addAction("android.intent.action.MEDIA_REMOVED");
            filter.addAction("android.intent.action.MEDIA_BAD_REMOVAL");
            filter.addAction("android.intent.action.MEDIA_EJECT");
            if (!isSDCardReceiverRegister) {
                context.registerReceiver(sdCardReceiver, filter);
                isSDCardReceiverRegister = true;
            }
            sdCardUpdateState(context);
        }
    }

    public static synchronized void sdCardStopListener(Context context) {
        synchronized (Util.class) {
            if (isSDCardReceiverRegister && saveContext == context) {
                context.unregisterReceiver(sdCardReceiver);
                isSDCardReceiverRegister = false;
                mSdcardListener = null;
            }
        }
    }

    public static void sysSetActionBness(Activity action, float bness) {
        LayoutParams lp = action.getWindow().getAttributes();
        lp.screenBrightness = bness;
        action.getWindow().setAttributes(lp);
    }

    public static float sysGetActionBness(Activity action) {
        return action.getWindow().getAttributes().screenBrightness;
    }

    public static void sysIsLockScreen(Activity act, boolean isLock) {
        if (isLock) {
            switch (act.getResources().getConfiguration().orientation) {
                case 1:
                    act.setRequestedOrientation(1);
                    return;
                case 2:
                    act.setRequestedOrientation(0);
                    return;
                default:
                    return;
            }
        }
        act.setRequestedOrientation(-1);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService("connectivity");
        if (cm == null || cm.getActiveNetworkInfo() == null) {
            return false;
        }
        return cm.getActiveNetworkInfo().isAvailable();
    }

    public static boolean isGpsEnabled(Context context) {
        List<String> accessibleProviders = ((LocationManager) context.getSystemService
                ("location")).getProviders(true);
        if (accessibleProviders == null || accessibleProviders.size() <= 0) {
            return false;
        }
        return true;
    }

    public static boolean isWifiEnabled(Context context) {
        ConnectivityManager mgrConn = (ConnectivityManager) context.getSystemService
                ("connectivity");
        return (mgrConn.getActiveNetworkInfo() != null && mgrConn.getActiveNetworkInfo().getState
                () == State.CONNECTED) || ((TelephonyManager) context.getSystemService("phone"))
                .getNetworkType() == 3;
    }

    public static final void openGPS(Context context) {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings", "com.android.settings.widget" +
                ".SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (CanceledException e) {
            e.printStackTrace();
        }
    }

    public static boolean is3rd(Context context) {
        NetworkInfo networkINfo = ((ConnectivityManager) context.getSystemService("connectivity")
        ).getActiveNetworkInfo();
        if (networkINfo == null || networkINfo.getType() != 0) {
            return false;
        }
        return true;
    }

    public static boolean isWifi(Context context) {
        NetworkInfo networkINfo = ((ConnectivityManager) context.getSystemService("connectivity")
        ).getActiveNetworkInfo();
        if (networkINfo == null || networkINfo.getType() != 1) {
            return false;
        }
        return true;
    }
}
