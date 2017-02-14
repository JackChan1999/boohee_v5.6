package com.xiaomi.smack.packet;

import android.os.Bundle;
import android.text.TextUtils;

import com.alipay.sdk.sys.a;
import com.xiaomi.smack.util.g;

public class c extends d {
    private String a = null;
    private String d = null;
    private String e;
    private String f;
    private String g;
    private String h;
    private boolean i = false;
    private String j;
    private String  k = "";
    private String  l = "";
    private String  m = "";
    private String  n = "";
    private boolean o = false;

    public c(Bundle bundle) {
        super(bundle);
        this.a = bundle.getString("ext_msg_type");
        this.e = bundle.getString("ext_msg_lang");
        this.d = bundle.getString("ext_msg_thread");
        this.f = bundle.getString("ext_msg_sub");
        this.g = bundle.getString("ext_msg_body");
        this.h = bundle.getString("ext_body_encode");
        this.j = bundle.getString("ext_msg_appid");
        this.i = bundle.getBoolean("ext_msg_trans", false);
        this.o = bundle.getBoolean("ext_msg_encrypt", false);
        this.k = bundle.getString("ext_msg_seq");
        this.l = bundle.getString("ext_msg_mseq");
        this.m = bundle.getString("ext_msg_fseq");
        this.n = bundle.getString("ext_msg_status");
    }

    public String a() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<message");
        if (t() != null) {
            stringBuilder.append(" xmlns=\"").append(t()).append(a.e);
        }
        if (this.e != null) {
            stringBuilder.append(" xml:lang=\"").append(i()).append(a.e);
        }
        if (k() != null) {
            stringBuilder.append(" id=\"").append(k()).append(a.e);
        }
        if (m() != null) {
            stringBuilder.append(" to=\"").append(g.a(m())).append(a.e);
        }
        if (!TextUtils.isEmpty(e())) {
            stringBuilder.append(" seq=\"").append(e()).append(a.e);
        }
        if (!TextUtils.isEmpty(f())) {
            stringBuilder.append(" mseq=\"").append(f()).append(a.e);
        }
        if (!TextUtils.isEmpty(g())) {
            stringBuilder.append(" fseq=\"").append(g()).append(a.e);
        }
        if (!TextUtils.isEmpty(h())) {
            stringBuilder.append(" status=\"").append(h()).append(a.e);
        }
        if (n() != null) {
            stringBuilder.append(" from=\"").append(g.a(n())).append(a.e);
        }
        if (l() != null) {
            stringBuilder.append(" chid=\"").append(g.a(l())).append(a.e);
        }
        if (this.i) {
            stringBuilder.append(" transient=\"true\"");
        }
        if (!TextUtils.isEmpty(this.j)) {
            stringBuilder.append(" appid=\"").append(d()).append(a.e);
        }
        if (!TextUtils.isEmpty(this.a)) {
            stringBuilder.append(" type=\"").append(this.a).append(a.e);
        }
        if (this.o) {
            stringBuilder.append(" s=\"1\"");
        }
        stringBuilder.append(">");
        if (this.f != null) {
            stringBuilder.append("<subject>").append(g.a(this.f));
            stringBuilder.append("</subject>");
        }
        if (this.g != null) {
            stringBuilder.append("<body");
            if (!TextUtils.isEmpty(this.h)) {
                stringBuilder.append(" encode=\"").append(this.h).append(a.e);
            }
            stringBuilder.append(">").append(g.a(this.g)).append("</body>");
        }
        if (this.d != null) {
            stringBuilder.append("<thread>").append(this.d).append("</thread>");
        }
        if ("error".equalsIgnoreCase(this.a)) {
            h p = p();
            if (p != null) {
                stringBuilder.append(p.d());
            }
        }
        stringBuilder.append(s());
        stringBuilder.append("</message>");
        return stringBuilder.toString();
    }

    public void a(String str) {
        this.j = str;
    }

    public void a(String str, String str2) {
        this.g = str;
        this.h = str2;
    }

    public void a(boolean z) {
        this.i = z;
    }

    public String b() {
        return this.a;
    }

    public void b(String str) {
        this.k = str;
    }

    public void b(boolean z) {
        this.o = z;
    }

    public Bundle c() {
        Bundle c = super.c();
        if (!TextUtils.isEmpty(this.a)) {
            c.putString("ext_msg_type", this.a);
        }
        if (this.e != null) {
            c.putString("ext_msg_lang", this.e);
        }
        if (this.f != null) {
            c.putString("ext_msg_sub", this.f);
        }
        if (this.g != null) {
            c.putString("ext_msg_body", this.g);
        }
        if (!TextUtils.isEmpty(this.h)) {
            c.putString("ext_body_encode", this.h);
        }
        if (this.d != null) {
            c.putString("ext_msg_thread", this.d);
        }
        if (this.j != null) {
            c.putString("ext_msg_appid", this.j);
        }
        if (this.i) {
            c.putBoolean("ext_msg_trans", true);
        }
        if (!TextUtils.isEmpty(this.k)) {
            c.putString("ext_msg_seq", this.k);
        }
        if (!TextUtils.isEmpty(this.l)) {
            c.putString("ext_msg_mseq", this.l);
        }
        if (!TextUtils.isEmpty(this.m)) {
            c.putString("ext_msg_fseq", this.m);
        }
        if (this.o) {
            c.putBoolean("ext_msg_encrypt", true);
        }
        if (!TextUtils.isEmpty(this.n)) {
            c.putString("ext_msg_status", this.n);
        }
        return c;
    }

    public void c(String str) {
        this.l = str;
    }

    public String d() {
        return this.j;
    }

    public void d(String str) {
        this.m = str;
    }

    public String e() {
        return this.k;
    }

    public void e(String str) {
        this.n = str;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        c cVar = (c) obj;
        if (!super.equals(cVar)) {
            return false;
        }
        if (this.g != null) {
            if (!this.g.equals(cVar.g)) {
                return false;
            }
        } else if (cVar.g != null) {
            return false;
        }
        if (this.e != null) {
            if (!this.e.equals(cVar.e)) {
                return false;
            }
        } else if (cVar.e != null) {
            return false;
        }
        if (this.f != null) {
            if (!this.f.equals(cVar.f)) {
                return false;
            }
        } else if (cVar.f != null) {
            return false;
        }
        if (this.d != null) {
            if (!this.d.equals(cVar.d)) {
                return false;
            }
        } else if (cVar.d != null) {
            return false;
        }
        if (this.a != cVar.a) {
            z = false;
        }
        return z;
    }

    public String f() {
        return this.l;
    }

    public void f(String str) {
        this.a = str;
    }

    public String g() {
        return this.m;
    }

    public void g(String str) {
        this.f = str;
    }

    public String h() {
        return this.n;
    }

    public void h(String str) {
        this.g = str;
    }

    public int hashCode() {
        int i = 0;
        int hashCode = ((this.e != null ? this.e.hashCode() : 0) + (((this.d != null ? this.d
                .hashCode() : 0) + (((this.g != null ? this.g.hashCode() : 0) + ((this.a != null
                ? this.a.hashCode() : 0) * 31)) * 31)) * 31)) * 31;
        if (this.f != null) {
            i = this.f.hashCode();
        }
        return hashCode + i;
    }

    public String i() {
        return this.e;
    }

    public void i(String str) {
        this.d = str;
    }

    public void j(String str) {
        this.e = str;
    }
}
