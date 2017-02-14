package com.squareup.okhttp.internal;

import java.io.IOException;

import okio.Buffer;
import okio.Sink;
import okio.Timeout;

class DiskLruCache$4 implements Sink {
    DiskLruCache$4() {
    }

    public void write(Buffer source, long byteCount) throws IOException {
        source.skip(byteCount);
    }

    public void flush() throws IOException {
    }

    public Timeout timeout() {
        return Timeout.NONE;
    }

    public void close() throws IOException {
    }
}
