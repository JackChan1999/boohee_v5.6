package com.squareup.okhttp.internal.http;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

class HttpDate$1 extends ThreadLocal<DateFormat> {
    HttpDate$1() {
    }

    protected DateFormat initialValue() {
        DateFormat rfc1123 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        rfc1123.setLenient(false);
        rfc1123.setTimeZone(HttpDate.access$000());
        return rfc1123;
    }
}
