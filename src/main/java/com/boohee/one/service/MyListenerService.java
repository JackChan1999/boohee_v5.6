package com.boohee.one.service;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

@TargetApi(18)
public class MyListenerService extends NotificationListenerService {
    public void onNotificationPosted(StatusBarNotification sbn) {
    }

    public void onNotificationRemoved(StatusBarNotification sbn) {
    }

    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }
}
