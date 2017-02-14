package com.umeng.socialize.bean;

/* compiled from: SHARE_MEDIA */
enum af extends SHARE_MEDIA {
    af(String str,
    int i, String
    str2)

    {
        super(str, i, str2);
    }

    public boolean isSupportAuthorization() {
        return true;
    }

    public int getReqCode() {
        return HandlerRequestCode.QZONE_REQUEST_CODE;
    }
    }
