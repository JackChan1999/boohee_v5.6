package com.xiaomi.push.service;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Notification;
import android.app.Notification.BigTextStyle;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.support.v7.widget.ActivityChooserView.ActivityChooserViewAdapter;
import android.text.TextUtils;
import android.util.Pair;
import android.widget.RemoteViews;

import com.boohee.model.ModelName;
import com.boohee.model.status.Post;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.xmpush.thrift.c;
import com.xiaomi.xmpush.thrift.h;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

public class s {
    public static  long                              a = 0;
    private static LinkedList<Pair<Integer, String>> b = new LinkedList();

    private static int a(Context context, String str, String str2) {
        return str.equals(context.getPackageName()) ? context.getResources().getIdentifier(str2,
                "drawable", str) : 0;
    }

    private static Notification a(Notification notification, String str) {
        try {
            Field declaredField = Notification.class.getDeclaredField("extraNotification");
            declaredField.setAccessible(true);
            Object obj = declaredField.get(notification);
            Method declaredMethod = obj.getClass().getDeclaredMethod("setTargetPkg", new
                    Class[]{CharSequence.class});
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(obj, new Object[]{str});
        } catch (Throwable e) {
            b.a(e);
        }
        return notification;
    }

    @SuppressLint({"NewApi"})
    private static Notification a(Context context, h hVar, byte[] bArr, RemoteViews remoteViews,
                                  PendingIntent pendingIntent) {
        c m = hVar.m();
        Builder builder = new Builder(context);
        String[] a = a(context, m);
        builder.setContentTitle(a[0]);
        builder.setContentText(a[1]);
        if (remoteViews != null) {
            builder.setContent(remoteViews);
        } else if (VERSION.SDK_INT >= 16) {
            builder.setStyle(new BigTextStyle().bigText(a[1]));
        }
        builder.setWhen(System.currentTimeMillis());
        builder.setContentIntent(pendingIntent);
        int a2 = a(context, a(hVar), "mipush_notification");
        int a3 = a(context, a(hVar), "mipush_small_notification");
        if (a2 <= 0 || a3 <= 0) {
            builder.setSmallIcon(f(context, a(hVar)));
        } else {
            builder.setLargeIcon(a(context, a2));
            builder.setSmallIcon(a3);
        }
        builder.setAutoCancel(true);
        long currentTimeMillis = System.currentTimeMillis();
        Map s = m.s();
        if (s != null && s.containsKey("ticker")) {
            builder.setTicker((CharSequence) s.get("ticker"));
        }
        if (currentTimeMillis - a > 10000) {
            a = currentTimeMillis;
            int c = e(context, a(hVar)) ? c(context, a(hVar)) : m.f;
            builder.setDefaults(c);
            if (!(s == null || (c & 1) == 0)) {
                String str = (String) s.get("sound_uri");
                if (!TextUtils.isEmpty(str) && str.startsWith("android.resource://" + a(hVar))) {
                    builder.setDefaults(c ^ 1);
                    builder.setSound(Uri.parse(str));
                }
            }
        }
        return builder.getNotification();
    }

