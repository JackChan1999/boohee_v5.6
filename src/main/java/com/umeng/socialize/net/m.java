package com.umeng.socialize.net;

import android.content.Context;

import com.umeng.socialize.bean.SNSPair;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.net.base.SocializeRequest;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import com.umeng.socialize.utils.SocializeUtils;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: ShareMultiFollowRequest */
public class m extends SocializeRequest {
    private static final String a = "/share/follow/";
    private static final int    b = 18;
    private SNSPair  c;
    private String[] d;

    public m(Context context, SocializeEntity socializeEntity, SNSPair sNSPair, String... strArr) {
        super(context, "", n.class, socializeEntity, 18, RequestMethod.POST);
        this.mContext = context;
        this.c = sNSPair;
        this.d = strArr;
    }

    protected Map<String, Object> addSelfParams(Map<String, Object> map) {
        StringBuilder stringBuilder = new StringBuilder();
        if (this.d != null) {
            for (String str : this.d) {
                stringBuilder.append(str + ",");
            }
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_SHARE_TO, this.c.mPaltform
                    .toString());
            jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_SHARE_FOLLOWS, stringBuilder
                    .toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return packParamsMap(TAG, addParamsToJson(jSONObject, map).toString());
    }

    protected String getPath() {
        return a + SocializeUtils.getAppkey(this.mContext) + "/" + this.c.mUsid + "/";
    }
}
