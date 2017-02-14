package com.tencent.tinker.android.dex;

public class DexException extends RuntimeException {
    static final long serialVersionUID = 1;

    public DexException(String message) {
        super(message);
    }

    public DexException(Throwable cause) {
        super(cause);
    }
}
