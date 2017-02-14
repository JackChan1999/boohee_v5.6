package com.umeng.socialize.bean;

/* compiled from: SHARE_MEDIA */
enum aj extends SHARE_MEDIA {
    aj(String str,
    int i, String
    str2)

    {
        super(str, i, str2);
    }

    public int getReqCode() {
        return HandlerRequestCode.WX_CIRCLE_REQUEST_CODE;
    }

    public boolean isSupportAuthorization() {
        return true;
    }
    }
