package com.xiaomi.push.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Build.VERSION;
import android.provider.Settings.Global;
import android.provider.Settings.Secure;

import com.boohee.utils.MiBandHelper;

public class aa {
    private static aa      a;
    private        Context b;
    private int c = 0;

    private aa(Context context) {
        this.b = context.getApplicationContext();
    }

    public static aa a(Context context) {
        if (a == null) {
            a = new aa(context);
        }
        return a;
    }

    public boolean a() {
        return "@SHIP.TO.2A2FE0D7@".contains("xmsf") || "@SHIP.TO.2A2FE0D7@".contains
                (MiBandHelper.KEY_DATA_SOURCE) || "@SHIP.TO.2A2FE0D7@".contains("miui");
    }

    @SuppressLint({"NewApi"})
    public int b() {
        if (this.c != 0) {
            return this.c;
        }
        if (VERSION.SDK_INT >= 17) {
            this.c = Global.getInt(this.b.getContentResolver(), "device_provisioned", 0);
            return this.c;
        }
        this.c = Secure.getInt(this.b.getContentResolver(), "device_provisioned", 0);
        return this.c;
    }

    @SuppressLint({"NewApi"})
    public Uri c() {
        return VERSION.SDK_INT >= 17 ? Global.getUriFor("device_provisioned") : Secure.getUriFor
                ("device_provisioned");
    }
}
