package com.qiniu.android.dns.http;

import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.qiniu.android.dns.Domain;
import com.qiniu.android.dns.IResolver;
import com.qiniu.android.dns.NetworkInfo;
import com.qiniu.android.dns.Record;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public final class DnspodFree implements IResolver {
    private final String ip;

    public DnspodFree(String ip) {
        this.ip = ip;
    }

    public DnspodFree() {
        this("119.29.29.29");
    }

    public Record[] resolve(Domain domain, NetworkInfo info) throws IOException {
        HttpURLConnection httpConn = (HttpURLConnection) new URL("http://119.29.29" +
                ".29/d?ttl=1&dn=" + domain.domain).openConnection();
        httpConn.setConnectTimeout(BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT);
        httpConn.setReadTimeout(10000);
        if (httpConn.getResponseCode() != 200) {
            return null;
        }
        int length = httpConn.getContentLength();
        if (length <= 0 || length > 1024) {
            return null;
        }
        InputStream is = httpConn.getInputStream();
        byte[] data = new byte[length];
        int read = is.read(data);
        is.close();
        if (read <= 0) {
            return null;
        }
        String[] r1 = new String(data, 0, read).split(",");
        if (r1.length != 2) {
            return null;
        }
        try {
            int ttl = Integer.parseInt(r1[1]);
            String[] ips = r1[0].split(com.boohee.one.http.DnspodFree.IP_SPLIT);
            if (ips.length == 0) {
                return null;
            }
            Record[] records = new Record[ips.length];
            long time = System.currentTimeMillis() / 1000;
            for (int i = 0; i < ips.length; i++) {
                records[i] = new Record(ips[i], 1, ttl, time);
            }
            return records;
        } catch (Exception e) {
            return null;
        }
    }
}
