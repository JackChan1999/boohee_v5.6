package com.umeng.socialize.bean;

/* compiled from: SHARE_MEDIA */
enum m extends SHARE_MEDIA {
    m(String str,
    int i, String
    str2)

    {
        super(str, i, str2);
    }

    public int getReqCode() {
        return HandlerRequestCode.FACEBOOK_REQUEST_AUTH_CODE;
    }

    public boolean isSupportAuthorization() {
        return true;
    }
    }
