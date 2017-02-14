package rx.internal.operators;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import rx.Observable;
import rx.Observable$Operator;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func1;
import rx.observers.SerializedSubscriber;
import rx.subscriptions.CompositeSubscription;

public final class OperatorBufferWithStartEndObservable<T, TOpening, TClosing> implements Observable$Operator<List<T>, T> {
    final Func1<? super TOpening, ? extends Observable<? extends TClosing>> bufferClosing;
    final Observable<? extends TOpening> bufferOpening;

    final class BufferingSubscriber extends Subscriber<T> {
        final Subscriber<? super List<T>> child;
        final List<List<T>> chunks = new LinkedList();
        final CompositeSubscription closingSubscriptions = new CompositeSubscription();
        boolean done;

        public BufferingSubscriber(Subscriber<? super List<T>> child) {
            this.child = child;
            add(this.closingSubscriptions);
        }

        public void onNext(T t) {
            synchronized (this) {
                for (List<T> chunk : this.chunks) {
                    chunk.add(t);
                }
            }
        }

        public void onError(Throwable e) {
            synchronized (this) {
                if (this.done) {
                    return;
                }
                this.done = true;
                this.chunks.clear();
                this.child.onError(e);
                unsubscribe();
            }
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onCompleted() {
            /*
            r5 = this;
            monitor-enter(r5);	 Catch:{ Throwable -> 0x002d }
            r4 = r5.done;	 Catch:{ all -> 0x0034 }
            if (r4 == 0) goto L_0x0007;
        L_0x0005:
            monitor-exit(r5);	 Catch:{ all -> 0x0034 }
        L_0x0006:
            return;
        L_0x0007:
            r4 = 1;
            r5.done = r4;	 Catch:{ all -> 0x0034 }
            r3 = new java.util.LinkedList;	 Catch:{ all -> 0x0034 }
            r4 = r5.chunks;	 Catch:{ all -> 0x0034 }
            r3.<init>(r4);	 Catch:{ all -> 0x0034 }
            r4 = r5.chunks;	 Catch:{ all -> 0x0034 }
            r4.clear();	 Catch:{ all -> 0x0034 }
            monitor-exit(r5);	 Catch:{ all -> 0x0034 }
            r1 = r3.iterator();	 Catch:{ Throwable -> 0x002d }
        L_0x001b:
            r4 = r1.hasNext();	 Catch:{ Throwable -> 0x002d }
            if (r4 == 0) goto L_0x0037;
        L_0x0021:
            r0 = r1.next();	 Catch:{ Throwable -> 0x002d }
            r0 = (java.util.List) r0;	 Catch:{ Throwable -> 0x002d }
            r4 = r5.child;	 Catch:{ Throwable -> 0x002d }
            r4.onNext(r0);	 Catch:{ Throwable -> 0x002d }
            goto L_0x001b;
        L_0x002d:
            r2 = move-exception;
            r4 = r5.child;
            rx.exceptions.Exceptions.throwOrReport(r2, r4);
            goto L_0x0006;
        L_0x0034:
            r4 = move-exception;
            monitor-exit(r5);	 Catch:{ all -> 0x0034 }
            throw r4;	 Catch:{ Throwable -> 0x002d }
        L_0x0037:
            r4 = r5.child;
            r4.onCompleted();
            r5.unsubscribe();
            goto L_0x0006;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorBufferWithStartEndObservable.BufferingSubscriber.onCompleted():void");
        }

        void startBuffer(TOpening v) {
            final List<T> chunk = new ArrayList();
            synchronized (this) {
                if (this.done) {
                    return;
                }
                this.chunks.add(chunk);
                try {
                    Observable<? extends TClosing> cobs = (Observable) OperatorBufferWithStartEndObservable.this.bufferClosing.call(v);
                    Subscriber<TClosing> closeSubscriber = new Subscriber<TClosing>() {
                        public void onNext(TClosing tClosing) {
                            BufferingSubscriber.this.closingSubscriptions.remove(this);
                            BufferingSubscriber.this.endBuffer(chunk);
                        }

                        public void onError(Throwable e) {
                            BufferingSubscriber.this.onError(e);
                        }

                        public void onCompleted() {
                            BufferingSubscriber.this.closingSubscriptions.remove(this);
                            BufferingSubscriber.this.endBuffer(chunk);
                        }
                    };
                    this.closingSubscriptions.add(closeSubscriber);
                    cobs.unsafeSubscribe(closeSubscriber);
                } catch (Throwable t) {
                    Exceptions.throwOrReport(t, this);
                }
            }
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        void endBuffer(java.util.List<T> r5) {
            /*
            r4 = this;
            r0 = 0;
            monitor-enter(r4);
            r3 = r4.done;	 Catch:{ all -> 0x0029 }
            if (r3 == 0) goto L_0x0008;
        L_0x0006:
            monitor-exit(r4);	 Catch:{ all -> 0x0029 }
        L_0x0007:
            return;
        L_0x0008:
            r3 = r4.chunks;	 Catch:{ all -> 0x0029 }
            r2 = r3.iterator();	 Catch:{ all -> 0x0029 }
        L_0x000e:
            r3 = r2.hasNext();	 Catch:{ all -> 0x0029 }
            if (r3 == 0) goto L_0x0020;
        L_0x0014:
            r1 = r2.next();	 Catch:{ all -> 0x0029 }
            r1 = (java.util.List) r1;	 Catch:{ all -> 0x0029 }
            if (r1 != r5) goto L_0x000e;
        L_0x001c:
            r0 = 1;
            r2.remove();	 Catch:{ all -> 0x0029 }
        L_0x0020:
            monitor-exit(r4);	 Catch:{ all -> 0x0029 }
            if (r0 == 0) goto L_0x0007;
        L_0x0023:
            r3 = r4.child;
            r3.onNext(r5);
            goto L_0x0007;
        L_0x0029:
            r3 = move-exception;
            monitor-exit(r4);	 Catch:{ all -> 0x0029 }
            throw r3;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorBufferWithStartEndObservable.BufferingSubscriber.endBuffer(java.util.List):void");
        }
    }

    public OperatorBufferWithStartEndObservable(Observable<? extends TOpening> bufferOpenings, Func1<? super TOpening, ? extends Observable<? extends TClosing>> bufferClosingSelector) {
        this.bufferOpening = bufferOpenings;
        this.bufferClosing = bufferClosingSelector;
    }

    public Subscriber<? super T> call(Subscriber<? super List<T>> child) {
        final BufferingSubscriber bsub = new BufferingSubscriber(new SerializedSubscriber(child));
        Subscriber<TOpening> openSubscriber = new Subscriber<TOpening>() {
            public void onNext(TOpening t) {
                bsub.startBuffer(t);
            }

            public void onError(Throwable e) {
                bsub.onError(e);
            }

            public void onCompleted() {
                bsub.onCompleted();
            }
        };
        child.add(openSubscriber);
        child.add(bsub);
        this.bufferOpening.unsafeSubscribe(openSubscriber);
        return bsub;
    }
}
