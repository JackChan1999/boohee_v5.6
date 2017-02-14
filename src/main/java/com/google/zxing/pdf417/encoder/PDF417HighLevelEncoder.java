package com.google.zxing.pdf417.encoder;

import com.google.zxing.WriterException;
import com.google.zxing.common.CharacterSetECI;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Arrays;

final class PDF417HighLevelEncoder {
    private static final int     BYTE_COMPACTION      = 1;
    private static final Charset DEFAULT_ENCODING     = Charset.forName("ISO-8859-1");
    private static final int     ECI_CHARSET          = 927;
    private static final int     ECI_GENERAL_PURPOSE  = 926;
    private static final int     ECI_USER_DEFINED     = 925;
    private static final int     LATCH_TO_BYTE        = 924;
    private static final int     LATCH_TO_BYTE_PADDED = 901;
    private static final int     LATCH_TO_NUMERIC     = 902;
    private static final int     LATCH_TO_TEXT        = 900;
    private static final byte[]  MIXED                = new byte[128];
    private static final int     NUMERIC_COMPACTION   = 2;
    private static final byte[]  PUNCTUATION          = new byte[128];
    private static final int     SHIFT_TO_BYTE        = 913;
    private static final int     SUBMODE_ALPHA        = 0;
    private static final int     SUBMODE_LOWER        = 1;
    private static final int     SUBMODE_MIXED        = 2;
    private static final int     SUBMODE_PUNCTUATION  = 3;
    private static final int     TEXT_COMPACTION      = 0;
    private static final byte[]  TEXT_MIXED_RAW       = new byte[]{(byte) 48, (byte) 49, (byte)
            50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57,
            (byte) 38, (byte) 13, (byte) 9, (byte) 44, (byte) 58, (byte) 35, (byte) 45, (byte)
            46, (byte) 36, (byte) 47, (byte) 43, (byte) 37, (byte) 42, (byte) 61, (byte) 94,
            (byte) 0, (byte) 32, (byte) 0, (byte) 0, (byte) 0};
    private static final byte[]  TEXT_PUNCTUATION_RAW = new byte[]{(byte) 59, (byte) 60, (byte)
            62, (byte) 64, (byte) 91, (byte) 92, (byte) 93, (byte) 95, (byte) 96, (byte) 126,
            (byte) 33, (byte) 13, (byte) 9, (byte) 44, (byte) 58, (byte) 10, (byte) 45, (byte)
            46, (byte) 36, (byte) 47, (byte) 34, (byte) 124, (byte) 42, (byte) 40, (byte) 41,
            (byte) 63, (byte) 123, (byte) 125, (byte) 39, (byte) 0};

    static {
        byte i;
        Arrays.fill(MIXED, (byte) -1);
        for (i = (byte) 0; i < TEXT_MIXED_RAW.length; i = (byte) (i + 1)) {
            byte b = TEXT_MIXED_RAW[i];
            if (b > (byte) 0) {
                MIXED[b] = i;
            }
        }
        Arrays.fill(PUNCTUATION, (byte) -1);
        for (i = (byte) 0; i < TEXT_PUNCTUATION_RAW.length; i = (byte) (i + 1)) {
            b = TEXT_PUNCTUATION_RAW[i];
            if (b > (byte) 0) {
                PUNCTUATION[b] = i;
            }
        }
    }

    private PDF417HighLevelEncoder() {
    }

