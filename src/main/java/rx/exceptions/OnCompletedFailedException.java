package rx.exceptions;

public final class OnCompletedFailedException extends RuntimeException {
    private static final long serialVersionUID = 8622579378868820554L;

    public OnCompletedFailedException(Throwable throwable) {
        if (throwable == null) {
            throwable = new NullPointerException();
        }
        super(throwable);
    }

    public OnCompletedFailedException(String message, Throwable throwable) {
        if (throwable == null) {
            throwable = new NullPointerException();
        }
        super(message, throwable);
    }
}
