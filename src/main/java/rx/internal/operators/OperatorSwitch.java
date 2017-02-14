package rx.internal.operators;

import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.Observable$Operator;
import rx.Producer;
import rx.Subscriber;
import rx.internal.producers.ProducerArbiter;
import rx.observers.SerializedSubscriber;
import rx.subscriptions.SerialSubscription;

public final class OperatorSwitch<T> implements Observable$Operator<T, Observable<? extends T>> {

    private static final class Holder {
        static final OperatorSwitch<Object> INSTANCE = new OperatorSwitch();

        private Holder() {
        }
    }

    private static final class InnerSubscriber<T> extends Subscriber<T> {
        private final ProducerArbiter arbiter;
        private final int id;
        private final SwitchSubscriber<T> parent;

        InnerSubscriber(int id, ProducerArbiter arbiter, SwitchSubscriber<T> parent) {
            this.id = id;
            this.arbiter = arbiter;
            this.parent = parent;
        }

        public void setProducer(Producer p) {
            this.arbiter.setProducer(p);
        }

        public void onNext(T t) {
            this.parent.emit(t, this.id, this);
        }

        public void onError(Throwable e) {
            this.parent.error(e, this.id);
        }

        public void onCompleted() {
            this.parent.complete(this.id);
        }
    }

    private static final class SwitchSubscriber<T> extends Subscriber<Observable<? extends T>> {
        boolean active;
        final ProducerArbiter arbiter;
        InnerSubscriber<T> currentSubscriber;
        boolean emitting;
        final Object guard = new Object();
        int index;
        boolean mainDone;
        final NotificationLite<?> nl = NotificationLite.instance();
        List<Object> queue;
        final SerializedSubscriber<T> serializedChild;
        final SerialSubscription ssub;

        SwitchSubscriber(Subscriber<? super T> child) {
            this.serializedChild = new SerializedSubscriber(child);
            this.arbiter = new ProducerArbiter();
            this.ssub = new SerialSubscription();
            child.add(this.ssub);
            child.setProducer(new Producer() {
                public void request(long n) {
                    if (n > 0) {
                        SwitchSubscriber.this.arbiter.request(n);
                    }
                }
            });
        }

        public void onNext(Observable<? extends T> t) {
            synchronized (this.guard) {
                int id = this.index + 1;
                this.index = id;
                this.active = true;
                this.currentSubscriber = new InnerSubscriber(id, this.arbiter, this);
            }
            this.ssub.set(this.currentSubscriber);
            t.unsafeSubscribe(this.currentSubscriber);
        }

        public void onError(Throwable e) {
            this.serializedChild.onError(e);
            unsubscribe();
        }

