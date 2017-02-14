package com.alipay.sdk.packet.impl;

import android.text.TextUtils;
import com.alipay.sdk.cons.a;
import com.alipay.sdk.packet.d;
import com.alipay.sdk.sys.b;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import org.json.JSONException;
import org.json.JSONObject;

public final class c extends d {
    protected final JSONObject a() throws JSONException {
        return d.a("device", "findAccount");
    }

    protected final String a(String str, JSONObject jSONObject) {
        b a = b.a();
        JSONObject a2 = com.alipay.sdk.util.b.a(new JSONObject(), jSONObject);
        com.alipay.sdk.tid.b a3 = com.alipay.sdk.tid.b.a();
        try {
            if (!TextUtils.isEmpty(a3.a)) {
                a2.put(com.alipay.sdk.cons.b.c, a3.a);
            }
            a2.put(com.alipay.sdk.cons.b.g, a.c());
            a2.put(com.alipay.sdk.cons.b.h, a.c);
            a2.put(com.alipay.sdk.cons.b.j, a3.b);
            a2.put(SocializeProtocolConstants.PROTOCOL_KEY_IMEI, com.alipay.sdk.util.a.a(a.a).b());
            a2.put("imsi", com.alipay.sdk.util.a.a(a.a).a());
        } catch (JSONException e) {
        }
        return a2.toString();
    }
}
