package com.tencent.open.b;

import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;

import com.alipay.sdk.sys.a;
import com.boohee.status.FriendShipActivity;
import com.tencent.connect.common.Constants;
import com.tencent.open.a.f;
import com.tencent.open.utils.Global;
import com.tencent.open.utils.OpenConfig;
import com.tencent.open.utils.ThreadManager;
import com.tencent.open.utils.Util;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONObject;

/* compiled from: ProGuard */
public class g {
    protected static final String a = (f.d + ".ReportManager");
    protected static g b;
    protected Random             c = new Random();
    protected List<Serializable> d = Collections.synchronizedList(new ArrayList());
    protected List<Serializable> e = Collections.synchronizedList(new ArrayList());
    protected HandlerThread      f = null;
    protected Handler g;

    public static synchronized g a() {
        g gVar;
        synchronized (g.class) {
            if (b == null) {
                b = new g();
            }
            gVar = b;
        }
        return gVar;
    }

    private g() {
        if (this.f == null) {
            this.f = new HandlerThread("opensdk.report.handlerthread", 10);
            this.f.start();
        }
        if (this.f.isAlive() && this.f.getLooper() != null) {
            this.g = new Handler(this, this.f.getLooper()) {
                final /* synthetic */ g a;

                public void handleMessage(Message message) {
                    switch (message.what) {
                        case 1000:
                            this.a.b();
                            break;
                        case 1001:
                            this.a.e();
                            break;
                    }
                    super.handleMessage(message);
                }
            };
        }
    }

    public void a(final Bundle bundle, String str, final boolean z) {
        if (bundle != null) {
            f.b(a, "-->reportVia, bundle: " + bundle.toString());
            if (a("report_via", str) || z) {
                this.g.post(new Runnable(this) {
                    final /* synthetic */ g c;

                    public void run() {
                        try {
                            Bundle bundle = new Bundle();
                            bundle.putString("uin", Constants.DEFAULT_UIN);
                            bundle.putString(SocializeProtocolConstants.PROTOCOL_KEY_IMEI, c.b
                                    (Global.getContext()));
                            bundle.putString("imsi", c.c(Global.getContext()));
                            bundle.putString("android_id", c.d(Global.getContext()));
                            bundle.putString(SocializeProtocolConstants.PROTOCOL_KEY_MAC, c.a());
                            bundle.putString("platform", "1");
                            bundle.putString("os_ver", VERSION.RELEASE);
                            bundle.putString(FriendShipActivity.FRIENDSHIP_POSITION, Util
                                    .getLocation(Global.getContext()));
                            bundle.putString("network", a.a(Global.getContext()));
                            bundle.putString("language", c.b());
                            bundle.putString("resolution", c.a(Global.getContext()));
                            bundle.putString("apn", a.b(Global.getContext()));
                            bundle.putString("model_name", Build.MODEL);
                            bundle.putString("timezone", TimeZone.getDefault().getID());
                            bundle.putString("sdk_ver", Constants.SDK_VERSION);
                            bundle.putString("qz_ver", Util.getAppVersionName(Global.getContext()
                                    , Constants.PACKAGE_QZONE));
                            bundle.putString("qq_ver", Util.getVersionName(Global.getContext(),
                                    "com.tencent.mobileqq"));
                            bundle.putString("qua", Util.getQUA3(Global.getContext(), Global
                                    .getPackageName()));
                            bundle.putString("packagename", Global.getPackageName());
                            bundle.putString("app_ver", Util.getAppVersionName(Global.getContext
                                    (), Global.getPackageName()));
                            if (bundle != null) {
                                bundle.putAll(bundle);
                            }
                            this.c.e.add(new b(bundle));
                            int size = this.c.e.size();
                            int i = OpenConfig.getInstance(Global.getContext(), null).getInt
                                    ("Agent_ReportTimeInterval");
                            if (i == 0) {
                                i = 10000;
                            }
                            if (this.c.a("report_via", size) || z) {
                                this.c.e();
                                this.c.g.removeMessages(1001);
                            } else if (!this.c.g.hasMessages(1001)) {
                                Message obtain = Message.obtain();
                                obtain.what = 1001;
                                this.c.g.sendMessageDelayed(obtain, (long) i);
                            }
                        } catch (Throwable e) {
                            f.b(g.a, "--> reporVia, exception in sub thread.", e);
                        }
                    }
                });
            }
        }
    }

    public void a(String str, long j, long j2, long j3, int i) {
        a(str, j, j2, j3, i, "", false);
    }

