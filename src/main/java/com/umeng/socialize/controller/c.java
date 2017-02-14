package com.umeng.socialize.controller;

import com.umeng.socialize.bean.SHARE_MEDIA;

/* compiled from: AppPlatformFactory */
/* synthetic */ class c {
    static final /* synthetic */ int[] a = new int[SHARE_MEDIA.values().length];

    static {
        try {
            a[SHARE_MEDIA.TWITTER.ordinal()] = 1;
        } catch (NoSuchFieldError e) {
        }
        try {
            a[SHARE_MEDIA.GOOGLEPLUS.ordinal()] = 2;
        } catch (NoSuchFieldError e2) {
        }
        try {
            a[SHARE_MEDIA.FACEBOOK.ordinal()] = 3;
        } catch (NoSuchFieldError e3) {
        }
    }
}
