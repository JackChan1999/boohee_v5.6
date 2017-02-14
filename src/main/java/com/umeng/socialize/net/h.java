package com.umeng.socialize.net;

import android.content.Context;

import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.common.SocializeConstants;
import com.umeng.socialize.net.base.SocializeRequest;
import com.umeng.socialize.utils.SocializeUtils;

import java.util.Map;

/* compiled from: ProfileRequest */
public class h extends SocializeRequest {
    private static final String a = "/user/profile/get/";
    private static final int    b = 3;

    public h(Context context, SocializeEntity socializeEntity) {
        super(context, "", i.class, socializeEntity, 3, RequestMethod.GET);
        this.mContext = context;
    }

    protected Map<String, Object> addSelfParams(Map<String, Object> map) {
        return map;
    }

    protected String getPath() {
        return a + SocializeUtils.getAppkey(this.mContext) + "/" + SocializeConstants.UID + "/";
    }
}
