package rx.internal.operators;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import rx.Observable;
import rx.Observable$OnSubscribe;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func0;
import rx.observables.ConnectableObservable;
import rx.subjects.Subject;

public final class OperatorMulticast<T, R> extends ConnectableObservable<R> {
    final AtomicReference<Subject<? super T, ? extends R>> connectedSubject;
    final Object guard;
    private Subscription guardedSubscription;
    final Observable<? extends T> source;
    final Func0<? extends Subject<? super T, ? extends R>> subjectFactory;
    private Subscriber<T> subscription;
    final List<Subscriber<? super R>> waitingForConnect;

    class AnonymousClass1 implements Observable$OnSubscribe<R> {
        final /* synthetic */ AtomicReference val$connectedSubject;
        final /* synthetic */ Object val$guard;
        final /* synthetic */ List val$waitingForConnect;

        AnonymousClass1(Object obj, AtomicReference atomicReference, List list) {
            this.val$guard = obj;
            this.val$connectedSubject = atomicReference;
            this.val$waitingForConnect = list;
        }

        public void call(Subscriber<? super R> subscriber) {
            synchronized (this.val$guard) {
                if (this.val$connectedSubject.get() == null) {
                    this.val$waitingForConnect.add(subscriber);
                } else {
                    ((Subject) this.val$connectedSubject.get()).unsafeSubscribe(subscriber);
                }
            }
        }
    }

    public OperatorMulticast(Observable<? extends T> source, Func0<? extends Subject<? super T, ? extends R>> subjectFactory) {
        this(new Object(), new AtomicReference(), new ArrayList(), source, subjectFactory);
    }

    private OperatorMulticast(Object guard, AtomicReference<Subject<? super T, ? extends R>> connectedSubject, List<Subscriber<? super R>> waitingForConnect, Observable<? extends T> source, Func0<? extends Subject<? super T, ? extends R>> subjectFactory) {
        super(new AnonymousClass1(guard, connectedSubject, waitingForConnect));
        this.guard = guard;
        this.connectedSubject = connectedSubject;
        this.waitingForConnect = waitingForConnect;
        this.source = source;
        this.subjectFactory = subjectFactory;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void connect(rx.functions.Action1<? super rx.Subscription> r8) {
        /*
        r7 = this;
        r6 = r7.guard;
        monitor-enter(r6);
        r5 = r7.subscription;	 Catch:{ all -> 0x0050 }
        if (r5 == 0) goto L_0x000e;
    L_0x0007:
        r5 = r7.guardedSubscription;	 Catch:{ all -> 0x0050 }
        r8.call(r5);	 Catch:{ all -> 0x0050 }
        monitor-exit(r6);	 Catch:{ all -> 0x0050 }
    L_0x000d:
        return;
    L_0x000e:
        r5 = r7.subjectFactory;	 Catch:{ all -> 0x0050 }
        r4 = r5.call();	 Catch:{ all -> 0x0050 }
        r4 = (rx.subjects.Subject) r4;	 Catch:{ all -> 0x0050 }
        r5 = rx.observers.Subscribers.from(r4);	 Catch:{ all -> 0x0050 }
        r7.subscription = r5;	 Catch:{ all -> 0x0050 }
        r0 = new java.util.concurrent.atomic.AtomicReference;	 Catch:{ all -> 0x0050 }
        r0.<init>();	 Catch:{ all -> 0x0050 }
        r5 = new rx.internal.operators.OperatorMulticast$2;	 Catch:{ all -> 0x0050 }
        r5.<init>(r0);	 Catch:{ all -> 0x0050 }
        r5 = rx.subscriptions.Subscriptions.create(r5);	 Catch:{ all -> 0x0050 }
        r0.set(r5);	 Catch:{ all -> 0x0050 }
        r5 = r0.get();	 Catch:{ all -> 0x0050 }
        r5 = (rx.Subscription) r5;	 Catch:{ all -> 0x0050 }
        r7.guardedSubscription = r5;	 Catch:{ all -> 0x0050 }
        r5 = r7.waitingForConnect;	 Catch:{ all -> 0x0050 }
        r1 = r5.iterator();	 Catch:{ all -> 0x0050 }
    L_0x003b:
        r5 = r1.hasNext();	 Catch:{ all -> 0x0050 }
        if (r5 == 0) goto L_0x0053;
    L_0x0041:
        r2 = r1.next();	 Catch:{ all -> 0x0050 }
        r2 = (rx.Subscriber) r2;	 Catch:{ all -> 0x0050 }
        r5 = new rx.internal.operators.OperatorMulticast$3;	 Catch:{ all -> 0x0050 }
        r5.<init>(r2, r2);	 Catch:{ all -> 0x0050 }
        r4.unsafeSubscribe(r5);	 Catch:{ all -> 0x0050 }
        goto L_0x003b;
    L_0x0050:
        r5 = move-exception;
        monitor-exit(r6);	 Catch:{ all -> 0x0050 }
        throw r5;
    L_0x0053:
        r5 = r7.waitingForConnect;	 Catch:{ all -> 0x0050 }
        r5.clear();	 Catch:{ all -> 0x0050 }
        r5 = r7.connectedSubject;	 Catch:{ all -> 0x0050 }
        r5.set(r4);	 Catch:{ all -> 0x0050 }
        monitor-exit(r6);	 Catch:{ all -> 0x0050 }
        r5 = r7.guardedSubscription;
        r8.call(r5);
        r6 = r7.guard;
        monitor-enter(r6);
        r3 = r7.subscription;	 Catch:{ all -> 0x0071 }
        monitor-exit(r6);	 Catch:{ all -> 0x0071 }
        if (r3 == 0) goto L_0x000d;
    L_0x006b:
        r5 = r7.source;
        r5.subscribe(r3);
        goto L_0x000d;
    L_0x0071:
        r5 = move-exception;
        monitor-exit(r6);	 Catch:{ all -> 0x0071 }
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorMulticast.connect(rx.functions.Action1):void");
    }
}
