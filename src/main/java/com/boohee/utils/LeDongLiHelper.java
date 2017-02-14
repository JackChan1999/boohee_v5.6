package com.boohee.utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;

import java.util.List;

public class LeDongLiHelper {
    public static final String BROADCAST_ACTION = "BOOHEE_RECEIVE_LEDONGLI_STEP";
    public static final String KEY_DATA_SOURCE  = "ledongli";
    public static final String KEY_LE_DONG_LI   = "KEY_LE_DONG_LI";
    public static final String PACKAGE_NAME     = "package_name";
    public static final String QUERY_STRING     = "query_ledongli_data";
    public static BroadcastReceiver receiver;
    public static CallBack          resultCallBack;

    public interface CallBack {
        void onFailed(String str);

        void onSuccess(int i);
    }

    public static void LoadLeDongLiSteps(Activity activity, CallBack callBack) {
        if (activity != null && callBack != null) {
            resultCallBack = callBack;
            Intent intent = new Intent("cn.ledongli.ldl.ledongliservice");
            intent.putExtra(PACKAGE_NAME, activity.getPackageName());
            intent.putExtra(QUERY_STRING, "step");
            Intent wrapperedIntent = createExplicitFromImplicitIntent(activity, intent);
            if (wrapperedIntent == null) {
                resultCallBack.onFailed("乐动力不在呀,请安装乐动力~~");
            } else if (SystemUtil.getAppVersionCode("cn.ledongli.ldl") <= 422) {
                resultCallBack.onFailed("乐动力版本过低，请更新到4.2.2以上的版本");
            } else {
                registerReciver(activity);
                activity.startService(wrapperedIntent);
            }
        }
    }

    public static Intent createExplicitFromImplicitIntent(Context context, Intent implicitIntent) {
        List<ResolveInfo> resolveInfo = context.getPackageManager().queryIntentServices
                (implicitIntent, 0);
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }
        ResolveInfo serviceInfo = (ResolveInfo) resolveInfo.get(0);
        ComponentName component = new ComponentName(serviceInfo.serviceInfo.packageName,
                serviceInfo.serviceInfo.name);
        Intent explicitIntent = new Intent(implicitIntent);
        explicitIntent.setComponent(component);
        return explicitIntent;
    }

    public static void registerReciver(Activity activity) {
        initReciever();
        activity.registerReceiver(receiver, new IntentFilter(BROADCAST_ACTION));
    }

    public static void unRegisterReciver(Activity activity) {
        if (receiver != null) {
            activity.unregisterReceiver(receiver);
            receiver = null;
        }
    }

    private static void initReciever() {
        if (receiver == null) {
            receiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    int step = intent.getExtras().getInt("step");
                    if (LeDongLiHelper.resultCallBack != null) {
                        LeDongLiHelper.resultCallBack.onSuccess(step);
                    }
                }
            };
        }
    }

    public static void destroy(Activity activity) {
        unRegisterReciver(activity);
        receiver = null;
        resultCallBack = null;
    }
}
