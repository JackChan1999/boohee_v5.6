package rx;

import rx.functions.Func1;

public interface Observable$Transformer<T, R> extends Func1<Observable<T>, Observable<R>> {
}
