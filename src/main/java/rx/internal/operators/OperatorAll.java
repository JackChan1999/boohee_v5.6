package rx.internal.operators;

import rx.Observable$Operator;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func1;
import rx.internal.producers.SingleDelayedProducer;

public final class OperatorAll<T> implements Observable$Operator<Boolean, T> {
    private final Func1<? super T, Boolean> predicate;

    public OperatorAll(Func1<? super T, Boolean> predicate) {
        this.predicate = predicate;
    }

    public Subscriber<? super T> call(final Subscriber<? super Boolean> child) {
        final SingleDelayedProducer<Boolean> producer = new SingleDelayedProducer(child);
        Subscriber<T> s = new Subscriber<T>() {
            boolean done;

            public void onNext(T t) {
                try {
                    if (!((Boolean) OperatorAll.this.predicate.call(t)).booleanValue() && !this.done) {
                        this.done = true;
                        producer.setValue(Boolean.valueOf(false));
                        unsubscribe();
                    }
                } catch (Throwable e) {
                    Exceptions.throwOrReport(e, this, t);
                }
            }

            public void onError(Throwable e) {
                child.onError(e);
            }

            public void onCompleted() {
                if (!this.done) {
                    this.done = true;
                    producer.setValue(Boolean.valueOf(true));
                }
            }
        };
        child.add(s);
        child.setProducer(producer);
        return s;
    }
}
