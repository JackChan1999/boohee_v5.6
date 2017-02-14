package com.baidu.location;

import android.os.HandlerThread;

class ao {
    private static HandlerThread a = null;

    ao() {
    }

    static HandlerThread a() {
        if (a == null) {
            a = new HandlerThread("ServiceStartArguments", 10);
            a.start();
        }
        return a;
    }
}
