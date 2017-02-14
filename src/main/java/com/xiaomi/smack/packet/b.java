package com.xiaomi.smack.packet;

import android.os.Bundle;

import com.xiaomi.smack.util.g;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class b extends d {
    private       a                   a = a.a;
    private final Map<String, String> d = new HashMap();

    public static class a {
        public static final a a = new a("get");
        public static final a b = new a("set");
        public static final a c = new a("result");
        public static final a d = new a("error");
        public static final a e = new a("command");
        private String f;

        private a(String str) {
            this.f = str;
        }

        public static a a(String str) {
            if (str == null) {
                return null;
            }
            String toLowerCase = str.toLowerCase();
            return a.toString().equals(toLowerCase) ? a : b.toString().equals(toLowerCase) ? b :
                    d.toString().equals(toLowerCase) ? d : c.toString().equals(toLowerCase) ? c :
                            e.toString().equals(toLowerCase) ? e : null;
        }

        public String toString() {
            return this.f;
        }
    }

    public b(Bundle bundle) {
        super(bundle);
        if (bundle.containsKey("ext_iq_type")) {
            this.a = a.a(bundle.getString("ext_iq_type"));
        }
    }

    public String a() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<iq ");
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
            stringBuilder.append("chid=\"").append(g.a(l())).append("\" ");
        }
        for (Entry entry : this.d.entrySet()) {
            stringBuilder.append(g.a((String) entry.getKey())).append("=\"");
            stringBuilder.append(g.a((String) entry.getValue())).append("\" ");
        }
        if (this.a == null) {
            stringBuilder.append("type=\"get\">");
        } else {
            stringBuilder.append("type=\"").append(b()).append("\">");
        }
        String d = d();
        if (d != null) {
            stringBuilder.append(d);
        }
        stringBuilder.append(s());
        h p = p();
        if (p != null) {
            stringBuilder.append(p.d());
        }
        stringBuilder.append("</iq>");
        return stringBuilder.toString();
    }

    public synchronized String a(String str) {
        return (String) this.d.get(str);
    }

    public void a(a aVar) {
        if (aVar == null) {
            this.a = a.a;
        } else {
            this.a = aVar;
        }
    }

    public synchronized void a(String str, String str2) {
        this.d.put(str, str2);
    }

    public synchronized void a(Map<String, String> map) {
        this.d.putAll(map);
    }

    public a b() {
        return this.a;
    }

    public Bundle c() {
        Bundle c = super.c();
        if (this.a != null) {
            c.putString("ext_iq_type", this.a.toString());
        }
        return c;
    }

    public String d() {
        return null;
    }
}
