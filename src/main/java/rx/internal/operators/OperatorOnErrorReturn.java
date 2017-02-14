package rx.internal.operators;

import java.util.Arrays;
import rx.Observable$Operator;
import rx.Producer;
import rx.Subscriber;
import rx.exceptions.CompositeException;
import rx.exceptions.Exceptions;
import rx.functions.Func1;
import rx.plugins.RxJavaPlugins;

public final class OperatorOnErrorReturn<T> implements Observable$Operator<T, T> {
    final Func1<Throwable, ? extends T> resultFunction;

    public OperatorOnErrorReturn(Func1<Throwable, ? extends T> resultFunction) {
        this.resultFunction = resultFunction;
    }

    public Subscriber<? super T> call(final Subscriber<? super T> child) {
        Subscriber<T> parent = new Subscriber<T>() {
            private boolean done = false;

            public void onNext(T t) {
                if (!this.done) {
                    child.onNext(t);
                }
            }

            public void onError(Throwable e) {
                if (this.done) {
                    Exceptions.throwIfFatal(e);
                    return;
                }
                this.done = true;
                try {
                    RxJavaPlugins.getInstance().getErrorHandler().handleError(e);
                    unsubscribe();
                    child.onNext(OperatorOnErrorReturn.this.resultFunction.call(e));
                    child.onCompleted();
                } catch (Throwable x) {
                    Exceptions.throwIfFatal(x);
                    child.onError(new CompositeException(Arrays.asList(new Throwable[]{e, x})));
                }
            }

            public void onCompleted() {
                if (!this.done) {
                    this.done = true;
                    child.onCompleted();
                }
            }

            public void setProducer(final Producer producer) {
                child.setProducer(new Producer() {
                    public void request(long n) {
                        producer.request(n);
                    }
                });
            }
        };
        child.add(parent);
        return parent;
    }
}
