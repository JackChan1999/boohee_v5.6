package com.umeng.socialize.bean;

/* compiled from: SHARE_MEDIA */
enum ag extends SHARE_MEDIA {
    ag(String str,
    int i, String
    str2)

    {
        super(str, i, str2);
    }

    public boolean isSupportAuthorization() {
        return true;
    }

    public int getReqCode() {
        return HandlerRequestCode.QQ_REQUEST_CODE;
    }
    }
