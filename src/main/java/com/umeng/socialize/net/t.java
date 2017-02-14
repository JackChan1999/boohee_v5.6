package com.umeng.socialize.net;

import android.content.Context;
import android.text.TextUtils;

import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.common.SocializeConstants;
import com.umeng.socialize.net.base.SocializeRequest;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import com.umeng.socialize.utils.SocializeUtils;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: UpdatePlatformKeyRequest */
public class t extends SocializeRequest {
    private static final String a = "/share/keysecret/";
    private static final int    b = 25;

    public t(Context context, SocializeEntity socializeEntity) {
        super(context, "", u.class, socializeEntity, 25, RequestMethod.POST);
        this.mContext = context;
    }

    protected Map<String, Object> addSelfParams(Map<String, Object> map) {
        JSONObject jSONObject = new JSONObject();
        CharSequence extra = this.mEntity.getExtra(SocializeConstants.FIELD_WX_APPID);
        String extra2 = this.mEntity.getExtra(SocializeConstants.FIELD_WX_SECRET);
        CharSequence extra3 = this.mEntity.getExtra(SocializeConstants.FIELD_QZONE_ID);
        String extra4 = this.mEntity.getExtra("qzone_secret");
        try {
            if (!TextUtils.isEmpty(extra)) {
                jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_WX_APPID, extra);
                jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_WX_SECRET, extra2);
            }
            if (!TextUtils.isEmpty(extra3)) {
                map.put(SocializeProtocolConstants.PROTOCOL_KEY_QZONE_ID, extra3);
                map.put("qzone_secret", extra4);
            }
            String appkey = SocializeUtils.getAppkey(this.mContext);
            jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_AK, appkey);
            jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_UMENG_SECRET, SocializeUtils
                    .reverse(appkey));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return packParamsMap(TAG, addParamsToJson(jSONObject, map).toString());
    }

    protected String getPath() {
        return a + SocializeUtils.getAppkey(this.mContext) + "/";
    }
}
