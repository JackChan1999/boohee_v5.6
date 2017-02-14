package com.google.zxing.common;

import java.util.Arrays;

public final class BitMatrix implements Cloneable {
    private final int[] bits;
    private final int   height;
    private final int   rowSize;
    private final int   width;

    public BitMatrix(int dimension) {
        this(dimension, dimension);
    }

    public BitMatrix(int width, int height) {
        if (width < 1 || height < 1) {
            throw new IllegalArgumentException("Both dimensions must be greater than 0");
        }
        this.width = width;
        this.height = height;
        this.rowSize = (width + 31) / 32;
        this.bits = new int[(this.rowSize * height)];
    }

    private BitMatrix(int width, int height, int rowSize, int[] bits) {
        this.width = width;
        this.height = height;
        this.rowSize = rowSize;
        this.bits = bits;
    }

    public static BitMatrix parse(String stringRepresentation, String setString, String
            unsetString) {
        if (stringRepresentation == null) {
            throw new IllegalArgumentException();
        }
        boolean[] bits = new boolean[stringRepresentation.length()];
        int bitsPos = 0;
        int rowStartPos = 0;
        int rowLength = -1;
        int nRows = 0;
        int pos = 0;
        while (pos < stringRepresentation.length()) {
            if (stringRepresentation.charAt(pos) == '\n' || stringRepresentation.charAt(pos) ==
                    '\r') {
                if (bitsPos > rowStartPos) {
                    if (rowLength == -1) {
                        rowLength = bitsPos - rowStartPos;
                    } else if (bitsPos - rowStartPos != rowLength) {
                        throw new IllegalArgumentException("row lengths do not match");
                    }
                    rowStartPos = bitsPos;
                    nRows++;
                }
                pos++;
            } else if (stringRepresentation.substring(pos, setString.length() + pos).equals
                    (setString)) {
                pos += setString.length();
                bits[bitsPos] = true;
                bitsPos++;
            } else if (stringRepresentation.substring(pos, unsetString.length() + pos).equals
                    (unsetString)) {
                pos += unsetString.length();
                bits[bitsPos] = false;
                bitsPos++;
            } else {
                throw new IllegalArgumentException("illegal character encountered: " +
                        stringRepresentation.substring(pos));
            }
        }
        if (bitsPos > rowStartPos) {
            if (rowLength == -1) {
                rowLength = bitsPos - rowStartPos;
            } else if (bitsPos - rowStartPos != rowLength) {
                throw new IllegalArgumentException("row lengths do not match");
            }
            nRows++;
        }
        BitMatrix matrix = new BitMatrix(rowLength, nRows);
        for (int i = 0; i < bitsPos; i++) {
            if (bits[i]) {
                matrix.set(i % rowLength, i / rowLength);
            }
        }
        return matrix;
    }

    public boolean get(int x, int y) {
        return ((this.bits[(this.rowSize * y) + (x / 32)] >>> (x & 31)) & 1) != 0;
    }

    public void set(int x, int y) {
        int offset = (this.rowSize * y) + (x / 32);
        int[] iArr = this.bits;
        iArr[offset] = iArr[offset] | (1 << (x & 31));
    }

    public void unset(int x, int y) {
        int offset = (this.rowSize * y) + (x / 32);
        int[] iArr = this.bits;
        iArr[offset] = iArr[offset] & ((1 << (x & 31)) ^ -1);
    }

    public void flip(int x, int y) {
        int offset = (this.rowSize * y) + (x / 32);
        int[] iArr = this.bits;
        iArr[offset] = iArr[offset] ^ (1 << (x & 31));
    }

    public void xor(BitMatrix mask) {
        if (this.width == mask.getWidth() && this.height == mask.getHeight() && this.rowSize ==
                mask.getRowSize()) {
            BitArray rowArray = new BitArray((this.width / 32) + 1);
            for (int y = 0; y < this.height; y++) {
                int offset = y * this.rowSize;
                int[] row = mask.getRow(y, rowArray).getBitArray();
                for (int x = 0; x < this.rowSize; x++) {
                    int[] iArr = this.bits;
                    int i = offset + x;
                    iArr[i] = iArr[i] ^ row[x];
                }
            }
            return;
        }
        throw new IllegalArgumentException("input matrix dimensions do not match");
    }

    public void clear() {
        int max = this.bits.length;
        for (int i = 0; i < max; i++) {
            this.bits[i] = 0;
        }
    }

    public void setRegion(int left, int top, int width, int height) {
        if (top < 0 || left < 0) {
            throw new IllegalArgumentException("Left and top must be nonnegative");
        } else if (height < 1 || width < 1) {
            throw new IllegalArgumentException("Height and width must be at least 1");
        } else {
            int right = left + width;
            int bottom = top + height;
            if (bottom > this.height || right > this.width) {
                throw new IllegalArgumentException("The region must fit inside the matrix");
            }
            for (int y = top; y < bottom; y++) {
                int offset = y * this.rowSize;
                for (int x = left; x < right; x++) {
                    int[] iArr = this.bits;
                    int i = (x / 32) + offset;
                    iArr[i] = iArr[i] | (1 << (x & 31));
                }
            }
        }
    }

