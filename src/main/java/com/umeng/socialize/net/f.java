package com.umeng.socialize.net;

import android.content.Context;
import android.text.TextUtils;

import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.UMToken;
import com.umeng.socialize.net.base.SocializeRequest;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import com.umeng.socialize.utils.SocializeUtils;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: PlatformKeyUploadReq */
public class f extends SocializeRequest {
    private static final String a = "/share/token/";
    private static final int    b = 21;
    private UMToken c;

    public f(Context context, SocializeEntity socializeEntity, UMToken uMToken) {
        super(context, "", g.class, socializeEntity, 21, RequestMethod.POST);
        this.c = uMToken;
    }

    protected Map<String, Object> addSelfParams(Map<String, Object> map) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("usid", this.c.mUsid);
            jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_SHARE_TO, this.c.mPaltform);
            jSONObject.put("access_token", this.c.getToken());
            jSONObject.put("expires_in", this.c.getExpireIn());
            if (!TextUtils.isEmpty(this.c.getOpenId())) {
                jSONObject.put("openid", this.c.getOpenId());
            }
            CharSequence appId = this.c.getAppId();
            if (!TextUtils.isEmpty(appId)) {
                jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_APP_ID, appId);
            }
            appId = this.c.getAppKey();
            if (!TextUtils.isEmpty(appId)) {
                jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_APP_ID, appId);
            }
            appId = this.c.getRefreshToken();
            if (!TextUtils.isEmpty(appId)) {
                jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_REFRESH_TOKEN, appId);
            }
            appId = this.c.getScope();
            if (!TextUtils.isEmpty(appId)) {
                jSONObject.put("scope", appId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return packParamsMap(TAG, addParamsToJson(jSONObject, map).toString());
    }

    protected String getPath() {
        return a + SocializeUtils.getAppkey(this.mContext) + "/";
    }
}
