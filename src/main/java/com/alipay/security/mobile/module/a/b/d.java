package com.alipay.security.mobile.module.a.b;

import com.alipay.security.mobile.module.commonutils.CommonUtils;
import java.util.HashMap;
import java.util.Map;

public final class d {
    private String a;
    private String b;
    private String c;
    private String d;
    private String e;
    private String f;
    private String g;
    private String h;
    private Map<String, String> i;

    public final String a() {
        return CommonUtils.getNonNullString(this.a);
    }

    public final void a(String str) {
        this.a = str;
    }

    public final void a(Map<String, String> map) {
        this.i = map;
    }

    public final String b() {
        return CommonUtils.getNonNullString(this.b);
    }

    public final void b(String str) {
        this.c = str;
    }

    public final String c() {
        return CommonUtils.getNonNullString(this.c);
    }

    public final void c(String str) {
        this.d = str;
    }

    public final String d() {
        return CommonUtils.getNonNullString(this.d);
    }

    public final void d(String str) {
        this.e = str;
    }

    public final String e() {
        return CommonUtils.getNonNullString(this.e);
    }

    public final void e(String str) {
        this.f = str;
    }

    public final String f() {
        return CommonUtils.getNonNullString(this.f);
    }

    public final void f(String str) {
        this.g = str;
    }

    public final String g() {
        return CommonUtils.getNonNullString(this.g);
    }

    public final void g(String str) {
        this.h = str;
    }

    public final String h() {
        return CommonUtils.getNonNullString(this.h);
    }

    public final Map<String, String> i() {
        return this.i == null ? new HashMap() : this.i;
    }
}
