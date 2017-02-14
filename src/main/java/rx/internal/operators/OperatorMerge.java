package rx.internal.operators;

import android.support.v7.widget.ActivityChooserView.ActivityChooserViewAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;
import rx.Observable;
import rx.Observable$Operator;
import rx.Producer;
import rx.Subscriber;
import rx.exceptions.CompositeException;
import rx.exceptions.MissingBackpressureException;
import rx.exceptions.OnErrorThrowable;
import rx.internal.util.RxRingBuffer;
import rx.internal.util.ScalarSynchronousObservable;
import rx.internal.util.atomic.SpscAtomicArrayQueue;
import rx.internal.util.atomic.SpscExactAtomicArrayQueue;
import rx.internal.util.atomic.SpscUnboundedAtomicArrayQueue;
import rx.internal.util.unsafe.Pow2;
import rx.internal.util.unsafe.SpscArrayQueue;
import rx.internal.util.unsafe.UnsafeAccess;
import rx.subscriptions.CompositeSubscription;

public final class OperatorMerge<T> implements Observable$Operator<T, Observable<? extends T>> {
    final boolean delayErrors;
    final int maxConcurrent;

    private static final class HolderDelayErrors {
        static final OperatorMerge<Object> INSTANCE = new OperatorMerge(true, ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED);

        private HolderDelayErrors() {
        }
    }

    private static final class HolderNoDelay {
        static final OperatorMerge<Object> INSTANCE = new OperatorMerge(false, ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED);

        private HolderNoDelay() {
        }
    }

    static final class InnerSubscriber<T> extends Subscriber<T> {
        static final int limit = (RxRingBuffer.SIZE / 4);
        volatile boolean done;
        final long id;
        int outstanding;
        final MergeSubscriber<T> parent;
        volatile RxRingBuffer queue;

        public InnerSubscriber(MergeSubscriber<T> parent, long id) {
            this.parent = parent;
            this.id = id;
        }

        public void onStart() {
            this.outstanding = RxRingBuffer.SIZE;
            request((long) RxRingBuffer.SIZE);
        }

        public void onNext(T t) {
            this.parent.tryEmit(this, t);
        }

        public void onError(Throwable e) {
            this.done = true;
            this.parent.getOrCreateErrorQueue().offer(e);
            this.parent.emit();
        }

        public void onCompleted() {
            this.done = true;
            this.parent.emit();
        }

        public void requestMore(long n) {
            int r = this.outstanding - ((int) n);
            if (r > limit) {
                this.outstanding = r;
                return;
            }
            this.outstanding = RxRingBuffer.SIZE;
            int k = RxRingBuffer.SIZE - r;
            if (k > 0) {
                request((long) k);
            }
        }
    }

    static final class MergeProducer<T> extends AtomicLong implements Producer {
        private static final long serialVersionUID = -1214379189873595503L;
        final MergeSubscriber<T> subscriber;

        public MergeProducer(MergeSubscriber<T> subscriber) {
            this.subscriber = subscriber;
        }

        public void request(long n) {
            if (n > 0) {
                if (get() != Long.MAX_VALUE) {
                    BackpressureUtils.getAndAddRequest(this, n);
                    this.subscriber.emit();
                }
            } else if (n < 0) {
                throw new IllegalArgumentException("n >= 0 required");
            }
        }

        public long produced(int n) {
            return addAndGet((long) (-n));
        }
    }

    static final class MergeSubscriber<T> extends Subscriber<Observable<? extends T>> {
        static final InnerSubscriber<?>[] EMPTY = new InnerSubscriber[0];
        final Subscriber<? super T> child;
        final boolean delayErrors;
        volatile boolean done;
        boolean emitting;
        volatile ConcurrentLinkedQueue<Throwable> errors;
        final Object innerGuard = new Object();
        volatile InnerSubscriber<?>[] innerSubscribers = EMPTY;
        long lastId;
        int lastIndex;
        final int maxConcurrent;
        boolean missed;
        final NotificationLite<T> nl = NotificationLite.instance();
        MergeProducer<T> producer;
        volatile Queue<Object> queue;
        volatile CompositeSubscription subscriptions;
        long uniqueId;

        public MergeSubscriber(Subscriber<? super T> child, boolean delayErrors, int maxConcurrent) {
            this.child = child;
            this.delayErrors = delayErrors;
            this.maxConcurrent = maxConcurrent;
            request(maxConcurrent == ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED ? Long.MAX_VALUE : (long) maxConcurrent);
        }

