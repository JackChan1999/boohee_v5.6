package com.xiaomi.mipush.sdk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;

import com.alipay.security.mobile.module.deviceinfo.constant.DeviceInfoConstant;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.channel.commonutils.string.d;
import com.xiaomi.push.service.receivers.NetworkStatusReceiver;
import com.xiaomi.push.service.y;
import com.xiaomi.xmpush.thrift.c;
import com.xiaomi.xmpush.thrift.f;
import com.xiaomi.xmpush.thrift.i;
import com.xiaomi.xmpush.thrift.j;
import com.xiaomi.xmpush.thrift.o;
import com.xiaomi.xmpush.thrift.q;
import com.xiaomi.xmpush.thrift.s;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

public abstract class MiPushClient {
    public static final String  ACCEPT_TIME_SEPARATOR     = ",";
    public static final String  COMMAND_REGISTER          = "register";
    public static final String  COMMAND_SET_ACCEPT_TIME   = "accept-time";
    public static final String  COMMAND_SET_ACCOUNT       = "set-account";
    public static final String  COMMAND_SET_ALIAS         = "set-alias";
    public static final String  COMMAND_SUBSCRIBE_TOPIC   = "subscribe-topic";
    public static final String  COMMAND_UNSET_ACCOUNT     = "unset-account";
    public static final String  COMMAND_UNSET_ALIAS       = "unset-alias";
    public static final String  COMMAND_UNSUBSCRIBE_TOPIC = "unsubscibe-topic";
    private static      boolean awakeService              = true;
    private static Context sContext;
    private static long sCurMsgId = System.currentTimeMillis();

    @Deprecated
    public static abstract class MiPushClientCallback {
        private String category;

        protected String getCategory() {
            return this.category;
        }

        public void onCommandResult(String str, long j, String str2, List<String> list) {
        }

        public void onInitializeResult(long j, String str, String str2) {
        }

        public void onReceiveMessage(MiPushMessage miPushMessage) {
        }

        public void onReceiveMessage(String str, String str2, String str3, boolean z) {
        }

        public void onSubscribeResult(long j, String str, String str2) {
        }

        public void onUnsubscribeResult(long j, String str, String str2) {
        }

        protected void setCategory(String str) {
            this.category = str;
        }
    }

    public static class a extends Exception {
        private PackageItemInfo a;

        public a(String str, PackageItemInfo packageItemInfo) {
            super(str);
            this.a = packageItemInfo;
        }
    }

    private static boolean acceptTimeSet(Context context, String str, String str2) {
        return TextUtils.equals(context.getSharedPreferences("mipush_extra", 0).getString
                ("accept_time", ""), str + "," + str2);
    }

    public static long accountSetTime(Context context, String str) {
        return context.getSharedPreferences("mipush_extra", 0).getLong("account_" + str, -1);
    }

    static synchronized void addAcceptTime(Context context, String str, String str2) {
        synchronized (MiPushClient.class) {
            context.getSharedPreferences("mipush_extra", 0).edit().putString("accept_time", str +
                    "," + str2).commit();
        }
    }

    static synchronized void addAccount(Context context, String str) {
        synchronized (MiPushClient.class) {
            context.getSharedPreferences("mipush_extra", 0).edit().putLong("account_" + str,
                    System.currentTimeMillis()).commit();
        }
    }

    static synchronized void addAlias(Context context, String str) {
        synchronized (MiPushClient.class) {
            context.getSharedPreferences("mipush_extra", 0).edit().putLong("alias_" + str, System
                    .currentTimeMillis()).commit();
        }
    }

    private static void addPullNotificationTime(Context context) {
        context.getSharedPreferences("mipush_extra", 0).edit().putLong("last_pull_notification",
                System.currentTimeMillis()).commit();
    }

    private static void addRegRequestTime(Context context) {
        context.getSharedPreferences("mipush_extra", 0).edit().putLong("last_reg_request", System
                .currentTimeMillis()).commit();
    }

    static synchronized void addTopic(Context context, String str) {
        synchronized (MiPushClient.class) {
            context.getSharedPreferences("mipush_extra", 0).edit().putLong("topic_" + str, System
                    .currentTimeMillis()).commit();
        }
    }

