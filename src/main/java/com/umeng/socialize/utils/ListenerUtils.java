package com.umeng.socialize.utils;

import android.os.Bundle;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;

import java.util.Map;

public class ListenerUtils {
    public static SnsPostListener createSnsPostListener() {
        return new SnsPostListener() {
            public void onStart() {
            }

            public void onComplete(SHARE_MEDIA share_media, int i, SocializeEntity
                    socializeEntity) {
            }
        };
    }

    public static UMAuthListener creAuthListener() {
        return new UMAuthListener() {
            public void onStart(SHARE_MEDIA share_media) {
            }

            public void onError(SocializeException socializeException, SHARE_MEDIA share_media) {
            }

            public void onComplete(Bundle bundle, SHARE_MEDIA share_media) {
            }

            public void onCancel(SHARE_MEDIA share_media) {
            }
        };
    }

    public static UMDataListener createDataListener() {
        return new UMDataListener() {
            public void onStart() {
            }

            public void onComplete(int i, Map<String, Object> map) {
            }
        };
    }
}
