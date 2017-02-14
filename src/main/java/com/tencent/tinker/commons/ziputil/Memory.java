package com.tencent.tinker.commons.ziputil;

import java.nio.ByteOrder;

public final class Memory {
    private Memory() {
    }

    public static int peekInt(byte[] src, int offset, ByteOrder order) {
        if (order == ByteOrder.BIG_ENDIAN) {
            int offset2 = offset + 1;
            offset = offset2 + 1;
            offset2 = offset + 1;
            int i = ((((src[offset] & 255) << 24) | ((src[offset2] & 255) << 16)) | ((src[offset]
                    & 255) << 8)) | ((src[offset2] & 255) << 0);
            offset = offset2;
            return i;
        }
        offset2 = offset + 1;
        offset = offset2 + 1;
        offset2 = offset + 1;
        i = ((((src[offset] & 255) << 0) | ((src[offset2] & 255) << 8)) | ((src[offset] & 255) <<
                16)) | ((src[offset2] & 255) << 24);
        offset = offset2;
        return i;
    }

    public static long peekLong(byte[] src, int offset, ByteOrder order) {
        if (order == ByteOrder.BIG_ENDIAN) {
            int offset2 = offset + 1;
            offset = offset2 + 1;
            offset2 = offset + 1;
            offset = offset2 + 1;
            int h = ((((src[offset] & 255) << 24) | ((src[offset2] & 255) << 16)) | ((src[offset]
                    & 255) << 8)) | ((src[offset2] & 255) << 0);
            offset2 = offset + 1;
            offset = offset2 + 1;
            offset2 = offset + 1;
            int l = ((((src[offset] & 255) << 24) | ((src[offset2] & 255) << 16)) | ((src[offset]
                    & 255) << 8)) | ((src[offset2] & 255) << 0);
            offset = offset2;
            return (((long) h) << 32) | (((long) l) & 4294967295L);
        }
        offset2 = offset + 1;
        offset = offset2 + 1;
        offset2 = offset + 1;
        offset = offset2 + 1;
        l = ((((src[offset] & 255) << 0) | ((src[offset2] & 255) << 8)) | ((src[offset] & 255) <<
                16)) | ((src[offset2] & 255) << 24);
        offset2 = offset + 1;
        offset = offset2 + 1;
        offset2 = offset + 1;
        long j = (((long) (((((src[offset] & 255) << 0) | ((src[offset2] & 255) << 8)) | (
                (src[offset] & 255) << 16)) | ((src[offset2] & 255) << 24))) << 32) | (((long) l)
                & 4294967295L);
        offset = offset2;
        return j;
    }

    public static short peekShort(byte[] src, int offset, ByteOrder order) {
        if (order == ByteOrder.BIG_ENDIAN) {
            return (short) ((src[offset] << 8) | (src[offset + 1] & 255));
        }
        return (short) ((src[offset + 1] << 8) | (src[offset] & 255));
    }

    public static void pokeInt(byte[] dst, int offset, int value, ByteOrder order) {
        if (order == ByteOrder.BIG_ENDIAN) {
            int i = offset + 1;
            dst[offset] = (byte) ((value >> 24) & 255);
            offset = i + 1;
            dst[i] = (byte) ((value >> 16) & 255);
            i = offset + 1;
            dst[offset] = (byte) ((value >> 8) & 255);
            dst[i] = (byte) ((value >> 0) & 255);
            offset = i;
            return;
        }
        i = offset + 1;
        dst[offset] = (byte) ((value >> 0) & 255);
        offset = i + 1;
        dst[i] = (byte) ((value >> 8) & 255);
        i = offset + 1;
        dst[offset] = (byte) ((value >> 16) & 255);
        dst[i] = (byte) ((value >> 24) & 255);
        offset = i;
    }

    public static void pokeLong(byte[] dst, int offset, long value, ByteOrder order) {
        if (order == ByteOrder.BIG_ENDIAN) {
            int i = (int) (value >> 32);
            int i2 = offset + 1;
            dst[offset] = (byte) ((i >> 24) & 255);
            offset = i2 + 1;
            dst[i2] = (byte) ((i >> 16) & 255);
            i2 = offset + 1;
            dst[offset] = (byte) ((i >> 8) & 255);
            offset = i2 + 1;
            dst[i2] = (byte) ((i >> 0) & 255);
            i = (int) value;
            i2 = offset + 1;
            dst[offset] = (byte) ((i >> 24) & 255);
            offset = i2 + 1;
            dst[i2] = (byte) ((i >> 16) & 255);
            i2 = offset + 1;
            dst[offset] = (byte) ((i >> 8) & 255);
            dst[i2] = (byte) ((i >> 0) & 255);
            offset = i2;
            return;
        }
        i = (int) value;
        i2 = offset + 1;
        dst[offset] = (byte) ((i >> 0) & 255);
        offset = i2 + 1;
        dst[i2] = (byte) ((i >> 8) & 255);
        i2 = offset + 1;
        dst[offset] = (byte) ((i >> 16) & 255);
        offset = i2 + 1;
        dst[i2] = (byte) ((i >> 24) & 255);
        i = (int) (value >> 32);
        i2 = offset + 1;
        dst[offset] = (byte) ((i >> 0) & 255);
        offset = i2 + 1;
        dst[i2] = (byte) ((i >> 8) & 255);
        i2 = offset + 1;
        dst[offset] = (byte) ((i >> 16) & 255);
        dst[i2] = (byte) ((i >> 24) & 255);
        offset = i2;
    }

    public static void pokeShort(byte[] dst, int offset, short value, ByteOrder order) {
        if (order == ByteOrder.BIG_ENDIAN) {
            int offset2 = offset + 1;
            dst[offset] = (byte) ((value >> 8) & 255);
            dst[offset2] = (byte) ((value >> 0) & 255);
            offset = offset2;
            return;
        }
        offset2 = offset + 1;
        dst[offset] = (byte) ((value >> 0) & 255);
        dst[offset2] = (byte) ((value >> 8) & 255);
        offset = offset2;
    }
}
