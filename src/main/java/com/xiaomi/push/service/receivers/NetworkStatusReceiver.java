package com.xiaomi.push.service.receivers;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.channel.commonutils.network.d;
import com.xiaomi.mipush.sdk.a;
import com.xiaomi.mipush.sdk.g;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class NetworkStatusReceiver extends BroadcastReceiver {
    private static int                     a = 1;
    private static int                     b = 1;
    private static int                     c = 2;
    private static BlockingQueue<Runnable> d = new LinkedBlockingQueue();
    private static ThreadPoolExecutor      e = new ThreadPoolExecutor(a, b, (long) c, TimeUnit
            .SECONDS, d);

    private void a(Context context) {
        if (!(g.a(context).b() || !a.a(context).i() || a.a(context).n())) {
            try {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(context, "com.xiaomi.push.service" +
                        ".XMPushService"));
                intent.setAction("com.xiaomi.push.network_status_changed");
                context.startService(intent);
            } catch (Throwable e) {
                b.a(e);
            }
        }
        if (d.d(context) && g.a(context).f()) {
            g.a(context).c();
        }
    }

    public void onReceive(Context context, Intent intent) {
        e.execute(new a(this, context));
    }
}
