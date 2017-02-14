package com.tencent.tinker.loader;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build.VERSION;
import android.util.ArrayMap;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;

class TinkerResourcePatcher {
    private static Method       addAssetPathMethod       = null;
    private static Field        assetsFiled              = null;
    private static Method       ensureStringBlocksMethod = null;
    private static AssetManager newAssetManager          = null;
    private static Collection<WeakReference<Resources>> references;
    private static Field resourcesImplFiled = null;

    TinkerResourcePatcher() {
    }

    public static void isResourceCanPatch(Context context) throws Throwable {
        newAssetManager = (AssetManager) AssetManager.class.getConstructor(new Class[0])
                .newInstance(new Object[0]);
        addAssetPathMethod = AssetManager.class.getDeclaredMethod("addAssetPath", new
                Class[]{String.class});
        addAssetPathMethod.setAccessible(true);
        ensureStringBlocksMethod = AssetManager.class.getDeclaredMethod("ensureStringBlocks", new
                Class[0]);
        ensureStringBlocksMethod.setAccessible(true);
        Field fMActiveResources;
        if (VERSION.SDK_INT >= 19) {
            Class<?> resourcesManagerClass = Class.forName("android.app.ResourcesManager");
            Method mGetInstance = resourcesManagerClass.getDeclaredMethod("getInstance", new
                    Class[0]);
            mGetInstance.setAccessible(true);
            Object resourcesManager = mGetInstance.invoke(null, new Object[0]);
            try {
                fMActiveResources = resourcesManagerClass.getDeclaredField("mActiveResources");
                fMActiveResources.setAccessible(true);
                references = ((ArrayMap) fMActiveResources.get(resourcesManager)).values();
            } catch (NoSuchFieldException e) {
                Field mResourceReferences = resourcesManagerClass.getDeclaredField
                        ("mResourceReferences");
                mResourceReferences.setAccessible(true);
                references = (Collection) mResourceReferences.get(resourcesManager);
            }
        } else {
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            fMActiveResources = activityThread.getDeclaredField("mActiveResources");
            fMActiveResources.setAccessible(true);
            references = ((HashMap) fMActiveResources.get(getActivityThread(context,
                    activityThread))).values();
        }
        if (references == null || references.isEmpty()) {
            throw new IllegalStateException("resource references is null or empty");
        }
        try {
            assetsFiled = Resources.class.getDeclaredField("mAssets");
            assetsFiled.setAccessible(true);
        } catch (Throwable th) {
            resourcesImplFiled = Resources.class.getDeclaredField("mResourcesImpl");
            resourcesImplFiled.setAccessible(true);
        }
    }

    public static void monkeyPatchExistingResources(String externalResourceFile) throws Throwable {
        if (externalResourceFile != null) {
            if (((Integer) addAssetPathMethod.invoke(newAssetManager, new
                    Object[]{externalResourceFile})).intValue() == 0) {
                throw new IllegalStateException("Could not create new AssetManager");
            }
            ensureStringBlocksMethod.invoke(newAssetManager, new Object[0]);
            for (WeakReference<Resources> wr : references) {
                Resources resources = (Resources) wr.get();
                if (resources != null) {
                    try {
                        assetsFiled.set(resources, newAssetManager);
                    } catch (Throwable th) {
                        Object resourceImpl = resourcesImplFiled.get(resources);
                        Field implAssets = resourceImpl.getClass().getDeclaredField("mAssets");
                        implAssets.setAccessible(true);
                        implAssets.set(resourceImpl, newAssetManager);
                    }
                    resources.updateConfiguration(resources.getConfiguration(), resources
                            .getDisplayMetrics());
                }
            }
        }
    }

    private static Object getActivityThread(Context context, Class<?> activityThread) {
        if (activityThread == null) {
            try {
                activityThread = Class.forName("android.app.ActivityThread");
            } catch (Throwable th) {
                return null;
            }
        }
        Method m = activityThread.getMethod("currentActivityThread", new Class[0]);
        m.setAccessible(true);
        Object currentActivityThread = m.invoke(null, new Object[0]);
        if (currentActivityThread != null || context == null) {
            return currentActivityThread;
        }
        Field mLoadedApk = context.getClass().getField("mLoadedApk");
        mLoadedApk.setAccessible(true);
        Object apk = mLoadedApk.get(context);
        Field mActivityThreadField = apk.getClass().getDeclaredField("mActivityThread");
        mActivityThreadField.setAccessible(true);
        return mActivityThreadField.get(apk);
    }
}
