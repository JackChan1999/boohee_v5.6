package com.alipay.sdk.app;

public final class n {
    static String a;

    private static void a(String str) {
        a = str;
    }

    private static String c() {
        return a;
    }

    public static String a() {
        o a = o.a(o.CANCELED.a());
        return a(a.a(), a.b(), "");
    }

    private static String d() {
        o a = o.a(o.DOUBLE_REQUEST.a());
        return a(a.a(), a.b(), "");
    }

    public static String b() {
        o a = o.a(o.PARAMS_ERROR.a());
        return a(a.a(), a.b(), "");
    }

    public static String a(int i, String str, String str2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("resultStatus={").append(i).append("};memo={").append(str).append("};result={").append(str2).append("}");
        return stringBuilder.toString();
    }
}
