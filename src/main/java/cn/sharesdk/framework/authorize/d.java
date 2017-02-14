package cn.sharesdk.framework.authorize;

import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.LinearLayout.LayoutParams;

class d extends WebChromeClient {
    final /* synthetic */ int a;
    final /* synthetic */ RegisterView b;

    d(RegisterView registerView, int i) {
        this.b = registerView;
        this.a = i;
    }

    public void onProgressChanged(WebView webView, int i) {
        super.onProgressChanged(webView, i);
        LayoutParams layoutParams = (LayoutParams) this.b.d.getLayoutParams();
        layoutParams.width = (this.a * i) / 100;
        this.b.d.setLayoutParams(layoutParams);
        if (i <= 0 || i >= 100) {
            this.b.d.setVisibility(8);
        } else {
            this.b.d.setVisibility(0);
        }
    }
}
