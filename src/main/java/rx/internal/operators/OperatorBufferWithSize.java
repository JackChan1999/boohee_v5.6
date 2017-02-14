package rx.internal.operators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import rx.Observable$Operator;
import rx.Producer;
import rx.Subscriber;
import rx.exceptions.Exceptions;

public final class OperatorBufferWithSize<T> implements Observable$Operator<List<T>, T> {
    final int count;
    final int skip;

    public OperatorBufferWithSize(int count, int skip) {
        if (count <= 0) {
            throw new IllegalArgumentException("count must be greater than 0");
        } else if (skip <= 0) {
            throw new IllegalArgumentException("skip must be greater than 0");
        } else {
            this.count = count;
            this.skip = skip;
        }
    }

    public Subscriber<? super T> call(final Subscriber<? super List<T>> child) {
        return this.count == this.skip ? new Subscriber<T>(child) {
            List<T> buffer;

            public void setProducer(final Producer producer) {
                child.setProducer(new Producer() {
                    private volatile boolean infinite = false;

                    public void request(long n) {
                        if (!this.infinite) {
                            if (n >= Long.MAX_VALUE / ((long) OperatorBufferWithSize.this.count)) {
                                this.infinite = true;
                                producer.request(Long.MAX_VALUE);
                                return;
                            }
                            producer.request(((long) OperatorBufferWithSize.this.count) * n);
                        }
                    }
                });
            }

            public void onNext(T t) {
                if (this.buffer == null) {
                    this.buffer = new ArrayList(OperatorBufferWithSize.this.count);
                }
                this.buffer.add(t);
                if (this.buffer.size() == OperatorBufferWithSize.this.count) {
                    List<T> oldBuffer = this.buffer;
                    this.buffer = null;
                    child.onNext(oldBuffer);
                }
            }

            public void onError(Throwable e) {
                this.buffer = null;
                child.onError(e);
            }

            public void onCompleted() {
                List<T> oldBuffer = this.buffer;
                this.buffer = null;
                if (oldBuffer != null) {
                    try {
                        child.onNext(oldBuffer);
                    } catch (Throwable t) {
                        Exceptions.throwOrReport(t, this);
                        return;
                    }
                }
                child.onCompleted();
            }
        } : new Subscriber<T>(child) {
            final List<List<T>> chunks = new LinkedList();
            int index;

            public void setProducer(final Producer producer) {
                child.setProducer(new Producer() {
                    private volatile boolean firstRequest = true;
                    private volatile boolean infinite = false;

                    private void requestInfinite() {
                        this.infinite = true;
                        producer.request(Long.MAX_VALUE);
                    }

                    public void request(long n) {
                        if (n != 0) {
                            if (n < 0) {
                                throw new IllegalArgumentException("request a negative number: " + n);
                            } else if (!this.infinite) {
                                if (n == Long.MAX_VALUE) {
                                    requestInfinite();
                                } else if (this.firstRequest) {
                                    this.firstRequest = false;
                                    if (n - 1 >= (Long.MAX_VALUE - ((long) OperatorBufferWithSize.this.count)) / ((long) OperatorBufferWithSize.this.skip)) {
                                        requestInfinite();
                                    } else {
                                        producer.request(((long) OperatorBufferWithSize.this.count) + (((long) OperatorBufferWithSize.this.skip) * (n - 1)));
                                    }
                                } else if (n >= Long.MAX_VALUE / ((long) OperatorBufferWithSize.this.skip)) {
                                    requestInfinite();
                                } else {
                                    producer.request(((long) OperatorBufferWithSize.this.skip) * n);
                                }
                            }
                        }
                    }
                });
            }

            public void onNext(T t) {
                int i = this.index;
                this.index = i + 1;
                if (i % OperatorBufferWithSize.this.skip == 0) {
                    this.chunks.add(new ArrayList(OperatorBufferWithSize.this.count));
                }
                Iterator<List<T>> it = this.chunks.iterator();
                while (it.hasNext()) {
                    List<T> chunk = (List) it.next();
                    chunk.add(t);
                    if (chunk.size() == OperatorBufferWithSize.this.count) {
                        it.remove();
                        child.onNext(chunk);
                    }
                }
            }

            public void onError(Throwable e) {
                this.chunks.clear();
                child.onError(e);
            }

            public void onCompleted() {
                try {
                    for (List<T> chunk : this.chunks) {
                        child.onNext(chunk);
                    }
                    child.onCompleted();
                } catch (Throwable t) {
                    Exceptions.throwOrReport(t, this);
                } finally {
                    this.chunks.clear();
                }
            }
        };
    }
}
