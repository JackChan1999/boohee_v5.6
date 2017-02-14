package com.tencent.wxop.stat;

import android.app.Activity;

public class EasyActivity extends Activity {
    protected void onPause() {
        super.onPause();
        e.m(this);
    }

    protected void onResume() {
        super.onResume();
        e.l(this);
    }
}
