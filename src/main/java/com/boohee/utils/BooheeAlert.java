package com.boohee.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;

import com.boohee.apn.ApnActivity;
import com.boohee.database.OnePreference;
import com.boohee.one.R;
import com.boohee.one.ui.MainActivity;
import com.boohee.widgets.LightAlertDialog;

import java.util.Calendar;

public class BooheeAlert {
    public static final String CURRENT_VER    = "current_version_code";
    public static final int    DELAY_TIME     = 1;
    public static final String FIRST_START    = "first_start";
    public static final String IS_SCORE_ALERT = "is_score_alert";

    public static boolean showAllDialog(MainActivity activity) {
        if (activity == null) {
            return false;
        }
        OnePreference op = OnePreference.getInstance(activity);
        int versionCode = 0;
        try {
            versionCode = activity.getPackageManager().getPackageInfo(activity.getPackageName(),
                    0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        long firstStart = op.getLong(FIRST_START);
        int oldVersionCode = op.getInt(CURRENT_VER);
        if (0 == firstStart || versionCode > oldVersionCode) {
            op.putLong(FIRST_START, System.currentTimeMillis());
            op.putInt(CURRENT_VER, versionCode);
            op.putBoolean(IS_SCORE_ALERT, false);
            return true;
        } else if (op.getBoolean(IS_SCORE_ALERT, false)) {
            return false;
        } else {
            Calendar calOld = Calendar.getInstance();
            calOld.setTimeInMillis(firstStart);
            if (Math.abs(Calendar.getInstance().get(6) - calOld.get(6)) < 1) {
                return false;
            }
            boolean result = showScoreAlert(activity);
            op.putBoolean(IS_SCORE_ALERT, true);
            return result;
        }
    }

    public static boolean showScoreAlert(final Activity activity) {
        if (activity.isFinishing()) {
            return false;
        }
        try {
            LightAlertDialog dialog = LightAlertDialog.create((Context) activity, (int) R.string
                    .a33, (int) R.string.a30);
            dialog.setNegativeButton((int) R.string.a2z, null);
            dialog.setPositiveButton((int) R.string.a32, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        Intent intent = new Intent("android.intent.action.VIEW");
                        intent.setData(Uri.parse("market://details?id=" + activity.getPackageName
                                ()));
                        activity.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            dialog.setNeutralButton(R.string.a31, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ApnActivity.comeOnBaby(activity, true);
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private static boolean showFirstStart(MainActivity activity) {
        if (activity.isFinishing()) {
            return false;
        }
        try {
            LightAlertDialog dialog = LightAlertDialog.create((Context) activity, (int) R.string
                    .a12, (int) R.string.a0v);
            dialog.setNegativeButton((int) R.string.ot, null);
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
