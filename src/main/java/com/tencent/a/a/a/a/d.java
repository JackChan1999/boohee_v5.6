package com.tencent.a.a.a.a;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

final class d extends f {
    public d(Context context) {
        super(context);
    }

    protected final boolean a() {
        return true;
    }

    protected final String b() {
        String string;
        synchronized (this) {
            Log.i("MID", "read mid from sharedPreferences");
            string = PreferenceManager.getDefaultSharedPreferences(this.e).getString(h.f
                    ("4kU71lN96TJUomD1vOU9lgj9Tw=="), null);
        }
        return string;
    }

    protected final void b(String str) {
        synchronized (this) {
            Log.i("MID", "write mid to sharedPreferences");
            Editor edit = PreferenceManager.getDefaultSharedPreferences(this.e).edit();
            edit.putString(h.f("4kU71lN96TJUomD1vOU9lgj9Tw=="), str);
            edit.commit();
        }
    }
}
