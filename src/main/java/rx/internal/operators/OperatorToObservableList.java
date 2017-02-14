package rx.internal.operators;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import rx.Observable$Operator;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.internal.producers.SingleDelayedProducer;

public final class OperatorToObservableList<T> implements Observable$Operator<List<T>, T> {

    private static final class Holder {
        static final OperatorToObservableList<Object> INSTANCE = new OperatorToObservableList();

        private Holder() {
        }
    }

    public static <T> OperatorToObservableList<T> instance() {
        return Holder.INSTANCE;
    }

    private OperatorToObservableList() {
    }

    public Subscriber<? super T> call(final Subscriber<? super List<T>> o) {
        final SingleDelayedProducer<List<T>> producer = new SingleDelayedProducer(o);
        Subscriber<T> result = new Subscriber<T>() {
            boolean completed = false;
            List<T> list = new LinkedList();

            public void onStart() {
                request(Long.MAX_VALUE);
            }

            public void onCompleted() {
                if (!this.completed) {
                    this.completed = true;
                    try {
                        List<T> result = new ArrayList(this.list);
                        this.list = null;
                        producer.setValue(result);
                    } catch (Throwable t) {
                        Exceptions.throwOrReport(t, this);
                    }
                }
            }

            public void onError(Throwable e) {
                o.onError(e);
            }

            public void onNext(T value) {
                if (!this.completed) {
                    this.list.add(value);
                }
            }
        };
        o.add(result);
        o.setProducer(producer);
        return result;
    }
}
