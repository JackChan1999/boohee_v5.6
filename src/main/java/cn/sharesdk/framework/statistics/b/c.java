package cn.sharesdk.framework.statistics.b;

import android.content.Context;

public abstract class c {
    public long e;
    public String f;
    public String g;
    public String h;
    public int i;
    public String j;
    public int k;
    public String l;
    public String m;

    protected abstract String a();

    protected abstract void a(long j);

    public boolean a(Context context) {
        int b = b();
        int c = c();
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - e() < ((long) b)) {
            return d() < ((long) c);
        } else {
            a(currentTimeMillis);
            return true;
        }
    }

    protected abstract int b();

    public void b(Context context) {
        f();
    }

    protected abstract int c();

    protected abstract long d();

    protected abstract long e();

    protected abstract void f();

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(a()).append(':');
        stringBuilder.append(this.e).append('|');
        stringBuilder.append(this.f).append('|');
        stringBuilder.append(this.g).append('|');
        stringBuilder.append(this.h).append('|');
        stringBuilder.append(this.i).append('|');
        stringBuilder.append(this.j).append('|');
        stringBuilder.append(this.k).append('|');
        stringBuilder.append(this.l);
        return stringBuilder.toString();
    }
}