    static String encodeHighLevel(String msg, Compaction compaction, Charset encoding) throws
            WriterException {
        StringBuilder sb = new StringBuilder(msg.length());
        if (encoding == null) {
            encoding = DEFAULT_ENCODING;
        } else if (!DEFAULT_ENCODING.equals(encoding)) {
            CharacterSetECI eci = CharacterSetECI.getCharacterSetECIByName(encoding.name());
            if (eci != null) {
                encodingECI(eci.getValue(), sb);
            }
        }
        int len = msg.length();
        int p = 0;
        int textSubMode = 0;
        if (compaction == Compaction.TEXT) {
            encodeText(msg, 0, len, sb, 0);
        } else if (compaction == Compaction.BYTE) {
            bytes = msg.getBytes(encoding);
            encodeBinary(bytes, 0, bytes.length, 1, sb);
        } else if (compaction == Compaction.NUMERIC) {
            sb.append('Ά');
            encodeNumeric(msg, 0, len, sb);
        } else {
            int encodingMode = 0;
            while (p < len) {
                int n = determineConsecutiveDigitCount(msg, p);
                if (n >= 13) {
                    sb.append('Ά');
                    encodingMode = 2;
                    textSubMode = 0;
                    encodeNumeric(msg, p, n, sb);
                    p += n;
                } else {
                    int t = determineConsecutiveTextCount(msg, p);
                    if (t >= 5 || n == len) {
                        if (encodingMode != 0) {
                            sb.append('΄');
                            encodingMode = 0;
                            textSubMode = 0;
                        }
                        textSubMode = encodeText(msg, p, t, sb, textSubMode);
                        p += t;
                    } else {
                        int b = determineConsecutiveBinaryCount(msg, p, encoding);
                        if (b == 0) {
                            b = 1;
                        }
                        bytes = msg.substring(p, p + b).getBytes(encoding);
                        if (bytes.length == 1 && encodingMode == 0) {
                            encodeBinary(bytes, 0, 1, 0, sb);
                        } else {
                            encodeBinary(bytes, 0, bytes.length, encodingMode, sb);
                            encodingMode = 1;
                            textSubMode = 0;
                        }
                        p += b;
                    }
                }
            }
        }
        return sb.toString();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static int encodeText(java.lang.CharSequence r11, int r12, int r13, java.lang
            .StringBuilder r14, int r15) {
        /*
        r8 = new java.lang.StringBuilder;
        r8.<init>(r13);
        r7 = r15;
        r3 = 0;
    L_0x0007:
        r9 = r12 + r3;
        r0 = r11.charAt(r9);
        switch(r7) {
            case 0: goto L_0x003f;
            case 1: goto L_0x007e;
            case 2: goto L_0x00c5;
            default: goto L_0x0010;
        };
    L_0x0010:
        r9 = isPunctuation(r0);
        if (r9 == 0) goto L_0x011c;
    L_0x0016:
        r9 = PUNCTUATION;
        r9 = r9[r0];
        r9 = (char) r9;
        r8.append(r9);
    L_0x001e:
        r3 = r3 + 1;
        if (r3 < r13) goto L_0x0007;
    L_0x0022:
        r1 = 0;
        r4 = r8.length();
        r2 = 0;
    L_0x0028:
        if (r2 >= r4) goto L_0x012d;
    L_0x002a:
        r9 = r2 % 2;
        if (r9 == 0) goto L_0x0124;
    L_0x002e:
        r6 = 1;
    L_0x002f:
        if (r6 == 0) goto L_0x0127;
    L_0x0031:
        r9 = r1 * 30;
        r10 = r8.charAt(r2);
        r9 = r9 + r10;
        r1 = (char) r9;
        r14.append(r1);
    L_0x003c:
        r2 = r2 + 1;
        goto L_0x0028;
    L_0x003f:
        r9 = isAlphaUpper(r0);
        if (r9 == 0) goto L_0x0056;
    L_0x0045:
        r9 = 32;
        if (r0 != r9) goto L_0x004f;
    L_0x0049:
        r9 = 26;
        r8.append(r9);
        goto L_0x001e;
    L_0x004f:
        r9 = r0 + -65;
        r9 = (char) r9;
        r8.append(r9);
        goto L_0x001e;
    L_0x0056:
        r9 = isAlphaLower(r0);
        if (r9 == 0) goto L_0x0063;
    L_0x005c:
        r7 = 1;
        r9 = 27;
        r8.append(r9);
        goto L_0x0007;
    L_0x0063:
        r9 = isMixed(r0);
        if (r9 == 0) goto L_0x0070;
    L_0x0069:
        r7 = 2;
        r9 = 28;
        r8.append(r9);
        goto L_0x0007;
    L_0x0070:
        r9 = 29;
        r8.append(r9);
        r9 = PUNCTUATION;
        r9 = r9[r0];
        r9 = (char) r9;
        r8.append(r9);
        goto L_0x001e;
    L_0x007e:
        r9 = isAlphaLower(r0);
        if (r9 == 0) goto L_0x0095;
    L_0x0084:
        r9 = 32;
        if (r0 != r9) goto L_0x008e;
    L_0x0088:
        r9 = 26;
        r8.append(r9);
        goto L_0x001e;
    L_0x008e:
        r9 = r0 + -97;
        r9 = (char) r9;
        r8.append(r9);
        goto L_0x001e;
    L_0x0095:
        r9 = isAlphaUpper(r0);
        if (r9 == 0) goto L_0x00a8;
    L_0x009b:
        r9 = 27;
        r8.append(r9);
        r9 = r0 + -65;
        r9 = (char) r9;
        r8.append(r9);
        goto L_0x001e;
    L_0x00a8:
        r9 = isMixed(r0);
        if (r9 == 0) goto L_0x00b6;
    L_0x00ae:
        r7 = 2;
        r9 = 28;
        r8.append(r9);
        goto L_0x0007;
    L_0x00b6:
        r9 = 29;
        r8.append(r9);
        r9 = PUNCTUATION;
        r9 = r9[r0];
        r9 = (char) r9;
        r8.append(r9);
        goto L_0x001e;
    L_0x00c5:
        r9 = isMixed(r0);
        if (r9 == 0) goto L_0x00d5;
    L_0x00cb:
        r9 = MIXED;
        r9 = r9[r0];
        r9 = (char) r9;
        r8.append(r9);
        goto L_0x001e;
    L_0x00d5:
        r9 = isAlphaUpper(r0);
        if (r9 == 0) goto L_0x00e3;
    L_0x00db:
        r7 = 0;
        r9 = 28;
        r8.append(r9);
        goto L_0x0007;
    L_0x00e3:
        r9 = isAlphaLower(r0);
        if (r9 == 0) goto L_0x00f1;
    L_0x00e9:
        r7 = 1;
        r9 = 27;
        r8.append(r9);
        goto L_0x0007;
    L_0x00f1:
        r9 = r12 + r3;
        r9 = r9 + 1;
        if (r9 >= r13) goto L_0x010d;
    L_0x00f7:
        r9 = r12 + r3;
        r9 = r9 + 1;
        r5 = r11.charAt(r9);
        r9 = isPunctuation(r5);
        if (r9 == 0) goto L_0x010d;
    L_0x0105:
        r7 = 3;
        r9 = 25;
        r8.append(r9);
        goto L_0x0007;
    L_0x010d:
        r9 = 29;
        r8.append(r9);
        r9 = PUNCTUATION;
        r9 = r9[r0];
        r9 = (char) r9;
        r8.append(r9);
        goto L_0x001e;
    L_0x011c:
        r7 = 0;
        r9 = 29;
        r8.append(r9);
        goto L_0x0007;
    L_0x0124:
        r6 = 0;
        goto L_0x002f;
    L_0x0127:
        r1 = r8.charAt(r2);
        goto L_0x003c;
    L_0x012d:
        r9 = r4 % 2;
        if (r9 == 0) goto L_0x0139;
    L_0x0131:
        r9 = r1 * 30;
        r9 = r9 + 29;
        r9 = (char) r9;
        r14.append(r9);
    L_0x0139:
        return r7;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.pdf417" +
                ".encoder.PDF417HighLevelEncoder.encodeText(java.lang.CharSequence, int, int, " +
                "java.lang.StringBuilder, int):int");
    }

    private static void encodeBinary(byte[] bytes, int startpos, int count, int startmode,
                                     StringBuilder sb) {
        int i;
        if (count == 1 && startmode == 0) {
            sb.append('Α');
        } else {
            if (count % 6 == 0) {
                sb.append('Μ');
            } else {
                sb.append('΅');
            }
        }
        int idx = startpos;
        if (count >= 6) {
            char[] chars = new char[5];
            while ((startpos + count) - idx >= 6) {
                long t = 0;
                for (i = 0; i < 6; i++) {
                    t = (t << 8) + ((long) (bytes[idx + i] & 255));
                }
                for (i = 0; i < 5; i++) {
                    chars[i] = (char) ((int) (t % 900));
                    t /= 900;
                }
                for (i = chars.length - 1; i >= 0; i--) {
                    sb.append(chars[i]);
                }
                idx += 6;
            }
        }
        for (i = idx; i < startpos + count; i++) {
            sb.append((char) (bytes[i] & 255));
        }
    }

    private static void encodeNumeric(String msg, int startpos, int count, StringBuilder sb) {
        int idx = 0;
        StringBuilder tmp = new StringBuilder((count / 3) + 1);
        BigInteger num900 = BigInteger.valueOf(900);
        BigInteger num0 = BigInteger.valueOf(0);
        while (idx < count) {
            tmp.setLength(0);
            int len = Math.min(44, count - idx);
            BigInteger bigint = new BigInteger('1' + msg.substring(startpos + idx, (startpos +
                    idx) + len));
            do {
                tmp.append((char) bigint.mod(num900).intValue());
                bigint = bigint.divide(num900);
            } while (!bigint.equals(num0));
            for (int i = tmp.length() - 1; i >= 0; i--) {
                sb.append(tmp.charAt(i));
            }
            idx += len;
        }
    }

    private static boolean isDigit(char ch) {
        return ch >= '0' && ch <= '9';
    }

    private static boolean isAlphaUpper(char ch) {
        return ch == ' ' || (ch >= 'A' && ch <= 'Z');
    }

    private static boolean isAlphaLower(char ch) {
        return ch == ' ' || (ch >= 'a' && ch <= 'z');
    }

    private static boolean isMixed(char ch) {
        return MIXED[ch] != (byte) -1;
    }

    private static boolean isPunctuation(char ch) {
        return PUNCTUATION[ch] != (byte) -1;
    }

    private static boolean isText(char ch) {
        return ch == '\t' || ch == '\n' || ch == '\r' || (ch >= ' ' && ch <= '~');
    }

    private static int determineConsecutiveDigitCount(CharSequence msg, int startpos) {
        int count = 0;
        int len = msg.length();
        int idx = startpos;
        if (idx < len) {
            char ch = msg.charAt(idx);
            while (isDigit(ch) && idx < len) {
                count++;
                idx++;
                if (idx < len) {
                    ch = msg.charAt(idx);
                }
            }
        }
        return count;
    }

    private static int determineConsecutiveTextCount(CharSequence msg, int startpos) {
        int len = msg.length();
        int idx = startpos;
        while (idx < len) {
            char ch = msg.charAt(idx);
            int numericCount = 0;
            while (numericCount < 13 && isDigit(ch) && idx < len) {
                numericCount++;
                idx++;
                if (idx < len) {
                    ch = msg.charAt(idx);
                }
            }
            if (numericCount >= 13) {
                return (idx - startpos) - numericCount;
            }
            if (numericCount <= 0) {
                if (!isText(msg.charAt(idx))) {
                    break;
                }
                idx++;
            }
        }
        return idx - startpos;
    }

    private static int determineConsecutiveBinaryCount(String msg, int startpos, Charset
            encoding) throws WriterException {
        CharsetEncoder encoder = encoding.newEncoder();
        int len = msg.length();
        int idx = startpos;
        while (idx < len) {
            char ch = msg.charAt(idx);
            int numericCount = 0;
            while (numericCount < 13 && isDigit(ch)) {
                numericCount++;
                int i = idx + numericCount;
                if (i >= len) {
                    break;
                }
                ch = msg.charAt(i);
            }
            if (numericCount >= 13) {
                return idx - startpos;
            }
            ch = msg.charAt(idx);
            if (encoder.canEncode(ch)) {
                idx++;
            } else {
                throw new WriterException("Non-encodable character detected: " + ch + " (Unicode:" +
                        " " + ch + ')');
            }
        }
        return idx - startpos;
    }

    private static void encodingECI(int eci, StringBuilder sb) throws WriterException {
        if (eci >= 0 && eci < LATCH_TO_TEXT) {
            sb.append('Ο');
            sb.append((char) eci);
        } else if (eci < 810900) {
            sb.append('Ξ');
            sb.append((char) ((eci / LATCH_TO_TEXT) - 1));
            sb.append((char) (eci % LATCH_TO_TEXT));
        } else if (eci < 811800) {
            sb.append('Ν');
            sb.append((char) (810900 - eci));
        } else {
            throw new WriterException("ECI number not in valid range from 0..811799, but was " + eci);
        }
    }
}
