package com.xiaomi.mipush.sdk;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;

import com.xiaomi.channel.commonutils.logger.b;

import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageHandleService extends IntentService {
    private static ConcurrentLinkedQueue<a> a = new ConcurrentLinkedQueue();

    public MessageHandleService() {
        super("MessageHandleThread");
    }

    public static void addJob(a aVar) {
        if (aVar != null) {
            a.add(aVar);
        }
    }

    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            a aVar = (a) a.poll();
            if (aVar != null) {
                try {
                    PushMessageReceiver a = aVar.a();
                    Intent b = aVar.b();
                    MiPushCommandMessage miPushCommandMessage;
                    switch (b.getIntExtra(PushMessageHelper.MESSAGE_TYPE, 1)) {
                        case 1:
                            a a2 = f.a(this).a(b);
                            if (a2 == null) {
                                return;
                            }
                            if (a2 instanceof MiPushMessage) {
                                MiPushMessage miPushMessage = (MiPushMessage) a2;
                                if (!miPushMessage.isArrivedMessage()) {
                                    a.onReceiveMessage(this, miPushMessage);
                                }
                                if (miPushMessage.getPassThrough() == 1) {
                                    a.onReceivePassThroughMessage(this, miPushMessage);
                                    return;
                                } else if (miPushMessage.isNotified()) {
                                    a.onNotificationMessageClicked(this, miPushMessage);
                                    return;
                                } else {
                                    a.onNotificationMessageArrived(this, miPushMessage);
                                    return;
                                }
                            } else if (a2 instanceof MiPushCommandMessage) {
                                miPushCommandMessage = (MiPushCommandMessage) a2;
                                a.onCommandResult(this, miPushCommandMessage);
                                if (TextUtils.equals(miPushCommandMessage.getCommand(),
                                        MiPushClient.COMMAND_REGISTER)) {
                                    a.onReceiveRegisterResult(this, miPushCommandMessage);
                                    return;
                                }
                                return;
                            } else {
                                return;
                            }
                        case 3:
                            miPushCommandMessage = (MiPushCommandMessage) b.getSerializableExtra
                                    (PushMessageHelper.KEY_COMMAND);
                            a.onCommandResult(this, miPushCommandMessage);
                            if (TextUtils.equals(miPushCommandMessage.getCommand(), MiPushClient
                                    .COMMAND_REGISTER)) {
                                a.onReceiveRegisterResult(this, miPushCommandMessage);
                                return;
                            }
                            return;
                        case 4:
                            return;
                        default:
                            return;
                    }
                } catch (Throwable e) {
                    b.a(e);
                }
                b.a(e);
            }
        }
    }
}