    public void a(String str, long j, long j2, long j3, int i, String str2, boolean z) {
        f.b(a, "-->reportCgi, command: " + str + " | startTime: " + j + " | reqSize:" + j2 + " | " +
                "rspSize: " + j3 + " | responseCode: " + i + " | detail: " + str2);
        if (a("report_cgi", "" + i) || z) {
            final long j4 = j;
            final String str3 = str;
            final String str4 = str2;
            final int i2 = i;
            final long j5 = j2;
            final long j6 = j3;
            final boolean z2 = z;
            this.g.post(new Runnable(this) {
                final /* synthetic */ g h;

                public void run() {
                    int i = 1;
                    try {
                        long elapsedRealtime = SystemClock.elapsedRealtime() - j4;
                        Bundle bundle = new Bundle();
                        String a = a.a(Global.getContext());
                        bundle.putString("apn", a);
                        bundle.putString("appid", "1000067");
                        bundle.putString("commandid", str3);
                        bundle.putString("detail", str4);
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("network=").append(a).append('&');
                        stringBuilder.append("sdcard=").append(Environment
                                .getExternalStorageState().equals("mounted") ? 1 : 0).append('&');
                        stringBuilder.append("wifi=").append(a.e(Global.getContext()));
                        bundle.putString("deviceInfo", stringBuilder.toString());
                        int a2 = 100 / this.h.a(i2);
                        if (a2 > 0) {
                            if (a2 > 100) {
                                i = 100;
                            } else {
                                i = a2;
                            }
                        }
                        bundle.putString("frequency", i + "");
                        bundle.putString("reqSize", j5 + "");
                        bundle.putString("resultCode", i2 + "");
                        bundle.putString("rspSize", j6 + "");
                        bundle.putString("timeCost", elapsedRealtime + "");
                        bundle.putString("uin", Constants.DEFAULT_UIN);
                        this.h.d.add(new b(bundle));
                        int size = this.h.d.size();
                        i = OpenConfig.getInstance(Global.getContext(), null).getInt
                                ("Agent_ReportTimeInterval");
                        if (i == 0) {
                            i = 10000;
                        }
                        if (this.h.a("report_cgi", size) || z2) {
                            this.h.b();
                            this.h.g.removeMessages(1000);
                        } else if (!this.h.g.hasMessages(1000)) {
                            Message obtain = Message.obtain();
                            obtain.what = 1000;
                            this.h.g.sendMessageDelayed(obtain, (long) i);
                        }
                    } catch (Throwable e) {
                        f.b(g.a, "--> reportCGI, exception in sub thread.", e);
                    }
                }
            });
        }
    }

