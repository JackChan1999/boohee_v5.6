package com.umeng.socialize.net;

import com.umeng.socialize.net.base.SocializeReseponse;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;

/* compiled from: ExpiresInResponse */
public class d extends SocializeReseponse {
    public Map<String, Object> a;

    public d(JSONObject jSONObject) {
        super(jSONObject);
    }

    public void parseJsonObject() {
        super.parseJsonObject();
        JSONObject jSONObject = this.mJsonData;
        if (jSONObject != null) {
            this.a = new HashMap();
            Iterator keys = jSONObject.keys();
            String str = "";
            Boolean.valueOf(false);
            while (keys.hasNext()) {
                str = (String) keys.next();
                this.a.put(str, Boolean.valueOf(jSONObject.optBoolean(str, false)));
            }
        }
    }
}
