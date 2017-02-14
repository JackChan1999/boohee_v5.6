package rx.internal.operators;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;
import rx.Observable$Operator;
import rx.Observer;
import rx.Producer;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.exceptions.OnErrorThrowable;
import rx.functions.Func0;
import rx.functions.Func2;
import rx.internal.util.atomic.SpscLinkedAtomicQueue;
import rx.internal.util.unsafe.SpscLinkedQueue;
import rx.internal.util.unsafe.UnsafeAccess;

public final class OperatorScan<R, T> implements Observable$Operator<R, T> {
    private static final Object NO_INITIAL_VALUE = new Object();
    private final Func2<R, ? super T, R> accumulator;
    private final Func0<R> initialValueFactory;

    class AnonymousClass1 implements Func0<R> {
        final /* synthetic */ Object val$initialValue;

        AnonymousClass1(Object obj) {
            this.val$initialValue = obj;
        }

        public R call() {
            return this.val$initialValue;
        }
    }

    static final class InitialProducer<R> implements Producer, Observer<R> {
        final Subscriber<? super R> child;
        volatile boolean done;
        boolean emitting;
        Throwable error;
        boolean missed;
        long missedRequested;
        volatile Producer producer;
        final Queue<Object> queue;
        final AtomicLong requested;

        public InitialProducer(R initialValue, Subscriber<? super R> child) {
            Queue<Object> q;
            this.child = child;
            if (UnsafeAccess.isUnsafeAvailable()) {
                q = new SpscLinkedQueue();
            } else {
                q = new SpscLinkedAtomicQueue();
            }
            this.queue = q;
            q.offer(NotificationLite.instance().next(initialValue));
            this.requested = new AtomicLong();
        }

        public void onNext(R t) {
            this.queue.offer(NotificationLite.instance().next(t));
            emit();
        }

        boolean checkTerminated(boolean d, boolean empty, Subscriber<? super R> child) {
            if (child.isUnsubscribed()) {
                return true;
            }
            if (d) {
                Throwable err = this.error;
                if (err != null) {
                    child.onError(err);
                    return true;
                } else if (empty) {
                    child.onCompleted();
                    return true;
                }
            }
            return false;
        }

        public void onError(Throwable e) {
            this.error = e;
            this.done = true;
            emit();
        }

        public void onCompleted() {
            this.done = true;
            emit();
        }

        public void request(long n) {
            if (n < 0) {
                throw new IllegalArgumentException("n >= required but it was " + n);
            } else if (n != 0) {
                BackpressureUtils.getAndAddRequest(this.requested, n);
                Producer p = this.producer;
                if (p == null) {
                    synchronized (this.requested) {
                        p = this.producer;
                        if (p == null) {
                            this.missedRequested = BackpressureUtils.addCap(this.missedRequested, n);
                        }
                    }
                }
                if (p != null) {
                    p.request(n);
                }
                emit();
            }
        }

        public void setProducer(Producer p) {
            if (p == null) {
                throw new NullPointerException();
            }
            synchronized (this.requested) {
                if (this.producer != null) {
                    throw new IllegalStateException("Can't set more than one Producer!");
                }
                long mr = this.missedRequested - 1;
                this.missedRequested = 0;
                this.producer = p;
            }
            if (mr > 0) {
                p.request(mr);
            }
            emit();
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

        void emitLoop() {
            Subscriber<? super R> child = this.child;
            Queue<Object> queue = this.queue;
            NotificationLite<R> nl = NotificationLite.instance();
            AtomicLong requested = this.requested;
            long r = requested.get();
            while (true) {
                boolean max = r == Long.MAX_VALUE;
                if (!checkTerminated(this.done, queue.isEmpty(), child)) {
                    long e = 0;
                    while (r != 0) {
                        boolean d = this.done;
                        Object o = queue.poll();
                        boolean empty = o == null;
                        if (!checkTerminated(d, empty, child)) {
                            if (empty) {
                                break;
                            }
                            R v = nl.getValue(o);
                            try {
                                child.onNext(v);
                                r--;
                                e--;
                            } catch (Throwable ex) {
                                Exceptions.throwIfFatal(ex);
                                child.onError(OnErrorThrowable.addValueAsLastCause(ex, v));
                                return;
                            }
                        }
                        return;
                    }
                    if (!(e == 0 || max)) {
                        r = requested.addAndGet(e);
                    }
                    synchronized (this) {
                        if (this.missed) {
                            this.missed = false;
                        } else {
                            this.emitting = false;
                            return;
                        }
                    }
                }
                return;
            }
        }
    }

    public OperatorScan(R initialValue, Func2<R, ? super T, R> accumulator) {
        this(new AnonymousClass1(initialValue), (Func2) accumulator);
    }

    public OperatorScan(Func0<R> initialValueFactory, Func2<R, ? super T, R> accumulator) {
        this.initialValueFactory = initialValueFactory;
        this.accumulator = accumulator;
    }

    public OperatorScan(Func2<R, ? super T, R> accumulator) {
        this(NO_INITIAL_VALUE, (Func2) accumulator);
    }

    public Subscriber<? super T> call(final Subscriber<? super R> child) {
        final R initialValue = this.initialValueFactory.call();
        if (initialValue == NO_INITIAL_VALUE) {
            return new Subscriber<T>(child) {
                boolean once;
                R value;

                public void onNext(T t) {
                    R v;
                    if (this.once) {
                        try {
                            v = OperatorScan.this.accumulator.call(this.value, t);
                        } catch (Throwable e) {
                            Exceptions.throwIfFatal(e);
                            child.onError(OnErrorThrowable.addValueAsLastCause(e, t));
                            return;
                        }
                    }
                    this.once = true;
                    v = t;
                    this.value = v;
                    child.onNext(v);
                }

                public void onError(Throwable e) {
                    child.onError(e);
                }

                public void onCompleted() {
                    child.onCompleted();
                }
            };
        }
        final InitialProducer<R> ip = new InitialProducer(initialValue, child);
        Subscriber<? super T> parent = new Subscriber<T>() {
            private R value = initialValue;

            public void onNext(T currentValue) {
                try {
                    R v = OperatorScan.this.accumulator.call(this.value, currentValue);
                    this.value = v;
                    ip.onNext(v);
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    onError(OnErrorThrowable.addValueAsLastCause(e, currentValue));
                }
            }

            public void onError(Throwable e) {
                ip.onError(e);
            }

            public void onCompleted() {
                ip.onCompleted();
            }

            public void setProducer(Producer producer) {
                ip.setProducer(producer);
            }
        };
        child.add(parent);
        child.setProducer(ip);
        return parent;
    }
}
