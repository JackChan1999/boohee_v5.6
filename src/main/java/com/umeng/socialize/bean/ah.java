package com.umeng.socialize.bean;

/* compiled from: SHARE_MEDIA */
enum ah extends SHARE_MEDIA {
    ah(String str,
    int i, String
    str2)

    {
        super(str, i, str2);
    }

    public boolean isCustomPlatform() {
        return false;
    }

    public boolean isSupportAuthorization() {
        return true;
    }

    public int getReqCode() {
        return HandlerRequestCode.RENREN_REQUEST_CODE;
    }
    }
