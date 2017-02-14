package rx.internal.operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import rx.Observable;
import rx.Observable$Operator;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Func0;
import rx.observers.SerializedSubscriber;
import rx.subscriptions.SerialSubscription;

public final class OperatorWindowWithObservableFactory<T, U> implements Observable$Operator<Observable<T>, T> {
    static final Object NEXT_SUBJECT = new Object();
    static final NotificationLite<Object> nl = NotificationLite.instance();
    final Func0<? extends Observable<? extends U>> otherFactory;

    static final class BoundarySubscriber<T, U> extends Subscriber<U> {
        boolean done;
        final SourceSubscriber<T, U> sub;

        public BoundarySubscriber(Subscriber<?> subscriber, SourceSubscriber<T, U> sub) {
            this.sub = sub;
        }

        public void onStart() {
            request(Long.MAX_VALUE);
        }

        public void onNext(U u) {
            if (!this.done) {
                this.done = true;
                this.sub.replaceWindow();
            }
        }

        public void onError(Throwable e) {
            this.sub.onError(e);
        }

        public void onCompleted() {
            if (!this.done) {
                this.done = true;
                this.sub.onCompleted();
            }
        }
    }

    static final class SourceSubscriber<T, U> extends Subscriber<T> {
        final Subscriber<? super Observable<T>> child;
        Observer<T> consumer;
        boolean emitting;
        final Object guard = new Object();
        final Func0<? extends Observable<? extends U>> otherFactory;
        Observable<T> producer;
        List<Object> queue;
        final SerialSubscription ssub = new SerialSubscription();

        public SourceSubscriber(Subscriber<? super Observable<T>> child, Func0<? extends Observable<? extends U>> otherFactory) {
            this.child = new SerializedSubscriber(child);
            this.otherFactory = otherFactory;
            add(this.ssub);
        }

