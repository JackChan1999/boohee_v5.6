package com.baidu.location;

import java.util.List;

class at implements ax {
    public static String kt = null;
    private int kA = 0;
    private boolean kB = false;
    private int kC = 0;
    private int kD = 0;
    private String kE = "";
    private String kF = "";
    private boolean kG = false;
    public int ko = 0;
    private int kp = 1;
    private List kq = null;
    private final boolean kr = false;
    private double ks = 0.0d;
    private String ku = "";
    private boolean kv = false;
    private double kw = 0.0d;
    private char kx = 'N';
    private String ky = "";
    private boolean kz = false;

    public at(List list, String str, String str2, String str3) {
        this.kq = list;
        this.ky = str;
        this.kE = str2;
        this.kF = str3;
        cS();
    }

    private void cS() {
        int i = 0;
        if (t(this.kF)) {
            String substring = this.kF.substring(0, this.kF.length() - 3);
            int i2 = 0;
            while (i < substring.length()) {
                if (substring.charAt(i) == ',') {
                    i2++;
                }
                i++;
            }
            String[] split = substring.split(",", i2 + 1);
            if (split.length >= 6) {
                if (!(split[2].equals("") || split[split.length - 3].equals("") || split[split.length - 2].equals("") || split[split.length - 1].equals(""))) {
                    this.kp = Integer.valueOf(split[2]).intValue();
                    this.kw = Double.valueOf(split[split.length - 3]).doubleValue();
                    this.ks = Double.valueOf(split[split.length - 2]).doubleValue();
                    this.kB = true;
                }
            } else {
                return;
            }
        }
        this.kz = this.kB;
    }

    private boolean t(String str) {
        if (str == null || str.length() <= 8) {
            return false;
        }
        int i = 0;
        for (int i2 = 1; i2 < str.length() - 3; i2++) {
            i ^= str.charAt(i2);
        }
        return Integer.toHexString(i).equalsIgnoreCase(str.substring(str.length() + -2, str.length()));
    }

    public double cT() {
        return this.ks;
    }

    public String cU() {
        return this.ku;
    }

    public boolean cV() {
        return this.kz;
    }

    public double cW() {
        return this.kw;
    }

    public int cX() {
        return this.kA;
    }
}