    protected void b() {
        ThreadManager.executeOnNetWorkThread(new Runnable(this) {
            final /* synthetic */ g a;

            {
                this.a = r1;
            }

            /* JADX WARNING: inconsistent code. */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                r8 = this;
                r1 = 0;
                r0 = r8.a;	 Catch:{ Exception -> 0x00bd }
                r4 = r0.c();	 Catch:{ Exception -> 0x00bd }
                if (r4 != 0) goto L_0x000a;
            L_0x0009:
                return;
            L_0x000a:
                r0 = com.tencent.open.utils.Global.getContext();	 Catch:{ Exception -> 0x00bd }
                r2 = 0;
                r0 = com.tencent.open.utils.OpenConfig.getInstance(r0, r2);	 Catch:{ Exception ->
                0x00bd }
                r2 = "Common_HttpRetryCount";
                r0 = r0.getInt(r2);	 Catch:{ Exception -> 0x00bd }
                if (r0 != 0) goto L_0x00c8;
            L_0x001c:
                r0 = 3;
                r3 = r0;
            L_0x001e:
                r0 = com.tencent.open.b.g.a;	 Catch:{ Exception -> 0x00bd }
                r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00bd }
                r2.<init>();	 Catch:{ Exception -> 0x00bd }
                r5 = "-->doReportCgi, retryCount: ";
                r2 = r2.append(r5);	 Catch:{ Exception -> 0x00bd }
                r2 = r2.append(r3);	 Catch:{ Exception -> 0x00bd }
                r2 = r2.toString();	 Catch:{ Exception -> 0x00bd }
                com.tencent.open.a.f.b(r0, r2);	 Catch:{ Exception -> 0x00bd }
                r0 = r1;
            L_0x0038:
                r0 = r0 + 1;
                r2 = com.tencent.open.utils.Global.getContext();	 Catch:{
                ConnectTimeoutException -> 0x00cb, SocketTimeoutException -> 0x00d7, Exception ->
                 0x00e1 }
                r5 = 0;
                r6 = "http://wspeed.qq.com/w.cgi";
                r2 = com.tencent.open.utils.HttpUtils.getHttpClient(r2, r5, r6);	 Catch:{
                ConnectTimeoutException -> 0x00cb, SocketTimeoutException -> 0x00d7, Exception ->
                 0x00e1 }
                r5 = new org.apache.http.client.methods.HttpPost;	 Catch:{
                ConnectTimeoutException -> 0x00cb, SocketTimeoutException -> 0x00d7, Exception ->
                 0x00e1 }
                r6 = "http://wspeed.qq.com/w.cgi";
                r5.<init>(r6);	 Catch:{ ConnectTimeoutException -> 0x00cb, SocketTimeoutException
                 -> 0x00d7, Exception -> 0x00e1 }
                r6 = "Accept-Encoding";
                r7 = "gzip";
                r5.addHeader(r6, r7);	 Catch:{ ConnectTimeoutException -> 0x00cb,
                SocketTimeoutException -> 0x00d7, Exception -> 0x00e1 }
                r6 = "Content-Type";
                r7 = "application/x-www-form-urlencoded";
                r5.setHeader(r6, r7);	 Catch:{ ConnectTimeoutException -> 0x00cb,
                SocketTimeoutException -> 0x00d7, Exception -> 0x00e1 }
                r6 = com.tencent.open.utils.HttpUtils.encodeUrl(r4);	 Catch:{
                ConnectTimeoutException -> 0x00cb, SocketTimeoutException -> 0x00d7, Exception ->
                 0x00e1 }
                r6 = r6.getBytes();	 Catch:{ ConnectTimeoutException -> 0x00cb,
                SocketTimeoutException -> 0x00d7, Exception -> 0x00e1 }
                r7 = new org.apache.http.entity.ByteArrayEntity;	 Catch:{
                ConnectTimeoutException -> 0x00cb, SocketTimeoutException -> 0x00d7, Exception ->
                 0x00e1 }
                r7.<init>(r6);	 Catch:{ ConnectTimeoutException -> 0x00cb, SocketTimeoutException
                 -> 0x00d7, Exception -> 0x00e1 }
                r5.setEntity(r7);	 Catch:{ ConnectTimeoutException -> 0x00cb,
                SocketTimeoutException -> 0x00d7, Exception -> 0x00e1 }
                r2 = r2.execute(r5);	 Catch:{ ConnectTimeoutException -> 0x00cb,
                SocketTimeoutException -> 0x00d7, Exception -> 0x00e1 }
                r2 = r2.getStatusLine();	 Catch:{ ConnectTimeoutException -> 0x00cb,
                SocketTimeoutException -> 0x00d7, Exception -> 0x00e1 }
                r2 = r2.getStatusCode();	 Catch:{ ConnectTimeoutException -> 0x00cb,
                SocketTimeoutException -> 0x00d7, Exception -> 0x00e1 }
                r5 = com.tencent.open.b.g.a;	 Catch:{ ConnectTimeoutException -> 0x00cb,
                SocketTimeoutException -> 0x00d7, Exception -> 0x00e1 }
                r6 = new java.lang.StringBuilder;	 Catch:{ ConnectTimeoutException -> 0x00cb,
                SocketTimeoutException -> 0x00d7, Exception -> 0x00e1 }
                r6.<init>();	 Catch:{ ConnectTimeoutException -> 0x00cb, SocketTimeoutException
                 -> 0x00d7, Exception -> 0x00e1 }
                r7 = "-->doReportCgi, statusCode: ";
                r6 = r6.append(r7);	 Catch:{ ConnectTimeoutException -> 0x00cb,
                SocketTimeoutException -> 0x00d7, Exception -> 0x00e1 }
                r6 = r6.append(r2);	 Catch:{ ConnectTimeoutException -> 0x00cb,
                SocketTimeoutException -> 0x00d7, Exception -> 0x00e1 }
                r6 = r6.toString();	 Catch:{ ConnectTimeoutException -> 0x00cb,
                SocketTimeoutException -> 0x00d7, Exception -> 0x00e1 }
                com.tencent.open.a.f.b(r5, r6);	 Catch:{ ConnectTimeoutException -> 0x00cb,
                SocketTimeoutException -> 0x00d7, Exception -> 0x00e1 }
                r5 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
                if (r2 != r5) goto L_0x00a4;
            L_0x0099:
                r2 = com.tencent.open.b.f.a();	 Catch:{ ConnectTimeoutException -> 0x00cb,
                SocketTimeoutException -> 0x00d7, Exception -> 0x00e1 }
                r5 = "report_cgi";
                r2.b(r5);	 Catch:{ ConnectTimeoutException -> 0x00cb, SocketTimeoutException ->
                0x00d7, Exception -> 0x00e1 }
                r1 = 1;
            L_0x00a4:
                if (r1 != 0) goto L_0x00b4;
            L_0x00a6:
                r0 = com.tencent.open.b.f.a();	 Catch:{ Exception -> 0x00bd }
                r1 = "report_cgi";
                r2 = r8.a;	 Catch:{ Exception -> 0x00bd }
                r2 = r2.d;	 Catch:{ Exception -> 0x00bd }
                r0.a(r1, r2);	 Catch:{ Exception -> 0x00bd }
            L_0x00b4:
                r0 = r8.a;	 Catch:{ Exception -> 0x00bd }
                r0 = r0.d;	 Catch:{ Exception -> 0x00bd }
                r0.clear();	 Catch:{ Exception -> 0x00bd }
                goto L_0x0009;
            L_0x00bd:
                r0 = move-exception;
                r1 = com.tencent.open.b.g.a;
                r2 = "-->doReportCgi, doupload exception out.";
                com.tencent.open.a.f.a(r1, r2, r0);
                goto L_0x0009;
            L_0x00c8:
                r3 = r0;
                goto L_0x001e;
            L_0x00cb:
                r2 = move-exception;
                r5 = com.tencent.open.b.g.a;	 Catch:{ Exception -> 0x00bd }
                r6 = "-->doReportCgi, doupload exception";
                com.tencent.open.a.f.a(r5, r6, r2);	 Catch:{ Exception -> 0x00bd }
            L_0x00d4:
                if (r0 < r3) goto L_0x0038;
            L_0x00d6:
                goto L_0x00a4;
            L_0x00d7:
                r2 = move-exception;
                r5 = com.tencent.open.b.g.a;	 Catch:{ Exception -> 0x00bd }
                r6 = "-->doReportCgi, doupload exception";
                com.tencent.open.a.f.a(r5, r6, r2);	 Catch:{ Exception -> 0x00bd }
                goto L_0x00d4;
            L_0x00e1:
                r0 = move-exception;
                r2 = com.tencent.open.b.g.a;	 Catch:{ Exception -> 0x00bd }
                r3 = "-->doReportCgi, doupload exception";
                com.tencent.open.a.f.a(r2, r3, r0);	 Catch:{ Exception -> 0x00bd }
                goto L_0x00a4;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.tencent.open" +
                        ".b.g.4.run():void");
            }
        });
    }

    protected boolean a(String str, String str2) {
        boolean z = true;
        boolean z2 = false;
        f.b(a, "-->availableFrequency, report: " + str + " | ext: " + str2);
        if (!TextUtils.isEmpty(str)) {
            int i;
            int a;
            if (str.equals("report_cgi")) {
                try {
                    a = a(Integer.parseInt(str2));
                    if (this.c.nextInt(100) >= a) {
                        z = false;
                    }
                    z2 = z;
                    i = a;
                } catch (Exception e) {
                }
            } else if (str.equals("report_via")) {
                a = e.a(str2);
                if (this.c.nextInt(100) < a) {
                    z2 = true;
                    i = a;
                } else {
                    i = a;
                }
            } else {
                i = 100;
            }
            f.b(a, "-->availableFrequency, result: " + z2 + " | frequency: " + i);
        }
        return z2;
    }

    protected boolean a(String str, int i) {
        int i2 = 5;
        int i3;
        if (str.equals("report_cgi")) {
            i3 = OpenConfig.getInstance(Global.getContext(), null).getInt
                    ("Common_CGIReportMaxcount");
            if (i3 != 0) {
                i2 = i3;
            }
        } else if (str.equals("report_via")) {
            i3 = OpenConfig.getInstance(Global.getContext(), null).getInt("Agent_ReportBatchCount");
            if (i3 != 0) {
                i2 = i3;
            }
        } else {
            i2 = 0;
        }
        f.b(a, "-->availableCount, report: " + str + " | dataSize: " + i + " | maxcount: " + i2);
        if (i >= i2) {
            return true;
        }
        return false;
    }

    protected int a(int i) {
        int i2;
        if (i == 0) {
            i2 = OpenConfig.getInstance(Global.getContext(), null).getInt
                    ("Common_CGIReportFrequencySuccess");
            if (i2 == 0) {
                return 10;
            }
            return i2;
        }
        i2 = OpenConfig.getInstance(Global.getContext(), null).getInt
                ("Common_CGIReportFrequencyFailed");
        if (i2 == 0) {
            return 100;
        }
        return i2;
    }

    protected Bundle c() {
        if (this.d.size() == 0) {
            return null;
        }
        b bVar = (b) this.d.get(0);
        if (bVar == null) {
            f.b(a, "-->prepareCgiData, the 0th cgireportitem is null.");
            return null;
        }
        String str = (String) bVar.a.get("appid");
        Collection a = f.a().a("report_cgi");
        if (a != null) {
            this.d.addAll(a);
        }
        f.b(a, "-->prepareCgiData, mCgiList size: " + this.d.size());
        if (this.d.size() == 0) {
            return null;
        }
        Bundle bundle = new Bundle();
        try {
            bundle.putString("appid", str);
            bundle.putString("releaseversion", Constants.SDK_VERSION_REPORT);
            bundle.putString("device", Build.DEVICE);
            bundle.putString("qua", Constants.SDK_QUA);
            bundle.putString("key", "apn,frequency,commandid,resultcode,tmcost,reqsize,rspsize," +
                    "detail,touin,deviceinfo");
            for (int i = 0; i < this.d.size(); i++) {
                bVar = (b) this.d.get(i);
                bundle.putString(i + "_1", (String) bVar.a.get("apn"));
                bundle.putString(i + "_2", (String) bVar.a.get("frequency"));
                bundle.putString(i + "_3", (String) bVar.a.get("commandid"));
                bundle.putString(i + "_4", (String) bVar.a.get("resultCode"));
                bundle.putString(i + "_5", (String) bVar.a.get("timeCost"));
                bundle.putString(i + "_6", (String) bVar.a.get("reqSize"));
                bundle.putString(i + "_7", (String) bVar.a.get("rspSize"));
                bundle.putString(i + "_8", (String) bVar.a.get("detail"));
                bundle.putString(i + "_9", (String) bVar.a.get("uin"));
                bundle.putString(i + "_10", c.e(Global.getContext()) + a.b + ((String) bVar.a.get
                        ("deviceInfo")));
            }
            f.b(a, "-->prepareCgiData, end. params: " + bundle.toString());
            return bundle;
        } catch (Throwable e) {
            f.b(a, "-->prepareCgiData, exception.", e);
            return null;
        }
    }

    protected Bundle d() {
        Collection a = f.a().a("report_via");
        if (a != null) {
            this.e.addAll(a);
        }
        f.b(a, "-->prepareViaData, mViaList size: " + this.e.size());
        if (this.e.size() == 0) {
            return null;
        }
        JSONArray jSONArray = new JSONArray();
        for (Serializable serializable : this.e) {
            JSONObject jSONObject = new JSONObject();
            b bVar = (b) serializable;
            for (String str : bVar.a.keySet()) {
                try {
                    Object obj = (String) bVar.a.get(str);
                    if (obj == null) {
                        obj = "";
                    }
                    jSONObject.put(str, obj);
                } catch (Throwable e) {
                    f.a(a, "-->prepareViaData, put bundle to json array exception", e);
                }
            }
            jSONArray.put(jSONObject);
        }
        f.b(a, "-->prepareViaData, JSONArray array: " + jSONArray.toString());
        Bundle bundle = new Bundle();
        JSONObject jSONObject2 = new JSONObject();
        try {
            jSONObject2.put("data", jSONArray);
            bundle.putString("data", jSONObject2.toString());
            return bundle;
        } catch (Throwable e2) {
            f.a(a, "-->prepareViaData, put bundle to json array exception", e2);
            return null;
        }
    }

    protected void e() {
        ThreadManager.executeOnNetWorkThread(new Runnable(this) {
            final /* synthetic */ g a;

            {
                this.a = r1;
            }

            /* JADX WARNING: inconsistent code. */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                r18 = this;
                r0 = r18;
                r2 = r0.a;	 Catch:{ Exception -> 0x00aa }
                r14 = r2.d();	 Catch:{ Exception -> 0x00aa }
                if (r14 != 0) goto L_0x000b;
            L_0x000a:
                return;
            L_0x000b:
                r2 = com.tencent.open.b.g.a;	 Catch:{ Exception -> 0x00aa }
                r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00aa }
                r3.<init>();	 Catch:{ Exception -> 0x00aa }
                r4 = "-->doReportVia, params: ";
                r3 = r3.append(r4);	 Catch:{ Exception -> 0x00aa }
                r4 = r14.toString();	 Catch:{ Exception -> 0x00aa }
                r3 = r3.append(r4);	 Catch:{ Exception -> 0x00aa }
                r3 = r3.toString();	 Catch:{ Exception -> 0x00aa }
                com.tencent.open.a.f.b(r2, r3);	 Catch:{ Exception -> 0x00aa }
                r11 = com.tencent.open.b.e.a();	 Catch:{ Exception -> 0x00aa }
                r10 = 0;
                r3 = 0;
                r8 = android.os.SystemClock.elapsedRealtime();	 Catch:{ Exception -> 0x00aa }
                r6 = 0;
                r4 = 0;
                r2 = 0;
            L_0x0037:
                r10 = r10 + 1;
                r12 = com.tencent.open.utils.Global.getContext();	 Catch:{
                ConnectTimeoutException -> 0x00b8, SocketTimeoutException -> 0x00c8,
                JSONException -> 0x00d3, NetworkUnavailableException -> 0x00da,
                HttpStatusException -> 0x00ee, IOException -> 0x010f, Exception -> 0x011a }
                r13 = "http://appsupport.qq.com/cgi-bin/appstage/mstats_batch_report";
                r15 = "POST";
                r15 = com.tencent.open.utils.HttpUtils.openUrl2(r12, r13, r15, r14);	 Catch:{
                ConnectTimeoutException -> 0x00b8, SocketTimeoutException -> 0x00c8,
                JSONException -> 0x00d3, NetworkUnavailableException -> 0x00da,
                HttpStatusException -> 0x00ee, IOException -> 0x010f, Exception -> 0x011a }
                r12 = r15.response;	 Catch:{ ConnectTimeoutException -> 0x00b8,
                SocketTimeoutException -> 0x00c8, JSONException -> 0x00d3,
                NetworkUnavailableException -> 0x00da, HttpStatusException -> 0x00ee, IOException
                 -> 0x010f, Exception -> 0x011a }
                r12 = com.tencent.open.utils.Util.parseJson(r12);	 Catch:{
                ConnectTimeoutException -> 0x00b8, SocketTimeoutException -> 0x00c8,
                JSONException -> 0x00d3, NetworkUnavailableException -> 0x00da,
                HttpStatusException -> 0x00ee, IOException -> 0x010f, Exception -> 0x011a }
                r13 = "ret";
                r12 = r12.getInt(r13);	 Catch:{ JSONException -> 0x00b5, ConnectTimeoutException
                -> 0x00b8, SocketTimeoutException -> 0x00c8, NetworkUnavailableException ->
                0x00da, HttpStatusException -> 0x00ee, IOException -> 0x010f, Exception -> 0x011a }
            L_0x0054:
                if (r12 == 0) goto L_0x005e;
            L_0x0056:
                r12 = r15.response;	 Catch:{ ConnectTimeoutException -> 0x00b8,
                SocketTimeoutException -> 0x00c8, JSONException -> 0x00d3,
                NetworkUnavailableException -> 0x00da, HttpStatusException -> 0x00ee, IOException
                 -> 0x010f, Exception -> 0x011a }
                r12 = android.text.TextUtils.isEmpty(r12);	 Catch:{ ConnectTimeoutException ->
                0x00b8, SocketTimeoutException -> 0x00c8, JSONException -> 0x00d3,
                NetworkUnavailableException -> 0x00da, HttpStatusException -> 0x00ee, IOException
                 -> 0x010f, Exception -> 0x011a }
                if (r12 != 0) goto L_0x0060;
            L_0x005e:
                r3 = 1;
                r10 = r11;
            L_0x0060:
                r12 = r15.reqSize;	 Catch:{ ConnectTimeoutException -> 0x00b8,
                SocketTimeoutException -> 0x00c8, JSONException -> 0x00d3,
                NetworkUnavailableException -> 0x00da, HttpStatusException -> 0x00ee, IOException
                 -> 0x010f, Exception -> 0x011a }
                r4 = r15.rspSize;	 Catch:{ ConnectTimeoutException -> 0x00b8,
                SocketTimeoutException -> 0x00c8, JSONException -> 0x00d3,
                NetworkUnavailableException -> 0x00da, HttpStatusException -> 0x0137, IOException
                 -> 0x010f, Exception -> 0x011a }
                r6 = r12;
            L_0x0065:
                if (r10 < r11) goto L_0x0037;
            L_0x0067:
                r10 = r2;
                r13 = r3;
                r16 = r8;
                r8 = r4;
                r4 = r16;
            L_0x006e:
                r0 = r18;
                r2 = r0.a;	 Catch:{ Exception -> 0x00aa }
                r3 = "mapp_apptrace_sdk";
                r11 = 0;
                r12 = 0;
                r2.a(r3, r4, r6, r8, r10, r11, r12);	 Catch:{ Exception -> 0x00aa }
                if (r13 == 0) goto L_0x0123;
            L_0x007c:
                r2 = com.tencent.open.b.f.a();	 Catch:{ Exception -> 0x00aa }
                r3 = "report_via";
                r2.b(r3);	 Catch:{ Exception -> 0x00aa }
            L_0x0086:
                r0 = r18;
                r2 = r0.a;	 Catch:{ Exception -> 0x00aa }
                r2 = r2.e;	 Catch:{ Exception -> 0x00aa }
                r2.clear();	 Catch:{ Exception -> 0x00aa }
                r2 = com.tencent.open.b.g.a;	 Catch:{ Exception -> 0x00aa }
                r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00aa }
                r3.<init>();	 Catch:{ Exception -> 0x00aa }
                r4 = "-->doReportVia, uploadSuccess: ";
                r3 = r3.append(r4);	 Catch:{ Exception -> 0x00aa }
                r3 = r3.append(r13);	 Catch:{ Exception -> 0x00aa }
                r3 = r3.toString();	 Catch:{ Exception -> 0x00aa }
                com.tencent.open.a.f.b(r2, r3);	 Catch:{ Exception -> 0x00aa }
                goto L_0x000a;
            L_0x00aa:
                r2 = move-exception;
                r3 = com.tencent.open.b.g.a;
                r4 = "-->doReportVia, exception in serial executor.";
                com.tencent.open.a.f.b(r3, r4, r2);
                goto L_0x000a;
            L_0x00b5:
                r12 = move-exception;
                r12 = -4;
                goto L_0x0054;
            L_0x00b8:
                r2 = move-exception;
                r2 = r10;
                r8 = android.os.SystemClock.elapsedRealtime();	 Catch:{ Exception -> 0x00aa }
                r12 = 0;
                r6 = 0;
                r4 = -7;
                r10 = r2;
                r2 = r4;
                r4 = r6;
                r6 = r12;
                goto L_0x0065;
            L_0x00c8:
                r2 = move-exception;
                r8 = android.os.SystemClock.elapsedRealtime();	 Catch:{ Exception -> 0x00aa }
                r6 = 0;
                r4 = 0;
                r2 = -8;
                goto L_0x0065;
            L_0x00d3:
                r2 = move-exception;
                r6 = 0;
                r4 = 0;
                r2 = -4;
                goto L_0x0065;
            L_0x00da:
                r2 = move-exception;
                r0 = r18;
                r2 = r0.a;	 Catch:{ Exception -> 0x00aa }
                r2 = r2.e;	 Catch:{ Exception -> 0x00aa }
                r2.clear();	 Catch:{ Exception -> 0x00aa }
                r2 = com.tencent.open.b.g.a;	 Catch:{ Exception -> 0x00aa }
                r3 = "doReportVia, NetworkUnavailableException.";
                com.tencent.open.a.f.b(r2, r3);	 Catch:{ Exception -> 0x00aa }
                goto L_0x000a;
            L_0x00ee:
                r10 = move-exception;
                r16 = r10;
                r10 = r3;
                r3 = r16;
            L_0x00f4:
                r3 = r3.getMessage();	 Catch:{ Exception -> 0x0135 }
                r11 = "http status code error:";
                r12 = "";
                r3 = r3.replace(r11, r12);	 Catch:{ Exception -> 0x0135 }
                r2 = java.lang.Integer.parseInt(r3);	 Catch:{ Exception -> 0x0135 }
            L_0x0106:
                r13 = r10;
                r10 = r2;
                r16 = r8;
                r8 = r4;
                r4 = r16;
                goto L_0x006e;
            L_0x010f:
                r2 = move-exception;
                r6 = 0;
                r4 = 0;
                r2 = com.tencent.open.utils.HttpUtils.getErrorCodeFromException(r2);	 Catch:{
                Exception -> 0x00aa }
                goto L_0x0065;
            L_0x011a:
                r2 = move-exception;
                r6 = 0;
                r4 = 0;
                r2 = -6;
                r10 = r11;
                goto L_0x0065;
            L_0x0123:
                r2 = com.tencent.open.b.f.a();	 Catch:{ Exception -> 0x00aa }
                r3 = "report_via";
                r0 = r18;
                r4 = r0.a;	 Catch:{ Exception -> 0x00aa }
                r4 = r4.e;	 Catch:{ Exception -> 0x00aa }
                r2.a(r3, r4);	 Catch:{ Exception -> 0x00aa }
                goto L_0x0086;
            L_0x0135:
                r3 = move-exception;
                goto L_0x0106;
            L_0x0137:
                r6 = move-exception;
                r10 = r3;
                r3 = r6;
                r6 = r12;
                goto L_0x00f4;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.tencent.open" +
                        ".b.g.5.run():void");
            }
        });
    }

    public void a(String str, String str2, Bundle bundle, boolean z) {
        final Bundle bundle2 = bundle;
        final String str3 = str;
        final boolean z2 = z;
        final String str4 = str2;
        ThreadManager.executeOnSubThread(new Runnable(this) {
            final /* synthetic */ g e;

            /* JADX WARNING: inconsistent code. */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                r10 = this;
                r2 = 1;
                r0 = 0;
                r1 = r2;	 Catch:{ Exception -> 0x00bf }
                if (r1 != 0) goto L_0x000f;
            L_0x0006:
                r0 = com.tencent.open.b.g.a;	 Catch:{ Exception -> 0x00bf }
                r1 = "-->httpRequest, params is null!";
                com.tencent.open.a.f.e(r0, r1);	 Catch:{ Exception -> 0x00bf }
            L_0x000e:
                return;
            L_0x000f:
                r1 = com.tencent.open.b.e.a();	 Catch:{ Exception -> 0x00bf }
                if (r1 != 0) goto L_0x00ca;
            L_0x0015:
                r1 = 3;
                r4 = r1;
            L_0x0017:
                r1 = com.tencent.open.b.g.a;	 Catch:{ Exception -> 0x00bf }
                r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00bf }
                r3.<init>();	 Catch:{ Exception -> 0x00bf }
                r5 = "-->httpRequest, retryCount: ";
                r3 = r3.append(r5);	 Catch:{ Exception -> 0x00bf }
                r3 = r3.append(r4);	 Catch:{ Exception -> 0x00bf }
                r3 = r3.toString();	 Catch:{ Exception -> 0x00bf }
                com.tencent.open.a.f.b(r1, r3);	 Catch:{ Exception -> 0x00bf }
                r1 = com.tencent.open.utils.Global.getContext();	 Catch:{ Exception -> 0x00bf }
                r3 = 0;
                r5 = r3;	 Catch:{ Exception -> 0x00bf }
                r5 = com.tencent.open.utils.HttpUtils.getHttpClient(r1, r3, r5);	 Catch:{
                Exception -> 0x00bf }
                r1 = r2;	 Catch:{ Exception -> 0x00bf }
                r1 = com.tencent.open.utils.HttpUtils.encodeUrl(r1);	 Catch:{ Exception ->
                0x00bf }
                r3 = r4;	 Catch:{ Exception -> 0x00bf }
                if (r3 == 0) goto L_0x0139;
            L_0x0045:
                r1 = java.net.URLEncoder.encode(r1);	 Catch:{ Exception -> 0x00bf }
                r3 = r1;
            L_0x004a:
                r1 = r5;	 Catch:{ Exception -> 0x00bf }
                r1 = r1.toUpperCase();	 Catch:{ Exception -> 0x00bf }
                r6 = "GET";
                r1 = r1.equals(r6);	 Catch:{ Exception -> 0x00bf }
                if (r1 == 0) goto L_0x00cd;
            L_0x0059:
                r6 = new java.lang.StringBuffer;	 Catch:{ Exception -> 0x00bf }
                r1 = r3;	 Catch:{ Exception -> 0x00bf }
                r6.<init>(r1);	 Catch:{ Exception -> 0x00bf }
                r6.append(r3);	 Catch:{ Exception -> 0x00bf }
                r1 = new org.apache.http.client.methods.HttpGet;	 Catch:{ Exception -> 0x00bf }
                r3 = r6.toString();	 Catch:{ Exception -> 0x00bf }
                r1.<init>(r3);	 Catch:{ Exception -> 0x00bf }
                r3 = r1;
            L_0x006d:
                r1 = "Accept-Encoding";
                r6 = "gzip";
                r3.addHeader(r1, r6);	 Catch:{ Exception -> 0x00bf }
                r1 = "Content-Type";
                r6 = "application/x-www-form-urlencoded";
                r3.addHeader(r1, r6);	 Catch:{ Exception -> 0x00bf }
                r1 = r0;
            L_0x0080:
                r1 = r1 + 1;
                r6 = r5.execute(r3);	 Catch:{ ConnectTimeoutException -> 0x0106,
                SocketTimeoutException -> 0x0112, Exception -> 0x011c }
                r6 = r6.getStatusLine();	 Catch:{ ConnectTimeoutException -> 0x0106,
                SocketTimeoutException -> 0x0112, Exception -> 0x011c }
                r6 = r6.getStatusCode();	 Catch:{ ConnectTimeoutException -> 0x0106,
                SocketTimeoutException -> 0x0112, Exception -> 0x011c }
                r7 = com.tencent.open.b.g.a;	 Catch:{ ConnectTimeoutException -> 0x0106,
                SocketTimeoutException -> 0x0112, Exception -> 0x011c }
                r8 = new java.lang.StringBuilder;	 Catch:{ ConnectTimeoutException -> 0x0106,
                SocketTimeoutException -> 0x0112, Exception -> 0x011c }
                r8.<init>();	 Catch:{ ConnectTimeoutException -> 0x0106, SocketTimeoutException
                 -> 0x0112, Exception -> 0x011c }
                r9 = "-->httpRequest, statusCode: ";
                r8 = r8.append(r9);	 Catch:{ ConnectTimeoutException -> 0x0106,
                SocketTimeoutException -> 0x0112, Exception -> 0x011c }
                r8 = r8.append(r6);	 Catch:{ ConnectTimeoutException -> 0x0106,
                SocketTimeoutException -> 0x0112, Exception -> 0x011c }
                r8 = r8.toString();	 Catch:{ ConnectTimeoutException -> 0x0106,
                SocketTimeoutException -> 0x0112, Exception -> 0x011c }
                com.tencent.open.a.f.b(r7, r8);	 Catch:{ ConnectTimeoutException -> 0x0106,
                SocketTimeoutException -> 0x0112, Exception -> 0x011c }
                r7 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
                if (r6 == r7) goto L_0x00fc;
            L_0x00ab:
                r6 = com.tencent.open.b.g.a;	 Catch:{ ConnectTimeoutException -> 0x0106,
                SocketTimeoutException -> 0x0112, Exception -> 0x011c }
                r7 = "-->ReportCenter httpRequest : HttpStatuscode != 200";
                com.tencent.open.a.f.b(r6, r7);	 Catch:{ ConnectTimeoutException -> 0x0106,
                SocketTimeoutException -> 0x0112, Exception -> 0x011c }
            L_0x00b3:
                if (r0 != r2) goto L_0x0126;
            L_0x00b5:
                r0 = com.tencent.open.b.g.a;	 Catch:{ Exception -> 0x00bf }
                r1 = "-->ReportCenter httpRequest Thread request success";
                com.tencent.open.a.f.b(r0, r1);	 Catch:{ Exception -> 0x00bf }
                goto L_0x000e;
            L_0x00bf:
                r0 = move-exception;
                r0 = com.tencent.open.b.g.a;
                r1 = "-->httpRequest, exception in serial executor.";
                com.tencent.open.a.f.b(r0, r1);
                goto L_0x000e;
            L_0x00ca:
                r4 = r1;
                goto L_0x0017;
            L_0x00cd:
                r1 = r5;	 Catch:{ Exception -> 0x00bf }
                r1 = r1.toUpperCase();	 Catch:{ Exception -> 0x00bf }
                r6 = "POST";
                r1 = r1.equals(r6);	 Catch:{ Exception -> 0x00bf }
                if (r1 == 0) goto L_0x00f2;
            L_0x00dc:
                r1 = new org.apache.http.client.methods.HttpPost;	 Catch:{ Exception -> 0x00bf }
                r6 = r3;	 Catch:{ Exception -> 0x00bf }
                r1.<init>(r6);	 Catch:{ Exception -> 0x00bf }
                r3 = r3.getBytes();	 Catch:{ Exception -> 0x00bf }
                r6 = new org.apache.http.entity.ByteArrayEntity;	 Catch:{ Exception -> 0x00bf }
                r6.<init>(r3);	 Catch:{ Exception -> 0x00bf }
                r1.setEntity(r6);	 Catch:{ Exception -> 0x00bf }
                r3 = r1;
                goto L_0x006d;
            L_0x00f2:
                r0 = com.tencent.open.b.g.a;	 Catch:{ Exception -> 0x00bf }
                r1 = "-->httpRequest unkonw request method return.";
                com.tencent.open.a.f.e(r0, r1);	 Catch:{ Exception -> 0x00bf }
                goto L_0x000e;
            L_0x00fc:
                r0 = com.tencent.open.b.g.a;	 Catch:{ ConnectTimeoutException -> 0x0136,
                SocketTimeoutException -> 0x0133, Exception -> 0x0130 }
                r6 = "-->ReportCenter httpRequest Thread success";
                com.tencent.open.a.f.b(r0, r6);	 Catch:{ ConnectTimeoutException -> 0x0136,
                SocketTimeoutException -> 0x0133, Exception -> 0x0130 }
                r0 = r2;
                goto L_0x00b3;
            L_0x0106:
                r6 = move-exception;
            L_0x0107:
                r6 = com.tencent.open.b.g.a;	 Catch:{ Exception -> 0x00bf }
                r7 = "-->ReportCenter httpRequest ConnectTimeoutException";
                com.tencent.open.a.f.b(r6, r7);	 Catch:{ Exception -> 0x00bf }
            L_0x010f:
                if (r1 < r4) goto L_0x0080;
            L_0x0111:
                goto L_0x00b3;
            L_0x0112:
                r6 = move-exception;
            L_0x0113:
                r6 = com.tencent.open.b.g.a;	 Catch:{ Exception -> 0x00bf }
                r7 = "-->ReportCenter httpRequest SocketTimeoutException";
                com.tencent.open.a.f.b(r6, r7);	 Catch:{ Exception -> 0x00bf }
                goto L_0x010f;
            L_0x011c:
                r1 = move-exception;
            L_0x011d:
                r1 = com.tencent.open.b.g.a;	 Catch:{ Exception -> 0x00bf }
                r3 = "-->ReportCenter httpRequest Exception";
                com.tencent.open.a.f.b(r1, r3);	 Catch:{ Exception -> 0x00bf }
                goto L_0x00b3;
            L_0x0126:
                r0 = com.tencent.open.b.g.a;	 Catch:{ Exception -> 0x00bf }
                r1 = "-->ReportCenter httpRequest Thread request failed";
                com.tencent.open.a.f.b(r0, r1);	 Catch:{ Exception -> 0x00bf }
                goto L_0x000e;
            L_0x0130:
                r0 = move-exception;
                r0 = r2;
                goto L_0x011d;
            L_0x0133:
                r0 = move-exception;
                r0 = r2;
                goto L_0x0113;
            L_0x0136:
                r0 = move-exception;
                r0 = r2;
                goto L_0x0107;
            L_0x0139:
                r3 = r1;
                goto L_0x004a;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.tencent.open" +
                        ".b.g.6.run():void");
            }
        });
    }
}
