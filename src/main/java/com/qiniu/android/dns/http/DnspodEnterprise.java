package com.qiniu.android.dns.http;

import com.baidu.location.LocationClientOption;
import com.boohee.one.http.DnspodFree;
import com.boohee.utility.TimeLinePatterns;
import com.qiniu.android.common.Constants;
import com.qiniu.android.dns.Domain;
import com.qiniu.android.dns.IResolver;
import com.qiniu.android.dns.NetworkInfo;
import com.qiniu.android.dns.Record;
import com.qiniu.android.dns.util.Hex;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public final class DnspodEnterprise implements IResolver {
    private final String        id;
    private final String        ip;
    private final SecretKeySpec key;

    public DnspodEnterprise(String id, String key, String ip) {
        this.id = id;
        this.ip = ip;
        byte[] k = new byte[0];
        try {
            this.key = new SecretKeySpec(key.getBytes(Constants.UTF_8), "DES");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    public DnspodEnterprise(String id, String key) {
        this(id, key, "119.29.29.29");
    }

    public Record[] resolve(Domain domain, NetworkInfo info) throws IOException {
        HttpURLConnection httpConn = (HttpURLConnection) new URL(TimeLinePatterns.WEB_SCHEME +
                this.ip + "/d?ttl=1&dn=" + encrypt(domain.domain) + "&id=" + this.id)
                .openConnection();
        httpConn.setConnectTimeout(LocationClientOption.MIN_SCAN_SPAN_NETWORK);
        httpConn.setReadTimeout(6000);
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
        String[] r1 = decrypt(new String(data, 0, read)).split(",");
        if (r1.length != 2) {
            return null;
        }
        try {
            int ttl = Integer.parseInt(r1[1]);
            String[] ips = r1[0].split(DnspodFree.IP_SPLIT);
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

    private String encrypt(String domain) {
        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(1, this.key);
            return Hex.encodeHexString(cipher.doFinal(domain.getBytes(Constants.UTF_8))) + "&id="
                    + this.id;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private String decrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(2, this.key);
            return new String(cipher.doFinal(Hex.decodeHex(data.toCharArray())));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
