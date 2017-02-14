package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.ResponseBody;

import okio.Buffer;
import okio.BufferedSource;

class HttpEngine$1 extends ResponseBody {
    HttpEngine$1() {
    }

    public MediaType contentType() {
        return null;
    }

    public long contentLength() {
        return 0;
    }

    public BufferedSource source() {
        return new Buffer();
    }
}
