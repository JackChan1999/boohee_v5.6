package com.tencent.stat;

import android.content.Context;

import com.boohee.one.http.DnspodFree;
import com.tencent.stat.a.e;
import com.tencent.stat.a.i;
import com.tencent.stat.common.k;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;

class j implements Runnable {
    private Context              a = null;
    private Map<String, Integer> b = null;

    public j(Context context, Map<String, Integer> map) {
        this.a = context;
        if (map != null) {
            this.b = map;
        }
    }

    private NetworkMonitor a(String str, int i) {
        NetworkMonitor networkMonitor = new NetworkMonitor();
        Socket socket = new Socket();
        int i2 = 0;
        try {
            networkMonitor.setDomain(str);
            networkMonitor.setPort(i);
            long currentTimeMillis = System.currentTimeMillis();
            SocketAddress inetSocketAddress = new InetSocketAddress(str, i);
            socket.connect(inetSocketAddress, 30000);
            networkMonitor.setMillisecondsConsume(System.currentTimeMillis() - currentTimeMillis);
            networkMonitor.setRemoteIp(inetSocketAddress.getAddress().getHostAddress());
            if (socket != null) {
                socket.close();
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (Object th) {
                    StatService.i.e(th);
                }
            }
        } catch (Exception e) {
            Exception exception = e;
            i2 = -1;
            StatService.i.e(exception);
            if (socket != null) {
                socket.close();
            }
        } catch (Object th2) {
            StatService.i.e(th2);
        }
        networkMonitor.setStatusCode(i2);
        return networkMonitor;
    }

    private Map<String, Integer> a() {
        Map<String, Integer> hashMap = new HashMap();
        String a = StatConfig.a("__MTA_TEST_SPEED__", null);
        if (!(a == null || a.trim().length() == 0)) {
            for (String a2 : a2.split(DnspodFree.IP_SPLIT)) {
                String[] split = a2.split(",");
                if (split != null && split.length == 2) {
                    String str = split[0];
                    if (!(str == null || str.trim().length() == 0)) {
                        try {
                            hashMap.put(str, Integer.valueOf(Integer.valueOf(split[1]).intValue()));
                        } catch (Exception e) {
                            StatService.i.e(e);
                        }
                    }
                }
            }
        }
        return hashMap;
    }

    public void run() {
        try {
            if (k.h(this.a)) {
                if (this.b == null) {
                    this.b = a();
                }
                if (this.b == null || this.b.size() == 0) {
                    StatService.i.w("empty domain list.");
                    return;
                }
                JSONArray jSONArray = new JSONArray();
                for (Entry entry : this.b.entrySet()) {
                    String str = (String) entry.getKey();
                    if (str == null || str.length() == 0) {
                        StatService.i.w("empty domain name.");
                    } else if (((Integer) entry.getValue()) == null) {
                        StatService.i.w("port is null for " + str);
                    } else {
                        jSONArray.put(a((String) entry.getKey(), ((Integer) entry.getValue())
                                .intValue()).toJSONObject());
                    }
                }
                if (jSONArray.length() != 0) {
                    e iVar = new i(this.a, StatService.a(this.a, false));
                    iVar.a(jSONArray.toString());
                    if (StatService.c(this.a) != null) {
                        StatService.c(this.a).post(new k(iVar));
                    }
                }
            }
        } catch (Object th) {
            StatService.i.e(th);
        }
    }
}
