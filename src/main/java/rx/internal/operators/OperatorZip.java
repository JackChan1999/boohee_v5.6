package rx.internal.operators;

import com.baidu.location.l.b;
import java.util.concurrent.atomic.AtomicLong;
import rx.Observable;
import rx.Observable$Operator;
import rx.Observer;
import rx.Producer;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.exceptions.MissingBackpressureException;
import rx.functions.Func2;
import rx.functions.Func3;
import rx.functions.Func4;
import rx.functions.Func5;
import rx.functions.Func6;
import rx.functions.Func7;
import rx.functions.Func8;
import rx.functions.Func9;
import rx.functions.FuncN;
import rx.functions.Functions;
import rx.internal.util.RxRingBuffer;
import rx.subscriptions.CompositeSubscription;

public final class OperatorZip<R> implements Observable$Operator<R, Observable<?>[]> {
    final FuncN<? extends R> zipFunction;

    private static final class Zip<R> extends AtomicLong {
        static final int THRESHOLD = ((int) (((double) RxRingBuffer.SIZE) * b.if));
        private final Observer<? super R> child;
        private final CompositeSubscription childSubscription = new CompositeSubscription();
        int emitted = 0;
        private Object[] observers;
        private AtomicLong requested;
        private final FuncN<? extends R> zipFunction;

        final class InnerSubscriber extends Subscriber {
            final RxRingBuffer items = RxRingBuffer.getSpmcInstance();

            InnerSubscriber() {
            }

            public void onStart() {
                request((long) RxRingBuffer.SIZE);
            }

            public void requestMore(long n) {
                request(n);
            }

            public void onCompleted() {
                this.items.onCompleted();
                Zip.this.tick();
            }

            public void onError(Throwable e) {
                Zip.this.child.onError(e);
            }

            public void onNext(Object t) {
                try {
                    this.items.onNext(t);
                } catch (MissingBackpressureException e) {
                    onError(e);
                }
                Zip.this.tick();
            }
        }

        public Zip(Subscriber<? super R> child, FuncN<? extends R> zipFunction) {
            this.child = child;
            this.zipFunction = zipFunction;
            child.add(this.childSubscription);
        }

        public void start(Observable[] os, AtomicLong requested) {
            int i;
            this.observers = new Object[os.length];
            this.requested = requested;
            for (i = 0; i < os.length; i++) {
                InnerSubscriber io = new InnerSubscriber();
                this.observers[i] = io;
                this.childSubscription.add(io);
            }
            for (i = 0; i < os.length; i++) {
                os[i].unsafeSubscribe((InnerSubscriber) this.observers[i]);
            }
        }

        void tick() {
            Object[] observers = this.observers;
            if (observers != null && getAndIncrement() == 0) {
                int length = observers.length;
                Observer<? super R> child = this.child;
                AtomicLong requested = this.requested;
                while (true) {
                    RxRingBuffer buffer;
                    Object[] vs = new Object[length];
                    boolean allHaveValues = true;
                    for (int i = 0; i < length; i++) {
                        buffer = ((InnerSubscriber) observers[i]).items;
                        Object n = buffer.peek();
                        if (n == null) {
                            allHaveValues = false;
                        } else if (buffer.isCompleted(n)) {
                            child.onCompleted();
                            this.childSubscription.unsubscribe();
                            return;
                        } else {
                            vs[i] = buffer.getValue(n);
                        }
                    }
                    if (requested.get() > 0 && allHaveValues) {
                        try {
                            child.onNext(this.zipFunction.call(vs));
                            requested.decrementAndGet();
                            this.emitted++;
                            for (Object obj : observers) {
                                buffer = ((InnerSubscriber) obj).items;
                                buffer.poll();
                                if (buffer.isCompleted(buffer.peek())) {
                                    child.onCompleted();
                                    this.childSubscription.unsubscribe();
                                    return;
                                }
                            }
                            if (this.emitted > THRESHOLD) {
                                for (Object obj2 : observers) {
                                    ((InnerSubscriber) obj2).requestMore((long) this.emitted);
                                }
                                this.emitted = 0;
                            }
                        } catch (Throwable e) {
                            Exceptions.throwOrReport(e, child, vs);
                            return;
                        }
                    } else if (decrementAndGet() <= 0) {
                        return;
                    }
                }
            }
        }
    }

    private static final class ZipProducer<R> extends AtomicLong implements Producer {
        private static final long serialVersionUID = -1216676403723546796L;
        private Zip<R> zipper;

        public ZipProducer(Zip<R> zipper) {
            this.zipper = zipper;
        }

        public void request(long n) {
            BackpressureUtils.getAndAddRequest(this, n);
            this.zipper.tick();
        }
    }

    private final class ZipSubscriber extends Subscriber<Observable[]> {
        final Subscriber<? super R> child;
        final ZipProducer<R> producer;
        boolean started = false;
        final Zip<R> zipper;

        public ZipSubscriber(Subscriber<? super R> child, Zip<R> zipper, ZipProducer<R> producer) {
            this.child = child;
            this.zipper = zipper;
            this.producer = producer;
        }

        public void onCompleted() {
            if (!this.started) {
                this.child.onCompleted();
            }
        }

        public void onError(Throwable e) {
            this.child.onError(e);
        }

        public void onNext(Observable[] observables) {
            if (observables == null || observables.length == 0) {
                this.child.onCompleted();
                return;
            }
            this.started = true;
            this.zipper.start(observables, this.producer);
        }
    }

    public OperatorZip(FuncN<? extends R> f) {
        this.zipFunction = f;
    }

    public OperatorZip(Func2 f) {
        this.zipFunction = Functions.fromFunc(f);
    }

    public OperatorZip(Func3 f) {
        this.zipFunction = Functions.fromFunc(f);
    }

    public OperatorZip(Func4 f) {
        this.zipFunction = Functions.fromFunc(f);
    }

    public OperatorZip(Func5 f) {
        this.zipFunction = Functions.fromFunc(f);
    }

    public OperatorZip(Func6 f) {
        this.zipFunction = Functions.fromFunc(f);
    }

    public OperatorZip(Func7 f) {
        this.zipFunction = Functions.fromFunc(f);
    }

    public OperatorZip(Func8 f) {
        this.zipFunction = Functions.fromFunc(f);
    }

    public OperatorZip(Func9 f) {
        this.zipFunction = Functions.fromFunc(f);
    }

    public Subscriber<? super Observable[]> call(Subscriber<? super R> child) {
        Zip<R> zipper = new Zip(child, this.zipFunction);
        ZipProducer<R> producer = new ZipProducer(zipper);
        ZipSubscriber subscriber = new ZipSubscriber(child, zipper, producer);
        child.add(subscriber);
        child.setProducer(producer);
        return subscriber;
    }
}
