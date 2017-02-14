package com.zxinsight.common.http;

import android.support.v4.media.session.PlaybackStateCompat;

import com.zxinsight.common.util.i;

public class o implements a<String, byte[]> {
    private i<String, byte[]> a = new p(this, ((int) (Runtime.getRuntime().maxMemory() /
            PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID)) / 8);

    public byte[] a(String str) {
        return (byte[]) this.a.a((Object) str);
    }

    public void a(String str, byte[] bArr) {
        this.a.b(str, bArr);
    }
}
