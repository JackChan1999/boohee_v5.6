package rx.observers;

import rx.Observer;
import rx.exceptions.Exceptions;
import rx.internal.operators.NotificationLite;

public class SerializedObserver<T> implements Observer<T> {
    private static final int MAX_DRAIN_ITERATION = 1024;
    private final Observer<? super T> actual;
    private boolean emitting;
    private final NotificationLite<T> nl = NotificationLite.instance();
    private FastList queue;
    private volatile boolean terminated;

    static final class FastList {
        Object[] array;
        int size;

        FastList() {
        }

        public void add(Object o) {
            int s = this.size;
            Object[] a = this.array;
            if (a == null) {
                a = new Object[16];
                this.array = a;
            } else if (s == a.length) {
                Object[] array2 = new Object[((s >> 2) + s)];
                System.arraycopy(a, 0, array2, 0, s);
                a = array2;
                this.array = a;
            }
            a[s] = o;
            this.size = s + 1;
        }
    }

    public SerializedObserver(Observer<? super T> s) {
        this.actual = s;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onNext(T r11) {
        /*
        r10 = this;
        r9 = 1;
        r7 = r10.terminated;
        if (r7 == 0) goto L_0x0006;
    L_0x0005:
        return;
    L_0x0006:
        monitor-enter(r10);
        r7 = r10.terminated;	 Catch:{ all -> 0x000d }
        if (r7 == 0) goto L_0x0010;
    L_0x000b:
        monitor-exit(r10);	 Catch:{ all -> 0x000d }
        goto L_0x0005;
    L_0x000d:
        r7 = move-exception;
        monitor-exit(r10);	 Catch:{ all -> 0x000d }
        throw r7;
    L_0x0010:
        r7 = r10.emitting;	 Catch:{ all -> 0x000d }
        if (r7 == 0) goto L_0x002a;
    L_0x0014:
        r5 = r10.queue;	 Catch:{ all -> 0x000d }
        if (r5 != 0) goto L_0x001f;
    L_0x0018:
        r5 = new rx.observers.SerializedObserver$FastList;	 Catch:{ all -> 0x000d }
        r5.<init>();	 Catch:{ all -> 0x000d }
        r10.queue = r5;	 Catch:{ all -> 0x000d }
    L_0x001f:
        r7 = r10.nl;	 Catch:{ all -> 0x000d }
        r7 = r7.next(r11);	 Catch:{ all -> 0x000d }
        r5.add(r7);	 Catch:{ all -> 0x000d }
        monitor-exit(r10);	 Catch:{ all -> 0x000d }
        goto L_0x0005;
    L_0x002a:
        r7 = 1;
        r10.emitting = r7;	 Catch:{ all -> 0x000d }
        monitor-exit(r10);	 Catch:{ all -> 0x000d }
        r7 = r10.actual;	 Catch:{ Throwable -> 0x0045 }
        r7.onNext(r11);	 Catch:{ Throwable -> 0x0045 }
    L_0x0033:
        r2 = 0;
    L_0x0034:
        r7 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        if (r2 >= r7) goto L_0x0033;
    L_0x0038:
        monitor-enter(r10);
        r5 = r10.queue;	 Catch:{ all -> 0x0042 }
        if (r5 != 0) goto L_0x0055;
    L_0x003d:
        r7 = 0;
        r10.emitting = r7;	 Catch:{ all -> 0x0042 }
        monitor-exit(r10);	 Catch:{ all -> 0x0042 }
        goto L_0x0005;
    L_0x0042:
        r7 = move-exception;
        monitor-exit(r10);	 Catch:{ all -> 0x0042 }
        throw r7;
    L_0x0045:
        r1 = move-exception;
        r10.terminated = r9;
        rx.exceptions.Exceptions.throwIfFatal(r1);
        r7 = r10.actual;
        r8 = rx.exceptions.OnErrorThrowable.addValueAsLastCause(r1, r11);
        r7.onError(r8);
        goto L_0x0005;
    L_0x0055:
        r7 = 0;
        r10.queue = r7;	 Catch:{ all -> 0x0042 }
        monitor-exit(r10);	 Catch:{ all -> 0x0042 }
        r0 = r5.array;
        r4 = r0.length;
        r3 = 0;
    L_0x005d:
        if (r3 >= r4) goto L_0x0063;
    L_0x005f:
        r6 = r0[r3];
        if (r6 != 0) goto L_0x0066;
    L_0x0063:
        r2 = r2 + 1;
        goto L_0x0034;
    L_0x0066:
        r7 = r10.nl;	 Catch:{ Throwable -> 0x0074 }
        r8 = r10.actual;	 Catch:{ Throwable -> 0x0074 }
        r7 = r7.accept(r8, r6);	 Catch:{ Throwable -> 0x0074 }
        if (r7 == 0) goto L_0x0084;
    L_0x0070:
        r7 = 1;
        r10.terminated = r7;	 Catch:{ Throwable -> 0x0074 }
        goto L_0x0005;
    L_0x0074:
        r1 = move-exception;
        r10.terminated = r9;
        rx.exceptions.Exceptions.throwIfFatal(r1);
        r7 = r10.actual;
        r8 = rx.exceptions.OnErrorThrowable.addValueAsLastCause(r1, r11);
        r7.onError(r8);
        goto L_0x0005;
    L_0x0084:
        r3 = r3 + 1;
        goto L_0x005d;
        */
        throw new UnsupportedOperationException("Method not decompiled: rx.observers.SerializedObserver.onNext(java.lang.Object):void");
    }

    public void onError(Throwable e) {
        Exceptions.throwIfFatal(e);
        if (!this.terminated) {
            synchronized (this) {
                if (this.terminated) {
                    return;
                }
                this.terminated = true;
                if (this.emitting) {
                    FastList list = this.queue;
                    if (list == null) {
                        list = new FastList();
                        this.queue = list;
                    }
                    list.add(this.nl.error(e));
                    return;
                }
                this.emitting = true;
                this.actual.onError(e);
            }
        }
    }

    public void onCompleted() {
        if (!this.terminated) {
            synchronized (this) {
                if (this.terminated) {
                    return;
                }
                this.terminated = true;
                if (this.emitting) {
                    FastList list = this.queue;
                    if (list == null) {
                        list = new FastList();
                        this.queue = list;
                    }
                    list.add(this.nl.completed());
                    return;
                }
                this.emitting = true;
                this.actual.onCompleted();
            }
        }
    }
}
