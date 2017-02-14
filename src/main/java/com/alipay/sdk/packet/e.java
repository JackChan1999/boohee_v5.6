package com.alipay.sdk.packet;

import com.alipay.sdk.cons.a;
import com.alipay.sdk.encrypt.c;
import com.alipay.sdk.encrypt.d;
import com.alipay.sdk.util.i;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.Locale;

public final class e {
    private boolean a;
    private String b = i.c();

    public e(boolean z) {
        this.a = z;
    }

    public final c a(b bVar, boolean z) {
        if (bVar == null) {
            return null;
        }
        byte[] a;
        byte[] bytes = bVar.a.getBytes();
        byte[] bytes2 = bVar.b.getBytes();
        if (z) {
            try {
                bytes2 = c.a(bytes2);
            } catch (Exception e) {
                z = false;
            }
        }
        if (this.a) {
            byte[] a2 = d.a(this.b, a.b);
            bytes2 = com.alipay.sdk.encrypt.e.a(this.b, bytes2);
            a = a(bytes, a2, bytes2);
        } else {
            a = a(bytes, bytes2);
        }
        return new c(z, a);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.alipay.sdk.packet.b a(com.alipay.sdk.packet.c r7) {
        /*
        r6 = this;
        r0 = 0;
        r1 = new java.io.ByteArrayInputStream;	 Catch:{ Exception -> 0x005b, all -> 0x0068 }
        r2 = r7.b;	 Catch:{ Exception -> 0x005b, all -> 0x0068 }
        r1.<init>(r2);	 Catch:{ Exception -> 0x005b, all -> 0x0068 }
        r2 = 5;
        r2 = new byte[r2];	 Catch:{ Exception -> 0x007c, all -> 0x007a }
        r1.read(r2);	 Catch:{ Exception -> 0x007c, all -> 0x007a }
        r3 = new java.lang.String;	 Catch:{ Exception -> 0x007c, all -> 0x007a }
        r3.<init>(r2);	 Catch:{ Exception -> 0x007c, all -> 0x007a }
        r2 = java.lang.Integer.parseInt(r3);	 Catch:{ Exception -> 0x007c, all -> 0x007a }
        r3 = new byte[r2];	 Catch:{ Exception -> 0x007c, all -> 0x007a }
        r1.read(r3);	 Catch:{ Exception -> 0x007c, all -> 0x007a }
        r2 = new java.lang.String;	 Catch:{ Exception -> 0x007c, all -> 0x007a }
        r2.<init>(r3);	 Catch:{ Exception -> 0x007c, all -> 0x007a }
        r3 = 5;
        r3 = new byte[r3];	 Catch:{ Exception -> 0x007f, all -> 0x007a }
        r1.read(r3);	 Catch:{ Exception -> 0x007f, all -> 0x007a }
        r4 = new java.lang.String;	 Catch:{ Exception -> 0x007f, all -> 0x007a }
        r4.<init>(r3);	 Catch:{ Exception -> 0x007f, all -> 0x007a }
        r3 = java.lang.Integer.parseInt(r4);	 Catch:{ Exception -> 0x007f, all -> 0x007a }
        if (r3 <= 0) goto L_0x0085;
    L_0x0032:
        r3 = new byte[r3];	 Catch:{ Exception -> 0x007f, all -> 0x007a }
        r1.read(r3);	 Catch:{ Exception -> 0x007f, all -> 0x007a }
        r4 = r6.a;	 Catch:{ Exception -> 0x007f, all -> 0x007a }
        if (r4 == 0) goto L_0x0041;
    L_0x003b:
        r4 = r6.b;	 Catch:{ Exception -> 0x007f, all -> 0x007a }
        r3 = com.alipay.sdk.encrypt.e.b(r4, r3);	 Catch:{ Exception -> 0x007f, all -> 0x007a }
    L_0x0041:
        r4 = r7.a;	 Catch:{ Exception -> 0x007f, all -> 0x007a }
        if (r4 == 0) goto L_0x0083;
    L_0x0045:
        r3 = com.alipay.sdk.encrypt.c.b(r3);	 Catch:{ Exception -> 0x007f, all -> 0x007a }
        r4 = r3;
    L_0x004a:
        r3 = new java.lang.String;	 Catch:{ Exception -> 0x007f, all -> 0x007a }
        r3.<init>(r4);	 Catch:{ Exception -> 0x007f, all -> 0x007a }
    L_0x004f:
        r1.close();	 Catch:{ Exception -> 0x0058 }
        r1 = r3;
    L_0x0053:
        if (r2 != 0) goto L_0x0072;
    L_0x0055:
        if (r1 != 0) goto L_0x0072;
    L_0x0057:
        return r0;
    L_0x0058:
        r1 = move-exception;
        r1 = r3;
        goto L_0x0053;
    L_0x005b:
        r1 = move-exception;
        r1 = r0;
        r2 = r0;
    L_0x005e:
        if (r1 == 0) goto L_0x0081;
    L_0x0060:
        r1.close();	 Catch:{ Exception -> 0x0065 }
        r1 = r0;
        goto L_0x0053;
    L_0x0065:
        r1 = move-exception;
        r1 = r0;
        goto L_0x0053;
    L_0x0068:
        r1 = move-exception;
        r5 = r1;
        r1 = r0;
        r0 = r5;
    L_0x006c:
        if (r1 == 0) goto L_0x0071;
    L_0x006e:
        r1.close();	 Catch:{ Exception -> 0x0078 }
    L_0x0071:
        throw r0;
    L_0x0072:
        r0 = new com.alipay.sdk.packet.b;
        r0.<init>(r2, r1);
        goto L_0x0057;
    L_0x0078:
        r1 = move-exception;
        goto L_0x0071;
    L_0x007a:
        r0 = move-exception;
        goto L_0x006c;
    L_0x007c:
        r2 = move-exception;
        r2 = r0;
        goto L_0x005e;
    L_0x007f:
        r3 = move-exception;
        goto L_0x005e;
    L_0x0081:
        r1 = r0;
        goto L_0x0053;
    L_0x0083:
        r4 = r3;
        goto L_0x004a;
    L_0x0085:
        r3 = r0;
        goto L_0x004f;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alipay.sdk.packet.e.a(com.alipay.sdk.packet.c):com.alipay.sdk.packet.b");
    }

    private static byte[] a(String str, String str2) {
        return d.a(str, str2);
    }

    private static byte[] a(String str, byte[] bArr) {
        return com.alipay.sdk.encrypt.e.a(str, bArr);
    }

    private static byte[] b(String str, byte[] bArr) {
        return com.alipay.sdk.encrypt.e.b(str, bArr);
    }

    private static byte[] a(byte[]... bArr) {
        ByteArrayOutputStream byteArrayOutputStream;
        DataOutputStream dataOutputStream;
        Throwable th;
        int i = 0;
        byte[] bArr2 = null;
        if (!(bArr == null || bArr.length == 0)) {
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
                try {
                    dataOutputStream = new DataOutputStream(byteArrayOutputStream);
                    while (i < bArr.length) {
                        try {
                            int length = bArr[i].length;
                            dataOutputStream.write(String.format(Locale.getDefault(), "%05d", new Object[]{Integer.valueOf(length)}).getBytes());
                            dataOutputStream.write(bArr[i]);
                            i++;
                        } catch (Exception e) {
                        } catch (Throwable th2) {
                            th = th2;
                        }
                    }
                    dataOutputStream.flush();
                    bArr2 = byteArrayOutputStream.toByteArray();
                    try {
                        byteArrayOutputStream.close();
                    } catch (Exception e2) {
                    }
                    try {
                        dataOutputStream.close();
                    } catch (Exception e3) {
                    }
                } catch (Exception e4) {
                    dataOutputStream = null;
                    if (byteArrayOutputStream != null) {
                        try {
                            byteArrayOutputStream.close();
                        } catch (Exception e5) {
                        }
                    }
                    if (dataOutputStream != null) {
                        try {
                            dataOutputStream.close();
                        } catch (Exception e6) {
                        }
                    }
                    return bArr2;
                } catch (Throwable th3) {
                    Throwable th4 = th3;
                    dataOutputStream = null;
                    th = th4;
                    if (byteArrayOutputStream != null) {
                        try {
                            byteArrayOutputStream.close();
                        } catch (Exception e7) {
                        }
                    }
                    if (dataOutputStream != null) {
                        try {
                            dataOutputStream.close();
                        } catch (Exception e8) {
                        }
                    }
                    throw th;
                }
            } catch (Exception e9) {
                dataOutputStream = null;
                byteArrayOutputStream = null;
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
                return bArr2;
            } catch (Throwable th32) {
                byteArrayOutputStream = null;
                th = th32;
                dataOutputStream = null;
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
                throw th;
            }
        }
        return bArr2;
    }

    private static String a(int i) {
        return String.format(Locale.getDefault(), "%05d", new Object[]{Integer.valueOf(i)});
    }

    private static int a(String str) {
        return Integer.parseInt(str);
    }
}
