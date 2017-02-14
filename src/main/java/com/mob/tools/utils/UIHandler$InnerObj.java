package com.mob.tools.utils;

import android.os.Handler.Callback;
import android.os.Message;

final class UIHandler$InnerObj {
    public final Callback callback;
    public final Message  msg;

    public UIHandler$InnerObj(Message message, Callback callback) {
        this.msg = message;
        this.callback = callback;
    }
}
