package rx.internal.util;

import java.util.concurrent.atomic.AtomicLong;
import rx.Producer;
import rx.annotations.Experimental;

@Experimental
public final class BackpressureDrainManager extends AtomicLong implements Producer {
    protected final BackpressureQueueCallback actual;
    protected boolean emitting;
    protected Throwable exception;
    protected volatile boolean terminated;

    public interface BackpressureQueueCallback {
        boolean accept(Object obj);

        void complete(Throwable th);

        Object peek();

        Object poll();
    }

    public BackpressureDrainManager(BackpressureQueueCallback actual) {
        this.actual = actual;
    }

    public final boolean isTerminated() {
        return this.terminated;
    }

    public final void terminate() {
        this.terminated = true;
    }

    public final void terminate(Throwable error) {
        if (!this.terminated) {
            this.exception = error;
            this.terminated = true;
        }
    }

    public final void terminateAndDrain() {
        this.terminated = true;
        drain();
    }

    public final void terminateAndDrain(Throwable error) {
        if (!this.terminated) {
            this.exception = error;
            this.terminated = true;
            drain();
        }
    }

    public final void request(long n) {
        if (n != 0) {
            boolean mayDrain;
            long r;
            long u;
            do {
                r = get();
                mayDrain = r == 0;
                if (r == Long.MAX_VALUE) {
                    break;
                } else if (n == Long.MAX_VALUE) {
                    u = n;
                    mayDrain = true;
                } else if (r > Long.MAX_VALUE - n) {
                    u = Long.MAX_VALUE;
                } else {
                    u = r + n;
                }
            } while (!compareAndSet(r, u));
            if (mayDrain) {
                drain();
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void drain() {
        /*
        r14 = this;
        monitor-enter(r14);
        r9 = r14.emitting;	 Catch:{ all -> 0x0036 }
        if (r9 == 0) goto L_0x0007;
    L_0x0005:
        monitor-exit(r14);	 Catch:{ all -> 0x0036 }
    L_0x0006:
        return;
    L_0x0007:
        r9 = 1;
        r14.emitting = r9;	 Catch:{ all -> 0x0036 }
        r8 = r14.terminated;	 Catch:{ all -> 0x0036 }
        monitor-exit(r14);	 Catch:{ all -> 0x0036 }
        r4 = r14.get();
        r7 = 0;
        r0 = r14.actual;	 Catch:{ all -> 0x0094 }
    L_0x0014:
        r2 = 0;
    L_0x0015:
        r10 = 0;
        r9 = (r4 > r10 ? 1 : (r4 == r10 ? 0 : -1));
        if (r9 > 0) goto L_0x001d;
    L_0x001b:
        if (r8 == 0) goto L_0x003f;
    L_0x001d:
        if (r8 == 0) goto L_0x006a;
    L_0x001f:
        r6 = r0.peek();	 Catch:{ all -> 0x0094 }
        if (r6 != 0) goto L_0x0039;
    L_0x0025:
        r7 = 1;
        r1 = r14.exception;	 Catch:{ all -> 0x0094 }
        r0.complete(r1);	 Catch:{ all -> 0x0094 }
        if (r7 != 0) goto L_0x0006;
    L_0x002d:
        monitor-enter(r14);
        r9 = 0;
        r14.emitting = r9;	 Catch:{ all -> 0x0033 }
        monitor-exit(r14);	 Catch:{ all -> 0x0033 }
        goto L_0x0006;
    L_0x0033:
        r9 = move-exception;
        monitor-exit(r14);	 Catch:{ all -> 0x0033 }
        throw r9;
    L_0x0036:
        r9 = move-exception;
        monitor-exit(r14);	 Catch:{ all -> 0x0036 }
        throw r9;
    L_0x0039:
        r10 = 0;
        r9 = (r4 > r10 ? 1 : (r4 == r10 ? 0 : -1));
        if (r9 != 0) goto L_0x006a;
    L_0x003f:
        monitor-enter(r14);	 Catch:{ all -> 0x0094 }
        r8 = r14.terminated;	 Catch:{ all -> 0x0091 }
        r9 = r0.peek();	 Catch:{ all -> 0x0091 }
        if (r9 == 0) goto L_0x0088;
    L_0x0048:
        r3 = 1;
    L_0x0049:
        r10 = r14.get();	 Catch:{ all -> 0x0091 }
        r12 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
        r9 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1));
        if (r9 != 0) goto L_0x009d;
    L_0x0056:
        if (r3 != 0) goto L_0x008a;
    L_0x0058:
        if (r8 != 0) goto L_0x008a;
    L_0x005a:
        r7 = 1;
        r9 = 0;
        r14.emitting = r9;	 Catch:{ all -> 0x0091 }
        monitor-exit(r14);	 Catch:{ all -> 0x0091 }
        if (r7 != 0) goto L_0x0006;
    L_0x0061:
        monitor-enter(r14);
        r9 = 0;
        r14.emitting = r9;	 Catch:{ all -> 0x0067 }
        monitor-exit(r14);	 Catch:{ all -> 0x0067 }
        goto L_0x0006;
    L_0x0067:
        r9 = move-exception;
        monitor-exit(r14);	 Catch:{ all -> 0x0067 }
        throw r9;
    L_0x006a:
        r6 = r0.poll();	 Catch:{ all -> 0x0094 }
        if (r6 == 0) goto L_0x003f;
    L_0x0070:
        r9 = r0.accept(r6);	 Catch:{ all -> 0x0094 }
        if (r9 == 0) goto L_0x0082;
    L_0x0076:
        r7 = 1;
        if (r7 != 0) goto L_0x0006;
    L_0x0079:
        monitor-enter(r14);
        r9 = 0;
        r14.emitting = r9;	 Catch:{ all -> 0x007f }
        monitor-exit(r14);	 Catch:{ all -> 0x007f }
        goto L_0x0006;
    L_0x007f:
        r9 = move-exception;
        monitor-exit(r14);	 Catch:{ all -> 0x007f }
        throw r9;
    L_0x0082:
        r10 = 1;
        r4 = r4 - r10;
        r2 = r2 + 1;
        goto L_0x0015;
    L_0x0088:
        r3 = 0;
        goto L_0x0049;
    L_0x008a:
        r4 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
    L_0x008f:
        monitor-exit(r14);	 Catch:{ all -> 0x0091 }
        goto L_0x0014;
    L_0x0091:
        r9 = move-exception;
        monitor-exit(r14);	 Catch:{ all -> 0x0091 }
        throw r9;	 Catch:{ all -> 0x0094 }
    L_0x0094:
        r9 = move-exception;
        if (r7 != 0) goto L_0x009c;
    L_0x0097:
        monitor-enter(r14);
        r10 = 0;
        r14.emitting = r10;	 Catch:{ all -> 0x00c0 }
        monitor-exit(r14);	 Catch:{ all -> 0x00c0 }
    L_0x009c:
        throw r9;
    L_0x009d:
        r9 = -r2;
        r10 = (long) r9;
        r4 = r14.addAndGet(r10);	 Catch:{ all -> 0x0091 }
        r10 = 0;
        r9 = (r4 > r10 ? 1 : (r4 == r10 ? 0 : -1));
        if (r9 == 0) goto L_0x00ab;
    L_0x00a9:
        if (r3 != 0) goto L_0x008f;
    L_0x00ab:
        if (r8 == 0) goto L_0x00af;
    L_0x00ad:
        if (r3 == 0) goto L_0x008f;
    L_0x00af:
        r7 = 1;
        r9 = 0;
        r14.emitting = r9;	 Catch:{ all -> 0x0091 }
        monitor-exit(r14);	 Catch:{ all -> 0x0091 }
        if (r7 != 0) goto L_0x0006;
    L_0x00b6:
        monitor-enter(r14);
        r9 = 0;
        r14.emitting = r9;	 Catch:{ all -> 0x00bd }
        monitor-exit(r14);	 Catch:{ all -> 0x00bd }
        goto L_0x0006;
    L_0x00bd:
        r9 = move-exception;
        monitor-exit(r14);	 Catch:{ all -> 0x00bd }
        throw r9;
    L_0x00c0:
        r9 = move-exception;
        monitor-exit(r14);	 Catch:{ all -> 0x00c0 }
        throw r9;
        */
        throw new UnsupportedOperationException("Method not decompiled: rx.internal.util.BackpressureDrainManager.drain():void");
    }
}
