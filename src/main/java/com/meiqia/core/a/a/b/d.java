package com.meiqia.core.a.a.b;

import com.meiqia.core.a.a.c.c;
import com.meiqia.core.a.a.d.e;
import com.meiqia.core.a.a.e.b;
import com.meiqia.core.a.a.e.f;
import com.meiqia.core.a.a.e.h;
import com.meiqia.core.a.a.e.i;
import com.meiqia.core.a.a.f.a;
import com.tencent.tinker.android.dx.instruction.Opcodes;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class d extends a {
    static final /* synthetic */ boolean f = (!d.class.desiredAssertionStatus());
    private ByteBuffer g;
    private       com.meiqia.core.a.a.d.d h = null;
    private final Random                  i = new Random();

    private byte a(e eVar) {
        if (eVar == e.CONTINUOUS) {
            return (byte) 0;
        }
        if (eVar == e.TEXT) {
            return (byte) 1;
        }
        if (eVar == e.BINARY) {
            return (byte) 2;
        }
        if (eVar == e.CLOSING) {
            return (byte) 8;
        }
        if (eVar == e.PING) {
            return (byte) 9;
        }
        if (eVar == e.PONG) {
            return (byte) 10;
        }
        throw new RuntimeException("Don't know how to handle " + eVar.toString());
    }

    private e a(byte b) {
        switch (b) {
            case (byte) 0:
                return e.CONTINUOUS;
            case (byte) 1:
                return e.TEXT;
            case (byte) 2:
                return e.BINARY;
            case (byte) 8:
                return e.CLOSING;
            case (byte) 9:
                return e.PING;
            case (byte) 10:
                return e.PONG;
            default:
                throw new c("unknow optcode " + ((short) b));
        }
    }

    private String a(String str) {
        String str2 = str.trim() + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
        try {
            return a.a(MessageDigest.getInstance("SHA1").digest(str2.getBytes()));
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] a(long j, int i) {
        byte[] bArr = new byte[i];
        int i2 = (i * 8) - 8;
        for (int i3 = 0; i3 < i; i3++) {
            bArr[i3] = (byte) ((int) (j >>> (i2 - (i3 * 8))));
        }
        return bArr;
    }

    public static int b(f fVar) {
        int i = -1;
        String b = fVar.b("Sec-WebSocket-Version");
        if (b.length() > 0) {
            try {
                i = new Integer(b.trim()).intValue();
            } catch (NumberFormatException e) {
            }
        }
        return i;
    }

    public c a(com.meiqia.core.a.a.e.a aVar) {
        int b = b(aVar);
        return (b == 7 || b == 8) ? a((f) aVar) ? c.MATCHED : c.NOT_MATCHED : c.NOT_MATCHED;
    }

    public c a(com.meiqia.core.a.a.e.a aVar, h hVar) {
        if (!aVar.c("Sec-WebSocket-Key") || !hVar.c("Sec-WebSocket-Accept")) {
            return c.NOT_MATCHED;
        }
        return a(aVar.b("Sec-WebSocket-Key")).equals(hVar.b("Sec-WebSocket-Accept")) ? c.MATCHED
                : c.NOT_MATCHED;
    }

    public b a(b bVar) {
        bVar.a("Upgrade", "websocket");
        bVar.a("Connection", "Upgrade");
        bVar.a("Sec-WebSocket-Version", "8");
        byte[] bArr = new byte[16];
        this.i.nextBytes(bArr);
        bVar.a("Sec-WebSocket-Key", a.a(bArr));
        return bVar;
    }

    public com.meiqia.core.a.a.e.c a(com.meiqia.core.a.a.e.a aVar, i iVar) {
        iVar.a("Upgrade", "websocket");
        iVar.a("Connection", aVar.b("Connection"));
        iVar.a("Switching Protocols");
        String b = aVar.b("Sec-WebSocket-Key");
        if (b == null) {
            throw new com.meiqia.core.a.a.c.d("missing Sec-WebSocket-Key");
        }
        iVar.a("Sec-WebSocket-Accept", a(b));
        return iVar;
    }

    public ByteBuffer a(com.meiqia.core.a.a.d.d dVar) {
        int i = -128;
        int i2 = 0;
        ByteBuffer c = dVar.c();
        int i3 = this.d == com.meiqia.core.a.a.c.CLIENT ? 1 : 0;
        int i4 = c.remaining() <= Opcodes.NEG_LONG ? 1 : c.remaining() <= 65535 ? 2 : 8;
        ByteBuffer allocate = ByteBuffer.allocate(((i3 != 0 ? 4 : 0) + ((i4 > 1 ? i4 + 1 : i4) +
                1)) + c.remaining());
        allocate.put((byte) (((byte) (dVar.d() ? -128 : 0)) | a(dVar.f())));
        byte[] a = a((long) c.remaining(), i4);
        if (f || a.length == i4) {
            if (i4 == 1) {
                byte b = a[0];
                if (i3 == 0) {
                    i = 0;
                }
                allocate.put((byte) (b | i));
            } else if (i4 == 2) {
                if (i3 == 0) {
                    i = 0;
                }
                allocate.put((byte) (i | 126));
                allocate.put(a);
            } else if (i4 == 8) {
                if (i3 == 0) {
                    i = 0;
                }
                allocate.put((byte) (i | 127));
                allocate.put(a);
            } else {
                throw new RuntimeException("Size representation not supported/specified");
            }
            if (i3 != 0) {
                ByteBuffer allocate2 = ByteBuffer.allocate(4);
                allocate2.putInt(this.i.nextInt());
                allocate.put(allocate2.array());
                while (c.hasRemaining()) {
                    allocate.put((byte) (c.get() ^ allocate2.get(i2 % 4)));
                    i2++;
                }
            } else {
                allocate.put(c);
            }
            if (f || allocate.remaining() == 0) {
                allocate.flip();
                return allocate;
            }
            throw new AssertionError(allocate.remaining());
        }
        throw new AssertionError();
    }

    public List<com.meiqia.core.a.a.d.d> a(String str, boolean z) {
        com.meiqia.core.a.a.d.c fVar = new com.meiqia.core.a.a.d.f();
        try {
            fVar.a(ByteBuffer.wrap(com.meiqia.core.a.a.f.c.a(str)));
            fVar.a(true);
            fVar.a(e.TEXT);
            fVar.b(z);
            return Collections.singletonList(fVar);
        } catch (Throwable e) {
            throw new com.meiqia.core.a.a.c.f(e);
        }
    }

    public void a() {
        this.g = null;
    }

    public b b() {
        return b.TWOWAY;
    }

    public a c() {
        return new d();
    }

    public List<com.meiqia.core.a.a.d.d> c(ByteBuffer byteBuffer) {
        List<com.meiqia.core.a.a.d.d> linkedList = new LinkedList();
        if (this.g != null) {
            try {
                byteBuffer.mark();
                int remaining = byteBuffer.remaining();
                int remaining2 = this.g.remaining();
                if (remaining2 > remaining) {
                    this.g.put(byteBuffer.array(), byteBuffer.position(), remaining);
                    byteBuffer.position(remaining + byteBuffer.position());
                    return Collections.emptyList();
                }
                this.g.put(byteBuffer.array(), byteBuffer.position(), remaining2);
                byteBuffer.position(byteBuffer.position() + remaining2);
                linkedList.add(e((ByteBuffer) this.g.duplicate().position(0)));
                this.g = null;
            } catch (e e) {
                this.g.limit();
                r0 = ByteBuffer.allocate(a(e.a()));
                if (f || r0.limit() > this.g.limit()) {
                    ByteBuffer allocate;
                    this.g.rewind();
                    allocate.put(this.g);
                    this.g = allocate;
                    return c(byteBuffer);
                }
                throw new AssertionError();
            }
        }
        while (byteBuffer.hasRemaining()) {
            byteBuffer.mark();
            try {
                linkedList.add(e(byteBuffer));
            } catch (e e2) {
                byteBuffer.reset();
                this.g = ByteBuffer.allocate(a(e2.a()));
                this.g.put(byteBuffer);
            }
        }
        return linkedList;
    }

    public com.meiqia.core.a.a.d.d e(ByteBuffer byteBuffer) {
        int i = 2;
        int i2 = 0;
        int remaining = byteBuffer.remaining();
        if (remaining < 2) {
            throw new e(this, 2);
        }
        byte b = byteBuffer.get();
        boolean z = (b >> 8) != 0;
        byte b2 = (byte) ((b & 127) >> 4);
        if (b2 != (byte) 0) {
            throw new c("bad rsv " + b2);
        }
        byte b3 = byteBuffer.get();
        int i3 = (b3 & -128) != 0 ? 1 : 0;
        int i4 = (byte) (b3 & 127);
        e a = a((byte) (b & 15));
        if (z || !(a == e.PING || a == e.PONG || a == e.CLOSING)) {
            int i5;
            if (i4 < 0 || i4 > Opcodes.NEG_LONG) {
                if (a == e.PING || a == e.PONG || a == e.CLOSING) {
                    throw new c("more than 125 octets");
                } else if (i4 == 126) {
                    if (remaining < 4) {
                        throw new e(this, 4);
                    }
                    byte[] bArr = new byte[3];
                    bArr[1] = byteBuffer.get();
                    bArr[2] = byteBuffer.get();
                    i4 = new BigInteger(bArr).intValue();
                    i = 4;
                } else if (remaining < 10) {
                    throw new e(this, 10);
                } else {
                    byte[] bArr2 = new byte[8];
                    for (i5 = 0; i5 < 8; i5++) {
                        bArr2[i5] = byteBuffer.get();
                    }
                    long longValue = new BigInteger(bArr2).longValue();
                    if (longValue > 2147483647L) {
                        throw new com.meiqia.core.a.a.c.e("Payloadsize is to big...");
                    }
                    i = 10;
                    i4 = (int) longValue;
                }
            }
            i5 = ((i3 != 0 ? 4 : 0) + i) + i4;
            if (remaining < i5) {
                throw new e(this, i5);
            }
            com.meiqia.core.a.a.d.d bVar;
            ByteBuffer allocate = ByteBuffer.allocate(a(i4));
            if (i3 != 0) {
                byte[] bArr3 = new byte[4];
                byteBuffer.get(bArr3);
                while (i2 < i4) {
                    allocate.put((byte) (byteBuffer.get() ^ bArr3[i2 % 4]));
                    i2++;
                }
            } else {
                allocate.put(byteBuffer.array(), byteBuffer.position(), allocate.limit());
                byteBuffer.position(byteBuffer.position() + allocate.limit());
            }
            if (a == e.CLOSING) {
                bVar = new com.meiqia.core.a.a.d.b();
            } else {
                bVar = new com.meiqia.core.a.a.d.f();
                bVar.a(z);
                bVar.a(a);
            }
            allocate.flip();
            bVar.a(allocate);
            return bVar;
        }
        throw new c("control frames may no be fragmented");
    }
}
