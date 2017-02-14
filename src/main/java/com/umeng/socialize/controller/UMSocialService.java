package com.umeng.socialize.controller;

import android.content.Context;

import com.umeng.socialize.bean.CallbackConfig.ICallbackListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.ShareType;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.listener.SocializeListeners.FetchFriendsListener;
import com.umeng.socialize.controller.listener.SocializeListeners.FetchUserListener;
import com.umeng.socialize.controller.listener.SocializeListeners.MulStatusListener;
import com.umeng.socialize.controller.listener.SocializeListeners.SocializeClientListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMediaObject;

public interface UMSocialService extends AuthService, CommentService, LikeService, ShareService,
        UserCenterService {
    void follow(Context context, SHARE_MEDIA share_media, MulStatusListener mulStatusListener,
                String... strArr);

    SocializeConfig getConfig();

    SocializeEntity getEntity();

    void getFriends(Context context, FetchFriendsListener fetchFriendsListener, SHARE_MEDIA
            share_media);

    void getPlatformInfo(Context context, SHARE_MEDIA share_media, UMDataListener uMDataListener);

    void getUserInfo(Context context, FetchUserListener fetchUserListener);

    boolean hasShareContent();

    boolean hasShareImage();

    void initEntity(Context context, SocializeClientListener socializeClientListener);

    boolean registerListener(ICallbackListener iCallbackListener) throws SocializeException;

    boolean registerListener(ICallbackListener iCallbackListener, int i) throws SocializeException;

    void setAppWebSite(SHARE_MEDIA share_media, String str);

    void setAppWebSite(String str);

    void setConfig(SocializeConfig socializeConfig);

    void setCustomId(String str);

    void setEntityName(String str);

    void setGlobalConfig(SocializeConfig socializeConfig);

    void setShareContent(String str);

    @Deprecated
    void setShareImage(UMImage uMImage);

    boolean setShareMedia(UMediaObject uMediaObject);

    void setShareType(ShareType shareType);

    boolean unregisterListener(ICallbackListener iCallbackListener);
}
