package com.alipay.apmobilesecuritysdk.face;

import android.content.Context;
import com.alipay.apmobilesecuritysdk.a.a;
import com.alipay.apmobilesecuritysdk.d.b;
import com.alipay.security.mobile.module.commonutils.CommonUtils;
import com.alipay.security.mobile.module.commonutils.Dbg;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.LinkedList;
import java.util.Map;

public class APSecuritySdk {
    private static APSecuritySdk a;
    private static Object b = new Object();
    private Context c;
    private volatile boolean d = false;
    private Thread e;
    private LinkedList<RunningTask> f = new LinkedList();

    public interface InitResultListener {
        void onResult(TokenResult tokenResult);
    }

    private class RunningTask {
        final /* synthetic */ APSecuritySdk a;
        private int b;
        private String c;
        private String d;
        private String e;
        private InitResultListener f;

        public RunningTask(APSecuritySdk aPSecuritySdk, int i, String str, String str2, String str3, InitResultListener initResultListener) {
            this.a = aPSecuritySdk;
            this.b = i;
            this.e = str3;
            if (CommonUtils.isBlank(str)) {
                this.c = b.a(aPSecuritySdk.c);
            } else {
                this.c = str;
            }
            Dbg.log("Utdid = " + this.c);
            this.d = str2;
            this.f = initResultListener;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
            r4 = this;
            r3 = 0;
            r0 = r4.a;
            r0 = r0.d;
            if (r0 == 0) goto L_0x000a;
        L_0x0009:
            return;
        L_0x000a:
            r0 = r4.a;
            r1 = 1;
            r0.d = r1;
            r0 = r4.a;	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r0 = r0.c;	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            com.alipay.apmobilesecuritysdk.d.a.a(r0);	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r0 = r4.b;	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            com.alipay.apmobilesecuritysdk.d.a.b();	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r0 = new java.util.HashMap;	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r0.<init>();	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r1 = "tid";
            r2 = r4.d;	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r0.put(r1, r2);	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r1 = "utdid";
            r2 = r4.c;	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r0.put(r1, r2);	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r1 = "umid";
            r2 = r4.a;	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r2 = r2.c;	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            com.alipay.apmobilesecuritysdk.d.a.a(r2);	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r2 = com.alipay.apmobilesecuritysdk.d.a.a();	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r0.put(r1, r2);	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r1 = "userId";
            r2 = r4.e;	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r0.put(r1, r2);	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r1 = r4.a;	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r1 = r1.c;	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            com.alipay.apmobilesecuritysdk.face.SecureSdk.getApdidToken(r1, r0);	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r0 = r4.f;	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            if (r0 == 0) goto L_0x00ee;
        L_0x005b:
            r0 = new com.alipay.apmobilesecuritysdk.face.APSecuritySdk$TokenResult;	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r1 = r4.a;	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r0.<init>(r1);	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r1 = r4.a;	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r1 = r1.c;	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r1 = com.alipay.apmobilesecuritysdk.a.a.b(r1);	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r0.apdid = r1;	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r1 = r4.a;	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r1 = r1.c;	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r1 = com.alipay.apmobilesecuritysdk.a.a.a(r1);	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r0.apdidToken = r1;	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r1 = r4.a;	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r1 = r1.c;	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            com.alipay.apmobilesecuritysdk.d.a.a(r1);	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r1 = com.alipay.apmobilesecuritysdk.d.a.a();	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r0.umidToken = r1;	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r1 = r4.a;	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r1 = r1.c;	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r1 = com.alipay.apmobilesecuritysdk.f.b.a(r1);	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r0.clientKey = r1;	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r1 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r2 = "[*]result.apdid     = ";
            r1.<init>(r2);	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r2 = r0.apdid;	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r1 = r1.append(r2);	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r1 = r1.toString();	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            com.alipay.security.mobile.module.commonutils.Dbg.log(r1);	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r1 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r2 = "[*]result.token     = ";
            r1.<init>(r2);	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r2 = r0.apdidToken;	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r1 = r1.append(r2);	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r1 = r1.toString();	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            com.alipay.security.mobile.module.commonutils.Dbg.log(r1);	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r1 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r2 = "[*]result.umid      = ";
            r1.<init>(r2);	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r2 = r0.umidToken;	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r1 = r1.append(r2);	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r1 = r1.toString();	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            com.alipay.security.mobile.module.commonutils.Dbg.log(r1);	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r1 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r2 = "[*]result.clientKey = ";
            r1.<init>(r2);	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r2 = r0.clientKey;	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r1 = r1.append(r2);	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r1 = r1.toString();	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            com.alipay.security.mobile.module.commonutils.Dbg.log(r1);	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r1 = r4.f;	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
            r1.onResult(r0);	 Catch:{ Throwable -> 0x00f5, all -> 0x00fd }
        L_0x00ee:
            r0 = r4.a;
            r0.d = r3;
            goto L_0x0009;
        L_0x00f5:
            r0 = move-exception;
            r0 = r4.a;
            r0.d = r3;
            goto L_0x0009;
        L_0x00fd:
            r0 = move-exception;
            r1 = r4.a;
            r1.d = r3;
            throw r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.alipay.apmobilesecuritysdk.face.APSecuritySdk.RunningTask.run():void");
        }
    }

