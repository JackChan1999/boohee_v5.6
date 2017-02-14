package com.umeng.socialize.bean;

/* compiled from: SHARE_MEDIA */
enum ak extends SHARE_MEDIA {
    ak(String str,
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
        return HandlerRequestCode.TENCENT_WB_REQUEST_CODE;
    }
    }
