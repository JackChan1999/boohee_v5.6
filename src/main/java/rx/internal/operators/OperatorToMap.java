package rx.internal.operators;

import java.util.HashMap;
import java.util.Map;
import rx.Observable$Operator;
import rx.Subscriber;
import rx.functions.Func0;
import rx.functions.Func1;

public final class OperatorToMap<T, K, V> implements Observable$Operator<Map<K, V>, T> {
    private final Func1<? super T, ? extends K> keySelector;
    private final Func0<? extends Map<K, V>> mapFactory;
    private final Func1<? super T, ? extends V> valueSelector;

    public static final class DefaultToMapFactory<K, V> implements Func0<Map<K, V>> {
        public Map<K, V> call() {
            return new HashMap();
        }
    }

    public OperatorToMap(Func1<? super T, ? extends K> keySelector, Func1<? super T, ? extends V> valueSelector) {
        this(keySelector, valueSelector, new DefaultToMapFactory());
    }

    public OperatorToMap(Func1<? super T, ? extends K> keySelector, Func1<? super T, ? extends V> valueSelector, Func0<? extends Map<K, V>> mapFactory) {
        this.keySelector = keySelector;
        this.valueSelector = valueSelector;
        this.mapFactory = mapFactory;
    }

    public Subscriber<? super T> call(final Subscriber<? super Map<K, V>> subscriber) {
        return new Subscriber<T>(subscriber) {
            private Map<K, V> map = ((Map) OperatorToMap.this.mapFactory.call());

            public void onStart() {
                request(Long.MAX_VALUE);
            }

            public void onNext(T v) {
                this.map.put(OperatorToMap.this.keySelector.call(v), OperatorToMap.this.valueSelector.call(v));
            }

            public void onError(Throwable e) {
                this.map = null;
                subscriber.onError(e);
            }

            public void onCompleted() {
                Map<K, V> map0 = this.map;
                this.map = null;
                subscriber.onNext(map0);
                subscriber.onCompleted();
            }
        };
    }
}
