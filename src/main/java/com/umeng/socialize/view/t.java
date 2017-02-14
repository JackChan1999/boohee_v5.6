package com.umeng.socialize.view;

import com.umeng.socialize.utils.Log;
import com.umeng.socialize.view.wigets.KeyboardListenRelativeLayout.IOnKeyboardStateChangedListener;

/* compiled from: ShareActivity */
class t implements IOnKeyboardStateChangedListener {
    final /* synthetic */ ShareActivity a;

    t(ShareActivity shareActivity) {
        this.a = shareActivity;
    }

    public void a(int i) {
        this.a.A = i;
        Log.d(ShareActivity.b, "onKeyboardStateChanged  now state is " + i);
    }
}
