package com.umeng.socialize.net;

import com.umeng.socialize.net.base.SocializeReseponse;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import com.umeng.socialize.utils.Log;

import org.json.JSONObject;

/* compiled from: ActionBarResponse */
public class b extends SocializeReseponse {
    public int    a;
    public int    b;
    public int    c;
    public String d;
    public String e;
    public int    f;
    public int    g;
    public String h;
    public String i;
    public int    j;

    public b(JSONObject jSONObject) {
        super(jSONObject);
    }

    public void parseJsonObject() {
        JSONObject jSONObject = this.mJsonData;
        if (jSONObject == null) {
            Log.e(SocializeReseponse.TAG, "data json is null....");
            return;
        }
        try {
            if (jSONObject.has(SocializeProtocolConstants.PROTOCOL_KEY_COMMENT_COUNT)) {
                this.b = jSONObject.getInt(SocializeProtocolConstants.PROTOCOL_KEY_COMMENT_COUNT);
            }
            if (jSONObject.has(SocializeProtocolConstants.PROTOCOL_KEY_ENTITY_KEY)) {
                this.e = jSONObject.getString(SocializeProtocolConstants.PROTOCOL_KEY_ENTITY_KEY);
            }
            if (jSONObject.has(SocializeProtocolConstants.PROTOCOL_KEY_FRIST_TIME)) {
                this.f = jSONObject.getInt(SocializeProtocolConstants.PROTOCOL_KEY_FRIST_TIME);
            }
            if (jSONObject.has(SocializeProtocolConstants.PROTOCOL_KEY_FR)) {
                this.g = jSONObject.optInt(SocializeProtocolConstants.PROTOCOL_KEY_FR, 0);
            }
            if (jSONObject.has(SocializeProtocolConstants.PROTOCOL_KEY_LIKE_COUNT)) {
                this.c = jSONObject.getInt(SocializeProtocolConstants.PROTOCOL_KEY_LIKE_COUNT);
            }
            if (jSONObject.has(SocializeProtocolConstants.PROTOCOL_KEY_PV)) {
                this.a = jSONObject.getInt(SocializeProtocolConstants.PROTOCOL_KEY_PV);
            }
            if (jSONObject.has(SocializeProtocolConstants.PROTOCOL_KEY_SID)) {
                this.d = jSONObject.getString(SocializeProtocolConstants.PROTOCOL_KEY_SID);
            }
            if (jSONObject.has(SocializeProtocolConstants.PROTOCOL_KEY_UID)) {
                this.h = jSONObject.getString(SocializeProtocolConstants.PROTOCOL_KEY_UID);
            }
            if (jSONObject.has(SocializeProtocolConstants.PROTOCOL_KEY_SHARE_NUM)) {
                this.j = jSONObject.getInt(SocializeProtocolConstants.PROTOCOL_KEY_SHARE_NUM);
            }
        } catch (Exception e) {
            Log.e(TAG, "Parse json error[ " + jSONObject.toString() + " ]", e);
        }
    }
}
