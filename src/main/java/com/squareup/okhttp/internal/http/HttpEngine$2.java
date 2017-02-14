package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.internal.Util;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Source;
import okio.Timeout;

class HttpEngine$2 implements Source {
    boolean cacheRequestClosed;
    final /* synthetic */ HttpEngine     this$0;
    final /* synthetic */ BufferedSink   val$cacheBody;
    final /* synthetic */ CacheRequest   val$cacheRequest;
    final /* synthetic */ BufferedSource val$source;

    HttpEngine$2(HttpEngine this$0, BufferedSource bufferedSource, CacheRequest cacheRequest,
                 BufferedSink bufferedSink) {
        this.this$0 = this$0;
        this.val$source = bufferedSource;
        this.val$cacheRequest = cacheRequest;
        this.val$cacheBody = bufferedSink;
    }

    public long read(Buffer sink, long byteCount) throws IOException {
        try {
            long bytesRead = this.val$source.read(sink, byteCount);
            if (bytesRead == -1) {
                if (!this.cacheRequestClosed) {
                    this.cacheRequestClosed = true;
                    this.val$cacheBody.close();
                }
                return -1;
            }
            sink.copyTo(this.val$cacheBody.buffer(), sink.size() - bytesRead, bytesRead);
            this.val$cacheBody.emitCompleteSegments();
            return bytesRead;
        } catch (IOException e) {
            if (!this.cacheRequestClosed) {
                this.cacheRequestClosed = true;
                this.val$cacheRequest.abort();
            }
            throw e;
        }
    }

    public Timeout timeout() {
        return this.val$source.timeout();
    }

    public void close() throws IOException {
        if (!(this.cacheRequestClosed || Util.discard(this, 100, TimeUnit.MILLISECONDS))) {
            this.cacheRequestClosed = true;
            this.val$cacheRequest.abort();
        }
        this.val$source.close();
    }
}
