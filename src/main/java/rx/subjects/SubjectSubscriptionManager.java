package rx.subjects;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import rx.Observable$OnSubscribe;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Actions;
import rx.internal.operators.NotificationLite;
import rx.subscriptions.Subscriptions;

final class SubjectSubscriptionManager<T> extends AtomicReference<State<T>> implements Observable$OnSubscribe<T> {
    boolean active = true;
    volatile Object latest;
    public final NotificationLite<T> nl = NotificationLite.instance();
    Action1<SubjectObserver<T>> onAdded = Actions.empty();
    Action1<SubjectObserver<T>> onStart = Actions.empty();
    Action1<SubjectObserver<T>> onTerminated = Actions.empty();

    protected static final class State<T> {
        static final State EMPTY = new State(false, NO_OBSERVERS);
        static final SubjectObserver[] NO_OBSERVERS = new SubjectObserver[0];
        static final State TERMINATED = new State(true, NO_OBSERVERS);
        final SubjectObserver[] observers;
        final boolean terminated;

        public State(boolean terminated, SubjectObserver[] observers) {
            this.terminated = terminated;
            this.observers = observers;
        }

        public State add(SubjectObserver o) {
            int n = this.observers.length;
            SubjectObserver[] b = new SubjectObserver[(n + 1)];
            System.arraycopy(this.observers, 0, b, 0, n);
            b[n] = o;
            return new State(this.terminated, b);
        }

        public State remove(SubjectObserver o) {
            SubjectObserver[] a = this.observers;
            int n = a.length;
            if (n == 1 && a[0] == o) {
                return EMPTY;
            }
            if (n == 0) {
                return this;
            }
            SubjectObserver[] b = new SubjectObserver[(n - 1)];
            int i = 0;
            int j = 0;
            while (i < n) {
                int j2;
                SubjectObserver ai = a[i];
                if (ai == o) {
                    j2 = j;
                } else if (j == n - 1) {
                    return this;
                } else {
                    j2 = j + 1;
                    b[j] = ai;
                }
                i++;
                j = j2;
            }
            if (j == 0) {
                return EMPTY;
            }
            if (j < n - 1) {
                SubjectObserver[] c = new SubjectObserver[j];
                System.arraycopy(b, 0, c, 0, j);
                b = c;
            }
            return new State(this.terminated, b);
        }
    }

    protected static final class SubjectObserver<T> implements Observer<T> {
        final Observer<? super T> actual;
        protected volatile boolean caughtUp;
        boolean emitting;
        boolean fastPath;
        boolean first = true;
        private volatile Object index;
        List<Object> queue;

        public SubjectObserver(Observer<? super T> actual) {
            this.actual = actual;
        }

        public void onNext(T t) {
            this.actual.onNext(t);
        }

        public void onError(Throwable e) {
            this.actual.onError(e);
        }

