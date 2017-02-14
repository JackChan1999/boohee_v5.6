package com.umeng.socialize.net;

import android.content.Context;

import com.umeng.socialize.bean.SNSPair;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.net.base.SocializeRequest;
import com.umeng.socialize.utils.SocializeUtils;

import java.util.Map;

/* compiled from: UserInfoRequest */
public class x extends SocializeRequest {
    private static final String a = "/share/userinfo/";
    private static final int    b = 12;
    private SNSPair c;

    public x(Context context, SocializeEntity socializeEntity, SNSPair sNSPair) {
        super(context, "", y.class, socializeEntity, 12, RequestMethod.GET);
        this.mContext = context;
        this.c = sNSPair;
    }

    protected Map<String, Object> addSelfParams(Map<String, Object> map) {
        map.put("sns", this.c.mPaltform.toString());
        return map;
    }

    protected String getPath() {
        return a + SocializeUtils.getAppkey(this.mContext) + "/" + this.c.mUsid + "/";
    }
}
