package rx.internal.operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Observable$Operator;
import rx.Observer;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.Subscriber;
import rx.functions.Action0;
import rx.observers.SerializedObserver;
import rx.observers.SerializedSubscriber;
import rx.subscriptions.Subscriptions;

public final class OperatorWindowWithTime<T> implements Observable$Operator<Observable<T>, T> {
    static final Object NEXT_SUBJECT = new Object();
    static final NotificationLite<Object> nl = NotificationLite.instance();
    final Scheduler scheduler;
    final int size;
    final long timeshift;
    final long timespan;
    final TimeUnit unit;

    static final class CountedSerializedSubject<T> {
        final Observer<T> consumer;
        int count;
        final Observable<T> producer;

        public CountedSerializedSubject(Observer<T> consumer, Observable<T> producer) {
            this.consumer = new SerializedObserver(consumer);
            this.producer = producer;
        }
    }

    final class ExactSubscriber extends Subscriber<T> {
        final Subscriber<? super Observable<T>> child;
        boolean emitting;
        final Object guard = new Object();
        List<Object> queue;
        volatile State<T> state = State.empty();
        final Worker worker;

        public ExactSubscriber(Subscriber<? super Observable<T>> child, Worker worker) {
            this.child = new SerializedSubscriber(child);
            this.worker = worker;
            child.add(Subscriptions.create(new Action0(OperatorWindowWithTime.this) {
                public void call() {
                    if (ExactSubscriber.this.state.consumer == null) {
                        ExactSubscriber.this.unsubscribe();
                    }
                }
            }));
        }

