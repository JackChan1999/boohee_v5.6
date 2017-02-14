package rx.internal.operators;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import rx.Observable;
import rx.Observable$Operator;
import rx.Producer;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.internal.util.atomic.SpscAtomicArrayQueue;
import rx.internal.util.unsafe.SpscArrayQueue;
import rx.internal.util.unsafe.UnsafeAccess;
import rx.subscriptions.Subscriptions;

public final class OperatorEagerConcatMap<T, R> implements Observable$Operator<R, T> {
    final int bufferSize;
    final Func1<? super T, ? extends Observable<? extends R>> mapper;

    static final class EagerInnerSubscriber<T> extends Subscriber<T> {
        volatile boolean done;
        Throwable error;
        final EagerOuterSubscriber<?, T> parent;
        final Queue<T> queue;

        public EagerInnerSubscriber(EagerOuterSubscriber<?, T> parent, int bufferSize) {
            Queue<T> q;
            this.parent = parent;
            if (UnsafeAccess.isUnsafeAvailable()) {
                q = new SpscArrayQueue(bufferSize);
            } else {
                q = new SpscAtomicArrayQueue(bufferSize);
            }
            this.queue = q;
            request((long) bufferSize);
        }

        public void onNext(T t) {
            this.queue.offer(t);
            this.parent.drain();
        }

        public void onError(Throwable e) {
            this.error = e;
            this.done = true;
            this.parent.drain();
        }

        public void onCompleted() {
            this.done = true;
            this.parent.drain();
        }

        void requestMore(long n) {
            request(n);
        }
    }

    static final class EagerOuterProducer extends AtomicLong implements Producer {
        private static final long serialVersionUID = -657299606803478389L;
        final EagerOuterSubscriber<?, ?> parent;

        public EagerOuterProducer(EagerOuterSubscriber<?, ?> parent) {
            this.parent = parent;
        }

        public void request(long n) {
            if (n < 0) {
                throw new IllegalStateException("n >= 0 required but it was " + n);
            } else if (n > 0) {
                BackpressureUtils.getAndAddRequest(this, n);
                this.parent.drain();
            }
        }
    }

    static final class EagerOuterSubscriber<T, R> extends Subscriber<T> {
        final Subscriber<? super R> actual;
        final int bufferSize;
        volatile boolean cancelled;
        volatile boolean done;
        Throwable error;
        final Func1<? super T, ? extends Observable<? extends R>> mapper;
        private EagerOuterProducer sharedProducer;
        final LinkedList<EagerInnerSubscriber<R>> subscribers = new LinkedList();
        final AtomicInteger wip = new AtomicInteger();

        public EagerOuterSubscriber(Func1<? super T, ? extends Observable<? extends R>> mapper, int bufferSize, Subscriber<? super R> actual) {
            this.mapper = mapper;
            this.bufferSize = bufferSize;
            this.actual = actual;
        }

        void init() {
            this.sharedProducer = new EagerOuterProducer(this);
            add(Subscriptions.create(new Action0() {
                public void call() {
                    EagerOuterSubscriber.this.cancelled = true;
                    if (EagerOuterSubscriber.this.wip.getAndIncrement() == 0) {
                        EagerOuterSubscriber.this.cleanup();
                    }
                }
            }));
            this.actual.add(this);
            this.actual.setProducer(this.sharedProducer);
        }

