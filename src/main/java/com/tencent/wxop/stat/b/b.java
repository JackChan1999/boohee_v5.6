package com.tencent.wxop.stat.b;

import android.util.Log;

import com.tencent.wxop.stat.c;
import com.umeng.socialize.common.SocializeConstants;

public final class b {
    private String  a  = "default";
    private boolean ch = true;
    private int     cp = 2;

    public b(String str) {
        this.a = str;
    }

    private String c() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace == null) {
            return null;
        }
        for (StackTraceElement stackTraceElement : stackTrace) {
            if (!stackTraceElement.isNativeMethod() && !stackTraceElement.getClassName().equals
                    (Thread.class.getName()) && !stackTraceElement.getClassName().equals(getClass
                    ().getName())) {
                return "[" + Thread.currentThread().getName() + SocializeConstants.OP_OPEN_PAREN
                        + Thread.currentThread().getId() + "): " + stackTraceElement.getFileName
                        () + ":" + stackTraceElement.getLineNumber() + "]";
            }
        }
        return null;
    }

    public final void a(Throwable th) {
        if (this.cp <= 6) {
            Log.e(this.a, "", th);
            c.F();
        }
    }

    public final void ap() {
        this.ch = false;
    }

    public final void b(Object obj) {
        if (this.ch && this.cp <= 4) {
            String c = c();
            Log.i(this.a, c == null ? obj.toString() : c + " - " + obj);
            c.F();
        }
    }

    public final void b(Throwable th) {
        if (this.ch) {
            a(th);
        }
    }

    public final void c(Object obj) {
        if (this.ch) {
            warn(obj);
        }
    }

    public final void d(Object obj) {
        if (this.ch) {
            error(obj);
        }
    }

    public final void debug(Object obj) {
        if (this.cp <= 3) {
            String c = c();
            Log.d(this.a, c == null ? obj.toString() : c + " - " + obj);
            c.F();
        }
    }

    public final void e(Object obj) {
        if (this.ch) {
            debug(obj);
        }
    }

    public final void error(Object obj) {
        if (this.cp <= 6) {
            String c = c();
            Log.e(this.a, c == null ? obj.toString() : c + " - " + obj);
            c.F();
        }
    }

    public final void warn(Object obj) {
        if (this.cp <= 5) {
            String c = c();
            Log.w(this.a, c == null ? obj.toString() : c + " - " + obj);
            c.F();
        }
    }
}
