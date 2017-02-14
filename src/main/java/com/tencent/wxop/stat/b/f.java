package com.tencent.wxop.stat.b;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class f {
    ExecutorService cG;

    public f() {
        this.cG = null;
        this.cG = Executors.newSingleThreadExecutor();
    }

    public final void a(Runnable runnable) {
        this.cG.execute(runnable);
    }
}
