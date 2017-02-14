package com.boohee.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.view.View;

import com.boohee.myview.NewBadgeView;
import com.boohee.one.R;
import com.squareup.leakcanary.internal.LeakCanaryInternals;

public class BadgeUtils {
    static final String TAG = BadgeUtils.class.getSimpleName();

    private static void setToXiaoMi(android.content.Context r14, int r15) {
        /* JADX: method processing error */
/*
Error: java.util.NoSuchElementException
	at java.util.HashMap$HashIterator.nextNode(HashMap.java:1439)
	at java.util.HashMap$KeyIterator.next(HashMap.java:1461)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.applyRemove(BlockFinallyExtract
	.java:535)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.extractFinally(BlockFinallyExtract
	.java:175)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.processExceptionHandler
	(BlockFinallyExtract.java:79)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:51)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
        /*
        r13 = 101010; // 0x18a92 float:1.41545E-40 double:4.99056E-319;
        r4 = getLauncherClassName(r14);
        if (r4 != 0) goto L_0x000a;
    L_0x0009:
        return;
    L_0x000a:
        r10 = "notification";
        r8 = r14.getSystemService(r10);
        r8 = (android.app.NotificationManager) r8;
        r9 = 0;
        r3 = 1;
        r0 = new android.support.v4.app.NotificationCompat$Builder;	 Catch:{ Exception -> 0x00a4,
        all -> 0x00e5 }
        r0.<init>(r14);	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r10 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r10.<init>();	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r11 = "您有";	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r10 = r10.append(r15);	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r11 = "未读消息";	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r10 = r10.toString();	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r0.setContentTitle(r10);	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r10 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r10.<init>();	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r11 = "您有";	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r10 = r10.append(r15);	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r11 = "未读消息";	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r10 = r10.toString();	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r0.setTicker(r10);	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r10 = 1;	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r0.setAutoCancel(r10);	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r10 = 2130968995; // 0x7f0401a3 float:1.754666E38 double:1.052838573E-314;	 Catch:{
        Exception -> 0x00a4, all -> 0x00e5 }
        r0.setSmallIcon(r10);	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r10 = 4;	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r0.setDefaults(r10);	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r9 = r0.build();	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r10 = "android.app.MiuiNotification";	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r7 = java.lang.Class.forName(r10);	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r6 = r7.newInstance();	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r10 = r6.getClass();	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r11 = "messageCount";	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r2 = r10.getDeclaredField(r11);	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r10 = 1;	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r2.setAccessible(r10);	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r10 = java.lang.Integer.valueOf(r15);	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r2.set(r6, r10);	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r10 = r9.getClass();	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r11 = "extraNotification";	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r2 = r10.getField(r11);	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r10 = 1;	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r2.setAccessible(r10);	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r2.set(r9, r6);	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        if (r9 == 0) goto L_0x0009;
    L_0x009d:
        if (r3 == 0) goto L_0x0009;
    L_0x009f:
        r8.notify(r13, r9);
        goto L_0x0009;
    L_0x00a4:
        r1 = move-exception;
        r1.printStackTrace();	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r3 = 0;	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r5 = new android.content.Intent;	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r10 = "android.intent.action.APPLICATION_MESSAGE_UPDATE";	 Catch:{ Exception -> 0x00a4,
        all -> 0x00e5 }
        r5.<init>(r10);	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r10 = "android.intent.extra.update_application_component_name";	 Catch:{ Exception ->
        0x00a4, all -> 0x00e5 }
        r11 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r11.<init>();	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r12 = r14.getPackageName();	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r11 = r11.append(r12);	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r12 = "/";	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r11 = r11.append(r12);	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r11 = r11.append(r4);	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r11 = r11.toString();	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r5.putExtra(r10, r11);	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r10 = "android.intent.extra.update_application_message_text";	 Catch:{ Exception ->
        0x00a4, all -> 0x00e5 }
        r5.putExtra(r10, r15);	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        r14.sendBroadcast(r5);	 Catch:{ Exception -> 0x00a4, all -> 0x00e5 }
        if (r9 == 0) goto L_0x0009;
    L_0x00de:
        if (r3 == 0) goto L_0x0009;
    L_0x00e0:
        r8.notify(r13, r9);
        goto L_0x0009;
    L_0x00e5:
        r10 = move-exception;
        if (r9 == 0) goto L_0x00ed;
    L_0x00e8:
        if (r3 == 0) goto L_0x00ed;
    L_0x00ea:
        r8.notify(r13, r9);
    L_0x00ed:
        throw r10;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.boohee.utils" +
                ".BadgeUtils.setToXiaoMi(android.content.Context, int):void");
    }

    public static void addBadge(NewBadgeView badge, View targetView, int count) {
        if (badge != null) {
            badge.getLayoutParams().height = 20;
            badge.getLayoutParams().width = 20;
            badge.setTargetView(targetView);
            badge.setBadgeGravity(8388661);
            badge.setText("");
            badge.setBadgeMargin(0, 5, 25, 0);
            if (count > 0) {
                badge.setVisibility(0);
                badge.setHideOnNull(false);
                return;
            }
            badge.setVisibility(8);
        }
    }

    public static void initBadgeView(Context context, NewBadgeView badgeView, View targetView) {
        badgeView = new NewBadgeView(context);
        badgeView.setTargetView(targetView);
        badgeView.setBadgeGravity(17);
        badgeView.setTextColor(-1);
        badgeView.setBackground(10, context.getResources().getColor(R.color.he));
    }

    public static void addPaddingBadge(NewBadgeView badge, View targetView, int count) {
        if (badge != null) {
            badge.getLayoutParams().height = 20;
            badge.getLayoutParams().width = 20;
            badge.setTargetView(targetView);
            badge.setBadgeGravity(8388661);
            badge.setText("");
            badge.setPadding(5, 0, 0, 25);
            if (count > 0) {
                badge.setVisibility(0);
                badge.setHideOnNull(false);
                return;
            }
            badge.setVisibility(8);
        }
    }

    public static void setIconBadge(Context context, int count) {
        try {
            if (!Build.MANUFACTURER.equalsIgnoreCase(MiBandHelper.KEY_DATA_SOURCE)) {
                if (Build.MANUFACTURER.equalsIgnoreCase(LeakCanaryInternals.SAMSUNG)) {
                    setToSamsumg(context, count);
                } else if (Build.MANUFACTURER.toLowerCase().contains("sony")) {
                    setToSony(context, count);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setToSony(Context context, int count) {
        String launcherClassName = getLauncherClassName(context);
        if (launcherClassName != null) {
            boolean isShow = true;
            if (count <= 0) {
                isShow = false;
            }
            Intent localIntent = new Intent();
            localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE", isShow);
            localIntent.setAction("com.sonyericsson.home.action.UPDATE_BADGE");
            localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME",
                    launcherClassName);
            localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.MESSAGE", count);
            localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME", context
                    .getPackageName());
            context.sendBroadcast(localIntent);
        }
    }

    public static void setToSamsumg(Context context, int count) {
        String launcherClassName = getLauncherClassName(context);
        if (launcherClassName != null) {
            Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
            intent.putExtra("badge_count", count);
            intent.putExtra("badge_count_package_name", context.getPackageName());
            intent.putExtra("badge_count_class_name", launcherClassName);
            context.sendBroadcast(intent);
        }
    }

    public static String getLauncherClassName(Context context) {
        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        for (ResolveInfo resolveInfo : pm.queryIntentActivities(intent, 0)) {
            if (resolveInfo.activityInfo.applicationInfo.packageName.equalsIgnoreCase(context
                    .getPackageName())) {
                return resolveInfo.activityInfo.name;
            }
        }
        return null;
    }
}
