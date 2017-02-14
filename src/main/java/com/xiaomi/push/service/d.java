package com.xiaomi.push.service;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Pair;

import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.service.XMPushService.e;

import java.util.ArrayList;
import java.util.List;

public class d extends HandlerThread {
    private volatile long    a = 0;
    private volatile boolean b = false;
    private volatile Handler c;
    private List<Pair<e, Long>> d = new ArrayList();

    public d(String str) {
        super(str);
    }

    public void a() {
        for (int i = 1; i < 15; i++) {
            a(i);
        }
    }

    public void a(int i) {
        if (this.c != null) {
            this.c.removeMessages(i);
        }
    }

    public void a(int i, Object obj) {
        if (this.c != null) {
            this.c.removeMessages(i, obj);
        }
    }

    public void a(e eVar, long j) {
        synchronized (this.d) {
            if (this.c != null) {
                Message obtain = Message.obtain();
                obtain.what = eVar.e;
                obtain.obj = eVar;
                this.c.sendMessageDelayed(obtain, j);
            } else {
                b.a("the job is pended, the controller is not ready.");
                this.d.add(new Pair(eVar, Long.valueOf(j)));
            }
        }
    }

    public boolean b() {
        return this.b && System.currentTimeMillis() - this.a > 600000;
    }

    public boolean b(int i) {
        return this.c != null ? this.c.hasMessages(i) : false;
    }

    protected void onLooperPrepared() {
        this.c = new e(this, getLooper());
        synchronized (this.d) {
            for (Pair pair : this.d) {
                b.a("executing the pending job.");
                a((e) pair.first, ((Long) pair.second).longValue());
            }
            this.d.clear();
        }
    }
}
