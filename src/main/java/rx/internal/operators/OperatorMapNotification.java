package rx.internal.operators;

import com.umeng.socialize.common.SocializeConstants;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;
import rx.Observable$Operator;
import rx.Producer;
import rx.Subscriber;
import rx.Subscription;
import rx.exceptions.Exceptions;
import rx.exceptions.MissingBackpressureException;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.internal.producers.ProducerArbiter;
import rx.internal.util.unsafe.SpscArrayQueue;
import rx.internal.util.unsafe.UnsafeAccess;

public final class OperatorMapNotification<T, R> implements Observable$Operator<R, T> {
    private final Func0<? extends R> onCompleted;
    private final Func1<? super Throwable, ? extends R> onError;
    private final Func1<? super T, ? extends R> onNext;

    final class MapNotificationSubscriber extends Subscriber<T> {
        final SingleEmitter<R> emitter;
        private final Subscriber<? super R> o;
        private final ProducerArbiter pa;

        private MapNotificationSubscriber(ProducerArbiter pa, Subscriber<? super R> o) {
            this.pa = pa;
            this.o = o;
            this.emitter = new SingleEmitter(o, pa, this);
        }

        void init() {
            this.o.setProducer(this.emitter);
        }

        public void setProducer(Producer producer) {
            this.pa.setProducer(producer);
        }

        public void onCompleted() {
            try {
                this.emitter.offerAndComplete(OperatorMapNotification.this.onCompleted.call());
            } catch (Throwable e) {
                Exceptions.throwOrReport(e, this.o);
            }
        }

        public void onError(Throwable e) {
            try {
                this.emitter.offerAndComplete(OperatorMapNotification.this.onError.call(e));
            } catch (Throwable e2) {
                Exceptions.throwOrReport(e2, this.o);
            }
        }

        public void onNext(T t) {
            try {
                this.emitter.offer(OperatorMapNotification.this.onNext.call(t));
            } catch (Throwable e) {
                Exceptions.throwOrReport(e, this.o, t);
            }
        }
    }

    static final class SingleEmitter<T> extends AtomicLong implements Producer, Subscription {
        private static final long serialVersionUID = -249869671366010660L;
        final Subscription cancel;
        final Subscriber<? super T> child;
        volatile boolean complete;
        boolean emitting;
        boolean missed;
        final NotificationLite<T> nl;
        final Producer producer;
        final Queue<Object> queue;

        public SingleEmitter(Subscriber<? super T> child, Producer producer, Subscription cancel) {
            this.child = child;
            this.producer = producer;
            this.cancel = cancel;
            this.queue = UnsafeAccess.isUnsafeAvailable() ? new SpscArrayQueue(2) : new ConcurrentLinkedQueue();
            this.nl = NotificationLite.instance();
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
            this.producer.request(n);
            drain();
        }

        void produced(long n) {
            long r;
            long u;
            do {
                r = get();
                if (r >= 0) {
                    u = r - n;
                    if (u < 0) {
                        throw new IllegalStateException("More produced (" + n + ") than requested (" + r + SocializeConstants.OP_CLOSE_PAREN);
                    }
                } else {
                    return;
                }
            } while (!compareAndSet(r, u));
        }

        public void offer(T value) {
            if (this.queue.offer(value)) {
                drain();
                return;
            }
            this.child.onError(new MissingBackpressureException());
            unsubscribe();
        }

