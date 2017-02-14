package com.xiaomi.mipush.sdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.text.TextUtils;

import com.boohee.utility.TimeLinePatterns;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.channel.commonutils.string.d;
import com.xiaomi.push.service.s;
import com.xiaomi.push.service.y;
import com.xiaomi.xmpush.thrift.a;
import com.xiaomi.xmpush.thrift.c;
import com.xiaomi.xmpush.thrift.e;
import com.xiaomi.xmpush.thrift.g;
import com.xiaomi.xmpush.thrift.h;
import com.xiaomi.xmpush.thrift.i;
import com.xiaomi.xmpush.thrift.k;
import com.xiaomi.xmpush.thrift.n;
import com.xiaomi.xmpush.thrift.p;
import com.xiaomi.xmpush.thrift.r;
import com.xiaomi.xmpush.thrift.t;
import com.xiaomi.xmpush.thrift.u;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TimeZone;

public class f {
    private static f a = null;
    private static Queue<String> c;
    private static Object d = new Object();
    private Context b;

    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] a = new int[a.values().length];

        static {
            try {
                a[a.e.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                a[a.a.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                a[a.b.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                a[a.c.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                a[a.d.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                a[a.j.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                a[a.i.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
        }
    }

    private f(Context context) {
        this.b = context.getApplicationContext();
        if (this.b == null) {
            this.b = context;
        }
    }

    public static Intent a(Context context, String str, Map<String, String> map) {
        Intent launchIntentForPackage;
        URISyntaxException e;
        MalformedURLException malformedURLException;
        if (map == null || !map.containsKey("notify_effect")) {
            return null;
        }
        String str2 = (String) map.get("notify_effect");
        if (y.a.equals(str2)) {
            try {
                launchIntentForPackage = context.getPackageManager().getLaunchIntentForPackage(str);
            } catch (Exception e2) {
                b.d("Cause: " + e2.getMessage());
                launchIntentForPackage = null;
            }
        } else {
            Intent intent;
            if (y.b.equals(str2)) {
                if (map.containsKey("intent_uri")) {
                    str2 = (String) map.get("intent_uri");
                    if (str2 != null) {
                        try {
                            launchIntentForPackage = Intent.parseUri(str2, 1);
                            try {
                                launchIntentForPackage.setPackage(str);
                            } catch (URISyntaxException e3) {
                                e = e3;
                                b.d("Cause: " + e.getMessage());
                                if (launchIntentForPackage == null) {
                                    return null;
                                }
                                launchIntentForPackage.addFlags(268435456);
                                try {
                                    return context.getPackageManager().resolveActivity
                                            (launchIntentForPackage, 65536) != null ? null :
                                            launchIntentForPackage;
                                } catch (Exception e22) {
                                    b.d("Cause: " + e22.getMessage());
                                    return null;
                                }
                            }
                        } catch (URISyntaxException e4) {
                            e = e4;
                            launchIntentForPackage = null;
                            b.d("Cause: " + e.getMessage());
                            if (launchIntentForPackage == null) {
                                return null;
                            }
                            launchIntentForPackage.addFlags(268435456);
                            if (context.getPackageManager().resolveActivity
                                    (launchIntentForPackage, 65536) != null) {
                            }
                        }
                    }
                    launchIntentForPackage = null;
                } else if (map.containsKey("class_name")) {
                    str2 = (String) map.get("class_name");
                    intent = new Intent();
                    intent.setComponent(new ComponentName(str, str2));
                    try {
                        if (map.containsKey("intent_flag")) {
                            intent.setFlags(Integer.parseInt((String) map.get("intent_flag")));
                        }
                    } catch (NumberFormatException e5) {
                        b.d("Cause by intent_flag: " + e5.getMessage());
                    }
                    launchIntentForPackage = intent;
                }
            } else if (y.c.equals(str2)) {
                str2 = (String) map.get("web_uri");
                if (str2 != null) {
                    str2 = str2.trim();
                    String str3 = (str2.startsWith(TimeLinePatterns.WEB_SCHEME) || str2
                            .startsWith("https://")) ? str2 : TimeLinePatterns.WEB_SCHEME + str2;
                    try {
                        str2 = new URL(str3).getProtocol();
                        if ("http".equals(str2) || com.alipay.sdk.cons.b.a.equals(str2)) {
                            launchIntentForPackage = new Intent("android.intent.action.VIEW");
                            try {
                                launchIntentForPackage.setData(Uri.parse(str3));
                            } catch (MalformedURLException e6) {
                                MalformedURLException malformedURLException2 = e6;
                                intent = launchIntentForPackage;
                                malformedURLException = malformedURLException2;
                                b.d("Cause: " + malformedURLException.getMessage());
                                launchIntentForPackage = intent;
                                if (launchIntentForPackage == null) {
                                    return null;
                                }
                                launchIntentForPackage.addFlags(268435456);
                                if (context.getPackageManager().resolveActivity
                                        (launchIntentForPackage, 65536) != null) {
                                }
                            }
                        }
                        launchIntentForPackage = null;
                    } catch (MalformedURLException e7) {
                        malformedURLException = e7;
                        intent = null;
                        b.d("Cause: " + malformedURLException.getMessage());
                        launchIntentForPackage = intent;
                        if (launchIntentForPackage == null) {
                            return null;
                        }
                        launchIntentForPackage.addFlags(268435456);
                        if (context.getPackageManager().resolveActivity(launchIntentForPackage,
                                65536) != null) {
                        }
                    }
                }
            }
            launchIntentForPackage = null;
        }
        if (launchIntentForPackage == null) {
            return null;
        }
        launchIntentForPackage.addFlags(268435456);
        if (context.getPackageManager().resolveActivity(launchIntentForPackage, 65536) != null) {
        }
    }

    private PushMessageHandler.a a(h hVar, boolean z, byte[] bArr) {
        PushMessageHandler.a aVar = null;
        try {
            org.apache.thrift.b a = e.a(this.b, hVar);
            if (a == null) {
                b.d("receiving an un-recognized message. " + hVar.a);
                return null;
            }
            b.c("receive a message." + a);
            a a2 = hVar.a();
            b.a("processing a message, action=" + a2);
            List list;
            switch (AnonymousClass1.a[a2.ordinal()]) {
                case 1:
                    if (!a.a(this.b).l() || z) {
                        n nVar = (n) a;
                        com.xiaomi.xmpush.thrift.b l = nVar.l();
                        if (l == null) {
                            b.d("receive an empty message without push content, drop it");
                            return null;
                        }
                        String b;
                        if (z) {
                            if (s.b(hVar)) {
                                MiPushClient.reportIgnoreRegMessageClicked(this.b, l.b(), hVar.m
                                        (), hVar.f, l.d());
                            } else {
                                MiPushClient.reportMessageClicked(this.b, l.b(), hVar.m(), l.d());
                            }
                        }
                        if (!z) {
                            if (!TextUtils.isEmpty(nVar.j()) && MiPushClient.aliasSetTime(this.b,
                                    nVar.j()) < 0) {
                                MiPushClient.addAlias(this.b, nVar.j());
                            } else if (!TextUtils.isEmpty(nVar.h()) && MiPushClient
                                    .topicSubscribedTime(this.b, nVar.h()) < 0) {
                                MiPushClient.addTopic(this.b, nVar.h());
                            }
                        }
                        CharSequence charSequence = (hVar.h == null || hVar.h.s() == null) ? null
                                : (String) hVar.h.j.get("jobkey");
                        if (TextUtils.isEmpty(charSequence)) {
                            b = l.b();
                        } else {
                            CharSequence charSequence2 = charSequence;
                        }
                        if (z || !a(this.b, b)) {
                            Serializable generateMessage = PushMessageHelper.generateMessage
                                    (nVar, hVar.m(), z);
                            if (generateMessage.getPassThrough() == 0 && !z && s.a
                                    (generateMessage.getExtra())) {
                                s.a(this.b, hVar, bArr);
                                return null;
                            }
                            b.a("receive a message, msgid=" + l.b() + ", jobkey=" + b);
                            if (z && generateMessage.getExtra() != null && generateMessage
                                    .getExtra().containsKey("notify_effect")) {
                                Map extra = generateMessage.getExtra();
                                String str = (String) extra.get("notify_effect");
                                if (s.b(hVar)) {
                                    Intent a3 = a(this.b, hVar.f, extra);
                                    if (a3 == null) {
                                        b.a("Getting Intent fail from ignore reg message. ");
                                        return null;
                                    }
                                    Object f = l.f();
                                    if (!TextUtils.isEmpty(f)) {
                                        a3.putExtra("payload", f);
                                    }
                                    this.b.startActivity(a3);
                                    return null;
                                }
                                Intent a4 = a(this.b, this.b.getPackageName(), extra);
                                if (a4 == null) {
                                    return null;
                                }
                                if (!str.equals(y.c)) {
                                    a4.putExtra(PushMessageHelper.KEY_MESSAGE, generateMessage);
                                }
                                this.b.startActivity(a4);
                                return null;
                            }
                            Serializable serializable = generateMessage;
                        } else {
                            b.a("drop a duplicate message, key=" + b);
                        }
                        if (hVar.m() != null || z) {
                            return aVar;
                        }
                        a(nVar, hVar.m());
                        return aVar;
                    }
                    b.a("receive a message in pause state. drop it");
                    return null;
                case 2:
                    k kVar = (k) a;
                    if (kVar.f == 0) {
                        a.a(this.b).b(kVar.h, kVar.i);
                    }
                    if (TextUtils.isEmpty(kVar.h)) {
                        list = null;
                    } else {
                        list = new ArrayList();
                        list.add(kVar.h);
                    }
                    aVar = PushMessageHelper.generateCommandMessage(MiPushClient
                            .COMMAND_REGISTER, list, kVar.f, kVar.g, null);
                    g.a(this.b).d();
                    return aVar;
                case 3:
                    if (((r) a).f == 0) {
                        a.a(this.b).h();
                        MiPushClient.clearExtras(this.b);
                    }
                    PushMessageHandler.a();
                    return null;
                case 4:
                    p pVar = (p) a;
                    if (pVar.f == 0) {
                        MiPushClient.addTopic(this.b, pVar.h());
                    }
                    if (TextUtils.isEmpty(pVar.h())) {
                        list = null;
                    } else {
                        list = new ArrayList();
                        list.add(pVar.h());
                    }
                    return PushMessageHelper.generateCommandMessage(MiPushClient
                            .COMMAND_SUBSCRIBE_TOPIC, list, pVar.f, pVar.g, pVar.k());
                case 5:
                    t tVar = (t) a;
                    if (tVar.f == 0) {
                        MiPushClient.removeTopic(this.b, tVar.h());
                    }
                    if (TextUtils.isEmpty(tVar.h())) {
                        list = null;
                    } else {
                        list = new ArrayList();
                        list.add(tVar.h());
                    }
                    return PushMessageHelper.generateCommandMessage(MiPushClient
                            .COMMAND_UNSUBSCRIBE_TOPIC, list, tVar.f, tVar.g, tVar.k());
                case 6:
                    g gVar = (g) a;
                    Object e = gVar.e();
                    List k = gVar.k();
                    if (gVar.g == 0) {
                        if (TextUtils.equals(e, MiPushClient.COMMAND_SET_ACCEPT_TIME) && k !=
                                null && k.size() > 1) {
                            MiPushClient.addAcceptTime(this.b, (String) k.get(0), (String) k.get
                                    (1));
                            if ("00:00".equals(k.get(0)) && "00:00".equals(k.get(1))) {
                                a.a(this.b).a(true);
                            } else {
                                a.a(this.b).a(false);
                            }
                            list = a(TimeZone.getTimeZone("GMT+08"), TimeZone.getDefault(), k);
                            return PushMessageHelper.generateCommandMessage(e, list, gVar.g, gVar
                                    .h, gVar.m());
                        } else if (TextUtils.equals(e, MiPushClient.COMMAND_SET_ALIAS) && k !=
                                null && k.size() > 0) {
                            MiPushClient.addAlias(this.b, (String) k.get(0));
                            list = k;
                            return PushMessageHelper.generateCommandMessage(e, list, gVar.g, gVar
                                    .h, gVar.m());
                        } else if (TextUtils.equals(e, MiPushClient.COMMAND_UNSET_ALIAS) && k !=
                                null && k.size() > 0) {
                            MiPushClient.removeAlias(this.b, (String) k.get(0));
                            list = k;
                            return PushMessageHelper.generateCommandMessage(e, list, gVar.g, gVar
                                    .h, gVar.m());
                        } else if (TextUtils.equals(e, MiPushClient.COMMAND_SET_ACCOUNT) && k !=
                                null && k.size() > 0) {
                            MiPushClient.addAccount(this.b, (String) k.get(0));
                            list = k;
                            return PushMessageHelper.generateCommandMessage(e, list, gVar.g, gVar
                                    .h, gVar.m());
                        } else if (TextUtils.equals(e, MiPushClient.COMMAND_UNSET_ACCOUNT) && k
                                != null && k.size() > 0) {
                            MiPushClient.removeAccount(this.b, (String) k.get(0));
                        }
                    }
                    list = k;
                    return PushMessageHelper.generateCommandMessage(e, list, gVar.g, gVar.h, gVar
                            .m());
                case 7:
                    i iVar = (i) a;
                    if ("registration id expired".equalsIgnoreCase(iVar.e)) {
                        MiPushClient.reInitialize(this.b);
                        return null;
                    } else if (!"client_info_update_ok".equalsIgnoreCase(iVar.e) || iVar.h() ==
                            null || !iVar.h().containsKey("app_version")) {
                        return null;
                    } else {
                        a.a(this.b).a((String) iVar.h().get("app_version"));
                        return null;
                    }
                default:
                    return null;
            }
        } catch (Throwable e2) {
            b.a(e2);
            b.d("receive a message which action string is not valid. is the reg expired?");
            return null;
        }
    }

    private PushMessageHandler.a a(h hVar, byte[] bArr) {
        String str = null;
        try {
            org.apache.thrift.b a = e.a(this.b, hVar);
            if (a == null) {
                b.d("message arrived: receiving an un-recognized message. " + hVar.a);
                return null;
            }
            b.c("message arrived: receive a message." + a);
            a a2 = hVar.a();
            b.a("message arrived: processing an arrived message, action=" + a2);
            switch (AnonymousClass1.a[a2.ordinal()]) {
                case 1:
                    n nVar = (n) a;
                    com.xiaomi.xmpush.thrift.b l = nVar.l();
                    if (l == null) {
                        b.d("message arrived: receive an empty message without push content, drop" +
                                " it");
                        return null;
                    }
                    if (!(hVar.h == null || hVar.h.s() == null)) {
                        str = (String) hVar.h.j.get("jobkey");
                    }
                    MiPushMessage generateMessage = PushMessageHelper.generateMessage(nVar, hVar
                            .m(), false);
                    generateMessage.setArrivedMessage(true);
                    b.a("message arrived: receive a message, msgid=" + l.b() + ", jobkey=" + str);
                    return generateMessage;
                default:
                    return null;
            }
        } catch (Throwable e) {
            b.a(e);
            b.d("message arrived: receive a message which action string is not valid. is the reg " +
                    "expired?");
            return null;
        }
    }

    public static f a(Context context) {
        if (a == null) {
            a = new f(context);
        }
        return a;
    }

    private void a(h hVar) {
        c m = hVar.m();
        org.apache.thrift.b eVar = new e();
        eVar.b(hVar.h());
        eVar.a(m.b());
        eVar.a(m.d());
        if (!TextUtils.isEmpty(m.f())) {
            eVar.c(m.f());
        }
        g.a(this.b).a(eVar, a.f, false, hVar.m());
    }

    private void a(n nVar, c cVar) {
        org.apache.thrift.b eVar = new e();
        eVar.b(nVar.e());
        eVar.a(nVar.c());
        eVar.a(nVar.l().h());
        if (!TextUtils.isEmpty(nVar.h())) {
            eVar.c(nVar.h());
        }
        if (!TextUtils.isEmpty(nVar.j())) {
            eVar.d(nVar.j());
        }
        g.a(this.b).a(eVar, a.f, cVar);
    }

    private static boolean a(Context context, String str) {
        boolean z = false;
        synchronized (d) {
            SharedPreferences j = a.a(context).j();
            if (c == null) {
                String[] split = j.getString("pref_msg_ids", "").split(",");
                c = new LinkedList();
                for (Object add : split) {
                    c.add(add);
                }
            }
            if (c.contains(str)) {
                z = true;
            } else {
                c.add(str);
                if (c.size() > 10) {
                    c.poll();
                }
                String a = d.a(c, ",");
                Editor edit = j.edit();
                edit.putString("pref_msg_ids", a);
                edit.commit();
            }
        }
        return z;
    }

    public PushMessageHandler.a a(Intent intent) {
        String action = intent.getAction();
        b.a("receive an intent from server, action=" + action);
        String stringExtra = intent.getStringExtra("mrt");
        if (stringExtra == null) {
            stringExtra = Long.toString(System.currentTimeMillis());
        }
        byte[] byteArrayExtra;
        if ("com.xiaomi.mipush.RECEIVE_MESSAGE".equals(action)) {
            byteArrayExtra = intent.getByteArrayExtra("mipush_payload");
            boolean booleanExtra = intent.getBooleanExtra("mipush_notified", false);
            if (byteArrayExtra == null) {
                b.d("receiving an empty message, drop");
                return null;
            }
            h hVar = new h();
            try {
                u.a(hVar, byteArrayExtra);
                a a = a.a(this.b);
                c m = hVar.m();
                if (!(hVar.a() != a.e || m == null || a.l() || booleanExtra)) {
                    if (m != null) {
                        hVar.m().a("mrt", stringExtra);
                        hVar.m().a("mat", Long.toString(System.currentTimeMillis()));
                    }
                    a(hVar);
                }
                if (hVar.a() == a.e && !hVar.c()) {
                    if (!s.b(hVar)) {
                        action = "drop an un-encrypted messages. %1$s, %2$s";
                        Object[] objArr = new Object[2];
                        objArr[0] = hVar.j();
                        objArr[1] = m != null ? m.b() : "";
                        b.a(String.format(action, objArr));
                        return null;
                    } else if (!(booleanExtra && m.s() != null && m.s().containsKey
                            ("notify_effect"))) {
                        b.a(String.format("drop an un-encrypted messages. %1$s, %2$s", new
                                Object[]{hVar.j(), m.b()}));
                        return null;
                    }
                }
                if (a.i() || hVar.a == a.a) {
                    if (!a.i() || !a.n()) {
                        return a(hVar, booleanExtra, byteArrayExtra);
                    }
                    if (hVar.a == a.b) {
                        a.h();
                        MiPushClient.clearExtras(this.b);
                        PushMessageHandler.a();
                    } else {
                        MiPushClient.unregisterPush(this.b);
                    }
                } else if (s.b(hVar)) {
                    return a(hVar, booleanExtra, byteArrayExtra);
                } else {
                    b.d("receive message without registration. need unregister or re-register!");
                }
            } catch (Throwable e) {
                b.a(e);
            } catch (Throwable e2) {
                b.a(e2);
            }
        } else if ("com.xiaomi.mipush.ERROR".equals(action)) {
            PushMessageHandler.a miPushCommandMessage = new MiPushCommandMessage();
            Object hVar2 = new h();
            try {
                byteArrayExtra = intent.getByteArrayExtra("mipush_payload");
                if (byteArrayExtra != null) {
                    u.a(hVar2, byteArrayExtra);
                }
            } catch (org.apache.thrift.f e3) {
            }
            miPushCommandMessage.setCommand(String.valueOf(hVar2.a()));
            miPushCommandMessage.setResultCode((long) intent.getIntExtra("mipush_error_code", 0));
            miPushCommandMessage.setReason(intent.getStringExtra("mipush_error_msg"));
            b.d("receive a error message. code = " + intent.getIntExtra("mipush_error_code", 0) +
                    ", msg= " + intent.getStringExtra("mipush_error_msg"));
            return miPushCommandMessage;
        } else if ("com.xiaomi.mipush.MESSAGE_ARRIVED".equals(action)) {
            byte[] byteArrayExtra2 = intent.getByteArrayExtra("mipush_payload");
            if (byteArrayExtra2 == null) {
                b.d("message arrived: receiving an empty message, drop");
                return null;
            }
            h hVar3 = new h();
            try {
                u.a(hVar3, byteArrayExtra2);
                a a2 = a.a(this.b);
                if (s.b(hVar3)) {
                    b.d("message arrived: receive ignore reg message, ignore!");
                } else if (!a2.i()) {
                    b.d("message arrived: receive message without registration. need unregister " +
                            "or re-register!");
                } else if (!a2.i() || !a2.n()) {
                    return a(hVar3, byteArrayExtra2);
                } else {
                    b.d("message arrived: app info is invalidated");
                }
            } catch (Throwable e22) {
                b.a(e22);
            } catch (Throwable e222) {
                b.a(e222);
            }
        }
        return null;
    }

    public List<String> a(TimeZone timeZone, TimeZone timeZone2, List<String> list) {
        if (timeZone.equals(timeZone2)) {
            return list;
        }
        long rawOffset = (long) (((timeZone.getRawOffset() - timeZone2.getRawOffset()) / 1000) /
                60);
        long parseLong = Long.parseLong(((String) list.get(0)).split(":")[0]);
        long parseLong2 = Long.parseLong(((String) list.get(0)).split(":")[1]);
        parseLong = ((((parseLong * 60) + parseLong2) - rawOffset) + 1440) % 1440;
        long parseLong3 = (((Long.parseLong(((String) list.get(1)).split(":")[1]) + (60 * Long
                .parseLong(((String) list.get(1)).split(":")[0]))) - rawOffset) + 1440) % 1440;
        List arrayList = new ArrayList();
        List list2 = arrayList;
        list2.add(String.format("%1$02d:%2$02d", new Object[]{Long.valueOf(parseLong / 60), Long
                .valueOf(parseLong % 60)}));
        list2 = arrayList;
        list2.add(String.format("%1$02d:%2$02d", new Object[]{Long.valueOf(parseLong3 / 60), Long
                .valueOf(parseLong3 % 60)}));
        return arrayList;
    }
}
