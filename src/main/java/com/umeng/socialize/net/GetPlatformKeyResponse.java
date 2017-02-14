package com.umeng.socialize.net;

import android.text.TextUtils;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.net.base.SocializeReseponse;
import com.umeng.socialize.utils.Log;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class GetPlatformKeyResponse extends SocializeReseponse {
    public Map<String, Object> mData;
    public Map<String, String> mExtraData;
    public Map<String, String> mSecrets;

    public GetPlatformKeyResponse(JSONObject jSONObject) {
        super(jSONObject);
    }

    public void parseJsonObject() {
        JSONObject jSONObject = this.mJsonData;
        this.mData = new HashMap();
        this.mSecrets = new HashMap();
        this.mExtraData = new HashMap();
        if (jSONObject == null) {
            Log.e(SocializeReseponse.TAG, "data json is null....");
            return;
        }
        try {
            for (SHARE_MEDIA share_media : SHARE_MEDIA.getDefaultPlatform()) {
                try {
                    JSONObject optJSONObject = jSONObject.optJSONObject(share_media.toString());
                    if (optJSONObject != null) {
                        String string = optJSONObject.getString("key");
                        this.mData.put(share_media.toString(), string);
                        this.mSecrets.put(share_media.toString(), optJSONObject.optString
                                ("secret"));
                        CharSequence optString = optJSONObject.optString("id");
                        if (!TextUtils.isEmpty(optString)) {
                            this.mExtraData.put(string, optString);
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Parse json error[ " + jSONObject.toString() + " ]", e);
                }
            }
            Log.i(TAG, "platform key found: " + this.mData.keySet().toString());
        } catch (Exception e2) {
            Log.e(TAG, "Parse json error[ " + jSONObject.toString() + " ]", e2);
        }
    }
}
