package com.xiaomi.stats;

import com.xiaomi.smack.p;

import java.net.UnknownHostException;

final class c {

    static class a {
        com.xiaomi.push.thrift.a a;
        String                   b;

        a() {
        }
    }

    static a a(Exception exception) {
        Throwable a;
        e(exception);
        if ((exception instanceof p) && ((p) exception).a() != null) {
            a = ((p) exception).a();
        }
        a aVar = new a();
        String message = a.getMessage();
        if (a.getCause() != null) {
            message = a.getCause().getMessage();
        }
        message = a.getClass().getSimpleName() + ":" + message;
        int a2 = com.xiaomi.smack.c.a(a);
        if (a2 != 0) {
            aVar.a = com.xiaomi.push.thrift.a.a(a2 + com.xiaomi.push.thrift.a
                    .GSLB_REQUEST_SUCCESS.a());
        }
        if (aVar.a == null) {
            aVar.a = com.xiaomi.push.thrift.a.GSLB_TCP_ERR_OTHER;
        }
        if (aVar.a == com.xiaomi.push.thrift.a.GSLB_TCP_ERR_OTHER) {
            aVar.b = message;
        }
        return aVar;
    }

    static a b(Exception exception) {
        Throwable a;
        e(exception);
        if ((exception instanceof p) && ((p) exception).a() != null) {
            a = ((p) exception).a();
        }
        a aVar = new a();
        String message = a.getMessage();
        if (a.getCause() != null) {
            message = a.getCause().getMessage();
        }
        int a2 = com.xiaomi.smack.c.a(a);
        message = a.getClass().getSimpleName() + ":" + message;
        if (a2 != 0) {
            aVar.a = com.xiaomi.push.thrift.a.a(a2 + com.xiaomi.push.thrift.a.CONN_SUCCESS.a());
            if (aVar.a == com.xiaomi.push.thrift.a.CONN_BOSH_ERR) {
                Throwable cause = a.getCause();
                if (cause != null && (cause instanceof UnknownHostException)) {
                    aVar.a = com.xiaomi.push.thrift.a.CONN_BOSH_UNKNOWNHOST;
                }
            }
        } else {
            aVar.a = com.xiaomi.push.thrift.a.CONN_XMPP_ERR;
        }
        if (aVar.a == com.xiaomi.push.thrift.a.CONN_TCP_ERR_OTHER || aVar.a == com.xiaomi.push
                .thrift.a.CONN_XMPP_ERR || aVar.a == com.xiaomi.push.thrift.a.CONN_BOSH_ERR) {
            aVar.b = message;
        }
        return aVar;
    }

    static a c(Exception exception) {
        Throwable a;
        e(exception);
        if ((exception instanceof p) && ((p) exception).a() != null) {
            a = ((p) exception).a();
        }
        a aVar = new a();
        String message = a.getMessage();
        if (a.getCause() != null) {
            message = a.getCause().getMessage();
        }
        int a2 = com.xiaomi.smack.c.a(a);
        String str = a.getClass().getSimpleName() + ":" + message;
        switch (a2) {
            case 105:
                aVar.a = com.xiaomi.push.thrift.a.BIND_TCP_READ_TIMEOUT;
                break;
            case 109:
                aVar.a = com.xiaomi.push.thrift.a.BIND_TCP_CONNRESET;
                break;
            case 110:
                aVar.a = com.xiaomi.push.thrift.a.BIND_TCP_BROKEN_PIPE;
                break;
            case 199:
                aVar.a = com.xiaomi.push.thrift.a.BIND_TCP_ERR;
                break;
            case 499:
                aVar.a = com.xiaomi.push.thrift.a.BIND_BOSH_ERR;
                if (message.startsWith("Terminal binding condition encountered: item-not-found")) {
                    aVar.a = com.xiaomi.push.thrift.a.BIND_BOSH_ITEM_NOT_FOUND;
                    break;
                }
                break;
            default:
                aVar.a = com.xiaomi.push.thrift.a.BIND_XMPP_ERR;
                break;
        }
        if (aVar.a == com.xiaomi.push.thrift.a.BIND_TCP_ERR || aVar.a == com.xiaomi.push.thrift.a
                .BIND_XMPP_ERR || aVar.a == com.xiaomi.push.thrift.a.BIND_BOSH_ERR) {
            aVar.b = str;
        }
        return aVar;
    }

    static a d(Exception exception) {
        Throwable a;
        e(exception);
        if ((exception instanceof p) && ((p) exception).a() != null) {
            a = ((p) exception).a();
        }
        a aVar = new a();
        String message = a.getMessage();
        int a2 = com.xiaomi.smack.c.a(a);
        String str = a.getClass().getSimpleName() + ":" + message;
        switch (a2) {
            case 105:
                aVar.a = com.xiaomi.push.thrift.a.CHANNEL_TCP_READTIMEOUT;
                break;
            case 109:
                aVar.a = com.xiaomi.push.thrift.a.CHANNEL_TCP_CONNRESET;
                break;
            case 110:
                aVar.a = com.xiaomi.push.thrift.a.CHANNEL_TCP_BROKEN_PIPE;
                break;
            case 199:
                aVar.a = com.xiaomi.push.thrift.a.CHANNEL_TCP_ERR;
                break;
            case 499:
                aVar.a = com.xiaomi.push.thrift.a.CHANNEL_BOSH_EXCEPTION;
                if (message.startsWith("Terminal binding condition encountered: item-not-found")) {
                    aVar.a = com.xiaomi.push.thrift.a.CHANNEL_BOSH_ITEMNOTFIND;
                    break;
                }
                break;
            default:
                aVar.a = com.xiaomi.push.thrift.a.CHANNEL_XMPPEXCEPTION;
                break;
        }
        if (aVar.a == com.xiaomi.push.thrift.a.CHANNEL_TCP_ERR || aVar.a == com.xiaomi.push
                .thrift.a.CHANNEL_XMPPEXCEPTION || aVar.a == com.xiaomi.push.thrift.a
                .CHANNEL_BOSH_EXCEPTION) {
            aVar.b = str;
        }
        return aVar;
    }

    private static void e(Exception exception) {
        if (exception == null) {
            throw new NullPointerException();
        }
    }
}
