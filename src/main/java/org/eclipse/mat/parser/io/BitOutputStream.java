package org.eclipse.mat.parser.io;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import org.eclipse.mat.hprof.Messages;

public class BitOutputStream implements Flushable, Closeable {
    public static final int DEFAULT_BUFFER_SIZE = 16384;
    static final int TEMP_BUFFER_SIZE = 128;
    private int avail;
    private byte[] buffer;
    private int current;
    private int free;
    private OutputStream os;
    private int pos;
    private byte[] tempBuffer = new byte[128];

    public BitOutputStream(OutputStream os) {
        this.os = os;
        this.buffer = new byte[16384];
        this.avail = 16384;
        this.free = 8;
    }

    public void flush() throws IOException {
        align();
        this.os.write(this.buffer, 0, this.pos);
        this.pos = 0;
        this.avail = this.buffer.length;
        this.os.flush();
    }

    public void close() throws IOException {
        flush();
        this.os.close();
        this.os = null;
        this.buffer = null;
        this.tempBuffer = null;
    }

    private void write(int b) throws IOException {
        int i = this.avail;
        this.avail = i - 1;
        if (i == 0) {
            if (this.os == null) {
                this.avail = 0;
                throw new IOException(Messages.BitOutputStream_Error_ArrayFull.pattern);
            } else if (this.buffer == null) {
                this.os.write(b);
                this.avail = 0;
                return;
            } else {
                this.os.write(this.buffer);
                this.avail = this.buffer.length - 1;
                this.pos = 0;
            }
        }
        byte[] bArr = this.buffer;
        int i2 = this.pos;
        this.pos = i2 + 1;
        bArr[i2] = (byte) b;
    }

    private int writeInCurrent(int b, int len) throws IOException {
        int i = this.current;
        int i2 = ((1 << len) - 1) & b;
        int i3 = this.free - len;
        this.free = i3;
        this.current = i | (i2 << i3);
        if (this.free == 0) {
            write(this.current);
            this.free = 8;
            this.current = 0;
        }
        return len;
    }

    private int align() throws IOException {
        if (this.free != 8) {
            return writeInCurrent(0, this.free);
        }
        return 0;
    }

    public int writeBit(int bit) throws IOException {
        return writeInCurrent(bit, 1);
    }

    public int writeInt(int x, int len) throws IOException {
        if (len <= this.free) {
            return writeInCurrent(x, len);
        }
        int i;
        int queue = (len - this.free) & 7;
        int blocks = (len - this.free) >> 3;
        int i2 = blocks;
        if (queue != 0) {
            this.tempBuffer[blocks] = (byte) x;
            x >>= queue;
            i = i2;
        } else {
            i = i2;
        }
        while (true) {
            i2 = i - 1;
            if (i == 0) {
                break;
            }
            this.tempBuffer[i2] = (byte) x;
            x >>>= 8;
            i = i2;
        }
        writeInCurrent(x, this.free);
        for (i2 = 0; i2 < blocks; i2++) {
            write(this.tempBuffer[i2]);
        }
        if (queue == 0) {
            return len;
        }
        writeInCurrent(this.tempBuffer[blocks], queue);
        return len;
    }
}
