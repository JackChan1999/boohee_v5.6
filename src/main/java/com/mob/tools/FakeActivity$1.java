package com.mob.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler.Callback;
import android.os.Message;

class FakeActivity$1 implements Callback {
    final /* synthetic */ FakeActivity this$0;

    FakeActivity$1(FakeActivity fakeActivity) {
        this.this$0 = fakeActivity;
    }

    public boolean handleMessage(Message message) {
        Object[] objArr = (Object[]) message.obj;
        Context context = (Context) objArr[0];
        Intent intent = (Intent) objArr[1];
        if (!(context instanceof Activity)) {
            intent.addFlags(268435456);
        }
        context.startActivity(intent);
        return false;
    }
}
