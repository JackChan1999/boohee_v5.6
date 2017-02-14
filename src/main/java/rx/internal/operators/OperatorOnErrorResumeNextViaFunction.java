package rx.internal.operators;

import rx.Observable;
import rx.Observable$Operator;
import rx.Producer;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func1;
import rx.internal.producers.ProducerArbiter;
import rx.plugins.RxJavaPlugins;
import rx.subscriptions.SerialSubscription;

public final class OperatorOnErrorResumeNextViaFunction<T> implements Observable$Operator<T, T> {
    private final Func1<Throwable, ? extends Observable<? extends T>> resumeFunction;

    public OperatorOnErrorResumeNextViaFunction(Func1<Throwable, ? extends Observable<? extends T>> f) {
        this.resumeFunction = f;
    }

    public Subscriber<? super T> call(final Subscriber<? super T> child) {
        final ProducerArbiter pa = new ProducerArbiter();
        final SerialSubscription ssub = new SerialSubscription();
        Subscriber<T> parent = new Subscriber<T>() {
            private boolean done = false;

            public void onCompleted() {
                if (!this.done) {
                    this.done = true;
                    child.onCompleted();
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
                    Subscriber<T> next = new Subscriber<T>() {
                        public void onNext(T t) {
                            child.onNext(t);
                        }

                        public void onError(Throwable e) {
                            child.onError(e);
                        }

                        public void onCompleted() {
                            child.onCompleted();
                        }

                        public void setProducer(Producer producer) {
                            pa.setProducer(producer);
                        }
                    };
                    ssub.set(next);
                    ((Observable) OperatorOnErrorResumeNextViaFunction.this.resumeFunction.call(e)).unsafeSubscribe(next);
                } catch (Throwable e2) {
                    Exceptions.throwOrReport(e2, child);
                }
            }

            public void onNext(T t) {
                if (!this.done) {
                    child.onNext(t);
                }
            }

            public void setProducer(Producer producer) {
                pa.setProducer(producer);
            }
        };
        child.add(ssub);
        ssub.set(parent);
        child.setProducer(pa);
        return parent;
    }
}
