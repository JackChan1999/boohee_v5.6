package rx.internal.operators;

import android.support.v7.widget.ActivityChooserView.ActivityChooserViewAdapter;
import com.umeng.socialize.common.SocializeConstants;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import rx.Observable;
import rx.Observable$OnSubscribe;
import rx.Producer;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.exceptions.Exceptions;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.observables.ConnectableObservable;
import rx.schedulers.Timestamped;
import rx.subscriptions.Subscriptions;

public final class OperatorReplay<T> extends ConnectableObservable<T> {
    static final Func0 DEFAULT_UNBOUNDED_FACTORY = new Func0() {
        public Object call() {
            return new UnboundedReplayBuffer(16);
        }
    };
    final Func0<? extends ReplayBuffer<T>> bufferFactory;
    final AtomicReference<ReplaySubscriber<T>> current;
    final Observable<? extends T> source;

    interface ReplayBuffer<T> {
        void complete();

        void error(Throwable th);

        void next(T t);

        void replay(InnerProducer<T> innerProducer);
    }

    static class BoundedReplayBuffer<T> extends AtomicReference<Node> implements ReplayBuffer<T> {
        private static final long serialVersionUID = 2346567790059478686L;
        final NotificationLite<T> nl = NotificationLite.instance();
        int size;
        Node tail;

        public BoundedReplayBuffer() {
            Node n = new Node(null);
            this.tail = n;
            set(n);
        }

        final void addLast(Node n) {
            this.tail.set(n);
            this.tail = n;
            this.size++;
        }

        final void removeFirst() {
            Node next = (Node) ((Node) get()).get();
            if (next == null) {
                throw new IllegalStateException("Empty list!");
            }
            this.size--;
            setFirst(next);
        }

        final void removeSome(int n) {
            Node head = (Node) get();
            while (n > 0) {
                head = (Node) head.get();
                n--;
                this.size--;
            }
            setFirst(head);
        }

        final void setFirst(Node n) {
            set(n);
        }

        public final void next(T value) {
            addLast(new Node(enterTransform(this.nl.next(value))));
            truncate();
        }

        public final void error(Throwable e) {
            addLast(new Node(enterTransform(this.nl.error(e))));
            truncateFinal();
        }

