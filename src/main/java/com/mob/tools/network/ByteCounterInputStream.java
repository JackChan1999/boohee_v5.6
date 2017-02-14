package com.mob.tools.network;

import java.io.IOException;
import java.io.InputStream;

public class ByteCounterInputStream extends InputStream {
    private InputStream    is;
    private OnReadListener listener;
    private long           readBytes;

    public ByteCounterInputStream(InputStream inputStream) {
        this.is = inputStream;
    }

    public int available() throws IOException {
        return this.is.available();
    }

    public void close() throws IOException {
        this.is.close();
    }

    public void mark(int i) {
        this.is.mark(i);
    }

    public boolean markSupported() {
        return this.is.markSupported();
    }

    public int read() throws IOException {
        int read = this.is.read();
        if (read >= 0) {
            this.readBytes++;
            if (this.listener != null) {
                this.listener.onRead(this.readBytes);
            }
        }
        return read;
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        int read = this.is.read(bArr, i, i2);
        if (read > 0) {
            this.readBytes += (long) read;
            if (this.listener != null) {
                this.listener.onRead(this.readBytes);
            }
        }
        return read;
    }

    public synchronized void reset() throws IOException {
        this.is.reset();
        this.readBytes = 0;
    }

    public void setOnInputStreamReadListener(OnReadListener onReadListener) {
        this.listener = onReadListener;
    }

    public long skip(long j) throws IOException {
        return this.is.skip(j);
    }
}
