package com.zxinsight.common.http;

import android.util.Log;

import com.zxinsight.common.util.l;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class v {
    public static final int                    a = (Runtime.getRuntime().availableProcessors() + 1);
    private             BlockingQueue<Request> b = new PriorityBlockingQueue();
    private             AtomicInteger          c = new AtomicInteger(0);
    private             int                    d = a;
    private             r[]                    e = null;

    protected v(int i) {
        this.d = i;
    }

    private final void c() {
        this.e = new r[this.d];
        for (int i = 0; i < this.d; i++) {
            this.e[i] = new r(this.b);
            this.e[i].start();
        }
    }

    public void a() {
        b();
        c();
    }

    public void b() {
        if (l.b(this.e)) {
            for (r a : this.e) {
                a.a();
            }
        }
    }

    public void a(Request request) {
        if (this.b.contains(request)) {
            Log.d("RequestQueue", "### 请求队列中已经含有");
            return;
        }
        request.a(d());
        this.b.add(request);
    }

    private int d() {
        return this.c.incrementAndGet();
    }
}
