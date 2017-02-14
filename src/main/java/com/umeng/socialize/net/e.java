package com.umeng.socialize.net;

import android.content.Context;

import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.net.base.SocializeRequest;
import com.umeng.socialize.utils.SocializeUtils;

import java.util.Map;

/* compiled from: GetPlatformKeyRequest */
public class e extends SocializeRequest {
    private static final String a = "/share/keysecret/";
    private static final int    b = 20;

    public e(Context context, SocializeEntity socializeEntity) {
        super(context, "", GetPlatformKeyResponse.class, socializeEntity, 20, RequestMethod.GET);
        this.mContext = context;
    }

    protected Map<String, Object> addSelfParams(Map<String, Object> map) {
        return map;
    }

    protected String getPath() {
        return a + SocializeUtils.getAppkey(this.mContext) + "/";
    }
}
