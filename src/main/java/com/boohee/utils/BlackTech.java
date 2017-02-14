package com.boohee.utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.boohee.one.MyApplication;
import com.boohee.one.ui.ChangeEnvironmentActivity;

public class BlackTech {
    public static final String  API_ENVIRONMENT_PREFS = "api_environment_prefs";
    public static final String  API_ENV_PRO           = "PRO";
    public static final String  API_ENV_QA            = "QA";
    public static final String  API_ENV_RC            = "RC";
    public static final String  CAN_IP_CONNECT_PREFS  = "can_ip_connect_prefs";
    public static final String  CURRENT_TEST_IP       = "current_test_ip";
    public static       boolean DEBUG                 = false;
    public static final String  IP_CONNECT_OPEN_PREFS = "ip_connect_status_prefs";
    public static long sCurrentTime;
    public static int  sTimes;

    public static boolean isIPConnectOpen() {
        return getPreferences().getBoolean(IP_CONNECT_OPEN_PREFS, true);
    }

    public static void setIPConnectOpen(boolean isOpen) {
        getPreferences().edit().putBoolean(IP_CONNECT_OPEN_PREFS, isOpen).commit();
    }

    public static boolean isCanIPConnect() {
        return getPreferences().getBoolean(CAN_IP_CONNECT_PREFS, false);
    }

    public static void setCanIPConnect(boolean canIPConnect) {
        getPreferences().edit().putBoolean(CAN_IP_CONNECT_PREFS, canIPConnect).commit();
    }

    public static void setApiEnvironment(String env) {
        getPreferences().edit().putString(API_ENVIRONMENT_PREFS, API_ENV_PRO).commit();
    }

    public static String getCurrentIp() {
        return getPreferences().getString(CURRENT_TEST_IP, "");
    }

    public static void setCurrentIp(String ip) {
        if (!TextUtils.isEmpty(ip)) {
            getPreferences().edit().putString(CURRENT_TEST_IP, ip).commit();
        }
    }

    public static void removeCurrentIp() {
        getPreferences().edit().remove(CURRENT_TEST_IP).commit();
    }

    public static String getApiEnvironment() {
        return getPreferences().getString(API_ENVIRONMENT_PREFS, API_ENV_PRO);
    }

    public static Boolean isApiProduction() {
        return Boolean.valueOf(getPreferences().getString(API_ENVIRONMENT_PREFS, API_ENV_PRO)
                .equals(API_ENV_PRO));
    }

    private static SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
    }

    public static void startDebugActivity(Toolbar toolbar, final Activity activity) {
        toolbar.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (System.currentTimeMillis() - BlackTech.sCurrentTime < 500) {
                    BlackTech.sTimes++;
                } else {
                    BlackTech.sTimes = 1;
                }
                BlackTech.sCurrentTime = System.currentTimeMillis();
                if (BlackTech.sTimes == 5) {
                    BlackTech.sTimes = 0;
                    ChangeEnvironmentActivity.comeOnBaby(activity);
                }
            }
        });
    }
}
