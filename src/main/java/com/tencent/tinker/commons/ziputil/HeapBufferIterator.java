package com.tencent.tinker.commons.ziputil;

import java.nio.ByteOrder;

public final class HeapBufferIterator extends BufferIterator {
    private final byte[]    buffer;
    private final int       byteCount;
    private final int       offset;
    private final ByteOrder order;
    private       int       position;

    HeapBufferIterator(byte[] buffer, int offset, int byteCount, ByteOrder order) {
        this.buffer = buffer;
        this.offset = offset;
        this.byteCount = byteCount;
        this.order = order;
    }

    public static BufferIterator iterator(byte[] buffer, int offset, int byteCount, ByteOrder
            order) {
        return new HeapBufferIterator(buffer, offset, byteCount, order);
    }

    public void seek(int offset) {
        this.position = offset;
    }

    public void skip(int byteCount) {
        this.position += byteCount;
    }

    public void readByteArray(byte[] dst, int dstOffset, int byteCount) {
        System.arraycopy(this.buffer, this.offset + this.position, dst, dstOffset, byteCount);
        this.position += byteCount;
    }

    public byte readByte() {
        byte result = this.buffer[this.offset + this.position];
        this.position++;
        return result;
    }

    public int readInt() {
        int result = Memory.peekInt(this.buffer, this.offset + this.position, this.order);
        this.position += 4;
        return result;
    }

    public short readShort() {
        short result = Memory.peekShort(this.buffer, this.offset + this.position, this.order);
        this.position += 2;
        return result;
    }
}
