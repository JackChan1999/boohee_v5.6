package rx;

import rx.functions.Action1;

class Observable$9 implements Observer<T> {
    final /* synthetic */ Observable this$0;
    final /* synthetic */ Action1 val$onNotification;

    Observable$9(Observable observable, Action1 action1) {
        this.this$0 = observable;
        this.val$onNotification = action1;
    }

    public final void onCompleted() {
        this.val$onNotification.call(Notification.createOnCompleted());
    }

    public final void onError(Throwable e) {
        this.val$onNotification.call(Notification.createOnError(e));
    }

    public final void onNext(T v) {
        this.val$onNotification.call(Notification.createOnNext(v));
    }
}
