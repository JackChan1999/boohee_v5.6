package com.alipay.apmobilesecuritysdk.a.a;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public final class b {
    public static Map<String, String> a(String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            Iterator keys = jSONObject.keys();
            Map<String, String> hashMap = new HashMap();
            while (keys.hasNext()) {
                String str2 = (String) keys.next();
                hashMap.put(str2, (String) jSONObject.get(str2));
            }
            return hashMap;
        } catch (JSONException e) {
            return null;
        }
    }
}
