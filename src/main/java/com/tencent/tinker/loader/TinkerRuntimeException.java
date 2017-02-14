package com.tencent.tinker.loader;

public class TinkerRuntimeException extends RuntimeException {
    private static final String TINKER_RUNTIME_EXCEPTION_PREFIX = "Tinker Exception:";
    private static final long   serialVersionUID                = 1;

    public TinkerRuntimeException(String detailMessage) {
        super(TINKER_RUNTIME_EXCEPTION_PREFIX + detailMessage);
    }

    public TinkerRuntimeException(String detailMessage, Throwable throwable) {
        super(TINKER_RUNTIME_EXCEPTION_PREFIX + detailMessage, throwable);
    }
}
