package com.tencent.tinker.android.dx.util;

public final class Hex {
    private Hex() {
    }

    public static String u8(long v) {
        char[] result = new char[16];
        for (int i = 0; i < 16; i++) {
            result[15 - i] = Character.forDigit(((int) v) & 15, 16);
            v >>= 4;
        }
        return new String(result);
    }

    public static String u4(int v) {
        char[] result = new char[8];
        for (int i = 0; i < 8; i++) {
            result[7 - i] = Character.forDigit(v & 15, 16);
            v >>= 4;
        }
        return new String(result);
    }

    public static String u3(int v) {
        char[] result = new char[6];
        for (int i = 0; i < 6; i++) {
            result[5 - i] = Character.forDigit(v & 15, 16);
            v >>= 4;
        }
        return new String(result);
    }

    public static String u2(int v) {
        char[] result = new char[4];
        for (int i = 0; i < 4; i++) {
            result[3 - i] = Character.forDigit(v & 15, 16);
            v >>= 4;
        }
        return new String(result);
    }

    public static String u2or4(int v) {
        if (v == ((char) v)) {
            return u2(v);
        }
        return u4(v);
    }

    public static String u1(int v) {
        char[] result = new char[2];
        for (int i = 0; i < 2; i++) {
            result[1 - i] = Character.forDigit(v & 15, 16);
            v >>= 4;
        }
        return new String(result);
    }

    public static String uNibble(int v) {
        return new String(new char[]{Character.forDigit(v & 15, 16)});
    }

    public static String s8(long v) {
        char[] result = new char[17];
        if (v < 0) {
            result[0] = '-';
            v = -v;
        } else {
            result[0] = '+';
        }
        for (int i = 0; i < 16; i++) {
            result[16 - i] = Character.forDigit(((int) v) & 15, 16);
            v >>= 4;
        }
        return new String(result);
    }

    public static String s4(int v) {
        char[] result = new char[9];
        if (v < 0) {
            result[0] = '-';
            v = -v;
        } else {
            result[0] = '+';
        }
        for (int i = 0; i < 8; i++) {
            result[8 - i] = Character.forDigit(v & 15, 16);
            v >>= 4;
        }
        return new String(result);
    }

    public static String s2(int v) {
        char[] result = new char[5];
        if (v < 0) {
            result[0] = '-';
            v = -v;
        } else {
            result[0] = '+';
        }
        for (int i = 0; i < 4; i++) {
            result[4 - i] = Character.forDigit(v & 15, 16);
            v >>= 4;
        }
        return new String(result);
    }

    public static String s1(int v) {
        char[] result = new char[3];
        if (v < 0) {
            result[0] = '-';
            v = -v;
        } else {
            result[0] = '+';
        }
        for (int i = 0; i < 2; i++) {
            result[2 - i] = Character.forDigit(v & 15, 16);
            v >>= 4;
        }
        return new String(result);
    }

    public static String dump(byte[] arr, int offset, int length, int outOffset, int bpl, int
            addressLength) {
        int end = offset + length;
        if (((offset | length) | end) < 0 || end > arr.length) {
            throw new IndexOutOfBoundsException("arr.length " + arr.length + "; " + offset +
                    "..!" + end);
        } else if (outOffset < 0) {
            throw new IllegalArgumentException("outOffset < 0");
        } else if (length == 0) {
            return "";
        } else {
            StringBuffer sb = new StringBuffer((length * 4) + 6);
            int col = 0;
            while (length > 0) {
                if (col == 0) {
                    String astr;
                    switch (addressLength) {
                        case 2:
                            astr = u1(outOffset);
                            break;
                        case 4:
                            astr = u2(outOffset);
                            break;
                        case 6:
                            astr = u3(outOffset);
                            break;
                        default:
                            astr = u4(outOffset);
                            break;
                    }
                    sb.append(astr);
                    sb.append(": ");
                } else if ((col & 1) == 0) {
                    sb.append(' ');
                }
                sb.append(u1(arr[offset]));
                outOffset++;
                offset++;
                col++;
                if (col == bpl) {
                    sb.append('\n');
                    col = 0;
                }
                length--;
            }
            if (col != 0) {
                sb.append('\n');
            }
            return sb.toString();
        }
    }

    public static String toHexString(byte[] ubytes) {
        StringBuilder strBuilder = new StringBuilder(ubytes.length << 1);
        for (byte b : ubytes) {
            strBuilder.append(u1(b));
        }
        return strBuilder.toString();
    }
}