    public class TokenResult {
        final /* synthetic */ APSecuritySdk a;
        public String apdid;
        public String apdidToken;
        public String clientKey;
        public String umidToken;

        public TokenResult(APSecuritySdk aPSecuritySdk) {
            this.a = aPSecuritySdk;
        }
    }

    private APSecuritySdk(Context context) {
        this.c = context;
    }

    public static APSecuritySdk getInstance(Context context) {
        APSecuritySdk aPSecuritySdk;
        synchronized (b) {
            if (a == null) {
                a = new APSecuritySdk(context);
            }
            aPSecuritySdk = a;
        }
        return aPSecuritySdk;
    }

    public String getApdidToken() {
        return a.a(this.c);
    }

    public TokenResult getTokenResult() {
        TokenResult tokenResult = new TokenResult(this);
        try {
            long currentTimeMillis = System.currentTimeMillis();
            tokenResult.apdidToken = a.a(this.c);
            Dbg.log("getLocalApdidToken spend " + (System.currentTimeMillis() - currentTimeMillis) + " ms");
            tokenResult.apdid = a.b(this.c);
            com.alipay.apmobilesecuritysdk.d.a.a(this.c);
            tokenResult.umidToken = com.alipay.apmobilesecuritysdk.d.a.a();
            tokenResult.clientKey = com.alipay.apmobilesecuritysdk.f.b.a(this.c);
        } catch (Throwable th) {
        }
        return tokenResult;
    }

    public void initToken(int i, Map<String, String> map, InitResultListener initResultListener) {
        String valueFromMap = CommonUtils.getValueFromMap(map, com.alipay.sdk.cons.b.g, "");
        String valueFromMap2 = CommonUtils.getValueFromMap(map, com.alipay.sdk.cons.b.c, "");
        String valueFromMap3 = CommonUtils.getValueFromMap(map, "userId", "");
        switch (i) {
            case 1:
                com.alipay.security.mobile.module.a.a.a.a("http://mobilegw.stable.alipay.net/mgw.htm");
                break;
            case 2:
                com.alipay.security.mobile.module.a.a.a.a("https://mobilegw.alipay.com/mgw.htm");
                break;
            case 3:
                com.alipay.security.mobile.module.a.a.a.a("http://mobilegw-1-64.test.alipay.net/mgw.htm");
                break;
            default:
                com.alipay.security.mobile.module.a.a.a.a("https://mobilegw.alipay.com/mgw.htm");
                break;
        }
        this.f.addLast(new RunningTask(this, i, valueFromMap, valueFromMap2, valueFromMap3, initResultListener));
        if (this.e == null) {
            this.e = new Thread(new Runnable(this) {
                final /* synthetic */ APSecuritySdk a;

                {
                    this.a = r1;
                }

                public void run() {
                    while (!this.a.f.isEmpty()) {
                        try {
                            RunningTask runningTask = (RunningTask) this.a.f.pollFirst();
                            if (runningTask != null) {
                                runningTask.run();
                            }
                        } catch (Throwable th) {
                        } finally {
                            this.a.e = null;
                        }
                    }
                }
            });
            this.e.setUncaughtExceptionHandler(new UncaughtExceptionHandler(this) {
                final /* synthetic */ APSecuritySdk a;

                {
                    this.a = r1;
                }

                public void uncaughtException(Thread thread, Throwable th) {
                }
            });
            this.e.start();
        }
    }
}
