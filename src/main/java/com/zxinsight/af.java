package com.zxinsight;

class af implements Runnable {
    final /* synthetic */ String          a;
    final /* synthetic */ WebViewActivity b;

    af(WebViewActivity webViewActivity, String str) {
        this.b = webViewActivity;
        this.a = str;
    }

    public void run() {
        if (this.b.b != null) {
            this.b.b.loadUrl(this.a);
        }
    }
}
