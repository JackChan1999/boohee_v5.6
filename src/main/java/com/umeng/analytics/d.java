package com.umeng.analytics;

import android.content.Context;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

import u.aly.aa;
import u.aly.ae;
import u.aly.ah;
import u.aly.bv;
import u.aly.m;
import u.aly.o;
import u.aly.p;
import u.aly.w;
import u.aly.y;

/* compiled from: InternalAgent */
public class d implements w {
    private Context a = null;
    private c b;
    private o  c = new o();
    private ae d = new ae();
    private aa e = new aa();
    private p f;
    private m g;
    private boolean h = false;

    d() {
        this.c.a((w) this);
    }

    private void e(Context context) {
        if (!this.h) {
            this.a = context.getApplicationContext();
            this.f = new p(this.a);
            this.g = m.a(this.a);
            this.h = true;
        }
    }

    void a(String str) {
        if (!AnalyticsConfig.ACTIVITY_DURATION_OPEN) {
            try {
                this.d.a(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void b(String str) {
        if (!AnalyticsConfig.ACTIVITY_DURATION_OPEN) {
            try {
                this.d.b(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void a(c cVar) {
        this.b = cVar;
    }

    public void a(int i) {
        AnalyticsConfig.mVerticalType = i;
    }

    public void a(String str, String str2) {
        AnalyticsConfig.mWrapperType = str;
        AnalyticsConfig.mWrapperVersion = str2;
    }

    void a(final Context context) {
        if (context == null) {
            bv.e("unexpected null context in onResume");
            return;
        }
        if (AnalyticsConfig.ACTIVITY_DURATION_OPEN) {
            this.d.a(context.getClass().getName());
        }
        try {
            if (!this.h) {
                e(context);
            }
            f.a(new g(this) {
                final /* synthetic */ d b;

                public void a() {
                    this.b.f(context.getApplicationContext());
                }
            });
        } catch (Throwable e) {
            bv.e("Exception occurred in Mobclick.onResume(). ", e);
        }
    }

    void b(final Context context) {
        if (context == null) {
            bv.e("unexpected null context in onPause");
            return;
        }
        if (AnalyticsConfig.ACTIVITY_DURATION_OPEN) {
            this.d.b(context.getClass().getName());
        }
        try {
            if (!this.h) {
                e(context);
            }
            f.a(new g(this) {
                final /* synthetic */ d b;

                public void a() {
                    this.b.g(context.getApplicationContext());
                }
            });
        } catch (Throwable e) {
            bv.e("Exception occurred in Mobclick.onRause(). ", e);
        }
    }

    public aa a() {
        return this.e;
    }

    public void a(Context context, String str, HashMap<String, Object> hashMap) {
        try {
            if (!this.h) {
                e(context);
            }
            this.f.a(str, (Map) hashMap);
        } catch (Throwable e) {
            bv.e(e);
        }
    }

    void a(Context context, String str) {
        if (!TextUtils.isEmpty(str)) {
            if (context == null) {
                bv.e("unexpected null context in reportError");
                return;
            }
            try {
                if (!this.h) {
                    e(context);
                }
                this.g.a(new ah(str).a(false));
            } catch (Throwable e) {
                bv.e(e);
            }
        }
    }

    void a(Context context, Throwable th) {
        if (context != null && th != null) {
            try {
                if (!this.h) {
                    e(context);
                }
                this.g.a(new ah(th).a(false));
            } catch (Throwable e) {
                bv.e(e);
            }
        }
    }

    private void f(Context context) {
        this.e.c(context);
        if (this.b != null) {
            this.b.a();
        }
    }

    private void g(Context context) {
        this.e.d(context);
        this.d.a(context);
        if (this.b != null) {
            this.b.b();
        }
        this.g.b();
    }

    void c(Context context) {
        try {
            if (!this.h) {
                e(context);
            }
            this.g.a();
        } catch (Throwable e) {
            bv.e(e);
        }
    }

    public void a(Context context, String str, String str2, long j, int i) {
        try {
            if (!this.h) {
                e(context);
            }
            this.f.a(str, str2, j, i);
        } catch (Throwable e) {
            bv.e(e);
        }
    }

    void a(Context context, String str, Map<String, Object> map, long j) {
        try {
            if (!this.h) {
                e(context);
            }
            this.f.a(str, (Map) map, j);
        } catch (Throwable e) {
            bv.e(e);
        }
    }

    void a(Context context, final String str, final String str2) {
        try {
            if (!this.h) {
                e(context);
            }
            f.a(new g(this) {
                final /* synthetic */ d c;

                public void a() {
                    this.c.f.a(str, str2);
                }
            });
        } catch (Throwable e) {
            bv.e(e);
        }
    }

    void b(Context context, final String str, final String str2) {
        try {
            f.a(new g(this) {
                final /* synthetic */ d c;

                public void a() {
                    this.c.f.b(str, str2);
                }
            });
        } catch (Throwable e) {
            bv.e(e);
        }
    }

    void a(Context context, final String str, final HashMap<String, Object> hashMap, final String
            str2) {
        try {
            if (!this.h) {
                e(context);
            }
            f.a(new g(this) {
                final /* synthetic */ d d;

                public void a() {
                    this.d.f.a(str, hashMap, str2);
                }
            });
        } catch (Throwable e) {
            bv.e(e);
        }
    }

    void c(Context context, final String str, final String str2) {
        try {
            f.a(new g(this) {
                final /* synthetic */ d c;

                public void a() {
                    this.c.f.c(str, str2);
                }
            });
        } catch (Throwable e) {
            bv.e(e);
        }
    }

    void d(Context context) {
        try {
            this.d.a();
            g(context);
            y.a(context).edit().commit();
            f.a();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void a(Throwable th) {
        try {
            this.d.a();
            if (this.a != null) {
                if (!(th == null || this.g == null)) {
                    this.g.b(new ah(th));
                }
                g(this.a);
                y.a(this.a).edit().commit();
            }
            f.a();
        } catch (Throwable e) {
            bv.e("Exception in onAppCrash", e);
        }
    }

    void b(final String str, final String str2) {
        try {
            f.a(new g(this) {
                final /* synthetic */ d c;

                public void a() {
                    String[] a = e.a(this.c.a);
                    if (a == null || !str.equals(a[0]) || !str2.equals(a[1])) {
                        boolean e = this.c.a().e(this.c.a);
                        m.a(this.c.a).c();
                        if (e) {
                            this.c.a().f(this.c.a);
                        }
                        e.a(this.c.a, str, str2);
                    }
                }
            });
        } catch (Throwable e) {
            bv.e(" Excepthon  in  onProfileSignIn", e);
        }
    }

    void b() {
        try {
            f.a(new g(this) {
                final /* synthetic */ d a;

                {
                    this.a = r1;
                }

                public void a() {
                    String[] a = e.a(this.a.a);
                    if (a != null && !TextUtils.isEmpty(a[0]) && !TextUtils.isEmpty(a[1])) {
                        boolean e = this.a.a().e(this.a.a);
                        m.a(this.a.a).c();
                        if (e) {
                            this.a.a().f(this.a.a);
                        }
                        e.b(this.a.a);
                    }
                }
            });
        } catch (Throwable e) {
            bv.e(" Excepthon  in  onProfileSignOff", e);
        }
    }
}
