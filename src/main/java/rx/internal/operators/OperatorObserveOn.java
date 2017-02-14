package rx.internal.operators;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import rx.Observable$Operator;
import rx.Producer;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.Subscriber;
import rx.Subscription;
import rx.exceptions.MissingBackpressureException;
import rx.functions.Action0;
import rx.internal.util.RxRingBuffer;
import rx.internal.util.SynchronizedQueue;
import rx.internal.util.unsafe.SpscArrayQueue;
import rx.internal.util.unsafe.UnsafeAccess;
import rx.schedulers.ImmediateScheduler;
import rx.schedulers.TrampolineScheduler;

public final class OperatorObserveOn<T> implements Observable$Operator<T, T> {
    private final Scheduler scheduler;

    private static final class ObserveOnSubscriber<T> extends Subscriber<T> {
        final Action0 action = new Action0() {
            public void call() {
                ObserveOnSubscriber.this.pollQueue();
            }
        };
        final Subscriber<? super T> child;
        final AtomicLong counter = new AtomicLong();
        volatile Throwable error;
        volatile boolean finished = false;
        final NotificationLite<T> on = NotificationLite.instance();
        final Queue<Object> queue;
        final Worker recursiveScheduler;
        final AtomicLong requested = new AtomicLong();
        final ScheduledUnsubscribe scheduledUnsubscribe;

        public ObserveOnSubscriber(Scheduler scheduler, Subscriber<? super T> child) {
            this.child = child;
            this.recursiveScheduler = scheduler.createWorker();
            if (UnsafeAccess.isUnsafeAvailable()) {
                this.queue = new SpscArrayQueue(RxRingBuffer.SIZE);
            } else {
                this.queue = new SynchronizedQueue(RxRingBuffer.SIZE);
            }
            this.scheduledUnsubscribe = new ScheduledUnsubscribe(this.recursiveScheduler);
        }

        void init() {
            this.child.add(this.scheduledUnsubscribe);
            this.child.setProducer(new Producer() {
                public void request(long n) {
                    BackpressureUtils.getAndAddRequest(ObserveOnSubscriber.this.requested, n);
                    ObserveOnSubscriber.this.schedule();
                }
            });
            this.child.add(this.recursiveScheduler);
            this.child.add(this);
        }

        public void onStart() {
            request((long) RxRingBuffer.SIZE);
        }

        public void onNext(T t) {
            if (!isUnsubscribed()) {
                if (this.queue.offer(this.on.next(t))) {
                    schedule();
                } else {
                    onError(new MissingBackpressureException());
                }
            }
        }

        public void onCompleted() {
            if (!isUnsubscribed() && !this.finished) {
                this.finished = true;
                schedule();
            }
        }

        public void onError(Throwable e) {
            if (!isUnsubscribed() && !this.finished) {
                this.error = e;
                unsubscribe();
                this.finished = true;
                schedule();
            }
        }

        protected void schedule() {
            if (this.counter.getAndIncrement() == 0) {
                this.recursiveScheduler.schedule(this.action);
            }
        }

        void pollQueue() {
            int emitted = 0;
            AtomicLong localRequested = this.requested;
            AtomicLong localCounter = this.counter;
            do {
                localCounter.set(1);
                long produced = 0;
                long r = localRequested.get();
                while (!this.child.isUnsubscribed()) {
                    if (this.finished) {
                        Throwable error = this.error;
                        if (error != null) {
                            this.queue.clear();
                            this.child.onError(error);
                            return;
                        } else if (this.queue.isEmpty()) {
                            this.child.onCompleted();
                            return;
                        }
                    }
                    if (r > 0) {
                        Object o = this.queue.poll();
                        if (o != null) {
                            this.child.onNext(this.on.getValue(o));
                            r--;
                            emitted++;
                            produced++;
                        }
                    }
                    if (produced > 0 && localRequested.get() != Long.MAX_VALUE) {
                        localRequested.addAndGet(-produced);
                    }
                }
                return;
            } while (localCounter.decrementAndGet() > 0);
            if (emitted > 0) {
                request((long) emitted);
            }
        }
    }

    static final class ScheduledUnsubscribe extends AtomicInteger implements Subscription {
        volatile boolean unsubscribed = false;
        final Worker worker;

        public ScheduledUnsubscribe(Worker worker) {
            this.worker = worker;
        }

        public boolean isUnsubscribed() {
            return this.unsubscribed;
        }

        public void unsubscribe() {
            if (getAndSet(1) == 0) {
                this.worker.schedule(new Action0() {
                    public void call() {
                        ScheduledUnsubscribe.this.worker.unsubscribe();
                        ScheduledUnsubscribe.this.unsubscribed = true;
                    }
                });
            }
        }
    }

    public OperatorObserveOn(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        if ((this.scheduler instanceof ImmediateScheduler) || (this.scheduler instanceof TrampolineScheduler)) {
            return child;
        }
        Subscriber parent = new ObserveOnSubscriber(this.scheduler, child);
        parent.init();
        return parent;
    }
}
