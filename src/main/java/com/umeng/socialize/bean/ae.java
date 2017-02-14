package com.umeng.socialize.bean;

/* compiled from: SHARE_MEDIA */
enum ae extends SHARE_MEDIA {
    ae(String str,
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
        return HandlerRequestCode.SINA_REQUEST_CODE;
    }
    }
