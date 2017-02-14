package com.meiqia.core.a.a.b;

import com.boohee.utils.Coder;
import com.meiqia.core.a.a.c.d;
import com.meiqia.core.a.a.d.e;
import com.meiqia.core.a.a.e.a;
import com.meiqia.core.a.a.e.b;
import com.meiqia.core.a.a.e.c;
import com.meiqia.core.a.a.e.f;
import com.meiqia.core.a.a.e.i;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class h extends g {
    private static final byte[]  j = new byte[]{(byte) -1, (byte) 0};
    private              boolean i = false;
    private final        Random  k = new Random();

    private static byte[] a(String str) {
        try {
            long parseLong = Long.parseLong(str.replaceAll("[^0-9]", ""));
            long length = (long) (str.split(" ").length - 1);
            if (length == 0) {
                throw new d("invalid Sec-WebSocket-Key (/key2/)");
            }
            parseLong = new Long(parseLong / length).longValue();
            return new byte[]{(byte) ((int) (parseLong >> 24)), (byte) ((int) ((parseLong << 8)
                    >> 24)), (byte) ((int) ((parseLong << 16) >> 24)), (byte) ((int) ((parseLong
                    << 24) >> 24))};
        } catch (NumberFormatException e) {
            throw new d("invalid Sec-WebSocket-Key (/key1/ or /key2/)");
        }
    }

    public static byte[] a(String str, String str2, byte[] bArr) {
        byte[] a = a(str);
        byte[] a2 = a(str2);
        try {
            return MessageDigest.getInstance(Coder.KEY_MD5).digest(new byte[]{a[0], a[1], a[2],
                    a[3], a2[0], a2[1], a2[2], a2[3], bArr[0], bArr[1], bArr[2], bArr[3],
                    bArr[4], bArr[5], bArr[6], bArr[7]});
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static String e() {
        Random random = new Random();
        long nextInt = (long) (random.nextInt(12) + 1);
        String l = Long.toString(((long) (random.nextInt(Math.abs(new Long(4294967295L / nextInt)
                .intValue())) + 1)) * nextInt);
        int nextInt2 = random.nextInt(12) + 1;
        for (int i = 0; i < nextInt2; i++) {
            int abs = Math.abs(random.nextInt(l.length()));
            char nextInt3 = (char) (random.nextInt(95) + 33);
            if (nextInt3 >= '0' && nextInt3 <= '9') {
                nextInt3 = (char) (nextInt3 - 15);
            }
            l = abs;
        }
        String str = l;
        for (int i2 = 0; ((long) i2) < nextInt; i2++) {
            str = Math.abs(random.nextInt(str.length() - 1) + 1);
        }
        return str;
    }

    public c a(a aVar) {
        return (aVar.b("Upgrade").equals("WebSocket") && aVar.b("Connection").contains("Upgrade")
                && aVar.b("Sec-WebSocket-Key1").length() > 0 && !aVar.b("Sec-WebSocket-Key2")
                .isEmpty() && aVar.c("Origin")) ? c.MATCHED : c.NOT_MATCHED;
    }

    public c a(a aVar, com.meiqia.core.a.a.e.h hVar) {
        if (this.i) {
            return c.NOT_MATCHED;
        }
        try {
            if (!hVar.b("Sec-WebSocket-Origin").equals(aVar.b("Origin")) || !a((f) hVar)) {
                return c.NOT_MATCHED;
            }
            byte[] c = hVar.c();
            if (c != null && c.length != 0) {
                return Arrays.equals(c, a(aVar.b("Sec-WebSocket-Key1"), aVar.b
                        ("Sec-WebSocket-Key2"), aVar.c())) ? c.MATCHED : c.NOT_MATCHED;
            } else {
                throw new com.meiqia.core.a.a.c.a();
            }
        } catch (Throwable e) {
            throw new RuntimeException("bad handshakerequest", e);
        }
    }

    public b a(b bVar) {
        bVar.a("Upgrade", "WebSocket");
        bVar.a("Connection", "Upgrade");
        bVar.a("Sec-WebSocket-Key1", e());
        bVar.a("Sec-WebSocket-Key2", e());
        if (!bVar.c("Origin")) {
            bVar.a("Origin", "random" + this.k.nextInt());
        }
        byte[] bArr = new byte[8];
        this.k.nextBytes(bArr);
        bVar.a(bArr);
        return bVar;
    }

    public c a(a aVar, i iVar) {
        iVar.a("WebSocket Protocol Handshake");
        iVar.a("Upgrade", "WebSocket");
        iVar.a("Connection", aVar.b("Connection"));
        iVar.a("Sec-WebSocket-Origin", aVar.b("Origin"));
        iVar.a("Sec-WebSocket-Location", "ws://" + aVar.b("Host") + aVar.a());
        String b = aVar.b("Sec-WebSocket-Key1");
        String b2 = aVar.b("Sec-WebSocket-Key2");
        byte[] c = aVar.c();
        if (b == null || b2 == null || c == null || c.length != 8) {
            throw new d("Bad keys");
        }
        iVar.a(a(b, b2, c));
        return iVar;
    }

    public ByteBuffer a(com.meiqia.core.a.a.d.d dVar) {
        return dVar.f() == e.CLOSING ? ByteBuffer.wrap(j) : super.a(dVar);
    }

    public b b() {
        return b.ONEWAY;
    }

    public a c() {
        return new h();
    }

    public List<com.meiqia.core.a.a.d.d> c(ByteBuffer byteBuffer) {
        byteBuffer.mark();
        List<com.meiqia.core.a.a.d.d> e = super.e(byteBuffer);
        if (e == null) {
            byteBuffer.reset();
            e = this.g;
            this.f = true;
            if (this.h == null) {
                this.h = ByteBuffer.allocate(2);
                if (byteBuffer.remaining() > this.h.remaining()) {
                    throw new com.meiqia.core.a.a.c.c();
                }
                this.h.put(byteBuffer);
                if (this.h.hasRemaining()) {
                    this.g = new LinkedList();
                } else if (Arrays.equals(this.h.array(), j)) {
                    e.add(new com.meiqia.core.a.a.d.b(1000));
                } else {
                    throw new com.meiqia.core.a.a.c.c();
                }
            }
            throw new com.meiqia.core.a.a.c.c();
        }
        return e;
    }

    public f d(ByteBuffer byteBuffer) {
        f a = a.a(byteBuffer, this.d);
        if ((a.c("Sec-WebSocket-Key1") || this.d == com.meiqia.core.a.a.c.CLIENT) && !a.c
                ("Sec-WebSocket-Version")) {
            byte[] bArr = new byte[(this.d == com.meiqia.core.a.a.c.SERVER ? 8 : 16)];
            try {
                byteBuffer.get(bArr);
                a.a(bArr);
            } catch (BufferUnderflowException e) {
                throw new com.meiqia.core.a.a.c.a(byteBuffer.capacity() + 16);
            }
        }
        return a;
    }
}