        void cleanup() {
            synchronized (this.subscribers) {
                List<Subscription> list = new ArrayList(this.subscribers);
                this.subscribers.clear();
            }
            for (Subscription s : list) {
                s.unsubscribe();
            }
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onNext(T r6) {
            /*
            r5 = this;
            r3 = r5.mapper;	 Catch:{ Throwable -> 0x0014 }
            r2 = r3.call(r6);	 Catch:{ Throwable -> 0x0014 }
            r2 = (rx.Observable) r2;	 Catch:{ Throwable -> 0x0014 }
            r1 = new rx.internal.operators.OperatorEagerConcatMap$EagerInnerSubscriber;
            r3 = r5.bufferSize;
            r1.<init>(r5, r3);
            r3 = r5.cancelled;
            if (r3 == 0) goto L_0x001b;
        L_0x0013:
            return;
        L_0x0014:
            r0 = move-exception;
            r3 = r5.actual;
            rx.exceptions.Exceptions.throwOrReport(r0, r3, r6);
            goto L_0x0013;
        L_0x001b:
            r4 = r5.subscribers;
            monitor-enter(r4);
            r3 = r5.cancelled;	 Catch:{ all -> 0x0024 }
            if (r3 == 0) goto L_0x0027;
        L_0x0022:
            monitor-exit(r4);	 Catch:{ all -> 0x0024 }
            goto L_0x0013;
        L_0x0024:
            r3 = move-exception;
            monitor-exit(r4);	 Catch:{ all -> 0x0024 }
            throw r3;
        L_0x0027:
            r3 = r5.subscribers;	 Catch:{ all -> 0x0024 }
            r3.add(r1);	 Catch:{ all -> 0x0024 }
            monitor-exit(r4);	 Catch:{ all -> 0x0024 }
            r3 = r5.cancelled;
            if (r3 != 0) goto L_0x0013;
        L_0x0031:
            r2.unsafeSubscribe(r1);
            r5.drain();
            goto L_0x0013;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorEagerConcatMap.EagerOuterSubscriber.onNext(java.lang.Object):void");
        }

        public void onError(Throwable e) {
            this.error = e;
            this.done = true;
            drain();
        }

        public void onCompleted() {
            this.done = true;
            drain();
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        void drain() {
            /*
            r22 = this;
            r0 = r22;
            r0 = r0.wip;
            r19 = r0;
            r19 = r19.getAndIncrement();
            if (r19 == 0) goto L_0x000d;
        L_0x000c:
            return;
        L_0x000d:
            r12 = 1;
            r0 = r22;
            r14 = r0.sharedProducer;
            r0 = r22;
            r2 = r0.actual;
        L_0x0016:
            r0 = r22;
            r0 = r0.cancelled;
            r19 = r0;
            if (r19 == 0) goto L_0x0022;
        L_0x001e:
            r22.cleanup();
            goto L_0x000c;
        L_0x0022:
            r0 = r22;
            r13 = r0.done;
            r0 = r22;
            r0 = r0.subscribers;
            r20 = r0;
            monitor-enter(r20);
            r0 = r22;
            r0 = r0.subscribers;	 Catch:{ all -> 0x004c }
            r19 = r0;
            r11 = r19.peek();	 Catch:{ all -> 0x004c }
            r11 = (rx.internal.operators.OperatorEagerConcatMap.EagerInnerSubscriber) r11;	 Catch:{ all -> 0x004c }
            monitor-exit(r20);	 Catch:{ all -> 0x004c }
            if (r11 != 0) goto L_0x004f;
        L_0x003c:
            r3 = 1;
        L_0x003d:
            if (r13 == 0) goto L_0x0057;
        L_0x003f:
            r0 = r22;
            r6 = r0.error;
            if (r6 == 0) goto L_0x0051;
        L_0x0045:
            r22.cleanup();
            r2.onError(r6);
            goto L_0x000c;
        L_0x004c:
            r19 = move-exception;
            monitor-exit(r20);	 Catch:{ all -> 0x004c }
            throw r19;
        L_0x004f:
            r3 = 0;
            goto L_0x003d;
        L_0x0051:
            if (r3 == 0) goto L_0x0057;
        L_0x0053:
            r2.onCompleted();
            goto L_0x000c;
        L_0x0057:
            if (r3 != 0) goto L_0x00b4;
        L_0x0059:
            r16 = r14.get();
            r4 = 0;
            r20 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
            r19 = (r16 > r20 ? 1 : (r16 == r20 ? 0 : -1));
            if (r19 != 0) goto L_0x0082;
        L_0x0068:
            r15 = 1;
        L_0x0069:
            r10 = r11.queue;
            r8 = 0;
        L_0x006c:
            r13 = r11.done;
            r18 = r10.peek();
            if (r18 != 0) goto L_0x0084;
        L_0x0074:
            r3 = 1;
        L_0x0075:
            if (r13 == 0) goto L_0x00c8;
        L_0x0077:
            r9 = r11.error;
            if (r9 == 0) goto L_0x0086;
        L_0x007b:
            r22.cleanup();
            r2.onError(r9);
            goto L_0x000c;
        L_0x0082:
            r15 = 0;
            goto L_0x0069;
        L_0x0084:
            r3 = 0;
            goto L_0x0075;
        L_0x0086:
            if (r3 == 0) goto L_0x00c8;
        L_0x0088:
            r0 = r22;
            r0 = r0.subscribers;
            r20 = r0;
            monitor-enter(r20);
            r0 = r22;
            r0 = r0.subscribers;	 Catch:{ all -> 0x00c5 }
            r19 = r0;
            r19.poll();	 Catch:{ all -> 0x00c5 }
            monitor-exit(r20);	 Catch:{ all -> 0x00c5 }
            r11.unsubscribe();
            r8 = 1;
        L_0x009d:
            r20 = 0;
            r19 = (r4 > r20 ? 1 : (r4 == r20 ? 0 : -1));
            if (r19 == 0) goto L_0x00b2;
        L_0x00a3:
            if (r15 != 0) goto L_0x00a8;
        L_0x00a5:
            r14.addAndGet(r4);
        L_0x00a8:
            if (r8 != 0) goto L_0x00b2;
        L_0x00aa:
            r0 = -r4;
            r20 = r0;
            r0 = r20;
            r11.requestMore(r0);
        L_0x00b2:
            if (r8 != 0) goto L_0x0016;
        L_0x00b4:
            r0 = r22;
            r0 = r0.wip;
            r19 = r0;
            r0 = -r12;
            r20 = r0;
            r12 = r19.addAndGet(r20);
            if (r12 != 0) goto L_0x0016;
        L_0x00c3:
            goto L_0x000c;
        L_0x00c5:
            r19 = move-exception;
            monitor-exit(r20);	 Catch:{ all -> 0x00c5 }
            throw r19;
        L_0x00c8:
            if (r3 != 0) goto L_0x009d;
        L_0x00ca:
            r20 = 0;
            r19 = (r16 > r20 ? 1 : (r16 == r20 ? 0 : -1));
            if (r19 == 0) goto L_0x009d;
        L_0x00d0:
            r10.poll();
            r0 = r18;
            r2.onNext(r0);	 Catch:{ Throwable -> 0x00e1 }
            r20 = 1;
            r16 = r16 - r20;
            r20 = 1;
            r4 = r4 - r20;
            goto L_0x006c;
        L_0x00e1:
            r7 = move-exception;
            r0 = r18;
            rx.exceptions.Exceptions.throwOrReport(r7, r2, r0);
            goto L_0x000c;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorEagerConcatMap.EagerOuterSubscriber.drain():void");
        }
    }

    public OperatorEagerConcatMap(Func1<? super T, ? extends Observable<? extends R>> mapper, int bufferSize) {
        this.mapper = mapper;
        this.bufferSize = bufferSize;
    }

    public Subscriber<? super T> call(Subscriber<? super R> t) {
        EagerOuterSubscriber<T, R> outer = new EagerOuterSubscriber(this.mapper, this.bufferSize, t);
        outer.init();
        return outer;
    }
}
