package com.meiqia.core.a.a.d;

import com.meiqia.core.a.a.f.c;

import java.nio.ByteBuffer;

import org.java_websocket.framing.CloseFrame;

public class b extends f implements a {
    static final ByteBuffer a = ByteBuffer.allocate(0);
    private int    f;
    private String g;

    public b() {
        super(e.CLOSING);
        a(true);
    }

    public b(int i) {
        super(e.CLOSING);
        a(true);
        a(i, "");
    }

    public b(int i, String str) {
        super(e.CLOSING);
        a(true);
        a(i, str);
    }

    private void a(int i, String str) {
        String str2 = str == null ? "" : str;
        if (i == CloseFrame.TLS_ERROR) {
            str2 = "";
            i = CloseFrame.NOCODE;
        }
        if (i != CloseFrame.NOCODE) {
            byte[] a = c.a(str2);
            ByteBuffer allocate = ByteBuffer.allocate(4);
            allocate.putInt(i);
            allocate.position(2);
            ByteBuffer allocate2 = ByteBuffer.allocate(a.length + 2);
            allocate2.put(allocate);
            allocate2.put(a);
            allocate2.rewind();
            a(allocate2);
        } else if (str2.length() > 0) {
            throw new com.meiqia.core.a.a.c.b(1002, "A close frame must have a closecode if it " +
                    "has a reason");
        }
    }

    private void g() {
        this.f = CloseFrame.NOCODE;
        ByteBuffer c = super.c();
        c.mark();
        if (c.remaining() >= 2) {
            ByteBuffer allocate = ByteBuffer.allocate(4);
            allocate.position(2);
            allocate.putShort(c.getShort());
            allocate.position(0);
            this.f = allocate.getInt();
            if (this.f == CloseFrame.ABNORMAL_CLOSE || this.f == CloseFrame.TLS_ERROR || this.f
                    == CloseFrame.NOCODE || this.f > 4999 || this.f < 1000 || this.f == 1004) {
                throw new com.meiqia.core.a.a.c.c("closecode must not be sent over the wire: " +
                        this.f);
            }
        }
        c.reset();
    }

    private void h() {
        if (this.f == CloseFrame.NOCODE) {
            this.g = c.a(super.c());
            return;
        }
        ByteBuffer c = super.c();
        int position = c.position();
        try {
            c.position(c.position() + 2);
            this.g = c.a(c);
            c.position(position);
        } catch (Throwable e) {
            throw new com.meiqia.core.a.a.c.c(e);
        } catch (Throwable th) {
            c.position(position);
        }
    }

    public int a() {
        return this.f;
    }

    public void a(ByteBuffer byteBuffer) {
        super.a(byteBuffer);
        g();
        h();
    }

    public String b() {
        return this.g;
    }

    public ByteBuffer c() {
        return this.f == CloseFrame.NOCODE ? a : super.c();
    }

    public String toString() {
        return super.toString() + "code: " + this.f;
    }
}
