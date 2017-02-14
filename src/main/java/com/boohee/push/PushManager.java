package com.boohee.push;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.os.Process;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.boohee.api.MessengerApi;
import com.boohee.database.UserPreference;
import com.boohee.model.ModelName;
import com.boohee.one.MyApplication;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.JsonParams;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.utils.AccountUtils;

import java.util.List;

import org.json.JSONObject;

public abstract class PushManager {
    public static final  String         KEY_IS_BINDED = "KEY_IS_BINDED";
    public static final  String         KEY_REG_ID    = "KEY_REG_ID";
    protected static     UserPreference pref          = UserPreference.getInstance(MyApplication
            .getContext());
    private static final PushManager    pushManager   = new XMPush();

    public abstract void initPush(Context context);

    public abstract void pausePush();

    public abstract void resumePush();

    public static PushManager getInstance() {
        return pushManager;
    }

    public void unBindRegId(Context context) {
        handleRegId(context, false);
    }

    public void bindRegId(Context context) {
        boolean isBind = pref.getBoolean(KEY_IS_BINDED);
        boolean isVistor = AccountUtils.isVisitorAccount(context);
        if (!isBind && !isVistor) {
            handleRegId(context, true);
        }
    }

    private static void handleRegId(Context context, final boolean isBind) {
        String regId = pref.getString(KEY_REG_ID);
        if (!TextUtils.isEmpty(regId)) {
            JsonParams params = new JsonParams();
            params.put("token", UserPreference.getToken(context));
            params.put("pkg_name", "com.boohee.one");
            params.put("reg_id", regId);
            BooheeClient.build(BooheeClient.MESSENGER).post(isBind ? MessengerApi.API_BIND :
                    MessengerApi.API_UNBIND, params, new JsonCallback(context) {
                public void ok(JSONObject object, boolean hasError) {
                    PushManager.pref.putBoolean(PushManager.KEY_IS_BINDED, isBind);
                }

                public void fail(String message) {
                }
            }, context);
        }
    }

    public void saveToken(String token) {
        UserPreference.getInstance(MyApplication.getContext()).putString(KEY_REG_ID, token);
    }

    protected boolean shouldInitPush(Context context) {
        if (PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).getBoolean
                ("isReceivePush", true) && isMainProcess(context)) {
            return true;
        }
        return false;
    }

    protected boolean isMainProcess(Context context) {
        List<RunningAppProcessInfo> processInfos = ((ActivityManager) context.getSystemService
                (ModelName.ACTIVITY)).getRunningAppProcesses();
        String mainProcessName = context.getPackageName();
        int myPid = Process.myPid();
        for (RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
}
