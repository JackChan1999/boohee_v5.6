package rx.internal.producers;

import java.util.List;
import rx.Observer;
import rx.Producer;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.internal.operators.BackpressureUtils;

public final class ProducerObserverArbiter<T> implements Producer, Observer<T> {
    static final Producer NULL_PRODUCER = new Producer() {
        public void request(long n) {
        }
    };
    final Subscriber<? super T> child;
    Producer currentProducer;
    boolean emitting;
    volatile boolean hasError;
    Producer missedProducer;
    long missedRequested;
    Object missedTerminal;
    List<T> queue;
    long requested;

    public ProducerObserverArbiter(Subscriber<? super T> child) {
        this.child = child;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onNext(T r7) {
        /*
        r6 = this;
        monitor-enter(r6);
        r4 = r6.emitting;	 Catch:{ all -> 0x003d }
        if (r4 == 0) goto L_0x0016;
    L_0x0005:
        r0 = r6.queue;	 Catch:{ all -> 0x003d }
        if (r0 != 0) goto L_0x0011;
    L_0x0009:
        r0 = new java.util.ArrayList;	 Catch:{ all -> 0x003d }
        r4 = 4;
        r0.<init>(r4);	 Catch:{ all -> 0x003d }
        r6.queue = r0;	 Catch:{ all -> 0x003d }
    L_0x0011:
        r0.add(r7);	 Catch:{ all -> 0x003d }
        monitor-exit(r6);	 Catch:{ all -> 0x003d }
    L_0x0015:
        return;
    L_0x0016:
        monitor-exit(r6);	 Catch:{ all -> 0x003d }
        r1 = 0;
        r4 = r6.child;	 Catch:{ all -> 0x0040 }
        r4.onNext(r7);	 Catch:{ all -> 0x0040 }
        r2 = r6.requested;	 Catch:{ all -> 0x0040 }
        r4 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
        r4 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r4 == 0) goto L_0x002e;
    L_0x0028:
        r4 = 1;
        r4 = r2 - r4;
        r6.requested = r4;	 Catch:{ all -> 0x0040 }
    L_0x002e:
        r6.emitLoop();	 Catch:{ all -> 0x0040 }
        r1 = 1;
        if (r1 != 0) goto L_0x0015;
    L_0x0034:
        monitor-enter(r6);
        r4 = 0;
        r6.emitting = r4;	 Catch:{ all -> 0x003a }
        monitor-exit(r6);	 Catch:{ all -> 0x003a }
        goto L_0x0015;
    L_0x003a:
        r4 = move-exception;
        monitor-exit(r6);	 Catch:{ all -> 0x003a }
        throw r4;
    L_0x003d:
        r4 = move-exception;
        monitor-exit(r6);	 Catch:{ all -> 0x003d }
        throw r4;
    L_0x0040:
        r4 = move-exception;
        if (r1 != 0) goto L_0x0048;
    L_0x0043:
        monitor-enter(r6);
        r5 = 0;
        r6.emitting = r5;	 Catch:{ all -> 0x0049 }
        monitor-exit(r6);	 Catch:{ all -> 0x0049 }
    L_0x0048:
        throw r4;
    L_0x0049:
        r4 = move-exception;
        monitor-exit(r6);	 Catch:{ all -> 0x0049 }
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: rx.internal.producers.ProducerObserverArbiter.onNext(java.lang.Object):void");
    }

    public void onError(Throwable e) {
        boolean emit;
        synchronized (this) {
            if (this.emitting) {
                this.missedTerminal = e;
                emit = false;
            } else {
                this.emitting = true;
                emit = true;
            }
        }
        if (emit) {
            this.child.onError(e);
        } else {
            this.hasError = true;
        }
    }

    public void onCompleted() {
        synchronized (this) {
            if (this.emitting) {
                this.missedTerminal = Boolean.valueOf(true);
                return;
            }
            this.emitting = true;
            this.child.onCompleted();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void request(long r12) {
        /*
        r11 = this;
        r8 = 0;
        r6 = (r12 > r8 ? 1 : (r12 == r8 ? 0 : -1));
        if (r6 >= 0) goto L_0x000f;
    L_0x0006:
        r6 = new java.lang.IllegalArgumentException;
        r7 = "n >= 0 required";
        r6.<init>(r7);
        throw r6;
    L_0x000f:
        r6 = (r12 > r8 ? 1 : (r12 == r8 ? 0 : -1));
        if (r6 != 0) goto L_0x0014;
    L_0x0013:
        return;
    L_0x0014:
        monitor-enter(r11);
        r6 = r11.emitting;	 Catch:{ all -> 0x0020 }
        if (r6 == 0) goto L_0x0023;
    L_0x0019:
        r6 = r11.missedRequested;	 Catch:{ all -> 0x0020 }
        r6 = r6 + r12;
        r11.missedRequested = r6;	 Catch:{ all -> 0x0020 }
        monitor-exit(r11);	 Catch:{ all -> 0x0020 }
        goto L_0x0013;
    L_0x0020:
        r6 = move-exception;
        monitor-exit(r11);	 Catch:{ all -> 0x0020 }
        throw r6;
    L_0x0023:
        r6 = 1;
        r11.emitting = r6;	 Catch:{ all -> 0x0020 }
        monitor-exit(r11);	 Catch:{ all -> 0x0020 }
        r0 = r11.currentProducer;
        r1 = 0;
        r2 = r11.requested;	 Catch:{ all -> 0x004d }
        r4 = r2 + r12;
        r6 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1));
        if (r6 >= 0) goto L_0x0037;
    L_0x0032:
        r4 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
    L_0x0037:
        r11.requested = r4;	 Catch:{ all -> 0x004d }
        r11.emitLoop();	 Catch:{ all -> 0x004d }
        r1 = 1;
        if (r1 != 0) goto L_0x0044;
    L_0x003f:
        monitor-enter(r11);
        r6 = 0;
        r11.emitting = r6;	 Catch:{ all -> 0x004a }
        monitor-exit(r11);	 Catch:{ all -> 0x004a }
    L_0x0044:
        if (r0 == 0) goto L_0x0013;
    L_0x0046:
        r0.request(r12);
        goto L_0x0013;
    L_0x004a:
        r6 = move-exception;
        monitor-exit(r11);	 Catch:{ all -> 0x004a }
        throw r6;
    L_0x004d:
        r6 = move-exception;
        if (r1 != 0) goto L_0x0055;
    L_0x0050:
        monitor-enter(r11);
        r7 = 0;
        r11.emitting = r7;	 Catch:{ all -> 0x0056 }
        monitor-exit(r11);	 Catch:{ all -> 0x0056 }
    L_0x0055:
        throw r6;
    L_0x0056:
        r6 = move-exception;
        monitor-exit(r11);	 Catch:{ all -> 0x0056 }
        throw r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: rx.internal.producers.ProducerObserverArbiter.request(long):void");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setProducer(rx.Producer r7) {
        /*
        r6 = this;
        monitor-enter(r6);
        r3 = r6.emitting;	 Catch:{ all -> 0x002e }
        if (r3 == 0) goto L_0x000e;
    L_0x0005:
        if (r7 == 0) goto L_0x000b;
    L_0x0007:
        r6.missedProducer = r7;	 Catch:{ all -> 0x002e }
        monitor-exit(r6);	 Catch:{ all -> 0x002e }
    L_0x000a:
        return;
    L_0x000b:
        r7 = NULL_PRODUCER;	 Catch:{ all -> 0x002e }
        goto L_0x0007;
    L_0x000e:
        r3 = 1;
        r6.emitting = r3;	 Catch:{ all -> 0x002e }
        monitor-exit(r6);	 Catch:{ all -> 0x002e }
        r2 = 0;
        r6.currentProducer = r7;
        r0 = r6.requested;
        r6.emitLoop();	 Catch:{ all -> 0x0034 }
        r2 = 1;
        if (r2 != 0) goto L_0x0022;
    L_0x001d:
        monitor-enter(r6);
        r3 = 0;
        r6.emitting = r3;	 Catch:{ all -> 0x0031 }
        monitor-exit(r6);	 Catch:{ all -> 0x0031 }
    L_0x0022:
        if (r7 == 0) goto L_0x000a;
    L_0x0024:
        r4 = 0;
        r3 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1));
        if (r3 == 0) goto L_0x000a;
    L_0x002a:
        r7.request(r0);
        goto L_0x000a;
    L_0x002e:
        r3 = move-exception;
        monitor-exit(r6);	 Catch:{ all -> 0x002e }
        throw r3;
    L_0x0031:
        r3 = move-exception;
        monitor-exit(r6);	 Catch:{ all -> 0x0031 }
        throw r3;
    L_0x0034:
        r3 = move-exception;
        if (r2 != 0) goto L_0x003c;
    L_0x0037:
        monitor-enter(r6);
        r4 = 0;
        r6.emitting = r4;	 Catch:{ all -> 0x003d }
        monitor-exit(r6);	 Catch:{ all -> 0x003d }
    L_0x003c:
        throw r3;
    L_0x003d:
        r3 = move-exception;
        monitor-exit(r6);	 Catch:{ all -> 0x003d }
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: rx.internal.producers.ProducerObserverArbiter.setProducer(rx.Producer):void");
    }

    void emitLoop() {
        Subscriber<? super T> c = this.child;
        long toRequest = 0;
        Producer requestFrom = null;
        while (true) {
            boolean quit = false;
            synchronized (this) {
                long localRequested = this.missedRequested;
                Producer localProducer = this.missedProducer;
                Boolean localTerminal = this.missedTerminal;
                List<T> q = this.queue;
                if (localRequested == 0 && localProducer == null && q == null && localTerminal == null) {
                    this.emitting = false;
                    quit = true;
                } else {
                    this.missedRequested = 0;
                    this.missedProducer = null;
                    this.queue = null;
                    this.missedTerminal = null;
                }
            }
            if (quit) {
                break;
            }
            boolean empty = q == null || q.isEmpty();
            if (localTerminal != null) {
                if (localTerminal != Boolean.TRUE) {
                    c.onError((Throwable) localTerminal);
                    return;
                } else if (empty) {
                    c.onCompleted();
                    return;
                }
            }
            long e = 0;
            if (q != null) {
                for (T v : q) {
                    if (!c.isUnsubscribed()) {
                        if (this.hasError) {
                            continue;
                            break;
                        }
                        try {
                            c.onNext(v);
                        } catch (Throwable ex) {
                            Exceptions.throwOrReport(ex, c, v);
                            return;
                        }
                    }
                    return;
                }
                e = 0 + ((long) q.size());
            }
            long r = this.requested;
            if (r != Long.MAX_VALUE) {
                long u;
                if (localRequested != 0) {
                    u = r + localRequested;
                    if (u < 0) {
                        u = Long.MAX_VALUE;
                    }
                    r = u;
                }
                if (!(e == 0 || r == Long.MAX_VALUE)) {
                    u = r - e;
                    if (u < 0) {
                        throw new IllegalStateException("More produced than requested");
                    }
                    r = u;
                }
                this.requested = r;
            }
            if (localProducer == null) {
                Producer p = this.currentProducer;
                if (p != null && localRequested != 0) {
                    toRequest = BackpressureUtils.addCap(toRequest, localRequested);
                    requestFrom = p;
                }
            } else if (localProducer == NULL_PRODUCER) {
                this.currentProducer = null;
            } else {
                this.currentProducer = localProducer;
                if (r != 0) {
                    toRequest = BackpressureUtils.addCap(toRequest, r);
                    requestFrom = localProducer;
                }
            }
        }
        if (toRequest != 0 && requestFrom != null) {
            requestFrom.request(toRequest);
        }
    }
}