    private static PendingIntent a(Context context, h hVar, c cVar, byte[] bArr) {
        Intent intent;
        if (cVar != null && !TextUtils.isEmpty(cVar.g)) {
            intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse(cVar.g));
            intent.addFlags(268435456);
            return PendingIntent.getActivity(context, 0, intent, 134217728);
        } else if (b(hVar)) {
            intent = new Intent();
            intent.setComponent(new ComponentName("com.xiaomi.xmsf", "com.xiaomi.mipush.sdk" +
                    ".PushMessageHandler"));
            intent.putExtra("mipush_payload", bArr);
            intent.putExtra("mipush_notified", true);
            intent.addCategory(String.valueOf(cVar.q()));
            return PendingIntent.getService(context, 0, intent, 134217728);
        } else {
            intent = new Intent("com.xiaomi.mipush.RECEIVE_MESSAGE");
            intent.setComponent(new ComponentName(hVar.f, "com.xiaomi.mipush.sdk" +
                    ".PushMessageHandler"));
            intent.putExtra("mipush_payload", bArr);
            intent.putExtra("mipush_notified", true);
            intent.addCategory(String.valueOf(cVar.q()));
            return PendingIntent.getService(context, 0, intent, 134217728);
        }
    }

    private static Bitmap a(Context context, int i) {
        return a(context.getResources().getDrawable(i));
    }

    public static Bitmap a(Drawable drawable) {
        int i = 1;
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        int intrinsicWidth = drawable.getIntrinsicWidth();
        if (intrinsicWidth <= 0) {
            intrinsicWidth = 1;
        }
        int intrinsicHeight = drawable.getIntrinsicHeight();
        if (intrinsicHeight > 0) {
            i = intrinsicHeight;
        }
        Bitmap createBitmap = Bitmap.createBitmap(intrinsicWidth, i, Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return createBitmap;
    }

    static String a(h hVar) {
        if ("com.xiaomi.xmsf".equals(hVar.f)) {
            c m = hVar.m();
            if (!(m == null || m.s() == null)) {
                String str = (String) m.s().get("miui_package_name");
                if (!TextUtils.isEmpty(str)) {
                    return str;
                }
            }
        }
        return hVar.f;
    }

    public static void a(Context context, h hVar, byte[] bArr) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService
                ("notification");
        c m = hVar.m();
        RemoteViews b = b(context, hVar, bArr);
        PendingIntent a = a(context, hVar, m, bArr);
        if (a == null) {
            b.a("The click PendingIntent is null. ");
            return;
        }
        Notification a2;
        int c;
        if (VERSION.SDK_INT >= 11) {
            a2 = a(context, hVar, bArr, b, a);
        } else {
            Notification notification = new Notification(f(context, a(hVar)), null, System
                    .currentTimeMillis());
            String[] a3 = a(context, m);
            try {
                notification.getClass().getMethod("setLatestEventInfo", new Class[]{Context
                        .class, CharSequence.class, CharSequence.class, PendingIntent.class})
                        .invoke(notification, new Object[]{context, a3[0], a3[1], a});
            } catch (Throwable e) {
                b.a(e);
            } catch (Throwable e2) {
                b.a(e2);
            } catch (Throwable e22) {
                b.a(e22);
            } catch (Throwable e222) {
                b.a(e222);
            }
            Map s = m.s();
            if (s != null && s.containsKey("ticker")) {
                notification.tickerText = (CharSequence) s.get("ticker");
            }
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - a > 10000) {
                a = currentTimeMillis;
                int i = m.f;
                if (e(context, a(hVar))) {
                    c = c(context, a(hVar));
                } else {
                    c = i;
                }
                notification.defaults = c;
                if (!(s == null || (c & 1) == 0)) {
                    String str = (String) s.get("sound_uri");
                    if (!TextUtils.isEmpty(str) && str.startsWith("android.resource://" + a(hVar)
                    )) {
                        notification.defaults = c ^ 1;
                        notification.sound = Uri.parse(str);
                    }
                }
            }
            notification.flags |= 16;
            if (b != null) {
                notification.contentView = b;
            }
            a2 = notification;
        }
        if ("com.xiaomi.xmsf".equals(context.getPackageName())) {
            a(a2, a(hVar));
        }
        c = m.q() + ((a(hVar).hashCode() / 10) * 10);
        notificationManager.notify(c, a2);
        Pair pair = new Pair(Integer.valueOf(c), a(hVar));
        synchronized (b) {
            b.add(pair);
            if (b.size() > 100) {
                b.remove();
            }
        }
    }

    public static void a(Context context, String str, int i) {
        int hashCode = ((str.hashCode() / 10) * 10) + i;
        ((NotificationManager) context.getSystemService("notification")).cancel(hashCode);
        synchronized (b) {
            Iterator it = b.iterator();
            while (it.hasNext()) {
                Pair pair = (Pair) it.next();
                if (hashCode == ((Integer) pair.first).intValue() && TextUtils.equals(str,
                        (CharSequence) pair.second)) {
                    b.remove(pair);
                    break;
                }
            }
        }
    }

    public static boolean a(Context context, String str) {
        List<RunningAppProcessInfo> runningAppProcesses = ((ActivityManager) context
                .getSystemService(ModelName.ACTIVITY)).getRunningAppProcesses();
        if (runningAppProcesses != null) {
            for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
                if (runningAppProcessInfo.importance == 100 && Arrays.asList
                        (runningAppProcessInfo.pkgList).contains(str)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean a(Map<String, String> map) {
        if (map == null || !map.containsKey("notify_foreground")) {
            return true;
        }
        return "1".equals((String) map.get("notify_foreground"));
    }

    private static String[] a(Context context, c cVar) {
        String h = cVar.h();
        String j = cVar.j();
        Map s = cVar.s();
        if (s != null) {
            int intValue = Float.valueOf((((float) context.getResources().getDisplayMetrics()
                    .widthPixels) / context.getResources().getDisplayMetrics().density) + 0.5f)
                    .intValue();
            String str;
            if (intValue <= 320) {
                str = (String) s.get("title_short");
                if (!TextUtils.isEmpty(str)) {
                    h = str;
                }
                CharSequence charSequence = (String) s.get("description_short");
                if (TextUtils.isEmpty(charSequence)) {
                    Object obj = j;
                }
                j = charSequence;
            } else if (intValue > 360) {
                str = (String) s.get("title_long");
                if (!TextUtils.isEmpty(str)) {
                    h = str;
                }
                str = (String) s.get("description_long");
                if (!TextUtils.isEmpty(str)) {
                    j = str;
                }
            }
        }
        return new String[]{h, j};
    }

    private static RemoteViews b(Context context, h hVar, byte[] bArr) {
        c m = hVar.m();
        String a = a(hVar);
        Map s = m.s();
        if (s == null) {
            return null;
        }
        String str = (String) s.get("layout_name");
        String str2 = (String) s.get("layout_value");
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return null;
        }
        try {
            Resources resourcesForApplication = context.getPackageManager()
                    .getResourcesForApplication(a);
            int identifier = resourcesForApplication.getIdentifier(str, "layout", a);
            if (identifier == 0) {
                return null;
            }
            RemoteViews remoteViews = new RemoteViews(a, identifier);
            try {
                JSONObject jSONObject;
                Iterator keys;
                JSONObject jSONObject2 = new JSONObject(str2);
                if (jSONObject2.has("text")) {
                    jSONObject = jSONObject2.getJSONObject("text");
                    keys = jSONObject.keys();
                    while (keys.hasNext()) {
                        str = (String) keys.next();
                        CharSequence string = jSONObject.getString(str);
                        identifier = resourcesForApplication.getIdentifier(str, "id", a);
                        if (identifier > 0) {
                            remoteViews.setTextViewText(identifier, string);
                        }
                    }
                }
                if (jSONObject2.has(Post.IMAGE_TYPE)) {
                    jSONObject = jSONObject2.getJSONObject(Post.IMAGE_TYPE);
                    keys = jSONObject.keys();
                    while (keys.hasNext()) {
                        str = (String) keys.next();
                        String string2 = jSONObject.getString(str);
                        identifier = resourcesForApplication.getIdentifier(str, "id", a);
                        int identifier2 = resourcesForApplication.getIdentifier(string2,
                                "drawable", a);
                        if (identifier > 0) {
                            remoteViews.setImageViewResource(identifier, identifier2);
                        }
                    }
                }
                if (jSONObject2.has("time")) {
                    jSONObject2 = jSONObject2.getJSONObject("time");
                    keys = jSONObject2.keys();
                    while (keys.hasNext()) {
                        str = (String) keys.next();
                        str2 = jSONObject2.getString(str);
                        if (str2.length() == 0) {
                            str2 = "yy-MM-dd hh:mm";
                        }
                        identifier = resourcesForApplication.getIdentifier(str, "id", a);
                        if (identifier > 0) {
                            remoteViews.setTextViewText(identifier, new SimpleDateFormat(str2)
                                    .format(new Date(System.currentTimeMillis())));
                        }
                    }
                }
                return remoteViews;
            } catch (Throwable e) {
                b.a(e);
                return null;
            }
        } catch (Throwable e2) {
            b.a(e2);
            return null;
        }
    }

    public static void b(Context context, String str) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService
                ("notification");
        synchronized (b) {
            Iterator it = ((LinkedList) b.clone()).iterator();
            while (it.hasNext()) {
                Pair pair = (Pair) it.next();
                if (TextUtils.equals((CharSequence) pair.second, str)) {
                    notificationManager.cancel(((Integer) pair.first).intValue());
                    b.remove(pair);
                }
            }
        }
    }

    static void b(Context context, String str, int i) {
        context.getSharedPreferences("pref_notify_type", 0).edit().putInt(str, i).commit();
    }

    public static boolean b(h hVar) {
        c m = hVar.m();
        return m != null && m.v();
    }

    static int c(Context context, String str) {
        return context.getSharedPreferences("pref_notify_type", 0).getInt(str,
                ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED);
    }

    static void d(Context context, String str) {
        context.getSharedPreferences("pref_notify_type", 0).edit().remove(str).commit();
    }

    static boolean e(Context context, String str) {
        return context.getSharedPreferences("pref_notify_type", 0).contains(str);
    }

    private static int f(Context context, String str) {
        int a = a(context, str, "mipush_notification");
        int a2 = a(context, str, "mipush_small_notification");
        if (a <= 0) {
            a = a2 > 0 ? a2 : context.getApplicationInfo().icon;
        }
        return (a != 0 || VERSION.SDK_INT < 9) ? a : context.getApplicationInfo().logo;
    }
}
