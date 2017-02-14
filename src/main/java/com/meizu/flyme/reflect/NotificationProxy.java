package com.meizu.flyme.reflect;

import android.app.Notification.Builder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class NotificationProxy extends Proxy {
    private static Class<?> sClass                     = Builder.class;
    private static Field    sField                     = null;
    private static Object   sObject                    = null;
    private static Method   sSetCircleProgressBarColor = null;
    private static Method   sSetProgressBarStype       = null;
    private static Method   ssetCircleProgressRimColor = null;

    public static void setProgressBarStype(Builder builder, boolean isCircle) {
        try {
            sField = sClass.getField("mFlymeNotificationBuilder");
            sObject = sField.get(builder);
            sSetProgressBarStype = sField.getType().getDeclaredMethod("setCircleProgressBar", new
                    Class[]{Boolean.TYPE});
            if (sObject != null) {
                Proxy.invoke(sSetProgressBarStype, sObject, Boolean.valueOf(isCircle));
            }
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
    }

    public static void setCircleProgressBarColor(int color) {
        try {
            if (sField != null && sObject != null) {
                sSetCircleProgressBarColor = sField.getType().getDeclaredMethod
                        ("setCircleProgressBarColor", new Class[]{Integer.TYPE});
                Proxy.invoke(sSetCircleProgressBarColor, sObject, Integer.valueOf(color));
            }
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
    }

    public static void setCircleProgressRimColor(int color) {
        try {
            if (sField != null && sObject != null) {
                ssetCircleProgressRimColor = sField.getType().getDeclaredMethod
                        ("ssetCircleProgressRimColor", new Class[]{Integer.TYPE});
                Proxy.invoke(ssetCircleProgressRimColor, sObject, Integer.valueOf(color));
            }
        } catch (Exception e) {
        }
    }
}
