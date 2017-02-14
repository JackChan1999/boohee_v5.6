package com.alipay.sdk.auth;

import android.app.Activity;

final class i implements Runnable {
    final /* synthetic */ Activity a;
    final /* synthetic */ StringBuilder b;
    final /* synthetic */ APAuthInfo c;

    public final void run() {
        /* JADX: method processing error */
/*
Error: java.util.NoSuchElementException
	at java.util.HashMap$HashIterator.nextNode(HashMap.java:1439)
	at java.util.HashMap$KeyIterator.next(HashMap.java:1461)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.applyRemove(BlockFinallyExtract.java:535)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.extractFinally(BlockFinallyExtract.java:175)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.processExceptionHandler(BlockFinallyExtract.java:79)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:51)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
        /*
        r5 = this;
        r0 = 0;
        r2 = new com.alipay.sdk.packet.impl.a;	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r2.<init>();	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r1 = 0;
        r3 = r5.a;	 Catch:{ Throwable -> 0x0124 }
        r4 = r5.b;	 Catch:{ Throwable -> 0x0124 }
        r4 = r4.toString();	 Catch:{ Throwable -> 0x0124 }
        r1 = r2.a(r3, r4);	 Catch:{ Throwable -> 0x0124 }
    L_0x0013:
        r2 = com.alipay.sdk.auth.h.c;	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        if (r2 == 0) goto L_0x0023;	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
    L_0x0019:
        r2 = com.alipay.sdk.auth.h.c;	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r2.b();	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        com.alipay.sdk.auth.h.c = null;	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
    L_0x0023:
        if (r1 != 0) goto L_0x0059;	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
    L_0x0025:
        r0 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r0.<init>();	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r1 = r5.c;	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r1 = r1.getRedirectUri();	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r0 = r0.append(r1);	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r1 = "?resultCode=202";	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r0 = r0.append(r1);	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r0 = r0.toString();	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        com.alipay.sdk.auth.h.d = r0;	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r0 = r5.a;	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r1 = com.alipay.sdk.auth.h.d;	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        com.alipay.sdk.auth.h.a(r0, r1);	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r0 = com.alipay.sdk.auth.h.c;
        if (r0 == 0) goto L_0x0058;
    L_0x0051:
        r0 = com.alipay.sdk.auth.h.c;
        r0.b();
    L_0x0058:
        return;
    L_0x0059:
        r1 = r1.a();	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r2 = "form";	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r1 = r1.optJSONObject(r2);	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r2 = "onload";	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r1 = r1.optJSONObject(r2);	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r2 = com.alipay.sdk.protocol.b.a(r1);	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r1 = r0;	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
    L_0x0070:
        r0 = r2.size();	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        if (r1 >= r0) goto L_0x0090;	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
    L_0x0076:
        r0 = r2.get(r1);	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r0 = (com.alipay.sdk.protocol.b) r0;	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r0 = r0.a;	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r3 = com.alipay.sdk.protocol.a.WapPay;	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        if (r0 != r3) goto L_0x00ce;	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
    L_0x0082:
        r0 = r2.get(r1);	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r0 = (com.alipay.sdk.protocol.b) r0;	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r0 = r0.b;	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r1 = 0;	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r0 = r0[r1];	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        com.alipay.sdk.auth.h.d = r0;	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
    L_0x0090:
        r0 = com.alipay.sdk.auth.h.d;	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r0 = android.text.TextUtils.isEmpty(r0);	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        if (r0 == 0) goto L_0x00d2;	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
    L_0x009a:
        r0 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r0.<init>();	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r1 = r5.c;	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r1 = r1.getRedirectUri();	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r0 = r0.append(r1);	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r1 = "?resultCode=202";	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r0 = r0.append(r1);	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r0 = r0.toString();	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        com.alipay.sdk.auth.h.d = r0;	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r0 = r5.a;	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r1 = com.alipay.sdk.auth.h.d;	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        com.alipay.sdk.auth.h.a(r0, r1);	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r0 = com.alipay.sdk.auth.h.c;
        if (r0 == 0) goto L_0x0058;
    L_0x00c6:
        r0 = com.alipay.sdk.auth.h.c;
        r0.b();
        goto L_0x0058;
    L_0x00ce:
        r0 = r1 + 1;
        r1 = r0;
        goto L_0x0070;
    L_0x00d2:
        r0 = new android.content.Intent;	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r1 = r5.a;	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r2 = com.alipay.sdk.auth.AuthActivity.class;	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r0.<init>(r1, r2);	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r1 = "params";	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r2 = com.alipay.sdk.auth.h.d;	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r0.putExtra(r1, r2);	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r1 = "redirectUri";	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r2 = r5.c;	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r2 = r2.getRedirectUri();	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r0.putExtra(r1, r2);	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r1 = r5.a;	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r1.startActivity(r0);	 Catch:{ Exception -> 0x0105, all -> 0x0115 }
        r0 = com.alipay.sdk.auth.h.c;
        if (r0 == 0) goto L_0x0058;
    L_0x00fc:
        r0 = com.alipay.sdk.auth.h.c;
        r0.b();
        goto L_0x0058;
    L_0x0105:
        r0 = move-exception;
        r0 = com.alipay.sdk.auth.h.c;
        if (r0 == 0) goto L_0x0058;
    L_0x010c:
        r0 = com.alipay.sdk.auth.h.c;
        r0.b();
        goto L_0x0058;
    L_0x0115:
        r0 = move-exception;
        r1 = com.alipay.sdk.auth.h.c;
        if (r1 == 0) goto L_0x0123;
    L_0x011c:
        r1 = com.alipay.sdk.auth.h.c;
        r1.b();
    L_0x0123:
        throw r0;
    L_0x0124:
        r2 = move-exception;
        goto L_0x0013;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alipay.sdk.auth.i.run():void");
    }

    i(Activity activity, StringBuilder stringBuilder, APAuthInfo aPAuthInfo) {
        this.a = activity;
        this.b = stringBuilder;
        this.c = aPAuthInfo;
    }
}
