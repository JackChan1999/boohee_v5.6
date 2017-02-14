package com.boohee.one;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.support.multidex.MultiDex;

import boohee.lib.share.ShareManager;

import com.alipay.euler.andfix.patch.PatchManager;
import com.boohee.cipher.BooheeCipher;
import com.boohee.one.cache.FileCache;
import com.boohee.one.http.DnspodFree;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.one.patch.PatchHelper;
import com.boohee.one.tinker.Log.OneTinkerLogImp;
import com.boohee.one.tinker.TinkerManager;
import com.boohee.push.PushManager;
import com.boohee.uploader.QiniuConfig;
import com.boohee.utility.App;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.AppUtils;
import com.boohee.utils.BlackTech;
import com.boohee.utils.BleUtil;
import com.boohee.utils.MeiQiaHelper;
import com.boohee.utils.MultiImageHelper;
import com.boohee.utils.SystemUtil;
import com.kitnew.ble.QNApiManager;
import com.kitnew.ble.QNResultCallback;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.loader.app.DefaultApplicationLike;
import com.umeng.analytics.AnalyticsConfig;

public class MyApplication extends DefaultApplicationLike {
    private static Application application;
    private static Context     context;
    private static boolean isMainOpened = false;
    private static PatchManager patchManager;
    private static RefWatcher   refWatcher;

    public static boolean getIsMainOpened() {
        return isMainOpened;
    }

    public static void setIsMainActivityOpened(boolean isOpened) {
        isMainOpened = isOpened;
    }

    public static Context getContext() {
        return context;
    }

    public static PatchManager getPatchManager() {
        return patchManager;
    }

    public static RefWatcher getRefWatcher(Context context) {
        return refWatcher == null ? null : refWatcher;
    }

    public MyApplication(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag,
                         long applicationStartElapsedTime, long applicationStartMillisTime,
                         Intent tinkerResultIntent, Resources[] resources, ClassLoader[]
                                 classLoader, AssetManager[] assetManager) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime,
                applicationStartMillisTime, tinkerResultIntent, resources, classLoader,
                assetManager);
    }

    public void onCreate() {
        super.onCreate();
        context = getApplication();
        application = getApplication();
        initLogger();
        if (SystemUtil.getProcessName(context).equalsIgnoreCase("com.boohee.one")) {
            new Thread(new Runnable() {
                public void run() {
                    MyApplication.this.init();
                }
            }).start();
            initImageLoader(context);
        }
    }

    private void init() {
        AnalyticsConfig.setChannel(AppUtils.getChannel(context));
        DnspodFree.getIpWithHost(BooheeClient.getHost(BooheeClient.API));
        DnspodFree.getIpWithHostNoCache(BooheeClient.getHost(BooheeClient.BH_ALL), false);
        App.checkDB(context);
        initBHLibrary();
        FileCache.init(context);
        PushManager.getInstance().initPush(context);
        MultiImageHelper.initMultiSelctor();
        ShareManager.init(context, "http://bohe-house.u.qiniudn.com/android/logo_256x256.png");
        PatchHelper.init(getContext());
        MeiQiaHelper.initSDK();
        if (BleUtil.hasBleFeature(context)) {
            QNApiManager.getApi(context).initSDK("bohe2016070708121217", true, new
                    QNResultCallback() {
                public void onCompete(int i) {
                }
            });
        }
    }

    private void initBHLibrary() {
        boolean z = true;
        String apiEnvironment = BlackTech.getApiEnvironment();
        boolean z2 = true;
        switch (apiEnvironment.hashCode()) {
            case 2576:
                if (apiEnvironment.equals(BlackTech.API_ENV_QA)) {
                    z2 = false;
                    break;
                }
                break;
            case 2609:
                if (apiEnvironment.equals(BlackTech.API_ENV_RC)) {
                    z2 = true;
                    break;
                }
                break;
            case 79501:
                if (apiEnvironment.equals(BlackTech.API_ENV_PRO)) {
                    z2 = true;
                    break;
                }
                break;
        }
        switch (z2) {
            case false:
                QiniuConfig.init(QiniuConfig.DOMAIN_QA);
                break;
            case true:
                QiniuConfig.init(QiniuConfig.DOMAIN_RC);
                break;
            case true:
                QiniuConfig.init(QiniuConfig.DOMAIN_PRO);
                break;
            default:
                QiniuConfig.init(QiniuConfig.DOMAIN_PRO);
                break;
        }
        Context context = context;
        if (BlackTech.isApiProduction().booleanValue()) {
            z = false;
        }
        BooheeCipher.setModule(context, z);
    }

    public static void initImageLoader(Context context) {
        ImageLoader.getInstance().init(new Builder(context).threadPriority(3)
                .denyCacheImageMultipleSizesInMemory().discCacheFileNameGenerator(new
                        Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(ImageLoaderOptions.randomColor()).build());
    }

    private void initLogger() {
        Logger.init("OneLogger").methodCount(3).hideThreadInfo().methodOffset(2);
    }

    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        MultiDex.install(base);
        TinkerManager.setTinkerApplicationLike(this);
        TinkerManager.initFastCrashProtect();
        TinkerManager.setUpgradeRetryEnable(true);
        TinkerInstaller.setLogIml(new OneTinkerLogImp());
        TinkerManager.installTinker(this);
    }
}
