package cn.sharesdk.tencent.qzone;

import android.webkit.WebView;
import com.mob.tools.SSDKWebViewClient;

class l extends SSDKWebViewClient {
    final /* synthetic */ i a;

    l(i iVar) {
        this.a = iVar;
    }

    public boolean shouldOverrideUrlLoading(WebView webView, String str) {
        if (str != null && str.startsWith(this.a.f)) {
            this.a.b(str);
        } else if (str != null && str.startsWith("mqzone://")) {
            this.a.c(str);
        }
        return super.shouldOverrideUrlLoading(webView, str);
    }
}
