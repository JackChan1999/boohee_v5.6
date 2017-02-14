package com.xiaomi.smack;

import android.text.TextUtils;

import com.tencent.connect.common.Constants;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import com.xiaomi.smack.packet.d;
import com.xiaomi.smack.packet.h;
import com.xiaomi.smack.util.g;

import java.util.HashMap;
import java.util.Map;

public class k {

    public class a extends d {
        final /* synthetic */ k a;

        public a(k kVar, com.xiaomi.push.service.w.b bVar, String str, a aVar) {
            Object obj;
            this.a = kVar;
            Map hashMap = new HashMap();
            int i = aVar.i();
            hashMap.put("challenge", str);
            hashMap.put("token", bVar.c);
            hashMap.put("chid", bVar.h);
            hashMap.put("from", bVar.b);
            hashMap.put("id", k());
            hashMap.put(SocializeProtocolConstants.PROTOCOL_KEY_SHARE_TO, "xiaomi.com");
            if (bVar.e) {
                hashMap.put("kick", "1");
            } else {
                hashMap.put("kick", "0");
            }
            if (aVar.k() > 0) {
                String format = String.format("conn:%1$d,t:%2$d", new Object[]{Integer.valueOf(i)
                        , Long.valueOf(aVar.k())});
                hashMap.put(Constants.PARAM_PLATFORM_ID, format);
                aVar.j();
                aVar.l();
                obj = format;
            } else {
                obj = null;
            }
            if (TextUtils.isEmpty(bVar.f)) {
                hashMap.put("client_attrs", "");
            } else {
                hashMap.put("client_attrs", bVar.f);
            }
            if (TextUtils.isEmpty(bVar.g)) {
                hashMap.put("cloud_attrs", "");
            } else {
                hashMap.put("cloud_attrs", bVar.g);
            }
            String a = (bVar.d.equals("XIAOMI-PASS") || bVar.d.equals("XMPUSH-PASS")) ? com
                    .xiaomi.channel.commonutils.string.b.a(bVar.d, null, hashMap, bVar.i) : bVar
                    .d.equals("XIAOMI-SASL") ? null : null;
            l(bVar.h);
            n(bVar.b);
            m("xiaomi.com");
            o(bVar.a);
            com.xiaomi.smack.packet.a aVar2 = new com.xiaomi.smack.packet.a("token", null,
                    (String[]) null, (String[]) null);
            aVar2.b(bVar.c);
            a(aVar2);
            aVar2 = new com.xiaomi.smack.packet.a("kick", null, (String[]) null, (String[]) null);
            aVar2.b(bVar.e ? "1" : "0");
            a(aVar2);
            aVar2 = new com.xiaomi.smack.packet.a("sig", null, (String[]) null, (String[]) null);
            aVar2.b(a);
            a(aVar2);
            com.xiaomi.smack.packet.a aVar3 = new com.xiaomi.smack.packet.a("method", null,
                    (String[]) null, (String[]) null);
            if (TextUtils.isEmpty(bVar.d)) {
                aVar3.b("XIAOMI-SASL");
            } else {
                aVar3.b(bVar.d);
            }
            a(aVar3);
            aVar3 = new com.xiaomi.smack.packet.a("client_attrs", null, (String[]) null,
                    (String[]) null);
            aVar3.b(bVar.f == null ? "" : g.a(bVar.f));
            a(aVar3);
            aVar3 = new com.xiaomi.smack.packet.a("cloud_attrs", null, (String[]) null,
                    (String[]) null);
            aVar3.b(bVar.g == null ? "" : g.a(bVar.g));
            a(aVar3);
            if (!TextUtils.isEmpty(obj)) {
                aVar3 = new com.xiaomi.smack.packet.a(Constants.PARAM_PLATFORM_ID, null,
                        (String[]) null, (String[]) null);
                aVar3.b(obj);
                a(aVar3);
            }
        }

        public String a() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<bind ");
            if (k() != null) {
                stringBuilder.append("id=\"" + k() + "\" ");
            }
            if (m() != null) {
                stringBuilder.append("to=\"").append(g.a(m())).append("\" ");
            }
            if (n() != null) {
                stringBuilder.append("from=\"").append(g.a(n())).append("\" ");
            }
            if (l() != null) {
                stringBuilder.append("chid=\"").append(g.a(l())).append("\">");
            }
            if (q() != null) {
                for (com.xiaomi.smack.packet.a d : q()) {
                    stringBuilder.append(d.d());
                }
            }
            stringBuilder.append("</bind>");
            return stringBuilder.toString();
        }
    }

    public static class b extends d {
        private a a;

        public static class a {
            public static final a a = new a("result");
            public static final a b = new a("error");
            private String c;

            private a(String str) {
                this.c = str;
            }

            public static a a(String str) {
                if (str == null) {
                    return null;
                }
                String toLowerCase = str.toLowerCase();
                return b.toString().equals(toLowerCase) ? b : a.toString().equals(toLowerCase) ?
                        a : null;
            }

            public String toString() {
                return this.c;
            }
        }

        public String a() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<bind ");
            if (k() != null) {
                stringBuilder.append("id=\"" + k() + "\" ");
            }
            if (m() != null) {
                stringBuilder.append("to=\"").append(g.a(m())).append("\" ");
            }
            if (n() != null) {
                stringBuilder.append("from=\"").append(g.a(n())).append("\" ");
            }
            if (l() != null) {
                stringBuilder.append(" chid=\"").append(g.a(l())).append("\" ");
            }
            if (this.a == null) {
                stringBuilder.append("type=\"result\">");
            } else {
                stringBuilder.append("type=\"").append(b()).append("\">");
            }
            if (q() != null) {
                for (com.xiaomi.smack.packet.a d : q()) {
                    stringBuilder.append(d.d());
                }
            }
            h p = p();
            if (p != null) {
                stringBuilder.append(p.d());
            }
            stringBuilder.append("</bind>");
            return stringBuilder.toString();
        }

        public void a(a aVar) {
            if (aVar == null) {
                this.a = a.a;
            } else {
                this.a = aVar;
            }
        }

        public a b() {
            return this.a;
        }
    }

    public void a(com.xiaomi.push.service.w.b bVar, String str, a aVar) {
        d aVar2 = new a(this, bVar, str, aVar);
        aVar.a(aVar2);
        com.xiaomi.channel.commonutils.logger.b.a("SMACK: bind id=" + aVar2.k());
    }
}
