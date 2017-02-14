package com.xiaomi.smack;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class c {
    public static int a(Throwable th) {
        Throwable a = (!(th instanceof p) || ((p) th).a() == null) ? th : ((p) th).a();
        String message = a.getMessage();
        if (a.getCause() != null) {
            message = a.getCause().getMessage();
        }
        return a instanceof SocketTimeoutException ? 105 : a instanceof SocketException ? message
                .indexOf("Network is unreachable") != -1 ? 102 : message.indexOf("Connection " +
                "refused") != -1 ? 103 : message.indexOf("Connection timed out") != -1 ? 105 :
                message.endsWith("EACCES (Permission denied)") ? 101 : message.indexOf
                        ("Connection reset by peer") != -1 ? 109 : message.indexOf("Broken pipe")
                        != -1 ? 110 : message.indexOf("No route to host") != -1 ? 104 : message
                        .endsWith("EINVAL (Invalid argument)") ? 106 : 199 : a instanceof
                UnknownHostException ? 107 : th instanceof p ? 399 : 0;
    }
}
