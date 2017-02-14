package com.zxinsight;

class aj implements s {
    final /* synthetic */ WebViewActivity a;

    aj(WebViewActivity webViewActivity) {
        this.a = webViewActivity;
    }

    public void a(boolean z) {
        if (z) {
            if (!this.a.c.isShown()) {
                this.a.c.setAnimation(WebViewActivity.a(1.0f, 0.0f));
                this.a.c.setVisibility(0);
            }
        } else if (this.a.c.isShown()) {
            this.a.c.setAnimation(WebViewActivity.a(0.0f, 1.0f));
            this.a.c.setVisibility(8);
        }
    }
}
