package com.umeng.socialize.net;

import com.umeng.socialize.net.base.SocializeReseponse;
import com.umeng.socialize.utils.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: UserInfoResponse */
public class y extends SocializeReseponse {
    public Map<String, Object> a;

    public y(JSONObject jSONObject) {
        super(jSONObject);
    }

    public void parseJsonObject() {
        this.a = new HashMap();
        try {
            JSONObject jSONObject = this.mJsonData;
            Iterator keys = jSONObject.keys();
            if (keys != null) {
                while (keys.hasNext()) {
                    try {
                        String str = (String) keys.next();
                        this.a.put(str, jSONObject.get(str));
                    } catch (JSONException e) {
                        Log.w("com.umeng.socialize", e.toString());
                    }
                }
            }
        } catch (Exception e2) {
            Log.w("com.umeng.socialize", e2.toString());
        }
    }
}
