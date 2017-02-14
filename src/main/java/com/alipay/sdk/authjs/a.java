package com.alipay.sdk.authjs;

import org.json.JSONException;
import org.json.JSONObject;

public final class a {
    public static final String a = "CallInfo";
    public static final String b = "call";
    public static final String c = "callback";
    public static final String d = "bundleName";
    public static final String e = "clientId";
    public static final String f = "param";
    public static final String g = "func";
    public static final String h = "msgType";
    public String i;
    public String j;
    public String k;
    public String l;
    public JSONObject m;
    private boolean n = false;

    public enum a {
        NONE_ERROR,
        FUNCTION_NOT_FOUND,
        INVALID_PARAMETER,
        RUNTIME_ERROR,
        NONE_PERMISS
    }

    private static String a(a aVar) {
        switch (b.a[aVar.ordinal()]) {
            case 1:
                return "function not found";
            case 2:
                return "invalid parameter";
            case 3:
                return "runtime error";
            default:
                return "none";
        }
    }

    private boolean a() {
        return this.n;
    }

    private void a(boolean z) {
        this.n = z;
    }

    public a(String str) {
        this.l = str;
    }

    private String b() {
        return this.i;
    }

    private void a(String str) {
        this.i = str;
    }

    private String c() {
        return this.j;
    }

    private void b(String str) {
        this.j = str;
    }

    private String d() {
        return this.k;
    }

    private void c(String str) {
        this.k = str;
    }

    private String e() {
        return this.l;
    }

    private void d(String str) {
        this.l = str;
    }

    private JSONObject f() {
        return this.m;
    }

    private void a(JSONObject jSONObject) {
        this.m = jSONObject;
    }

    private String g() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("clientId", this.i);
        jSONObject.put(g, this.k);
        jSONObject.put("param", this.m);
        jSONObject.put(h, this.l);
        return jSONObject.toString();
    }
}
