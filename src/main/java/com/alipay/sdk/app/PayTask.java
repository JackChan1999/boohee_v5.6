package com.alipay.sdk.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.alipay.sdk.data.c;
import com.alipay.sdk.sys.b;
import com.alipay.sdk.util.H5PayResultModel;
import com.alipay.sdk.util.e;
import com.alipay.sdk.util.i;
import com.boohee.one.http.DnspodFree;
import com.boohee.utility.TimeLinePatterns;
import com.qiniu.android.common.Constants;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import org.json.JSONObject;

public class PayTask {
    static final Object a = e.class;
    private static ConcurrentHashMap<String, String> h = new ConcurrentHashMap();
    private Activity b;
    private com.alipay.sdk.widget.a c;
    private String d = "wappaygw.alipay.com/service/rest.htm";
    private String e = "mclient.alipay.com/service/rest.htm";
    private String f = "mclient.alipay.com/home/exterfaceAssign.htm";
    private Map<String, a> g = new HashMap();

    private class a {
        String a;
        String b;
        final /* synthetic */ PayTask c;

        private a(PayTask payTask) {
            this.c = payTask;
            this.a = "";
            this.b = "";
        }

        private String a() {
            return this.a;
        }

        private void a(String str) {
            this.a = str;
        }

        private String b() {
            return this.b;
        }

        private void b(String str) {
            this.b = str;
        }
    }

    public PayTask(Activity activity) {
        this.b = activity;
        b a = b.a();
        Context context = this.b;
        c.a();
        a.a(context);
        com.alipay.sdk.app.statistic.a.a(activity);
        this.c = new com.alipay.sdk.widget.a(activity, com.alipay.sdk.widget.a.b);
    }

    public synchronized String pay(String str, boolean z) {
        String str2;
        if (z) {
            b();
        }
        str2 = "";
        try {
            str2 = a(str);
            com.alipay.sdk.data.a.b().a(this.b);
            c();
            com.alipay.sdk.app.statistic.a.a(this.b, str);
        } catch (Throwable th) {
            com.alipay.sdk.data.a.b().a(this.b);
            c();
            com.alipay.sdk.app.statistic.a.a(this.b, str);
        }
        return str2;
    }

    public synchronized String fetchOrderInfoFromH5PayUrl(String str) {
        String trim;
        if (!TextUtils.isEmpty(str)) {
            String trim2;
            trim = str.trim();
            if (trim.startsWith("https://" + this.d) || trim.startsWith(new StringBuilder(TimeLinePatterns.WEB_SCHEME).append(this.d).toString())) {
                trim2 = trim.replaceFirst("(http|https)://" + this.d + "\\?", "").trim();
                if (!TextUtils.isEmpty(trim2)) {
                    trim = "_input_charset=\"utf-8\"&ordertoken=\"" + i.a("<request_token>", "</request_token>", (String) i.a(trim2).get("req_data")) + "\"&pay_channel_id=\"alipay_sdk\"";
                }
            }
            try {
                if (trim.startsWith("https://" + this.e) || trim.startsWith(new StringBuilder(TimeLinePatterns.WEB_SCHEME).append(this.e).toString())) {
                    trim2 = trim.replaceFirst("(http|https)://" + this.e + "\\?", "").trim();
                    if (!TextUtils.isEmpty(trim2)) {
                        trim = "_input_charset=\"utf-8\"&ordertoken=\"" + i.a("<request_token>", "</request_token>", (String) i.a(trim2).get("req_data")) + "\"&pay_channel_id=\"alipay_sdk\"";
                    }
                }
                if ((trim.startsWith("https://" + this.f) || trim.startsWith(new StringBuilder(TimeLinePatterns.WEB_SCHEME).append(this.f).toString())) && trim.contains("alipay.wap.create.direct.pay.by.user") && !TextUtils.isEmpty(trim.replaceFirst("(http|https)://" + this.f + "\\?", "").trim())) {
                    try {
                        JSONObject jSONObject = new JSONObject();
                        jSONObject.put("url", str);
                        jSONObject.put("bizcontext", new com.alipay.sdk.sys.a(this.b).a("sdk_pay_channle", "h5Tonative"));
                        trim = "new_external_info==" + jSONObject.toString();
                    } catch (Throwable th) {
                    }
                }
                if (Pattern.compile("^(http|https)://(maliprod\\.alipay\\.com\\/w\\/trade_pay\\.do.?|mali\\.alipay\\.com\\/w\\/trade_pay\\.do.?|mclient\\.alipay\\.com\\/w\\/trade_pay\\.do.?)").matcher(str).find()) {
                    trim = i.a("?", "", str);
                    if (!TextUtils.isEmpty(trim)) {
                        Map a = i.a(trim);
                        StringBuilder stringBuilder = new StringBuilder();
                        if (a(false, true, com.alipay.sdk.app.statistic.c.q, stringBuilder, a, com.alipay.sdk.app.statistic.c.q, "alipay_trade_no")) {
                            a(true, false, "pay_phase_id", stringBuilder, a, "payPhaseId", "pay_phase_id", "out_relation_id");
                            stringBuilder.append("&biz_sub_type=\"TRADE\"");
                            stringBuilder.append("&biz_type=\"trade\"");
                            trim = (String) a.get(SocializeProtocolConstants.PROTOCOL_KEY_APP_NAME);
                            if (TextUtils.isEmpty(trim) && !TextUtils.isEmpty((CharSequence) a.get("cid"))) {
                                trim = "ali1688";
                            } else if (TextUtils.isEmpty(trim) && !(TextUtils.isEmpty((CharSequence) a.get(SocializeProtocolConstants.PROTOCOL_KEY_SID)) && TextUtils.isEmpty((CharSequence) a.get("s_id")))) {
                                trim = "tb";
                            }
                            stringBuilder.append("&app_name=\"" + trim + com.alipay.sdk.sys.a.e);
                            if (a(true, true, "extern_token", stringBuilder, a, "extern_token", "cid", SocializeProtocolConstants.PROTOCOL_KEY_SID, "s_id")) {
                                a(true, false, "appenv", stringBuilder, a, "appenv");
                                stringBuilder.append("&pay_channel_id=\"alipay_sdk\"");
                                a aVar = new a();
                                aVar.a = (String) a.get("return_url");
                                aVar.b = (String) a.get("pay_order_id");
                                this.g.put(stringBuilder.toString(), aVar);
                                trim = stringBuilder.toString();
                            } else {
                                trim = "";
                            }
                        }
                    }
                }
            } catch (Throwable th2) {
            }
        }
        trim = "";
        return trim;
    }

