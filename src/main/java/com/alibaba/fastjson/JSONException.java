package com.alibaba.fastjson;

public class JSONException extends RuntimeException {
    private static final long serialVersionUID = 1;

    public JSONException(String message) {
        super(message);
    }

    public JSONException(String message, Throwable cause) {
        super(message, cause);
    }
}
