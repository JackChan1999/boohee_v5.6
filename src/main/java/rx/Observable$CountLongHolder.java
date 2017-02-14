package rx;

import rx.functions.Func2;

final class Observable$CountLongHolder {
    static final Func2<Long, Object, Long> INSTANCE = new Func2<Long, Object, Long>() {
        public final Long call(Long count, Object o) {
            return Long.valueOf(count.longValue() + 1);
        }
    };

    private Observable$CountLongHolder() {
    }
}
