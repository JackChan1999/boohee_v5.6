package com.zxinsight.common.util;

import android.os.Process;
import android.text.TextUtils;

import com.zxinsight.a;
import com.zxinsight.analytics.domain.trackEvent.ErrorEvent;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

public class b implements UncaughtExceptionHandler {
    private static b                        a;
    private        UncaughtExceptionHandler b;

    private b() {
    }

    public static synchronized b a() {
        b bVar;
        synchronized (b.class) {
            if (a != null) {
                bVar = a;
            } else {
                a = new b();
                bVar = a;
            }
        }
        return bVar;
    }

    public void b() {
        this.b = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public void uncaughtException(Thread thread, Throwable th) {
        a(th);
        if (this.b == null || this.b == Thread.getDefaultUncaughtExceptionHandler()) {
            th.printStackTrace();
            Process.killProcess(Process.myPid());
            System.exit(0);
            return;
        }
        th.printStackTrace();
        this.b.uncaughtException(thread, th);
    }

    public boolean a(Throwable th) {
        if (th != null) {
            String b = b(th);
            String localizedMessage = th.getLocalizedMessage();
            if (TextUtils.isEmpty(localizedMessage)) {
                localizedMessage = th.toString();
            }
            a(localizedMessage, b);
            a.a().d();
        }
        return true;
    }

    private void a(String str, String str2) {
        ErrorEvent errorEvent = new ErrorEvent();
        errorEvent.l = str;
        errorEvent.ts = str2;
        errorEvent.a = "e";
        errorEvent.st = o.b();
        errorEvent.save();
    }

    private String b(Throwable th) {
        Writer stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        th.printStackTrace(printWriter);
        printWriter.close();
        return stringWriter.toString();
    }
}
