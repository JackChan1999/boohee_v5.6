package com.mob.tools.network;

import java.io.InputStream;

import org.apache.http.entity.InputStreamEntity;

public abstract class HTTPPart {
    private OnReadListener listener;
    private long           offset;

    protected abstract InputStream getInputStream() throws Throwable;

    public InputStreamEntity getInputStreamEntity() throws Throwable {
        InputStream byteCounterInputStream = new ByteCounterInputStream(getInputStream());
        byteCounterInputStream.setOnInputStreamReadListener(this.listener);
        if (this.offset > 0) {
            byteCounterInputStream.skip(this.offset);
        }
        return new InputStreamEntity(byteCounterInputStream, length() - this.offset);
    }

    protected abstract long length() throws Throwable;

    public void setOffset(long j) {
        this.offset = j;
    }

    public void setOnReadListener(OnReadListener onReadListener) {
        this.listener = onReadListener;
    }
}
