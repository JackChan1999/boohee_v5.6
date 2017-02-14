package rx.internal.operators;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import rx.Observable$Operator;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Action0;
import rx.observers.SerializedSubscriber;

public final class OperatorBufferWithTime<T> implements Observable$Operator<List<T>, T> {
    final int count;
    final Scheduler scheduler;
    final long timeshift;
    final long timespan;
    final TimeUnit unit;

    final class ExactSubscriber extends Subscriber<T> {
        final Subscriber<? super List<T>> child;
        List<T> chunk = new ArrayList();
        boolean done;
        final Worker inner;

        public ExactSubscriber(Subscriber<? super List<T>> child, Worker inner) {
            this.child = child;
            this.inner = inner;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onNext(T r4) {
            /*
            r3 = this;
            r0 = 0;
            monitor-enter(r3);
            r1 = r3.done;	 Catch:{ all -> 0x002b }
            if (r1 == 0) goto L_0x0008;
        L_0x0006:
            monitor-exit(r3);	 Catch:{ all -> 0x002b }
        L_0x0007:
            return;
        L_0x0008:
            r1 = r3.chunk;	 Catch:{ all -> 0x002b }
            r1.add(r4);	 Catch:{ all -> 0x002b }
            r1 = r3.chunk;	 Catch:{ all -> 0x002b }
            r1 = r1.size();	 Catch:{ all -> 0x002b }
            r2 = rx.internal.operators.OperatorBufferWithTime.this;	 Catch:{ all -> 0x002b }
            r2 = r2.count;	 Catch:{ all -> 0x002b }
            if (r1 != r2) goto L_0x0022;
        L_0x0019:
            r0 = r3.chunk;	 Catch:{ all -> 0x002b }
            r1 = new java.util.ArrayList;	 Catch:{ all -> 0x002b }
            r1.<init>();	 Catch:{ all -> 0x002b }
            r3.chunk = r1;	 Catch:{ all -> 0x002b }
        L_0x0022:
            monitor-exit(r3);	 Catch:{ all -> 0x002b }
            if (r0 == 0) goto L_0x0007;
        L_0x0025:
            r1 = r3.child;
            r1.onNext(r0);
            goto L_0x0007;
        L_0x002b:
            r1 = move-exception;
            monitor-exit(r3);	 Catch:{ all -> 0x002b }
            throw r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorBufferWithTime.ExactSubscriber.onNext(java.lang.Object):void");
        }

        public void onError(Throwable e) {
            synchronized (this) {
                if (this.done) {
                    return;
                }
                this.done = true;
                this.chunk = null;
                this.child.onError(e);
                unsubscribe();
            }
        }

        public void onCompleted() {
            try {
                this.inner.unsubscribe();
                synchronized (this) {
                    if (this.done) {
                        return;
                    }
                    this.done = true;
                    List<T> toEmit = this.chunk;
                    this.chunk = null;
                    this.child.onNext(toEmit);
                    this.child.onCompleted();
                    unsubscribe();
                }
            } catch (Throwable t) {
                Exceptions.throwOrReport(t, this.child);
            }
        }

        void scheduleExact() {
            this.inner.schedulePeriodically(new Action0() {
                public void call() {
                    ExactSubscriber.this.emit();
                }
            }, OperatorBufferWithTime.this.timespan, OperatorBufferWithTime.this.timespan, OperatorBufferWithTime.this.unit);
        }

        void emit() {
            synchronized (this) {
                if (this.done) {
                    return;
                }
                List<T> toEmit = this.chunk;
                this.chunk = new ArrayList();
                try {
                    this.child.onNext(toEmit);
                } catch (Throwable t) {
                    Exceptions.throwOrReport(t, this);
                }
            }
        }
    }

    final class InexactSubscriber extends Subscriber<T> {
        final Subscriber<? super List<T>> child;
        final List<List<T>> chunks = new LinkedList();
        boolean done;
        final Worker inner;

        public InexactSubscriber(Subscriber<? super List<T>> child, Worker inner) {
            this.child = child;
            this.inner = inner;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onNext(T r8) {
            /*
            r7 = this;
            r3 = 0;
            monitor-enter(r7);
            r5 = r7.done;	 Catch:{ all -> 0x0050 }
            if (r5 == 0) goto L_0x0008;
        L_0x0006:
            monitor-exit(r7);	 Catch:{ all -> 0x0050 }
        L_0x0007:
            return;
        L_0x0008:
            r5 = r7.chunks;	 Catch:{ all -> 0x0050 }
            r2 = r5.iterator();	 Catch:{ all -> 0x0050 }
            r4 = r3;
        L_0x000f:
            r5 = r2.hasNext();	 Catch:{ all -> 0x0055 }
            if (r5 == 0) goto L_0x0037;
        L_0x0015:
            r0 = r2.next();	 Catch:{ all -> 0x0055 }
            r0 = (java.util.List) r0;	 Catch:{ all -> 0x0055 }
            r0.add(r8);	 Catch:{ all -> 0x0055 }
            r5 = r0.size();	 Catch:{ all -> 0x0055 }
            r6 = rx.internal.operators.OperatorBufferWithTime.this;	 Catch:{ all -> 0x0055 }
            r6 = r6.count;	 Catch:{ all -> 0x0055 }
            if (r5 != r6) goto L_0x005a;
        L_0x0028:
            r2.remove();	 Catch:{ all -> 0x0055 }
            if (r4 != 0) goto L_0x0058;
        L_0x002d:
            r3 = new java.util.LinkedList;	 Catch:{ all -> 0x0055 }
            r3.<init>();	 Catch:{ all -> 0x0055 }
        L_0x0032:
            r3.add(r0);	 Catch:{ all -> 0x0050 }
        L_0x0035:
            r4 = r3;
            goto L_0x000f;
        L_0x0037:
            monitor-exit(r7);	 Catch:{ all -> 0x0055 }
            if (r4 == 0) goto L_0x0053;
        L_0x003a:
            r1 = r4.iterator();
        L_0x003e:
            r5 = r1.hasNext();
            if (r5 == 0) goto L_0x0053;
        L_0x0044:
            r0 = r1.next();
            r0 = (java.util.List) r0;
            r5 = r7.child;
            r5.onNext(r0);
            goto L_0x003e;
        L_0x0050:
            r5 = move-exception;
        L_0x0051:
            monitor-exit(r7);	 Catch:{ all -> 0x0050 }
            throw r5;
        L_0x0053:
            r3 = r4;
            goto L_0x0007;
        L_0x0055:
            r5 = move-exception;
            r3 = r4;
            goto L_0x0051;
        L_0x0058:
            r3 = r4;
            goto L_0x0032;
        L_0x005a:
            r3 = r4;
            goto L_0x0035;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorBufferWithTime.InexactSubscriber.onNext(java.lang.Object):void");
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
            r2 = new java.util.LinkedList;	 Catch:{ all -> 0x0034 }
            r4 = r5.chunks;	 Catch:{ all -> 0x0034 }
            r2.<init>(r4);	 Catch:{ all -> 0x0034 }
            r4 = r5.chunks;	 Catch:{ all -> 0x0034 }
            r4.clear();	 Catch:{ all -> 0x0034 }
            monitor-exit(r5);	 Catch:{ all -> 0x0034 }
            r1 = r2.iterator();	 Catch:{ Throwable -> 0x002d }
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
            r3 = move-exception;
            r4 = r5.child;
            rx.exceptions.Exceptions.throwOrReport(r3, r4);
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
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorBufferWithTime.InexactSubscriber.onCompleted():void");
        }

        void scheduleChunk() {
            this.inner.schedulePeriodically(new Action0() {
                public void call() {
                    InexactSubscriber.this.startNewChunk();
                }
            }, OperatorBufferWithTime.this.timeshift, OperatorBufferWithTime.this.timeshift, OperatorBufferWithTime.this.unit);
        }

        void startNewChunk() {
            final List<T> chunk = new ArrayList();
            synchronized (this) {
                if (this.done) {
                    return;
                }
                this.chunks.add(chunk);
                this.inner.schedule(new Action0() {
                    public void call() {
                        InexactSubscriber.this.emitChunk(chunk);
                    }
                }, OperatorBufferWithTime.this.timespan, OperatorBufferWithTime.this.unit);
            }
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        void emitChunk(java.util.List<T> r6) {
            /*
            r5 = this;
            r1 = 0;
            monitor-enter(r5);
            r4 = r5.done;	 Catch:{ all -> 0x002e }
            if (r4 == 0) goto L_0x0008;
        L_0x0006:
            monitor-exit(r5);	 Catch:{ all -> 0x002e }
        L_0x0007:
            return;
        L_0x0008:
            r4 = r5.chunks;	 Catch:{ all -> 0x002e }
            r2 = r4.iterator();	 Catch:{ all -> 0x002e }
        L_0x000e:
            r4 = r2.hasNext();	 Catch:{ all -> 0x002e }
            if (r4 == 0) goto L_0x0020;
        L_0x0014:
            r0 = r2.next();	 Catch:{ all -> 0x002e }
            r0 = (java.util.List) r0;	 Catch:{ all -> 0x002e }
            if (r0 != r6) goto L_0x000e;
        L_0x001c:
            r2.remove();	 Catch:{ all -> 0x002e }
            r1 = 1;
        L_0x0020:
            monitor-exit(r5);	 Catch:{ all -> 0x002e }
            if (r1 == 0) goto L_0x0007;
        L_0x0023:
            r4 = r5.child;	 Catch:{ Throwable -> 0x0029 }
            r4.onNext(r6);	 Catch:{ Throwable -> 0x0029 }
            goto L_0x0007;
        L_0x0029:
            r3 = move-exception;
            rx.exceptions.Exceptions.throwOrReport(r3, r5);
            goto L_0x0007;
        L_0x002e:
            r4 = move-exception;
            monitor-exit(r5);	 Catch:{ all -> 0x002e }
            throw r4;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorBufferWithTime.InexactSubscriber.emitChunk(java.util.List):void");
        }
    }

    public OperatorBufferWithTime(long timespan, long timeshift, TimeUnit unit, int count, Scheduler scheduler) {
        this.timespan = timespan;
        this.timeshift = timeshift;
        this.unit = unit;
        this.count = count;
        this.scheduler = scheduler;
    }

    public Subscriber<? super T> call(Subscriber<? super List<T>> child) {
        Worker inner = this.scheduler.createWorker();
        SerializedSubscriber<List<T>> serialized = new SerializedSubscriber(child);
        if (this.timespan == this.timeshift) {
            ExactSubscriber bsub = new ExactSubscriber(serialized, inner);
            bsub.add(inner);
            child.add(bsub);
            bsub.scheduleExact();
            return bsub;
        }
        Subscriber bsub2 = new InexactSubscriber(serialized, inner);
        bsub2.add(inner);
        child.add(bsub2);
        bsub2.startNewChunk();
        bsub2.scheduleChunk();
        return bsub2;
    }
}
