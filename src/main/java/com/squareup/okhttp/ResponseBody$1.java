package com.squareup.okhttp;

import okio.BufferedSource;

class ResponseBody$1 extends ResponseBody {
    final /* synthetic */ BufferedSource val$content;
    final /* synthetic */ long           val$contentLength;
    final /* synthetic */ MediaType      val$contentType;

    ResponseBody$1(MediaType mediaType, long j, BufferedSource bufferedSource) {
        this.val$contentType = mediaType;
        this.val$contentLength = j;
        this.val$content = bufferedSource;
    }

    public MediaType contentType() {
        return this.val$contentType;
    }

    public long contentLength() {
        return this.val$contentLength;
    }

    public BufferedSource source() {
        return this.val$content;
    }
}
