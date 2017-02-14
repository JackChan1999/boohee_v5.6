package com.xiaomi.push.service;

import android.content.Context;

import com.xiaomi.channel.commonutils.network.d;
import com.xiaomi.network.Fallback;
import com.xiaomi.network.HostFilter;
import com.xiaomi.network.HostManager;
import com.xiaomi.network.HostManager.HostManagerFactory;
import com.xiaomi.network.HostManager.HttpGet;
import com.xiaomi.network.HostManagerV2;
import com.xiaomi.smack.util.h;
import com.xiaomi.stats.e;
import com.xiaomi.stats.g;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class z extends com.xiaomi.push.service.af.a implements HostManagerFactory {
    private XMPushService a;
    private long          b;

    static class a implements HttpGet {
        a() {
        }

        public String a(String str) {
            URL url = new URL(str);
            int port = url.getPort() == -1 ? 80 : url.getPort();
            try {
                long currentTimeMillis = System.currentTimeMillis();
                String a = d.a(h.a(), url);
                g.a(url.getHost() + ":" + port, (int) (System.currentTimeMillis() -
                        currentTimeMillis), null);
                return a;
            } catch (Exception e) {
                g.a(url.getHost() + ":" + port, -1, e);
                throw e;
            }
        }
    }

    static class b extends HostManagerV2 {
        protected b(Context context, HostFilter hostFilter, HttpGet httpGet, String str) {
            super(context, hostFilter, httpGet, str);
        }

        protected String getRemoteFallbackJSON(ArrayList<String> arrayList, String str, String
                str2) {
            try {
                if (e.a().c()) {
                    str2 = h.b();
                }
                return super.getRemoteFallbackJSON(arrayList, str, str2);
            } catch (IOException e) {
                IOException iOException = e;
                g.a(0, com.xiaomi.push.thrift.a.GSLB_ERR.a(), 1, null, d.d(this.sAppContext) ? 1
                        : 0);
                throw iOException;
            }
        }
    }

    z(XMPushService xMPushService) {
        this.a = xMPushService;
    }

    public static void a(XMPushService xMPushService) {
        com.xiaomi.push.service.af.a zVar = new z(xMPushService);
        af.a().a(zVar);
        com.xiaomi.push.protobuf.a.a d = af.a().d();
        boolean z = true;
        if (d != null && d.f()) {
            z = d.f();
        }
        if (z) {
            HostManager.setHostManagerFactory(zVar);
        }
        HostManager.init(xMPushService, null, new a(), "0", "push", "2.2");
    }

    public HostManager a(Context context, HostFilter hostFilter, HttpGet httpGet, String str) {
        return new b(context, hostFilter, httpGet, str);
    }

    public void a(com.xiaomi.push.protobuf.a.a aVar) {
        if (aVar.f()) {
            com.xiaomi.channel.commonutils.logger.b.a("Switch to BucketV2 :" + aVar.e());
            HostManager instance = HostManager.getInstance();
            synchronized (HostManager.class) {
                if (aVar.e()) {
                    if (!(instance instanceof HostManagerV2)) {
                        HostManager.setHostManagerFactory(this);
                        HostManager.init(this.a, null, new a(), "0", "push", "2.2");
                    }
                } else if (HostManager.getInstance() instanceof HostManagerV2) {
                    HostManager.setHostManagerFactory(null);
                    HostManager.init(this.a, null, new a(), "0", "push", "2.2");
                }
            }
        }
    }

    public void a(com.xiaomi.push.protobuf.b.a aVar) {
        if (aVar.d() && System.currentTimeMillis() - this.b > 3600000) {
            com.xiaomi.channel.commonutils.logger.b.a("fetch bucket :" + aVar.c());
            this.b = System.currentTimeMillis();
            HostManager instance = HostManager.getInstance();
            instance.clear();
            instance.refreshFallbacks();
            com.xiaomi.smack.a g = this.a.g();
            if (g != null) {
                Fallback fallbacksByHost = instance.getFallbacksByHost(g.a().f());
                if (fallbacksByHost != null) {
                    boolean z;
                    ArrayList d = fallbacksByHost.d();
                    Iterator it = d.iterator();
                    while (it.hasNext()) {
                        if (((String) it.next()).equals(g.c())) {
                            z = false;
                            break;
                        }
                    }
                    z = true;
                    if (z && !d.isEmpty()) {
                        com.xiaomi.channel.commonutils.logger.b.a("bucket changed, force " +
                                "reconnect");
                        this.a.a(0, null);
                        this.a.a(false);
                    }
                }
            }
        }
    }
}
