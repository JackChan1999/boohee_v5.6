package rx.internal.operators;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import rx.Observable;
import rx.Observable$OnSubscribe;
import rx.Observer;
import rx.Producer;
import rx.Subscriber;
import rx.Subscription;
import rx.internal.util.LinkedArrayList;
import rx.subscriptions.SerialSubscription;

public final class CachedObservable<T> extends Observable<T> {
    private final CacheState<T> state;

    static final class CacheState<T> extends LinkedArrayList implements Observer<T> {
        static final ReplayProducer<?>[] EMPTY = new ReplayProducer[0];
        final SerialSubscription connection = new SerialSubscription();
        volatile boolean isConnected;
        final NotificationLite<T> nl = NotificationLite.instance();
        volatile ReplayProducer<?>[] producers = EMPTY;
        final Observable<? extends T> source;
        boolean sourceDone;

        public CacheState(Observable<? extends T> source, int capacityHint) {
            super(capacityHint);
            this.source = source;
        }

        public void addProducer(ReplayProducer<T> p) {
            synchronized (this.connection) {
                ReplayProducer<?>[] a = this.producers;
                int n = a.length;
                ReplayProducer<?>[] b = new ReplayProducer[(n + 1)];
                System.arraycopy(a, 0, b, 0, n);
                b[n] = p;
                this.producers = b;
            }
        }

        public void removeProducer(ReplayProducer<T> p) {
            synchronized (this.connection) {
                ReplayProducer<?>[] a = this.producers;
                int n = a.length;
                int j = -1;
                for (int i = 0; i < n; i++) {
                    if (a[i].equals(p)) {
                        j = i;
                        break;
                    }
                }
                if (j < 0) {
                } else if (n == 1) {
                    this.producers = EMPTY;
                } else {
                    ReplayProducer<?>[] b = new ReplayProducer[(n - 1)];
                    System.arraycopy(a, 0, b, 0, j);
                    System.arraycopy(a, j + 1, b, j, (n - j) - 1);
                    this.producers = b;
                }
            }
        }

        public void connect() {
            Subscriber<T> subscriber = new Subscriber<T>() {
                public void onNext(T t) {
                    CacheState.this.onNext(t);
                }

                public void onError(Throwable e) {
                    CacheState.this.onError(e);
                }

                public void onCompleted() {
                    CacheState.this.onCompleted();
                }
            };
            this.connection.set(subscriber);
            this.source.unsafeSubscribe(subscriber);
            this.isConnected = true;
        }

        public void onNext(T t) {
            if (!this.sourceDone) {
                add(this.nl.next(t));
                dispatch();
            }
        }

        public void onError(Throwable e) {
            if (!this.sourceDone) {
                this.sourceDone = true;
                add(this.nl.error(e));
                this.connection.unsubscribe();
                dispatch();
            }
        }

        public void onCompleted() {
            if (!this.sourceDone) {
                this.sourceDone = true;
                add(this.nl.completed());
                this.connection.unsubscribe();
                dispatch();
            }
        }

        void dispatch() {
            for (ReplayProducer<?> rp : this.producers) {
                rp.replay();
            }
        }
    }

    static final class CachedSubscribe<T> extends AtomicBoolean implements Observable$OnSubscribe<T> {
        private static final long serialVersionUID = -2817751667698696782L;
        final CacheState<T> state;

        public CachedSubscribe(CacheState<T> state) {
            this.state = state;
        }

        public void call(Subscriber<? super T> t) {
            ReplayProducer<T> rp = new ReplayProducer(t, this.state);
            this.state.addProducer(rp);
            t.add(rp);
            t.setProducer(rp);
            if (!get() && compareAndSet(false, true)) {
                this.state.connect();
            }
        }
    }

    static final class ReplayProducer<T> extends AtomicLong implements Producer, Subscription {
        private static final long serialVersionUID = -2557562030197141021L;
        final Subscriber<? super T> child;
        Object[] currentBuffer;
        int currentIndexInBuffer;
        boolean emitting;
        int index;
        boolean missed;
        final CacheState<T> state;

        public ReplayProducer(Subscriber<? super T> child, CacheState<T> state) {
            this.child = child;
            this.state = state;
        }

        public void request(long n) {
            long r;
            long u;
            do {
                r = get();
                if (r >= 0) {
                    u = r + n;
                    if (u < 0) {
                        u = Long.MAX_VALUE;
                    }
                } else {
                    return;
                }
            } while (!compareAndSet(r, u));
            replay();
        }

        public long produced(long n) {
            return addAndGet(-n);
        }

        public boolean isUnsubscribed() {
            return get() < 0;
        }

