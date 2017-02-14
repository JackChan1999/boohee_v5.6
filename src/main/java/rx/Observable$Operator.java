package rx;

import rx.functions.Func1;

public interface Observable$Operator<R, T> extends Func1<Subscriber<? super R>, Subscriber<? super T>> {
}
