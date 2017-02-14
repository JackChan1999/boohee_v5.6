package rx;

import rx.functions.Func2;

final class Observable$CountHolder {
    static final Func2<Integer, Object, Integer> INSTANCE = new Func2<Integer, Object, Integer>() {
        public final Integer call(Integer count, Object o) {
            return Integer.valueOf(count.intValue() + 1);
        }
    };

    private Observable$CountHolder() {
    }
}
