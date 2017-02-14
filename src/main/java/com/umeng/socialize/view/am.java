package com.umeng.socialize.view;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/* compiled from: ShareBoard */
class am implements OnTouchListener {
    final /* synthetic */ View a;
    final /* synthetic */ ak   b;

    am(ak akVar, View view) {
        this.b = akVar;
        this.a = view;
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            this.a.setBackgroundColor(-3355444);
        } else if (motionEvent.getAction() == 1) {
            this.a.setBackgroundColor(-1);
        }
        return false;
    }
}
