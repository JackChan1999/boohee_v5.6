package rx.internal.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import rx.Subscription;
import rx.exceptions.Exceptions;

public final class SubscriptionList implements Subscription {
    private LinkedList<Subscription> subscriptions;
    private volatile boolean unsubscribed;

    public SubscriptionList(Subscription... subscriptions) {
        this.subscriptions = new LinkedList(Arrays.asList(subscriptions));
    }

    public SubscriptionList(Subscription s) {
        this.subscriptions = new LinkedList();
        this.subscriptions.add(s);
    }

    public boolean isUnsubscribed() {
        return this.unsubscribed;
    }

    public void add(Subscription s) {
        if (!s.isUnsubscribed()) {
            if (!this.unsubscribed) {
                synchronized (this) {
                    if (!this.unsubscribed) {
                        LinkedList<Subscription> subs = this.subscriptions;
                        if (subs == null) {
                            subs = new LinkedList();
                            this.subscriptions = subs;
                        }
                        subs.add(s);
                        return;
                    }
                }
            }
            s.unsubscribe();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void remove(rx.Subscription r4) {
        /*
        r3 = this;
        r2 = r3.unsubscribed;
        if (r2 != 0) goto L_0x000f;
    L_0x0004:
        r1 = 0;
        monitor-enter(r3);
        r0 = r3.subscriptions;	 Catch:{ all -> 0x001b }
        r2 = r3.unsubscribed;	 Catch:{ all -> 0x001b }
        if (r2 != 0) goto L_0x000e;
    L_0x000c:
        if (r0 != 0) goto L_0x0010;
    L_0x000e:
        monitor-exit(r3);	 Catch:{ all -> 0x001b }
    L_0x000f:
        return;
    L_0x0010:
        r1 = r0.remove(r4);	 Catch:{ all -> 0x001b }
        monitor-exit(r3);	 Catch:{ all -> 0x001b }
        if (r1 == 0) goto L_0x000f;
    L_0x0017:
        r4.unsubscribe();
        goto L_0x000f;
    L_0x001b:
        r2 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x001b }
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: rx.internal.util.SubscriptionList.remove(rx.Subscription):void");
    }

    public void unsubscribe() {
        if (!this.unsubscribed) {
            synchronized (this) {
                if (this.unsubscribed) {
                    return;
                }
                this.unsubscribed = true;
                List<Subscription> list = this.subscriptions;
                this.subscriptions = null;
                unsubscribeFromAll(list);
            }
        }
    }

    private static void unsubscribeFromAll(Collection<Subscription> subscriptions) {
        if (subscriptions != null) {
            List<Throwable> es = null;
            for (Subscription s : subscriptions) {
                try {
                    s.unsubscribe();
                } catch (Throwable e) {
                    if (es == null) {
                        es = new ArrayList();
                    }
                    es.add(e);
                }
            }
            Exceptions.throwIfAny(es);
        }
    }

    public void clear() {
        if (!this.unsubscribed) {
            List<Subscription> list;
            synchronized (this) {
                list = this.subscriptions;
                this.subscriptions = null;
            }
            unsubscribeFromAll(list);
        }
    }

    public boolean hasSubscriptions() {
        boolean z = false;
        if (!this.unsubscribed) {
            synchronized (this) {
                if (!(this.unsubscribed || this.subscriptions == null || this.subscriptions.isEmpty())) {
                    z = true;
                }
            }
        }
        return z;
    }
}
