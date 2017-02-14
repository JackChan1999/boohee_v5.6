package org.eclipse.mat.parser.io;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.eclipse.mat.hprof.Messages;

public class PositionInputStream extends FilterInputStream implements DataInput {
    private long position = 0;
    private final byte[] readBuffer = new byte[32];

    public PositionInputStream(InputStream in) {
        super(in);
    }

    public int read() throws IOException {
        int res = super.read();
        if (res != -1) {
            this.position++;
        }
        return res;
    }

    public int read(byte[] b, int off, int len) throws IOException {
        int res = super.read(b, off, len);
        if (res != -1) {
            this.position += (long) res;
        }
        return res;
    }

    public long skip(long n) throws IOException {
        long res = super.skip(n);
        this.position += res;
        return res;
    }

    public boolean markSupported() {
        return false;
    }

    public void mark(int readLimit) {
        throw new UnsupportedOperationException(Messages.PositionInputStream_mark.pattern);
    }

    public void reset() {
        throw new UnsupportedOperationException(Messages.PositionInputStream_reset.pattern);
    }

    public final int skipBytes(int n) throws IOException {
        int total = 0;
        while (total < n) {
            int cur = (int) skip((long) (n - total));
            if (cur <= 0) {
                break;
            }
            total += cur;
        }
        return total;
    }

    public final int skipBytes(long n) throws IOException {
        int total = 0;
        while (((long) total) < n) {
            int cur = (int) skip(n - ((long) total));
            if (cur <= 0) {
                break;
            }
            total += cur;
        }
        return total;
    }

    public final void readFully(byte[] b) throws IOException {
        readFully(b, 0, b.length);
    }

    public final void readFully(byte[] b, int off, int len) throws IOException {
        int n = 0;
        while (n < len) {
            int count = read(b, off + n, len - n);
            if (count < 0) {
                throw new EOFException();
            }
            n += count;
        }
    }

    public long position() {
        return this.position;
    }

    public void seek(long pos) throws IOException {
        if (this.in instanceof BufferedRandomAccessInputStream) {
            this.position = pos;
            ((BufferedRandomAccessInputStream) this.in).seek(pos);
        } else if (this.in instanceof SimpleBufferedRandomAccessInputStream) {
            this.position = pos;
            ((SimpleBufferedRandomAccessInputStream) this.in).seek(pos);
        } else {
            throw new UnsupportedOperationException(Messages.PositionInputStream_seek.pattern);
        }
    }

    public final int readUnsignedByte() throws IOException {
        int ch = this.in.read();
        if (ch < 0) {
            throw new EOFException();
        }
        this.position++;
        return ch;
    }

    public final int readInt() throws IOException {
        readFully(this.readBuffer, 0, 4);
        return readInt(this.readBuffer, 0);
    }

    public final long readLong() throws IOException {
        readFully(this.readBuffer, 0, 8);
        return readLong(this.readBuffer, 0);
    }

    public boolean readBoolean() throws IOException {
        int ch = this.in.read();
        if (ch < 0) {
            throw new EOFException();
        }
        this.position++;
        return ch != 0;
    }

    public byte readByte() throws IOException {
        int ch = this.in.read();
        if (ch < 0) {
            throw new EOFException();
        }
        this.position++;
        return (byte) ch;
    }

    public char readChar() throws IOException {
        int ch1 = this.in.read();
        int ch2 = this.in.read();
        if ((ch1 | ch2) < 0) {
            throw new EOFException();
        }
        this.position += 2;
        return (char) ((ch1 << 8) + (ch2 << 0));
    }

    public double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }

    public float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    public String readLine() throws IOException {
        throw new UnsupportedOperationException();
    }

    public short readShort() throws IOException {
        int ch1 = this.in.read();
        int ch2 = this.in.read();
        if ((ch1 | ch2) < 0) {
            throw new EOFException();
        }
        this.position += 2;
        return (short) ((ch1 << 8) + (ch2 << 0));
    }

    public String readUTF() throws IOException {
        return DataInputStream.readUTF(this);
    }

    public int readUnsignedShort() throws IOException {
        int ch1 = this.in.read();
        int ch2 = this.in.read();
        if ((ch1 | ch2) < 0) {
            throw new EOFException();
        }
        this.position += 2;
        return (ch1 << 8) + (ch2 << 0);
    }

    public int readIntArray(int[] a) throws IOException {
        int len = a.length * 4;
        byte[] b = len > this.readBuffer.length ? new byte[len] : this.readBuffer;
        if (read(b, 0, len) != len) {
            throw new IOException();
        }
        for (int ii = 0; ii < a.length; ii++) {
            a[ii] = readInt(b, ii * 4);
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
        int len = a.length * 8;
        byte[] b = len > this.readBuffer.length ? new byte[len] : this.readBuffer;
        if (read(b, 0, len) != len) {
            throw new IOException();
        }
        for (int ii = 0; ii < a.length; ii++) {
            a[ii] = readLong(b, ii * 8);
        }
        return a.length;
    }

    private static final long readLong(byte[] b, int offset) {
        return (((((((((long) b[offset]) << 56) + (((long) (b[offset + 1] & 255)) << 48)) + (((long) (b[offset + 2] & 255)) << 40)) + (((long) (b[offset + 3] & 255)) << 32)) + (((long) (b[offset + 4] & 255)) << 24)) + ((long) ((b[offset + 5] & 255) << 16))) + ((long) ((b[offset + 6] & 255) << 8))) + ((long) ((b[offset + 7] & 255) << 0));
    }
}
