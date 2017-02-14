package com.meiqia.core.a.a.e;

public class d extends g implements b {
    private String a = "*";

    public String a() {
        return this.a;
    }

    public void a(String str) {
        if (str == null) {
            throw new IllegalArgumentException("http resource descriptor must not be null");
        }
        this.a = str;
    }
}
