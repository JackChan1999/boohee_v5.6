package com.meiqia.core.b;

import android.util.Log;

import com.meiqia.core.MeiQiaService;

public class e {
    public static void a(String str) {
        if (MeiQiaService.a) {
            Log.e("meiqia_log", str);
        }
    }

    public static void b(String str) {
        if (MeiQiaService.a) {
            Log.d("meiqia_log", str);
        }
    }
}
