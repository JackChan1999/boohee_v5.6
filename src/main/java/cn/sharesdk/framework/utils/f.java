package cn.sharesdk.framework.utils;

final class f extends ThreadLocal<char[]> {
    f() {
    }

    protected char[] a() {
        return new char[1024];
    }

    protected /* synthetic */ Object initialValue() {
        return a();
    }
}
