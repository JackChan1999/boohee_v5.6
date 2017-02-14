package rx.internal.operators;

import java.util.LinkedList;
import java.util.List;
import rx.Observable;
import rx.Observable$Operator;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Func1;
import rx.observers.SerializedObserver;
import rx.observers.SerializedSubscriber;
import rx.subscriptions.CompositeSubscription;

public final class OperatorWindowWithStartEndObservable<T, U, V> implements Observable$Operator<Observable<T>, T> {
    final Func1<? super U, ? extends Observable<? extends V>> windowClosingSelector;
    final Observable<? extends U> windowOpenings;

    static final class SerializedSubject<T> {
        final Observer<T> consumer;
        final Observable<T> producer;

        public SerializedSubject(Observer<T> consumer, Observable<T> producer) {
            this.consumer = new SerializedObserver(consumer);
            this.producer = producer;
        }
    }

    final class SourceSubscriber extends Subscriber<T> {
        final Subscriber<? super Observable<T>> child;
        final List<SerializedSubject<T>> chunks = new LinkedList();
        final CompositeSubscription csub;
        boolean done;
        final Object guard = new Object();

        public SourceSubscriber(Subscriber<? super Observable<T>> child, CompositeSubscription csub) {
            this.child = new SerializedSubscriber(child);
            this.csub = csub;
        }

