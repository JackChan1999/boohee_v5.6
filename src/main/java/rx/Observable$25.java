package rx;

import rx.functions.Func1;

class Observable$25 implements Func1<Observable<? extends Notification<?>>, Observable<?>> {
    final /* synthetic */ Observable this$0;
    final /* synthetic */ Func1 val$notificationHandler;

    Observable$25(Observable observable, Func1 func1) {
        this.this$0 = observable;
        this.val$notificationHandler = func1;
    }

    public Observable<?> call(Observable<? extends Notification<?>> notifications) {
        return (Observable) this.val$notificationHandler.call(notifications.map(new Func1<Notification<?>, Throwable>() {
            public Throwable call(Notification<?> notification) {
                return notification.getThrowable();
            }
        }));
    }
}
