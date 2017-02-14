package com.tencent.tinker.loader;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;

import dalvik.system.PathClassLoader;

import java.lang.reflect.Field;

@TargetApi(14)
class AndroidNClassLoader extends PathClassLoader {
    PathClassLoader originClassLoader;

    private AndroidNClassLoader(String dexPath, PathClassLoader parent) {
        super(dexPath, parent.getParent());
        this.originClassLoader = parent;
    }

    private static AndroidNClassLoader createAndroidNClassLoader(PathClassLoader original) throws
            Exception {
        AndroidNClassLoader androidNClassLoader = new AndroidNClassLoader("", original);
        Object originPathListObject = findField(original, "pathList").get(original);
        findField(originPathListObject, "definingContext").set(originPathListObject,
                androidNClassLoader);
        findField(androidNClassLoader, "pathList").set(androidNClassLoader, originPathListObject);
        return androidNClassLoader;
    }

    private static Field findField(Object instance, String name) throws NoSuchFieldException {
        Class<?> clazz = instance.getClass();
        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(name);
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                return field;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        throw new NoSuchFieldException("Field " + name + " not found in " + instance.getClass());
    }

    private static void reflectPackageInfoClassloader(Application application, ClassLoader
            reflectClassLoader) throws Exception {
        Context baseContext = (Context) findField(application, "mBase").get(application);
        Object basePackageInfo = findField(baseContext, "mPackageInfo").get(baseContext);
        Field classLoaderField = findField(basePackageInfo, "mClassLoader");
        Thread.currentThread().setContextClassLoader(reflectClassLoader);
        classLoaderField.set(basePackageInfo, reflectClassLoader);
    }

    public static AndroidNClassLoader inject(PathClassLoader originClassLoader, Application
            application) throws Exception {
        AndroidNClassLoader classLoader = createAndroidNClassLoader(originClassLoader);
        reflectPackageInfoClassloader(application, classLoader);
        return classLoader;
    }

    public Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }

    public String findLibrary(String name) {
        return super.findLibrary(name);
    }
}
