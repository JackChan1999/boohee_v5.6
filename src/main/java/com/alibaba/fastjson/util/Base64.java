package com.alibaba.fastjson.util;

import java.util.Arrays;

public class Base64 {
    public static final char[] CA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
    public static final int[] IA = new int[256];

    static {
        Arrays.fill(IA, -1);
        int iS = CA.length;
        for (int i = 0; i < iS; i++) {
            IA[CA[i]] = i;
        }
        IA[61] = 0;
    }

    public static final byte[] decodeFast(char[] chars, int offset, int charsLen) {
        if (charsLen == 0) {
            return new byte[0];
        }
        int sepCnt;
        int i;
        int sIx = offset;
        int eIx = (offset + charsLen) - 1;
        while (sIx < eIx && IA[chars[sIx]] < 0) {
            sIx++;
        }
        while (eIx > 0 && IA[chars[eIx]] < 0) {
            eIx--;
        }
        int pad = chars[eIx] == '=' ? chars[eIx + -1] == '=' ? 2 : 1 : 0;
        int cCnt = (eIx - sIx) + 1;
        if (charsLen > 76) {
            sepCnt = (chars[76] == '\r' ? cCnt / 78 : 0) << 1;
        } else {
            sepCnt = 0;
        }
        int len = (((cCnt - sepCnt) * 6) >> 3) - pad;
        byte[] bytes = new byte[len];
        int cc = 0;
        int eLen = (len / 3) * 3;
        int d = 0;
        int sIx2 = sIx;
        while (d < eLen) {
            sIx = sIx2 + 1;
            sIx2 = sIx + 1;
            sIx = sIx2 + 1;
            sIx2 = sIx + 1;
            int i2 = (((IA[chars[sIx2]] << 18) | (IA[chars[sIx]] << 12)) | (IA[chars[sIx2]] << 6)) | IA[chars[sIx]];
            i = d + 1;
            bytes[d] = (byte) (i2 >> 16);
            d = i + 1;
            bytes[i] = (byte) (i2 >> 8);
            i = d + 1;
            bytes[d] = (byte) i2;
            if (sepCnt > 0) {
                cc++;
                if (cc == 19) {
                    sIx = sIx2 + 2;
                    cc = 0;
                    d = i;
                    sIx2 = sIx;
                }
            }
            sIx = sIx2;
            d = i;
            sIx2 = sIx;
        }
        if (d < len) {
            i2 = 0;
            int j = 0;
            while (sIx2 <= eIx - pad) {
                i2 |= IA[chars[sIx2]] << (18 - (j * 6));
                j++;
                sIx2++;
            }
            int r = 16;
            while (d < len) {
                i = d + 1;
                bytes[d] = (byte) (i2 >> r);
                r -= 8;
                d = i;
            }
        }
        i = d;
        sIx = sIx2;
        return bytes;
    }

    public static final byte[] decodeFast(String chars, int offset, int charsLen) {
        if (charsLen == 0) {
            return new byte[0];
        }
        int sepCnt;
        int i;
        int sIx = offset;
        int eIx = (offset + charsLen) - 1;
        while (sIx < eIx && IA[chars.charAt(sIx)] < 0) {
            sIx++;
        }
        while (eIx > 0 && IA[chars.charAt(eIx)] < 0) {
            eIx--;
        }
        int pad = chars.charAt(eIx) == '=' ? chars.charAt(eIx + -1) == '=' ? 2 : 1 : 0;
        int cCnt = (eIx - sIx) + 1;
        if (charsLen > 76) {
            sepCnt = (chars.charAt(76) == '\r' ? cCnt / 78 : 0) << 1;
        } else {
            sepCnt = 0;
        }
        int len = (((cCnt - sepCnt) * 6) >> 3) - pad;
        byte[] bytes = new byte[len];
        int cc = 0;
        int eLen = (len / 3) * 3;
        int d = 0;
        int sIx2 = sIx;
        while (d < eLen) {
            sIx = sIx2 + 1;
            sIx2 = sIx + 1;
            sIx = sIx2 + 1;
            sIx2 = sIx + 1;
            int i2 = (((IA[chars.charAt(sIx2)] << 18) | (IA[chars.charAt(sIx)] << 12)) | (IA[chars.charAt(sIx2)] << 6)) | IA[chars.charAt(sIx)];
            i = d + 1;
            bytes[d] = (byte) (i2 >> 16);
            d = i + 1;
            bytes[i] = (byte) (i2 >> 8);
            i = d + 1;
            bytes[d] = (byte) i2;
            if (sepCnt > 0) {
                cc++;
                if (cc == 19) {
                    sIx = sIx2 + 2;
                    cc = 0;
                    d = i;
                    sIx2 = sIx;
                }
            }
            sIx = sIx2;
            d = i;
            sIx2 = sIx;
        }
        if (d < len) {
            i2 = 0;
            int j = 0;
            while (sIx2 <= eIx - pad) {
                i2 |= IA[chars.charAt(sIx2)] << (18 - (j * 6));
                j++;
                sIx2++;
            }
            int r = 16;
            while (d < len) {
                i = d + 1;
                bytes[d] = (byte) (i2 >> r);
                r -= 8;
                d = i;
            }
        }
        i = d;
        sIx = sIx2;
        return bytes;
    }

    public static final byte[] decodeFast(String s) {
        int sLen = s.length();
        if (sLen == 0) {
            return new byte[0];
        }
        int sepCnt;
        int i;
        int sIx = 0;
        int eIx = sLen - 1;
        while (sIx < eIx && IA[s.charAt(sIx) & 255] < 0) {
            sIx++;
        }
        while (eIx > 0 && IA[s.charAt(eIx) & 255] < 0) {
            eIx--;
        }
        int pad = s.charAt(eIx) == '=' ? s.charAt(eIx + -1) == '=' ? 2 : 1 : 0;
        int cCnt = (eIx - sIx) + 1;
        if (sLen > 76) {
            sepCnt = (s.charAt(76) == '\r' ? cCnt / 78 : 0) << 1;
        } else {
            sepCnt = 0;
        }
        int len = (((cCnt - sepCnt) * 6) >> 3) - pad;
        byte[] dArr = new byte[len];
        int cc = 0;
        int eLen = (len / 3) * 3;
        int d = 0;
        int sIx2 = sIx;
        while (d < eLen) {
            sIx = sIx2 + 1;
            sIx2 = sIx + 1;
            sIx = sIx2 + 1;
            sIx2 = sIx + 1;
            int i2 = (((IA[s.charAt(sIx2)] << 18) | (IA[s.charAt(sIx)] << 12)) | (IA[s.charAt(sIx2)] << 6)) | IA[s.charAt(sIx)];
            i = d + 1;
            dArr[d] = (byte) (i2 >> 16);
            d = i + 1;
            dArr[i] = (byte) (i2 >> 8);
            i = d + 1;
            dArr[d] = (byte) i2;
            if (sepCnt > 0) {
                cc++;
                if (cc == 19) {
                    sIx = sIx2 + 2;
                    cc = 0;
                    d = i;
                    sIx2 = sIx;
                }
            }
            sIx = sIx2;
            d = i;
            sIx2 = sIx;
        }
        if (d < len) {
            i2 = 0;
            int j = 0;
            while (sIx2 <= eIx - pad) {
                i2 |= IA[s.charAt(sIx2)] << (18 - (j * 6));
                j++;
                sIx2++;
            }
            int r = 16;
            while (d < len) {
                i = d + 1;
                dArr[d] = (byte) (i2 >> r);
                r -= 8;
                d = i;
            }
        }
        i = d;
        sIx = sIx2;
        return dArr;
    }
}
