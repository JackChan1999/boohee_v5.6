package rx.internal.operators;

import rx.Observable$Operator;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func1;
import rx.functions.Func2;

public final class OperatorTakeWhile<T> implements Observable$Operator<T, T> {
    private final Func2<? super T, ? super Integer, Boolean> predicate;

    class AnonymousClass1 implements Func2<T, Integer, Boolean> {
        final /* synthetic */ Func1 val$underlying;

        AnonymousClass1(Func1 func1) {
            this.val$underlying = func1;
        }

        public Boolean call(T input, Integer index) {
            return (Boolean) this.val$underlying.call(input);
        }
    }

    public OperatorTakeWhile(Func1<? super T, Boolean> underlying) {
        this(new AnonymousClass1(underlying));
    }

    public OperatorTakeWhile(Func2<? super T, ? super Integer, Boolean> predicate) {
        this.predicate = predicate;
    }

    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        Subscriber<T> s = new Subscriber<T>(false, subscriber) {
            private int counter = 0;
            private boolean done = false;

            public void onNext(T t) {
                try {
                    Func2 access$000 = OperatorTakeWhile.this.predicate;
                    int i = this.counter;
                    this.counter = i + 1;
                    if (((Boolean) access$000.call(t, Integer.valueOf(i))).booleanValue()) {
                        subscriber.onNext(t);
                        return;
                    }
                    this.done = true;
                    subscriber.onCompleted();
                    unsubscribe();
                } catch (Throwable e) {
                    this.done = true;
                    Exceptions.throwOrReport(e, subscriber, t);
                    unsubscribe();
                }
            }

            public void onCompleted() {
                if (!this.done) {
                    subscriber.onCompleted();
                }
            }

            public void onError(Throwable e) {
                if (!this.done) {
                    subscriber.onError(e);
                }
            }
        };
        subscriber.add(s);
        return s;
    }
}
