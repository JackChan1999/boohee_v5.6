package com.zxinsight;

import android.view.View;
import android.view.View.OnClickListener;

class ad implements OnClickListener {
    final /* synthetic */ WebViewActivity a;

    ad(WebViewActivity webViewActivity) {
        this.a = webViewActivity;
    }

    public void onClick(View view) {
        this.a.e.onBackPressed();
    }
}
