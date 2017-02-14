package com.xiaomi.mipush.sdk;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.xiaomi.xmpush.thrift.c;
import com.xiaomi.xmpush.thrift.n;

import java.util.List;

public class PushMessageHelper {
    public static final String KEY_COMMAND         = "key_command";
    public static final String KEY_MESSAGE         = "key_message";
    public static final int    MESSAGE_COMMAND     = 3;
    public static final int    MESSAGE_QUIT        = 4;
    public static final int    MESSAGE_RAW         = 1;
    public static final int    MESSAGE_SENDMESSAGE = 2;
    public static final String MESSAGE_TYPE        = "message_type";
    public static final int    PUSH_MODE_BROADCAST = 2;
    public static final int    PUSH_MODE_CALLBACK  = 1;
    private static      int    pushMode            = 0;

    public static MiPushCommandMessage generateCommandMessage(String str, List<String> list, long
            j, String str2, String str3) {
        MiPushCommandMessage miPushCommandMessage = new MiPushCommandMessage();
        miPushCommandMessage.setCommand(str);
        miPushCommandMessage.setCommandArguments(list);
        miPushCommandMessage.setResultCode(j);
        miPushCommandMessage.setReason(str2);
        miPushCommandMessage.setCategory(str3);
        return miPushCommandMessage;
    }

    public static MiPushMessage generateMessage(n nVar, c cVar, boolean z) {
        MiPushMessage miPushMessage = new MiPushMessage();
        miPushMessage.setMessageId(nVar.c());
        if (!TextUtils.isEmpty(nVar.j())) {
            miPushMessage.setMessageType(1);
            miPushMessage.setAlias(nVar.j());
        } else if (!TextUtils.isEmpty(nVar.h())) {
            miPushMessage.setMessageType(2);
            miPushMessage.setTopic(nVar.h());
        } else if (TextUtils.isEmpty(nVar.r())) {
            miPushMessage.setMessageType(0);
        } else {
            miPushMessage.setMessageType(3);
            miPushMessage.setUserAccount(nVar.r());
        }
        miPushMessage.setCategory(nVar.p());
        if (nVar.l() != null) {
            miPushMessage.setContent(nVar.l().f());
        }
        if (cVar != null) {
            if (TextUtils.isEmpty(miPushMessage.getMessageId())) {
                miPushMessage.setMessageId(cVar.b());
            }
            if (TextUtils.isEmpty(miPushMessage.getTopic())) {
                miPushMessage.setTopic(cVar.f());
            }
            miPushMessage.setDescription(cVar.j());
            miPushMessage.setTitle(cVar.h());
            miPushMessage.setNotifyType(cVar.l());
            miPushMessage.setNotifyId(cVar.q());
            miPushMessage.setPassThrough(cVar.o());
            miPushMessage.setExtra(cVar.s());
        }
        miPushMessage.setNotified(z);
        return miPushMessage;
    }

    public static int getPushMode(Context context) {
        if (pushMode == 0) {
            if (isUseCallbackPushMode(context)) {
                setPushMode(1);
            } else {
                setPushMode(2);
            }
        }
        return pushMode;
    }

    private static boolean isIntentAvailable(Context context, Intent intent) {
        try {
            List queryBroadcastReceivers = context.getPackageManager().queryBroadcastReceivers
                    (intent, 32);
            return (queryBroadcastReceivers == null || queryBroadcastReceivers.isEmpty()) ? false
                    : true;
        } catch (Exception e) {
            return true;
        }
    }

    public static boolean isUseCallbackPushMode(Context context) {
        Intent intent = new Intent("com.xiaomi.mipush.RECEIVE_MESSAGE");
        intent.setClassName(context.getPackageName(), "com.xiaomi.mipush.sdk.PushServiceReceiver");
        return isIntentAvailable(context, intent);
    }

    public static void sendCommandMessageBroadcast(Context context, MiPushCommandMessage
            miPushCommandMessage) {
        Intent intent = new Intent("com.xiaomi.mipush.RECEIVE_MESSAGE");
        intent.setPackage(context.getPackageName());
        intent.putExtra(MESSAGE_TYPE, 3);
        intent.putExtra(KEY_COMMAND, miPushCommandMessage);
        new PushServiceReceiver().onReceive(context, intent);
    }

    public static void sendQuitMessageBroadcast(Context context) {
        Intent intent = new Intent("com.xiaomi.mipush.RECEIVE_MESSAGE");
        intent.setPackage(context.getPackageName());
        intent.putExtra(MESSAGE_TYPE, 4);
        new PushServiceReceiver().onReceive(context, intent);
    }

    private static void setPushMode(int i) {
        pushMode = i;
    }
}
