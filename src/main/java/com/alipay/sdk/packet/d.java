package com.alipay.sdk.packet;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;
import android.widget.TextView;
import com.alipay.sdk.data.c;
import com.alipay.sdk.net.a;
import com.alipay.sdk.sys.b;
import com.alipay.sdk.util.i;
import com.boohee.one.http.DnspodFree;
import com.umeng.socialize.common.SocializeConstants;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class d {
    public static final String a = "msp-gzip";
    public static final String b = "Msp-Param";
    public static final String c = "Operation-Type";
    public static final String d = "content-type";
    public static final String e = "Version";
    public static final String f = "AppId";
    public static final String g = "des-mode";
    public static final String h = "namespace";
    public static final String i = "api_name";
    public static final String j = "api_version";
    public static final String k = "data";
    public static final String l = "params";
    public static final String m = "public_key";
    public static final String n = "device";
    public static final String o = "action";
    public static final String p = "type";
    public static final String q = "method";
    private static a t;
    protected boolean r = true;
    protected boolean s = true;

    protected abstract JSONObject a() throws JSONException;

    protected List<Header> a(boolean z, String str) {
        List<Header> arrayList = new ArrayList();
        arrayList.add(new BasicHeader(a, String.valueOf(z)));
        arrayList.add(new BasicHeader(c, "alipay.msp.cashier.dispatch.bytes"));
        arrayList.add(new BasicHeader(d, "application/octet-stream"));
        arrayList.add(new BasicHeader(e, "2.0"));
        arrayList.add(new BasicHeader(f, "TAOBAO"));
        arrayList.add(new BasicHeader(b, a.a(str)));
        arrayList.add(new BasicHeader(g, "CBC"));
        return arrayList;
    }

    protected String b() {
        return "4.9.0";
    }

    protected String c() throws JSONException {
        HashMap hashMap = new HashMap();
        hashMap.put("device", Build.MODEL);
        hashMap.put("namespace", "com.alipay.mobilecashier");
        hashMap.put(i, "com.alipay.mcpay");
        hashMap.put(j, b());
        return a(hashMap, new HashMap());
    }

    public static JSONObject a(String str, String str2) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        JSONObject jSONObject2 = new JSONObject();
        jSONObject2.put("type", str);
        jSONObject2.put("method", str2);
        jSONObject.put("action", jSONObject2);
        return jSONObject;
    }

    protected String a(String str, JSONObject jSONObject) {
        b a = b.a();
        com.alipay.sdk.tid.b a2 = com.alipay.sdk.tid.b.a();
        JSONObject a3 = com.alipay.sdk.util.b.a(new JSONObject(), jSONObject);
        try {
            String d;
            String str2;
            Object c;
            a3.put(com.alipay.sdk.cons.b.c, a2.a);
            String str3 = com.alipay.sdk.cons.b.b;
            c a4 = c.a();
            Context context = b.a().a;
            com.alipay.sdk.util.a a5 = com.alipay.sdk.util.a.a(context);
            if (TextUtils.isEmpty(a4.a)) {
                String a6 = i.a();
                String b = i.b();
                d = i.d(context);
                str2 = com.alipay.sdk.cons.a.a;
                a4.a = "Msp/15.0.4" + " (" + a6 + DnspodFree.IP_SPLIT + b + DnspodFree.IP_SPLIT + d + DnspodFree.IP_SPLIT + str2.substring(0, str2.indexOf("://")) + DnspodFree.IP_SPLIT + i.e(context) + DnspodFree.IP_SPLIT + Float.toString(new TextView(context).getTextSize());
            }
            d = com.alipay.sdk.util.a.b(context).a();
            str2 = "-1;-1";
            String str4 = "1";
            String a7 = a5.a();
            String b2 = a5.b();
            Context context2 = b.a().a;
            SharedPreferences sharedPreferences = context2.getSharedPreferences("virtualImeiAndImsi", 0);
            CharSequence string = sharedPreferences.getString("virtual_imsi", null);
            if (TextUtils.isEmpty(string)) {
                if (TextUtils.isEmpty(com.alipay.sdk.tid.b.a().a)) {
                    c = b.a().c();
                    string = TextUtils.isEmpty(c) ? c.b() : c.substring(3, 18);
                } else {
                    string = com.alipay.sdk.util.a.a(context2).a();
                }
                sharedPreferences.edit().putString("virtual_imsi", string).commit();
            }
            CharSequence charSequence = string;
            context2 = b.a().a;
            SharedPreferences sharedPreferences2 = context2.getSharedPreferences("virtualImeiAndImsi", 0);
            string = sharedPreferences2.getString("virtual_imei", null);
            if (TextUtils.isEmpty(string)) {
                string = TextUtils.isEmpty(com.alipay.sdk.tid.b.a().a) ? c.b() : com.alipay.sdk.util.a.a(context2).b();
                sharedPreferences2.edit().putString("virtual_imei", string).commit();
            }
            CharSequence charSequence2 = string;
            if (a2 != null) {
                a4.c = a2.b;
            }
            String replace = Build.MANUFACTURER.replace(DnspodFree.IP_SPLIT, " ");
            String replace2 = Build.MODEL.replace(DnspodFree.IP_SPLIT, " ");
            boolean b3 = b.b();
            String c2 = a5.c();
            WifiInfo connectionInfo = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo();
            String ssid = connectionInfo != null ? connectionInfo.getSSID() : "-1";
            connectionInfo = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo();
            String bssid = connectionInfo != null ? connectionInfo.getBSSID() : "00";
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(a4.a).append(DnspodFree.IP_SPLIT).append(d).append(DnspodFree.IP_SPLIT).append(str2).append(DnspodFree.IP_SPLIT).append(str4).append(DnspodFree.IP_SPLIT).append(a7).append(DnspodFree.IP_SPLIT).append(b2).append(DnspodFree.IP_SPLIT).append(a4.c).append(DnspodFree.IP_SPLIT).append(replace).append(DnspodFree.IP_SPLIT).append(replace2).append(DnspodFree.IP_SPLIT).append(b3).append(DnspodFree.IP_SPLIT).append(c2).append(";-1;-1;").append(a4.b).append(DnspodFree.IP_SPLIT).append(charSequence).append(DnspodFree.IP_SPLIT).append(charSequence2).append(DnspodFree.IP_SPLIT).append(ssid).append(DnspodFree.IP_SPLIT).append(bssid);
            if (a2 != null) {
                HashMap hashMap = new HashMap();
                hashMap.put(com.alipay.sdk.cons.b.c, a2.a);
                hashMap.put(com.alipay.sdk.cons.b.g, b.a().c());
                c = c.a(context, hashMap);
                if (!TextUtils.isEmpty(c)) {
                    stringBuilder.append(DnspodFree.IP_SPLIT).append(c);
                }
            }
            stringBuilder.append(SocializeConstants.OP_CLOSE_PAREN);
            a3.put(str3, stringBuilder.toString());
            a3.put(com.alipay.sdk.cons.b.e, i.b(a.a));
            a3.put(com.alipay.sdk.cons.b.f, i.a(a.a));
            a3.put(com.alipay.sdk.cons.b.d, str);
            a3.put(com.alipay.sdk.cons.b.h, com.alipay.sdk.cons.a.c);
            a3.put(com.alipay.sdk.cons.b.g, a.c());
            a3.put(com.alipay.sdk.cons.b.j, a2.b);
        } catch (Throwable th) {
        }
        return a3.toString();
    }

    private static boolean a(HttpResponse httpResponse) {
        String str = null;
        String str2 = a;
        if (httpResponse != null) {
            Header[] allHeaders = httpResponse.getAllHeaders();
            if (allHeaders != null && allHeaders.length > 0) {
                for (Header header : allHeaders) {
                    if (header != null) {
                        String name = header.getName();
                        if (name != null && name.equalsIgnoreCase(str2)) {
                            str = header.getValue();
                            break;
                        }
                    }
                }
            }
        }
        return Boolean.valueOf(str).booleanValue();
    }

    private static String a(HttpResponse httpResponse, String str) {
        if (httpResponse == null || str == null) {
            return null;
        }
        Header[] allHeaders = httpResponse.getAllHeaders();
        if (allHeaders == null || allHeaders.length <= 0) {
            return null;
        }
        for (Header header : allHeaders) {
            if (header != null) {
                String name = header.getName();
                if (name != null && name.equalsIgnoreCase(str)) {
                    return header.getValue();
                }
            }
        }
        return null;
    }

    protected static String a(HashMap<String, String> hashMap, HashMap<String, String> hashMap2) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        JSONObject jSONObject2 = new JSONObject();
        if (hashMap != null) {
            for (Entry entry : hashMap.entrySet()) {
                jSONObject2.put((String) entry.getKey(), entry.getValue());
            }
        }
        if (hashMap2 != null) {
            JSONObject jSONObject3 = new JSONObject();
            for (Entry entry2 : hashMap2.entrySet()) {
                jSONObject3.put((String) entry2.getKey(), entry2.getValue());
            }
            jSONObject2.put("params", jSONObject3);
        }
        jSONObject.put("data", jSONObject2);
        return jSONObject.toString();
    }

    private static boolean a(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        try {
            JSONObject jSONObject = new JSONObject(str).getJSONObject("data");
            if (!jSONObject.has("params")) {
                return false;
            }
            String optString = jSONObject.getJSONObject("params").optString(m, null);
            if (TextUtils.isEmpty(optString)) {
                return false;
            }
            b.a();
            c.a().a(optString);
            return true;
        } catch (JSONException e) {
            return false;
        }
    }

    private static a b(Context context, String str) {
        if (t == null) {
            t = new a(context, str);
        } else if (!TextUtils.equals(str, t.b)) {
            t.b = str;
        }
        return t;
    }

    public final b a(Context context) throws Throwable {
        return a(context, "", com.alipay.sdk.cons.a.a, true);
    }

    public b a(Context context, String str) throws Throwable {
        return a(context, str, com.alipay.sdk.cons.a.a, true);
    }

    private b a(Context context, String str, String str2) throws Throwable {
        return a(context, str, str2, true);
    }

    protected final b a(Context context, String str, String str2, boolean z) throws Throwable {
        "PacketTask::request url >" + str2;
        e eVar = new e(this.s);
        c a = eVar.a(new b(c(), a(str, a())), this.r);
        if (t == null) {
            t = new a(context, str2);
        } else if (!TextUtils.equals(str2, t.b)) {
            t.b = str2;
        }
        HttpResponse a2 = t.a(a.b, a(a.a, str));
        b a3 = eVar.a(new c(a(a2), b(a2)));
        if (a3 != null && a(a3.a) && z) {
            return a(context, str, str2, false);
        }
        return a3;
    }

    private static byte[] b(HttpResponse httpResponse) throws IllegalStateException, IOException {
        Throwable th;
        ByteArrayOutputStream byteArrayOutputStream = null;
        byte[] bArr = new byte[1024];
        InputStream content;
        try {
            content = httpResponse.getEntity().getContent();
            try {
                ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
                while (true) {
                    try {
                        int read = content.read(bArr);
                        if (read == -1) {
                            break;
                        }
                        byteArrayOutputStream2.write(bArr, 0, read);
                    } catch (Throwable th2) {
                        th = th2;
                        byteArrayOutputStream = byteArrayOutputStream2;
                    }
                }
                bArr = byteArrayOutputStream2.toByteArray();
                if (content != null) {
                    try {
                        content.close();
                    } catch (Exception e) {
                    }
                }
                try {
                    byteArrayOutputStream2.close();
                } catch (Exception e2) {
                }
                return bArr;
            } catch (Throwable th3) {
                th = th3;
                if (content != null) {
                    try {
                        content.close();
                    } catch (Exception e3) {
                    }
                }
                if (byteArrayOutputStream != null) {
                    try {
                        byteArrayOutputStream.close();
                    } catch (Exception e4) {
                    }
                }
                throw th;
            }
        } catch (Throwable th4) {
            th = th4;
            content = null;
            if (content != null) {
                content.close();
            }
            if (byteArrayOutputStream != null) {
                byteArrayOutputStream.close();
            }
            throw th;
        }
    }
}
