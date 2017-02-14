package rx.internal.operators;

import java.util.concurrent.atomic.AtomicLong;
import rx.Observable$Operator;
import rx.Producer;
import rx.Subscriber;

public final class OperatorTake<T> implements Observable$Operator<T, T> {
    final int limit;

    public OperatorTake(int limit) {
        this.limit = limit;
    }

    public Subscriber<? super T> call(final Subscriber<? super T> child) {
        Subscriber<T> parent = new Subscriber<T>() {
            boolean completed;
            int count;

            public void onCompleted() {
                if (!this.completed) {
                    this.completed = true;
                    child.onCompleted();
                }
            }

            public void onError(Throwable e) {
                if (!this.completed) {
                    this.completed = true;
                    try {
                        child.onError(e);
                    } finally {
                        unsubscribe();
                    }
                }
            }

            public void onNext(T i) {
                if (!isUnsubscribed()) {
                    int i2 = this.count;
                    this.count = i2 + 1;
                    if (i2 < OperatorTake.this.limit) {
                        boolean stop = this.count == OperatorTake.this.limit;
                        child.onNext(i);
                        if (stop && !this.completed) {
                            this.completed = true;
                            try {
                                child.onCompleted();
                            } finally {
                                unsubscribe();
                            }
                        }
                    }
                }
            }

            public void setProducer(final Producer producer) {
                child.setProducer(new Producer() {
                    final AtomicLong requested = new AtomicLong(0);

                    public void request(long n) {
                        if (n > 0 && !AnonymousClass1.this.completed) {
                            long c;
                            long r;
                            do {
                                r = this.requested.get();
                                c = Math.min(n, ((long) OperatorTake.this.limit) - r);
                                if (c == 0) {
                                    return;
                                }
                            } while (!this.requested.compareAndSet(r, r + c));
                            producer.request(c);
                        }
                    }
                });
            }
        };
        if (this.limit == 0) {
            child.onCompleted();
            parent.unsubscribe();
        }
        child.add(parent);
        return parent;
    }
}
