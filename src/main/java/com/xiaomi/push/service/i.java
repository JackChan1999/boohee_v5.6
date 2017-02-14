package com.xiaomi.push.service;

import android.content.Context;
import android.text.TextUtils;

import com.xiaomi.channel.commonutils.string.d;

import java.util.ArrayList;
import java.util.List;

public class i {
    private static i a = null;
    private Context b;
    private List<String> c = new ArrayList();

    private i(Context context) {
        int i = 0;
        this.b = context.getApplicationContext();
        if (this.b == null) {
            this.b = context;
        }
        String[] split = this.b.getSharedPreferences("mipush_app_info", 0).getString
                ("unregistered_pkg_names", "").split(",");
        int length = split.length;
        while (i < length) {
            CharSequence charSequence = split[i];
            if (TextUtils.isEmpty(charSequence)) {
                this.c.add(charSequence);
            }
            i++;
        }
    }

    public static i a(Context context) {
        if (a == null) {
            a = new i(context);
        }
        return a;
    }

    public boolean a(String str) {
        boolean contains;
        synchronized (this.c) {
            contains = this.c.contains(str);
        }
        return contains;
    }

    public void b(String str) {
        synchronized (this.c) {
            if (!this.c.contains(str)) {
                this.c.add(str);
                this.b.getSharedPreferences("mipush_app_info", 0).edit().putString
                        ("unregistered_pkg_names", d.a(this.c, ",")).commit();
            }
        }
    }

    public void c(String str) {
        synchronized (this.c) {
            if (this.c.contains(str)) {
                this.c.remove(str);
                this.b.getSharedPreferences("mipush_app_info", 0).edit().putString
                        ("unregistered_pkg_names", d.a(this.c, ",")).commit();
            }
        }
    }
}
