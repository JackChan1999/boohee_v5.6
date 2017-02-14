package cn.sharesdk.framework.statistics.b;

import android.content.Context;
import android.text.TextUtils;
import cn.sharesdk.framework.statistics.a.c;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

public class g extends c {
    private static int a;
    private static long b;

    protected String a() {
        return "[RUN]";
    }

    protected void a(long j) {
        b = j;
    }

    public boolean a(Context context) {
        c a = c.a(context);
        a = a.c("insertRunEventCount");
        b = a.b("lastInsertRunEventTime");
        return super.a(context);
    }

    protected int b() {
        return BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT;
    }

    public void b(Context context) {
        super.b(context);
        c a = c.a(context);
        a.a("lastInsertRunEventTime", Long.valueOf(b));
        a.a("insertRunEventCount", a);
    }

    protected int c() {
        return 5;
    }

    protected long d() {
        return (long) a;
    }

    protected long e() {
        return b;
    }

    protected void f() {
        a++;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(super.toString());
        stringBuilder.append('|');
        if (!TextUtils.isEmpty(this.m)) {
            stringBuilder.append(this.m);
        }
        return stringBuilder.toString();
    }
}
