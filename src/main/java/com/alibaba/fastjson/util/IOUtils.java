package com.alibaba.fastjson.util;

import com.alibaba.fastjson.JSONException;
import com.qiniu.android.dns.NetworkInfo;
import java.io.Closeable;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;

public class IOUtils {
    public static final char[] ASCII_CHARS = new char[]{'0', '0', '0', '1', '0', '2', '0', '3', '0', '4', '0', '5', '0', '6', '0', '7', '0', '8', '0', '9', '0', 'A', '0', 'B', '0', 'C', '0', 'D', '0', 'E', '0', 'F', '1', '0', '1', '1', '1', '2', '1', '3', '1', '4', '1', '5', '1', '6', '1', '7', '1', '8', '1', '9', '1', 'A', '1', 'B', '1', 'C', '1', 'D', '1', 'E', '1', 'F', '2', '0', '2', '1', '2', '2', '2', '3', '2', '4', '2', '5', '2', '6', '2', '7', '2', '8', '2', '9', '2', 'A', '2', 'B', '2', 'C', '2', 'D', '2', 'E', '2', 'F'};
    public static final char[] DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    static final char[] DigitOnes = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    static final char[] DigitTens = new char[]{'0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
    static final char[] digits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    public static final boolean[] firstIdentifierFlags = new boolean[256];
    public static final boolean[] identifierFlags = new boolean[256];
    public static final char[] replaceChars = new char[128];
    static final int[] sizeTable = new int[]{9, 99, NetworkInfo.ISP_OTHER, 9999, 99999, 999999, 9999999, 99999999, 999999999, ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED};
    public static final byte[] specicalFlags_doubleQuotes = new byte[256];
    public static final byte[] specicalFlags_singleQuotes = new byte[256];

    static {
        int i;
        char c = '\u0000';
        while (c < firstIdentifierFlags.length) {
            if (c >= 'A' && c <= 'Z') {
                firstIdentifierFlags[c] = true;
            } else if (c >= 'a' && c <= 'z') {
                firstIdentifierFlags[c] = true;
            } else if (c == '_') {
                firstIdentifierFlags[c] = true;
            }
            c = (char) (c + 1);
        }
        c = '\u0000';
        while (c < identifierFlags.length) {
            if (c >= 'A' && c <= 'Z') {
                identifierFlags[c] = true;
            } else if (c >= 'a' && c <= 'z') {
                identifierFlags[c] = true;
            } else if (c == '_') {
                identifierFlags[c] = true;
            } else if (c >= '0' && c <= '9') {
                identifierFlags[c] = true;
            }
            c = (char) (c + 1);
        }
        specicalFlags_doubleQuotes[0] = (byte) 4;
        specicalFlags_doubleQuotes[1] = (byte) 4;
        specicalFlags_doubleQuotes[2] = (byte) 4;
        specicalFlags_doubleQuotes[3] = (byte) 4;
        specicalFlags_doubleQuotes[4] = (byte) 4;
        specicalFlags_doubleQuotes[5] = (byte) 4;
        specicalFlags_doubleQuotes[6] = (byte) 4;
        specicalFlags_doubleQuotes[7] = (byte) 4;
        specicalFlags_doubleQuotes[8] = (byte) 1;
        specicalFlags_doubleQuotes[9] = (byte) 1;
        specicalFlags_doubleQuotes[10] = (byte) 1;
        specicalFlags_doubleQuotes[11] = (byte) 4;
        specicalFlags_doubleQuotes[12] = (byte) 1;
        specicalFlags_doubleQuotes[13] = (byte) 1;
        specicalFlags_doubleQuotes[34] = (byte) 1;
        specicalFlags_doubleQuotes[92] = (byte) 1;
        specicalFlags_singleQuotes[0] = (byte) 4;
        specicalFlags_singleQuotes[1] = (byte) 4;
        specicalFlags_singleQuotes[2] = (byte) 4;
        specicalFlags_singleQuotes[3] = (byte) 4;
        specicalFlags_singleQuotes[4] = (byte) 4;
        specicalFlags_singleQuotes[5] = (byte) 4;
        specicalFlags_singleQuotes[6] = (byte) 4;
        specicalFlags_singleQuotes[7] = (byte) 4;
        specicalFlags_singleQuotes[8] = (byte) 1;
        specicalFlags_singleQuotes[9] = (byte) 1;
        specicalFlags_singleQuotes[10] = (byte) 1;
        specicalFlags_singleQuotes[11] = (byte) 4;
        specicalFlags_singleQuotes[12] = (byte) 1;
        specicalFlags_singleQuotes[13] = (byte) 1;
        specicalFlags_singleQuotes[92] = (byte) 1;
        specicalFlags_singleQuotes[39] = (byte) 1;
        for (i = 14; i <= 31; i++) {
            specicalFlags_doubleQuotes[i] = (byte) 4;
            specicalFlags_singleQuotes[i] = (byte) 4;
        }
        for (i = 127; i <= 160; i++) {
            specicalFlags_doubleQuotes[i] = (byte) 4;
            specicalFlags_singleQuotes[i] = (byte) 4;
        }
        replaceChars[0] = '0';
        replaceChars[1] = '1';
        replaceChars[2] = '2';
        replaceChars[3] = '3';
        replaceChars[4] = '4';
        replaceChars[5] = '5';
        replaceChars[6] = '6';
        replaceChars[7] = '7';
        replaceChars[8] = 'b';
        replaceChars[9] = 't';
        replaceChars[10] = 'n';
        replaceChars[11] = 'v';
        replaceChars[12] = 'f';
        replaceChars[13] = 'r';
        replaceChars[34] = '\"';
        replaceChars[39] = '\'';
        replaceChars[47] = '/';
        replaceChars[92] = '\\';
    }

    public static void close(Closeable x) {
        if (x != null) {
            try {
                x.close();
            } catch (Exception e) {
            }
        }
    }

    public static int stringSize(long x) {
        long p = 10;
        for (int i = 1; i < 19; i++) {
            if (x < p) {
                return i;
            }
            p *= 10;
        }
        return 19;
    }

    public static void getChars(long i, int index, char[] buf) {
        int charPos = index;
        char sign = '\u0000';
        if (i < 0) {
            sign = '-';
            i = -i;
        }
        while (i > 2147483647L) {
            long q = i / 100;
            int r = (int) (i - (((q << 6) + (q << 5)) + (q << 2)));
            i = q;
            charPos--;
            buf[charPos] = DigitOnes[r];
            charPos--;
            buf[charPos] = DigitTens[r];
        }
        int i2 = (int) i;
        while (i2 >= 65536) {
            int q2 = i2 / 100;
            r = i2 - (((q2 << 6) + (q2 << 5)) + (q2 << 2));
            i2 = q2;
            charPos--;
            buf[charPos] = DigitOnes[r];
            charPos--;
            buf[charPos] = DigitTens[r];
        }
        do {
            q2 = (52429 * i2) >>> 19;
            charPos--;
            buf[charPos] = digits[i2 - ((q2 << 3) + (q2 << 1))];
            i2 = q2;
        } while (i2 != 0);
        if (sign != '\u0000') {
            buf[charPos - 1] = sign;
        }
    }

    public static void getChars(int i, int index, char[] buf) {
        int charPos = index;
        char sign = '\u0000';
        if (i < 0) {
            sign = '-';
            i = -i;
        }
        while (i >= 65536) {
            int q = i / 100;
            int r = i - (((q << 6) + (q << 5)) + (q << 2));
            i = q;
            charPos--;
            buf[charPos] = DigitOnes[r];
            charPos--;
            buf[charPos] = DigitTens[r];
        }
        do {
            q = (52429 * i) >>> 19;
            charPos--;
            buf[charPos] = digits[i - ((q << 3) + (q << 1))];
            i = q;
        } while (i != 0);
        if (sign != '\u0000') {
            buf[charPos - 1] = sign;
        }
    }

    public static void getChars(byte b, int index, char[] buf) {
        int i = b;
        int charPos = index;
        char sign = '\u0000';
        if (i < (byte) 0) {
            sign = '-';
            i = -i;
        }
        do {
            int q = (52429 * i) >>> 19;
            charPos--;
            buf[charPos] = digits[i - ((q << 3) + (q << 1))];
            i = q;
        } while (i != 0);
        if (sign != '\u0000') {
            buf[charPos - 1] = sign;
        }
    }

    public static int stringSize(int x) {
        int i = 0;
        while (x > sizeTable[i]) {
            i++;
        }
        return i + 1;
    }

    public static void decode(CharsetDecoder charsetDecoder, ByteBuffer byteBuf, CharBuffer charByte) {
        try {
            CoderResult cr = charsetDecoder.decode(byteBuf, charByte, true);
            if (!cr.isUnderflow()) {
                cr.throwException();
            }
            cr = charsetDecoder.flush(charByte);
            if (!cr.isUnderflow()) {
                cr.throwException();
            }
        } catch (CharacterCodingException x) {
            throw new JSONException(x.getMessage(), x);
        }
    }
}
