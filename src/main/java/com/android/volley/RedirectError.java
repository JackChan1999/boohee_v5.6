package com.android.volley;

public class RedirectError extends VolleyError {
    public RedirectError(Throwable cause) {
        super(cause);
    }

    public RedirectError(NetworkResponse response) {
        super(response);
    }
}
