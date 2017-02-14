package cn.sharesdk.framework.authorize;

import android.content.Intent;

public abstract class f {
    protected e a;
    protected int b;
    protected SSOListener c;

    public f(e eVar) {
        this.a = eVar;
        this.c = eVar.a().getSSOListener();
    }

    public abstract void a();

    public void a(int i) {
        this.b = i;
    }

    public void a(int i, int i2, Intent intent) {
    }

    protected void a(Intent intent) {
    }
}