    public static long aliasSetTime(Context context, String str) {
        return context.getSharedPreferences("mipush_extra", 0).getLong("alias_" + str, -1);
    }

    private static void awakePushServices(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("mipush_extra", 0);
        if (System.currentTimeMillis() - 600000 >= sharedPreferences.getLong("wake_up", 0)) {
            sharedPreferences.edit().putLong("wake_up", System.currentTimeMillis()).commit();
            new Thread(new d(context)).start();
        }
    }

    public static void checkManifest(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context
                    .getPackageName(), 4100);
            checkReceivers(context);
            checkServices(context, packageInfo);
            checkPermissions(context, packageInfo);
        } catch (Throwable e) {
            b.a(e);
        }
    }

    private static void checkNotNull(Object obj, String str) {
        if (obj == null) {
            throw new IllegalArgumentException("param " + str + " is not nullable");
        }
    }

    private static void checkPermissions(Context context, PackageInfo packageInfo) {
        int i;
        Set hashSet = new HashSet();
        hashSet.addAll(Arrays.asList(new String[]{"android.permission.INTERNET", "android" +
                ".permission.ACCESS_NETWORK_STATE", context.getPackageName() + ".permission" +
                ".MIPUSH_RECEIVE", "android.permission.ACCESS_WIFI_STATE", "android.permission" +
                ".READ_PHONE_STATE", "android.permission.GET_TASKS", "android.permission" +
                ".VIBRATE"}));
        if (packageInfo.permissions != null) {
            for (PermissionInfo permissionInfo : packageInfo.permissions) {
                if (r4.equals(permissionInfo.name)) {
                    i = 1;
                    break;
                }
            }
        }
        i = 0;
        if (i == 0) {
            throw new a(String.format("<permission android:name=\"%1$s\" /> is undefined.", new
                    Object[]{r4}), null);
        }
        if (packageInfo.requestedPermissions != null) {
            for (CharSequence charSequence : packageInfo.requestedPermissions) {
                if (!TextUtils.isEmpty(charSequence) && hashSet.contains(charSequence)) {
                    hashSet.remove(charSequence);
                    if (hashSet.isEmpty()) {
                        break;
                    }
                }
            }
        }
        if (!hashSet.isEmpty()) {
            throw new a(String.format("<use-permission android:name=\"%1$s\" /> is missing.", new
                    Object[]{hashSet.iterator().next()}), null);
        }
    }

    private static void checkReceivers(Context context) {
        boolean z;
        PackageManager packageManager = context.getPackageManager();
        String packageName = context.getPackageName();
        Intent intent = new Intent("android.net.conn.CONNECTIVITY_CHANGE");
        intent.setPackage(packageName);
        findAndCheckReceiverInfo(packageManager, intent, NetworkStatusReceiver.class, new
                Boolean[]{Boolean.valueOf(true), Boolean.valueOf(true)});
        intent = new Intent(y.o);
        intent.setPackage(packageName);
        try {
            findAndCheckReceiverInfo(packageManager, intent, Class.forName("com.xiaomi.push" +
                    ".service.receivers.PingReceiver"), new Boolean[]{Boolean.valueOf(true),
                    Boolean.valueOf(false)});
        } catch (Throwable e) {
            b.a(e);
        }
        intent = new Intent("com.xiaomi.mipush.RECEIVE_MESSAGE");
        intent.setPackage(packageName);
        boolean z2 = false;
        for (ResolveInfo resolveInfo : packageManager.queryBroadcastReceivers(intent, 16384)) {
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            if (activityInfo != null) {
                try {
                    if (!TextUtils.isEmpty(activityInfo.name) && PushMessageReceiver.class
                            .isAssignableFrom(Class.forName(activityInfo.name)) && activityInfo
                            .enabled) {
                        z = true;
                        if (z) {
                            break;
                        }
                        z2 = z;
                    }
                } catch (Throwable e2) {
                    b.a(e2);
                    z = z2;
                }
            }
            z = false;
            if (z) {
                break;
            }
            z2 = z;
        }
        z = z2;
        if (!z) {
            throw new a("Receiver: none of the subclasses of PushMessageReceiver is enabled or " +
                    "defined.", null);
        }
    }

    private static void checkServices(Context context, PackageInfo packageInfo) {
        Map hashMap = new HashMap();
        hashMap.put("com.xiaomi.push.service.XMPushService", new Boolean[]{Boolean.valueOf(true),
                Boolean.valueOf(false)});
        hashMap.put(PushMessageHandler.class.getCanonicalName(), new Boolean[]{Boolean.valueOf
                (true), Boolean.valueOf(true)});
        hashMap.put(MessageHandleService.class.getCanonicalName(), new Boolean[]{Boolean.valueOf
                (true), Boolean.valueOf(false)});
        if (packageInfo.services != null) {
            for (PackageItemInfo packageItemInfo : packageInfo.services) {
                if (!TextUtils.isEmpty(packageItemInfo.name) && hashMap.containsKey
                        (packageItemInfo.name)) {
                    Boolean[] boolArr = (Boolean[]) hashMap.remove(packageItemInfo.name);
                    if (boolArr[0].booleanValue() != packageItemInfo.enabled) {
                        throw new a(String.format("Wrong attribute: %n    <service " +
                                "android:name=\"%1$s\" .../> android:enabled should be %<b.", new
                                Object[]{packageItemInfo.name, boolArr[0]}), packageItemInfo);
                    } else if (boolArr[1].booleanValue() != packageItemInfo.exported) {
                        throw new a(String.format("Wrong attribute: %n    <service " +
                                "android:name=\"%1$s\" .../> android:exported should be %<b.",
                                new Object[]{packageItemInfo.name, boolArr[1]}), packageItemInfo);
                    } else if (hashMap.isEmpty()) {
                        break;
                    }
                }
            }
        }
        if (!hashMap.isEmpty()) {
            throw new a(String.format("<service android:name=\"%1$s\" /> is missing or disabled" +
                    ".", new Object[]{hashMap.keySet().iterator().next()}), null);
        }
    }

    protected static void clearExtras(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("mipush_extra", 0);
        long j = sharedPreferences.getLong("wake_up", 0);
        Editor edit = sharedPreferences.edit();
        edit.clear();
        if (j > 0) {
            edit.putLong("wake_up", j);
        }
        edit.commit();
    }

    public static void clearLocalNotificationType(Context context) {
        g.a(context).e();
    }

    public static void clearNotification(Context context) {
        g.a(context).a(-1);
    }

    public static void clearNotification(Context context, int i) {
        g.a(context).a(i);
    }

    private static void findAndCheckReceiverInfo(PackageManager packageManager, Intent intent,
                                                 Class<?> cls, Boolean[] boolArr) {
        int i;
        for (ResolveInfo resolveInfo : packageManager.queryBroadcastReceivers(intent, 16384)) {
            PackageItemInfo packageItemInfo = resolveInfo.activityInfo;
            if (packageItemInfo != null && cls.getCanonicalName().equals(packageItemInfo.name)) {
                if (boolArr[0].booleanValue() != packageItemInfo.enabled) {
                    throw new a(String.format("Wrong attribute: %n    <receiver " +
                            "android:name=\"%1$s\" .../> android:enabled should be %<b.", new
                            Object[]{packageItemInfo.name, boolArr[0]}), packageItemInfo);
                } else if (boolArr[1].booleanValue() != packageItemInfo.exported) {
                    throw new a(String.format("Wrong attribute: %n    <receiver " +
                            "android:name=\"%1$s\" .../> android:exported should be %<b.", new
                            Object[]{packageItemInfo.name, boolArr[1]}), packageItemInfo);
                } else {
                    i = 1;
                    if (i == 0) {
                        throw new a(String.format("<receiver android:name=\"%1$s\" /> is missing " +
                                "or disabled.", new Object[]{cls.getCanonicalName()}), null);
                    }
                }
            }
        }
        i = 0;
        if (i == 0) {
            throw new a(String.format("<receiver android:name=\"%1$s\" /> is missing or disabled" +
                    ".", new Object[]{cls.getCanonicalName()}), null);
        }
    }

    protected static synchronized String generatePacketID() {
        String str;
        synchronized (MiPushClient.class) {
            str = d.a(4) + sCurMsgId;
            sCurMsgId++;
        }
        return str;
    }

    public static List<String> getAllAlias(Context context) {
        List<String> arrayList = new ArrayList();
        for (String str : context.getSharedPreferences("mipush_extra", 0).getAll().keySet()) {
            if (str.startsWith("alias_")) {
                arrayList.add(str.substring("alias_".length()));
            }
        }
        return arrayList;
    }

    public static List<String> getAllTopic(Context context) {
        List<String> arrayList = new ArrayList();
        for (String str : context.getSharedPreferences("mipush_extra", 0).getAll().keySet()) {
            if (str.startsWith("topic_") && !str.contains("**ALL**")) {
                arrayList.add(str.substring("topic_".length()));
            }
        }
        return arrayList;
    }

    public static List<String> getAllUserAccount(Context context) {
        List<String> arrayList = new ArrayList();
        for (String str : context.getSharedPreferences("mipush_extra", 0).getAll().keySet()) {
            if (str.startsWith("account_")) {
                arrayList.add(str.substring("account_".length()));
            }
        }
        return arrayList;
    }

    public static String getRegId(Context context) {
        return a.a(context).i() ? a.a(context).e() : null;
    }

    @Deprecated
    public static void initialize(Context context, String str, String str2, MiPushClientCallback
            miPushClientCallback) {
        boolean z = false;
        checkNotNull(context, "context");
        checkNotNull(str, "appID");
        checkNotNull(str2, "appToken");
        try {
            sContext = context.getApplicationContext();
            if (sContext == null) {
                sContext = context;
            }
            if (miPushClientCallback != null) {
                PushMessageHandler.a(miPushClientCallback);
            }
            if (a.a(sContext).m() != Constants.a()) {
                z = true;
            }
            if (z || shouldSendRegRequest(sContext)) {
                if (z || !a.a(sContext).a(str, str2) || a.a(sContext).n()) {
                    String a = d.a(6);
                    a.a(sContext).h();
                    a.a(sContext).a(Constants.a());
                    a.a(sContext).a(str, str2, a);
                    clearExtras(sContext);
                    j jVar = new j();
                    jVar.a(generatePacketID());
                    jVar.b(str);
                    jVar.e(str2);
                    jVar.d(context.getPackageName());
                    jVar.f(a);
                    jVar.c(a.a(context, context.getPackageName()));
                    g.a(sContext).a(jVar, z);
                } else {
                    if (1 == PushMessageHelper.getPushMode(context)) {
                        checkNotNull(miPushClientCallback, com.alipay.sdk.authjs.a.c);
                        miPushClientCallback.onInitializeResult(0, null, a.a(context).e());
                    } else {
                        List arrayList = new ArrayList();
                        arrayList.add(a.a(context).e());
                        PushMessageHelper.sendCommandMessageBroadcast(sContext, PushMessageHelper
                                .generateCommandMessage(COMMAND_REGISTER, arrayList, 0, null,
                                        null));
                    }
                    g.a(context).a();
                    if (a.a(sContext).a()) {
                        org.apache.thrift.b iVar = new i();
                        iVar.b(a.a(context).c());
                        iVar.c("client_info_update");
                        iVar.a(generatePacketID());
                        iVar.h = new HashMap();
                        iVar.h.put("app_version", a.a(sContext, sContext.getPackageName()));
                        CharSequence g = a.a(sContext).g();
                        if (!TextUtils.isEmpty(g)) {
                            iVar.h.put("deviceid", g);
                        }
                        g.a(context).a(iVar, com.xiaomi.xmpush.thrift.a.Notification, false, null);
                    }
                    if (!com.xiaomi.channel.commonutils.android.a.a(sContext, "update_devId",
                            false)) {
                        updateIMEI();
                        com.xiaomi.channel.commonutils.android.a.b(sContext, "update_devId", true);
                    }
                    if (shouldUseMIUIPush(sContext) && shouldPullNotification(sContext)) {
                        org.apache.thrift.b iVar2 = new i();
                        iVar2.b(a.a(sContext).c());
                        iVar2.c("pull");
                        iVar2.a(generatePacketID());
                        iVar2.a(false);
                        g.a(sContext).a(iVar2, com.xiaomi.xmpush.thrift.a.Notification, false,
                                null, false);
                        addPullNotificationTime(sContext);
                    }
                }
                if (awakeService) {
                    awakePushServices(sContext);
                }
                addRegRequestTime(sContext);
                return;
            }
            g.a(context).a();
            b.a("Could not send  register message within 5s repeatly .");
        } catch (Throwable th) {
            b.a(th);
        }
    }

    public static void pausePush(Context context, String str) {
        setAcceptTime(context, 0, 0, 0, 0, str);
    }

    static void reInitialize(Context context) {
        if (a.a(context).i()) {
            String a = d.a(6);
            String c = a.a(context).c();
            String d = a.a(context).d();
            a.a(context).h();
            a.a(context).a(c, d, a);
            j jVar = new j();
            jVar.a(generatePacketID());
            jVar.b(c);
            jVar.e(d);
            jVar.f(a);
            jVar.d(context.getPackageName());
            jVar.c(a.a(context, context.getPackageName()));
            g.a(context).a(jVar, false);
        }
    }

    public static void registerPush(Context context, String str, String str2) {
        new Thread(new b(context, str, str2)).start();
    }

    static synchronized void removeAccount(Context context, String str) {
        synchronized (MiPushClient.class) {
            context.getSharedPreferences("mipush_extra", 0).edit().remove("account_" + str)
                    .commit();
        }
    }

    static synchronized void removeAlias(Context context, String str) {
        synchronized (MiPushClient.class) {
            context.getSharedPreferences("mipush_extra", 0).edit().remove("alias_" + str).commit();
        }
    }

    static synchronized void removeTopic(Context context, String str) {
        synchronized (MiPushClient.class) {
            context.getSharedPreferences("mipush_extra", 0).edit().remove("topic_" + str).commit();
        }
    }

    static void reportIgnoreRegMessageClicked(Context context, String str, c cVar, String str2,
                                              String str3) {
        org.apache.thrift.b iVar = new i();
        if (TextUtils.isEmpty(str3)) {
            b.d("do not report clicked message");
            return;
        }
        iVar.b(str3);
        iVar.c("bar:click");
        iVar.a(str);
        iVar.a(false);
        g.a(context).a(iVar, com.xiaomi.xmpush.thrift.a.Notification, false, true, cVar, true,
                str2, str3);
    }

    public static void reportMessageClicked(Context context, MiPushMessage miPushMessage) {
        c cVar = new c();
        cVar.a(miPushMessage.getMessageId());
        cVar.b(miPushMessage.getTopic());
        cVar.d(miPushMessage.getDescription());
        cVar.c(miPushMessage.getTitle());
        cVar.c(miPushMessage.getNotifyId());
        cVar.a(miPushMessage.getNotifyType());
        cVar.b(miPushMessage.getPassThrough());
        cVar.a(miPushMessage.getExtra());
        reportMessageClicked(context, miPushMessage.getMessageId(), cVar, null);
    }

    @Deprecated
    public static void reportMessageClicked(Context context, String str) {
        reportMessageClicked(context, str, null, null);
    }

    static void reportMessageClicked(Context context, String str, c cVar, String str2) {
        Object iVar = new i();
        if (!TextUtils.isEmpty(str2)) {
            iVar.b(str2);
        } else if (a.a(context).b()) {
            iVar.b(a.a(context).c());
        } else {
            b.d("do not report clicked message");
            return;
        }
        iVar.c("bar:click");
        iVar.a(str);
        iVar.a(false);
        g.a(context).a(iVar, com.xiaomi.xmpush.thrift.a.Notification, false, cVar);
    }

    public static void resumePush(Context context, String str) {
        setAcceptTime(context, 0, 0, 23, 59, str);
    }

    public static void setAcceptTime(Context context, int i, int i2, int i3, int i4, String str) {
        if (i < 0 || i >= 24 || i3 < 0 || i3 >= 24 || i2 < 0 || i2 >= 60 || i4 < 0 || i4 >= 60) {
            throw new IllegalArgumentException("the input parameter is not valid.");
        }
        long rawOffset = (long) (((TimeZone.getTimeZone("GMT+08").getRawOffset() - TimeZone
                .getDefault().getRawOffset()) / 1000) / 60);
        long j = ((((long) ((i * 60) + i2)) + rawOffset) + 1440) % 1440;
        rawOffset = ((rawOffset + ((long) ((i3 * 60) + i4))) + 1440) % 1440;
        ArrayList arrayList = new ArrayList();
        arrayList.add(String.format("%1$02d:%2$02d", new Object[]{Long.valueOf(j / 60), Long
                .valueOf(j % 60)}));
        arrayList.add(String.format("%1$02d:%2$02d", new Object[]{Long.valueOf(rawOffset / 60),
                Long.valueOf(rawOffset % 60)}));
        List arrayList2 = new ArrayList();
        arrayList2.add(String.format("%1$02d:%2$02d", new Object[]{Integer.valueOf(i), Integer
                .valueOf(i2)}));
        arrayList2.add(String.format("%1$02d:%2$02d", new Object[]{Integer.valueOf(i3), Integer
                .valueOf(i4)}));
        if (!acceptTimeSet(context, (String) arrayList.get(0), (String) arrayList.get(1))) {
            setCommand(context, COMMAND_SET_ACCEPT_TIME, arrayList, str);
        } else if (1 == PushMessageHelper.getPushMode(context)) {
            PushMessageHandler.a(context, str, COMMAND_SET_ACCEPT_TIME, 0, null, arrayList2);
        } else {
            PushMessageHelper.sendCommandMessageBroadcast(context, PushMessageHelper
                    .generateCommandMessage(COMMAND_SET_ACCEPT_TIME, arrayList2, 0, null, null));
        }
    }

    public static void setAlias(Context context, String str, String str2) {
        if (!TextUtils.isEmpty(str)) {
            setCommand(context, COMMAND_SET_ALIAS, str, str2);
        }
    }

    protected static void setCommand(Context context, String str, String str2, String str3) {
        ArrayList arrayList = new ArrayList();
        if (!TextUtils.isEmpty(str2)) {
            arrayList.add(str2);
        }
        if (!COMMAND_SET_ALIAS.equalsIgnoreCase(str) || System.currentTimeMillis() - aliasSetTime
                (context, str2) >= 3600000) {
            if (COMMAND_UNSET_ALIAS.equalsIgnoreCase(str) && aliasSetTime(context, str2) < 0) {
                b.a("Don't cancel alias for " + arrayList + " is unseted");
            } else if (!COMMAND_SET_ACCOUNT.equalsIgnoreCase(str) || System.currentTimeMillis() -
                    accountSetTime(context, str2) >= 3600000) {
                if (!COMMAND_UNSET_ACCOUNT.equalsIgnoreCase(str) || accountSetTime(context, str2)
                        >= 0) {
                    setCommand(context, str, arrayList, str3);
                } else {
                    b.a("Don't cancel account for " + arrayList + " is unseted");
                }
            } else if (1 == PushMessageHelper.getPushMode(context)) {
                PushMessageHandler.a(context, str3, str, 0, null, arrayList);
            } else {
                PushMessageHelper.sendCommandMessageBroadcast(context, PushMessageHelper
                        .generateCommandMessage(COMMAND_SET_ACCOUNT, arrayList, 0, null, null));
            }
        } else if (1 == PushMessageHelper.getPushMode(context)) {
            PushMessageHandler.a(context, str3, str, 0, null, arrayList);
        } else {
            PushMessageHelper.sendCommandMessageBroadcast(context, PushMessageHelper
                    .generateCommandMessage(COMMAND_SET_ALIAS, arrayList, 0, null, null));
        }
    }

    protected static void setCommand(Context context, String str, ArrayList<String> arrayList,
                                     String str2) {
        if (!TextUtils.isEmpty(a.a(context).c())) {
            org.apache.thrift.b fVar = new f();
            fVar.a(generatePacketID());
            fVar.b(a.a(context).c());
            fVar.c(str);
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                fVar.d((String) it.next());
            }
            fVar.f(str2);
            fVar.e(context.getPackageName());
            g.a(context).a(fVar, com.xiaomi.xmpush.thrift.a.Command, null);
        }
    }

    public static void setLocalNotificationType(Context context, int i) {
        g.a(context).b(i & -1);
    }

    public static void setUserAccount(Context context, String str, String str2) {
        if (!TextUtils.isEmpty(str)) {
            setCommand(context, COMMAND_SET_ACCOUNT, str, str2);
        }
    }

    private static boolean shouldPullNotification(Context context) {
        return System.currentTimeMillis() - context.getSharedPreferences("mipush_extra", 0)
                .getLong("last_pull_notification", -1) > DeviceInfoConstant.REQUEST_LOCATE_INTERVAL;
    }

    private static boolean shouldSendRegRequest(Context context) {
        return System.currentTimeMillis() - context.getSharedPreferences("mipush_extra", 0)
                .getLong("last_reg_request", -1) > 5000;
    }

    public static boolean shouldUseMIUIPush(Context context) {
        return g.a(context).b();
    }

    public static void subscribe(Context context, String str, String str2) {
        if (!TextUtils.isEmpty(a.a(context).c()) && !TextUtils.isEmpty(str)) {
            if (System.currentTimeMillis() - topicSubscribedTime(context, str) > com.umeng
                    .analytics.a.h) {
                org.apache.thrift.b oVar = new o();
                oVar.a(generatePacketID());
                oVar.b(a.a(context).c());
                oVar.c(str);
                oVar.d(context.getPackageName());
                oVar.e(str2);
                g.a(context).a(oVar, com.xiaomi.xmpush.thrift.a.Subscription, null);
            } else if (1 == PushMessageHelper.getPushMode(context)) {
                PushMessageHandler.a(context, str2, 0, null, str);
            } else {
                List arrayList = new ArrayList();
                arrayList.add(str);
                PushMessageHelper.sendCommandMessageBroadcast(context, PushMessageHelper
                        .generateCommandMessage(COMMAND_SUBSCRIBE_TOPIC, arrayList, 0, null, null));
            }
        }
    }

    public static long topicSubscribedTime(Context context, String str) {
        return context.getSharedPreferences("mipush_extra", 0).getLong("topic_" + str, -1);
    }

    public static void unregisterPush(Context context) {
        if (a.a(context).b()) {
            q qVar = new q();
            qVar.a(generatePacketID());
            qVar.b(a.a(context).c());
            qVar.c(a.a(context).e());
            qVar.e(a.a(context).d());
            qVar.d(context.getPackageName());
            g.a(context).a(qVar);
            PushMessageHandler.a();
            a.a(context).k();
            clearExtras(context);
            clearLocalNotificationType(context);
            clearNotification(context);
        }
    }

    public static void unsetAlias(Context context, String str, String str2) {
        setCommand(context, COMMAND_UNSET_ALIAS, str, str2);
    }

    public static void unsetUserAccount(Context context, String str, String str2) {
        setCommand(context, COMMAND_UNSET_ACCOUNT, str, str2);
    }

    public static void unsubscribe(Context context, String str, String str2) {
        if (!a.a(context).b()) {
            return;
        }
        if (topicSubscribedTime(context, str) < 0) {
            b.a("Don't cancel subscribe for " + str + " is unsubscribed");
            return;
        }
        org.apache.thrift.b sVar = new s();
        sVar.a(generatePacketID());
        sVar.b(a.a(context).c());
        sVar.c(str);
        sVar.d(context.getPackageName());
        sVar.e(str2);
        g.a(context).a(sVar, com.xiaomi.xmpush.thrift.a.UnSubscription, null);
    }

    private static void updateIMEI() {
        new Thread(new c()).start();
    }
}
