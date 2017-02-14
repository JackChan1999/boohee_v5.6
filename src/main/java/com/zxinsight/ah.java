package com.zxinsight;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

class ah implements OnTouchListener {
    final /* synthetic */ WebViewActivity a;

    ah(WebViewActivity webViewActivity) {
        this.a = webViewActivity;
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        return motionEvent.getAction() == 2;
    }
}
