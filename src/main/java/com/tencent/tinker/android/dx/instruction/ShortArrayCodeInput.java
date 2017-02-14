package com.tencent.tinker.android.dx.instruction;

import java.io.EOFException;

public final class ShortArrayCodeInput extends CodeCursor {
    private final short[] array;

    public ShortArrayCodeInput(short[] array) {
        if (array == null) {
            throw new NullPointerException("array == null");
        }
        this.array = array;
    }

    public boolean hasMore() {
        return cursor() < this.array.length;
    }

    public int read() throws EOFException {
        try {
            int value = this.array[cursor()];
            advance(1);
            return 65535 & value;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new EOFException();
        }
    }

    public int readInt() throws EOFException {
        return (read() << 16) | read();
    }

    public long readLong() throws EOFException {
        return (((((long) read()) << 16) | ((long) read())) | (((long) read()) << 32)) | (((long)
                read()) << 48);
    }
}
