package com.zxinsight.common.http;

import java.net.HttpURLConnection;
import java.net.URL;

final class aa implements c {
    aa() {
    }

    public HttpURLConnection a(URL url) {
        return (HttpURLConnection) url.openConnection();
    }
}
