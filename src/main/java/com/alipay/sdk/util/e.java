package com.alipay.sdk.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.text.TextUtils;
import com.alipay.android.app.IAlixPay;
import com.alipay.android.app.IRemoteServiceCallback;
import com.alipay.sdk.app.statistic.c;
import com.umeng.socialize.common.SocializeConstants;
import java.util.List;

public class e {
    public static final String a = "failed";
    private Activity b;
    private IAlixPay c;
    private final Object d = IAlixPay.class;
    private boolean e;
    private a f;
    private ServiceConnection g = new f(this);
    private IRemoteServiceCallback h = new g(this);

    public interface a {
        void a();
    }

    public e(Activity activity, a aVar) {
        this.b = activity;
        this.f = aVar;
    }

    public final String a(String str) {
        Intent intent;
        try {
            com.alipay.sdk.util.i.a aVar;
            Context context = this.b;
            String str2 = i.b;
            for (PackageInfo packageInfo : context.getPackageManager().getInstalledPackages(64)) {
                if (packageInfo.packageName.equals(str2)) {
                    com.alipay.sdk.util.i.a aVar2 = new com.alipay.sdk.util.i.a();
                    aVar2.a = packageInfo.signatures[0].toByteArray();
                    aVar2.b = packageInfo.versionCode;
                    aVar = aVar2;
                    break;
                }
            }
            aVar = null;
            if (aVar != null) {
                str2 = i.a(aVar.a);
                if (!(str2 == null || TextUtils.equals(str2, com.alipay.sdk.cons.a.h))) {
                    com.alipay.sdk.app.statistic.a.a(c.b, c.j, str2);
                    return "failed";
                }
            }
            if (aVar.b > 78) {
                intent = new Intent();
                intent.setClassName(i.b, "com.alipay.android.app.TransProcessPayActivity");
                this.b.startActivity(intent);
                Thread.sleep(150);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        intent = new Intent();
        intent.setPackage(i.b);
        intent.setAction("com.eg.android.AlipayGphone.IAlixPay");
        return a(str, intent);
    }

    private String a(String str, Intent intent) {
        String str2;
        String f = i.f(this.b);
        this.b.getApplicationContext().bindService(intent, this.g, 1);
        synchronized (this.d) {
            if (this.c == null) {
                try {
                    this.d.wait((long) com.alipay.sdk.data.a.b().a());
                } catch (InterruptedException e) {
                }
            }
        }
        try {
            if (this.c == null) {
                String f2 = i.f(this.b);
                List installedPackages = this.b.getPackageManager().getInstalledPackages(0);
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < installedPackages.size(); i++) {
                    PackageInfo packageInfo = (PackageInfo) installedPackages.get(i);
                    int i2 = packageInfo.applicationInfo.flags;
                    if ((i2 & 1) == 0 && (i2 & 128) == 0) {
                        i2 = 1;
                    } else {
                        boolean z = false;
                    }
                    if (i2 != 0) {
                        if (packageInfo.packageName.equals(i.b)) {
                            stringBuilder.append(packageInfo.packageName).append(packageInfo.versionCode).append(SocializeConstants.OP_DIVIDER_MINUS);
                        } else if (!(packageInfo.packageName.contains("theme") || packageInfo.packageName.startsWith("com.google.") || packageInfo.packageName.startsWith("com.android."))) {
                            stringBuilder.append(packageInfo.packageName).append(SocializeConstants.OP_DIVIDER_MINUS);
                        }
                    }
                }
                com.alipay.sdk.app.statistic.a.a(c.b, c.k, f + "|" + f2 + "|" + stringBuilder.toString());
                str2 = "failed";
                try {
                    this.b.unbindService(this.g);
                } catch (Throwable th) {
                }
                this.h = null;
                this.g = null;
                this.c = null;
                if (this.e) {
                    this.b.setRequestedOrientation(0);
                    this.e = false;
                }
                return str2;
            }
            if (this.f != null) {
                this.f.a();
            }
            if (this.b.getRequestedOrientation() == 0) {
                this.b.setRequestedOrientation(1);
                this.e = true;
            }
            this.c.registerCallback(this.h);
            str2 = this.c.Pay(str);
            this.c.unregisterCallback(this.h);
            try {
                this.b.unbindService(this.g);
            } catch (Throwable th2) {
            }
            this.h = null;
            this.g = null;
            this.c = null;
            if (this.e) {
                this.b.setRequestedOrientation(0);
                this.e = false;
            }
            return str2;
        } catch (Throwable th3) {
        }
        this.h = null;
        this.g = null;
        this.c = null;
        if (this.e) {
            this.b.setRequestedOrientation(0);
            this.e = false;
        }
        return str2;
    }

    public final void a() {
        this.b = null;
    }
}
