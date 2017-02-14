package org.eclipse.mat.parser.io;

import java.io.Closeable;
import java.io.EOFException;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;

public class BitInputStream implements Flushable, Closeable {
    public static final int DEFAULT_BUFFER_SIZE = 16384;
    private int avail;
    private byte[] buffer = new byte[16384];
    private int current;
    private int fill;
    private InputStream is;
    private int pos;

    public BitInputStream(InputStream is) {
        this.is = is;
    }

    public void flush() {
        this.avail = 0;
        this.pos = 0;
        this.fill = 0;
    }

    public void close() throws IOException {
        this.is.close();
        this.is = null;
        this.buffer = null;
    }

    private int read() throws IOException {
        if (this.avail == 0) {
            this.avail = this.is.read(this.buffer);
            if (this.avail == -1) {
                this.avail = 0;
                throw new EOFException();
            }
            this.pos = 0;
        }
        this.avail--;
        byte[] bArr = this.buffer;
        int i = this.pos;
        this.pos = i + 1;
        return bArr[i] & 255;
    }

    private int readFromCurrent(int len) throws IOException {
        if (len == 0) {
            return 0;
        }
        if (this.fill == 0) {
            this.current = read();
            this.fill = 8;
        }
        int i = this.current;
        int i2 = this.fill - len;
        this.fill = i2;
        return (i >>> i2) & ((1 << len) - 1);
    }

    public int readBit() throws IOException {
        return readFromCurrent(1);
    }

    public int readInt(int len) throws IOException {
        if (len <= this.fill) {
            return readFromCurrent(len);
        }
        len -= this.fill;
        int x = readFromCurrent(this.fill);
        int i = len >> 3;
        while (true) {
            int i2 = i - 1;
            if (i != 0) {
                x = (x << 8) | read();
                i = i2;
            } else {
                len &= 7;
                return (x << len) | readFromCurrent(len);
            }
        }
    }
}
