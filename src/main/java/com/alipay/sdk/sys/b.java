package com.alipay.sdk.sys;

import android.content.Context;
import android.text.TextUtils;
import com.alipay.sdk.data.c;
import com.ta.utdid2.device.UTDevice;
import java.io.File;

public final class b {
    private static b b;
    public Context a;

    private b() {
    }

    public static b a() {
        if (b == null) {
            b = new b();
        }
        return b;
    }

    private Context d() {
        return this.a;
    }

    public final void a(Context context) {
        this.a = context.getApplicationContext();
    }

    private static c e() {
        return c.a();
    }

    public static boolean b() {
        String[] strArr = new String[]{"/system/xbin/", "/system/bin/", "/system/sbin/", "/sbin/", "/vendor/bin/"};
        int i = 0;
        while (i < strArr.length) {
            try {
                if (new File(strArr[i] + "su").exists()) {
                    String a = a(new String[]{"ls", "-l", strArr[i] + "su"});
                    if (TextUtils.isEmpty(a) || a.indexOf("root") == a.lastIndexOf("root")) {
                        return false;
                    }
                    return true;
                }
                i++;
            } catch (Exception e) {
            }
        }
        return false;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String a(java.lang.String[] r6) {
        /*
        r2 = "";
        r0 = 0;
        r1 = new java.lang.ProcessBuilder;	 Catch:{ Throwable -> 0x0037, all -> 0x0040 }
        r1.<init>(r6);	 Catch:{ Throwable -> 0x0037, all -> 0x0040 }
        r3 = 0;
        r1.redirectErrorStream(r3);	 Catch:{ Throwable -> 0x0037, all -> 0x0040 }
        r1 = r1.start();	 Catch:{ Throwable -> 0x0037, all -> 0x0040 }
        r3 = new java.io.DataOutputStream;	 Catch:{ Throwable -> 0x004e, all -> 0x004c }
        r0 = r1.getOutputStream();	 Catch:{ Throwable -> 0x004e, all -> 0x004c }
        r3.<init>(r0);	 Catch:{ Throwable -> 0x004e, all -> 0x004c }
        r0 = new java.io.DataInputStream;	 Catch:{ Throwable -> 0x004e, all -> 0x004c }
        r4 = r1.getInputStream();	 Catch:{ Throwable -> 0x004e, all -> 0x004c }
        r0.<init>(r4);	 Catch:{ Throwable -> 0x004e, all -> 0x004c }
        r0 = r0.readLine();	 Catch:{ Throwable -> 0x004e, all -> 0x004c }
        r2 = "exit\n";
        r3.writeBytes(r2);	 Catch:{ Throwable -> 0x0051, all -> 0x004c }
        r3.flush();	 Catch:{ Throwable -> 0x0051, all -> 0x004c }
        r1.waitFor();	 Catch:{ Throwable -> 0x0051, all -> 0x004c }
        r1.destroy();	 Catch:{ Exception -> 0x0048 }
    L_0x0036:
        return r0;
    L_0x0037:
        r1 = move-exception;
        r1 = r0;
        r0 = r2;
    L_0x003a:
        r1.destroy();	 Catch:{ Exception -> 0x003e }
        goto L_0x0036;
    L_0x003e:
        r1 = move-exception;
        goto L_0x0036;
    L_0x0040:
        r1 = move-exception;
        r5 = r1;
        r1 = r0;
        r0 = r5;
    L_0x0044:
        r1.destroy();	 Catch:{ Exception -> 0x004a }
    L_0x0047:
        throw r0;
    L_0x0048:
        r1 = move-exception;
        goto L_0x0036;
    L_0x004a:
        r1 = move-exception;
        goto L_0x0047;
    L_0x004c:
        r0 = move-exception;
        goto L_0x0044;
    L_0x004e:
        r0 = move-exception;
        r0 = r2;
        goto L_0x003a;
    L_0x0051:
        r2 = move-exception;
        goto L_0x003a;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alipay.sdk.sys.b.a(java.lang.String[]):java.lang.String");
    }

    public final String c() {
        String str = "";
        try {
            str = UTDevice.getUtdid(this.a);
        } catch (Throwable th) {
        }
        return str;
    }
}
