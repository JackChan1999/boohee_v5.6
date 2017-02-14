package rx.internal.operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import rx.Observable$Operator;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func2;
import rx.internal.producers.SingleDelayedProducer;

public final class OperatorToObservableSortedList<T> implements Observable$Operator<List<T>, T> {
    private static Comparator DEFAULT_SORT_FUNCTION = new DefaultComparableFunction();
    private final int initialCapacity;
    private final Comparator<? super T> sortFunction;

    private static class DefaultComparableFunction implements Comparator<Object> {
        private DefaultComparableFunction() {
        }

        public int compare(Object t1, Object t2) {
            return ((Comparable) t1).compareTo((Comparable) t2);
        }
    }

    public OperatorToObservableSortedList(int initialCapacity) {
        this.sortFunction = DEFAULT_SORT_FUNCTION;
        this.initialCapacity = initialCapacity;
    }

    public OperatorToObservableSortedList(final Func2<? super T, ? super T, Integer> sortFunction, int initialCapacity) {
        this.initialCapacity = initialCapacity;
        this.sortFunction = new Comparator<T>() {
            public int compare(T o1, T o2) {
                return ((Integer) sortFunction.call(o1, o2)).intValue();
            }
        };
    }

    public Subscriber<? super T> call(final Subscriber<? super List<T>> child) {
        final SingleDelayedProducer<List<T>> producer = new SingleDelayedProducer(child);
        Subscriber<T> result = new Subscriber<T>() {
            boolean completed;
            List<T> list = new ArrayList(OperatorToObservableSortedList.this.initialCapacity);

            public void onStart() {
                request(Long.MAX_VALUE);
            }

            public void onCompleted() {
                if (!this.completed) {
                    this.completed = true;
                    List<T> a = this.list;
                    this.list = null;
                    try {
                        Collections.sort(a, OperatorToObservableSortedList.this.sortFunction);
                        producer.setValue(a);
                    } catch (Throwable e) {
                        Exceptions.throwOrReport(e, this);
                    }
                }
            }

            public void onError(Throwable e) {
                child.onError(e);
            }

            public void onNext(T value) {
                if (!this.completed) {
                    this.list.add(value);
                }
            }
        };
        child.add(result);
        child.setProducer(producer);
        return result;
    }
}
