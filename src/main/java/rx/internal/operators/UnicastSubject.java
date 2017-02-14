package rx.internal.operators;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import rx.Observable$OnSubscribe;
import rx.Observer;
import rx.Producer;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.exceptions.OnErrorThrowable;
import rx.functions.Action0;
import rx.internal.util.atomic.SpscLinkedAtomicQueue;
import rx.internal.util.atomic.SpscUnboundedAtomicArrayQueue;
import rx.internal.util.unsafe.SpscLinkedQueue;
import rx.internal.util.unsafe.SpscUnboundedArrayQueue;
import rx.internal.util.unsafe.UnsafeAccess;
import rx.subjects.Subject;
import rx.subscriptions.Subscriptions;

public final class UnicastSubject<T> extends Subject<T, T> {
    final State<T> state;

    static final class State<T> extends AtomicLong implements Producer, Observer<T>, Action0, Observable$OnSubscribe<T> {
        private static final long serialVersionUID = -9044104859202255786L;
        volatile boolean caughtUp;
        volatile boolean done;
        boolean emitting;
        Throwable error;
        boolean missed;
        final NotificationLite<T> nl = NotificationLite.instance();
        final Queue<Object> queue;
        final AtomicReference<Subscriber<? super T>> subscriber = new AtomicReference();

        public State(int capacityHint) {
            Queue<Object> q = capacityHint > 1 ? UnsafeAccess.isUnsafeAvailable() ? new SpscUnboundedArrayQueue(capacityHint) : new SpscUnboundedAtomicArrayQueue(capacityHint) : UnsafeAccess.isUnsafeAvailable() ? new SpscLinkedQueue() : new SpscLinkedAtomicQueue();
            this.queue = q;
        }

        public void onNext(T t) {
            if (!this.done) {
                if (!this.caughtUp) {
                    boolean stillReplay = false;
                    synchronized (this) {
                        if (!this.caughtUp) {
                            this.queue.offer(this.nl.next(t));
                            stillReplay = true;
                        }
                    }
                    if (stillReplay) {
                        replay();
                        return;
                    }
                }
                Subscriber<? super T> s = (Subscriber) this.subscriber.get();
                try {
                    s.onNext(t);
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    s.onError(OnErrorThrowable.addValueAsLastCause(ex, t));
                }
            }
        }

        public void onError(Throwable e) {
            if (!this.done) {
                this.error = e;
                this.done = true;
                if (!this.caughtUp) {
                    boolean stillReplay;
                    synchronized (this) {
                        stillReplay = !this.caughtUp;
                    }
                    if (stillReplay) {
                        replay();
                        return;
                    }
                }
                ((Subscriber) this.subscriber.get()).onError(e);
            }
        }

        public void onCompleted() {
            if (!this.done) {
                this.done = true;
                if (!this.caughtUp) {
                    boolean stillReplay;
                    synchronized (this) {
                        stillReplay = !this.caughtUp;
                    }
                    if (stillReplay) {
                        replay();
                        return;
                    }
                }
                ((Subscriber) this.subscriber.get()).onCompleted();
            }
        }

        public void request(long n) {
            if (n < 0) {
                throw new IllegalArgumentException("n >= 0 required");
            } else if (n > 0) {
                BackpressureUtils.getAndAddRequest(this, n);
                replay();
            } else if (this.done) {
                replay();
            }
        }

