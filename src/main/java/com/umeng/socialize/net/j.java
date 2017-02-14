package com.umeng.socialize.net;

import android.content.Context;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.common.SocializeConstants;
import com.umeng.socialize.net.base.SocializeRequest;
import com.umeng.socialize.net.base.SocializeReseponse;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import com.umeng.socialize.utils.SocializeUtils;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: ShareDeleteOauthRequest */
public class j extends SocializeRequest {
    private static final String a = "/share/auth_delete/";
    private static final int    b = 15;
    private SHARE_MEDIA c;

    public j(Context context, SocializeEntity socializeEntity, SHARE_MEDIA share_media) {
        super(context, "", SocializeReseponse.class, socializeEntity, 15, RequestMethod.POST);
        this.mContext = context;
        this.c = share_media;
    }

    protected Map<String, Object> addSelfParams(Map<String, Object> map) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_UID, SocializeConstants.UID);
            jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_VERIFY_MEDIA, this.c.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return packParamsMap(TAG, addParamsToJson(jSONObject, map).toString());
    }

    protected String getPath() {
        return a + SocializeUtils.getAppkey(this.mContext) + "/";
    }
}
