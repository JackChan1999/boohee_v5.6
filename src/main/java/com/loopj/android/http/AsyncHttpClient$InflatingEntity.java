package com.loopj.android.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.zip.GZIPInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;

class AsyncHttpClient$InflatingEntity extends HttpEntityWrapper {
    GZIPInputStream     gzippedStream;
    PushbackInputStream pushbackStream;
    InputStream         wrappedStream;

    public AsyncHttpClient$InflatingEntity(HttpEntity wrapped) {
        super(wrapped);
    }

    public InputStream getContent() throws IOException {
        this.wrappedStream = this.wrappedEntity.getContent();
        this.pushbackStream = new PushbackInputStream(this.wrappedStream, 2);
        if (!AsyncHttpClient.isInputStreamGZIPCompressed(this.pushbackStream)) {
            return this.pushbackStream;
        }
        this.gzippedStream = new GZIPInputStream(this.pushbackStream);
        return this.gzippedStream;
    }

    public long getContentLength() {
        return this.wrappedEntity == null ? 0 : this.wrappedEntity.getContentLength();
    }

    public void consumeContent() throws IOException {
        AsyncHttpClient.silentCloseInputStream(this.wrappedStream);
        AsyncHttpClient.silentCloseInputStream(this.pushbackStream);
        AsyncHttpClient.silentCloseInputStream(this.gzippedStream);
        super.consumeContent();
    }
}
