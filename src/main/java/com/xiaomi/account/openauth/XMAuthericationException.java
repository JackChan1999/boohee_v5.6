package com.xiaomi.account.openauth;

public class XMAuthericationException extends Exception {
    private static final long serialVersionUID = 1;

    public XMAuthericationException(String msg) {
        super(msg);
    }

    public XMAuthericationException(Throwable throwable) {
        super(throwable);
    }
}
