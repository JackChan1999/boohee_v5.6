package com.umeng.socialize.net;

import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.net.base.SocializeReseponse;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: ShareMultiFollowResponse */
public class n extends SocializeReseponse {
    public Map<String, Integer> a;

    public n(JSONObject jSONObject) {
        super(jSONObject);
    }

    public void parseJsonObject() {
        super.parseJsonObject();
        this.a = new HashMap();
        Iterator keys = this.mJsonData.keys();
        while (keys.hasNext()) {
            String str = (String) keys.next();
            try {
                JSONObject jSONObject = this.mJsonData.getJSONObject(str);
                this.a.put(str, Integer.valueOf(jSONObject != null ? jSONObject.optInt
                        (SocializeProtocolConstants.PROTOCOL_KEY_ST, StatusCode
                                .ST_CODE_SDK_UNKNOW) : StatusCode.ST_CODE_SDK_UNKNOW));
            } catch (JSONException e) {
            }
        }
    }
}
