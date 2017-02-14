package com.umeng.socialize.net;

import android.content.Context;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.common.SocializeConstants;
import com.umeng.socialize.net.base.SocializeRequest;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import com.umeng.socialize.utils.SocializeUtils;

import java.util.Map;

/* compiled from: ExpiresInRequest */
public class c extends SocializeRequest {
    private static final String a = "/share/validate_token/";
    private static final int    b = 24;
    private SHARE_MEDIA[] c;

    public c(Context context, SocializeEntity socializeEntity, SHARE_MEDIA[] share_mediaArr) {
        super(context, "", d.class, socializeEntity, 24, RequestMethod.GET);
        this.c = share_mediaArr;
    }

    protected Map<String, Object> addSelfParams(Map<String, Object> map) {
        StringBuilder stringBuilder = new StringBuilder();
        if (this.c != null && this.c.length > 0) {
            for (SHARE_MEDIA share_media : this.c) {
                if (share_media != SHARE_MEDIA.GENERIC) {
                    stringBuilder.append(share_media.toString()).append(",");
                }
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        map.put(SocializeProtocolConstants.PROTOCOL_KEY_PLATFORM, stringBuilder.toString());
        map.put(SocializeProtocolConstants.PROTOCOL_KEY_UID, SocializeConstants.UID);
        return map;
    }

    protected String getPath() {
        return a + SocializeUtils.getAppkey(this.mContext) + "/";
    }
}
