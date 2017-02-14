package com.xiaomi.channel.commonutils.logger;

import android.util.Log;

import com.boohee.utils.MiBandHelper;

public class a implements LoggerInterface {
    private String a = MiBandHelper.KEY_DATA_SOURCE;

    public void log(String str) {
        Log.v(this.a, str);
    }

    public void log(String str, Throwable th) {
        Log.v(this.a, str, th);
    }

    public void setTag(String str) {
        this.a = str;
    }
}
