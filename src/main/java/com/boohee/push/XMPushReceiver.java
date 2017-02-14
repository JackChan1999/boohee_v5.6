package com.boohee.push;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.boohee.one.MyApplication;
import com.boohee.one.R;
import com.boohee.one.pedometer.StepCounterUtil;
import com.boohee.utility.BooheeScheme;
import com.boohee.utility.Config;
import com.boohee.utils.BadgeUtils;
import com.boohee.utils.Helper;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class XMPushReceiver extends PushMessageReceiver {
    public static final String TAG = XMPushReceiver.class.getSimpleName();

    public void onNotificationMessageArrived(Context context, MiPushMessage message) {
        Helper.showLog(TAG, "onNotificationMessageArrived");
        StepCounterUtil.startStepService(context);
        if (message != null && !TextUtils.isEmpty(message.getContent())) {
            try {
                BadgeUtils.setIconBadge(context, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onNotificationMessageClicked(Context context, MiPushMessage miPushMessage) {
        Helper.showLog(TAG, "onNotificationMessageClicked");
        if (miPushMessage != null && !TextUtils.isEmpty(miPushMessage.getContent())) {
            try {
                handleMessage(context, miPushMessage.getContent().trim());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void onCommandResult(Context context, MiPushCommandMessage message) {
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        if (arguments != null && MiPushClient.COMMAND_REGISTER.equals(command) && arguments.size
                () == 1) {
            String regId = (String) arguments.get(0);
            Helper.showLog("REGID", regId);
            if (message.getResultCode() == 0) {
                PushManager.getInstance().saveToken(regId);
                setDefaultTags();
                delTags();
            }
        }
    }

    private void setDefaultTags() {
        MiPushClient.subscribe(MyApplication.getContext(), Config.getVersionName(), null);
    }

    private void delTags() {
        List<String> allTags = MiPushClient.getAllTopic(MyApplication.getContext());
        String versionName = Config.getVersionName();
        for (String tag : allTags) {
            if (!(TextUtils.isEmpty(tag) || versionName.equals(tag))) {
                MiPushClient.unsubscribe(MyApplication.getContext(), tag, null);
            }
        }
    }

    private void handleMessage(Context context, String msg) throws JSONException {
        JSONObject obj = new JSONObject(msg);
        String url = null;
        String title = context.getResources().getString(R.string.c3);
        if (obj.has("title")) {
            title = obj.getString("title");
        }
        if (obj.has("url")) {
            url = obj.getString("url");
        }
        Intent intent = new Intent();
        intent.setFlags(268435456);
        BooheeScheme.handleUrl(context, url, title, intent, false);
    }
}
