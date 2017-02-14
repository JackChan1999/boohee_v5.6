package com.squareup.okhttp.internal.http;

import com.loopj.android.http.HttpDelete;
import com.tencent.connect.common.Constants;

public final class HttpMethod {
    public static boolean invalidatesCache(String method) {
        return method.equals(Constants.HTTP_POST) || method.equals("PATCH") || method.equals
                ("PUT") || method.equals(HttpDelete.METHOD_NAME) || method.equals("MOVE");
    }

    public static boolean requiresRequestBody(String method) {
        return method.equals(Constants.HTTP_POST) || method.equals("PUT") || method.equals
                ("PATCH") || method.equals("PROPPATCH") || method.equals("REPORT");
    }

    public static boolean permitsRequestBody(String method) {
        return requiresRequestBody(method) || method.equals("OPTIONS") || method.equals
                (HttpDelete.METHOD_NAME) || method.equals("PROPFIND") || method.equals("MKCOL")
                || method.equals("LOCK");
    }

    public static boolean redirectsToGet(String method) {
        return !method.equals("PROPFIND");
    }

    private HttpMethod() {
    }
}
