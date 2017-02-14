package rx;

import java.util.List;
import rx.functions.Func1;

class Observable$4 implements Func1<List<? extends Observable<?>>, Observable<?>[]> {
    Observable$4() {
    }

    public Observable<?>[] call(List<? extends Observable<?>> o) {
        return (Observable[]) o.toArray(new Observable[o.size()]);
    }
}
