package com.tencent.a.a.a.a;

import android.content.Context;
import android.provider.Settings.System;
import android.util.Log;

public final class e extends f {
    public e(Context context) {
        super(context);
    }

    protected final boolean a() {
        return h.a(this.e, "android.permission.WRITE_SETTINGS");
    }

    protected final String b() {
        String string;
        synchronized (this) {
            Log.i("MID", "read mid from Settings.System");
            string = System.getString(this.e.getContentResolver(), h.f
                    ("4kU71lN96TJUomD1vOU9lgj9Tw=="));
        }
        return string;
    }

    protected final void b(String str) {
        synchronized (this) {
            Log.i("MID", "write mid to Settings.System");
            System.putString(this.e.getContentResolver(), h.f("4kU71lN96TJUomD1vOU9lgj9Tw=="), str);
        }
    }
}
