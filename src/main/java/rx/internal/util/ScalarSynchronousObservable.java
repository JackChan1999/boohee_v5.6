package rx.internal.util;

import rx.Observable;
import rx.Observable$OnSubscribe;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.internal.schedulers.EventLoopsScheduler;

public final class ScalarSynchronousObservable<T> extends Observable<T> {
    private final T t;

    class AnonymousClass1 implements Observable$OnSubscribe<T> {
        final /* synthetic */ Object val$t;

        AnonymousClass1(Object obj) {
            this.val$t = obj;
        }

        public void call(Subscriber<? super T> s) {
            s.onNext(this.val$t);
            s.onCompleted();
        }
    }

    static final class DirectScheduledEmission<T> implements Observable$OnSubscribe<T> {
        private final EventLoopsScheduler es;
        private final T value;

        DirectScheduledEmission(EventLoopsScheduler es, T value) {
            this.es = es;
            this.value = value;
        }

        public void call(Subscriber<? super T> child) {
            child.add(this.es.scheduleDirect(new ScalarSynchronousAction(child, this.value)));
        }
    }

    static final class NormalScheduledEmission<T> implements Observable$OnSubscribe<T> {
        private final Scheduler scheduler;
        private final T value;

        NormalScheduledEmission(Scheduler scheduler, T value) {
            this.scheduler = scheduler;
            this.value = value;
        }

        public void call(Subscriber<? super T> subscriber) {
            Worker worker = this.scheduler.createWorker();
            subscriber.add(worker);
            worker.schedule(new ScalarSynchronousAction(subscriber, this.value));
        }
    }

    static final class ScalarSynchronousAction<T> implements Action0 {
        private final Subscriber<? super T> subscriber;
        private final T value;

        private ScalarSynchronousAction(Subscriber<? super T> subscriber, T value) {
            this.subscriber = subscriber;
            this.value = value;
        }

        public void call() {
            try {
                this.subscriber.onNext(this.value);
                this.subscriber.onCompleted();
            } catch (Throwable t) {
                this.subscriber.onError(t);
            }
        }
    }

    public static final <T> ScalarSynchronousObservable<T> create(T t) {
        return new ScalarSynchronousObservable(t);
    }

    protected ScalarSynchronousObservable(T t) {
        super(new AnonymousClass1(t));
        this.t = t;
    }

    public T get() {
        return this.t;
    }

    public Observable<T> scalarScheduleOn(Scheduler scheduler) {
        if (scheduler instanceof EventLoopsScheduler) {
            return create(new DirectScheduledEmission((EventLoopsScheduler) scheduler, this.t));
        }
        return create(new NormalScheduledEmission(scheduler, this.t));
    }

    public <R> Observable<R> scalarFlatMap(final Func1<? super T, ? extends Observable<? extends R>> func) {
        return create(new Observable$OnSubscribe<R>() {
            public void call(final Subscriber<? super R> child) {
                Observable<? extends R> o = (Observable) func.call(ScalarSynchronousObservable.this.t);
                if (o.getClass() == ScalarSynchronousObservable.class) {
                    child.onNext(((ScalarSynchronousObservable) o).t);
                    child.onCompleted();
                    return;
                }
                o.unsafeSubscribe(new Subscriber<R>(child) {
                    public void onNext(R v) {
                        child.onNext(v);
                    }

                    public void onError(Throwable e) {
                        child.onError(e);
                    }

                    public void onCompleted() {
                        child.onCompleted();
                    }
                });
            }
        });
    }
}
