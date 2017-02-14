package com.alipay.euler.andfix;

import android.os.Build.VERSION;
import android.util.Log;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class AndFix {
    private static final String TAG = "AndFix";

    private static native void replaceMethod(Method method, Method method2);

    private static native void setFieldFlag(Field field);

    private static native boolean setup(boolean z, int i);

    static {
        try {
            Runtime.getRuntime().loadLibrary("andfix");
        } catch (Throwable e) {
            Log.e(TAG, "loadLibrary", e);
        }
    }

    public static void addReplaceMethod(Method src, Method dest) {
        try {
            replaceMethod(src, dest);
            initFields(dest.getDeclaringClass());
        } catch (Throwable e) {
            Log.e(TAG, "addReplaceMethod", e);
        }
    }

    public static Class<?> initTargetClass(Class<?> clazz) {
        try {
            Class<?> targetClazz = Class.forName(clazz.getName(), true, clazz.getClassLoader());
            initFields(targetClazz);
            return targetClazz;
        } catch (Exception e) {
            Log.e(TAG, "initTargetClass", e);
            return null;
        }
    }

    private static void initFields(Class<?> clazz) {
        for (Field srcField : clazz.getDeclaredFields()) {
            Log.d(TAG, "modify " + clazz.getName() + "." + srcField.getName() + " flag:");
            setFieldFlag(srcField);
        }
    }

    public static boolean setup() {
        boolean z = false;
        try {
            String vmVersion = System.getProperty("java.vm.version");
            boolean isArt = vmVersion != null && vmVersion.startsWith("2");
            z = setup(isArt, VERSION.SDK_INT);
        } catch (Exception e) {
            Log.e(TAG, "setup", e);
        }
        return z;
    }
}
