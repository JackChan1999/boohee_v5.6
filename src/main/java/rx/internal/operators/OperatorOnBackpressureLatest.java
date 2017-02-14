package rx.internal.operators;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import rx.Observable$Operator;
import rx.Observer;
import rx.Producer;
import rx.Subscriber;
import rx.Subscription;

public final class OperatorOnBackpressureLatest<T> implements Observable$Operator<T, T> {

    static final class Holder {
        static final OperatorOnBackpressureLatest<Object> INSTANCE = new OperatorOnBackpressureLatest();

        Holder() {
        }
    }

    static final class LatestEmitter<T> extends AtomicLong implements Producer, Subscription, Observer<T> {
        static final Object EMPTY = new Object();
        static final long NOT_REQUESTED = -4611686018427387904L;
        private static final long serialVersionUID = -1364393685005146274L;
        final Subscriber<? super T> child;
        volatile boolean done;
        boolean emitting;
        boolean missed;
        LatestSubscriber<? super T> parent;
        Throwable terminal;
        final AtomicReference<Object> value = new AtomicReference(EMPTY);

        public LatestEmitter(Subscriber<? super T> child) {
            this.child = child;
            lazySet(NOT_REQUESTED);
        }

        public void request(long n) {
            if (n >= 0) {
                long r;
                long u;
                do {
                    r = get();
                    if (r != Long.MIN_VALUE) {
                        if (r == NOT_REQUESTED) {
                            u = n;
                        } else {
                            u = r + n;
                            if (u < 0) {
                                u = Long.MAX_VALUE;
                            }
                        }
                    } else {
                        return;
                    }
                } while (!compareAndSet(r, u));
                if (r == NOT_REQUESTED) {
                    this.parent.requestMore(Long.MAX_VALUE);
                }
                emit();
            }
        }

        long produced(long n) {
            long u;
            long r;
            do {
                r = get();
                if (r < 0) {
                    return r;
                }
                u = r - n;
            } while (!compareAndSet(r, u));
            return u;
        }

        public boolean isUnsubscribed() {
            return get() == Long.MIN_VALUE;
        }

        public void unsubscribe() {
            if (get() >= 0) {
                getAndSet(Long.MIN_VALUE);
            }
        }

        public void onNext(T t) {
            this.value.lazySet(t);
            emit();
        }

        public void onError(Throwable e) {
            this.terminal = e;
            this.done = true;
            emit();
        }

