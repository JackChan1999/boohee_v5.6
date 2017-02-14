package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.ResponseBody;

import okio.BufferedSource;

public final class RealResponseBody extends ResponseBody {
    private final Headers        headers;
    private final BufferedSource source;

    public RealResponseBody(Headers headers, BufferedSource source) {
        this.headers = headers;
        this.source = source;
    }

    public MediaType contentType() {
        String contentType = this.headers.get("Content-Type");
        return contentType != null ? MediaType.parse(contentType) : null;
    }

    public long contentLength() {
        return OkHeaders.contentLength(this.headers);
    }

    public BufferedSource source() {
        return this.source;
    }
}
