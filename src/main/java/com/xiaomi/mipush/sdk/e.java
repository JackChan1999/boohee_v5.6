package com.xiaomi.mipush.sdk;

import android.content.Context;
import android.text.TextUtils;

import com.xiaomi.xmpush.thrift.a;
import com.xiaomi.xmpush.thrift.d;
import com.xiaomi.xmpush.thrift.g;
import com.xiaomi.xmpush.thrift.h;
import com.xiaomi.xmpush.thrift.i;
import com.xiaomi.xmpush.thrift.k;
import com.xiaomi.xmpush.thrift.m;
import com.xiaomi.xmpush.thrift.n;
import com.xiaomi.xmpush.thrift.p;
import com.xiaomi.xmpush.thrift.r;
import com.xiaomi.xmpush.thrift.t;
import com.xiaomi.xmpush.thrift.u;

import java.nio.ByteBuffer;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.thrift.b;
import org.apache.thrift.f;

public class e {
    private static final byte[] a = new byte[]{(byte) 100, (byte) 23, (byte) 84, (byte) 114,
            (byte) 72, (byte) 0, (byte) 4, (byte) 97, (byte) 73, (byte) 97, (byte) 2, (byte) 52,
            (byte) 84, (byte) 102, (byte) 18, (byte) 32};

    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] a = new int[a.values().length];

        static {
            try {
                a[a.a.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                a[a.b.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                a[a.c.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                a[a.d.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                a[a.e.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                a[a.f.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                a[a.g.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                a[a.h.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                a[a.i.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                a[a.j.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
        }
    }

    protected static <T extends b<T, ?>> h a(Context context, T t, a aVar) {
        return a(context, t, aVar, !aVar.equals(a.a), context.getPackageName(), a.a(context).c());
    }

    protected static <T extends b<T, ?>> h a(Context context, T t, a aVar, boolean z, String str,
                                             String str2) {
        byte[] a = u.a(t);
        if (a == null) {
            com.xiaomi.channel.commonutils.logger.b.a("invoke convertThriftObjectToBytes method, " +
                    "return null.");
            return null;
        }
        h hVar = new h();
        if (z) {
            String f = a.a(context).f();
            if (TextUtils.isEmpty(f)) {
                com.xiaomi.channel.commonutils.logger.b.a("regSecret is empty, return null");
                return null;
            }
            try {
                a = b(com.xiaomi.channel.commonutils.string.a.a(f), a);
            } catch (Exception e) {
                com.xiaomi.channel.commonutils.logger.b.d("encryption error. ");
            }
        }
        d dVar = new d();
        dVar.a = 5;
        dVar.b = "fakeid";
        hVar.a(dVar);
        hVar.a(ByteBuffer.wrap(a));
        hVar.a(aVar);
        hVar.c(true);
        hVar.b(str);
        hVar.a(z);
        hVar.a(str2);
        return hVar;
    }

    private static Cipher a(byte[] bArr, int i) {
        Key secretKeySpec = new SecretKeySpec(bArr, "AES");
        AlgorithmParameterSpec ivParameterSpec = new IvParameterSpec(a);
        Cipher instance = Cipher.getInstance("AES/CBC/PKCS5Padding");
        instance.init(i, secretKeySpec, ivParameterSpec);
        return instance;
    }

    protected static b a(Context context, h hVar) {
        if (hVar.c()) {
            try {
                byte[] a = a(com.xiaomi.channel.commonutils.string.a.a(a.a(context).f()), hVar.f());
            } catch (Throwable e) {
                throw new f("the aes decrypt failed.", e);
            }
        }
        a = hVar.f();
        b a2 = a(hVar.a());
        if (a2 != null) {
            u.a(a2, a);
        }
        return a2;
    }

    private static b a(a aVar) {
        switch (AnonymousClass1.a[aVar.ordinal()]) {
            case 1:
                return new k();
            case 2:
                return new r();
            case 3:
                return new p();
            case 4:
                return new t();
            case 5:
                return new n();
            case 6:
                return new com.xiaomi.xmpush.thrift.e();
            case 7:
                return new g();
            case 8:
                return new m();
            case 9:
                return new i();
            case 10:
                return new g();
            default:
                return null;
        }
    }

    public static byte[] a(byte[] bArr, byte[] bArr2) {
        return a(bArr, 2).doFinal(bArr2);
    }

    public static byte[] b(byte[] bArr, byte[] bArr2) {
        return a(bArr, 1).doFinal(bArr2);
    }
}
