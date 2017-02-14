package com.tencent.mm.sdk.b;

import android.util.Log;

import com.tencent.mm.sdk.b.a.a;

final class b implements a {
    b() {
    }

    public final void e(String str, String str2) {
        if (a.level <= 2) {
            Log.i(str, str2);
        }
    }

    public final void f(String str, String str2) {
        if (a.level <= 1) {
            Log.d(str, str2);
        }
    }

    public final void g(String str, String str2) {
        if (a.level <= 3) {
            Log.w(str, str2);
        }
    }

    public final int h() {
        return a.level;
    }

    public final void h(String str, String str2) {
        if (a.level <= 4) {
            Log.e(str, str2);
        }
    }
}
