package com.umeng.socialize.view;

/* compiled from: ShareActivity */
class ai implements Runnable {
    final /* synthetic */ ShareActivity a;

    ai(ShareActivity shareActivity) {
        this.a = shareActivity;
    }

    public void run() {
        this.a.finish();
    }
}
