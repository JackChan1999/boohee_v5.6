package com.meiqia.core.a.a.d;

import com.meiqia.core.a.a.f.c;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class f implements c {
    protected static byte[] b = new byte[0];
    private   ByteBuffer a;
    protected boolean    c;
    protected e          d;
    protected boolean    e;

    public f(d dVar) {
        this.c = dVar.d();
        this.d = dVar.f();
        this.a = dVar.c();
        this.e = dVar.e();
    }

    public f(e eVar) {
        this.d = eVar;
        this.a = ByteBuffer.wrap(b);
    }

    public void a(e eVar) {
        this.d = eVar;
    }

    public void a(ByteBuffer byteBuffer) {
        this.a = byteBuffer;
    }

    public void a(boolean z) {
        this.c = z;
    }

    public void b(boolean z) {
        this.e = z;
    }

    public ByteBuffer c() {
        return this.a;
    }

    public boolean d() {
        return this.c;
    }

    public boolean e() {
        return this.e;
    }

    public e f() {
        return this.d;
    }

    public String toString() {
        return "Framedata{ optcode:" + f() + ", fin:" + d() + ", payloadlength:[pos:" + this.a
                .position() + ", len:" + this.a.remaining() + "], payload:" + Arrays.toString(c.a
                (new String(this.a.array()))) + "}";
    }
}
