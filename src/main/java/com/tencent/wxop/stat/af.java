package com.tencent.wxop.stat;

import android.content.Context;

import com.tencent.wxop.stat.b.l;

import java.util.Timer;
import java.util.TimerTask;

public class af {
    private static volatile af      dd = null;
    private                 Timer   dc = null;
    private                 Context h  = null;

    private af(Context context) {
        this.h = context.getApplicationContext();
        this.dc = new Timer(false);
    }

    public static af Y(Context context) {
        if (dd == null) {
            synchronized (af.class) {
                if (dd == null) {
                    dd = new af(context);
                }
            }
        }
        return dd;
    }

    public final void ah() {
        if (c.j() == d.PERIOD) {
            long u = (long) ((c.u() * 60) * 1000);
            if (c.k()) {
                l.av().b("setupPeriodTimer delay:" + u);
            }
            TimerTask agVar = new ag(this);
            if (this.dc != null) {
                if (c.k()) {
                    l.av().b("setupPeriodTimer schedule delay:" + u);
                }
                this.dc.schedule(agVar, u);
            } else if (c.k()) {
                l.av().c("setupPeriodTimer schedule timer == null");
            }
        }
    }
}