        Queue<Throwable> getOrCreateErrorQueue() {
            ConcurrentLinkedQueue<Throwable> q = this.errors;
            if (q == null) {
                synchronized (this) {
                    try {
                        q = this.errors;
                        if (q == null) {
                            ConcurrentLinkedQueue<Throwable> q2 = new ConcurrentLinkedQueue();
                            try {
                                this.errors = q2;
                                q = q2;
                            } catch (Throwable th) {
                                Throwable th2 = th;
                                q = q2;
                                throw th2;
                            }
                        }
                    } catch (Throwable th3) {
                        th2 = th3;
                        throw th2;
                    }
                }
            }
            return q;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        rx.subscriptions.CompositeSubscription getOrCreateComposite() {
            /*
            r4 = this;
            r0 = r4.subscriptions;
            if (r0 != 0) goto L_0x0019;
        L_0x0004:
            r2 = 0;
            monitor-enter(r4);
            r0 = r4.subscriptions;	 Catch:{ all -> 0x001a }
            if (r0 != 0) goto L_0x0013;
        L_0x000a:
            r1 = new rx.subscriptions.CompositeSubscription;	 Catch:{ all -> 0x001a }
            r1.<init>();	 Catch:{ all -> 0x001a }
            r4.subscriptions = r1;	 Catch:{ all -> 0x001d }
            r2 = 1;
            r0 = r1;
        L_0x0013:
            monitor-exit(r4);	 Catch:{ all -> 0x001a }
            if (r2 == 0) goto L_0x0019;
        L_0x0016:
            r4.add(r0);
        L_0x0019:
            return r0;
        L_0x001a:
            r3 = move-exception;
        L_0x001b:
            monitor-exit(r4);	 Catch:{ all -> 0x001a }
            throw r3;
        L_0x001d:
            r3 = move-exception;
            r0 = r1;
            goto L_0x001b;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorMerge.MergeSubscriber.getOrCreateComposite():rx.subscriptions.CompositeSubscription");
        }

        public void onNext(Observable<? extends T> t) {
            if (t != null) {
                if (t instanceof ScalarSynchronousObservable) {
                    tryEmit(((ScalarSynchronousObservable) t).get());
                    return;
                }
                long j = this.uniqueId;
                this.uniqueId = 1 + j;
                InnerSubscriber<T> inner = new InnerSubscriber(this, j);
                addInner(inner);
                t.unsafeSubscribe(inner);
                emit();
            }
        }

        private void reportError() {
            List<Throwable> list = new ArrayList(this.errors);
            if (list.size() == 1) {
                this.child.onError((Throwable) list.get(0));
            } else {
                this.child.onError(new CompositeException(list));
            }
        }

        public void onError(Throwable e) {
            getOrCreateErrorQueue().offer(e);
            this.done = true;
            emit();
        }

        public void onCompleted() {
            this.done = true;
            emit();
        }

        void addInner(InnerSubscriber<T> inner) {
            getOrCreateComposite().add(inner);
            synchronized (this.innerGuard) {
                InnerSubscriber<?>[] a = this.innerSubscribers;
                int n = a.length;
                InnerSubscriber<?>[] b = new InnerSubscriber[(n + 1)];
                System.arraycopy(a, 0, b, 0, n);
                b[n] = inner;
                this.innerSubscribers = b;
            }
        }

        void removeInner(InnerSubscriber<T> inner) {
            RxRingBuffer q = inner.queue;
            if (q != null) {
                q.release();
            }
            this.subscriptions.remove(inner);
            synchronized (this.innerGuard) {
                InnerSubscriber<?>[] a = this.innerSubscribers;
                int n = a.length;
                int j = -1;
                for (int i = 0; i < n; i++) {
                    if (inner.equals(a[i])) {
                        j = i;
                        break;
                    }
                }
                if (j < 0) {
                } else if (n == 1) {
                    this.innerSubscribers = EMPTY;
                } else {
                    InnerSubscriber<?>[] b = new InnerSubscriber[(n - 1)];
                    System.arraycopy(a, 0, b, 0, j);
                    System.arraycopy(a, j + 1, b, j, (n - j) - 1);
                    this.innerSubscribers = b;
                }
            }
        }

        void tryEmit(InnerSubscriber<T> subscriber, T value) {
            boolean success = false;
            long r = this.producer.get();
            if (r != 0) {
                synchronized (this) {
                    r = this.producer.get();
                    if (!(this.emitting || r == 0)) {
                        this.emitting = true;
                        success = true;
                    }
                }
            }
            if (success) {
                emitScalar(subscriber, value, r);
            } else {
                queueScalar(subscriber, value);
            }
        }

        protected void queueScalar(InnerSubscriber<T> subscriber, T value) {
            RxRingBuffer q = subscriber.queue;
            if (q == null) {
                q = RxRingBuffer.getSpscInstance();
                subscriber.add(q);
                subscriber.queue = q;
            }
            try {
                q.onNext(this.nl.next(value));
                emit();
            } catch (MissingBackpressureException ex) {
                subscriber.unsubscribe();
                subscriber.onError(ex);
            } catch (IllegalStateException ex2) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.unsubscribe();
                    subscriber.onError(ex2);
                }
            }
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        protected void emitScalar(rx.internal.operators.OperatorMerge.InnerSubscriber<T> r6, T r7, long r8) {
            /*
            r5 = this;
            r0 = 0;
            r2 = r5.child;	 Catch:{ Throwable -> 0x002c }
            r2.onNext(r7);	 Catch:{ Throwable -> 0x002c }
        L_0x0006:
            r2 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
            r2 = (r8 > r2 ? 1 : (r8 == r2 ? 0 : -1));
            if (r2 == 0) goto L_0x0015;
        L_0x000f:
            r2 = r5.producer;	 Catch:{ all -> 0x004e }
            r3 = 1;
            r2.produced(r3);	 Catch:{ all -> 0x004e }
        L_0x0015:
            r2 = 1;
            r6.requestMore(r2);	 Catch:{ all -> 0x004e }
            monitor-enter(r5);	 Catch:{ all -> 0x004e }
            r0 = 1;
            r2 = r5.missed;	 Catch:{ all -> 0x0069 }
            if (r2 != 0) goto L_0x005a;
        L_0x0020:
            r2 = 0;
            r5.emitting = r2;	 Catch:{ all -> 0x0069 }
            monitor-exit(r5);	 Catch:{ all -> 0x0069 }
            if (r0 != 0) goto L_0x002b;
        L_0x0026:
            monitor-enter(r5);
            r2 = 0;
            r5.emitting = r2;	 Catch:{ all -> 0x0057 }
            monitor-exit(r5);	 Catch:{ all -> 0x0057 }
        L_0x002b:
            return;
        L_0x002c:
            r1 = move-exception;
            r2 = r5.delayErrors;	 Catch:{ all -> 0x004e }
            if (r2 != 0) goto L_0x0046;
        L_0x0031:
            rx.exceptions.Exceptions.throwIfFatal(r1);	 Catch:{ all -> 0x004e }
            r0 = 1;
            r6.unsubscribe();	 Catch:{ all -> 0x004e }
            r6.onError(r1);	 Catch:{ all -> 0x004e }
            if (r0 != 0) goto L_0x002b;
        L_0x003d:
            monitor-enter(r5);
            r2 = 0;
            r5.emitting = r2;	 Catch:{ all -> 0x0043 }
            monitor-exit(r5);	 Catch:{ all -> 0x0043 }
            goto L_0x002b;
        L_0x0043:
            r2 = move-exception;
            monitor-exit(r5);	 Catch:{ all -> 0x0043 }
            throw r2;
        L_0x0046:
            r2 = r5.getOrCreateErrorQueue();	 Catch:{ all -> 0x004e }
            r2.offer(r1);	 Catch:{ all -> 0x004e }
            goto L_0x0006;
        L_0x004e:
            r2 = move-exception;
            if (r0 != 0) goto L_0x0056;
        L_0x0051:
            monitor-enter(r5);
            r3 = 0;
            r5.emitting = r3;	 Catch:{ all -> 0x006f }
            monitor-exit(r5);	 Catch:{ all -> 0x006f }
        L_0x0056:
            throw r2;
        L_0x0057:
            r2 = move-exception;
            monitor-exit(r5);	 Catch:{ all -> 0x0057 }
            throw r2;
        L_0x005a:
            r2 = 0;
            r5.missed = r2;	 Catch:{ all -> 0x0069 }
            monitor-exit(r5);	 Catch:{ all -> 0x0069 }
            if (r0 != 0) goto L_0x0065;
        L_0x0060:
            monitor-enter(r5);
            r2 = 0;
            r5.emitting = r2;	 Catch:{ all -> 0x006c }
            monitor-exit(r5);	 Catch:{ all -> 0x006c }
        L_0x0065:
            r5.emitLoop();
            goto L_0x002b;
        L_0x0069:
            r2 = move-exception;
            monitor-exit(r5);	 Catch:{ all -> 0x0069 }
            throw r2;	 Catch:{ all -> 0x004e }
        L_0x006c:
            r2 = move-exception;
            monitor-exit(r5);	 Catch:{ all -> 0x006c }
            throw r2;
        L_0x006f:
            r2 = move-exception;
            monitor-exit(r5);	 Catch:{ all -> 0x006f }
            throw r2;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorMerge.MergeSubscriber.emitScalar(rx.internal.operators.OperatorMerge$InnerSubscriber, java.lang.Object, long):void");
        }

        public void requestMore(long n) {
            request(n);
        }

        void tryEmit(T value) {
            boolean success = false;
            long r = this.producer.get();
            if (r != 0) {
                synchronized (this) {
                    r = this.producer.get();
                    if (!(this.emitting || r == 0)) {
                        this.emitting = true;
                        success = true;
                    }
                }
            }
            if (success) {
                emitScalar(value, r);
            } else {
                queueScalar(value);
            }
        }

        protected void queueScalar(T value) {
            Queue<Object> q = this.queue;
            if (q == null) {
                int mc = this.maxConcurrent;
                if (mc == ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED) {
                    q = new SpscUnboundedAtomicArrayQueue(RxRingBuffer.SIZE);
                } else if (!Pow2.isPowerOfTwo(mc)) {
                    q = new SpscExactAtomicArrayQueue(mc);
                } else if (UnsafeAccess.isUnsafeAvailable()) {
                    q = new SpscArrayQueue(mc);
                } else {
                    q = new SpscAtomicArrayQueue(mc);
                }
                this.queue = q;
            }
            if (q.offer(value)) {
                emit();
                return;
            }
            unsubscribe();
            onError(OnErrorThrowable.addValueAsLastCause(new MissingBackpressureException(), value));
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        protected void emitScalar(T r5, long r6) {
            /*
            r4 = this;
            r0 = 0;
            r2 = r4.child;	 Catch:{ Throwable -> 0x002c }
            r2.onNext(r5);	 Catch:{ Throwable -> 0x002c }
        L_0x0006:
            r2 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
            r2 = (r6 > r2 ? 1 : (r6 == r2 ? 0 : -1));
            if (r2 == 0) goto L_0x0015;
        L_0x000f:
            r2 = r4.producer;	 Catch:{ all -> 0x004e }
            r3 = 1;
            r2.produced(r3);	 Catch:{ all -> 0x004e }
        L_0x0015:
            r2 = 1;
            r4.requestMore(r2);	 Catch:{ all -> 0x004e }
            monitor-enter(r4);	 Catch:{ all -> 0x004e }
            r0 = 1;
            r2 = r4.missed;	 Catch:{ all -> 0x0069 }
            if (r2 != 0) goto L_0x005a;
        L_0x0020:
            r2 = 0;
            r4.emitting = r2;	 Catch:{ all -> 0x0069 }
            monitor-exit(r4);	 Catch:{ all -> 0x0069 }
            if (r0 != 0) goto L_0x002b;
        L_0x0026:
            monitor-enter(r4);
            r2 = 0;
            r4.emitting = r2;	 Catch:{ all -> 0x0057 }
            monitor-exit(r4);	 Catch:{ all -> 0x0057 }
        L_0x002b:
            return;
        L_0x002c:
            r1 = move-exception;
            r2 = r4.delayErrors;	 Catch:{ all -> 0x004e }
            if (r2 != 0) goto L_0x0046;
        L_0x0031:
            rx.exceptions.Exceptions.throwIfFatal(r1);	 Catch:{ all -> 0x004e }
            r0 = 1;
            r4.unsubscribe();	 Catch:{ all -> 0x004e }
            r4.onError(r1);	 Catch:{ all -> 0x004e }
            if (r0 != 0) goto L_0x002b;
        L_0x003d:
            monitor-enter(r4);
            r2 = 0;
            r4.emitting = r2;	 Catch:{ all -> 0x0043 }
            monitor-exit(r4);	 Catch:{ all -> 0x0043 }
            goto L_0x002b;
        L_0x0043:
            r2 = move-exception;
            monitor-exit(r4);	 Catch:{ all -> 0x0043 }
            throw r2;
        L_0x0046:
            r2 = r4.getOrCreateErrorQueue();	 Catch:{ all -> 0x004e }
            r2.offer(r1);	 Catch:{ all -> 0x004e }
            goto L_0x0006;
        L_0x004e:
            r2 = move-exception;
            if (r0 != 0) goto L_0x0056;
        L_0x0051:
            monitor-enter(r4);
            r3 = 0;
            r4.emitting = r3;	 Catch:{ all -> 0x006f }
            monitor-exit(r4);	 Catch:{ all -> 0x006f }
        L_0x0056:
            throw r2;
        L_0x0057:
            r2 = move-exception;
            monitor-exit(r4);	 Catch:{ all -> 0x0057 }
            throw r2;
        L_0x005a:
            r2 = 0;
            r4.missed = r2;	 Catch:{ all -> 0x0069 }
            monitor-exit(r4);	 Catch:{ all -> 0x0069 }
            if (r0 != 0) goto L_0x0065;
        L_0x0060:
            monitor-enter(r4);
            r2 = 0;
            r4.emitting = r2;	 Catch:{ all -> 0x006c }
            monitor-exit(r4);	 Catch:{ all -> 0x006c }
        L_0x0065:
            r4.emitLoop();
            goto L_0x002b;
        L_0x0069:
            r2 = move-exception;
            monitor-exit(r4);	 Catch:{ all -> 0x0069 }
            throw r2;	 Catch:{ all -> 0x004e }
        L_0x006c:
            r2 = move-exception;
            monitor-exit(r4);	 Catch:{ all -> 0x006c }
            throw r2;
        L_0x006f:
            r2 = move-exception;
            monitor-exit(r4);	 Catch:{ all -> 0x006f }
            throw r2;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorMerge.MergeSubscriber.emitScalar(java.lang.Object, long):void");
        }

        void emit() {
            synchronized (this) {
                if (this.emitting) {
                    this.missed = true;
                    return;
                }
                this.emitting = true;
                emitLoop();
            }
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        void emitLoop() {
            /*
            r32 = this;
            r23 = 0;
            r0 = r32;
            r4 = r0.child;	 Catch:{ all -> 0x0106 }
        L_0x0006:
            r30 = r32.checkTerminate();	 Catch:{ all -> 0x0106 }
            if (r30 == 0) goto L_0x001e;
        L_0x000c:
            r23 = 1;
            if (r23 != 0) goto L_0x001a;
        L_0x0010:
            monitor-enter(r32);
            r30 = 0;
            r0 = r30;
            r1 = r32;
            r1.emitting = r0;	 Catch:{ all -> 0x001b }
            monitor-exit(r32);	 Catch:{ all -> 0x001b }
        L_0x001a:
            return;
        L_0x001b:
            r30 = move-exception;
            monitor-exit(r32);	 Catch:{ all -> 0x001b }
            throw r30;
        L_0x001e:
            r0 = r32;
            r0 = r0.queue;	 Catch:{ all -> 0x0106 }
            r26 = r0;
            r0 = r32;
            r0 = r0.producer;	 Catch:{ all -> 0x0106 }
            r30 = r0;
            r20 = r30.get();	 Catch:{ all -> 0x0106 }
            r30 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
            r30 = (r20 > r30 ? 1 : (r20 == r30 ? 0 : -1));
            if (r30 != 0) goto L_0x0063;
        L_0x0037:
            r28 = 1;
        L_0x0039:
            r19 = 0;
            if (r26 == 0) goto L_0x0079;
        L_0x003d:
            r22 = 0;
            r16 = 0;
        L_0x0041:
            r30 = 0;
            r30 = (r20 > r30 ? 1 : (r20 == r30 ? 0 : -1));
            if (r30 <= 0) goto L_0x0068;
        L_0x0047:
            r16 = r26.poll();	 Catch:{ all -> 0x0106 }
            r30 = r32.checkTerminate();	 Catch:{ all -> 0x0106 }
            if (r30 == 0) goto L_0x0066;
        L_0x0051:
            r23 = 1;
            if (r23 != 0) goto L_0x001a;
        L_0x0055:
            monitor-enter(r32);
            r30 = 0;
            r0 = r30;
            r1 = r32;
            r1.emitting = r0;	 Catch:{ all -> 0x0060 }
            monitor-exit(r32);	 Catch:{ all -> 0x0060 }
            goto L_0x001a;
        L_0x0060:
            r30 = move-exception;
            monitor-exit(r32);	 Catch:{ all -> 0x0060 }
            throw r30;
        L_0x0063:
            r28 = 0;
            goto L_0x0039;
        L_0x0066:
            if (r16 != 0) goto L_0x00b6;
        L_0x0068:
            if (r22 <= 0) goto L_0x0071;
        L_0x006a:
            if (r28 == 0) goto L_0x0114;
        L_0x006c:
            r20 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
        L_0x0071:
            r30 = 0;
            r30 = (r20 > r30 ? 1 : (r20 == r30 ? 0 : -1));
            if (r30 == 0) goto L_0x0079;
        L_0x0077:
            if (r16 != 0) goto L_0x003d;
        L_0x0079:
            r0 = r32;
            r5 = r0.done;	 Catch:{ all -> 0x0106 }
            r0 = r32;
            r0 = r0.queue;	 Catch:{ all -> 0x0106 }
            r26 = r0;
            r0 = r32;
            r9 = r0.innerSubscribers;	 Catch:{ all -> 0x0106 }
            r15 = r9.length;	 Catch:{ all -> 0x0106 }
            if (r5 == 0) goto L_0x0129;
        L_0x008a:
            if (r26 == 0) goto L_0x0092;
        L_0x008c:
            r30 = r26.isEmpty();	 Catch:{ all -> 0x0106 }
            if (r30 == 0) goto L_0x0129;
        L_0x0092:
            if (r15 != 0) goto L_0x0129;
        L_0x0094:
            r0 = r32;
            r6 = r0.errors;	 Catch:{ all -> 0x0106 }
            if (r6 == 0) goto L_0x00a0;
        L_0x009a:
            r30 = r6.isEmpty();	 Catch:{ all -> 0x0106 }
            if (r30 == 0) goto L_0x0124;
        L_0x00a0:
            r4.onCompleted();	 Catch:{ all -> 0x0106 }
        L_0x00a3:
            r23 = 1;
            if (r23 != 0) goto L_0x001a;
        L_0x00a7:
            monitor-enter(r32);
            r30 = 0;
            r0 = r30;
            r1 = r32;
            r1.emitting = r0;	 Catch:{ all -> 0x00b3 }
            monitor-exit(r32);	 Catch:{ all -> 0x00b3 }
            goto L_0x001a;
        L_0x00b3:
            r30 = move-exception;
            monitor-exit(r32);	 Catch:{ all -> 0x00b3 }
            throw r30;
        L_0x00b6:
            r0 = r32;
            r0 = r0.nl;	 Catch:{ all -> 0x0106 }
            r30 = r0;
            r0 = r30;
            r1 = r16;
            r29 = r0.getValue(r1);	 Catch:{ all -> 0x0106 }
            r0 = r29;
            r4.onNext(r0);	 Catch:{ Throwable -> 0x00d3 }
        L_0x00c9:
            r19 = r19 + 1;
            r22 = r22 + 1;
            r30 = 1;
            r20 = r20 - r30;
            goto L_0x0041;
        L_0x00d3:
            r27 = move-exception;
            r0 = r32;
            r0 = r0.delayErrors;	 Catch:{ all -> 0x0106 }
            r30 = r0;
            if (r30 != 0) goto L_0x00fa;
        L_0x00dc:
            rx.exceptions.Exceptions.throwIfFatal(r27);	 Catch:{ all -> 0x0106 }
            r23 = 1;
            r32.unsubscribe();	 Catch:{ all -> 0x0106 }
            r0 = r27;
            r4.onError(r0);	 Catch:{ all -> 0x0106 }
            if (r23 != 0) goto L_0x001a;
        L_0x00eb:
            monitor-enter(r32);
            r30 = 0;
            r0 = r30;
            r1 = r32;
            r1.emitting = r0;	 Catch:{ all -> 0x00f7 }
            monitor-exit(r32);	 Catch:{ all -> 0x00f7 }
            goto L_0x001a;
        L_0x00f7:
            r30 = move-exception;
            monitor-exit(r32);	 Catch:{ all -> 0x00f7 }
            throw r30;
        L_0x00fa:
            r30 = r32.getOrCreateErrorQueue();	 Catch:{ all -> 0x0106 }
            r0 = r30;
            r1 = r27;
            r0.offer(r1);	 Catch:{ all -> 0x0106 }
            goto L_0x00c9;
        L_0x0106:
            r30 = move-exception;
            if (r23 != 0) goto L_0x0113;
        L_0x0109:
            monitor-enter(r32);
            r31 = 0;
            r0 = r31;
            r1 = r32;
            r1.emitting = r0;	 Catch:{ all -> 0x02bd }
            monitor-exit(r32);	 Catch:{ all -> 0x02bd }
        L_0x0113:
            throw r30;
        L_0x0114:
            r0 = r32;
            r0 = r0.producer;	 Catch:{ all -> 0x0106 }
            r30 = r0;
            r0 = r30;
            r1 = r22;
            r20 = r0.produced(r1);	 Catch:{ all -> 0x0106 }
            goto L_0x0071;
        L_0x0124:
            r32.reportError();	 Catch:{ all -> 0x0106 }
            goto L_0x00a3;
        L_0x0129:
            r10 = 0;
            if (r15 <= 0) goto L_0x0271;
        L_0x012c:
            r0 = r32;
            r0 = r0.lastId;	 Catch:{ all -> 0x0106 }
            r24 = r0;
            r0 = r32;
            r8 = r0.lastIndex;	 Catch:{ all -> 0x0106 }
            if (r15 <= r8) goto L_0x0144;
        L_0x0138:
            r30 = r9[r8];	 Catch:{ all -> 0x0106 }
            r0 = r30;
            r0 = r0.id;	 Catch:{ all -> 0x0106 }
            r30 = r0;
            r30 = (r30 > r24 ? 1 : (r30 == r24 ? 0 : -1));
            if (r30 == 0) goto L_0x016a;
        L_0x0144:
            if (r15 > r8) goto L_0x0147;
        L_0x0146:
            r8 = 0;
        L_0x0147:
            r14 = r8;
            r7 = 0;
        L_0x0149:
            if (r7 >= r15) goto L_0x0157;
        L_0x014b:
            r30 = r9[r14];	 Catch:{ all -> 0x0106 }
            r0 = r30;
            r0 = r0.id;	 Catch:{ all -> 0x0106 }
            r30 = r0;
            r30 = (r30 > r24 ? 1 : (r30 == r24 ? 0 : -1));
            if (r30 != 0) goto L_0x0187;
        L_0x0157:
            r8 = r14;
            r0 = r32;
            r0.lastIndex = r14;	 Catch:{ all -> 0x0106 }
            r30 = r9[r14];	 Catch:{ all -> 0x0106 }
            r0 = r30;
            r0 = r0.id;	 Catch:{ all -> 0x0106 }
            r30 = r0;
            r0 = r30;
            r2 = r32;
            r2.lastId = r0;	 Catch:{ all -> 0x0106 }
        L_0x016a:
            r14 = r8;
            r7 = 0;
        L_0x016c:
            if (r7 >= r15) goto L_0x025f;
        L_0x016e:
            r30 = r32.checkTerminate();	 Catch:{ all -> 0x0106 }
            if (r30 == 0) goto L_0x018f;
        L_0x0174:
            r23 = 1;
            if (r23 != 0) goto L_0x001a;
        L_0x0178:
            monitor-enter(r32);
            r30 = 0;
            r0 = r30;
            r1 = r32;
            r1.emitting = r0;	 Catch:{ all -> 0x0184 }
            monitor-exit(r32);	 Catch:{ all -> 0x0184 }
            goto L_0x001a;
        L_0x0184:
            r30 = move-exception;
            monitor-exit(r32);	 Catch:{ all -> 0x0184 }
            throw r30;
        L_0x0187:
            r14 = r14 + 1;
            if (r14 != r15) goto L_0x018c;
        L_0x018b:
            r14 = 0;
        L_0x018c:
            r7 = r7 + 1;
            goto L_0x0149;
        L_0x018f:
            r13 = r9[r14];	 Catch:{ all -> 0x0106 }
            r16 = 0;
        L_0x0193:
            r17 = 0;
        L_0x0195:
            r30 = 0;
            r30 = (r20 > r30 ? 1 : (r20 == r30 ? 0 : -1));
            if (r30 <= 0) goto L_0x01ba;
        L_0x019b:
            r30 = r32.checkTerminate();	 Catch:{ all -> 0x0106 }
            if (r30 == 0) goto L_0x01b4;
        L_0x01a1:
            r23 = 1;
            if (r23 != 0) goto L_0x001a;
        L_0x01a5:
            monitor-enter(r32);
            r30 = 0;
            r0 = r30;
            r1 = r32;
            r1.emitting = r0;	 Catch:{ all -> 0x01b1 }
            monitor-exit(r32);	 Catch:{ all -> 0x01b1 }
            goto L_0x001a;
        L_0x01b1:
            r30 = move-exception;
            monitor-exit(r32);	 Catch:{ all -> 0x01b1 }
            throw r30;
        L_0x01b4:
            r0 = r13.queue;	 Catch:{ all -> 0x0106 }
            r18 = r0;
            if (r18 != 0) goto L_0x020a;
        L_0x01ba:
            if (r17 <= 0) goto L_0x01d6;
        L_0x01bc:
            if (r28 != 0) goto L_0x024f;
        L_0x01be:
            r0 = r32;
            r0 = r0.producer;	 Catch:{ all -> 0x0106 }
            r30 = r0;
            r0 = r30;
            r1 = r17;
            r20 = r0.produced(r1);	 Catch:{ all -> 0x0106 }
        L_0x01cc:
            r0 = r17;
            r0 = (long) r0;	 Catch:{ all -> 0x0106 }
            r30 = r0;
            r0 = r30;
            r13.requestMore(r0);	 Catch:{ all -> 0x0106 }
        L_0x01d6:
            r30 = 0;
            r30 = (r20 > r30 ? 1 : (r20 == r30 ? 0 : -1));
            if (r30 == 0) goto L_0x01de;
        L_0x01dc:
            if (r16 != 0) goto L_0x0193;
        L_0x01de:
            r11 = r13.done;	 Catch:{ all -> 0x0106 }
            r12 = r13.queue;	 Catch:{ all -> 0x0106 }
            if (r11 == 0) goto L_0x0259;
        L_0x01e4:
            if (r12 == 0) goto L_0x01ec;
        L_0x01e6:
            r30 = r12.isEmpty();	 Catch:{ all -> 0x0106 }
            if (r30 == 0) goto L_0x0259;
        L_0x01ec:
            r0 = r32;
            r0.removeInner(r13);	 Catch:{ all -> 0x0106 }
            r30 = r32.checkTerminate();	 Catch:{ all -> 0x0106 }
            if (r30 == 0) goto L_0x0256;
        L_0x01f7:
            r23 = 1;
            if (r23 != 0) goto L_0x001a;
        L_0x01fb:
            monitor-enter(r32);
            r30 = 0;
            r0 = r30;
            r1 = r32;
            r1.emitting = r0;	 Catch:{ all -> 0x0207 }
            monitor-exit(r32);	 Catch:{ all -> 0x0207 }
            goto L_0x001a;
        L_0x0207:
            r30 = move-exception;
            monitor-exit(r32);	 Catch:{ all -> 0x0207 }
            throw r30;
        L_0x020a:
            r16 = r18.poll();	 Catch:{ all -> 0x0106 }
            if (r16 == 0) goto L_0x01ba;
        L_0x0210:
            r0 = r32;
            r0 = r0.nl;	 Catch:{ all -> 0x0106 }
            r30 = r0;
            r0 = r30;
            r1 = r16;
            r29 = r0.getValue(r1);	 Catch:{ all -> 0x0106 }
            r0 = r29;
            r4.onNext(r0);	 Catch:{ Throwable -> 0x022b }
            r30 = 1;
            r20 = r20 - r30;
            r17 = r17 + 1;
            goto L_0x0195;
        L_0x022b:
            r27 = move-exception;
            r23 = 1;
            rx.exceptions.Exceptions.throwIfFatal(r27);	 Catch:{ all -> 0x0106 }
            r0 = r27;
            r4.onError(r0);	 Catch:{ all -> 0x024a }
            r32.unsubscribe();	 Catch:{ all -> 0x0106 }
            if (r23 != 0) goto L_0x001a;
        L_0x023b:
            monitor-enter(r32);
            r30 = 0;
            r0 = r30;
            r1 = r32;
            r1.emitting = r0;	 Catch:{ all -> 0x0247 }
            monitor-exit(r32);	 Catch:{ all -> 0x0247 }
            goto L_0x001a;
        L_0x0247:
            r30 = move-exception;
            monitor-exit(r32);	 Catch:{ all -> 0x0247 }
            throw r30;
        L_0x024a:
            r30 = move-exception;
            r32.unsubscribe();	 Catch:{ all -> 0x0106 }
            throw r30;	 Catch:{ all -> 0x0106 }
        L_0x024f:
            r20 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
            goto L_0x01cc;
        L_0x0256:
            r19 = r19 + 1;
            r10 = 1;
        L_0x0259:
            r30 = 0;
            r30 = (r20 > r30 ? 1 : (r20 == r30 ? 0 : -1));
            if (r30 != 0) goto L_0x02a6;
        L_0x025f:
            r0 = r32;
            r0.lastIndex = r14;	 Catch:{ all -> 0x0106 }
            r30 = r9[r14];	 Catch:{ all -> 0x0106 }
            r0 = r30;
            r0 = r0.id;	 Catch:{ all -> 0x0106 }
            r30 = r0;
            r0 = r30;
            r2 = r32;
            r2.lastId = r0;	 Catch:{ all -> 0x0106 }
        L_0x0271:
            if (r19 <= 0) goto L_0x027f;
        L_0x0273:
            r0 = r19;
            r0 = (long) r0;	 Catch:{ all -> 0x0106 }
            r30 = r0;
            r0 = r32;
            r1 = r30;
            r0.request(r1);	 Catch:{ all -> 0x0106 }
        L_0x027f:
            if (r10 != 0) goto L_0x0006;
        L_0x0281:
            monitor-enter(r32);	 Catch:{ all -> 0x0106 }
            r0 = r32;
            r0 = r0.missed;	 Catch:{ all -> 0x02ba }
            r30 = r0;
            if (r30 != 0) goto L_0x02af;
        L_0x028a:
            r23 = 1;
            r30 = 0;
            r0 = r30;
            r1 = r32;
            r1.emitting = r0;	 Catch:{ all -> 0x02ba }
            monitor-exit(r32);	 Catch:{ all -> 0x02ba }
            if (r23 != 0) goto L_0x001a;
        L_0x0297:
            monitor-enter(r32);
            r30 = 0;
            r0 = r30;
            r1 = r32;
            r1.emitting = r0;	 Catch:{ all -> 0x02a3 }
            monitor-exit(r32);	 Catch:{ all -> 0x02a3 }
            goto L_0x001a;
        L_0x02a3:
            r30 = move-exception;
            monitor-exit(r32);	 Catch:{ all -> 0x02a3 }
            throw r30;
        L_0x02a6:
            r14 = r14 + 1;
            if (r14 != r15) goto L_0x02ab;
        L_0x02aa:
            r14 = 0;
        L_0x02ab:
            r7 = r7 + 1;
            goto L_0x016c;
        L_0x02af:
            r30 = 0;
            r0 = r30;
            r1 = r32;
            r1.missed = r0;	 Catch:{ all -> 0x02ba }
            monitor-exit(r32);	 Catch:{ all -> 0x02ba }
            goto L_0x0006;
        L_0x02ba:
            r30 = move-exception;
            monitor-exit(r32);	 Catch:{ all -> 0x02ba }
            throw r30;	 Catch:{ all -> 0x0106 }
        L_0x02bd:
            r30 = move-exception;
            monitor-exit(r32);	 Catch:{ all -> 0x02bd }
            throw r30;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorMerge.MergeSubscriber.emitLoop():void");
        }

        boolean checkTerminate() {
            if (this.child.isUnsubscribed()) {
                return true;
            }
            Queue<Throwable> e = this.errors;
            if (this.delayErrors || e == null || e.isEmpty()) {
                return false;
            }
            try {
                reportError();
                return true;
            } finally {
                unsubscribe();
            }
        }
    }

    public static <T> OperatorMerge<T> instance(boolean delayErrors) {
        if (delayErrors) {
            return HolderDelayErrors.INSTANCE;
        }
        return HolderNoDelay.INSTANCE;
    }

    public static <T> OperatorMerge<T> instance(boolean delayErrors, int maxConcurrent) {
        if (maxConcurrent == ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED) {
            return instance(delayErrors);
        }
        return new OperatorMerge(delayErrors, maxConcurrent);
    }

    private OperatorMerge(boolean delayErrors, int maxConcurrent) {
        this.delayErrors = delayErrors;
        this.maxConcurrent = maxConcurrent;
    }

    public Subscriber<Observable<? extends T>> call(Subscriber<? super T> child) {
        MergeSubscriber<T> subscriber = new MergeSubscriber(child, this.delayErrors, this.maxConcurrent);
        MergeProducer<T> producer = new MergeProducer(subscriber);
        subscriber.producer = producer;
        child.add(subscriber);
        child.setProducer(producer);
        return subscriber;
    }
}