        public void onStart() {
            request(Long.MAX_VALUE);
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onNext(T r6) {
            /*
            r5 = this;
            r3 = r5.guard;
            monitor-enter(r3);
            r2 = r5.emitting;	 Catch:{ all -> 0x0031 }
            if (r2 == 0) goto L_0x0019;
        L_0x0007:
            r2 = r5.queue;	 Catch:{ all -> 0x0031 }
            if (r2 != 0) goto L_0x0012;
        L_0x000b:
            r2 = new java.util.ArrayList;	 Catch:{ all -> 0x0031 }
            r2.<init>();	 Catch:{ all -> 0x0031 }
            r5.queue = r2;	 Catch:{ all -> 0x0031 }
        L_0x0012:
            r2 = r5.queue;	 Catch:{ all -> 0x0031 }
            r2.add(r6);	 Catch:{ all -> 0x0031 }
            monitor-exit(r3);	 Catch:{ all -> 0x0031 }
        L_0x0018:
            return;
        L_0x0019:
            r2 = 1;
            r5.emitting = r2;	 Catch:{ all -> 0x0031 }
            monitor-exit(r3);	 Catch:{ all -> 0x0031 }
            r1 = 0;
            r2 = r5.emitValue(r6);	 Catch:{ all -> 0x0067 }
            if (r2 != 0) goto L_0x0034;
        L_0x0024:
            if (r1 != 0) goto L_0x0018;
        L_0x0026:
            r3 = r5.guard;
            monitor-enter(r3);
            r2 = 0;
            r5.emitting = r2;	 Catch:{ all -> 0x002e }
            monitor-exit(r3);	 Catch:{ all -> 0x002e }
            goto L_0x0018;
        L_0x002e:
            r2 = move-exception;
            monitor-exit(r3);	 Catch:{ all -> 0x002e }
            throw r2;
        L_0x0031:
            r2 = move-exception;
            monitor-exit(r3);	 Catch:{ all -> 0x0031 }
            throw r2;
        L_0x0034:
            r3 = r5.guard;	 Catch:{ all -> 0x0067 }
            monitor-enter(r3);	 Catch:{ all -> 0x0067 }
            r0 = r5.queue;	 Catch:{ all -> 0x0064 }
            if (r0 != 0) goto L_0x004d;
        L_0x003b:
            r2 = 0;
            r5.emitting = r2;	 Catch:{ all -> 0x0064 }
            r1 = 1;
            monitor-exit(r3);	 Catch:{ all -> 0x0064 }
            if (r1 != 0) goto L_0x0018;
        L_0x0042:
            r3 = r5.guard;
            monitor-enter(r3);
            r2 = 0;
            r5.emitting = r2;	 Catch:{ all -> 0x004a }
            monitor-exit(r3);	 Catch:{ all -> 0x004a }
            goto L_0x0018;
        L_0x004a:
            r2 = move-exception;
            monitor-exit(r3);	 Catch:{ all -> 0x004a }
            throw r2;
        L_0x004d:
            r2 = 0;
            r5.queue = r2;	 Catch:{ all -> 0x0064 }
            monitor-exit(r3);	 Catch:{ all -> 0x0064 }
            r2 = r5.drain(r0);	 Catch:{ all -> 0x0067 }
            if (r2 != 0) goto L_0x0034;
        L_0x0057:
            if (r1 != 0) goto L_0x0018;
        L_0x0059:
            r3 = r5.guard;
            monitor-enter(r3);
            r2 = 0;
            r5.emitting = r2;	 Catch:{ all -> 0x0061 }
            monitor-exit(r3);	 Catch:{ all -> 0x0061 }
            goto L_0x0018;
        L_0x0061:
            r2 = move-exception;
            monitor-exit(r3);	 Catch:{ all -> 0x0061 }
            throw r2;
        L_0x0064:
            r2 = move-exception;
            monitor-exit(r3);	 Catch:{ all -> 0x0064 }
            throw r2;	 Catch:{ all -> 0x0067 }
        L_0x0067:
            r2 = move-exception;
            if (r1 != 0) goto L_0x0071;
        L_0x006a:
            r3 = r5.guard;
            monitor-enter(r3);
            r4 = 0;
            r5.emitting = r4;	 Catch:{ all -> 0x0072 }
            monitor-exit(r3);	 Catch:{ all -> 0x0072 }
        L_0x0071:
            throw r2;
        L_0x0072:
            r2 = move-exception;
            monitor-exit(r3);	 Catch:{ all -> 0x0072 }
            throw r2;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorWindowWithTime.ExactSubscriber.onNext(java.lang.Object):void");
        }

        boolean drain(List<Object> queue) {
            if (queue == null) {
                return true;
            }
            for (T o : queue) {
                if (o == OperatorWindowWithTime.NEXT_SUBJECT) {
                    if (!replaceSubject()) {
                        return false;
                    }
                } else if (OperatorWindowWithTime.nl.isError(o)) {
                    error(OperatorWindowWithTime.nl.getError(o));
                    return true;
                } else if (OperatorWindowWithTime.nl.isCompleted(o)) {
                    complete();
                    return true;
                } else if (!emitValue(o)) {
                    return false;
                }
            }
            return true;
        }

        boolean replaceSubject() {
            Observer<T> s = this.state.consumer;
            if (s != null) {
                s.onCompleted();
            }
            if (this.child.isUnsubscribed()) {
                this.state = this.state.clear();
                unsubscribe();
                return false;
            }
            UnicastSubject<T> bus = UnicastSubject.create();
            this.state = this.state.create(bus, bus);
            this.child.onNext(bus);
            return true;
        }

        boolean emitValue(T t) {
            State<T> s = this.state;
            if (s.consumer == null) {
                if (!replaceSubject()) {
                    return false;
                }
                s = this.state;
            }
            s.consumer.onNext(t);
            if (s.count == OperatorWindowWithTime.this.size - 1) {
                s.consumer.onCompleted();
                s = s.clear();
            } else {
                s = s.next();
            }
            this.state = s;
            return true;
        }

        public void onError(Throwable e) {
            synchronized (this.guard) {
                if (this.emitting) {
                    this.queue = Collections.singletonList(OperatorWindowWithTime.nl.error(e));
                    return;
                }
                this.queue = null;
                this.emitting = true;
                error(e);
            }
        }

        void error(Throwable e) {
            Observer<T> s = this.state.consumer;
            this.state = this.state.clear();
            if (s != null) {
                s.onError(e);
            }
            this.child.onError(e);
            unsubscribe();
        }

        void complete() {
            Observer<T> s = this.state.consumer;
            this.state = this.state.clear();
            if (s != null) {
                s.onCompleted();
            }
            this.child.onCompleted();
            unsubscribe();
        }

        public void onCompleted() {
            synchronized (this.guard) {
                if (this.emitting) {
                    if (this.queue == null) {
                        this.queue = new ArrayList();
                    }
                    this.queue.add(OperatorWindowWithTime.nl.completed());
                    return;
                }
                List<Object> localQueue = this.queue;
                this.queue = null;
                this.emitting = true;
                try {
                    drain(localQueue);
                    complete();
                } catch (Throwable e) {
                    error(e);
                }
            }
        }

        void scheduleExact() {
            this.worker.schedulePeriodically(new Action0() {
                public void call() {
                    ExactSubscriber.this.nextWindow();
                }
            }, 0, OperatorWindowWithTime.this.timespan, OperatorWindowWithTime.this.unit);
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        void nextWindow() {
            /*
            r5 = this;
            r3 = r5.guard;
            monitor-enter(r3);
            r2 = r5.emitting;	 Catch:{ all -> 0x0033 }
            if (r2 == 0) goto L_0x001b;
        L_0x0007:
            r2 = r5.queue;	 Catch:{ all -> 0x0033 }
            if (r2 != 0) goto L_0x0012;
        L_0x000b:
            r2 = new java.util.ArrayList;	 Catch:{ all -> 0x0033 }
            r2.<init>();	 Catch:{ all -> 0x0033 }
            r5.queue = r2;	 Catch:{ all -> 0x0033 }
        L_0x0012:
            r2 = r5.queue;	 Catch:{ all -> 0x0033 }
            r4 = rx.internal.operators.OperatorWindowWithTime.NEXT_SUBJECT;	 Catch:{ all -> 0x0033 }
            r2.add(r4);	 Catch:{ all -> 0x0033 }
            monitor-exit(r3);	 Catch:{ all -> 0x0033 }
        L_0x001a:
            return;
        L_0x001b:
            r2 = 1;
            r5.emitting = r2;	 Catch:{ all -> 0x0033 }
            monitor-exit(r3);	 Catch:{ all -> 0x0033 }
            r1 = 0;
            r2 = r5.replaceSubject();	 Catch:{ all -> 0x0069 }
            if (r2 != 0) goto L_0x0036;
        L_0x0026:
            if (r1 != 0) goto L_0x001a;
        L_0x0028:
            r3 = r5.guard;
            monitor-enter(r3);
            r2 = 0;
            r5.emitting = r2;	 Catch:{ all -> 0x0030 }
            monitor-exit(r3);	 Catch:{ all -> 0x0030 }
            goto L_0x001a;
        L_0x0030:
            r2 = move-exception;
            monitor-exit(r3);	 Catch:{ all -> 0x0030 }
            throw r2;
        L_0x0033:
            r2 = move-exception;
            monitor-exit(r3);	 Catch:{ all -> 0x0033 }
            throw r2;
        L_0x0036:
            r3 = r5.guard;	 Catch:{ all -> 0x0069 }
            monitor-enter(r3);	 Catch:{ all -> 0x0069 }
            r0 = r5.queue;	 Catch:{ all -> 0x0066 }
            if (r0 != 0) goto L_0x004f;
        L_0x003d:
            r2 = 0;
            r5.emitting = r2;	 Catch:{ all -> 0x0066 }
            r1 = 1;
            monitor-exit(r3);	 Catch:{ all -> 0x0066 }
            if (r1 != 0) goto L_0x001a;
        L_0x0044:
            r3 = r5.guard;
            monitor-enter(r3);
            r2 = 0;
            r5.emitting = r2;	 Catch:{ all -> 0x004c }
            monitor-exit(r3);	 Catch:{ all -> 0x004c }
            goto L_0x001a;
        L_0x004c:
            r2 = move-exception;
            monitor-exit(r3);	 Catch:{ all -> 0x004c }
            throw r2;
        L_0x004f:
            r2 = 0;
            r5.queue = r2;	 Catch:{ all -> 0x0066 }
            monitor-exit(r3);	 Catch:{ all -> 0x0066 }
            r2 = r5.drain(r0);	 Catch:{ all -> 0x0069 }
            if (r2 != 0) goto L_0x0036;
        L_0x0059:
            if (r1 != 0) goto L_0x001a;
        L_0x005b:
            r3 = r5.guard;
            monitor-enter(r3);
            r2 = 0;
            r5.emitting = r2;	 Catch:{ all -> 0x0063 }
            monitor-exit(r3);	 Catch:{ all -> 0x0063 }
            goto L_0x001a;
        L_0x0063:
            r2 = move-exception;
            monitor-exit(r3);	 Catch:{ all -> 0x0063 }
            throw r2;
        L_0x0066:
            r2 = move-exception;
            monitor-exit(r3);	 Catch:{ all -> 0x0066 }
            throw r2;	 Catch:{ all -> 0x0069 }
        L_0x0069:
            r2 = move-exception;
            if (r1 != 0) goto L_0x0073;
        L_0x006c:
            r3 = r5.guard;
            monitor-enter(r3);
            r4 = 0;
            r5.emitting = r4;	 Catch:{ all -> 0x0074 }
            monitor-exit(r3);	 Catch:{ all -> 0x0074 }
        L_0x0073:
            throw r2;
        L_0x0074:
            r2 = move-exception;
            monitor-exit(r3);	 Catch:{ all -> 0x0074 }
            throw r2;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorWindowWithTime.ExactSubscriber.nextWindow():void");
        }
    }

    final class InexactSubscriber extends Subscriber<T> {
        final Subscriber<? super Observable<T>> child;
        final List<CountedSerializedSubject<T>> chunks = new LinkedList();
        boolean done;
        final Object guard = new Object();
        final Worker worker;

        public InexactSubscriber(Subscriber<? super Observable<T>> child, Worker worker) {
            super(child);
            this.child = child;
            this.worker = worker;
        }

        public void onStart() {
            request(Long.MAX_VALUE);
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onNext(T r8) {
            /*
            r7 = this;
            r5 = r7.guard;
            monitor-enter(r5);
            r4 = r7.done;	 Catch:{ all -> 0x0032 }
            if (r4 == 0) goto L_0x0009;
        L_0x0007:
            monitor-exit(r5);	 Catch:{ all -> 0x0032 }
        L_0x0008:
            return;
        L_0x0009:
            r3 = new java.util.ArrayList;	 Catch:{ all -> 0x0032 }
            r4 = r7.chunks;	 Catch:{ all -> 0x0032 }
            r3.<init>(r4);	 Catch:{ all -> 0x0032 }
            r4 = r7.chunks;	 Catch:{ all -> 0x0032 }
            r2 = r4.iterator();	 Catch:{ all -> 0x0032 }
        L_0x0016:
            r4 = r2.hasNext();	 Catch:{ all -> 0x0032 }
            if (r4 == 0) goto L_0x0035;
        L_0x001c:
            r0 = r2.next();	 Catch:{ all -> 0x0032 }
            r0 = (rx.internal.operators.OperatorWindowWithTime.CountedSerializedSubject) r0;	 Catch:{ all -> 0x0032 }
            r4 = r0.count;	 Catch:{ all -> 0x0032 }
            r4 = r4 + 1;
            r0.count = r4;	 Catch:{ all -> 0x0032 }
            r6 = rx.internal.operators.OperatorWindowWithTime.this;	 Catch:{ all -> 0x0032 }
            r6 = r6.size;	 Catch:{ all -> 0x0032 }
            if (r4 != r6) goto L_0x0016;
        L_0x002e:
            r2.remove();	 Catch:{ all -> 0x0032 }
            goto L_0x0016;
        L_0x0032:
            r4 = move-exception;
            monitor-exit(r5);	 Catch:{ all -> 0x0032 }
            throw r4;
        L_0x0035:
            monitor-exit(r5);	 Catch:{ all -> 0x0032 }
            r1 = r3.iterator();
        L_0x003a:
            r4 = r1.hasNext();
            if (r4 == 0) goto L_0x0008;
        L_0x0040:
            r0 = r1.next();
            r0 = (rx.internal.operators.OperatorWindowWithTime.CountedSerializedSubject) r0;
            r4 = r0.consumer;
            r4.onNext(r8);
            r4 = r0.count;
            r5 = rx.internal.operators.OperatorWindowWithTime.this;
            r5 = r5.size;
            if (r4 != r5) goto L_0x003a;
        L_0x0053:
            r4 = r0.consumer;
            r4.onCompleted();
            goto L_0x003a;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorWindowWithTime.InexactSubscriber.onNext(java.lang.Object):void");
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onError(java.lang.Throwable r6) {
            /*
            r5 = this;
            r4 = r5.guard;
            monitor-enter(r4);
            r3 = r5.done;	 Catch:{ all -> 0x002f }
            if (r3 == 0) goto L_0x0009;
        L_0x0007:
            monitor-exit(r4);	 Catch:{ all -> 0x002f }
        L_0x0008:
            return;
        L_0x0009:
            r3 = 1;
            r5.done = r3;	 Catch:{ all -> 0x002f }
            r2 = new java.util.ArrayList;	 Catch:{ all -> 0x002f }
            r3 = r5.chunks;	 Catch:{ all -> 0x002f }
            r2.<init>(r3);	 Catch:{ all -> 0x002f }
            r3 = r5.chunks;	 Catch:{ all -> 0x002f }
            r3.clear();	 Catch:{ all -> 0x002f }
            monitor-exit(r4);	 Catch:{ all -> 0x002f }
            r1 = r2.iterator();
        L_0x001d:
            r3 = r1.hasNext();
            if (r3 == 0) goto L_0x0032;
        L_0x0023:
            r0 = r1.next();
            r0 = (rx.internal.operators.OperatorWindowWithTime.CountedSerializedSubject) r0;
            r3 = r0.consumer;
            r3.onError(r6);
            goto L_0x001d;
        L_0x002f:
            r3 = move-exception;
            monitor-exit(r4);	 Catch:{ all -> 0x002f }
            throw r3;
        L_0x0032:
            r3 = r5.child;
            r3.onError(r6);
            goto L_0x0008;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorWindowWithTime.InexactSubscriber.onError(java.lang.Throwable):void");
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onCompleted() {
            /*
            r5 = this;
            r4 = r5.guard;
            monitor-enter(r4);
            r3 = r5.done;	 Catch:{ all -> 0x002f }
            if (r3 == 0) goto L_0x0009;
        L_0x0007:
            monitor-exit(r4);	 Catch:{ all -> 0x002f }
        L_0x0008:
            return;
        L_0x0009:
            r3 = 1;
            r5.done = r3;	 Catch:{ all -> 0x002f }
            r2 = new java.util.ArrayList;	 Catch:{ all -> 0x002f }
            r3 = r5.chunks;	 Catch:{ all -> 0x002f }
            r2.<init>(r3);	 Catch:{ all -> 0x002f }
            r3 = r5.chunks;	 Catch:{ all -> 0x002f }
            r3.clear();	 Catch:{ all -> 0x002f }
            monitor-exit(r4);	 Catch:{ all -> 0x002f }
            r1 = r2.iterator();
        L_0x001d:
            r3 = r1.hasNext();
            if (r3 == 0) goto L_0x0032;
        L_0x0023:
            r0 = r1.next();
            r0 = (rx.internal.operators.OperatorWindowWithTime.CountedSerializedSubject) r0;
            r3 = r0.consumer;
            r3.onCompleted();
            goto L_0x001d;
        L_0x002f:
            r3 = move-exception;
            monitor-exit(r4);	 Catch:{ all -> 0x002f }
            throw r3;
        L_0x0032:
            r3 = r5.child;
            r3.onCompleted();
            goto L_0x0008;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorWindowWithTime.InexactSubscriber.onCompleted():void");
        }

        void scheduleChunk() {
            this.worker.schedulePeriodically(new Action0() {
                public void call() {
                    InexactSubscriber.this.startNewChunk();
                }
            }, OperatorWindowWithTime.this.timeshift, OperatorWindowWithTime.this.timeshift, OperatorWindowWithTime.this.unit);
        }

        void startNewChunk() {
            final CountedSerializedSubject<T> chunk = createCountedSerializedSubject();
            synchronized (this.guard) {
                if (this.done) {
                    return;
                }
                this.chunks.add(chunk);
                try {
                    this.child.onNext(chunk.producer);
                    this.worker.schedule(new Action0() {
                        public void call() {
                            InexactSubscriber.this.terminateChunk(chunk);
                        }
                    }, OperatorWindowWithTime.this.timespan, OperatorWindowWithTime.this.unit);
                } catch (Throwable e) {
                    onError(e);
                }
            }
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        void terminateChunk(rx.internal.operators.OperatorWindowWithTime.CountedSerializedSubject<T> r6) {
            /*
            r5 = this;
            r2 = 0;
            r4 = r5.guard;
            monitor-enter(r4);
            r3 = r5.done;	 Catch:{ all -> 0x002b }
            if (r3 == 0) goto L_0x000a;
        L_0x0008:
            monitor-exit(r4);	 Catch:{ all -> 0x002b }
        L_0x0009:
            return;
        L_0x000a:
            r3 = r5.chunks;	 Catch:{ all -> 0x002b }
            r1 = r3.iterator();	 Catch:{ all -> 0x002b }
        L_0x0010:
            r3 = r1.hasNext();	 Catch:{ all -> 0x002b }
            if (r3 == 0) goto L_0x0022;
        L_0x0016:
            r0 = r1.next();	 Catch:{ all -> 0x002b }
            r0 = (rx.internal.operators.OperatorWindowWithTime.CountedSerializedSubject) r0;	 Catch:{ all -> 0x002b }
            if (r0 != r6) goto L_0x0010;
        L_0x001e:
            r2 = 1;
            r1.remove();	 Catch:{ all -> 0x002b }
        L_0x0022:
            monitor-exit(r4);	 Catch:{ all -> 0x002b }
            if (r2 == 0) goto L_0x0009;
        L_0x0025:
            r3 = r6.consumer;
            r3.onCompleted();
            goto L_0x0009;
        L_0x002b:
            r3 = move-exception;
            monitor-exit(r4);	 Catch:{ all -> 0x002b }
            throw r3;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorWindowWithTime.InexactSubscriber.terminateChunk(rx.internal.operators.OperatorWindowWithTime$CountedSerializedSubject):void");
        }

        CountedSerializedSubject<T> createCountedSerializedSubject() {
            UnicastSubject<T> bus = UnicastSubject.create();
            return new CountedSerializedSubject(bus, bus);
        }
    }

    static final class State<T> {
        static final State<Object> EMPTY = new State(null, null, 0);
        final Observer<T> consumer;
        final int count;
        final Observable<T> producer;

        public State(Observer<T> consumer, Observable<T> producer, int count) {
            this.consumer = consumer;
            this.producer = producer;
            this.count = count;
        }

        public State<T> next() {
            return new State(this.consumer, this.producer, this.count + 1);
        }

        public State<T> create(Observer<T> consumer, Observable<T> producer) {
            return new State(consumer, producer, 0);
        }

        public State<T> clear() {
            return empty();
        }

        public static <T> State<T> empty() {
            return EMPTY;
        }
    }

    public OperatorWindowWithTime(long timespan, long timeshift, TimeUnit unit, int size, Scheduler scheduler) {
        this.timespan = timespan;
        this.timeshift = timeshift;
        this.unit = unit;
        this.size = size;
        this.scheduler = scheduler;
    }

    public Subscriber<? super T> call(Subscriber<? super Observable<T>> child) {
        Worker worker = this.scheduler.createWorker();
        if (this.timespan == this.timeshift) {
            ExactSubscriber s = new ExactSubscriber(child, worker);
            s.add(worker);
            s.scheduleExact();
            return s;
        }
        Subscriber s2 = new InexactSubscriber(child, worker);
        s2.add(worker);
        s2.startNewChunk();
        s2.scheduleChunk();
        return s2;
    }
}
