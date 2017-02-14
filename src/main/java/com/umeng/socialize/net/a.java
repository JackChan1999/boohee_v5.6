package com.umeng.socialize.net;

import android.content.Context;
import android.text.TextUtils;

import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.net.base.SocializeRequest;
import com.umeng.socialize.net.utils.AesHelper;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import com.umeng.socialize.utils.SocializeUtils;

import java.util.Map;

/* compiled from: ActionBarRequest */
public class a extends SocializeRequest {
    private static final String a = "/bar/get/";
    private static final int    b = 1;
    private              int    c = 0;

    public a(Context context, SocializeEntity socializeEntity, int i) {
        super(context, "", b.class, socializeEntity, 1, RequestMethod.GET);
        this.mContext = context;
        this.mEntity = socializeEntity;
        this.c = i;
        AesHelper.setPassword(SocializeUtils.getAppkey(this.mContext));
    }

    protected Map<String, Object> addSelfParams(Map<String, Object> map) {
        map.put(SocializeProtocolConstants.PROTOCOL_KEY_DESCRIPTOR, this.mEntity.mDescriptor);
        map.put(SocializeProtocolConstants.PROTOCOL_KEY_NEW_INSTALL, Integer.valueOf(this.c));
        if (!TextUtils.isEmpty(this.mEntity.getNickName())) {
            map.put(SocializeProtocolConstants.PROTOCOL_KEY_ENTITY_NAME, this.mEntity.getNickName
                    ());
        }
        if (!TextUtils.isEmpty(this.mEntity.mCustomID)) {
            map.put(SocializeProtocolConstants.PROTOCOL_KEY_CUSTOM_ID, this.mEntity.mCustomID);
        }
        return map;
    }

    protected String getPath() {
        return a + SocializeUtils.getAppkey(this.mContext) + "/";
    }
}
