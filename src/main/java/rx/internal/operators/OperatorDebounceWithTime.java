package rx.internal.operators;

import java.util.concurrent.TimeUnit;
import rx.Observable$Operator;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.Subscriber;
import rx.functions.Action0;
import rx.observers.SerializedSubscriber;
import rx.subscriptions.SerialSubscription;

public final class OperatorDebounceWithTime<T> implements Observable$Operator<T, T> {
    final Scheduler scheduler;
    final long timeout;
    final TimeUnit unit;

    static final class DebounceState<T> {
        boolean emitting;
        boolean hasValue;
        int index;
        boolean terminate;
        T value;

        DebounceState() {
        }

        public synchronized int next(T value) {
            int i;
            this.value = value;
            this.hasValue = true;
            i = this.index + 1;
            this.index = i;
            return i;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void emit(int r4, rx.Subscriber<T> r5, rx.Subscriber<?> r6) {
            /*
            r3 = this;
            monitor-enter(r3);
            r2 = r3.emitting;	 Catch:{ all -> 0x002b }
            if (r2 != 0) goto L_0x000d;
        L_0x0005:
            r2 = r3.hasValue;	 Catch:{ all -> 0x002b }
            if (r2 == 0) goto L_0x000d;
        L_0x0009:
            r2 = r3.index;	 Catch:{ all -> 0x002b }
            if (r4 == r2) goto L_0x000f;
        L_0x000d:
            monitor-exit(r3);	 Catch:{ all -> 0x002b }
        L_0x000e:
            return;
        L_0x000f:
            r1 = r3.value;	 Catch:{ all -> 0x002b }
            r2 = 0;
            r3.value = r2;	 Catch:{ all -> 0x002b }
            r2 = 0;
            r3.hasValue = r2;	 Catch:{ all -> 0x002b }
            r2 = 1;
            r3.emitting = r2;	 Catch:{ all -> 0x002b }
            monitor-exit(r3);	 Catch:{ all -> 0x002b }
            r5.onNext(r1);	 Catch:{ Throwable -> 0x002e }
            monitor-enter(r3);
            r2 = r3.terminate;	 Catch:{ all -> 0x0028 }
            if (r2 != 0) goto L_0x0033;
        L_0x0023:
            r2 = 0;
            r3.emitting = r2;	 Catch:{ all -> 0x0028 }
            monitor-exit(r3);	 Catch:{ all -> 0x0028 }
            goto L_0x000e;
        L_0x0028:
            r2 = move-exception;
            monitor-exit(r3);	 Catch:{ all -> 0x0028 }
            throw r2;
        L_0x002b:
            r2 = move-exception;
            monitor-exit(r3);	 Catch:{ all -> 0x002b }
            throw r2;
        L_0x002e:
            r0 = move-exception;
            rx.exceptions.Exceptions.throwOrReport(r0, r6, r1);
            goto L_0x000e;
        L_0x0033:
            monitor-exit(r3);	 Catch:{ all -> 0x0028 }
            r5.onCompleted();
            goto L_0x000e;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorDebounceWithTime.DebounceState.emit(int, rx.Subscriber, rx.Subscriber):void");
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void emitAndComplete(rx.Subscriber<T> r5, rx.Subscriber<?> r6) {
            /*
            r4 = this;
            monitor-enter(r4);
            r3 = r4.emitting;	 Catch:{ all -> 0x0021 }
            if (r3 == 0) goto L_0x000a;
        L_0x0005:
            r3 = 1;
            r4.terminate = r3;	 Catch:{ all -> 0x0021 }
            monitor-exit(r4);	 Catch:{ all -> 0x0021 }
        L_0x0009:
            return;
        L_0x000a:
            r2 = r4.value;	 Catch:{ all -> 0x0021 }
            r1 = r4.hasValue;	 Catch:{ all -> 0x0021 }
            r3 = 0;
            r4.value = r3;	 Catch:{ all -> 0x0021 }
            r3 = 0;
            r4.hasValue = r3;	 Catch:{ all -> 0x0021 }
            r3 = 1;
            r4.emitting = r3;	 Catch:{ all -> 0x0021 }
            monitor-exit(r4);	 Catch:{ all -> 0x0021 }
            if (r1 == 0) goto L_0x001d;
        L_0x001a:
            r5.onNext(r2);	 Catch:{ Throwable -> 0x0024 }
        L_0x001d:
            r5.onCompleted();
            goto L_0x0009;
        L_0x0021:
            r3 = move-exception;
            monitor-exit(r4);	 Catch:{ all -> 0x0021 }
            throw r3;
        L_0x0024:
            r0 = move-exception;
            rx.exceptions.Exceptions.throwOrReport(r0, r6, r2);
            goto L_0x0009;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorDebounceWithTime.DebounceState.emitAndComplete(rx.Subscriber, rx.Subscriber):void");
        }

        public synchronized void clear() {
            this.index++;
            this.value = null;
            this.hasValue = false;
        }
    }

    public OperatorDebounceWithTime(long timeout, TimeUnit unit, Scheduler scheduler) {
        this.timeout = timeout;
        this.unit = unit;
        this.scheduler = scheduler;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        final Worker worker = this.scheduler.createWorker();
        final SerializedSubscriber<T> s = new SerializedSubscriber(child);
        final SerialSubscription ssub = new SerialSubscription();
        s.add(worker);
        s.add(ssub);
        return new Subscriber<T>(child) {
            final Subscriber<?> self = this;
            final DebounceState<T> state = new DebounceState();

            public void onStart() {
                request(Long.MAX_VALUE);
            }

            public void onNext(T t) {
                final int index = this.state.next(t);
                ssub.set(worker.schedule(new Action0() {
                    public void call() {
                        AnonymousClass1.this.state.emit(index, s, AnonymousClass1.this.self);
                    }
                }, OperatorDebounceWithTime.this.timeout, OperatorDebounceWithTime.this.unit));
            }

            public void onError(Throwable e) {
                s.onError(e);
                unsubscribe();
                this.state.clear();
            }

            public void onCompleted() {
                this.state.emitAndComplete(s, this);
            }
        };
    }
}