        public void onCompleted() {
            this.done = true;
            emit();
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        void emit() {
            /*
            r8 = this;
            monitor-enter(r8);
            r6 = r8.emitting;	 Catch:{ all -> 0x0028 }
            if (r6 == 0) goto L_0x000a;
        L_0x0005:
            r6 = 1;
            r8.missed = r6;	 Catch:{ all -> 0x0028 }
            monitor-exit(r8);	 Catch:{ all -> 0x0028 }
        L_0x0009:
            return;
        L_0x000a:
            r6 = 1;
            r8.emitting = r6;	 Catch:{ all -> 0x0028 }
            r6 = 0;
            r8.missed = r6;	 Catch:{ all -> 0x0028 }
            monitor-exit(r8);	 Catch:{ all -> 0x0028 }
            r1 = 0;
        L_0x0012:
            r2 = r8.get();	 Catch:{ all -> 0x006e }
            r6 = -9223372036854775808;
            r6 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1));
            if (r6 != 0) goto L_0x002b;
        L_0x001c:
            r1 = 1;
        L_0x001d:
            if (r1 != 0) goto L_0x0009;
        L_0x001f:
            monitor-enter(r8);
            r6 = 0;
            r8.emitting = r6;	 Catch:{ all -> 0x0025 }
            monitor-exit(r8);	 Catch:{ all -> 0x0025 }
            goto L_0x0009;
        L_0x0025:
            r6 = move-exception;
            monitor-exit(r8);	 Catch:{ all -> 0x0025 }
            throw r6;
        L_0x0028:
            r6 = move-exception;
            monitor-exit(r8);	 Catch:{ all -> 0x0028 }
            throw r6;
        L_0x002b:
            r6 = r8.value;	 Catch:{ all -> 0x006e }
            r4 = r6.get();	 Catch:{ all -> 0x006e }
            r6 = 0;
            r6 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1));
            if (r6 <= 0) goto L_0x004f;
        L_0x0037:
            r6 = EMPTY;	 Catch:{ all -> 0x006e }
            if (r4 == r6) goto L_0x004f;
        L_0x003b:
            r5 = r4;
            r6 = r8.child;	 Catch:{ all -> 0x006e }
            r6.onNext(r5);	 Catch:{ all -> 0x006e }
            r6 = r8.value;	 Catch:{ all -> 0x006e }
            r7 = EMPTY;	 Catch:{ all -> 0x006e }
            r6.compareAndSet(r4, r7);	 Catch:{ all -> 0x006e }
            r6 = 1;
            r8.produced(r6);	 Catch:{ all -> 0x006e }
            r4 = EMPTY;	 Catch:{ all -> 0x006e }
        L_0x004f:
            r6 = EMPTY;	 Catch:{ all -> 0x006e }
            if (r4 != r6) goto L_0x0060;
        L_0x0053:
            r6 = r8.done;	 Catch:{ all -> 0x006e }
            if (r6 == 0) goto L_0x0060;
        L_0x0057:
            r0 = r8.terminal;	 Catch:{ all -> 0x006e }
            if (r0 == 0) goto L_0x0077;
        L_0x005b:
            r6 = r8.child;	 Catch:{ all -> 0x006e }
            r6.onError(r0);	 Catch:{ all -> 0x006e }
        L_0x0060:
            monitor-enter(r8);	 Catch:{ all -> 0x006e }
            r6 = r8.missed;	 Catch:{ all -> 0x006b }
            if (r6 != 0) goto L_0x007d;
        L_0x0065:
            r6 = 0;
            r8.emitting = r6;	 Catch:{ all -> 0x006b }
            r1 = 1;
            monitor-exit(r8);	 Catch:{ all -> 0x006b }
            goto L_0x001d;
        L_0x006b:
            r6 = move-exception;
            monitor-exit(r8);	 Catch:{ all -> 0x006b }
            throw r6;	 Catch:{ all -> 0x006e }
        L_0x006e:
            r6 = move-exception;
            if (r1 != 0) goto L_0x0076;
        L_0x0071:
            monitor-enter(r8);
            r7 = 0;
            r8.emitting = r7;	 Catch:{ all -> 0x0082 }
            monitor-exit(r8);	 Catch:{ all -> 0x0082 }
        L_0x0076:
            throw r6;
        L_0x0077:
            r6 = r8.child;	 Catch:{ all -> 0x006e }
            r6.onCompleted();	 Catch:{ all -> 0x006e }
            goto L_0x0060;
        L_0x007d:
            r6 = 0;
            r8.missed = r6;	 Catch:{ all -> 0x006b }
            monitor-exit(r8);	 Catch:{ all -> 0x006b }
            goto L_0x0012;
        L_0x0082:
            r6 = move-exception;
            monitor-exit(r8);	 Catch:{ all -> 0x0082 }
            throw r6;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorOnBackpressureLatest.LatestEmitter.emit():void");
        }
    }

    static final class LatestSubscriber<T> extends Subscriber<T> {
        private final LatestEmitter<T> producer;

        private LatestSubscriber(LatestEmitter<T> producer) {
            this.producer = producer;
        }

        public void onStart() {
            request(0);
        }

        public void onNext(T t) {
            this.producer.onNext(t);
        }

        public void onError(Throwable e) {
            this.producer.onError(e);
        }

        public void onCompleted() {
            this.producer.onCompleted();
        }

        void requestMore(long n) {
            request(n);
        }
    }

    public static <T> OperatorOnBackpressureLatest<T> instance() {
        return Holder.INSTANCE;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        LatestEmitter<T> producer = new LatestEmitter(child);
        LatestSubscriber<T> parent = new LatestSubscriber(producer);
        producer.parent = parent;
        child.add(parent);
        child.add(producer);
        child.setProducer(producer);
        return parent;
    }
}
