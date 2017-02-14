package cn.sharesdk.framework.utils;

public class b extends d {
    private static final char[] a = new char[]{'+'};
    private static final char[] b = "0123456789ABCDEF".toCharArray();
    private final boolean c;
    private final boolean[] d;

    public b(String str, boolean z) {
        if (str.matches(".*[0-9A-Za-z].*")) {
            throw new IllegalArgumentException("Alphanumeric characters are always 'safe' and should not be explicitly specified");
        } else if (z && str.contains(" ")) {
            throw new IllegalArgumentException("plusForSpace cannot be specified when space is a 'safe' character");
        } else if (str.contains("%")) {
            throw new IllegalArgumentException("The '%' character cannot be specified as 'safe'");
        } else {
            this.c = z;
            this.d = a(str);
        }
    }

    private static boolean[] a(String str) {
        int i;
        int i2 = 0;
        char[] toCharArray = str.toCharArray();
        int i3 = 122;
        for (char max : toCharArray) {
            i3 = Math.max(max, i3);
        }
        boolean[] zArr = new boolean[(i3 + 1)];
        for (i = 48; i <= 57; i++) {
            zArr[i] = true;
        }
        for (i = 65; i <= 90; i++) {
            zArr[i] = true;
        }
        for (i = 97; i <= 122; i++) {
            zArr[i] = true;
        }
        i = toCharArray.length;
        while (i2 < i) {
            zArr[toCharArray[i2]] = true;
            i2++;
        }
        return zArr;
    }

    protected int a(CharSequence charSequence, int i, int i2) {
        while (i < i2) {
            char charAt = charSequence.charAt(i);
            if (charAt >= this.d.length || !this.d[charAt]) {
                break;
            }
            i++;
        }
        return i;
    }

    protected char[] a(int i) {
        if (i < this.d.length && this.d[i]) {
            return null;
        }
        if (i == 32 && this.c) {
            return a;
        }
        if (i <= 127) {
            return new char[]{'%', b[i & 15], b[i >>> 4]};
        } else if (i <= 2047) {
            r0 = new char[6];
            r0[0] = '%';
            r0[3] = '%';
            r0[5] = b[i & 15];
            r1 = i >>> 4;
            r0[4] = b[(r1 & 3) | 8];
            r1 >>>= 2;
            r0[2] = b[r1 & 15];
            r0[1] = b[(r1 >>> 4) | 12];
            return r0;
        } else if (i <= 65535) {
            r0 = new char[9];
            r1 = i >>> 4;
            r0[7] = b[(r1 & 3) | 8];
            r1 >>>= 2;
            r0[5] = b[r1 & 15];
            r1 >>>= 4;
            r0[4] = b[(r1 & 3) | 8];
            r0[2] = b[r1 >>> 2];
            return r0;
        } else if (i <= Surrogate.UCS4_MAX) {
            r0 = new char[12];
            r1 = i >>> 4;
            r0[10] = b[(r1 & 3) | 8];
            r1 >>>= 2;
            r0[8] = b[r1 & 15];
            r1 >>>= 4;
            r0[7] = b[(r1 & 3) | 8];
            r1 >>>= 2;
            r0[5] = b[r1 & 15];
            r1 >>>= 4;
            r0[4] = b[(r1 & 3) | 8];
            r0[2] = b[(r1 >>> 2) & 7];
            return r0;
        } else {
            throw new IllegalArgumentException("Invalid unicode character value " + i);
        }
    }

    public String escape(String str) {
        int length = str.length();
        for (int i = 0; i < length; i++) {
            char charAt = str.charAt(i);
            if (charAt >= this.d.length || !this.d[charAt]) {
                return a(str, i);
            }
        }
        return str;
    }
}
