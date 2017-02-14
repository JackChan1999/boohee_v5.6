package com.qiniu.android.utils;

import android.os.Handler;
import android.os.Looper;

public final class AsyncRun {
    public static void run(Runnable r) {
        new Handler(Looper.getMainLooper()).post(r);
    }
}
