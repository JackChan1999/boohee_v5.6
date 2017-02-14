package com.zxinsight.common.http;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;

public abstract class b<V> extends t<V> {
    private final Closeable a;
    private final boolean   b;

    protected b(Closeable closeable, boolean z) {
        this.a = closeable;
        this.b = z;
    }

    protected void a() {
        if (this.a instanceof Flushable) {
            ((Flushable) this.a).flush();
        }
        if (this.b) {
            try {
                this.a.close();
                return;
            } catch (IOException e) {
                return;
            }
        }
        this.a.close();
    }
}
