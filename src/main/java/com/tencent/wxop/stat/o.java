package com.tencent.wxop.stat;

import android.content.Context;

import com.boohee.one.http.DnspodFree;
import com.tencent.wxop.stat.a.d;
import com.tencent.wxop.stat.a.g;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;

final class o implements Runnable {
    private f                    bM = null;
    private Map<String, Integer> bO = null;
    private Context              e  = null;

    public o(Context context) {
        this.e = context;
        this.bM = null;
    }

    private static b a(String str, int i) {
        Throwable th;
        b bVar = new b();
        Socket socket = new Socket();
        int i2 = 0;
        try {
            bVar.setDomain(str);
            bVar.setPort(i);
            long currentTimeMillis = System.currentTimeMillis();
            SocketAddress inetSocketAddress = new InetSocketAddress(str, i);
            socket.connect(inetSocketAddress, 30000);
            bVar.a(System.currentTimeMillis() - currentTimeMillis);
            bVar.k(inetSocketAddress.getAddress().getHostAddress());
            socket.close();
            try {
                socket.close();
            } catch (Throwable th2) {
                e.aV.b(th2);
            }
        } catch (Throwable e) {
            th2 = e;
            i2 = -1;
            e.aV.b(th2);
            socket.close();
        } catch (Throwable th22) {
            e.aV.b(th22);
        }
        bVar.setStatusCode(i2);
        return bVar;
    }

    private static Map<String, Integer> ag() {
        Map<String, Integer> hashMap = new HashMap();
        String l = c.l("__MTA_TEST_SPEED__");
        if (!(l == null || l.trim().length() == 0)) {
            for (String l2 : l2.split(DnspodFree.IP_SPLIT)) {
                String[] split = l2.split(",");
                if (split != null && split.length == 2) {
                    String str = split[0];
                    if (!(str == null || str.trim().length() == 0)) {
                        try {
                            hashMap.put(str, Integer.valueOf(Integer.valueOf(split[1]).intValue()));
                        } catch (Throwable e) {
                            e.aV.b(e);
                        }
                    }
                }
            }
        }
        return hashMap;
    }

    public final void run() {
        try {
            if (this.bO == null) {
                this.bO = ag();
            }
            if (this.bO == null || this.bO.size() == 0) {
                e.aV.b((Object) "empty domain list.");
                return;
            }
            JSONArray jSONArray = new JSONArray();
            for (Entry entry : this.bO.entrySet()) {
                String str = (String) entry.getKey();
                if (str == null || str.length() == 0) {
                    e.aV.c("empty domain name.");
                } else if (((Integer) entry.getValue()) == null) {
                    e.aV.c("port is null for " + str);
                } else {
                    jSONArray.put(a((String) entry.getKey(), ((Integer) entry.getValue())
                            .intValue()).i());
                }
            }
            if (jSONArray.length() != 0) {
                d gVar = new g(this.e, e.a(this.e, false, this.bM), this.bM);
                gVar.b(jSONArray.toString());
                new p(gVar).ah();
            }
        } catch (Throwable th) {
            e.aV.b(th);
        }
    }
}
