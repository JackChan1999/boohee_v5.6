package com.meiqia.core;

import android.content.Context;
import android.content.Intent;

import com.meiqia.core.b.e;
import com.meiqia.core.b.h;
import com.meiqia.core.b.j;
import com.meiqia.core.bean.MQMessage;

import java.util.ArrayList;
import java.util.List;

class a {
    private static a                a;
    private final  h                b;
    private final  bc               c;
    private final  MQMessageManager d;
    private        Context          e;
    private List<String> f = new ArrayList();

    private a(Context context) {
        this.e = context;
        this.b = new h(context);
        this.c = new bc(context);
        this.d = MQMessageManager.getInstance(context);
    }

    public static a a(Context context) {
        if (a == null) {
            synchronized (a.class) {
                if (a == null) {
                    a = new a(context.getApplicationContext());
                }
            }
        }
        return a;
    }

    private void b(MQMessage mQMessage) {
        this.c.a(mQMessage);
        this.b.a(mQMessage.getCreated_on());
        this.b.b(mQMessage.getId());
    }

    private boolean c(MQMessage mQMessage) {
        return (mQMessage == null || this.c.b(mQMessage.getId()) != null || d(mQMessage)) ? false
                : true;
    }

    private boolean d(MQMessage mQMessage) {
        String valueOf = String.valueOf(mQMessage.getId());
        if (this.f.contains(valueOf)) {
            return true;
        }
        this.f.add(valueOf);
        if (this.f.size() > 5) {
            this.f.remove(this.f.size() - 1);
        }
        return false;
    }

    private void e(MQMessage mQMessage) {
        this.d.addMQMessage(mQMessage);
        Intent intent = new Intent("new_msg_received_action");
        intent.putExtra("msgId", String.valueOf(mQMessage.getId()));
        j.a(this.e, intent);
        e.b("newMsg received : type = " + mQMessage.getContent_type() + "  content = " +
                mQMessage.getContent());
    }

    public void a(MQMessage mQMessage) {
        if (c(mQMessage)) {
            b(mQMessage);
            e(mQMessage);
        }
    }
}
