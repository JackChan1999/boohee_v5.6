package com.umeng.analytics.social;

import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

/* compiled from: UMException */
public class a extends RuntimeException {
    private static final long   b = -4656673116019167471L;
    protected            int    a = BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT;
    private              String c = "";

    public int a() {
        return this.a;
    }

    public a(int i, String str) {
        super(str);
        this.a = i;
        this.c = str;
    }

    public a(String str, Throwable th) {
        super(str, th);
        this.c = str;
    }

    public a(String str) {
        super(str);
        this.c = str;
    }

    public String getMessage() {
        return this.c;
    }
}
