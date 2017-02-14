package com.zxinsight.common.http;

import android.os.Process;

import com.zxinsight.common.util.h;
import com.zxinsight.common.util.l;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

final class r extends Thread {
    private static w                 a = new w();
    private static a<String, byte[]> b = new o();
    private BlockingQueue<Request> c;
    private boolean d = false;

    public r(BlockingQueue<Request> blockingQueue) {
        this.c = blockingQueue;
    }

    public void run() {
        Process.setThreadPriority(10);
        while (!this.d) {
            try {
                Request request = (Request) this.c.take();
                if (!request.h()) {
                    a(request);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                if (this.d) {
                    return;
                }
            }
        }
    }

    private void a(Request request) {
        byte[] bArr = new byte[0];
        if (request.j() || !c(request)) {
            try {
                switch (s.a[request.c().ordinal()]) {
                    case 1:
                        bArr = z.a(request.b()).a(request.e()).a(request.l()).b(request.k()).e();
                        break;
                    case 2:
                        z a = z.b(request.b()).a(request.l()).b(request.k()).a(request.e());
                        if (request.f() == null) {
                            bArr = a.f().c("application/json", null).e();
                            break;
                        } else {
                            bArr = a.f().c("application/json", null).e(request.f().toString()).e();
                            break;
                        }
                }
                if (request.g() && l.b(bArr)) {
                    b.a(b(request), bArr);
                }
            } catch (RestException e) {
                if (request.i() > 0) {
                    request.b(request.i() - 1);
                    request.a(true);
                    a(request);
                    return;
                } else if (request.i() == 0) {
                    a.a(request);
                    return;
                } else {
                    return;
                }
            } catch (IOException e2) {
                if (request.i() > 0) {
                    request.b(request.i() - 1);
                    request.a(true);
                    a(request);
                    return;
                } else if (request.i() == 0) {
                    a.a(request);
                    return;
                } else {
                    return;
                }
            }
        }
        bArr = (byte[]) b.a(b(request));
        a.a(request, bArr);
    }

    private String b(Request request) {
        if (!l.b(request.b())) {
            return null;
        }
        if (h.c(request.f())) {
            return f.a(request.b() + request.f().toString());
        }
        return f.a(request.b());
    }

    private boolean c(Request request) {
        return request.g() && l.b(b(request)) && b.a(b(request)) != null;
    }

    public void a() {
        this.d = true;
        interrupt();
    }
}
