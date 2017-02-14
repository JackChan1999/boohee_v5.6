package com.xiaomi.network;

import android.content.Context;

import com.boohee.utils.Coder;
import com.xiaomi.channel.commonutils.network.d;
import com.xiaomi.common.logger.thrift.a;
import com.xiaomi.common.logger.thrift.mfs.b;
import com.xiaomi.common.logger.thrift.mfs.c;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TreeMap;

import org.apache.thrift.g;

public class UploadHostStatHelper {
    private static UploadHostStatHelper e;
    private       List<HttpRecordCallback> a = new ArrayList();
    private final Random                   b = new Random();
    private       Timer                    c = new Timer("Upload Http Record Timer");
    private       boolean                  d = false;
    private       Context                  f = null;

    public interface HttpRecordCallback {
        List<b> a();

        double b();
    }

    private UploadHostStatHelper(Context context) {
        this.f = context.getApplicationContext();
    }

    public static synchronized UploadHostStatHelper a() {
        UploadHostStatHelper uploadHostStatHelper;
        synchronized (UploadHostStatHelper.class) {
            uploadHostStatHelper = e;
        }
        return uploadHostStatHelper;
    }

    private String a(String str) {
        try {
            MessageDigest instance = MessageDigest.getInstance(Coder.KEY_MD5);
            instance.update(b(str));
            BigInteger bigInteger = new BigInteger(1, instance.digest());
            return String.format("%1$032X", new Object[]{bigInteger});
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized void a(Context context) {
        synchronized (UploadHostStatHelper.class) {
            if (e == null) {
                e = new UploadHostStatHelper(context);
            }
        }
    }

    private void a(String str, String str2) {
        String valueOf = String.valueOf(System.nanoTime());
        String valueOf2 = String.valueOf(System.currentTimeMillis());
        Map treeMap = new TreeMap();
        treeMap.put("n", valueOf);
        treeMap.put("d", str2);
        treeMap.put("t", valueOf2);
        treeMap.put("s", a(valueOf + str2 + valueOf2 + "56C6A520%$C99119A0&^229(!@2746C7"));
        d.a(this.f, String.format("http://%1$s/diagnoses/v1/report", new Object[]{str}), treeMap);
    }

    private void a(List<b> list, double d) {
        for (b bVar : list) {
            org.apache.thrift.b cVar = new c();
            cVar.a("httpapi");
            cVar.a(bVar);
            cVar.a(new a());
            String str = new String(com.xiaomi.channel.commonutils.string.a.a(new g(new org
                    .apache.thrift.protocol.b.a()).a(cVar)));
            if (((double) this.b.nextInt(10000)) < 10000.0d * d) {
                try {
                    a("f3.mi-stat.gslb.mi-idc.com", str);
                } catch (IOException e) {
                } catch (Exception e2) {
                }
            }
        }
    }

    private byte[] b(String str) {
        try {
            return str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            return str.getBytes();
        }
    }

    public synchronized void a(HttpRecordCallback httpRecordCallback) {
        this.a.add(httpRecordCallback);
    }

    public void b() {
        if (!this.d) {
            this.d = true;
            this.c.schedule(new d(this), 60000);
        }
    }

    public synchronized void b(HttpRecordCallback httpRecordCallback) {
        this.a.remove(httpRecordCallback);
    }
}
