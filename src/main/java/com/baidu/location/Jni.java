package com.baidu.location;

class Jni implements ax, n {
    private static int e7 = 14;
    private static int e8 = 13;
    private static int e9 = 1024;
    private static int fa = 11;
    private static int fb = 12;
    private static boolean fc;
    private static int fd = 1;
    private static int fe = 2;
    private static int ff = 0;

    static {
        fc = false;
        try {
            System.loadLibrary("locSDK4d");
        } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
            fc = true;
            throw new IllegalStateException("no found the liblocSDK4d.so file, please correct settings");
        }
    }

    Jni() {
    }

    private static native String a(byte[] bArr, int i);

    private static native String b(double d, double d2, int i, int i2);

    private static native String c(byte[] bArr, int i);

    private static native void f(byte[] bArr, byte[] bArr2);

    private static native String g(byte[] bArr);

    public static String i(String str) {
        return fc ? "err!" : l(str) + "|tp=3";
    }

    public static double[] if(double d, double d2, String str) {
        double[] dArr = new double[]{0.0d, 0.0d};
        if (fc) {
            return dArr;
        }
        int i = -1;
        if (str.equals(BDGeofence.COORD_TYPE_BD09)) {
            i = ff;
        } else if (str.equals(BDGeofence.COORD_TYPE_BD09LL)) {
            i = fd;
        } else if (str.equals(BDGeofence.COORD_TYPE_GCJ)) {
            i = fe;
        } else if (str.equals("gps2gcj")) {
            i = fa;
        } else if (str.equals("bd092gcj")) {
            i = fb;
        } else if (str.equals("bd09ll2gcj")) {
            i = e8;
        }
        try {
            String[] split = b(d, d2, i, 132456).split(":");
            dArr[0] = Double.parseDouble(split[0]);
            dArr[1] = Double.parseDouble(split[1]);
        } catch (Exception e) {
        }
        return dArr;
    }

    public static void int(String str, String str2) {
        try {
            f(str.getBytes(), str2.getBytes());
        } catch (Exception e) {
        }
    }

    public static String j(String str) {
        return fc ? "err!" : c(str.getBytes(), 132456);
    }

    public static String k(String str) {
        try {
            String g = g(str.getBytes());
            return (g == null || g.length() < 2 || "no".equals(g)) ? null : g;
        } catch (Exception e) {
            return null;
        }
    }

    public static String l(String str) {
        int i = 740;
        int i2 = 0;
        if (fc) {
            return "err!";
        }
        byte[] bytes = str.getBytes();
        byte[] bArr = new byte[e9];
        int length = bytes.length;
        if (length <= 740) {
            i = length;
        }
        length = 0;
        while (i2 < i) {
            if (bytes[i2] != (byte) 0) {
                bArr[length] = bytes[i2];
                length++;
            }
            i2++;
        }
        return a(bArr, 132456);
    }
}