    public BitArray getRow(int y, BitArray row) {
        if (row == null || row.getSize() < this.width) {
            row = new BitArray(this.width);
        } else {
            row.clear();
        }
        int offset = y * this.rowSize;
        for (int x = 0; x < this.rowSize; x++) {
            row.setBulk(x * 32, this.bits[offset + x]);
        }
        return row;
    }

    public void setRow(int y, BitArray row) {
        System.arraycopy(row.getBitArray(), 0, this.bits, this.rowSize * y, this.rowSize);
    }

    public void rotate180() {
        int width = getWidth();
        int height = getHeight();
        BitArray topRow = new BitArray(width);
        BitArray bottomRow = new BitArray(width);
        for (int i = 0; i < (height + 1) / 2; i++) {
            topRow = getRow(i, topRow);
            bottomRow = getRow((height - 1) - i, bottomRow);
            topRow.reverse();
            bottomRow.reverse();
            setRow(i, bottomRow);
            setRow((height - 1) - i, topRow);
        }
    }

    public int[] getEnclosingRectangle() {
        int left = this.width;
        int top = this.height;
        int right = -1;
        int bottom = -1;
        for (int y = 0; y < this.height; y++) {
            for (int x32 = 0; x32 < this.rowSize; x32++) {
                int theBits = this.bits[(this.rowSize * y) + x32];
                if (theBits != 0) {
                    int bit;
                    if (y < top) {
                        top = y;
                    }
                    if (y > bottom) {
                        bottom = y;
                    }
                    if (x32 * 32 < left) {
                        bit = 0;
                        while ((theBits << (31 - bit)) == 0) {
                            bit++;
                        }
                        if ((x32 * 32) + bit < left) {
                            left = (x32 * 32) + bit;
                        }
                    }
                    if ((x32 * 32) + 31 > right) {
                        bit = 31;
                        while ((theBits >>> bit) == 0) {
                            bit--;
                        }
                        if ((x32 * 32) + bit > right) {
                            right = (x32 * 32) + bit;
                        }
                    }
                }
            }
        }
        int height = bottom - top;
        if (right - left < 0 || height < 0) {
            return null;
        }
        return new int[]{left, top, width, height};
    }

    public int[] getTopLeftOnBit() {
        int bitsOffset = 0;
        while (bitsOffset < this.bits.length && this.bits[bitsOffset] == 0) {
            bitsOffset++;
        }
        if (bitsOffset == this.bits.length) {
            return null;
        }
        int bit;
        int y = bitsOffset / this.rowSize;
        int x = (bitsOffset % this.rowSize) * 32;
        for (bit = 0; (this.bits[bitsOffset] << (31 - bit)) == 0; bit++) {
        }
        return new int[]{x + bit, y};
    }

    public int[] getBottomRightOnBit() {
        int bitsOffset = this.bits.length - 1;
        while (bitsOffset >= 0 && this.bits[bitsOffset] == 0) {
            bitsOffset--;
        }
        if (bitsOffset < 0) {
            return null;
        }
        int bit;
        int y = bitsOffset / this.rowSize;
        int x = (bitsOffset % this.rowSize) * 32;
        for (bit = 31; (this.bits[bitsOffset] >>> bit) == 0; bit--) {
        }
        return new int[]{x + bit, y};
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getRowSize() {
        return this.rowSize;
    }

    public boolean equals(Object o) {
        if (!(o instanceof BitMatrix)) {
            return false;
        }
        BitMatrix other = (BitMatrix) o;
        if (this.width == other.width && this.height == other.height && this.rowSize == other
                .rowSize && Arrays.equals(this.bits, other.bits)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (((((((this.width * 31) + this.width) * 31) + this.height) * 31) + this.rowSize) *
                31) + Arrays.hashCode(this.bits);
    }

    public String toString() {
        return toString("X ", "  ");
    }

    public String toString(String setString, String unsetString) {
        return toString(setString, unsetString, "\n");
    }

    @Deprecated
    public String toString(String setString, String unsetString, String lineSeparator) {
        StringBuilder result = new StringBuilder(this.height * (this.width + 1));
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                String str;
                if (get(x, y)) {
                    str = setString;
                } else {
                    str = unsetString;
                }
                result.append(str);
            }
            result.append(lineSeparator);
        }
        return result.toString();
    }

    public BitMatrix clone() {
        return new BitMatrix(this.width, this.height, this.rowSize, (int[]) this.bits.clone());
    }
}
