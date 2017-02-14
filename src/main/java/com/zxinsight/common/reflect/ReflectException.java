package com.zxinsight.common.reflect;

public class ReflectException extends RuntimeException {
    private static final long serialVersionUID = -6654702552823551870L;

    public ReflectException(String str) {
        super(str);
    }

    public ReflectException(String str, Throwable th) {
        super(str, th);
    }

    public ReflectException(Throwable th) {
        super(th);
    }
}
