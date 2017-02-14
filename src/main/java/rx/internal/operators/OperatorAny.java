package rx.internal.operators;

import rx.Observable$Operator;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func1;
import rx.internal.producers.SingleDelayedProducer;

public final class OperatorAny<T> implements Observable$Operator<Boolean, T> {
    private final Func1<? super T, Boolean> predicate;
    private final boolean returnOnEmpty;

    public OperatorAny(Func1<? super T, Boolean> predicate, boolean returnOnEmpty) {
        this.predicate = predicate;
        this.returnOnEmpty = returnOnEmpty;
    }

    public Subscriber<? super T> call(final Subscriber<? super Boolean> child) {
        final SingleDelayedProducer<Boolean> producer = new SingleDelayedProducer(child);
        Subscriber<T> s = new Subscriber<T>() {
            boolean done;
            boolean hasElements;

            public void onNext(T t) {
                this.hasElements = true;
                try {
                    if (((Boolean) OperatorAny.this.predicate.call(t)).booleanValue() && !this.done) {
                        this.done = true;
                        producer.setValue(Boolean.valueOf(!OperatorAny.this.returnOnEmpty));
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
                    if (this.hasElements) {
                        producer.setValue(Boolean.valueOf(false));
                    } else {
                        producer.setValue(Boolean.valueOf(OperatorAny.this.returnOnEmpty));
                    }
                }
            }
        };
        child.add(s);
        child.setProducer(producer);
        return s;
    }
}
