package org.eclipse.mat.parser.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

public class SimpleBufferedRandomAccessInputStream extends InputStream {
    private int buf_end;
    private int buf_pos;
    private byte[] buffer;
    private RandomAccessFile raf;
    private byte[] readBuffer;
    private long real_pos;

    public SimpleBufferedRandomAccessInputStream(RandomAccessFile in) throws IOException {
        this(in, 8192);
    }

    public SimpleBufferedRandomAccessInputStream(RandomAccessFile in, int bufsize) throws IOException {
        this.readBuffer = new byte[32];
        this.raf = in;
        invalidate();
        this.buffer = new byte[bufsize];
    }

    private void invalidate() throws IOException {
        this.buf_end = 0;
        this.buf_pos = 0;
        this.real_pos = this.raf.getFilePointer();
    }

    public final int read() throws IOException {
        if ((this.buf_pos >= this.buf_end && fillBuffer() < 0) || this.buf_end == 0) {
            return -1;
        }
        byte[] bArr = this.buffer;
        int i = this.buf_pos;
        this.buf_pos = i + 1;
        return bArr[i] & 255;
    }

    public int read(byte[] b, int off, int len) throws IOException {
        if (len == 0) {
            return 0;
        }
        if ((this.buf_pos >= this.buf_end && fillBuffer() < 0) || this.buf_end == 0) {
            return -1;
        }
        int copied = 0;
        while (copied < len) {
            if (this.buf_pos >= this.buf_end && fillBuffer() < 0) {
                return copied;
            }
            int length = Math.min(len - copied, this.buf_end - this.buf_pos);
            System.arraycopy(this.buffer, this.buf_pos, b, off + copied, length);
            this.buf_pos += length;
            copied += length;
        }
        return copied;
    }

    private int fillBuffer() throws IOException {
        int n = this.raf.read(this.buffer);
        if (n >= 0) {
            this.real_pos += (long) n;
            this.buf_end = n;
            this.buf_pos = 0;
        }
        return n;
    }

    public boolean markSupported() {
        return false;
    }

    public void close() throws IOException {
        this.raf.close();
        this.buffer = null;
    }

    public void seek(long pos) throws IOException {
        int n = (int) (this.real_pos - pos);
        if (n < 0 || n > this.buf_end) {
            this.raf.seek(pos);
            invalidate();
            return;
        }
        this.buf_pos = this.buf_end - n;
    }

    public long getFilePointer() {
        return (this.real_pos - ((long) this.buf_end)) + ((long) this.buf_pos);
    }

    public final int readInt() throws IOException {
        if (this.buf_pos + 4 < this.buf_end) {
            int a = readInt(this.buffer, this.buf_pos);
            this.buf_pos += 4;
            return a;
        } else if (read(this.readBuffer, 0, 4) == 4) {
            return readInt(this.readBuffer, 0);
        } else {
            throw new IOException();
        }
    }

    public final long readLong() throws IOException {
        if (this.buf_pos + 8 < this.buf_end) {
            long a = readLong(this.buffer, this.buf_pos);
            this.buf_pos += 8;
            return a;
        } else if (read(this.readBuffer, 0, 8) == 8) {
            return readLong(this.readBuffer, 0);
        } else {
            throw new IOException();
        }
    }

    public int readIntArray(int[] a) throws IOException {
        byte[] b;
        int offset = 0;
        int len = a.length * 4;
        if (this.buf_pos + len < this.buf_end) {
            b = this.buffer;
            offset = this.buf_pos;
            this.buf_pos += len;
        } else {
            b = len > this.readBuffer.length ? new byte[len] : this.readBuffer;
            if (read(b, 0, len) != len) {
                throw new IOException();
            }
        }
        for (int ii = 0; ii < a.length; ii++) {
            a[ii] = readInt(b, (ii * 4) + offset);
        }
        return a.length;
    }

    private static final int readInt(byte[] b, int offset) throws IOException {
        int ch1 = b[offset] & 255;
        int ch2 = b[offset + 1] & 255;
        int ch3 = b[offset + 2] & 255;
        int ch4 = b[offset + 3] & 255;
        if ((((ch1 | ch2) | ch3) | ch4) >= 0) {
            return (((ch1 << 24) + (ch2 << 16)) + (ch3 << 8)) + (ch4 << 0);
        }
        throw new EOFException();
    }

    public int readLongArray(long[] a) throws IOException {
        byte[] b;
        int offset = 0;
        int len = a.length * 8;
        if (this.buf_pos + len < this.buf_end) {
            b = this.buffer;
            offset = this.buf_pos;
            this.buf_pos += len;
        } else {
            b = len > this.readBuffer.length ? new byte[len] : this.readBuffer;
            if (read(b, 0, len) != len) {
                throw new IOException();
            }
        }
        for (int ii = 0; ii < a.length; ii++) {
            a[ii] = readLong(b, (ii * 8) + offset);
        }
        return a.length;
    }

    private static final long readLong(byte[] b, int offset) {
        return (((((((((long) b[offset]) << 56) + (((long) (b[offset + 1] & 255)) << 48)) + (((long) (b[offset + 2] & 255)) << 40)) + (((long) (b[offset + 3] & 255)) << 32)) + (((long) (b[offset + 4] & 255)) << 24)) + ((long) ((b[offset + 5] & 255) << 16))) + ((long) ((b[offset + 6] & 255) << 8))) + ((long) ((b[offset + 7] & 255) << 0));
    }
}