        public void onStart() {
            request(Long.MAX_VALUE);
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onNext(T r6) {
            /*
            r5 = this;
            r4 = r5.guard;
            monitor-enter(r4);
            r3 = r5.done;	 Catch:{ all -> 0x0027 }
            if (r3 == 0) goto L_0x0009;
        L_0x0007:
            monitor-exit(r4);	 Catch:{ all -> 0x0027 }
        L_0x0008:
            return;
        L_0x0009:
            r2 = new java.util.ArrayList;	 Catch:{ all -> 0x0027 }
            r3 = r5.chunks;	 Catch:{ all -> 0x0027 }
            r2.<init>(r3);	 Catch:{ all -> 0x0027 }
            monitor-exit(r4);	 Catch:{ all -> 0x0027 }
            r1 = r2.iterator();
        L_0x0015:
            r3 = r1.hasNext();
            if (r3 == 0) goto L_0x0008;
        L_0x001b:
            r0 = r1.next();
            r0 = (rx.internal.operators.OperatorWindowWithStartEndObservable.SerializedSubject) r0;
            r3 = r0.consumer;
            r3.onNext(r6);
            goto L_0x0015;
        L_0x0027:
            r3 = move-exception;
            monitor-exit(r4);	 Catch:{ all -> 0x0027 }
            throw r3;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorWindowWithStartEndObservable.SourceSubscriber.onNext(java.lang.Object):void");
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onError(java.lang.Throwable r6) {
            /*
            r5 = this;
            r4 = r5.guard;	 Catch:{ all -> 0x0034 }
            monitor-enter(r4);	 Catch:{ all -> 0x0034 }
            r3 = r5.done;	 Catch:{ all -> 0x003b }
            if (r3 == 0) goto L_0x000e;
        L_0x0007:
            monitor-exit(r4);	 Catch:{ all -> 0x003b }
            r3 = r5.csub;
            r3.unsubscribe();
        L_0x000d:
            return;
        L_0x000e:
            r3 = 1;
            r5.done = r3;	 Catch:{ all -> 0x003b }
            r2 = new java.util.ArrayList;	 Catch:{ all -> 0x003b }
            r3 = r5.chunks;	 Catch:{ all -> 0x003b }
            r2.<init>(r3);	 Catch:{ all -> 0x003b }
            r3 = r5.chunks;	 Catch:{ all -> 0x003b }
            r3.clear();	 Catch:{ all -> 0x003b }
            monitor-exit(r4);	 Catch:{ all -> 0x003b }
            r1 = r2.iterator();	 Catch:{ all -> 0x0034 }
        L_0x0022:
            r3 = r1.hasNext();	 Catch:{ all -> 0x0034 }
            if (r3 == 0) goto L_0x003e;
        L_0x0028:
            r0 = r1.next();	 Catch:{ all -> 0x0034 }
            r0 = (rx.internal.operators.OperatorWindowWithStartEndObservable.SerializedSubject) r0;	 Catch:{ all -> 0x0034 }
            r3 = r0.consumer;	 Catch:{ all -> 0x0034 }
            r3.onError(r6);	 Catch:{ all -> 0x0034 }
            goto L_0x0022;
        L_0x0034:
            r3 = move-exception;
            r4 = r5.csub;
            r4.unsubscribe();
            throw r3;
        L_0x003b:
            r3 = move-exception;
            monitor-exit(r4);	 Catch:{ all -> 0x003b }
            throw r3;	 Catch:{ all -> 0x0034 }
        L_0x003e:
            r3 = r5.child;	 Catch:{ all -> 0x0034 }
            r3.onError(r6);	 Catch:{ all -> 0x0034 }
            r3 = r5.csub;
            r3.unsubscribe();
            goto L_0x000d;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorWindowWithStartEndObservable.SourceSubscriber.onError(java.lang.Throwable):void");
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onCompleted() {
            /*
            r5 = this;
            r4 = r5.guard;	 Catch:{ all -> 0x0034 }
            monitor-enter(r4);	 Catch:{ all -> 0x0034 }
            r3 = r5.done;	 Catch:{ all -> 0x003b }
            if (r3 == 0) goto L_0x000e;
        L_0x0007:
            monitor-exit(r4);	 Catch:{ all -> 0x003b }
            r3 = r5.csub;
            r3.unsubscribe();
        L_0x000d:
            return;
        L_0x000e:
            r3 = 1;
            r5.done = r3;	 Catch:{ all -> 0x003b }
            r2 = new java.util.ArrayList;	 Catch:{ all -> 0x003b }
            r3 = r5.chunks;	 Catch:{ all -> 0x003b }
            r2.<init>(r3);	 Catch:{ all -> 0x003b }
            r3 = r5.chunks;	 Catch:{ all -> 0x003b }
            r3.clear();	 Catch:{ all -> 0x003b }
            monitor-exit(r4);	 Catch:{ all -> 0x003b }
            r1 = r2.iterator();	 Catch:{ all -> 0x0034 }
        L_0x0022:
            r3 = r1.hasNext();	 Catch:{ all -> 0x0034 }
            if (r3 == 0) goto L_0x003e;
        L_0x0028:
            r0 = r1.next();	 Catch:{ all -> 0x0034 }
            r0 = (rx.internal.operators.OperatorWindowWithStartEndObservable.SerializedSubject) r0;	 Catch:{ all -> 0x0034 }
            r3 = r0.consumer;	 Catch:{ all -> 0x0034 }
            r3.onCompleted();	 Catch:{ all -> 0x0034 }
            goto L_0x0022;
        L_0x0034:
            r3 = move-exception;
            r4 = r5.csub;
            r4.unsubscribe();
            throw r3;
        L_0x003b:
            r3 = move-exception;
            monitor-exit(r4);	 Catch:{ all -> 0x003b }
            throw r3;	 Catch:{ all -> 0x0034 }
        L_0x003e:
            r3 = r5.child;	 Catch:{ all -> 0x0034 }
            r3.onCompleted();	 Catch:{ all -> 0x0034 }
            r3 = r5.csub;
            r3.unsubscribe();
            goto L_0x000d;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorWindowWithStartEndObservable.SourceSubscriber.onCompleted():void");
        }

        void beginWindow(U token) {
            final SerializedSubject<T> s = createSerializedSubject();
            synchronized (this.guard) {
                if (this.done) {
                    return;
                }
                this.chunks.add(s);
                this.child.onNext(s.producer);
                try {
                    Observable<? extends V> end = (Observable) OperatorWindowWithStartEndObservable.this.windowClosingSelector.call(token);
                    Subscriber<V> v = new Subscriber<V>() {
                        boolean once = true;

                        public void onNext(V v) {
                            onCompleted();
                        }

                        public void onError(Throwable e) {
                        }

                        public void onCompleted() {
                            if (this.once) {
                                this.once = false;
                                SourceSubscriber.this.endWindow(s);
                                SourceSubscriber.this.csub.remove(this);
                            }
                        }
                    };
                    this.csub.add(v);
                    end.unsafeSubscribe(v);
                } catch (Throwable e) {
                    onError(e);
                }
            }
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        void endWindow(rx.internal.operators.OperatorWindowWithStartEndObservable.SerializedSubject<T> r6) {
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
            r0 = r3.iterator();	 Catch:{ all -> 0x002b }
        L_0x0010:
            r3 = r0.hasNext();	 Catch:{ all -> 0x002b }
            if (r3 == 0) goto L_0x0022;
        L_0x0016:
            r1 = r0.next();	 Catch:{ all -> 0x002b }
            r1 = (rx.internal.operators.OperatorWindowWithStartEndObservable.SerializedSubject) r1;	 Catch:{ all -> 0x002b }
            if (r1 != r6) goto L_0x0010;
        L_0x001e:
            r2 = 1;
            r0.remove();	 Catch:{ all -> 0x002b }
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
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorWindowWithStartEndObservable.SourceSubscriber.endWindow(rx.internal.operators.OperatorWindowWithStartEndObservable$SerializedSubject):void");
        }

        SerializedSubject<T> createSerializedSubject() {
            UnicastSubject<T> bus = UnicastSubject.create();
            return new SerializedSubject(bus, bus);
        }
    }

    public OperatorWindowWithStartEndObservable(Observable<? extends U> windowOpenings, Func1<? super U, ? extends Observable<? extends V>> windowClosingSelector) {
        this.windowOpenings = windowOpenings;
        this.windowClosingSelector = windowClosingSelector;
    }

    public Subscriber<? super T> call(Subscriber<? super Observable<T>> child) {
        CompositeSubscription csub = new CompositeSubscription();
        child.add(csub);
        final SourceSubscriber sub = new SourceSubscriber(child, csub);
        Subscriber<U> open = new Subscriber<U>() {
            public void onStart() {
                request(Long.MAX_VALUE);
            }

            public void onNext(U t) {
                sub.beginWindow(t);
            }

            public void onError(Throwable e) {
                sub.onError(e);
            }

            public void onCompleted() {
                sub.onCompleted();
            }
        };
        csub.add(sub);
        csub.add(open);
        this.windowOpenings.unsafeSubscribe(open);
        return sub;
    }
}
