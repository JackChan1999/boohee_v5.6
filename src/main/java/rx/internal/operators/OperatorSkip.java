package rx.internal.operators;

import rx.Observable$Operator;
import rx.Producer;
import rx.Subscriber;

public final class OperatorSkip<T> implements Observable$Operator<T, T> {
    final int toSkip;

    public OperatorSkip(int n) {
        this.toSkip = n;
    }

    public Subscriber<? super T> call(final Subscriber<? super T> child) {
        return new Subscriber<T>(child) {
            int skipped = 0;

            public void onCompleted() {
                child.onCompleted();
            }

            public void onError(Throwable e) {
                child.onError(e);
            }

            public void onNext(T t) {
                if (this.skipped >= OperatorSkip.this.toSkip) {
                    child.onNext(t);
                } else {
                    this.skipped++;
                }
            }

            public void setProducer(Producer producer) {
                child.setProducer(producer);
                producer.request((long) OperatorSkip.this.toSkip);
            }
        };
    }
}
