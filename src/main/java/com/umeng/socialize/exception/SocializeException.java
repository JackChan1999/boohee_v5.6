package com.umeng.socialize.exception;

import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

public class SocializeException extends RuntimeException {
    private static final long   b = -4656673116019167471L;
    protected            int    a = BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT;
    private              String c = "";

    public int getErrorCode() {
        return this.a;
    }

    public SocializeException(int i, String str) {
        super(str);
        this.a = i;
        this.c = str;
    }

    public SocializeException(String str, Throwable th) {
        super(str, th);
        this.c = str;
    }

    public SocializeException(String str) {
        super(str);
        this.c = str;
    }

    public String getMessage() {
        return this.c;
    }
}