    private static boolean a(boolean z, boolean z2, String str, StringBuilder stringBuilder, Map<String, String> map, String... strArr) {
        String str2;
        String str3 = "";
        for (Object obj : strArr) {
            if (!TextUtils.isEmpty((CharSequence) map.get(obj))) {
                str2 = (String) map.get(obj);
                break;
            }
        }
        str2 = str3;
        if (TextUtils.isEmpty(str2)) {
            if (z2) {
                return false;
            }
        } else if (z) {
            stringBuilder.append(com.alipay.sdk.sys.a.b).append(str).append("=\"").append(str2).append(com.alipay.sdk.sys.a.e);
        } else {
            stringBuilder.append(str).append("=\"").append(str2).append(com.alipay.sdk.sys.a.e);
        }
        return true;
    }

    public synchronized H5PayResultModel h5Pay(String str, boolean z) {
        H5PayResultModel h5PayResultModel;
        synchronized (this) {
            H5PayResultModel h5PayResultModel2 = new H5PayResultModel();
            try {
                str.trim();
                String[] split = pay(str, z).split(DnspodFree.IP_SPLIT);
                Map hashMap = new HashMap();
                for (String str2 : split) {
                    String substring = str2.substring(0, str2.indexOf("={"));
                    String str3 = substring + "={";
                    hashMap.put(substring, str2.substring(str3.length() + str2.indexOf(str3), str2.lastIndexOf("}")));
                }
                if (hashMap.containsKey("resultStatus")) {
                    h5PayResultModel2.setResultCode((String) hashMap.get("resultStatus"));
                }
                if (hashMap.containsKey("callBackUrl")) {
                    h5PayResultModel2.setReturnUrl((String) hashMap.get("callBackUrl"));
                } else if (hashMap.containsKey("result")) {
                    try {
                        String str4 = (String) hashMap.get("result");
                        if (str4.length() > 15) {
                            a aVar = (a) this.g.get(str);
                            if (aVar != null) {
                                if (TextUtils.isEmpty(aVar.b)) {
                                    h5PayResultModel2.setReturnUrl(aVar.a);
                                } else {
                                    h5PayResultModel2.setReturnUrl(com.alipay.sdk.data.a.b().i.replace("$OrderId$", aVar.b));
                                }
                                this.g.remove(str);
                                h5PayResultModel = h5PayResultModel2;
                            } else {
                                CharSequence a = i.a("&callBackUrl=\"", com.alipay.sdk.sys.a.e, str4);
                                if (TextUtils.isEmpty(a)) {
                                    a = i.a("&call_back_url=\"", com.alipay.sdk.sys.a.e, str4);
                                    if (TextUtils.isEmpty(a)) {
                                        a = i.a("&return_url=\"", com.alipay.sdk.sys.a.e, str4);
                                        if (TextUtils.isEmpty(a)) {
                                            a = URLDecoder.decode(i.a("&return_url=", com.alipay.sdk.sys.a.b, str4), Constants.UTF_8);
                                            if (TextUtils.isEmpty(a)) {
                                                str4 = URLDecoder.decode(i.a("&callBackUrl=", com.alipay.sdk.sys.a.b, str4), Constants.UTF_8);
                                                if (TextUtils.isEmpty(str4)) {
                                                    str4 = com.alipay.sdk.data.a.b().i;
                                                }
                                                h5PayResultModel2.setReturnUrl(URLDecoder.decode(str4, Constants.UTF_8));
                                            }
                                        }
                                    }
                                }
                                CharSequence charSequence = a;
                                if (TextUtils.isEmpty(str4)) {
                                    str4 = com.alipay.sdk.data.a.b().i;
                                }
                                h5PayResultModel2.setReturnUrl(URLDecoder.decode(str4, Constants.UTF_8));
                            }
                        } else {
                            a aVar2 = (a) this.g.get(str);
                            if (aVar2 != null) {
                                h5PayResultModel2.setReturnUrl(aVar2.a);
                                this.g.remove(str);
                                h5PayResultModel = h5PayResultModel2;
                            }
                        }
                    } catch (Throwable th) {
                    }
                }
            } catch (Throwable th2) {
            }
            h5PayResultModel = h5PayResultModel2;
        }
        return h5PayResultModel;
    }

