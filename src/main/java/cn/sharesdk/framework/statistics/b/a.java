package cn.sharesdk.framework.statistics.b;

import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

public class a extends c {
    private static int c;
    private static long d;
    public int a;
    public String b;

    protected String a() {
        return "[API]";
    }

    protected void a(long j) {
        d = j;
    }

    protected int b() {
        return BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT;
    }

    protected int c() {
        return 50;
    }

    protected long d() {
        return (long) c;
    }

    protected long e() {
        return d;
    }

    protected void f() {
        c++;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(super.toString());
        stringBuilder.append('|').append(this.a);
        stringBuilder.append('|').append(this.b);
        return stringBuilder.toString();
    }
}
