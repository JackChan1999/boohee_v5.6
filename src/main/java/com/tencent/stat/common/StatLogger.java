package com.tencent.stat.common;

import android.util.Log;

import com.umeng.socialize.common.SocializeConstants;

public final class StatLogger {
    private String  a = "default";
    private boolean b = true;
    private int     c = 2;

    public StatLogger(String str) {
        this.a = str;
    }

    private String a() {
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

    public void d(Object obj) {
        if (isDebugEnable()) {
            debug(obj);
        }
    }

    public void debug(Object obj) {
        if (this.c <= 3) {
            String a = a();
            Log.d(this.a, a == null ? obj.toString() : a + " - " + obj);
        }
    }

    public void e(Exception exception) {
        if (isDebugEnable()) {
            error(exception);
        }
    }

    public void e(Object obj) {
        if (isDebugEnable()) {
            error(obj);
        }
    }

    public void error(Exception exception) {
        if (this.c <= 6) {
            StringBuffer stringBuffer = new StringBuffer();
            String a = a();
            StackTraceElement[] stackTrace = exception.getStackTrace();
            if (a != null) {
                stringBuffer.append(a + " - " + exception + "\r\n");
            } else {
                stringBuffer.append(exception + "\r\n");
            }
            if (stackTrace != null && stackTrace.length > 0) {
                for (StackTraceElement stackTraceElement : stackTrace) {
                    if (stackTraceElement != null) {
                        stringBuffer.append("[ " + stackTraceElement.getFileName() + ":" +
                                stackTraceElement.getLineNumber() + " ]\r\n");
                    }
                }
            }
            Log.e(this.a, stringBuffer.toString());
        }
    }

    public void error(Object obj) {
        if (this.c <= 6) {
            String a = a();
            Log.e(this.a, a == null ? obj.toString() : a + " - " + obj);
        }
    }

    public int getLogLevel() {
        return this.c;
    }

    public void i(Object obj) {
        if (isDebugEnable()) {
            info(obj);
        }
    }

    public void info(Object obj) {
        if (this.c <= 4) {
            String a = a();
            Log.i(this.a, a == null ? obj.toString() : a + " - " + obj);
        }
    }

    public boolean isDebugEnable() {
        return this.b;
    }

    public void setDebugEnable(boolean z) {
        this.b = z;
    }

    public void setLogLevel(int i) {
        this.c = i;
    }

    public void setTag(String str) {
        this.a = str;
    }

    public void v(Object obj) {
        if (isDebugEnable()) {
            verbose(obj);
        }
    }

    public void verbose(Object obj) {
        if (this.c <= 2) {
            String a = a();
            Log.v(this.a, a == null ? obj.toString() : a + " - " + obj);
        }
    }

    public void w(Object obj) {
        if (isDebugEnable()) {
            warn(obj);
        }
    }

    public void warn(Object obj) {
        if (this.c <= 5) {
            String a = a();
            Log.w(this.a, a == null ? obj.toString() : a + " - " + obj);
        }
    }
}
