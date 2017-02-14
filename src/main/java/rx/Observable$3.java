package rx;

import rx.functions.Func2;

class Observable$3 implements Func2<T, T, Boolean> {
    Observable$3() {
    }

    public final Boolean call(T first, T second) {
        if (first != null) {
            return Boolean.valueOf(first.equals(second));
        }
        return Boolean.valueOf(second == null);
    }
}
