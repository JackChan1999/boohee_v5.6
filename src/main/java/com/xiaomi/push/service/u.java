package com.xiaomi.push.service;

import com.baidu.location.a0;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.network.Host;
import com.xiaomi.push.protobuf.a.a;
import com.xiaomi.stats.e;

import java.net.Socket;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class u {
    private static final Pattern            a = Pattern.compile("([0-9]{1,3}\\.[0-9]{1,3}\\" +
            ".[0-9]{1,3}\\.[0-9]{1,3})");
    private static       long               b = 0;
    private static       ThreadPoolExecutor c = new ThreadPoolExecutor(1, 1, 20, TimeUnit
            .SECONDS, new LinkedBlockingQueue());

    public static void a() {
        long currentTimeMillis = System.currentTimeMillis();
        if ((c.getActiveCount() <= 0 || currentTimeMillis - b >= a0.i2) && e.a().c()) {
            a d = af.a().d();
            if (d != null && d.l() > 0) {
                b = currentTimeMillis;
                a(d.k(), true);
            }
        }
    }

    public static void a(List<String> list, boolean z) {
        c.execute(new v(list, z));
    }

    private static boolean b(String str) {
        long currentTimeMillis = System.currentTimeMillis();
        try {
            b.a("ConnectivityTest: begin to connect to " + str);
            Socket socket = new Socket();
            socket.connect(Host.b(str, 5222), BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT);
            socket.setTcpNoDelay(true);
            b.a("ConnectivityTest: connect to " + str + " in " + (System.currentTimeMillis() -
                    currentTimeMillis));
            socket.close();
            return true;
        } catch (Throwable th) {
            b.d("ConnectivityTest: could not connect to:" + str + " exception: " + th.getClass()
                    .getSimpleName() + " description: " + th.getMessage());
            return false;
        }
    }
}
