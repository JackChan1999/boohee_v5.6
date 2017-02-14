package com.boohee.one.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.text.TextUtils;

import com.baidu.location.LocationClientOption;

public class DaemonService extends Service {
    public static final String ACTION_SHUTDOWN = "com.boohee.one.step:action_shutdown_daemon";
    public static final String TAG             = DaemonService.class.getSimpleName();
    private AlarmManager  am;
    private boolean       isRunning;
    private PendingIntent pendingIntent;
    private volatile boolean sPower    = true;
    private          int     sleepTime = LocationClientOption.MIN_SCAN_SPAN_NETWORK;

    public void onCreate() {
        super.onCreate();
        this.pendingIntent = PendingIntent.getService(this, 291, new Intent(this, DaemonService
                .class), 134217728);
        this.am = (AlarmManager) getSystemService("alarm");
        this.am.setRepeating(2, 900000, a0.i2, this.pendingIntent);
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (!TextUtils.isEmpty(action)) {
                boolean z = true;
                switch (action.hashCode()) {
                    case 1301354127:
                        if (action.equals(ACTION_SHUTDOWN)) {
                            z = false;
                            break;
                        }
                        break;
                }
                switch (z) {
                    case false:
                        this.sPower = false;
                        if (!(this.am == null || this.pendingIntent == null)) {
                            this.am.cancel(this.pendingIntent);
                        }
                        this.sPower = false;
                        this.sleepTime = 1000000000;
                        stopSelf();
                        return 2;
                }
            }
        }
        if (!this.isRunning) {
            this.isRunning = true;
            new Thread(new Runnable() {
                public void run() {
                    while (DaemonService.this.sPower) {
                        if (System.currentTimeMillis() >= 123456789000000L) {
                            DaemonService.this.sPower = false;
                        }
                        DaemonService.this.startService(new Intent(DaemonService.this,
                                StepCounterService.class));
                        SystemClock.sleep((long) DaemonService.this.sleepTime);
                    }
                }
            }).start();
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
