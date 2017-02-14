package com.baidu.location;

import android.text.TextUtils;

public class aq implements BDGeofence {
    private static final String byte = "Administrative";
    private static final int case = 2;
    private static final int d = 100;
    private static final int e = 2;
    private static final int else = 1;
    private static final String for = "Circle";
    private static final int i = 3;
    public static final int int = 1;
    private static final long void = 2592000;
    private final int a;
    private float b;
    private final int c;
    private boolean char;
    private final String do;
    private boolean f;
    protected int g;
    private final double goto;
    private long h;
    private boolean if;
    private final long long;
    private final String new;
    private final double try;

    public aq(int i, String str, double d, double d2, int i2, long j, String str2) {
        do(i2);
        if(str);
        a(d, d2);
        a(str2);
        if(j);
        this.c = i;
        this.do = str;
        this.goto = d;
        this.try = d2;
        this.a = i2;
        this.long = j;
        this.new = str2;
    }

    public aq(String str, double d, double d2, int i, long j, String str2) {
        this(1, str, d2, d, i, j, str2);
    }

    private static void a(double d, double d2) {
    }

    private static void a(String str) {
        if (!str.equals(BDGeofence.COORD_TYPE_BD09) && !str.equals(BDGeofence.COORD_TYPE_BD09LL) && !str.equals(BDGeofence.COORD_TYPE_GCJ)) {
            throw new IllegalArgumentException("invalid coord type: " + str);
        }
    }

    private static void do(int i) {
        if (i != 1) {
            throw new IllegalArgumentException("invalid radius type: " + i);
        }
    }

    private static String if(int i) {
        switch (i) {
            case 1:
                return for;
            case 2:
                return byte;
            default:
                return null;
        }
    }

    private static void if(long j) {
        if (((double) j) / 1000.0d > 2592000.0d) {
            throw new IllegalArgumentException("invalid druationMillis :" + j);
        }
    }

    private static void if(String str) {
        if (TextUtils.isEmpty(str) || str.length() > 100) {
            throw new IllegalArgumentException("Geofence name is null or too long: " + str);
        }
    }

    public double a() {
        return this.try;
    }

    public void a(float f) {
        this.b = f;
    }

    protected void a(int i) {
        this.g = i;
    }

    public void a(long j) {
        this.h = j;
    }

    public void a(boolean z) {
        this.if = z;
    }

    public boolean byte() {
        return this.f;
    }

    public double case() {
        return this.goto;
    }

    public int char() {
        return this.char ? 1 : this.if ? 2 : 3;
    }

    public float do() {
        return this.b;
    }

    public void do(boolean z) {
        this.f = z;
    }

    public long else() {
        return this.h;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof aq)) {
            return false;
        }
        aq aqVar = (aq) obj;
        return this.a != aqVar.a ? false : this.goto != aqVar.goto ? false : this.try != aqVar.try ? false : this.c != aqVar.c ? false : this.new == aqVar.new;
    }

    public boolean for() {
        return this.char;
    }

    public String getGeofenceId() {
        return this.do;
    }

    public long goto() {
        return this.long;
    }

    public void if(boolean z) {
        this.char = z;
    }

    public boolean if() {
        return this.if;
    }

    public String int() {
        return this.new;
    }

    public int new() {
        return this.a;
    }

    public String toString() {
        return String.format("Geofence[Type:%s, Name:%s, latitude:%.6f, longitude:%.6f, radius:%.0f, expriation:%d, coordType:%s, fenceType:%d]", new Object[]{if(this.c), this.do, Double.valueOf(this.goto), Double.valueOf(this.try), Float.valueOf(this.b), Long.valueOf(this.long), this.new, Integer.valueOf(char())});
    }

    protected int try() {
        return this.g;
    }
}