        public void offerAndComplete(T value) {
            if (this.queue.offer(value)) {
                this.complete = true;
                drain();
                return;
            }
            this.child.onError(new MissingBackpressureException());
            unsubscribe();
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        void drain() {
            /*
            r8 = this;
            monitor-enter(r8);
            r6 = r8.emitting;	 Catch:{ all -> 0x0033 }
            if (r6 == 0) goto L_0x000a;
        L_0x0005:
            r6 = 1;
            r8.missed = r6;	 Catch:{ all -> 0x0033 }
            monitor-exit(r8);	 Catch:{ all -> 0x0033 }
        L_0x0009:
            return;
        L_0x000a:
            r6 = 1;
            r8.emitting = r6;	 Catch:{ all -> 0x0033 }
            r6 = 0;
            r8.missed = r6;	 Catch:{ all -> 0x0033 }
            monitor-exit(r8);	 Catch:{ all -> 0x0033 }
            r4 = 0;
        L_0x0012:
            r2 = r8.get();	 Catch:{ all -> 0x0084 }
            r0 = r8.complete;	 Catch:{ all -> 0x0084 }
            r6 = r8.queue;	 Catch:{ all -> 0x0084 }
            r1 = r6.isEmpty();	 Catch:{ all -> 0x0084 }
            if (r0 == 0) goto L_0x0036;
        L_0x0020:
            if (r1 == 0) goto L_0x0036;
        L_0x0022:
            r6 = r8.child;	 Catch:{ all -> 0x0084 }
            r6.onCompleted();	 Catch:{ all -> 0x0084 }
            r4 = 1;
            if (r4 != 0) goto L_0x0009;
        L_0x002a:
            monitor-enter(r8);
            r6 = 0;
            r8.emitting = r6;	 Catch:{ all -> 0x0030 }
            monitor-exit(r8);	 Catch:{ all -> 0x0030 }
            goto L_0x0009;
        L_0x0030:
            r6 = move-exception;
            monitor-exit(r8);	 Catch:{ all -> 0x0030 }
            throw r6;
        L_0x0033:
            r6 = move-exception;
            monitor-exit(r8);	 Catch:{ all -> 0x0033 }
            throw r6;
        L_0x0036:
            r6 = 0;
            r6 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1));
            if (r6 <= 0) goto L_0x0054;
        L_0x003c:
            r6 = r8.queue;	 Catch:{ all -> 0x0084 }
            r5 = r6.poll();	 Catch:{ all -> 0x0084 }
            if (r5 == 0) goto L_0x0069;
        L_0x0044:
            r6 = r8.child;	 Catch:{ all -> 0x0084 }
            r7 = r8.nl;	 Catch:{ all -> 0x0084 }
            r7 = r7.getValue(r5);	 Catch:{ all -> 0x0084 }
            r6.onNext(r7);	 Catch:{ all -> 0x0084 }
            r6 = 1;
            r8.produced(r6);	 Catch:{ all -> 0x0084 }
        L_0x0054:
            monitor-enter(r8);	 Catch:{ all -> 0x0084 }
            r6 = r8.missed;	 Catch:{ all -> 0x0081 }
            if (r6 != 0) goto L_0x007c;
        L_0x0059:
            r4 = 1;
            r6 = 0;
            r8.emitting = r6;	 Catch:{ all -> 0x0081 }
            monitor-exit(r8);	 Catch:{ all -> 0x0081 }
            if (r4 != 0) goto L_0x0009;
        L_0x0060:
            monitor-enter(r8);
            r6 = 0;
            r8.emitting = r6;	 Catch:{ all -> 0x0066 }
            monitor-exit(r8);	 Catch:{ all -> 0x0066 }
            goto L_0x0009;
        L_0x0066:
            r6 = move-exception;
            monitor-exit(r8);	 Catch:{ all -> 0x0066 }
            throw r6;
        L_0x0069:
            if (r0 == 0) goto L_0x0054;
        L_0x006b:
            r6 = r8.child;	 Catch:{ all -> 0x0084 }
            r6.onCompleted();	 Catch:{ all -> 0x0084 }
            r4 = 1;
            if (r4 != 0) goto L_0x0009;
        L_0x0073:
            monitor-enter(r8);
            r6 = 0;
            r8.emitting = r6;	 Catch:{ all -> 0x0079 }
            monitor-exit(r8);	 Catch:{ all -> 0x0079 }
            goto L_0x0009;
        L_0x0079:
            r6 = move-exception;
            monitor-exit(r8);	 Catch:{ all -> 0x0079 }
            throw r6;
        L_0x007c:
            r6 = 0;
            r8.missed = r6;	 Catch:{ all -> 0x0081 }
            monitor-exit(r8);	 Catch:{ all -> 0x0081 }
            goto L_0x0012;
        L_0x0081:
            r6 = move-exception;
            monitor-exit(r8);	 Catch:{ all -> 0x0081 }
            throw r6;	 Catch:{ all -> 0x0084 }
        L_0x0084:
            r6 = move-exception;
            if (r4 != 0) goto L_0x008c;
        L_0x0087:
            monitor-enter(r8);
            r7 = 0;
            r8.emitting = r7;	 Catch:{ all -> 0x008d }
            monitor-exit(r8);	 Catch:{ all -> 0x008d }
        L_0x008c:
            throw r6;
        L_0x008d:
            r6 = move-exception;
            monitor-exit(r8);	 Catch:{ all -> 0x008d }
            throw r6;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorMapNotification.SingleEmitter.drain():void");
        }

        public boolean isUnsubscribed() {
            return get() < 0;
        }

        public void unsubscribe() {
            if (get() != Long.MIN_VALUE && getAndSet(Long.MIN_VALUE) != Long.MIN_VALUE) {
                this.cancel.unsubscribe();
            }
        }
    }

    public OperatorMapNotification(Func1<? super T, ? extends R> onNext, Func1<? super Throwable, ? extends R> onError, Func0<? extends R> onCompleted) {
        this.onNext = onNext;
        this.onError = onError;
        this.onCompleted = onCompleted;
    }

    public Subscriber<? super T> call(Subscriber<? super R> o) {
        MapNotificationSubscriber subscriber = new MapNotificationSubscriber(new ProducerArbiter(), o);
        o.add(subscriber);
        subscriber.init();
        return subscriber;
    }
}
