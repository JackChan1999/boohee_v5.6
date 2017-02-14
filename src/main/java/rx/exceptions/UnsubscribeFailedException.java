package rx.exceptions;

public final class UnsubscribeFailedException extends RuntimeException {
    private static final long serialVersionUID = 4594672310593167598L;

    public UnsubscribeFailedException(Throwable throwable) {
        if (throwable == null) {
            throwable = new NullPointerException();
        }
        super(throwable);
    }

    public UnsubscribeFailedException(String message, Throwable throwable) {
        if (throwable == null) {
            throwable = new NullPointerException();
        }
        super(message, throwable);
    }
}
