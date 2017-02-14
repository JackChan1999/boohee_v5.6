package com.tencent.tinker.android.dx.instruction;

public final class ShortArrayCodeOutput extends CodeCursor {
    private short[] array;

    public ShortArrayCodeOutput(int initSize) {
        if (initSize < 0) {
            throw new IllegalArgumentException("initSize < 0");
        }
        this.array = new short[initSize];
    }

    public ShortArrayCodeOutput(short[] array) {
        if (array == null) {
            throw new IllegalArgumentException("array is null.");
        }
        this.array = array;
    }

    public short[] getArray() {
        int cursor = cursor();
        if (cursor == this.array.length) {
            return this.array;
        }
        short[] result = new short[cursor];
        System.arraycopy(this.array, 0, result, 0, cursor);
        return result;
    }

    public void write(short codeUnit) {
        ensureArrayLength(1);
        this.array[cursor()] = codeUnit;
        advance(1);
    }

    public void write(short u0, short u1) {
        write(u0);
        write(u1);
    }

    public void write(short u0, short u1, short u2) {
        write(u0);
        write(u1);
        write(u2);
    }

    public void write(short u0, short u1, short u2, short u3) {
        write(u0);
        write(u1);
        write(u2);
        write(u3);
    }

    public void write(short u0, short u1, short u2, short u3, short u4) {
        write(u0);
        write(u1);
        write(u2);
        write(u3);
        write(u4);
    }

    public void writeInt(int value) {
        write((short) value);
        write((short) (value >> 16));
    }

    public void writeLong(long value) {
        write((short) ((int) value));
        write((short) ((int) (value >> 16)));
        write((short) ((int) (value >> 32)));
        write((short) ((int) (value >> 48)));
    }

    public void write(byte[] data) {
        int value = 0;
        boolean even = true;
        for (byte b : data) {
            if (even) {
                value = b & 255;
                even = false;
            } else {
                value |= b << 8;
                write((short) value);
                even = true;
            }
        }
        if (!even) {
            write((short) value);
        }
    }

    public void write(short[] data) {
        for (short unit : data) {
            write(unit);
        }
    }

    public void write(int[] data) {
        for (int i : data) {
            writeInt(i);
        }
    }

    public void write(long[] data) {
        for (long l : data) {
            writeLong(l);
        }
    }

    private void ensureArrayLength(int shortCountToWrite) {
        int currPos = cursor();
        if (this.array.length - currPos < shortCountToWrite) {
            short[] newArray = new short[(this.array.length + (this.array.length >> 1))];
            System.arraycopy(this.array, 0, newArray, 0, currPos);
            this.array = newArray;
        }
    }
}
