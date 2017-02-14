package com.umeng.socialize.net;

import android.content.Context;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.net.base.SocializeRequest;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import com.umeng.socialize.utils.SocializeUtils;

import java.util.Map;

/* compiled from: ShareFriendsRequest */
public class k extends SocializeRequest {
    private static final String a = "/share/friends/";
    private static final int    b = 14;
    private String      c;
    private SHARE_MEDIA d;

    public k(Context context, SocializeEntity socializeEntity, SHARE_MEDIA share_media, String
            str) {
        super(context, "", l.class, socializeEntity, 14, RequestMethod.GET);
        this.mContext = context;
        this.mEntity = socializeEntity;
        this.c = str;
        this.d = share_media;
    }

    protected Map<String, Object> addSelfParams(Map<String, Object> map) {
        map.put(SocializeProtocolConstants.PROTOCOL_KEY_SHARE_TO, this.d.toString());
        return map;
    }

    protected String getPath() {
        return a + SocializeUtils.getAppkey(this.mContext) + "/" + this.c + "/";
    }
}
