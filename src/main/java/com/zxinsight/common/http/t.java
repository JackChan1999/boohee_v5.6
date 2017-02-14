package com.zxinsight.common.http;

import java.io.IOException;
import java.util.concurrent.Callable;

public abstract class t<V> implements Callable<V> {
    protected abstract void a();

    protected abstract V b();

    public V call() {
        Throwable th;
        Object obj = 1;
        try {
            V b = b();
            try {
                a();
                return b;
            } catch (IOException e) {
                throw new RestException(e);
            }
        } catch (RestException e2) {
            throw e2;
        } catch (IOException e3) {
            throw new RestException(e3);
        } catch (Throwable th2) {
            th = th2;
        }
        try {
            a();
        } catch (IOException e4) {
            if (obj == null) {
                throw new RestException(e4);
            }
        }
        throw th;
    }
}
