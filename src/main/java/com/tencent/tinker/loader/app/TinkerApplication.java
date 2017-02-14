package com.tencent.tinker.loader.app;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.SystemClock;

import com.tencent.tinker.loader.TinkerLoader;
import com.tencent.tinker.loader.TinkerRuntimeException;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.tencent.tinker.loader.shareutil.ShareIntentUtil;
import com.tencent.tinker.loader.shareutil.ShareReflectUtil;
import com.tencent.tinker.loader.shareutil.ShareTinkerInternals;

public abstract class TinkerApplication extends Application {
    private static final String INTENT_PATCH_EXCEPTION = "intent_patch_exception";
    private static final int    TINKER_DISABLE         = 0;
    private static final String TINKER_LOADER_METHOD   = "tryLoad";
    private       long           applicationStartElapsedTime;
    private       long           applicationStartMillisTime;
    private       AssetManager[] assetManager;
    private       ClassLoader[]  classLoader;
    private       Object         delegate;
    private final String         delegateClassName;
    private final String         loaderClassName;
    private       Resources[]    resources;
    private final int            tinkerFlags;
    private final boolean        tinkerLoadVerifyFlag;
    private       Intent         tinkerResultIntent;
    private       boolean        useSafeMode;

    protected TinkerApplication(int tinkerFlags) {
        this(tinkerFlags, "com.tencent.tinker.loader.app.DefaultApplicationLike", TinkerLoader
                .class.getName(), false);
    }

    protected TinkerApplication(int tinkerFlags, String delegateClassName, String
            loaderClassName, boolean tinkerLoadVerifyFlag) {
        this.delegate = null;
        this.resources = new Resources[1];
        this.classLoader = new ClassLoader[1];
        this.assetManager = new AssetManager[1];
        this.tinkerFlags = tinkerFlags;
        this.delegateClassName = delegateClassName;
        this.loaderClassName = loaderClassName;
        this.tinkerLoadVerifyFlag = tinkerLoadVerifyFlag;
    }

    protected TinkerApplication(int tinkerFlags, String delegateClassName) {
        this(tinkerFlags, delegateClassName, TinkerLoader.class.getName(), false);
    }

    private Object createDelegate() {
        try {
            return Class.forName(this.delegateClassName, false, getClassLoader()).getConstructor
                    (new Class[]{Application.class, Integer.TYPE, Boolean.TYPE, Long.TYPE, Long
                            .TYPE, Intent.class, Resources[].class, ClassLoader[].class,
                            AssetManager[].class}).newInstance(new Object[]{this, Integer.valueOf
                    (this.tinkerFlags), Boolean.valueOf(this.tinkerLoadVerifyFlag), Long.valueOf
                    (this.applicationStartElapsedTime), Long.valueOf(this
                    .applicationStartMillisTime), this.tinkerResultIntent, this.resources, this
                    .classLoader, this.assetManager});
        } catch (Throwable e) {
            TinkerRuntimeException tinkerRuntimeException = new TinkerRuntimeException
                    ("createDelegate failed", e);
        }
    }

    private synchronized void ensureDelegate() {
        if (this.delegate == null) {
            this.delegate = createDelegate();
        }
    }

    private void onBaseContextAttached(Context base) {
        this.applicationStartElapsedTime = SystemClock.elapsedRealtime();
        this.applicationStartMillisTime = System.currentTimeMillis();
        loadTinker();
        ensureDelegate();
        try {
            ShareReflectUtil.findMethod(this.delegate, "onBaseContextAttached", Context.class)
                    .invoke(this.delegate, new Object[]{base});
            if (this.useSafeMode) {
                getSharedPreferences(ShareConstants.TINKER_OWN_PREFERENCE_CONFIG +
                        ShareTinkerInternals.getProcessName(this), 0).edit().putInt
                        (ShareConstants.TINKER_SAFE_MODE_COUNT, 0).commit();
            }
        } catch (Throwable t) {
            TinkerRuntimeException tinkerRuntimeException = new TinkerRuntimeException
                    ("onBaseContextAttached method not found", t);
        }
    }

    protected final void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        onBaseContextAttached(base);
    }

    private void loadTinker() {
        if (this.tinkerFlags != 0) {
            this.tinkerResultIntent = new Intent();
            try {
                Class<?> tinkerLoadClass = Class.forName(this.loaderClassName, false,
                        getClassLoader());
                this.tinkerResultIntent = (Intent) tinkerLoadClass.getMethod
                        (TINKER_LOADER_METHOD, new Class[]{TinkerApplication.class, Integer.TYPE,
                                Boolean.TYPE}).invoke(tinkerLoadClass.getConstructor(new
                        Class[0]).newInstance(new Object[0]), new Object[]{this, Integer.valueOf
                        (this.tinkerFlags), Boolean.valueOf(this.tinkerLoadVerifyFlag)});
            } catch (Throwable e) {
                ShareIntentUtil.setIntentReturnCode(this.tinkerResultIntent, -19);
                this.tinkerResultIntent.putExtra("intent_patch_exception", e);
            }
        }
    }

    private void delegateMethod(String methodName) {
        if (this.delegate != null) {
            try {
                ShareReflectUtil.findMethod(this.delegate, methodName, new Class[0]).invoke(this
                        .delegate, new Object[0]);
            } catch (Throwable t) {
                TinkerRuntimeException tinkerRuntimeException = new TinkerRuntimeException(String
                        .format("%s method not found", new Object[]{methodName}), t);
            }
        }
    }

    public final void onCreate() {
        super.onCreate();
        ensureDelegate();
        delegateMethod("onCreate");
    }

    public final void onTerminate() {
        super.onTerminate();
        delegateMethod("onTerminate");
    }

    public final void onLowMemory() {
        super.onLowMemory();
        delegateMethod("onLowMemory");
    }

    private void delegateTrimMemory(int level) {
        if (this.delegate != null) {
            try {
                ShareReflectUtil.findMethod(this.delegate, "onTrimMemory", Integer.TYPE).invoke
                        (this.delegate, new Object[]{Integer.valueOf(level)});
            } catch (Throwable t) {
                TinkerRuntimeException tinkerRuntimeException = new TinkerRuntimeException
                        ("onTrimMemory method not found", t);
            }
        }
    }

    @TargetApi(14)
    public final void onTrimMemory(int level) {
        super.onTrimMemory(level);
        delegateTrimMemory(level);
    }

    private void delegateConfigurationChanged(Configuration newConfig) {
        if (this.delegate != null) {
            try {
                ShareReflectUtil.findMethod(this.delegate, "onConfigurationChanged",
                        Configuration.class).invoke(this.delegate, new Object[]{newConfig});
            } catch (Throwable t) {
                TinkerRuntimeException tinkerRuntimeException = new TinkerRuntimeException
                        ("onConfigurationChanged method not found", t);
            }
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        delegateConfigurationChanged(newConfig);
    }

    public Resources getResources() {
        if (this.resources[0] != null) {
            return this.resources[0];
        }
        return super.getResources();
    }

    public ClassLoader getClassLoader() {
        if (this.classLoader[0] != null) {
            return this.classLoader[0];
        }
        return super.getClassLoader();
    }

    public AssetManager getAssets() {
        if (this.assetManager[0] != null) {
            return this.assetManager[0];
        }
        return super.getAssets();
    }

    public void setUseSafeMode(boolean useSafeMode) {
        this.useSafeMode = useSafeMode;
    }
}
