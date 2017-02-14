package com.mob.tools;

import android.os.Handler.Callback;
import android.os.Message;

class FakeActivity$3 implements Callback {
    final /* synthetic */ FakeActivity this$0;
    final /* synthetic */ Runnable     val$r;

    FakeActivity$3(FakeActivity fakeActivity, Runnable runnable) {
        this.this$0 = fakeActivity;
        this.val$r = runnable;
    }

    public boolean handleMessage(Message message) {
        this.val$r.run();
        return false;
    }
}
