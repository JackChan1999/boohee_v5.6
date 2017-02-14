package rx.internal.operators;

import java.util.concurrent.atomic.AtomicLong;
import rx.Notification;
import rx.Observable$Operator;
import rx.Producer;
import rx.Subscriber;
import rx.plugins.RxJavaPlugins;

public final class OperatorMaterialize<T> implements Observable$Operator<Notification<T>, T> {

    private static final class Holder {
        static final OperatorMaterialize<Object> INSTANCE = new OperatorMaterialize();

        private Holder() {
        }
    }

    private static class ParentSubscriber<T> extends Subscriber<T> {
        private boolean busy = false;
        private final Subscriber<? super Notification<T>> child;
        private boolean missed = false;
        private final AtomicLong requested = new AtomicLong();
        private volatile Notification<T> terminalNotification;

        ParentSubscriber(Subscriber<? super Notification<T>> child) {
            this.child = child;
        }

        public void onStart() {
            request(0);
        }

        void requestMore(long n) {
            BackpressureUtils.getAndAddRequest(this.requested, n);
            request(n);
            drain();
        }

        public void onCompleted() {
            this.terminalNotification = Notification.createOnCompleted();
            drain();
        }

        public void onError(Throwable e) {
            this.terminalNotification = Notification.createOnError(e);
            RxJavaPlugins.getInstance().getErrorHandler().handleError(e);
            drain();
        }

        public void onNext(T t) {
            this.child.onNext(Notification.createOnNext(t));
            decrementRequested();
        }

        private void decrementRequested() {
            AtomicLong localRequested = this.requested;
            long r;
            do {
                r = localRequested.get();
                if (r == Long.MAX_VALUE) {
                    return;
                }
            } while (!localRequested.compareAndSet(r, r - 1));
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private void drain() {
            /*
            r6 = this;
            monitor-enter(r6);
            r2 = r6.busy;	 Catch:{ all -> 0x0039 }
            if (r2 == 0) goto L_0x000a;
        L_0x0005:
            r2 = 1;
            r6.missed = r2;	 Catch:{ all -> 0x0039 }
            monitor-exit(r6);	 Catch:{ all -> 0x0039 }
        L_0x0009:
            return;
        L_0x000a:
            monitor-exit(r6);	 Catch:{ all -> 0x0039 }
            r0 = r6.requested;
        L_0x000d:
            r2 = r6.child;
            r2 = r2.isUnsubscribed();
            if (r2 != 0) goto L_0x0009;
        L_0x0015:
            r1 = r6.terminalNotification;
            if (r1 == 0) goto L_0x003c;
        L_0x0019:
            r2 = r0.get();
            r4 = 0;
            r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
            if (r2 <= 0) goto L_0x003c;
        L_0x0023:
            r2 = 0;
            r6.terminalNotification = r2;
            r2 = r6.child;
            r2.onNext(r1);
            r2 = r6.child;
            r2 = r2.isUnsubscribed();
            if (r2 != 0) goto L_0x0009;
        L_0x0033:
            r2 = r6.child;
            r2.onCompleted();
            goto L_0x0009;
        L_0x0039:
            r2 = move-exception;
            monitor-exit(r6);	 Catch:{ all -> 0x0039 }
            throw r2;
        L_0x003c:
            monitor-enter(r6);
            r2 = r6.missed;	 Catch:{ all -> 0x0046 }
            if (r2 != 0) goto L_0x0049;
        L_0x0041:
            r2 = 0;
            r6.busy = r2;	 Catch:{ all -> 0x0046 }
            monitor-exit(r6);	 Catch:{ all -> 0x0046 }
            goto L_0x0009;
        L_0x0046:
            r2 = move-exception;
            monitor-exit(r6);	 Catch:{ all -> 0x0046 }
            throw r2;
        L_0x0049:
            monitor-exit(r6);	 Catch:{ all -> 0x0046 }
            goto L_0x000d;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorMaterialize.ParentSubscriber.drain():void");
        }
    }

    public static <T> OperatorMaterialize<T> instance() {
        return Holder.INSTANCE;
    }

    private OperatorMaterialize() {
    }

    public Subscriber<? super T> call(Subscriber<? super Notification<T>> child) {
        final ParentSubscriber<T> parent = new ParentSubscriber(child);
        child.add(parent);
        child.setProducer(new Producer() {
            public void request(long n) {
                if (n > 0) {
                    parent.requestMore(n);
                }
            }
        });
        return parent;
    }
}
