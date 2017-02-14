package com.baidu.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.tencent.connect.common.Constants;

class aw {
    private static aw a = null;
    private a do;
    private boolean for;
    private String if;

    public class a extends BroadcastReceiver {
        final /* synthetic */ aw a;

        public a(aw awVar) {
            this.a = awVar;
        }

        public void onReceive(Context context, Intent intent) {
            try {
                if (intent.getAction().equals("android.intent.action.BATTERY_CHANGED")) {
                    this.a.for = false;
                    int intExtra = intent.getIntExtra("status", 0);
                    int intExtra2 = intent.getIntExtra("plugged", 0);
                    switch (intExtra) {
                        case 2:
                            this.a.if = "4";
                            break;
                        case 3:
                        case 4:
                            this.a.if = "3";
                            break;
                        default:
                            this.a.if = null;
                            break;
                    }
                    switch (intExtra2) {
                        case 1:
                            this.a.if = Constants.VIA_SHARE_TYPE_INFO;
                            this.a.for = true;
                            break;
                        case 2:
                            this.a.if = "5";
                            this.a.for = true;
                            break;
                    }
                    if (this.a.for) {
                        ai.bA().bz();
                    } else {
                        ai.bA().bB();
                    }
                }
            } catch (Exception e) {
                this.a.if = null;
            }
        }
    }

    private aw() {
        this.for = false;
        this.if = null;
        this.do = null;
        this.do = new a(this);
    }

    public static aw do() {
        if (a == null) {
            a = new aw();
        }
        return a;
    }

    public String a() {
        return this.if;
    }

    public void for() {
        f.getServiceContext().registerReceiver(this.do, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
    }

    public void if() {
        if (this.do != null) {
            f.getServiceContext().unregisterReceiver(this.do);
        }
        this.do = null;
    }

    public boolean int() {
        return this.for;
    }
}
