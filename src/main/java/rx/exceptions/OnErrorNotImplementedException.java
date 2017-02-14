package rx.exceptions;

public class OnErrorNotImplementedException extends RuntimeException {
    private static final long serialVersionUID = -6298857009889503852L;

    public OnErrorNotImplementedException(String message, Throwable e) {
        if (e == null) {
            e = new NullPointerException();
        }
        super(message, e);
    }

    public OnErrorNotImplementedException(Throwable e) {
        String message = e != null ? e.getMessage() : null;
        if (e == null) {
            e = new NullPointerException();
        }
        super(message, e);
    }
}