        public void onCompleted() {
            this.actual.onCompleted();
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        protected void emitNext(java.lang.Object r2, rx.internal.operators.NotificationLite<T> r3) {
            /*
            r1 = this;
            r0 = r1.fastPath;
            if (r0 != 0) goto L_0x0022;
        L_0x0004:
            monitor-enter(r1);
            r0 = 0;
            r1.first = r0;	 Catch:{ all -> 0x0028 }
            r0 = r1.emitting;	 Catch:{ all -> 0x0028 }
            if (r0 == 0) goto L_0x001e;
        L_0x000c:
            r0 = r1.queue;	 Catch:{ all -> 0x0028 }
            if (r0 != 0) goto L_0x0017;
        L_0x0010:
            r0 = new java.util.ArrayList;	 Catch:{ all -> 0x0028 }
            r0.<init>();	 Catch:{ all -> 0x0028 }
            r1.queue = r0;	 Catch:{ all -> 0x0028 }
        L_0x0017:
            r0 = r1.queue;	 Catch:{ all -> 0x0028 }
            r0.add(r2);	 Catch:{ all -> 0x0028 }
            monitor-exit(r1);	 Catch:{ all -> 0x0028 }
        L_0x001d:
            return;
        L_0x001e:
            monitor-exit(r1);	 Catch:{ all -> 0x0028 }
            r0 = 1;
            r1.fastPath = r0;
        L_0x0022:
            r0 = r1.actual;
            r3.accept(r0, r2);
            goto L_0x001d;
        L_0x0028:
            r0 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x0028 }
            throw r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.subjects.SubjectSubscriptionManager.SubjectObserver.emitNext(java.lang.Object, rx.internal.operators.NotificationLite):void");
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        protected void emitFirst(java.lang.Object r3, rx.internal.operators.NotificationLite<T> r4) {
            /*
            r2 = this;
            r0 = 0;
            monitor-enter(r2);
            r1 = r2.first;	 Catch:{ all -> 0x001c }
            if (r1 == 0) goto L_0x000a;
        L_0x0006:
            r1 = r2.emitting;	 Catch:{ all -> 0x001c }
            if (r1 == 0) goto L_0x000c;
        L_0x000a:
            monitor-exit(r2);	 Catch:{ all -> 0x001c }
        L_0x000b:
            return;
        L_0x000c:
            r1 = 0;
            r2.first = r1;	 Catch:{ all -> 0x001c }
            if (r3 == 0) goto L_0x0012;
        L_0x0011:
            r0 = 1;
        L_0x0012:
            r2.emitting = r0;	 Catch:{ all -> 0x001c }
            monitor-exit(r2);	 Catch:{ all -> 0x001c }
            if (r3 == 0) goto L_0x000b;
        L_0x0017:
            r0 = 0;
            r2.emitLoop(r0, r3, r4);
            goto L_0x000b;
        L_0x001c:
            r0 = move-exception;
            monitor-exit(r2);	 Catch:{ all -> 0x001c }
            throw r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.subjects.SubjectSubscriptionManager.SubjectObserver.emitFirst(java.lang.Object, rx.internal.operators.NotificationLite):void");
        }

        protected void emitLoop(List<Object> localQueue, Object current, NotificationLite<T> nl) {
            boolean once = true;
            while (true) {
                if (localQueue != null) {
                    for (Object n : localQueue) {
                        accept(n, nl);
                    }
                }
                if (once) {
                    once = false;
                    try {
                        accept(current, nl);
                    } catch (Throwable th) {
                        if (!false) {
                            synchronized (this) {
                                this.emitting = false;
                            }
                        }
                    }
                }
                synchronized (this) {
                    localQueue = this.queue;
                    this.queue = null;
                    if (localQueue == null) {
                        break;
                    }
                }
            }
            this.emitting = false;
            if (!true) {
                synchronized (this) {
                    this.emitting = false;
                }
            }
        }

        protected void accept(Object n, NotificationLite<T> nl) {
            if (n != null) {
                nl.accept(this.actual, n);
            }
        }

        protected Observer<? super T> getActual() {
            return this.actual;
        }

        public <I> I index() {
            return this.index;
        }

        public void index(Object newIndex) {
            this.index = newIndex;
        }
    }

    public SubjectSubscriptionManager() {
        super(State.EMPTY);
    }

    public void call(Subscriber<? super T> child) {
        SubjectObserver<T> bo = new SubjectObserver(child);
        addUnsubscriber(child, bo);
        this.onStart.call(bo);
        if (!child.isUnsubscribed() && add(bo) && child.isUnsubscribed()) {
            remove(bo);
        }
    }

    void addUnsubscriber(Subscriber<? super T> child, final SubjectObserver<T> bo) {
        child.add(Subscriptions.create(new Action0() {
            public void call() {
                SubjectSubscriptionManager.this.remove(bo);
            }
        }));
    }

    void setLatest(Object value) {
        this.latest = value;
    }

    Object getLatest() {
        return this.latest;
    }

    SubjectObserver<T>[] observers() {
        return ((State) get()).observers;
    }

    boolean add(SubjectObserver<T> o) {
        State oldState;
        do {
            oldState = (State) get();
            if (oldState.terminated) {
                this.onTerminated.call(o);
                return false;
            }
        } while (!compareAndSet(oldState, oldState.add(o)));
        this.onAdded.call(o);
        return true;
    }

    void remove(SubjectObserver<T> o) {
        State oldState;
        State newState;
        do {
            oldState = (State) get();
            if (!oldState.terminated) {
                newState = oldState.remove(o);
                if (newState == oldState) {
                    return;
                }
            } else {
                return;
            }
        } while (!compareAndSet(oldState, newState));
    }

    SubjectObserver<T>[] next(Object n) {
        setLatest(n);
        return ((State) get()).observers;
    }

    SubjectObserver<T>[] terminate(Object n) {
        setLatest(n);
        this.active = false;
        if (((State) get()).terminated) {
            return State.NO_OBSERVERS;
        }
        return ((State) getAndSet(State.TERMINATED)).observers;
    }
}
