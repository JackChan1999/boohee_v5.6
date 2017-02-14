package rx.internal.operators;

import rx.Observable;
import rx.Observable$Operator;
import rx.Producer;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.plugins.RxJavaPlugins;

public final class OperatorOnErrorResumeNextViaObservable<T> implements Observable$Operator<T, T> {
    final Observable<? extends T> resumeSequence;

    public OperatorOnErrorResumeNextViaObservable(Observable<? extends T> resumeSequence) {
        this.resumeSequence = resumeSequence;
    }

    public Subscriber<? super T> call(final Subscriber<? super T> child) {
        Subscriber<T> s = new Subscriber<T>() {
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
                RxJavaPlugins.getInstance().getErrorHandler().handleError(e);
                unsubscribe();
                OperatorOnErrorResumeNextViaObservable.this.resumeSequence.unsafeSubscribe(child);
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
        child.add(s);
        return s;
    }
}
