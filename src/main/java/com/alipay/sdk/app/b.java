package com.alipay.sdk.app;

import android.content.Intent;
import android.net.Uri;
import android.webkit.DownloadListener;

final class b implements DownloadListener {
    final /* synthetic */ H5AuthActivity a;

    b(H5AuthActivity h5AuthActivity) {
        this.a = h5AuthActivity;
    }

    public final void onDownloadStart(String str, String str2, String str3, String str4, long j) {
        this.a.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
    }
}