        public final void complete() {
            addLast(new Node(enterTransform(this.nl.completed())));
            truncateFinal();
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final void replay(rx.internal.operators.OperatorReplay.InnerProducer<T> r13) {
            /*
            r12 = this;
            monitor-enter(r13);
            r10 = r13.emitting;	 Catch:{ all -> 0x0077 }
            if (r10 == 0) goto L_0x000a;
        L_0x0005:
            r10 = 1;
            r13.missed = r10;	 Catch:{ all -> 0x0077 }
            monitor-exit(r13);	 Catch:{ all -> 0x0077 }
        L_0x0009:
            return;
        L_0x000a:
            r10 = 1;
            r13.emitting = r10;	 Catch:{ all -> 0x0077 }
            monitor-exit(r13);	 Catch:{ all -> 0x0077 }
        L_0x000e:
            r10 = r13.isUnsubscribed();
            if (r10 != 0) goto L_0x0009;
        L_0x0014:
            r6 = r13.get();
            r8 = r6;
            r0 = 0;
            r3 = r13.index();
            r3 = (rx.internal.operators.OperatorReplay.Node) r3;
            if (r3 != 0) goto L_0x002b;
        L_0x0023:
            r3 = r12.get();
            r3 = (rx.internal.operators.OperatorReplay.Node) r3;
            r13.index = r3;
        L_0x002b:
            r10 = 0;
            r10 = (r6 > r10 ? 1 : (r6 == r10 ? 0 : -1));
            if (r10 == 0) goto L_0x0085;
        L_0x0031:
            r5 = r3.get();
            r5 = (rx.internal.operators.OperatorReplay.Node) r5;
            if (r5 == 0) goto L_0x0085;
        L_0x0039:
            r10 = r5.value;
            r4 = r12.leaveTransform(r10);
            r10 = r12.nl;	 Catch:{ Throwable -> 0x004d }
            r11 = r13.child;	 Catch:{ Throwable -> 0x004d }
            r10 = r10.accept(r11, r4);	 Catch:{ Throwable -> 0x004d }
            if (r10 == 0) goto L_0x007a;
        L_0x0049:
            r10 = 0;
            r13.index = r10;	 Catch:{ Throwable -> 0x004d }
            goto L_0x0009;
        L_0x004d:
            r2 = move-exception;
            r10 = 0;
            r13.index = r10;
            rx.exceptions.Exceptions.throwIfFatal(r2);
            r13.unsubscribe();
            r10 = r12.nl;
            r10 = r10.isError(r4);
            if (r10 != 0) goto L_0x0009;
        L_0x005f:
            r10 = r12.nl;
            r10 = r10.isCompleted(r4);
            if (r10 != 0) goto L_0x0009;
        L_0x0067:
            r10 = r13.child;
            r11 = r12.nl;
            r11 = r11.getValue(r4);
            r11 = rx.exceptions.OnErrorThrowable.addValueAsLastCause(r2, r11);
            r10.onError(r11);
            goto L_0x0009;
        L_0x0077:
            r10 = move-exception;
            monitor-exit(r13);	 Catch:{ all -> 0x0077 }
            throw r10;
        L_0x007a:
            r10 = 1;
            r0 = r0 + r10;
            r3 = r5;
            r10 = r13.isUnsubscribed();
            if (r10 == 0) goto L_0x002b;
        L_0x0084:
            goto L_0x0009;
        L_0x0085:
            r10 = 0;
            r10 = (r0 > r10 ? 1 : (r0 == r10 ? 0 : -1));
            if (r10 == 0) goto L_0x0099;
        L_0x008b:
            r13.index = r3;
            r10 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
            r10 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1));
            if (r10 == 0) goto L_0x0099;
        L_0x0096:
            r13.produced(r0);
        L_0x0099:
            monitor-enter(r13);
            r10 = r13.missed;	 Catch:{ all -> 0x00a4 }
            if (r10 != 0) goto L_0x00a7;
        L_0x009e:
            r10 = 0;
            r13.emitting = r10;	 Catch:{ all -> 0x00a4 }
            monitor-exit(r13);	 Catch:{ all -> 0x00a4 }
            goto L_0x0009;
        L_0x00a4:
            r10 = move-exception;
            monitor-exit(r13);	 Catch:{ all -> 0x00a4 }
            throw r10;
        L_0x00a7:
            r10 = 0;
            r13.missed = r10;	 Catch:{ all -> 0x00a4 }
            monitor-exit(r13);	 Catch:{ all -> 0x00a4 }
            goto L_0x000e;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorReplay.BoundedReplayBuffer.replay(rx.internal.operators.OperatorReplay$InnerProducer):void");
        }

        Object enterTransform(Object value) {
            return value;
        }

        Object leaveTransform(Object value) {
            return value;
        }

        void truncate() {
        }

        void truncateFinal() {
        }

        final void collect(Collection<? super T> output) {
            Node n = (Node) get();
            while (true) {
                Node next = (Node) n.get();
                if (next != null) {
                    Object v = leaveTransform(next.value);
                    if (!this.nl.isCompleted(v) && !this.nl.isError(v)) {
                        output.add(this.nl.getValue(v));
                        n = next;
                    } else {
                        return;
                    }
                }
                return;
            }
        }

        boolean hasError() {
            return this.tail.value != null && this.nl.isError(leaveTransform(this.tail.value));
        }

        boolean hasCompleted() {
            return this.tail.value != null && this.nl.isCompleted(leaveTransform(this.tail.value));
        }
    }

    static final class InnerProducer<T> extends AtomicLong implements Producer, Subscription {
        static final long UNSUBSCRIBED = Long.MIN_VALUE;
        private static final long serialVersionUID = -4453897557930727610L;
        final Subscriber<? super T> child;
        boolean emitting;
        Object index;
        boolean missed;
        final ReplaySubscriber<T> parent;
        final AtomicLong totalRequested = new AtomicLong();

        public InnerProducer(ReplaySubscriber<T> parent, Subscriber<? super T> child) {
            this.parent = parent;
            this.child = child;
        }

        public void request(long n) {
            if (n >= 0) {
                long r;
                long u;
                do {
                    r = get();
                    if (r == Long.MIN_VALUE) {
                        return;
                    }
                    if (r < 0 || n != 0) {
                        u = r + n;
                        if (u < 0) {
                            u = Long.MAX_VALUE;
                        }
                    } else {
                        return;
                    }
                } while (!compareAndSet(r, u));
                addTotalRequested(n);
                this.parent.manageRequests();
                this.parent.buffer.replay(this);
            }
        }

        void addTotalRequested(long n) {
            long r;
            long u;
            do {
                r = this.totalRequested.get();
                u = r + n;
                if (u < 0) {
                    u = Long.MAX_VALUE;
                }
            } while (!this.totalRequested.compareAndSet(r, u));
        }

        public long produced(long n) {
            if (n <= 0) {
                throw new IllegalArgumentException("Cant produce zero or less");
            }
            long u;
            long r;
            do {
                r = get();
                if (r == Long.MIN_VALUE) {
                    return Long.MIN_VALUE;
                }
                u = r - n;
                if (u < 0) {
                    throw new IllegalStateException("More produced (" + n + ") than requested (" + r + SocializeConstants.OP_CLOSE_PAREN);
                }
            } while (!compareAndSet(r, u));
            return u;
        }

        public boolean isUnsubscribed() {
            return get() == Long.MIN_VALUE;
        }

        public void unsubscribe() {
            if (get() != Long.MIN_VALUE && getAndSet(Long.MIN_VALUE) != Long.MIN_VALUE) {
                this.parent.remove(this);
                this.parent.manageRequests();
            }
        }

        <U> U index() {
            return this.index;
        }
    }

    static final class Node extends AtomicReference<Node> {
        private static final long serialVersionUID = 245354315435971818L;
        final Object value;

        public Node(Object value) {
            this.value = value;
        }
    }

    static final class ReplaySubscriber<T> extends Subscriber<T> implements Subscription {
        static final InnerProducer[] EMPTY = new InnerProducer[0];
        static final InnerProducer[] TERMINATED = new InnerProducer[0];
        final ReplayBuffer<T> buffer;
        boolean done;
        boolean emitting;
        long maxChildRequested;
        long maxUpstreamRequested;
        boolean missed;
        final NotificationLite<T> nl = NotificationLite.instance();
        volatile Producer producer;
        final AtomicReference<InnerProducer[]> producers = new AtomicReference(EMPTY);
        final AtomicBoolean shouldConnect = new AtomicBoolean();

        public ReplaySubscriber(AtomicReference<ReplaySubscriber<T>> atomicReference, ReplayBuffer<T> buffer) {
            this.buffer = buffer;
            request(0);
        }

        void init() {
            add(Subscriptions.create(new Action0() {
                public void call() {
                    ReplaySubscriber.this.producers.getAndSet(ReplaySubscriber.TERMINATED);
                }
            }));
        }

        boolean add(InnerProducer<T> producer) {
            if (producer == null) {
                throw new NullPointerException();
            }
            InnerProducer[] c;
            InnerProducer[] u;
            do {
                c = (InnerProducer[]) this.producers.get();
                if (c == TERMINATED) {
                    return false;
                }
                int len = c.length;
                u = new InnerProducer[(len + 1)];
                System.arraycopy(c, 0, u, 0, len);
                u[len] = producer;
            } while (!this.producers.compareAndSet(c, u));
            return true;
        }

        void remove(InnerProducer<T> producer) {
            InnerProducer[] c;
            InnerProducer[] u;
            do {
                c = (InnerProducer[]) this.producers.get();
                if (c != EMPTY && c != TERMINATED) {
                    int j = -1;
                    int len = c.length;
                    for (int i = 0; i < len; i++) {
                        if (c[i].equals(producer)) {
                            j = i;
                            break;
                        }
                    }
                    if (j < 0) {
                        return;
                    }
                    if (len == 1) {
                        u = EMPTY;
                    } else {
                        u = new InnerProducer[(len - 1)];
                        System.arraycopy(c, 0, u, 0, j);
                        System.arraycopy(c, j + 1, u, j, (len - j) - 1);
                    }
                } else {
                    return;
                }
            } while (!this.producers.compareAndSet(c, u));
        }

        public void setProducer(Producer p) {
            if (this.producer != null) {
                throw new IllegalStateException("Only a single producer can be set on a Subscriber.");
            }
            this.producer = p;
            manageRequests();
            replay();
        }

        public void onNext(T t) {
            if (!this.done) {
                this.buffer.next(t);
                replay();
            }
        }

        public void onError(Throwable e) {
            if (!this.done) {
                this.done = true;
                try {
                    this.buffer.error(e);
                    replay();
                } finally {
                    unsubscribe();
                }
            }
        }

        public void onCompleted() {
            if (!this.done) {
                this.done = true;
                try {
                    this.buffer.complete();
                    replay();
                } finally {
                    unsubscribe();
                }
            }
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        void manageRequests() {
            /*
            r22 = this;
            r20 = r22.isUnsubscribed();
            if (r20 == 0) goto L_0x0007;
        L_0x0006:
            return;
        L_0x0007:
            monitor-enter(r22);
            r0 = r22;
            r0 = r0.emitting;	 Catch:{ all -> 0x001a }
            r20 = r0;
            if (r20 == 0) goto L_0x001d;
        L_0x0010:
            r20 = 1;
            r0 = r20;
            r1 = r22;
            r1.missed = r0;	 Catch:{ all -> 0x001a }
            monitor-exit(r22);	 Catch:{ all -> 0x001a }
            goto L_0x0006;
        L_0x001a:
            r20 = move-exception;
            monitor-exit(r22);	 Catch:{ all -> 0x001a }
            throw r20;
        L_0x001d:
            r20 = 1;
            r0 = r20;
            r1 = r22;
            r1.emitting = r0;	 Catch:{ all -> 0x001a }
            monitor-exit(r22);	 Catch:{ all -> 0x001a }
        L_0x0026:
            r20 = r22.isUnsubscribed();
            if (r20 != 0) goto L_0x0006;
        L_0x002c:
            r0 = r22;
            r0 = r0.producers;
            r20 = r0;
            r4 = r20.get();
            r4 = (rx.internal.operators.OperatorReplay.InnerProducer[]) r4;
            r0 = r22;
            r14 = r0.maxChildRequested;
            r10 = r14;
            r5 = r4;
            r9 = r5.length;
            r8 = 0;
        L_0x0040:
            if (r8 >= r9) goto L_0x0055;
        L_0x0042:
            r13 = r5[r8];
            r0 = r13.totalRequested;
            r20 = r0;
            r20 = r20.get();
            r0 = r20;
            r10 = java.lang.Math.max(r10, r0);
            r8 = r8 + 1;
            goto L_0x0040;
        L_0x0055:
            r0 = r22;
            r0 = r0.maxUpstreamRequested;
            r18 = r0;
            r0 = r22;
            r12 = r0.producer;
            r6 = r10 - r14;
            r20 = 0;
            r20 = (r6 > r20 ? 1 : (r6 == r20 ? 0 : -1));
            if (r20 == 0) goto L_0x00b1;
        L_0x0067:
            r0 = r22;
            r0.maxChildRequested = r10;
            if (r12 == 0) goto L_0x009d;
        L_0x006d:
            r20 = 0;
            r20 = (r18 > r20 ? 1 : (r18 == r20 ? 0 : -1));
            if (r20 == 0) goto L_0x0099;
        L_0x0073:
            r20 = 0;
            r0 = r20;
            r2 = r22;
            r2.maxUpstreamRequested = r0;
            r20 = r18 + r6;
            r0 = r20;
            r12.request(r0);
        L_0x0082:
            monitor-enter(r22);
            r0 = r22;
            r0 = r0.missed;	 Catch:{ all -> 0x0096 }
            r20 = r0;
            if (r20 != 0) goto L_0x00c7;
        L_0x008b:
            r20 = 0;
            r0 = r20;
            r1 = r22;
            r1.emitting = r0;	 Catch:{ all -> 0x0096 }
            monitor-exit(r22);	 Catch:{ all -> 0x0096 }
            goto L_0x0006;
        L_0x0096:
            r20 = move-exception;
            monitor-exit(r22);	 Catch:{ all -> 0x0096 }
            throw r20;
        L_0x0099:
            r12.request(r6);
            goto L_0x0082;
        L_0x009d:
            r16 = r18 + r6;
            r20 = 0;
            r20 = (r16 > r20 ? 1 : (r16 == r20 ? 0 : -1));
            if (r20 >= 0) goto L_0x00aa;
        L_0x00a5:
            r16 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
        L_0x00aa:
            r0 = r16;
            r2 = r22;
            r2.maxUpstreamRequested = r0;
            goto L_0x0082;
        L_0x00b1:
            r20 = 0;
            r20 = (r18 > r20 ? 1 : (r18 == r20 ? 0 : -1));
            if (r20 == 0) goto L_0x0082;
        L_0x00b7:
            if (r12 == 0) goto L_0x0082;
        L_0x00b9:
            r20 = 0;
            r0 = r20;
            r2 = r22;
            r2.maxUpstreamRequested = r0;
            r0 = r18;
            r12.request(r0);
            goto L_0x0082;
        L_0x00c7:
            r20 = 0;
            r0 = r20;
            r1 = r22;
            r1.missed = r0;	 Catch:{ all -> 0x0096 }
            monitor-exit(r22);	 Catch:{ all -> 0x0096 }
            goto L_0x0026;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorReplay.ReplaySubscriber.manageRequests():void");
        }

        void replay() {
            for (InnerProducer<T> rp : (InnerProducer[]) this.producers.get()) {
                this.buffer.replay(rp);
            }
        }
    }

    static final class SizeAndTimeBoundReplayBuffer<T> extends BoundedReplayBuffer<T> {
        private static final long serialVersionUID = 3457957419649567404L;
        final int limit;
        final long maxAgeInMillis;
        final Scheduler scheduler;

        public SizeAndTimeBoundReplayBuffer(int limit, long maxAgeInMillis, Scheduler scheduler) {
            this.scheduler = scheduler;
            this.limit = limit;
            this.maxAgeInMillis = maxAgeInMillis;
        }

        Object enterTransform(Object value) {
            return new Timestamped(this.scheduler.now(), value);
        }

        Object leaveTransform(Object value) {
            return ((Timestamped) value).getValue();
        }

        void truncate() {
            long timeLimit = this.scheduler.now() - this.maxAgeInMillis;
            Node prev = (Node) get();
            Node next = (Node) prev.get();
            int e = 0;
            while (next != null) {
                if (this.size <= this.limit) {
                    if (next.value.getTimestampMillis() > timeLimit) {
                        break;
                    }
                    e++;
                    this.size--;
                    prev = next;
                    next = (Node) next.get();
                } else {
                    e++;
                    this.size--;
                    prev = next;
                    next = (Node) next.get();
                }
            }
            if (e != 0) {
                setFirst(prev);
            }
        }

        void truncateFinal() {
            long timeLimit = this.scheduler.now() - this.maxAgeInMillis;
            Node prev = (Node) get();
            Node next = (Node) prev.get();
            int e = 0;
            while (next != null && this.size > 1 && next.value.getTimestampMillis() <= timeLimit) {
                e++;
                this.size--;
                prev = next;
                next = (Node) next.get();
            }
            if (e != 0) {
                setFirst(prev);
            }
        }
    }

    static final class SizeBoundReplayBuffer<T> extends BoundedReplayBuffer<T> {
        private static final long serialVersionUID = -5898283885385201806L;
        final int limit;

        public SizeBoundReplayBuffer(int limit) {
            this.limit = limit;
        }

        void truncate() {
            if (this.size > this.limit) {
                removeFirst();
            }
        }
    }

    static final class UnboundedReplayBuffer<T> extends ArrayList<Object> implements ReplayBuffer<T> {
        private static final long serialVersionUID = 7063189396499112664L;
        final NotificationLite<T> nl = NotificationLite.instance();
        volatile int size;

        public UnboundedReplayBuffer(int capacityHint) {
            super(capacityHint);
        }

        public void next(T value) {
            add(this.nl.next(value));
            this.size++;
        }

        public void error(Throwable e) {
            add(this.nl.error(e));
            this.size++;
        }

        public void complete() {
            add(this.nl.completed());
            this.size++;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void replay(rx.internal.operators.OperatorReplay.InnerProducer<T> r15) {
            /*
            r14 = this;
            monitor-enter(r15);
            r11 = r15.emitting;	 Catch:{ all -> 0x004e }
            if (r11 == 0) goto L_0x000a;
        L_0x0005:
            r11 = 1;
            r15.missed = r11;	 Catch:{ all -> 0x004e }
            monitor-exit(r15);	 Catch:{ all -> 0x004e }
        L_0x0009:
            return;
        L_0x000a:
            r11 = 1;
            r15.emitting = r11;	 Catch:{ all -> 0x004e }
            monitor-exit(r15);	 Catch:{ all -> 0x004e }
        L_0x000e:
            r11 = r15.isUnsubscribed();
            if (r11 != 0) goto L_0x0009;
        L_0x0014:
            r10 = r14.size;
            r1 = r15.index();
            r1 = (java.lang.Integer) r1;
            if (r1 == 0) goto L_0x0051;
        L_0x001e:
            r0 = r1.intValue();
        L_0x0022:
            r6 = r15.get();
            r8 = r6;
            r2 = 0;
        L_0x0029:
            r12 = 0;
            r11 = (r6 > r12 ? 1 : (r6 == r12 ? 0 : -1));
            if (r11 == 0) goto L_0x007a;
        L_0x002f:
            if (r0 >= r10) goto L_0x007a;
        L_0x0031:
            r5 = r14.get(r0);
            r11 = r14.nl;	 Catch:{ Throwable -> 0x0053 }
            r12 = r15.child;	 Catch:{ Throwable -> 0x0053 }
            r11 = r11.accept(r12, r5);	 Catch:{ Throwable -> 0x0053 }
            if (r11 != 0) goto L_0x0009;
        L_0x003f:
            r11 = r15.isUnsubscribed();
            if (r11 != 0) goto L_0x0009;
        L_0x0045:
            r0 = r0 + 1;
            r12 = 1;
            r6 = r6 - r12;
            r12 = 1;
            r2 = r2 + r12;
            goto L_0x0029;
        L_0x004e:
            r11 = move-exception;
            monitor-exit(r15);	 Catch:{ all -> 0x004e }
            throw r11;
        L_0x0051:
            r0 = 0;
            goto L_0x0022;
        L_0x0053:
            r4 = move-exception;
            rx.exceptions.Exceptions.throwIfFatal(r4);
            r15.unsubscribe();
            r11 = r14.nl;
            r11 = r11.isError(r5);
            if (r11 != 0) goto L_0x0009;
        L_0x0062:
            r11 = r14.nl;
            r11 = r11.isCompleted(r5);
            if (r11 != 0) goto L_0x0009;
        L_0x006a:
            r11 = r15.child;
            r12 = r14.nl;
            r12 = r12.getValue(r5);
            r12 = rx.exceptions.OnErrorThrowable.addValueAsLastCause(r4, r12);
            r11.onError(r12);
            goto L_0x0009;
        L_0x007a:
            r12 = 0;
            r11 = (r2 > r12 ? 1 : (r2 == r12 ? 0 : -1));
            if (r11 == 0) goto L_0x0092;
        L_0x0080:
            r11 = java.lang.Integer.valueOf(r0);
            r15.index = r11;
            r12 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
            r11 = (r8 > r12 ? 1 : (r8 == r12 ? 0 : -1));
            if (r11 == 0) goto L_0x0092;
        L_0x008f:
            r15.produced(r2);
        L_0x0092:
            monitor-enter(r15);
            r11 = r15.missed;	 Catch:{ all -> 0x009d }
            if (r11 != 0) goto L_0x00a0;
        L_0x0097:
            r11 = 0;
            r15.emitting = r11;	 Catch:{ all -> 0x009d }
            monitor-exit(r15);	 Catch:{ all -> 0x009d }
            goto L_0x0009;
        L_0x009d:
            r11 = move-exception;
            monitor-exit(r15);	 Catch:{ all -> 0x009d }
            throw r11;
        L_0x00a0:
            r11 = 0;
            r15.missed = r11;	 Catch:{ all -> 0x009d }
            monitor-exit(r15);	 Catch:{ all -> 0x009d }
            goto L_0x000e;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorReplay.UnboundedReplayBuffer.replay(rx.internal.operators.OperatorReplay$InnerProducer):void");
        }
    }

    public static <T, U, R> Observable<R> multicastSelector(final Func0<? extends ConnectableObservable<U>> connectableFactory, final Func1<? super Observable<U>, ? extends Observable<R>> selector) {
        return Observable.create(new Observable$OnSubscribe<R>() {
            public void call(final Subscriber<? super R> child) {
                try {
                    ConnectableObservable<U> co = (ConnectableObservable) connectableFactory.call();
                    ((Observable) selector.call(co)).subscribe(child);
                    co.connect(new Action1<Subscription>() {
                        public void call(Subscription t) {
                            child.add(t);
                        }
                    });
                } catch (Throwable e) {
                    Exceptions.throwOrReport(e, child);
                }
            }
        });
    }

    public static <T> ConnectableObservable<T> observeOn(final ConnectableObservable<T> co, Scheduler scheduler) {
        final Observable<T> observable = co.observeOn(scheduler);
        return new ConnectableObservable<T>(new Observable$OnSubscribe<T>() {
            public void call(final Subscriber<? super T> child) {
                observable.unsafeSubscribe(new Subscriber<T>(child) {
                    public void onNext(T t) {
                        child.onNext(t);
                    }

                    public void onError(Throwable e) {
                        child.onError(e);
                    }

                    public void onCompleted() {
                        child.onCompleted();
                    }
                });
            }
        }) {
            public void connect(Action1<? super Subscription> connection) {
                co.connect(connection);
            }
        };
    }

    public static <T> ConnectableObservable<T> create(Observable<? extends T> source) {
        return create((Observable) source, DEFAULT_UNBOUNDED_FACTORY);
    }

    public static <T> ConnectableObservable<T> create(Observable<? extends T> source, final int bufferSize) {
        if (bufferSize == ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED) {
            return create(source);
        }
        return create((Observable) source, new Func0<ReplayBuffer<T>>() {
            public ReplayBuffer<T> call() {
                return new SizeBoundReplayBuffer(bufferSize);
            }
        });
    }

    public static <T> ConnectableObservable<T> create(Observable<? extends T> source, long maxAge, TimeUnit unit, Scheduler scheduler) {
        return create(source, maxAge, unit, scheduler, ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED);
    }

    public static <T> ConnectableObservable<T> create(Observable<? extends T> source, long maxAge, TimeUnit unit, final Scheduler scheduler, final int bufferSize) {
        final long maxAgeInMillis = unit.toMillis(maxAge);
        return create((Observable) source, new Func0<ReplayBuffer<T>>() {
            public ReplayBuffer<T> call() {
                return new SizeAndTimeBoundReplayBuffer(bufferSize, maxAgeInMillis, scheduler);
            }
        });
    }

    static <T> ConnectableObservable<T> create(Observable<? extends T> source, final Func0<? extends ReplayBuffer<T>> bufferFactory) {
        final AtomicReference<ReplaySubscriber<T>> curr = new AtomicReference();
        return new OperatorReplay(new Observable$OnSubscribe<T>() {
            public void call(Subscriber<? super T> child) {
                ReplaySubscriber<T> r;
                ReplaySubscriber<T> u;
                do {
                    r = (ReplaySubscriber) curr.get();
                    if (r != null) {
                        break;
                    }
                    u = new ReplaySubscriber(curr, (ReplayBuffer) bufferFactory.call());
                    u.init();
                } while (!curr.compareAndSet(r, u));
                r = u;
                InnerProducer<T> inner = new InnerProducer(r, child);
                r.add(inner);
                child.add(inner);
                child.setProducer(inner);
            }
        }, source, curr, bufferFactory);
    }

    private OperatorReplay(Observable$OnSubscribe<T> onSubscribe, Observable<? extends T> source, AtomicReference<ReplaySubscriber<T>> current, Func0<? extends ReplayBuffer<T>> bufferFactory) {
        super(onSubscribe);
        this.source = source;
        this.current = current;
        this.bufferFactory = bufferFactory;
    }

    public void connect(Action1<? super Subscription> connection) {
        ReplaySubscriber<T> ps;
        ReplaySubscriber<T> u;
        boolean doConnect;
        do {
            ps = (ReplaySubscriber) this.current.get();
            if (ps != null && !ps.isUnsubscribed()) {
                break;
            }
            u = new ReplaySubscriber(this.current, (ReplayBuffer) this.bufferFactory.call());
            u.init();
        } while (!this.current.compareAndSet(ps, u));
        ps = u;
        if (ps.shouldConnect.get() || !ps.shouldConnect.compareAndSet(false, true)) {
            doConnect = false;
        } else {
            doConnect = true;
        }
        connection.call(ps);
        if (doConnect) {
            this.source.unsafeSubscribe(ps);
        }
    }
}
