package com.meiqia.core;

import android.os.Handler.Callback;
import android.os.Message;

import com.meiqia.core.b.e;

class bp implements Callback {
    final /* synthetic */ MeiQiaService a;

    bp(MeiQiaService meiQiaService) {
        this.a = meiQiaService;
    }

    public boolean handleMessage(Message message) {
        if (1 == message.what) {
            e.b("socket reconnect");
            this.a.k.set(false);
            this.a.a();
        }
        return false;
    }
}
