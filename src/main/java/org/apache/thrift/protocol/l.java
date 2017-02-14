package org.apache.thrift.protocol;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import org.apache.thrift.f;
import org.apache.thrift.transport.d;

public class l extends a {
    private static int f = 10000;
    private static int g = 10000;
    private static int h = 10000;
    private static int i = 10485760;
    private static int j = 104857600;

    public static class a extends org.apache.thrift.protocol.a.a {
        public a() {
            super(false, true);
        }

        public a(boolean z, boolean z2, int i) {
            super(z, z2, i);
        }

        public f a(d dVar) {
            f lVar = new l(dVar, this.a, this.b);
            if (this.c != 0) {
                lVar.c(this.c);
            }
            return lVar;
        }
    }

    public l(d dVar, boolean z, boolean z2) {
        super(dVar, z, z2);
    }

    public e k() {
        byte r = r();
        byte r2 = r();
        int t = t();
        if (t <= f) {
            return new e(r, r2, t);
        }
        throw new g(3, "Thrift map size " + t + " out of range!");
    }

    public d m() {
        byte r = r();
        int t = t();
        if (t <= g) {
            return new d(r, t);
        }
        throw new g(3, "Thrift list size " + t + " out of range!");
    }

    public j o() {
        byte r = r();
        int t = t();
        if (t <= h) {
            return new j(r, t);
        }
        throw new g(3, "Thrift set size " + t + " out of range!");
    }

    public String w() {
        int t = t();
        if (t > i) {
            throw new g(3, "Thrift string size " + t + " out of range!");
        } else if (this.e.c() < t) {
            return b(t);
        } else {
            try {
                String str = new String(this.e.a(), this.e.b(), t, "UTF-8");
                this.e.a(t);
                return str;
            } catch (UnsupportedEncodingException e) {
                throw new f("JVM DOES NOT SUPPORT UTF-8");
            }
        }
    }

    public ByteBuffer x() {
        int t = t();
        if (t > j) {
            throw new g(3, "Thrift binary size " + t + " out of range!");
        }
        d(t);
        if (this.e.c() >= t) {
            ByteBuffer wrap = ByteBuffer.wrap(this.e.a(), this.e.b(), t);
            this.e.a(t);
            return wrap;
        }
        byte[] bArr = new byte[t];
        this.e.d(bArr, 0, t);
        return ByteBuffer.wrap(bArr);
    }
}
