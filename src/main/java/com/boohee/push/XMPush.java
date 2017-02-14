package com.boohee.push;

import android.content.Context;
import android.util.Log;

import com.boohee.one.MyApplication;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

public class XMPush extends PushManager {
    public static final String APP_ID  = "2882303761517135649";
    public static final String APP_KEY = "5461713542649";
    public static final String TAG     = "XMPush";

    public void initPush(Context context) {
        Logger.setLogger(context, new LoggerInterface() {
            public void setTag(String tag) {
            }

            public void log(String content, Throwable t) {
                Log.d(XMPush.TAG, content, t);
            }

            public void log(String content) {
                Log.d(XMPush.TAG, content);
            }
        });
        if (shouldInitPush(context)) {
            MiPushClient.registerPush(context, APP_ID, APP_KEY);
        }
    }

    public void resumePush() {
        MiPushClient.resumePush(MyApplication.getContext(), null);
    }

    public void pausePush() {
        MiPushClient.pausePush(MyApplication.getContext(), null);
    }
}