        public void call(Subscriber<? super T> subscriber) {
            if (this.subscriber.compareAndSet(null, subscriber)) {
                subscriber.add(Subscriptions.create(this));
                subscriber.setProducer(this);
                return;
            }
            subscriber.onError(new IllegalStateException("Only a single subscriber is allowed"));
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        void replay() {
            /*
            r14 = this;
            monitor-enter(r14);
            r12 = r14.emitting;	 Catch:{ all -> 0x0072 }
            if (r12 == 0) goto L_0x000a;
        L_0x0005:
            r12 = 1;
            r14.missed = r12;	 Catch:{ all -> 0x0072 }
            monitor-exit(r14);	 Catch:{ all -> 0x0072 }
        L_0x0009:
            return;
        L_0x000a:
            r12 = 1;
            r14.emitting = r12;	 Catch:{ all -> 0x0072 }
            monitor-exit(r14);	 Catch:{ all -> 0x0072 }
            r5 = r14.queue;
        L_0x0010:
            r12 = r14.subscriber;
            r8 = r12.get();
            r8 = (rx.Subscriber) r8;
            r9 = 0;
            if (r8 == 0) goto L_0x005a;
        L_0x001b:
            r0 = r14.done;
            r1 = r5.isEmpty();
            r12 = r14.checkTerminated(r0, r1, r8);
            if (r12 != 0) goto L_0x0009;
        L_0x0027:
            r6 = r14.get();
            r12 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
            r12 = (r6 > r12 ? 1 : (r6 == r12 ? 0 : -1));
            if (r12 != 0) goto L_0x0075;
        L_0x0034:
            r9 = 1;
        L_0x0035:
            r2 = 0;
        L_0x0037:
            r12 = 0;
            r12 = (r6 > r12 ? 1 : (r6 == r12 ? 0 : -1));
            if (r12 == 0) goto L_0x004e;
        L_0x003d:
            r0 = r14.done;
            r10 = r5.poll();
            if (r10 != 0) goto L_0x0077;
        L_0x0045:
            r1 = 1;
        L_0x0046:
            r12 = r14.checkTerminated(r0, r1, r8);
            if (r12 != 0) goto L_0x0009;
        L_0x004c:
            if (r1 == 0) goto L_0x0079;
        L_0x004e:
            if (r9 != 0) goto L_0x005a;
        L_0x0050:
            r12 = 0;
            r12 = (r2 > r12 ? 1 : (r2 == r12 ? 0 : -1));
            if (r12 == 0) goto L_0x005a;
        L_0x0056:
            r12 = -r2;
            r14.addAndGet(r12);
        L_0x005a:
            monitor-enter(r14);
            r12 = r14.missed;	 Catch:{ all -> 0x006f }
            if (r12 != 0) goto L_0x0099;
        L_0x005f:
            if (r9 == 0) goto L_0x006a;
        L_0x0061:
            r12 = r5.isEmpty();	 Catch:{ all -> 0x006f }
            if (r12 == 0) goto L_0x006a;
        L_0x0067:
            r12 = 1;
            r14.caughtUp = r12;	 Catch:{ all -> 0x006f }
        L_0x006a:
            r12 = 0;
            r14.emitting = r12;	 Catch:{ all -> 0x006f }
            monitor-exit(r14);	 Catch:{ all -> 0x006f }
            goto L_0x0009;
        L_0x006f:
            r12 = move-exception;
            monitor-exit(r14);	 Catch:{ all -> 0x006f }
            throw r12;
        L_0x0072:
            r12 = move-exception;
            monitor-exit(r14);	 Catch:{ all -> 0x0072 }
            throw r12;
        L_0x0075:
            r9 = 0;
            goto L_0x0035;
        L_0x0077:
            r1 = 0;
            goto L_0x0046;
        L_0x0079:
            r12 = r14.nl;
            r11 = r12.getValue(r10);
            r8.onNext(r11);	 Catch:{ Throwable -> 0x0089 }
            r12 = 1;
            r6 = r6 - r12;
            r12 = 1;
            r2 = r2 + r12;
            goto L_0x0037;
        L_0x0089:
            r4 = move-exception;
            r5.clear();
            rx.exceptions.Exceptions.throwIfFatal(r4);
            r12 = rx.exceptions.OnErrorThrowable.addValueAsLastCause(r4, r11);
            r8.onError(r12);
            goto L_0x0009;
        L_0x0099:
            r12 = 0;
            r14.missed = r12;	 Catch:{ all -> 0x006f }
            monitor-exit(r14);	 Catch:{ all -> 0x006f }
            goto L_0x0010;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.UnicastSubject.State.replay():void");
        }

        public void call() {
            this.done = true;
            synchronized (this) {
                if (this.emitting) {
                    return;
                }
                this.emitting = true;
                this.queue.clear();
            }
        }

        boolean checkTerminated(boolean done, boolean empty, Subscriber<? super T> s) {
            if (s.isUnsubscribed()) {
                this.queue.clear();
                return true;
            }
            if (done) {
                Throwable e = this.error;
                if (e != null) {
                    this.queue.clear();
                    s.onError(e);
                    return true;
                } else if (empty) {
                    s.onCompleted();
                    return true;
                }
            }
            return false;
        }
    }

    public static <T> UnicastSubject<T> create() {
        return create(16);
    }

    public static <T> UnicastSubject<T> create(int capacityHint) {
        return new UnicastSubject(new State(capacityHint));
    }

    private UnicastSubject(State<T> state) {
        super(state);
        this.state = state;
    }

    public void onNext(T t) {
        this.state.onNext(t);
    }

    public void onError(Throwable e) {
        this.state.onError(e);
    }

    public void onCompleted() {
        this.state.onCompleted();
    }

    public boolean hasObservers() {
        return this.state.subscriber.get() != null;
    }
}
