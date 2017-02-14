package rx.internal.operators;

import java.util.concurrent.atomic.AtomicLong;
import rx.Observable$OnSubscribe;
import rx.Producer;
import rx.Subscriber;

public final class OnSubscribeRange implements Observable$OnSubscribe<Integer> {
    private final int end;
    private final int start;

    private static final class RangeProducer extends AtomicLong implements Producer {
        private static final long serialVersionUID = 4114392207069098388L;
        private final int end;
        private long index;
        private final Subscriber<? super Integer> o;

        private RangeProducer(Subscriber<? super Integer> o, int start, int end) {
            this.o = o;
            this.index = (long) start;
            this.end = end;
        }

        public void request(long n) {
            if (get() != Long.MAX_VALUE) {
                if (n == Long.MAX_VALUE && compareAndSet(0, Long.MAX_VALUE)) {
                    fastpath();
                } else if (n > 0 && BackpressureUtils.getAndAddRequest(this, n) == 0) {
                    slowpath(n);
                }
            }
        }

        void slowpath(long r) {
            long idx = this.index;
            do {
                long fs = (((long) this.end) - idx) + 1;
                long e = Math.min(fs, r);
                boolean complete = fs <= r;
                fs = e + idx;
                Subscriber<? super Integer> o = this.o;
                long i = idx;
                while (i != fs) {
                    if (!o.isUnsubscribed()) {
                        o.onNext(Integer.valueOf((int) i));
                        i++;
                    } else {
                        return;
                    }
                }
                if (!complete) {
                    idx = fs;
                    this.index = fs;
                    r = addAndGet(-e);
                } else if (!o.isUnsubscribed()) {
                    o.onCompleted();
                    return;
                } else {
                    return;
                }
            } while (r != 0);
        }

        void fastpath() {
            long end = ((long) this.end) + 1;
            Subscriber<? super Integer> o = this.o;
            long i = this.index;
            while (i != end) {
                if (!o.isUnsubscribed()) {
                    o.onNext(Integer.valueOf((int) i));
                    i++;
                } else {
                    return;
                }
            }
            if (!o.isUnsubscribed()) {
                o.onCompleted();
            }
        }
    }

    public OnSubscribeRange(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public void call(Subscriber<? super Integer> o) {
        o.setProducer(new RangeProducer(o, this.start, this.end));
    }
}