    private static String a(String str, String str2) {
        String str3 = str2 + "={";
        return str.substring(str3.length() + str.indexOf(str3), str.lastIndexOf("}"));
    }

    private com.alipay.sdk.util.e.a a() {
        return new m(this);
    }

    private void b() {
        if (this.c != null) {
            this.c.a();
        }
    }

    private void c() {
        if (this.c != null) {
            this.c.b();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String a(java.lang.String r5) {
        /*
        r4 = this;
        r0 = new com.alipay.sdk.sys.a;
        r1 = r4.b;
        r0.<init>(r1);
        r1 = r0.a(r5);
        r2 = h;
        monitor-enter(r2);
        r0 = h;	 Catch:{ all -> 0x0053 }
        r0 = r0.get(r5);	 Catch:{ all -> 0x0053 }
        r0 = (java.lang.CharSequence) r0;	 Catch:{ all -> 0x0053 }
        r0 = android.text.TextUtils.isEmpty(r0);	 Catch:{ all -> 0x0053 }
        if (r0 != 0) goto L_0x0037;
    L_0x001c:
        r0 = com.alipay.sdk.app.o.DOUBLE_REQUEST;	 Catch:{ all -> 0x0053 }
        r0 = r0.a();	 Catch:{ all -> 0x0053 }
        r0 = com.alipay.sdk.app.o.a(r0);	 Catch:{ all -> 0x0053 }
        r1 = r0.a();	 Catch:{ all -> 0x0053 }
        r0 = r0.b();	 Catch:{ all -> 0x0053 }
        r3 = "";
        r0 = com.alipay.sdk.app.n.a(r1, r0, r3);	 Catch:{ all -> 0x0053 }
        monitor-exit(r2);	 Catch:{ all -> 0x0053 }
    L_0x0036:
        return r0;
    L_0x0037:
        r0 = h;	 Catch:{ all -> 0x0053 }
        r3 = "true";
        r0.put(r5, r3);	 Catch:{ all -> 0x0053 }
        monitor-exit(r2);	 Catch:{ all -> 0x0053 }
        r0 = "paymethod=\"expressGateway\"";
        r0 = r1.contains(r0);
        if (r0 == 0) goto L_0x0056;
    L_0x0049:
        r0 = r4.b(r1);
        r1 = h;
        r1.remove(r5);
        goto L_0x0036;
    L_0x0053:
        r0 = move-exception;
        monitor-exit(r2);
        throw r0;
    L_0x0056:
        r0 = r4.b;
        r0 = com.alipay.sdk.util.i.b(r0);
        if (r0 == 0) goto L_0x009a;
    L_0x005e:
        r2 = new com.alipay.sdk.util.e;
        r0 = r4.b;
        r3 = new com.alipay.sdk.app.m;
        r3.<init>(r4);
        r2.<init>(r0, r3);
        r0 = r2.a(r1);
        r2.a();
        r2 = "failed";
        r2 = android.text.TextUtils.equals(r0, r2);
        if (r2 == 0) goto L_0x0084;
    L_0x007a:
        r0 = r4.b(r1);
        r1 = h;
        r1.remove(r5);
        goto L_0x0036;
    L_0x0084:
        r1 = android.text.TextUtils.isEmpty(r0);
        if (r1 == 0) goto L_0x0094;
    L_0x008a:
        r0 = com.alipay.sdk.app.n.a();
        r1 = h;
        r1.remove(r5);
        goto L_0x0036;
    L_0x0094:
        r1 = h;
        r1.remove(r5);
        goto L_0x0036;
    L_0x009a:
        r0 = r4.b(r1);
        r1 = h;
        r1.remove(r5);
        goto L_0x0036;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alipay.sdk.app.PayTask.a(java.lang.String):java.lang.String");
    }

    public String getVersion() {
        return "15.0.4";
    }

    public boolean checkAccountIfExist() {
        boolean z = false;
        b a = b.a();
        Context context = this.b;
        c.a();
        a.a(context);
        try {
            z = new com.alipay.sdk.packet.impl.c().a(this.b).a().optBoolean("hasAccount", false);
        } catch (Throwable th) {
        }
        return z;
    }

    private String b(String str) {
        o oVar;
        int i = 0;
        b();
        com.alipay.sdk.tid.a aVar;
        try {
            List a = com.alipay.sdk.protocol.b.a(new com.alipay.sdk.packet.impl.e().a(this.b, str).a().optJSONObject(com.alipay.sdk.cons.c.c).optJSONObject(com.alipay.sdk.cons.c.d));
            for (int i2 = 0; i2 < a.size(); i2++) {
                if (((com.alipay.sdk.protocol.b) a.get(i2)).a == com.alipay.sdk.protocol.a.Update) {
                    String[] strArr = ((com.alipay.sdk.protocol.b) a.get(i2)).b;
                    if (strArr.length == 3 && TextUtils.equals(com.alipay.sdk.cons.b.c, strArr[0])) {
                        Context context = b.a().a;
                        com.alipay.sdk.tid.b a2 = com.alipay.sdk.tid.b.a();
                        if (!(TextUtils.isEmpty(strArr[1]) || TextUtils.isEmpty(strArr[2]))) {
                            a2.a = strArr[1];
                            a2.b = strArr[2];
                            aVar = new com.alipay.sdk.tid.a(context);
                            aVar.a(com.alipay.sdk.util.a.a(context).a(), com.alipay.sdk.util.a.a(context).b(), a2.a, a2.b);
                            aVar.close();
                        }
                    }
                }
            }
            c();
            while (i < a.size()) {
                if (((com.alipay.sdk.protocol.b) a.get(i)).a == com.alipay.sdk.protocol.a.WapPay) {
                    String a3 = a((com.alipay.sdk.protocol.b) a.get(i));
                    c();
                    return a3;
                }
                i++;
            }
            c();
            oVar = null;
        } catch (Exception e) {
            aVar.close();
        } catch (Throwable e2) {
            o a4 = o.a(o.NETWORK_ERROR.a());
            com.alipay.sdk.app.statistic.a.a(com.alipay.sdk.app.statistic.c.a, e2);
            c();
            oVar = a4;
        } catch (Throwable th) {
            c();
        }
        if (oVar == null) {
            oVar = o.a(o.FAILED.a());
        }
        return n.a(oVar.a(), oVar.b(), "");
    }

    private String a(com.alipay.sdk.protocol.b bVar) {
        String[] strArr = bVar.b;
        Intent intent = new Intent(this.b, H5PayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("url", strArr[0]);
        if (strArr.length == 2) {
            bundle.putString("cookie", strArr[1]);
        }
        intent.putExtras(bundle);
        this.b.startActivity(intent);
        synchronized (a) {
            try {
                a.wait();
            } catch (InterruptedException e) {
                return n.a();
            }
        }
        String str = n.a;
        if (TextUtils.isEmpty(str)) {
            return n.a();
        }
        return str;
    }
}
