package com.xiaomi.auth.service.talker;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class ServiceChecker {

    private static final class EmptyServiceConnection implements ServiceConnection {
        private EmptyServiceConnection() {
        }

        public void onServiceDisconnected(ComponentName name) {
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
        }
    }

    public static boolean isServiceSupport(Context context) {
        Intent intent = ServiceTalker.getAuthServiceIntent();
        ServiceConnection serviceConnection = new EmptyServiceConnection();
        boolean binded = context.bindService(intent, serviceConnection, 1);
        context.unbindService(serviceConnection);
        return binded;
    }
}
