package com.zxinsight;

import android.content.Intent;
import android.net.Uri;
import android.webkit.DownloadListener;

import com.zxinsight.common.util.c;

class p implements DownloadListener {
    final /* synthetic */ ProgressWebView a;

    p(ProgressWebView progressWebView) {
        this.a = progressWebView;
    }

    public void onDownloadStart(String str, String str2, String str3, String str4, long j) {
        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse(str));
            this.a.g.startActivity(intent);
        } catch (Exception e) {
            c.a(e.getLocalizedMessage());
        }
    }
}
