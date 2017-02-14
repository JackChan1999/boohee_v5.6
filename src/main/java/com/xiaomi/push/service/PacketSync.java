package com.xiaomi.push.service;

import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Base64;

import com.boohee.one.http.DnspodFree;
import com.xiaomi.network.Fallback;
import com.xiaomi.network.HostManager;
import com.xiaomi.push.service.w.c;
import com.xiaomi.smack.b;
import com.xiaomi.smack.l;
import com.xiaomi.smack.packet.a;
import com.xiaomi.smack.packet.d;
import com.xiaomi.smack.packet.h;
import com.xiaomi.smack.util.k;
import com.xiaomi.stats.g;

import java.util.Date;

public class PacketSync {
    private XMPushService a;

    public interface PacketReceiveHandler extends Parcelable {
    }

    PacketSync(XMPushService xMPushService) {
        this.a = xMPushService;
    }

    private void a(a aVar) {
        Object c = aVar.c();
        if (!TextUtils.isEmpty(c)) {
            String[] split = c.split(DnspodFree.IP_SPLIT);
            Fallback fallbacksByHost = HostManager.getInstance().getFallbacksByHost(b.b(), false);
            if (fallbacksByHost != null && split.length > 0) {
                fallbacksByHost.a(split);
                this.a.a(20, null);
                this.a.a(true);
            }
        }
    }

    private void b(d dVar) {
        Object m = dVar.m();
        Object l = dVar.l();
        if (!TextUtils.isEmpty(m) && !TextUtils.isEmpty(l)) {
            w.b b = w.a().b(l, m);
            if (b != null) {
                k.a(this.a, b.a, (long) k.a(dVar.a()), true, System.currentTimeMillis());
            }
        }
    }

    public void a(d dVar) {
        if (!"5".equals(dVar.l())) {
            b(dVar);
        }
        w.b b;
        if (dVar instanceof com.xiaomi.smack.k.b) {
            com.xiaomi.smack.k.b bVar = (com.xiaomi.smack.k.b) dVar;
            com.xiaomi.smack.k.b.a b2 = bVar.b();
            String l = bVar.l();
            String m = bVar.m();
            if (!TextUtils.isEmpty(l)) {
                b = w.a().b(l, m);
                if (b == null) {
                    return;
                }
                if (b2 == com.xiaomi.smack.k.b.a.a) {
                    b.a(c.binded, 1, 0, null, null);
                    com.xiaomi.channel.commonutils.logger.b.a("SMACK: channel bind succeeded, " +
                            "chid=" + l);
                    return;
                }
                h p = bVar.p();
                com.xiaomi.channel.commonutils.logger.b.a("SMACK: channel bind failed, error=" +
                        p.d());
                if (p != null) {
                    if ("auth".equals(p.b())) {
                        if ("invalid-sig".equals(p.a())) {
                            com.xiaomi.channel.commonutils.logger.b.a("SMACK: bind error " +
                                    "invalid-sig token = " + b.c + " sec = " + b.i);
                            g.a(0, com.xiaomi.push.thrift.a.BIND_INVALID_SIG.a(), 1, null, 0);
                        }
                        b.a(c.unbind, 1, 5, p.a(), p.b());
                        w.a().a(l, m);
                    } else if ("cancel".equals(p.b())) {
                        b.a(c.unbind, 1, 7, p.a(), p.b());
                        w.a().a(l, m);
                    } else if ("wait".equals(p.b())) {
                        this.a.b(b);
                        b.a(c.unbind, 1, 7, p.a(), p.b());
                    }
                    com.xiaomi.channel.commonutils.logger.b.a("SMACK: channel bind failed, chid="
                            + l + " reason=" + p.a());
                    return;
                }
                return;
            }
            return;
        }
        String l2 = dVar.l();
        if (TextUtils.isEmpty(l2)) {
            l2 = "1";
            dVar.l(l2);
        }
        a p2;
        String m2;
        String a;
        if (!l2.equals("0")) {
            if (dVar instanceof com.xiaomi.smack.packet.b) {
                p2 = dVar.p("kick");
                if (p2 != null) {
                    m2 = dVar.m();
                    a = p2.a("type");
                    String a2 = p2.a("reason");
                    com.xiaomi.channel.commonutils.logger.b.a("kicked by server, chid=" + l2 + " " +
                            "userid=" + m2 + " type=" + a + " reason=" + a2);
                    if ("wait".equals(a)) {
                        b = w.a().b(l2, m2);
                        if (b != null) {
                            this.a.b(b);
                            b.a(c.unbind, 3, 0, a2, a);
                            return;
                        }
                        return;
                    }
                    this.a.a(l2, m2, 3, a2, a);
                    w.a().a(l2, m2);
                    return;
                }
            } else if (dVar instanceof com.xiaomi.smack.packet.c) {
                com.xiaomi.smack.packet.c cVar = (com.xiaomi.smack.packet.c) dVar;
                if ("redir".equals(cVar.b())) {
                    p2 = cVar.p("hosts");
                    if (p2 != null) {
                        a(p2);
                        return;
                    }
                    return;
                }
            }
            this.a.d().a(this.a, l2, dVar);
        } else if (dVar instanceof com.xiaomi.smack.packet.b) {
            com.xiaomi.smack.packet.b bVar2 = (com.xiaomi.smack.packet.b) dVar;
            if ("0".equals(dVar.k()) && "result".equals(bVar2.b().toString())) {
                com.xiaomi.smack.a g = this.a.g();
                if (g instanceof l) {
                    ((l) g).w();
                }
                g.b();
            } else if ("command".equals(bVar2.b().toString())) {
                p2 = dVar.p("u");
                if (p2 != null) {
                    l2 = p2.a("url");
                    m2 = p2.a("startts");
                    a = p2.a("endts");
                    try {
                        Date date = new Date(Long.parseLong(m2));
                        Date date2 = new Date(Long.parseLong(a));
                        m2 = p2.a("token");
                        boolean equals = "true".equals(p2.a("force"));
                        Object a3 = p2.a("maxlen");
                        com.xiaomi.push.log.b.a(this.a).a(l2, m2, date2, date, !TextUtils.isEmpty
                                (a3) ? Integer.parseInt(a3) * 1024 : 0, equals);
                    } catch (NumberFormatException e) {
                        com.xiaomi.channel.commonutils.logger.b.a("parseLong fail " + e
                                .getMessage());
                    }
                }
            }
            if (bVar2.a("ps") != null) {
                try {
                    af.a().a(com.xiaomi.push.protobuf.b.a.b(Base64.decode(bVar2.a("ps"), 8)));
                } catch (IllegalArgumentException e2) {
                    com.xiaomi.channel.commonutils.logger.b.a("invalid Base64 exception + " + e2
                            .getMessage());
                } catch (com.google.protobuf.micro.c e3) {
                    com.xiaomi.channel.commonutils.logger.b.a("invalid pb exception + " + e3
                            .getMessage());
                }
            }
        }
    }
}
