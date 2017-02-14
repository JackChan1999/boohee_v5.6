package com.zxinsight.common.http;

import android.graphics.Bitmap;

import com.zxinsight.common.util.i;

class e implements k {
    final /* synthetic */ d a;
    private final i<String, Bitmap> b = new i(20);

    e(d dVar) {
        this.a = dVar;
    }

    public Bitmap a(String str) {
        return (Bitmap) this.b.a((Object) str);
    }

    public void a(String str, Bitmap bitmap) {
        this.b.b(str, bitmap);
    }
}
