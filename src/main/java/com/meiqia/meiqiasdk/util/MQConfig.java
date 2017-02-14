package com.meiqia.meiqiasdk.util;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;

import com.meiqia.core.MQManager;
import com.meiqia.core.callback.OnInitCallback;
import com.meiqia.meiqiasdk.controller.ControllerImpl;
import com.meiqia.meiqiasdk.controller.MQController;

public final class MQConfig {
    public static final int     DEFAULT                      = -1;
    public static       boolean isLoadMessagesFromNativeOpen = false;
    public static       boolean isSoundSwitchOpen            = true;
    public static       boolean isVoiceSwitchOpen            = true;
    private static MQController  sController;
    private static MQImageLoader sImageLoader;

    public static final class ui {
        @DrawableRes
        public static int            backArrowIconResId        = -1;
        @ColorRes
        public static int            leftChatBubbleColorResId  = -1;
        @ColorRes
        public static int            leftChatTextColorResId    = -1;
        @ColorRes
        public static int            rightChatBubbleColorResId = -1;
        @ColorRes
        public static int            rightChatTextColorResId   = -1;
        @ColorRes
        public static int            titleBackgroundResId      = -1;
        public static MQTitleGravity titleGravity              = MQTitleGravity.CENTER;
        @ColorRes
        public static int            titleTextColorResId       = -1;
    }

    public static MQController getController(Context context) {
        if (sController == null) {
            synchronized (MQConfig.class) {
                if (sController == null) {
                    sController = new ControllerImpl(context.getApplicationContext());
                }
            }
        }
        return sController;
    }

    public static void registerController(MQController controller) {
        sController = controller;
    }

    public static MQImageLoader getImageLoader(Context context) {
        if (sImageLoader == null) {
            synchronized (MQConfig.class) {
                if (sImageLoader == null) {
                    throw new RuntimeException("请调用MQConfig.init方法初始化美洽 " +
                            "SDK，并传入MQImageLoader接口的实现类");
                }
            }
        }
        return sImageLoader;
    }

    public static void init(Context context, String appKey, MQImageLoader imageLoader, OnInitCallback onInitCallBack) {
        sImageLoader = imageLoader;
        MQManager.init(context, appKey, onInitCallBack);
    }
}
