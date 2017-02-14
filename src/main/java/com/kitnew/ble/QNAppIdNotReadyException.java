package com.kitnew.ble;

public class QNAppIdNotReadyException extends RuntimeException {
    public QNAppIdNotReadyException() {
        super("Your app id is not ready");
    }
}
