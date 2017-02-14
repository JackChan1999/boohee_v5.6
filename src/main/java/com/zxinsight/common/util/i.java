package com.zxinsight.common.util;

import java.util.LinkedHashMap;

import lecho.lib.hellocharts.model.ColumnChartData;

public class i<K, V> {
    private final LinkedHashMap<K, V> a;
    private       int                 b;
    private       int                 c;
    private       int                 d;
    private       int                 e;
    private       int                 f;
    private       int                 g;
    private       int                 h;

    public i(int i) {
        if (i <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        this.c = i;
        this.a = new LinkedHashMap(0, ColumnChartData.DEFAULT_FILL_RATIO, true);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final V a(K r5) {
        /*
        r4 = this;
        if (r5 != 0) goto L_0x000b;
    L_0x0002:
        r0 = new java.lang.NullPointerException;
        r1 = "key == null";
        r0.<init>(r1);
        throw r0;
    L_0x000b:
        monitor-enter(r4);
        r0 = r4.a;	 Catch:{ all -> 0x002b }
        r0 = r0.get(r5);	 Catch:{ all -> 0x002b }
        if (r0 == 0) goto L_0x001c;
    L_0x0014:
        r1 = r4.g;	 Catch:{ all -> 0x002b }
        r1 = r1 + 1;
        r4.g = r1;	 Catch:{ all -> 0x002b }
        monitor-exit(r4);	 Catch:{ all -> 0x002b }
    L_0x001b:
        return r0;
    L_0x001c:
        r0 = r4.h;	 Catch:{ all -> 0x002b }
        r0 = r0 + 1;
        r4.h = r0;	 Catch:{ all -> 0x002b }
        monitor-exit(r4);	 Catch:{ all -> 0x002b }
        r1 = r4.b(r5);
        if (r1 != 0) goto L_0x002e;
    L_0x0029:
        r0 = 0;
        goto L_0x001b;
    L_0x002b:
        r0 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x002b }
        throw r0;
    L_0x002e:
        monitor-enter(r4);
        r0 = r4.e;	 Catch:{ all -> 0x0054 }
        r0 = r0 + 1;
        r4.e = r0;	 Catch:{ all -> 0x0054 }
        r0 = r4.a;	 Catch:{ all -> 0x0054 }
        r0 = r0.put(r5, r1);	 Catch:{ all -> 0x0054 }
        if (r0 == 0) goto L_0x004a;
    L_0x003d:
        r2 = r4.a;	 Catch:{ all -> 0x0054 }
        r2.put(r5, r0);	 Catch:{ all -> 0x0054 }
    L_0x0042:
        monitor-exit(r4);	 Catch:{ all -> 0x0054 }
        if (r0 == 0) goto L_0x0057;
    L_0x0045:
        r2 = 0;
        r4.a(r2, r5, r1, r0);
        goto L_0x001b;
    L_0x004a:
        r2 = r4.b;	 Catch:{ all -> 0x0054 }
        r3 = r4.c(r5, r1);	 Catch:{ all -> 0x0054 }
        r2 = r2 + r3;
        r4.b = r2;	 Catch:{ all -> 0x0054 }
        goto L_0x0042;
    L_0x0054:
        r0 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x0054 }
        throw r0;
    L_0x0057:
        r0 = r4.c;
        r4.a(r0);
        r0 = r1;
        goto L_0x001b;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zxinsight.common.util" +
                ".i.a(java.lang.Object):V");
    }

    public final V b(K k, V v) {
        if (k == null || v == null) {
            throw new NullPointerException("key == null || value == null");
        }
        V put;
        synchronized (this) {
            this.d++;
            this.b += c(k, v);
            put = this.a.put(k, v);
            if (put != null) {
                this.b -= c(k, put);
            }
        }
        if (put != null) {
            a(false, k, put, v);
        }
        a(this.c);
        return put;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void a(int r5) {
        /*
        r4 = this;
    L_0x0000:
        monitor-enter(r4);
        r0 = r4.b;	 Catch:{ all -> 0x0033 }
        if (r0 < 0) goto L_0x0011;
    L_0x0005:
        r0 = r4.a;	 Catch:{ all -> 0x0033 }
        r0 = r0.isEmpty();	 Catch:{ all -> 0x0033 }
        if (r0 == 0) goto L_0x0036;
    L_0x000d:
        r0 = r4.b;	 Catch:{ all -> 0x0033 }
        if (r0 == 0) goto L_0x0036;
    L_0x0011:
        r0 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x0033 }
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0033 }
        r1.<init>();	 Catch:{ all -> 0x0033 }
        r2 = r4.getClass();	 Catch:{ all -> 0x0033 }
        r2 = r2.getName();	 Catch:{ all -> 0x0033 }
        r1 = r1.append(r2);	 Catch:{ all -> 0x0033 }
        r2 = ".sizeOf() is reporting inconsistent results!";
        r1 = r1.append(r2);	 Catch:{ all -> 0x0033 }
        r1 = r1.toString();	 Catch:{ all -> 0x0033 }
        r0.<init>(r1);	 Catch:{ all -> 0x0033 }
        throw r0;	 Catch:{ all -> 0x0033 }
    L_0x0033:
        r0 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x0033 }
        throw r0;
    L_0x0036:
        r0 = r4.b;	 Catch:{ all -> 0x0033 }
        if (r0 <= r5) goto L_0x0042;
    L_0x003a:
        r0 = r4.a;	 Catch:{ all -> 0x0033 }
        r0 = r0.isEmpty();	 Catch:{ all -> 0x0033 }
        if (r0 == 0) goto L_0x0044;
    L_0x0042:
        monitor-exit(r4);	 Catch:{ all -> 0x0033 }
        return;
    L_0x0044:
        r0 = r4.a;	 Catch:{ all -> 0x0033 }
        r0 = r0.entrySet();	 Catch:{ all -> 0x0033 }
        r0 = r0.iterator();	 Catch:{ all -> 0x0033 }
        r0 = r0.next();	 Catch:{ all -> 0x0033 }
        r0 = (java.util.Map.Entry) r0;	 Catch:{ all -> 0x0033 }
        r1 = r0.getKey();	 Catch:{ all -> 0x0033 }
        r0 = r0.getValue();	 Catch:{ all -> 0x0033 }
        r2 = r4.a;	 Catch:{ all -> 0x0033 }
        r2.remove(r1);	 Catch:{ all -> 0x0033 }
        r2 = r4.b;	 Catch:{ all -> 0x0033 }
        r3 = r4.c(r1, r0);	 Catch:{ all -> 0x0033 }
        r2 = r2 - r3;
        r4.b = r2;	 Catch:{ all -> 0x0033 }
        r2 = r4.f;	 Catch:{ all -> 0x0033 }
        r2 = r2 + 1;
        r4.f = r2;	 Catch:{ all -> 0x0033 }
        monitor-exit(r4);	 Catch:{ all -> 0x0033 }
        r2 = 1;
        r3 = 0;
        r4.a(r2, r1, r0, r3);
        goto L_0x0000;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zxinsight.common.util" +
                ".i.a(int):void");
    }

    protected void a(boolean z, K k, V v, V v2) {
    }

    protected V b(K k) {
        return null;
    }

    private int c(K k, V v) {
        int a = a(k, v);
        if (a >= 0) {
            return a;
        }
        throw new IllegalStateException("Negative size: " + k + "=" + v);
    }

    protected int a(K k, V v) {
        return 1;
    }

    public final synchronized String toString() {
        String format;
        int i = 0;
        synchronized (this) {
            int i2 = this.g + this.h;
            if (i2 != 0) {
                i = (this.g * 100) / i2;
            }
            format = String.format("LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]", new Object[]{Integer.valueOf(this.c), Integer.valueOf(this.g), Integer.valueOf(this.h), Integer.valueOf(i)});
        }
        return format;
    }
}
