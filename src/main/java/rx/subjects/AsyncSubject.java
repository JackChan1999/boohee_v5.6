package rx.subjects;

import java.util.ArrayList;
import java.util.List;
import rx.Observable$OnSubscribe;
import rx.annotations.Beta;
import rx.exceptions.Exceptions;
import rx.functions.Action1;
import rx.internal.operators.NotificationLite;

public final class AsyncSubject<T> extends Subject<T, T> {
    volatile Object lastValue;
    private final NotificationLite<T> nl = NotificationLite.instance();
    final SubjectSubscriptionManager<T> state;

    public static <T> AsyncSubject<T> create() {
        final SubjectSubscriptionManager<T> state = new SubjectSubscriptionManager();
        state.onTerminated = new Action1<SubjectObserver<T>>() {
            public void call(SubjectObserver<T> o) {
                Object v = state.getLatest();
                NotificationLite<T> nl = state.nl;
                o.accept(v, nl);
                if (v == null || !(nl.isCompleted(v) || nl.isError(v))) {
                    o.onCompleted();
                }
            }
        };
        return new AsyncSubject(state, state);
    }

    protected AsyncSubject(Observable$OnSubscribe<T> onSubscribe, SubjectSubscriptionManager<T> state) {
        super(onSubscribe);
        this.state = state;
    }

    public void onCompleted() {
        if (this.state.active) {
            Object last = this.lastValue;
            if (last == null) {
                last = this.nl.completed();
            }
            for (SubjectObserver<T> bo : this.state.terminate(last)) {
                if (last == this.nl.completed()) {
                    bo.onCompleted();
                } else {
                    bo.onNext(this.nl.getValue(last));
                    bo.onCompleted();
                }
            }
        }
    }

    public void onError(Throwable e) {
        if (this.state.active) {
            List<Throwable> errors = null;
            for (SubjectObserver<T> bo : this.state.terminate(this.nl.error(e))) {
                try {
                    bo.onError(e);
                } catch (Throwable e2) {
                    if (errors == null) {
                        errors = new ArrayList();
                    }
                    errors.add(e2);
                }
            }
            Exceptions.throwIfAny(errors);
        }
    }

    public void onNext(T v) {
        this.lastValue = this.nl.next(v);
    }

    public boolean hasObservers() {
        return this.state.observers().length > 0;
    }

    @Beta
    public boolean hasValue() {
        return !this.nl.isError(this.state.getLatest()) && this.nl.isNext(this.lastValue);
    }

    @Beta
    public boolean hasThrowable() {
        return this.nl.isError(this.state.getLatest());
    }

    @Beta
    public boolean hasCompleted() {
        Object o = this.state.getLatest();
        return (o == null || this.nl.isError(o)) ? false : true;
    }

    @Beta
    public T getValue() {
        Object v = this.lastValue;
        if (this.nl.isError(this.state.getLatest()) || !this.nl.isNext(v)) {
            return null;
        }
        return this.nl.getValue(v);
    }

    @Beta
    public Throwable getThrowable() {
        Object o = this.state.getLatest();
        if (this.nl.isError(o)) {
            return this.nl.getError(o);
        }
        return null;
    }
}
