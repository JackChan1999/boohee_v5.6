package com.xiaomi.account.openauth.utils;

public class Base64Coder {
    private static       char[] map1                = new char[64];
    private static       byte[] map2                = new byte[128];
    private static final String systemLineSeparator = System.getProperty("line.separator");

    static {
        int i;
        char c = 'A';
        int i2 = 0;
        while (c <= 'Z') {
            i = i2 + 1;
            map1[i2] = c;
            c = (char) (c + 1);
            i2 = i;
        }
        c = 'a';
        while (c <= 'z') {
            i = i2 + 1;
            map1[i2] = c;
            c = (char) (c + 1);
            i2 = i;
        }
        c = '0';
        while (c <= '9') {
            i = i2 + 1;
            map1[i2] = c;
            c = (char) (c + 1);
            i2 = i;
        }
        i = i2 + 1;
        map1[i2] = '+';
        i2 = i + 1;
        map1[i] = '/';
        for (i = 0; i < map2.length; i++) {
            map2[i] = (byte) -1;
        }
        for (i = 0; i < 64; i++) {
            map2[map1[i]] = (byte) i;
        }
    }

    public static String encodeString(String s) {
        return new String(encode(s.getBytes()));
    }

    public static String encodeLines(byte[] in) {
        return encodeLines(in, 0, in.length, 76, systemLineSeparator);
    }

    public static String encodeLines(byte[] in, int iOff, int iLen, int lineLen, String
            lineSeparator) {
        int blockLen = (lineLen * 3) / 4;
        if (blockLen <= 0) {
            throw new IllegalArgumentException();
        }
        StringBuilder buf = new StringBuilder((((iLen + 2) / 3) * 4) + (lineSeparator.length() *
                (((iLen + blockLen) - 1) / blockLen)));
        int ip = 0;
        while (ip < iLen) {
            int l = Math.min(iLen - ip, blockLen);
            buf.append(encode(in, iOff + ip, l));
            buf.append(lineSeparator);
            ip += l;
        }
        return buf.toString();
    }

    public static char[] encode(byte[] in) {
        return encode(in, 0, in.length);
    }

    public static char[] encode(byte[] in, int iLen) {
        return encode(in, 0, iLen);
    }

    public static char[] encode(byte[] in, int iOff, int iLen) {
        int oDataLen = ((iLen * 4) + 2) / 3;
        char[] out = new char[(((iLen + 2) / 3) * 4)];
        int iEnd = iOff + iLen;
        int op = 0;
        int ip = iOff;
        while (ip < iEnd) {
            int i1;
            int i2;
            int ip2 = ip + 1;
            int i0 = in[ip] & 255;
            if (ip2 < iEnd) {
                ip = ip2 + 1;
                i1 = in[ip2] & 255;
            } else {
                i1 = 0;
                ip = ip2;
            }
            if (ip < iEnd) {
                ip2 = ip + 1;
                i2 = in[ip] & 255;
            } else {
                i2 = 0;
                ip2 = ip;
            }
            int o1 = ((i0 & 3) << 4) | (i1 >>> 4);
            int o2 = ((i1 & 15) << 2) | (i2 >>> 6);
            int o3 = i2 & 63;
            int i = op + 1;
            out[op] = map1[i0 >>> 2];
            op = i + 1;
            out[i] = map1[o1];
            out[op] = op < oDataLen ? map1[o2] : '=';
            i = op + 1;
            out[i] = i < oDataLen ? map1[o3] : '=';
            op = i + 1;
            ip = ip2;
        }
        return out;
    }

    public static String decodeString(String s) {
        return new String(decode(s));
    }

    public static byte[] decodeLines(String s) {
        char[] buf = new char[s.length()];
        int p = 0;
        for (int ip = 0; ip < s.length(); ip++) {
            char c = s.charAt(ip);
            if (!(c == ' ' || c == '\r' || c == '\n' || c == '\t')) {
                int p2 = p + 1;
                buf[p] = c;
                p = p2;
            }
        }
        return decode(buf, 0, p);
    }

    public static byte[] decode(String s) {
        return decode(s.toCharArray());
    }

    public static byte[] decode(char[] in) {
        return decode(in, 0, in.length);
    }

    public static byte[] decode(char[] in, int iOff, int iLen) {
        if (iLen % 4 != 0) {
            throw new IllegalArgumentException("Length of Base64 encoded input string is not a " +
                    "multiple of 4.");
        }
        while (iLen > 0 && in[(iOff + iLen) - 1] == '=') {
            iLen--;
        }
        int oLen = (iLen * 3) / 4;
        byte[] out = new byte[oLen];
        int iEnd = iOff + iLen;
        int op = 0;
        int ip = iOff;
        while (ip < iEnd) {
            int i2;
            int i3;
            int ip2 = ip + 1;
            int i0 = in[ip];
            ip = ip2 + 1;
            int i1 = in[ip2];
            if (ip < iEnd) {
                ip2 = ip + 1;
                i2 = in[ip];
                ip = ip2;
            } else {
                i2 = 65;
            }
            if (ip < iEnd) {
                ip2 = ip + 1;
                i3 = in[ip];
            } else {
                i3 = 65;
                ip2 = ip;
            }
            if (i0 > 127 || i1 > 127 || i2 > 127 || i3 > 127) {
                throw new IllegalArgumentException("Illegal character in Base64 encoded data.");
            }
            int b0 = map2[i0];
            int b1 = map2[i1];
            int b2 = map2[i2];
            int b3 = map2[i3];
            if (b0 < 0 || b1 < 0 || b2 < 0 || b3 < 0) {
                throw new IllegalArgumentException("Illegal character in Base64 encoded data.");
            }
            int o1 = ((b1 & 15) << 4) | (b2 >>> 2);
            int o2 = ((b2 & 3) << 6) | b3;
            int op2 = op + 1;
            out[op] = (byte) ((b0 << 2) | (b1 >>> 4));
            if (op2 < oLen) {
                op = op2 + 1;
                out[op2] = (byte) o1;
            } else {
                op = op2;
            }
            if (op < oLen) {
                op2 = op + 1;
                out[op] = (byte) o2;
                op = op2;
                ip = ip2;
            } else {
                ip = ip2;
            }
        }
        return out;
    }

    private Base64Coder() {
    }
}
