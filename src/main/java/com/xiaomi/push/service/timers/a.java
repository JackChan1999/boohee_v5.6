package com.xiaomi.push.service.timers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.SystemClock;

import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.service.y;
import com.xiaomi.smack.j;

public class a {
    private static volatile long          c = 0;
    private                 PendingIntent a = null;
    private                 Context       b = null;

    public a(Context context) {
        this.b = context;
    }

    private void a(AlarmManager alarmManager, long j, PendingIntent pendingIntent) {
        try {
            AlarmManager.class.getMethod("setExact", new Class[]{Integer.TYPE, Long.TYPE,
                    PendingIntent.class}).invoke(alarmManager, new Object[]{Integer.valueOf(0),
                    Long.valueOf(j), pendingIntent});
        } catch (Throwable e) {
            b.a(e);
        }
    }

    public synchronized void a() {
        if (this.a != null) {
            ((AlarmManager) this.b.getSystemService("alarm")).cancel(this.a);
            this.a = null;
            b.c("unregister timer");
            c = 0;
        }
    }

    public synchronized void a(Intent intent, long j) {
        if (this.a == null) {
            AlarmManager alarmManager = (AlarmManager) this.b.getSystemService("alarm");
            this.a = PendingIntent.getBroadcast(this.b, 0, intent, 0);
            if (VERSION.SDK_INT >= 19) {
                a(alarmManager, j, this.a);
            } else {
                alarmManager.set(0, j, this.a);
            }
            b.c("register timer " + c);
        }
    }

    public synchronized void a(boolean z) {
        Intent intent = new Intent(y.o);
        intent.setPackage(this.b.getPackageName());
        long c = (long) j.c();
        if (z || c == 0) {
            c = (c - (SystemClock.elapsedRealtime() % c)) + System.currentTimeMillis();
        } else {
            c += c;
            if (c < System.currentTimeMillis()) {
                c = c + System.currentTimeMillis();
            }
        }
        a(intent, c);
    }

    public synchronized boolean b() {
        return this.a != null;
    }
}