        public void unsubscribe() {
            if (get() >= 0 && getAndSet(-1) >= 0) {
                this.state.removeProducer(this);
            }
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void replay() {
            /*
            r20 = this;
            monitor-enter(r20);
            r0 = r20;
            r0 = r0.emitting;	 Catch:{ all -> 0x0046 }
            r17 = r0;
            if (r17 == 0) goto L_0x0013;
        L_0x0009:
            r17 = 1;
            r0 = r17;
            r1 = r20;
            r1.missed = r0;	 Catch:{ all -> 0x0046 }
            monitor-exit(r20);	 Catch:{ all -> 0x0046 }
        L_0x0012:
            return;
        L_0x0013:
            r17 = 1;
            r0 = r17;
            r1 = r20;
            r1.emitting = r0;	 Catch:{ all -> 0x0046 }
            monitor-exit(r20);	 Catch:{ all -> 0x0046 }
            r15 = 0;
            r0 = r20;
            r0 = r0.state;	 Catch:{ all -> 0x01b3 }
            r17 = r0;
            r0 = r17;
            r10 = r0.nl;	 Catch:{ all -> 0x01b3 }
            r0 = r20;
            r5 = r0.child;	 Catch:{ all -> 0x01b3 }
        L_0x002b:
            r12 = r20.get();	 Catch:{ all -> 0x01b3 }
            r18 = 0;
            r17 = (r12 > r18 ? 1 : (r12 == r18 ? 0 : -1));
            if (r17 >= 0) goto L_0x0049;
        L_0x0035:
            r15 = 1;
            if (r15 != 0) goto L_0x0012;
        L_0x0038:
            monitor-enter(r20);
            r17 = 0;
            r0 = r17;
            r1 = r20;
            r1.emitting = r0;	 Catch:{ all -> 0x0043 }
            monitor-exit(r20);	 Catch:{ all -> 0x0043 }
            goto L_0x0012;
        L_0x0043:
            r17 = move-exception;
            monitor-exit(r20);	 Catch:{ all -> 0x0043 }
            throw r17;
        L_0x0046:
            r17 = move-exception;
            monitor-exit(r20);	 Catch:{ all -> 0x0046 }
            throw r17;
        L_0x0049:
            r0 = r20;
            r0 = r0.state;	 Catch:{ all -> 0x01b3 }
            r17 = r0;
            r14 = r17.size();	 Catch:{ all -> 0x01b3 }
            if (r14 == 0) goto L_0x0181;
        L_0x0055:
            r0 = r20;
            r4 = r0.currentBuffer;	 Catch:{ all -> 0x01b3 }
            if (r4 != 0) goto L_0x0069;
        L_0x005b:
            r0 = r20;
            r0 = r0.state;	 Catch:{ all -> 0x01b3 }
            r17 = r0;
            r4 = r17.head();	 Catch:{ all -> 0x01b3 }
            r0 = r20;
            r0.currentBuffer = r4;	 Catch:{ all -> 0x01b3 }
        L_0x0069:
            r0 = r4.length;	 Catch:{ all -> 0x01b3 }
            r17 = r0;
            r9 = r17 + -1;
            r0 = r20;
            r7 = r0.index;	 Catch:{ all -> 0x01b3 }
            r0 = r20;
            r8 = r0.currentIndexInBuffer;	 Catch:{ all -> 0x01b3 }
            r18 = 0;
            r17 = (r12 > r18 ? 1 : (r12 == r18 ? 0 : -1));
            if (r17 != 0) goto L_0x00c0;
        L_0x007c:
            r11 = r4[r8];	 Catch:{ all -> 0x01b3 }
            r17 = r10.isCompleted(r11);	 Catch:{ all -> 0x01b3 }
            if (r17 == 0) goto L_0x009c;
        L_0x0084:
            r5.onCompleted();	 Catch:{ all -> 0x01b3 }
            r15 = 1;
            r20.unsubscribe();	 Catch:{ all -> 0x01b3 }
            if (r15 != 0) goto L_0x0012;
        L_0x008d:
            monitor-enter(r20);
            r17 = 0;
            r0 = r17;
            r1 = r20;
            r1.emitting = r0;	 Catch:{ all -> 0x0099 }
            monitor-exit(r20);	 Catch:{ all -> 0x0099 }
            goto L_0x0012;
        L_0x0099:
            r17 = move-exception;
            monitor-exit(r20);	 Catch:{ all -> 0x0099 }
            throw r17;
        L_0x009c:
            r17 = r10.isError(r11);	 Catch:{ all -> 0x01b3 }
            if (r17 == 0) goto L_0x0181;
        L_0x00a2:
            r17 = r10.getError(r11);	 Catch:{ all -> 0x01b3 }
            r0 = r17;
            r5.onError(r0);	 Catch:{ all -> 0x01b3 }
            r15 = 1;
            r20.unsubscribe();	 Catch:{ all -> 0x01b3 }
            if (r15 != 0) goto L_0x0012;
        L_0x00b1:
            monitor-enter(r20);
            r17 = 0;
            r0 = r17;
            r1 = r20;
            r1.emitting = r0;	 Catch:{ all -> 0x00bd }
            monitor-exit(r20);	 Catch:{ all -> 0x00bd }
            goto L_0x0012;
        L_0x00bd:
            r17 = move-exception;
            monitor-exit(r20);	 Catch:{ all -> 0x00bd }
            throw r17;
        L_0x00c0:
            r18 = 0;
            r17 = (r12 > r18 ? 1 : (r12 == r18 ? 0 : -1));
            if (r17 <= 0) goto L_0x0181;
        L_0x00c6:
            r16 = 0;
        L_0x00c8:
            if (r7 >= r14) goto L_0x0151;
        L_0x00ca:
            r18 = 0;
            r17 = (r12 > r18 ? 1 : (r12 == r18 ? 0 : -1));
            if (r17 <= 0) goto L_0x0151;
        L_0x00d0:
            r17 = r5.isUnsubscribed();	 Catch:{ all -> 0x01b3 }
            if (r17 == 0) goto L_0x00e8;
        L_0x00d6:
            r15 = 1;
            if (r15 != 0) goto L_0x0012;
        L_0x00d9:
            monitor-enter(r20);
            r17 = 0;
            r0 = r17;
            r1 = r20;
            r1.emitting = r0;	 Catch:{ all -> 0x00e5 }
            monitor-exit(r20);	 Catch:{ all -> 0x00e5 }
            goto L_0x0012;
        L_0x00e5:
            r17 = move-exception;
            monitor-exit(r20);	 Catch:{ all -> 0x00e5 }
            throw r17;
        L_0x00e8:
            if (r8 != r9) goto L_0x00f4;
        L_0x00ea:
            r17 = r4[r9];	 Catch:{ all -> 0x01b3 }
            r17 = (java.lang.Object[]) r17;	 Catch:{ all -> 0x01b3 }
            r0 = r17;
            r0 = (java.lang.Object[]) r0;	 Catch:{ all -> 0x01b3 }
            r4 = r0;
            r8 = 0;
        L_0x00f4:
            r11 = r4[r8];	 Catch:{ all -> 0x01b3 }
            r17 = r10.accept(r5, r11);	 Catch:{ Throwable -> 0x0111 }
            if (r17 == 0) goto L_0x0145;
        L_0x00fc:
            r15 = 1;
            r20.unsubscribe();	 Catch:{ Throwable -> 0x0111 }
            if (r15 != 0) goto L_0x0012;
        L_0x0102:
            monitor-enter(r20);
            r17 = 0;
            r0 = r17;
            r1 = r20;
            r1.emitting = r0;	 Catch:{ all -> 0x010e }
            monitor-exit(r20);	 Catch:{ all -> 0x010e }
            goto L_0x0012;
        L_0x010e:
            r17 = move-exception;
            monitor-exit(r20);	 Catch:{ all -> 0x010e }
            throw r17;
        L_0x0111:
            r6 = move-exception;
            rx.exceptions.Exceptions.throwIfFatal(r6);	 Catch:{ all -> 0x01b3 }
            r15 = 1;
            r20.unsubscribe();	 Catch:{ all -> 0x01b3 }
            r17 = r10.isError(r11);	 Catch:{ all -> 0x01b3 }
            if (r17 != 0) goto L_0x0134;
        L_0x011f:
            r17 = r10.isCompleted(r11);	 Catch:{ all -> 0x01b3 }
            if (r17 != 0) goto L_0x0134;
        L_0x0125:
            r17 = r10.getValue(r11);	 Catch:{ all -> 0x01b3 }
            r0 = r17;
            r17 = rx.exceptions.OnErrorThrowable.addValueAsLastCause(r6, r0);	 Catch:{ all -> 0x01b3 }
            r0 = r17;
            r5.onError(r0);	 Catch:{ all -> 0x01b3 }
        L_0x0134:
            if (r15 != 0) goto L_0x0012;
        L_0x0136:
            monitor-enter(r20);
            r17 = 0;
            r0 = r17;
            r1 = r20;
            r1.emitting = r0;	 Catch:{ all -> 0x0142 }
            monitor-exit(r20);	 Catch:{ all -> 0x0142 }
            goto L_0x0012;
        L_0x0142:
            r17 = move-exception;
            monitor-exit(r20);	 Catch:{ all -> 0x0142 }
            throw r17;
        L_0x0145:
            r8 = r8 + 1;
            r7 = r7 + 1;
            r18 = 1;
            r12 = r12 - r18;
            r16 = r16 + 1;
            goto L_0x00c8;
        L_0x0151:
            r17 = r5.isUnsubscribed();	 Catch:{ all -> 0x01b3 }
            if (r17 == 0) goto L_0x0169;
        L_0x0157:
            r15 = 1;
            if (r15 != 0) goto L_0x0012;
        L_0x015a:
            monitor-enter(r20);
            r17 = 0;
            r0 = r17;
            r1 = r20;
            r1.emitting = r0;	 Catch:{ all -> 0x0166 }
            monitor-exit(r20);	 Catch:{ all -> 0x0166 }
            goto L_0x0012;
        L_0x0166:
            r17 = move-exception;
            monitor-exit(r20);	 Catch:{ all -> 0x0166 }
            throw r17;
        L_0x0169:
            r0 = r20;
            r0.index = r7;	 Catch:{ all -> 0x01b3 }
            r0 = r20;
            r0.currentIndexInBuffer = r8;	 Catch:{ all -> 0x01b3 }
            r0 = r20;
            r0.currentBuffer = r4;	 Catch:{ all -> 0x01b3 }
            r0 = r16;
            r0 = (long) r0;	 Catch:{ all -> 0x01b3 }
            r18 = r0;
            r0 = r20;
            r1 = r18;
            r0.produced(r1);	 Catch:{ all -> 0x01b3 }
        L_0x0181:
            monitor-enter(r20);	 Catch:{ all -> 0x01b3 }
            r0 = r20;
            r0 = r0.missed;	 Catch:{ all -> 0x01b0 }
            r17 = r0;
            if (r17 != 0) goto L_0x01a5;
        L_0x018a:
            r17 = 0;
            r0 = r17;
            r1 = r20;
            r1.emitting = r0;	 Catch:{ all -> 0x01b0 }
            r15 = 1;
            monitor-exit(r20);	 Catch:{ all -> 0x01b0 }
            if (r15 != 0) goto L_0x0012;
        L_0x0196:
            monitor-enter(r20);
            r17 = 0;
            r0 = r17;
            r1 = r20;
            r1.emitting = r0;	 Catch:{ all -> 0x01a2 }
            monitor-exit(r20);	 Catch:{ all -> 0x01a2 }
            goto L_0x0012;
        L_0x01a2:
            r17 = move-exception;
            monitor-exit(r20);	 Catch:{ all -> 0x01a2 }
            throw r17;
        L_0x01a5:
            r17 = 0;
            r0 = r17;
            r1 = r20;
            r1.missed = r0;	 Catch:{ all -> 0x01b0 }
            monitor-exit(r20);	 Catch:{ all -> 0x01b0 }
            goto L_0x002b;
        L_0x01b0:
            r17 = move-exception;
            monitor-exit(r20);	 Catch:{ all -> 0x01b0 }
            throw r17;	 Catch:{ all -> 0x01b3 }
        L_0x01b3:
            r17 = move-exception;
            if (r15 != 0) goto L_0x01c0;
        L_0x01b6:
            monitor-enter(r20);
            r18 = 0;
            r0 = r18;
            r1 = r20;
            r1.emitting = r0;	 Catch:{ all -> 0x01c1 }
            monitor-exit(r20);	 Catch:{ all -> 0x01c1 }
        L_0x01c0:
            throw r17;
        L_0x01c1:
            r17 = move-exception;
            monitor-exit(r20);	 Catch:{ all -> 0x01c1 }
            throw r17;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.CachedObservable.ReplayProducer.replay():void");
        }
    }

    public static <T> CachedObservable<T> from(Observable<? extends T> source) {
        return from(source, 16);
    }

    public static <T> CachedObservable<T> from(Observable<? extends T> source, int capacityHint) {
        if (capacityHint < 1) {
            throw new IllegalArgumentException("capacityHint > 0 required");
        }
        CacheState<T> state = new CacheState(source, capacityHint);
        return new CachedObservable(new CachedSubscribe(state), state);
    }

    private CachedObservable(Observable$OnSubscribe<T> onSubscribe, CacheState<T> state) {
        super(onSubscribe);
        this.state = state;
    }

    boolean isConnected() {
        return this.state.isConnected;
    }

    boolean hasObservers() {
        return this.state.producers.length != 0;
    }
}