        public void onStart() {
            request(Long.MAX_VALUE);
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onNext(T r7) {
            /*
            r6 = this;
            r4 = r6.guard;
            monitor-enter(r4);
            r3 = r6.emitting;	 Catch:{ all -> 0x0049 }
            if (r3 == 0) goto L_0x0019;
        L_0x0007:
            r3 = r6.queue;	 Catch:{ all -> 0x0049 }
            if (r3 != 0) goto L_0x0012;
        L_0x000b:
            r3 = new java.util.ArrayList;	 Catch:{ all -> 0x0049 }
            r3.<init>();	 Catch:{ all -> 0x0049 }
            r6.queue = r3;	 Catch:{ all -> 0x0049 }
        L_0x0012:
            r3 = r6.queue;	 Catch:{ all -> 0x0049 }
            r3.add(r7);	 Catch:{ all -> 0x0049 }
            monitor-exit(r4);	 Catch:{ all -> 0x0049 }
        L_0x0018:
            return;
        L_0x0019:
            r0 = r6.queue;	 Catch:{ all -> 0x0049 }
            r3 = 0;
            r6.queue = r3;	 Catch:{ all -> 0x0049 }
            r3 = 1;
            r6.emitting = r3;	 Catch:{ all -> 0x0049 }
            monitor-exit(r4);	 Catch:{ all -> 0x0049 }
            r1 = 1;
            r2 = 0;
        L_0x0024:
            r6.drain(r0);	 Catch:{ all -> 0x0065 }
            if (r1 == 0) goto L_0x002d;
        L_0x0029:
            r1 = 0;
            r6.emitValue(r7);	 Catch:{ all -> 0x0065 }
        L_0x002d:
            r4 = r6.guard;	 Catch:{ all -> 0x0065 }
            monitor-enter(r4);	 Catch:{ all -> 0x0065 }
            r0 = r6.queue;	 Catch:{ all -> 0x0062 }
            r3 = 0;
            r6.queue = r3;	 Catch:{ all -> 0x0062 }
            if (r0 != 0) goto L_0x004c;
        L_0x0037:
            r3 = 0;
            r6.emitting = r3;	 Catch:{ all -> 0x0062 }
            r2 = 1;
            monitor-exit(r4);	 Catch:{ all -> 0x0062 }
            if (r2 != 0) goto L_0x0018;
        L_0x003e:
            r4 = r6.guard;
            monitor-enter(r4);
            r3 = 0;
            r6.emitting = r3;	 Catch:{ all -> 0x0046 }
            monitor-exit(r4);	 Catch:{ all -> 0x0046 }
            goto L_0x0018;
        L_0x0046:
            r3 = move-exception;
            monitor-exit(r4);	 Catch:{ all -> 0x0046 }
            throw r3;
        L_0x0049:
            r3 = move-exception;
            monitor-exit(r4);	 Catch:{ all -> 0x0049 }
            throw r3;
        L_0x004c:
            monitor-exit(r4);	 Catch:{ all -> 0x0062 }
            r3 = r6.child;	 Catch:{ all -> 0x0065 }
            r3 = r3.isUnsubscribed();	 Catch:{ all -> 0x0065 }
            if (r3 == 0) goto L_0x0024;
        L_0x0055:
            if (r2 != 0) goto L_0x0018;
        L_0x0057:
            r4 = r6.guard;
            monitor-enter(r4);
            r3 = 0;
            r6.emitting = r3;	 Catch:{ all -> 0x005f }
            monitor-exit(r4);	 Catch:{ all -> 0x005f }
            goto L_0x0018;
        L_0x005f:
            r3 = move-exception;
            monitor-exit(r4);	 Catch:{ all -> 0x005f }
            throw r3;
        L_0x0062:
            r3 = move-exception;
            monitor-exit(r4);	 Catch:{ all -> 0x0062 }
            throw r3;	 Catch:{ all -> 0x0065 }
        L_0x0065:
            r3 = move-exception;
            if (r2 != 0) goto L_0x006f;
        L_0x0068:
            r4 = r6.guard;
            monitor-enter(r4);
            r5 = 0;
            r6.emitting = r5;	 Catch:{ all -> 0x0070 }
            monitor-exit(r4);	 Catch:{ all -> 0x0070 }
        L_0x006f:
            throw r3;
        L_0x0070:
            r3 = move-exception;
            monitor-exit(r4);	 Catch:{ all -> 0x0070 }
            throw r3;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorWindowWithObservableFactory.SourceSubscriber.onNext(java.lang.Object):void");
        }

        void drain(List<Object> queue) {
            if (queue != null) {
                for (T o : queue) {
                    if (o == OperatorWindowWithObservableFactory.NEXT_SUBJECT) {
                        replaceSubject();
                    } else if (OperatorWindowWithObservableFactory.nl.isError(o)) {
                        error(OperatorWindowWithObservableFactory.nl.getError(o));
                        return;
                    } else if (OperatorWindowWithObservableFactory.nl.isCompleted(o)) {
                        complete();
                        return;
                    } else {
                        emitValue(o);
                    }
                }
            }
        }

        void replaceSubject() {
            Observer<T> s = this.consumer;
            if (s != null) {
                s.onCompleted();
            }
            createNewWindow();
            this.child.onNext(this.producer);
        }

        void createNewWindow() {
            UnicastSubject<T> bus = UnicastSubject.create();
            this.consumer = bus;
            this.producer = bus;
            try {
                Observable<? extends U> other = (Observable) this.otherFactory.call();
                BoundarySubscriber<T, U> bs = new BoundarySubscriber(this.child, this);
                this.ssub.set(bs);
                other.unsafeSubscribe(bs);
            } catch (Throwable e) {
                this.child.onError(e);
                unsubscribe();
            }
        }

        void emitValue(T t) {
            Observer<T> s = this.consumer;
            if (s != null) {
                s.onNext(t);
            }
        }

        public void onError(Throwable e) {
            synchronized (this.guard) {
                if (this.emitting) {
                    this.queue = Collections.singletonList(OperatorWindowWithObservableFactory.nl.error(e));
                    return;
                }
                this.queue = null;
                this.emitting = true;
                error(e);
            }
        }

        public void onCompleted() {
            synchronized (this.guard) {
                if (this.emitting) {
                    if (this.queue == null) {
                        this.queue = new ArrayList();
                    }
                    this.queue.add(OperatorWindowWithObservableFactory.nl.completed());
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

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        void replaceWindow() {
            /*
            r6 = this;
            r4 = r6.guard;
            monitor-enter(r4);
            r3 = r6.emitting;	 Catch:{ all -> 0x004b }
            if (r3 == 0) goto L_0x001b;
        L_0x0007:
            r3 = r6.queue;	 Catch:{ all -> 0x004b }
            if (r3 != 0) goto L_0x0012;
        L_0x000b:
            r3 = new java.util.ArrayList;	 Catch:{ all -> 0x004b }
            r3.<init>();	 Catch:{ all -> 0x004b }
            r6.queue = r3;	 Catch:{ all -> 0x004b }
        L_0x0012:
            r3 = r6.queue;	 Catch:{ all -> 0x004b }
            r5 = rx.internal.operators.OperatorWindowWithObservableFactory.NEXT_SUBJECT;	 Catch:{ all -> 0x004b }
            r3.add(r5);	 Catch:{ all -> 0x004b }
            monitor-exit(r4);	 Catch:{ all -> 0x004b }
        L_0x001a:
            return;
        L_0x001b:
            r0 = r6.queue;	 Catch:{ all -> 0x004b }
            r3 = 0;
            r6.queue = r3;	 Catch:{ all -> 0x004b }
            r3 = 1;
            r6.emitting = r3;	 Catch:{ all -> 0x004b }
            monitor-exit(r4);	 Catch:{ all -> 0x004b }
            r1 = 1;
            r2 = 0;
        L_0x0026:
            r6.drain(r0);	 Catch:{ all -> 0x0067 }
            if (r1 == 0) goto L_0x002f;
        L_0x002b:
            r1 = 0;
            r6.replaceSubject();	 Catch:{ all -> 0x0067 }
        L_0x002f:
            r4 = r6.guard;	 Catch:{ all -> 0x0067 }
            monitor-enter(r4);	 Catch:{ all -> 0x0067 }
            r0 = r6.queue;	 Catch:{ all -> 0x0064 }
            r3 = 0;
            r6.queue = r3;	 Catch:{ all -> 0x0064 }
            if (r0 != 0) goto L_0x004e;
        L_0x0039:
            r3 = 0;
            r6.emitting = r3;	 Catch:{ all -> 0x0064 }
            r2 = 1;
            monitor-exit(r4);	 Catch:{ all -> 0x0064 }
            if (r2 != 0) goto L_0x001a;
        L_0x0040:
            r4 = r6.guard;
            monitor-enter(r4);
            r3 = 0;
            r6.emitting = r3;	 Catch:{ all -> 0x0048 }
            monitor-exit(r4);	 Catch:{ all -> 0x0048 }
            goto L_0x001a;
        L_0x0048:
            r3 = move-exception;
            monitor-exit(r4);	 Catch:{ all -> 0x0048 }
            throw r3;
        L_0x004b:
            r3 = move-exception;
            monitor-exit(r4);	 Catch:{ all -> 0x004b }
            throw r3;
        L_0x004e:
            monitor-exit(r4);	 Catch:{ all -> 0x0064 }
            r3 = r6.child;	 Catch:{ all -> 0x0067 }
            r3 = r3.isUnsubscribed();	 Catch:{ all -> 0x0067 }
            if (r3 == 0) goto L_0x0026;
        L_0x0057:
            if (r2 != 0) goto L_0x001a;
        L_0x0059:
            r4 = r6.guard;
            monitor-enter(r4);
            r3 = 0;
            r6.emitting = r3;	 Catch:{ all -> 0x0061 }
            monitor-exit(r4);	 Catch:{ all -> 0x0061 }
            goto L_0x001a;
        L_0x0061:
            r3 = move-exception;
            monitor-exit(r4);	 Catch:{ all -> 0x0061 }
            throw r3;
        L_0x0064:
            r3 = move-exception;
            monitor-exit(r4);	 Catch:{ all -> 0x0064 }
            throw r3;	 Catch:{ all -> 0x0067 }
        L_0x0067:
            r3 = move-exception;
            if (r2 != 0) goto L_0x0071;
        L_0x006a:
            r4 = r6.guard;
            monitor-enter(r4);
            r5 = 0;
            r6.emitting = r5;	 Catch:{ all -> 0x0072 }
            monitor-exit(r4);	 Catch:{ all -> 0x0072 }
        L_0x0071:
            throw r3;
        L_0x0072:
            r3 = move-exception;
            monitor-exit(r4);	 Catch:{ all -> 0x0072 }
            throw r3;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorWindowWithObservableFactory.SourceSubscriber.replaceWindow():void");
        }

        void complete() {
            Observer<T> s = this.consumer;
            this.consumer = null;
            this.producer = null;
            if (s != null) {
                s.onCompleted();
            }
            this.child.onCompleted();
            unsubscribe();
        }

        void error(Throwable e) {
            Observer<T> s = this.consumer;
            this.consumer = null;
            this.producer = null;
            if (s != null) {
                s.onError(e);
            }
            this.child.onError(e);
            unsubscribe();
        }
    }

    public OperatorWindowWithObservableFactory(Func0<? extends Observable<? extends U>> otherFactory) {
        this.otherFactory = otherFactory;
    }

    public Subscriber<? super T> call(Subscriber<? super Observable<T>> child) {
        SourceSubscriber<T, U> sub = new SourceSubscriber(child, this.otherFactory);
        child.add(sub);
        sub.replaceWindow();
        return sub;
    }
}
