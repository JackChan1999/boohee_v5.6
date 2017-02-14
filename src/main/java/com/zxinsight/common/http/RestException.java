package com.zxinsight.common.http;

import java.io.IOException;

public class RestException extends RuntimeException {
    public static final  String RETRY_CONNECTION = "retry";
    private static final long   serialVersionUID = 8520390726356829268L;
    private String errorCode;

    protected RestException(IOException iOException) {
        super(iOException);
    }

    protected RestException(IOException iOException, String str) {
        super(iOException);
        this.errorCode = str;
    }

    protected RestException(String str) {
        this.errorCode = str;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public IOException getCause() {
        return (IOException) super.getCause();
    }
}
