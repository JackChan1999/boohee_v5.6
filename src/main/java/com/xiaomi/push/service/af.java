package com.xiaomi.push.service;

import com.xiaomi.channel.commonutils.misc.b.b;
import com.xiaomi.smack.util.h;
import com.xiaomi.smack.util.i;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class af {
    private static af      d = new af();
    private        List<a> a = new ArrayList();
    private com.xiaomi.push.protobuf.a.a b;
    private b                            c;

    public static abstract class a {
        public void a(com.xiaomi.push.protobuf.a.a aVar) {
        }

        public void a(com.xiaomi.push.protobuf.b.a aVar) {
        }
    }

    static {
        d.f();
    }

    private af() {
    }

    public static af a() {
        return d;
    }

    private void e() {
        if (this.c == null) {
            this.c = new ag(this);
            i.a(this.c);
        }
    }

    private void f() {
        Exception exception;
        Throwable th;
        InputStream inputStream = null;
        try {
            InputStream bufferedInputStream;
            if (this.b != null) {
                bufferedInputStream = new BufferedInputStream(h.a().openFileInput("XMCloudCfg"));
                try {
                    this.b = com.xiaomi.push.protobuf.a.a.c(com.google.protobuf.micro.a.a
                            (bufferedInputStream));
                    bufferedInputStream.close();
                } catch (Exception e) {
                    Exception exception2 = e;
                    inputStream = bufferedInputStream;
                    exception = exception2;
                    try {
                        com.xiaomi.channel.commonutils.logger.b.a("save config failure: " +
                                exception.getMessage());
                        com.xiaomi.channel.commonutils.file.a.a(inputStream);
                    } catch (Throwable th2) {
                        th = th2;
                        com.xiaomi.channel.commonutils.file.a.a(inputStream);
                        throw th;
                    }
                } catch (Throwable th3) {
                    Throwable th4 = th3;
                    inputStream = bufferedInputStream;
                    th = th4;
                    com.xiaomi.channel.commonutils.file.a.a(inputStream);
                    throw th;
                }
            }
            bufferedInputStream = null;
            com.xiaomi.channel.commonutils.file.a.a(bufferedInputStream);
        } catch (Exception e2) {
            exception = e2;
            com.xiaomi.channel.commonutils.logger.b.a("save config failure: " + exception
                    .getMessage());
            com.xiaomi.channel.commonutils.file.a.a(inputStream);
        }
    }

    private void g() {
        try {
            if (this.b != null) {
                OutputStream bufferedOutputStream = new BufferedOutputStream(h.a().openFileOutput
                        ("XMCloudCfg", 0));
                com.google.protobuf.micro.b a = com.google.protobuf.micro.b.a(bufferedOutputStream);
                this.b.a(a);
                a.a();
                bufferedOutputStream.close();
            }
        } catch (Exception e) {
            com.xiaomi.channel.commonutils.logger.b.a("save config failure: " + e.getMessage());
        }
    }

    void a(com.xiaomi.push.protobuf.b.a aVar) {
        if (aVar.h() && aVar.g() > c()) {
            e();
        }
        synchronized (this) {
        }
        for (a a : (a[]) this.a.toArray(new a[this.a.size()])) {
            a.a(aVar);
        }
    }

    public synchronized void a(a aVar) {
        this.a.add(aVar);
    }

    synchronized void b() {
        this.a.clear();
    }

    int c() {
        return this.b != null ? this.b.c() : 0;
    }

    public com.xiaomi.push.protobuf.a.a d() {
        return this.b;
    }
}
