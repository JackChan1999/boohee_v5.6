package com.pingplusplus.android;

import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;

import com.alipay.sdk.sys.a;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class PingppDataCollection {
    private static String[] a     = new String[]{SocializeProtocolConstants.PROTOCOL_KEY_APP_ID,
            "device_id", "enter_time", "sdk_type", "sdk_version", "system", "system_version"};
    public         String   appId = "null";
    public List    chIds;
    public Map     channels;
    public String  deviceId;
    public Long    enterTime;
    public String  firstChannel;
    public Object  gps;
    public String  ip;
    public String  lastChannel;
    public List    nocard;
    public Long    quitTime;
    public Integer sdkType;
    public String  sdkVersion;
    public String system        = "Android";
    public String systemVersion = (Build.MODEL + "," + VERSION.RELEASE);

    public PingppDataCollection(Context context) {
        PingppLog.a("systemVersion=" + this.systemVersion);
        this.sdkVersion = PaymentActivity.getVersion();
        this.deviceId = l.a(context).b();
        PingppLog.a(this.deviceId);
        this.chIds = new ArrayList();
        this.channels = new HashMap();
        this.enterTime = Long.valueOf(e());
    }

    private void a(String str) {
        this.chIds.add(str);
    }

    private void b(String str) {
        if (this.firstChannel == null) {
            this.firstChannel = str;
        }
        this.lastChannel = str;
        Object valueOf = Integer.valueOf(1);
        if (this.channels.containsKey(str)) {
            valueOf = Integer.valueOf(((Integer) this.channels.get(str)).intValue() + valueOf
                    .intValue());
        }
        this.channels.put(str, valueOf);
    }

    private Object c(String str) {
        try {
            return getClass().getDeclaredField(str).get(this);
        } catch (Exception e) {
            PingppLog.a(e);
            return null;
        }
    }

    private String d() {
        return l.b(b());
    }

    private static long e() {
        return new Date().getTime() / 1000;
    }

    private String[] f() {
        Field[] declaredFields = getClass().getDeclaredFields();
        List arrayList = new ArrayList();
        for (Field field : declaredFields) {
            if (Modifier.isPublic(field.getModifiers()) && !Modifier.isStatic(field.getModifiers
                    ())) {
                arrayList.add(field.getName());
            }
        }
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    public Map a() {
        Map hashMap = new HashMap();
        for (String str : f()) {
            Object c = c(str);
            if (c != null) {
                hashMap.put(l.a(str), c);
            }
        }
        return hashMap;
    }

    public void a(h hVar) {
        this.sdkType = hVar.c;
    }

    public void a(JSONObject jSONObject) {
        String string;
        try {
            a(jSONObject.getString("id"));
            b(jSONObject.getString("channel"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String str = null;
        try {
            str = jSONObject.getString("app");
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        if (str == null) {
            try {
                string = jSONObject.getJSONObject("app").getString("id");
            } catch (JSONException e22) {
                e22.printStackTrace();
            }
            if (string != null) {
                this.appId = string;
            }
        }
        string = str;
        if (string != null) {
            this.appId = string;
        }
    }

    public String b() {
        Map a = a();
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : a) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append(a.b);
            }
            stringBuilder.append(str).append("=").append(a.get(str) == null ? "" : a.get(str));
        }
        return stringBuilder.toString();
    }

    public void c() {
        this.quitTime = Long.valueOf(e());
    }

    public void sendToServer() {
        if (this.quitTime == null) {
            c();
        }
        Map hashMap = new HashMap();
        if (d() != null) {
            hashMap.put("X-Pingpp-Report-Token", d());
        }
        List arrayList = new ArrayList();
        arrayList.add(a());
        l.a().a("https://statistics.pingxx.com/report", arrayList, hashMap);
    }
}
