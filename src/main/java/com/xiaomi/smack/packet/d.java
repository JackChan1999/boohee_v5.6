package com.xiaomi.smack.packet;

import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;

import com.umeng.socialize.common.SocializeConstants;
import com.xiaomi.smack.util.g;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class d {
    private static         String              a = null;
    protected static final String              b = Locale.getDefault().getLanguage().toLowerCase();
    public static final    DateFormat          c = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss" +
            ".SSS'Z'");
    private static         String              d = (g.a(5) + SocializeConstants.OP_DIVIDER_MINUS);
    private static         long                e = 0;
    private                String              f = a;
    private                String              g = null;
    private                String              h = null;
    private                String              i = null;
    private                String              j = null;
    private                String              k = null;
    private                List<a>             l = new CopyOnWriteArrayList();
    private final          Map<String, Object> m = new HashMap();
    private                h                   n = null;

    static {
        c.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public d(Bundle bundle) {
        this.h = bundle.getString("ext_to");
        this.i = bundle.getString("ext_from");
        this.j = bundle.getString("ext_chid");
        this.g = bundle.getString("ext_pkt_id");
        Parcelable[] parcelableArray = bundle.getParcelableArray("ext_exts");
        if (parcelableArray != null) {
            this.l = new ArrayList(parcelableArray.length);
            for (Parcelable parcelable : parcelableArray) {
                a a = a.a((Bundle) parcelable);
                if (a != null) {
                    this.l.add(a);
                }
            }
        }
        Bundle bundle2 = bundle.getBundle("ext_ERROR");
        if (bundle2 != null) {
            this.n = new h(bundle2);
        }
    }

    public static synchronized String j() {
        String stringBuilder;
        synchronized (d.class) {
            StringBuilder append = new StringBuilder().append(d);
            long j = e;
            e = 1 + j;
            stringBuilder = append.append(Long.toString(j)).toString();
        }
        return stringBuilder;
    }

    public static String u() {
        return b;
    }

    public abstract String a();

    public void a(a aVar) {
        this.l.add(aVar);
    }

    public void a(h hVar) {
        this.n = hVar;
    }

    public a b(String str, String str2) {
        for (a aVar : this.l) {
            if ((str2 == null || str2.equals(aVar.b())) && str.equals(aVar.a())) {
                return aVar;
            }
        }
        return null;
    }

    public Bundle c() {
        Bundle bundle = new Bundle();
        if (!TextUtils.isEmpty(this.f)) {
            bundle.putString("ext_ns", this.f);
        }
        if (!TextUtils.isEmpty(this.i)) {
            bundle.putString("ext_from", this.i);
        }
        if (!TextUtils.isEmpty(this.h)) {
            bundle.putString("ext_to", this.h);
        }
        if (!TextUtils.isEmpty(this.g)) {
            bundle.putString("ext_pkt_id", this.g);
        }
        if (!TextUtils.isEmpty(this.j)) {
            bundle.putString("ext_chid", this.j);
        }
        if (this.n != null) {
            bundle.putBundle("ext_ERROR", this.n.c());
        }
        if (this.l != null) {
            Parcelable[] parcelableArr = new Bundle[this.l.size()];
            int i = 0;
            for (a e : this.l) {
                int i2;
                Bundle e2 = e.e();
                if (e2 != null) {
                    i2 = i + 1;
                    parcelableArr[i] = e2;
                } else {
                    i2 = i;
                }
                i = i2;
            }
            bundle.putParcelableArray("ext_exts", parcelableArr);
        }
        return bundle;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r5) {
        /*
        r4 = this;
        r0 = 1;
        r1 = 0;
        if (r4 != r5) goto L_0x0006;
    L_0x0004:
        r1 = r0;
    L_0x0005:
        return r1;
    L_0x0006:
        if (r5 == 0) goto L_0x0005;
    L_0x0008:
        r2 = r4.getClass();
        r3 = r5.getClass();
        if (r2 != r3) goto L_0x0005;
    L_0x0012:
        r5 = (com.xiaomi.smack.packet.d) r5;
        r2 = r4.n;
        if (r2 == 0) goto L_0x0083;
    L_0x0018:
        r2 = r4.n;
        r3 = r5.n;
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0005;
    L_0x0022:
        r2 = r4.i;
        if (r2 == 0) goto L_0x0089;
    L_0x0026:
        r2 = r4.i;
        r3 = r5.i;
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0005;
    L_0x0030:
        r2 = r4.l;
        r3 = r5.l;
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0005;
    L_0x003a:
        r2 = r4.g;
        if (r2 == 0) goto L_0x008f;
    L_0x003e:
        r2 = r4.g;
        r3 = r5.g;
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0005;
    L_0x0048:
        r2 = r4.j;
        if (r2 == 0) goto L_0x0095;
    L_0x004c:
        r2 = r4.j;
        r3 = r5.j;
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0005;
    L_0x0056:
        r2 = r4.m;
        if (r2 == 0) goto L_0x009b;
    L_0x005a:
        r2 = r4.m;
        r3 = r5.m;
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0005;
    L_0x0064:
        r2 = r4.h;
        if (r2 == 0) goto L_0x00a1;
    L_0x0068:
        r2 = r4.h;
        r3 = r5.h;
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0005;
    L_0x0072:
        r2 = r4.f;
        if (r2 == 0) goto L_0x00a7;
    L_0x0076:
        r2 = r4.f;
        r3 = r5.f;
        r2 = r2.equals(r3);
        if (r2 != 0) goto L_0x0081;
    L_0x0080:
        r0 = r1;
    L_0x0081:
        r1 = r0;
        goto L_0x0005;
    L_0x0083:
        r2 = r5.n;
        if (r2 == 0) goto L_0x0022;
    L_0x0087:
        goto L_0x0005;
    L_0x0089:
        r2 = r5.i;
        if (r2 == 0) goto L_0x0030;
    L_0x008d:
        goto L_0x0005;
    L_0x008f:
        r2 = r5.g;
        if (r2 == 0) goto L_0x0048;
    L_0x0093:
        goto L_0x0005;
    L_0x0095:
        r2 = r5.j;
        if (r2 == 0) goto L_0x0056;
    L_0x0099:
        goto L_0x0005;
    L_0x009b:
        r2 = r5.m;
        if (r2 == 0) goto L_0x0064;
    L_0x009f:
        goto L_0x0005;
    L_0x00a1:
        r2 = r5.h;
        if (r2 == 0) goto L_0x0072;
    L_0x00a5:
        goto L_0x0005;
    L_0x00a7:
        r2 = r5.f;
        if (r2 != 0) goto L_0x0080;
    L_0x00ab:
        goto L_0x0081;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.smack.packet.d" +
                ".equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        int i = 0;
        int hashCode = ((((((this.j != null ? this.j.hashCode() : 0) + (((this.i != null ? this.i
                .hashCode() : 0) + (((this.h != null ? this.h.hashCode() : 0) + (((this.g != null
                ? this.g.hashCode() : 0) + ((this.f != null ? this.f.hashCode() : 0) * 31)) * 31)
        ) * 31)) * 31)) * 31) + this.l.hashCode()) * 31) + this.m.hashCode()) * 31;
        if (this.n != null) {
            i = this.n.hashCode();
        }
        return hashCode + i;
    }

    public String k() {
        if ("ID_NOT_AVAILABLE".equals(this.g)) {
            return null;
        }
        if (this.g == null) {
            this.g = j();
        }
        return this.g;
    }

    public void k(String str) {
        this.g = str;
    }

    public String l() {
        return this.j;
    }

    public void l(String str) {
        this.j = str;
    }

    public String m() {
        return this.h;
    }

    public void m(String str) {
        this.h = str;
    }

    public String n() {
        return this.i;
    }

    public void n(String str) {
        this.i = str;
    }

    public String o() {
        return this.k;
    }

    public void o(String str) {
        this.k = str;
    }

    public a p(String str) {
        return b(str, null);
    }

    public h p() {
        return this.n;
    }

    public synchronized Object q(String str) {
        return this.m == null ? null : this.m.get(str);
    }

    public synchronized Collection<a> q() {
        return this.l == null ? Collections.emptyList() : Collections.unmodifiableList(new
                ArrayList(this.l));
    }

    public synchronized Collection<String> r() {
        return this.m == null ? Collections.emptySet() : Collections.unmodifiableSet(new HashSet
                (this.m.keySet()));
    }

    protected synchronized String s() {
        StringBuilder stringBuilder;
        ByteArrayOutputStream byteArrayOutputStream;
        Exception e;
        ObjectOutputStream objectOutputStream;
        ByteArrayOutputStream byteArrayOutputStream2;
        Throwable th;
        ObjectOutputStream objectOutputStream2;
        stringBuilder = new StringBuilder();
        for (a d : q()) {
            stringBuilder.append(d.d());
        }
        if (!(this.m == null || this.m.isEmpty())) {
            stringBuilder.append("<properties xmlns=\"http://www.jivesoftware" +
                    ".com/xmlns/xmpp/properties\">");
            for (String str : r()) {
                Object q = q(str);
                stringBuilder.append("<property>");
                stringBuilder.append("<name>").append(g.a(str)).append("</name>");
                stringBuilder.append("<value type=\"");
                if (q instanceof Integer) {
                    stringBuilder.append("integer\">").append(q).append("</value>");
                } else if (q instanceof Long) {
                    stringBuilder.append("long\">").append(q).append("</value>");
                } else if (q instanceof Float) {
                    stringBuilder.append("float\">").append(q).append("</value>");
                } else if (q instanceof Double) {
                    stringBuilder.append("double\">").append(q).append("</value>");
                } else if (q instanceof Boolean) {
                    stringBuilder.append("boolean\">").append(q).append("</value>");
                } else if (q instanceof String) {
                    stringBuilder.append("string\">");
                    stringBuilder.append(g.a((String) q));
                    stringBuilder.append("</value>");
                } else {
                    try {
                        byteArrayOutputStream = new ByteArrayOutputStream();
                        try {
                            objectOutputStream2 = new ObjectOutputStream(byteArrayOutputStream);
                            try {
                                objectOutputStream2.writeObject(q);
                                stringBuilder.append("java-object\">");
                                stringBuilder.append(g.a(byteArrayOutputStream.toByteArray()))
                                        .append("</value>");
                                if (objectOutputStream2 != null) {
                                    try {
                                        objectOutputStream2.close();
                                    } catch (Exception e2) {
                                    }
                                }
                                if (byteArrayOutputStream != null) {
                                    try {
                                        byteArrayOutputStream.close();
                                    } catch (Exception e3) {
                                    }
                                }
                            } catch (Exception e4) {
                                e = e4;
                                objectOutputStream = objectOutputStream2;
                                byteArrayOutputStream2 = byteArrayOutputStream;
                                try {
                                    e.printStackTrace();
                                    if (objectOutputStream != null) {
                                        try {
                                            objectOutputStream.close();
                                        } catch (Exception e5) {
                                        }
                                    }
                                    if (byteArrayOutputStream2 == null) {
                                        try {
                                            byteArrayOutputStream2.close();
                                        } catch (Exception e6) {
                                        }
                                    }
                                    stringBuilder.append("</property>");
                                } catch (Throwable th2) {
                                    th = th2;
                                    byteArrayOutputStream = byteArrayOutputStream2;
                                    objectOutputStream2 = objectOutputStream;
                                }
                            } catch (Throwable th3) {
                                th = th3;
                            }
                        } catch (Exception e7) {
                            e = e7;
                            objectOutputStream = null;
                            byteArrayOutputStream2 = byteArrayOutputStream;
                            e.printStackTrace();
                            if (objectOutputStream != null) {
                                objectOutputStream.close();
                            }
                            if (byteArrayOutputStream2 == null) {
                                byteArrayOutputStream2.close();
                            }
                            stringBuilder.append("</property>");
                        } catch (Throwable th4) {
                            th = th4;
                            objectOutputStream2 = null;
                        }
                    } catch (Exception e8) {
                        e = e8;
                        objectOutputStream = null;
                        byteArrayOutputStream2 = null;
                        e.printStackTrace();
                        if (objectOutputStream != null) {
                            objectOutputStream.close();
                        }
                        if (byteArrayOutputStream2 == null) {
                            byteArrayOutputStream2.close();
                        }
                        stringBuilder.append("</property>");
                    } catch (Throwable th5) {
                        th = th5;
                        objectOutputStream2 = null;
                        byteArrayOutputStream = null;
                    }
                }
                stringBuilder.append("</property>");
            }
            stringBuilder.append("</properties>");
        }
        return stringBuilder.toString();
        if (byteArrayOutputStream != null) {
            try {
                byteArrayOutputStream.close();
            } catch (Exception e9) {
            }
        }
        throw th;
        throw th;
        if (objectOutputStream2 != null) {
            try {
                objectOutputStream2.close();
            } catch (Exception e10) {
            }
        }
        if (byteArrayOutputStream != null) {
            byteArrayOutputStream.close();
        }
        throw th;
    }

    public String t() {
        return this.f;
    }
}