        public void onCompleted() {
            synchronized (this.guard) {
                this.mainDone = true;
                if (this.active) {
                } else if (this.emitting) {
                    if (this.queue == null) {
                        this.queue = new ArrayList();
                    }
                    this.queue.add(this.nl.completed());
                } else {
                    List<Object> localQueue = this.queue;
                    this.queue = null;
                    this.emitting = true;
                    drain(localQueue);
                    this.serializedChild.onCompleted();
                    unsubscribe();
                }
            }
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        void emit(T r7, int r8, rx.internal.operators.OperatorSwitch.InnerSubscriber<T> r9) {
            /*
            r6 = this;
            r4 = r6.guard;
            monitor-enter(r4);
            r3 = r6.index;	 Catch:{ all -> 0x001f }
            if (r8 == r3) goto L_0x0009;
        L_0x0007:
            monitor-exit(r4);	 Catch:{ all -> 0x001f }
        L_0x0008:
            return;
        L_0x0009:
            r3 = r6.emitting;	 Catch:{ all -> 0x001f }
            if (r3 == 0) goto L_0x0022;
        L_0x000d:
            r3 = r6.queue;	 Catch:{ all -> 0x001f }
            if (r3 != 0) goto L_0x0018;
        L_0x0011:
            r3 = new java.util.ArrayList;	 Catch:{ all -> 0x001f }
            r3.<init>();	 Catch:{ all -> 0x001f }
            r6.queue = r3;	 Catch:{ all -> 0x001f }
        L_0x0018:
            r3 = r6.queue;	 Catch:{ all -> 0x001f }
            r3.add(r7);	 Catch:{ all -> 0x001f }
            monitor-exit(r4);	 Catch:{ all -> 0x001f }
            goto L_0x0008;
        L_0x001f:
            r3 = move-exception;
            monitor-exit(r4);	 Catch:{ all -> 0x001f }
            throw r3;
        L_0x0022:
            r0 = r6.queue;	 Catch:{ all -> 0x001f }
            r3 = 0;
            r6.queue = r3;	 Catch:{ all -> 0x001f }
            r3 = 1;
            r6.emitting = r3;	 Catch:{ all -> 0x001f }
            monitor-exit(r4);	 Catch:{ all -> 0x001f }
            r1 = 1;
            r2 = 0;
        L_0x002d:
            r6.drain(r0);	 Catch:{ all -> 0x0068 }
            if (r1 == 0) goto L_0x003f;
        L_0x0032:
            r1 = 0;
            r3 = r6.serializedChild;	 Catch:{ all -> 0x0068 }
            r3.onNext(r7);	 Catch:{ all -> 0x0068 }
            r3 = r6.arbiter;	 Catch:{ all -> 0x0068 }
            r4 = 1;
            r3.produced(r4);	 Catch:{ all -> 0x0068 }
        L_0x003f:
            r4 = r6.guard;	 Catch:{ all -> 0x0068 }
            monitor-enter(r4);	 Catch:{ all -> 0x0068 }
            r0 = r6.queue;	 Catch:{ all -> 0x0065 }
            r3 = 0;
            r6.queue = r3;	 Catch:{ all -> 0x0065 }
            if (r0 != 0) goto L_0x005b;
        L_0x0049:
            r3 = 0;
            r6.emitting = r3;	 Catch:{ all -> 0x0065 }
            r2 = 1;
            monitor-exit(r4);	 Catch:{ all -> 0x0065 }
        L_0x004e:
            if (r2 != 0) goto L_0x0008;
        L_0x0050:
            r4 = r6.guard;
            monitor-enter(r4);
            r3 = 0;
            r6.emitting = r3;	 Catch:{ all -> 0x0058 }
            monitor-exit(r4);	 Catch:{ all -> 0x0058 }
            goto L_0x0008;
        L_0x0058:
            r3 = move-exception;
            monitor-exit(r4);	 Catch:{ all -> 0x0058 }
            throw r3;
        L_0x005b:
            monitor-exit(r4);	 Catch:{ all -> 0x0065 }
            r3 = r6.serializedChild;	 Catch:{ all -> 0x0068 }
            r3 = r3.isUnsubscribed();	 Catch:{ all -> 0x0068 }
            if (r3 == 0) goto L_0x002d;
        L_0x0064:
            goto L_0x004e;
        L_0x0065:
            r3 = move-exception;
            monitor-exit(r4);	 Catch:{ all -> 0x0065 }
            throw r3;	 Catch:{ all -> 0x0068 }
        L_0x0068:
            r3 = move-exception;
            if (r2 != 0) goto L_0x0072;
        L_0x006b:
            r4 = r6.guard;
            monitor-enter(r4);
            r5 = 0;
            r6.emitting = r5;	 Catch:{ all -> 0x0073 }
            monitor-exit(r4);	 Catch:{ all -> 0x0073 }
        L_0x0072:
            throw r3;
        L_0x0073:
            r3 = move-exception;
            monitor-exit(r4);	 Catch:{ all -> 0x0073 }
            throw r3;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorSwitch.SwitchSubscriber.emit(java.lang.Object, int, rx.internal.operators.OperatorSwitch$InnerSubscriber):void");
        }

        void drain(List<Object> localQueue) {
            if (localQueue != null) {
                for (T o : localQueue) {
                    if (this.nl.isCompleted(o)) {
                        this.serializedChild.onCompleted();
                        return;
                    } else if (this.nl.isError(o)) {
                        this.serializedChild.onError(this.nl.getError(o));
                        return;
                    } else {
                        this.serializedChild.onNext(o);
                        this.arbiter.produced(1);
                    }
                }
            }
        }

        void error(Throwable e, int id) {
            synchronized (this.guard) {
                if (id != this.index) {
                } else if (this.emitting) {
                    if (this.queue == null) {
                        this.queue = new ArrayList();
                    }
                    this.queue.add(this.nl.error(e));
                } else {
                    List<Object> localQueue = this.queue;
                    this.queue = null;
                    this.emitting = true;
                    drain(localQueue);
                    this.serializedChild.onError(e);
                    unsubscribe();
                }
            }
        }

        void complete(int id) {
            synchronized (this.guard) {
                if (id != this.index) {
                    return;
                }
                this.active = false;
                if (!this.mainDone) {
                } else if (this.emitting) {
                    if (this.queue == null) {
                        this.queue = new ArrayList();
                    }
                    this.queue.add(this.nl.completed());
                } else {
                    List<Object> localQueue = this.queue;
                    this.queue = null;
                    this.emitting = true;
                    drain(localQueue);
                    this.serializedChild.onCompleted();
                    unsubscribe();
                }
            }
        }
    }

    public static <T> OperatorSwitch<T> instance() {
        return Holder.INSTANCE;
    }

    private OperatorSwitch() {
    }

    public Subscriber<? super Observable<? extends T>> call(Subscriber<? super T> child) {
        SwitchSubscriber<T> sws = new SwitchSubscriber(child);
        child.add(sws);
        return sws;
    }
}
