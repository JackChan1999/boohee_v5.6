package rx.subjects;

import java.util.concurrent.TimeUnit;
import rx.Observable$OnSubscribe;
import rx.Observer;
import rx.Scheduler.Worker;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.internal.operators.NotificationLite;
import rx.schedulers.TestScheduler;

public final class TestSubject<T> extends Subject<T, T> {
    private final Worker innerScheduler;
    private final SubjectSubscriptionManager<T> state;

    public static <T> TestSubject<T> create(TestScheduler scheduler) {
        final SubjectSubscriptionManager<T> state = new SubjectSubscriptionManager();
        state.onAdded = new Action1<SubjectObserver<T>>() {
            public void call(SubjectObserver<T> o) {
                o.emitFirst(state.getLatest(), state.nl);
            }
        };
        state.onTerminated = state.onAdded;
        return new TestSubject(state, state, scheduler);
    }

    protected TestSubject(Observable$OnSubscribe<T> onSubscribe, SubjectSubscriptionManager<T> state, TestScheduler scheduler) {
        super(onSubscribe);
        this.state = state;
        this.innerScheduler = scheduler.createWorker();
    }

    public void onCompleted() {
        onCompleted(0);
    }

    private void _onCompleted() {
        if (this.state.active) {
            for (SubjectObserver<T> bo : this.state.terminate(NotificationLite.instance().completed())) {
                bo.onCompleted();
            }
        }
    }

    public void onCompleted(long delayTime) {
        this.innerScheduler.schedule(new Action0() {
            public void call() {
                TestSubject.this._onCompleted();
            }
        }, delayTime, TimeUnit.MILLISECONDS);
    }

    public void onError(Throwable e) {
        onError(e, 0);
    }

    private void _onError(Throwable e) {
        if (this.state.active) {
            for (SubjectObserver<T> bo : this.state.terminate(NotificationLite.instance().error(e))) {
                bo.onError(e);
            }
        }
    }

    public void onError(final Throwable e, long dalayTime) {
        this.innerScheduler.schedule(new Action0() {
            public void call() {
                TestSubject.this._onError(e);
            }
        }, dalayTime, TimeUnit.MILLISECONDS);
    }

    public void onNext(T v) {
        onNext(v, 0);
    }

    private void _onNext(T v) {
        for (Observer<? super T> o : this.state.observers()) {
            o.onNext(v);
        }
    }

    public void onNext(final T v, long delayTime) {
        this.innerScheduler.schedule(new Action0() {
            public void call() {
                TestSubject.this._onNext(v);
            }
        }, delayTime, TimeUnit.MILLISECONDS);
    }

    public boolean hasObservers() {
        return this.state.observers().length > 0;
    }
}
