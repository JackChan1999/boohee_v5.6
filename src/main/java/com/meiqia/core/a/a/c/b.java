package com.meiqia.core.a.a.c;

public class b extends Exception {
    private int a;

    public b(int i) {
        this.a = i;
    }

    public b(int i, String str) {
        super(str);
        this.a = i;
    }

    public b(int i, Throwable th) {
        super(th);
        this.a = i;
    }

    public int a() {
        return this.a;
    }
}
