package com.boohee.utility;

import android.app.Activity;

import com.boohee.api.OneApi;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.utils.Helper;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import org.json.JSONObject;

public class ShareHelper {
    public static final int    FROM_MIME    = 1;
    public static final int    FROM_STATUS  = 0;
    public static final int    FROM_UCHOICE = 2;
    public static final String QQ_APPID     = "100530867";
    public static final String QQ_APPKEY    = "d32ea174315e9c42bfbd481ac3b3fef6";
    public static final String WX_APPID     = "wxaddc5c19a31e9d39";
    public static final String WX_APPKEY    = "1f98ca2de209fb04baea8fd983c8fc45";
    private static ShareHelper instance;
    private static Activity    mActivity;
    private static UMSocialService mController = null;
    private static int             mFromWhere  = -1;
    private static SnsPostListener mListener;

    private class MSnsPostListener implements SnsPostListener {
        private MSnsPostListener() {
        }

        public void onStart() {
        }

        public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
            if (eCode == 200) {
                ShareHelper.this.shareSuccessfully();
                switch (ShareHelper.mFromWhere) {
                    case 0:
                        MobclickAgent.onEvent(ShareHelper.mActivity, Event.STATUS_ADD_SHARE_OK);
                        return;
                    case 1:
                        MobclickAgent.onEvent(ShareHelper.mActivity, Event.MINE_ADD_SHARE_OK);
                        return;
                    default:
                        return;
                }
            }
            Helper.showToast((CharSequence) "分享失败，请重试！");
        }
    }

    public static ShareHelper newInstance(Activity activity, int fromWhere) {
        mActivity = activity;
        mFromWhere = fromWhere;
        if (instance == null) {
            instance = new ShareHelper();
        }
        return instance;
    }

    private ShareHelper() {
        init();
    }

    private void init() {
        mController = UMServiceFactory.getUMSocialService("com.umeng.share");
        if (mListener != null) {
            mController.unregisterListener(mListener);
        }
        mListener = new MSnsPostListener();
        mController.registerListener(mListener);
        new UMWXHandler(mActivity, WX_APPID, WX_APPKEY).addToSocialSDK();
        new UMWXHandler(mActivity, WX_APPID, WX_APPKEY).setToCircle(true).addToSocialSDK();
        new QZoneSsoHandler(mActivity, "100530867", "d32ea174315e9c42bfbd481ac3b3fef6")
                .addToSocialSDK();
        new UMQQSsoHandler(mActivity, "100530867", "d32ea174315e9c42bfbd481ac3b3fef6")
                .addToSocialSDK();
        mController.getConfig().removePlatform(new SHARE_MEDIA[]{SHARE_MEDIA.TENCENT, SHARE_MEDIA
                .RENREN, SHARE_MEDIA.DOUBAN});
        mController.getConfig().setPlatformOrder(new SHARE_MEDIA[]{SHARE_MEDIA.SINA, SHARE_MEDIA
                .WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QZONE, SHARE_MEDIA.QQ});
    }

    public UMSocialService getUMSocialService() {
        init();
        return mController;
    }

    public void setSinaContent(String title, String msg, String contentUrl, UMImage image) {
        SinaShareContent content = new SinaShareContent(image);
        content.setTitle(title);
        if (contentUrl != null) {
            content.setShareContent(msg + " " + contentUrl);
        } else {
            content.setShareContent(msg);
        }
        content.setTargetUrl(contentUrl);
        mController.setShareMedia(content);
    }

    public void setWeixinContent(String title, String desc, String contentUrl, UMImage image) {
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent(desc);
        weixinContent.setTitle(title);
        weixinContent.setTargetUrl(contentUrl);
        weixinContent.setShareImage(image);
        mController.setShareMedia(weixinContent);
    }

    public void setCircleContent(String title, String desc, String contentUrl, UMImage image) {
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(title + desc);
        circleMedia.setTitle(title + desc);
        circleMedia.setShareImage(image);
        circleMedia.setTargetUrl(contentUrl);
        mController.setShareMedia(circleMedia);
    }

    public void setQZoneContent(String msg, String contentUrl, UMImage image) {
        QZoneShareContent qzone = new QZoneShareContent();
        qzone.setShareContent(msg);
        qzone.setTargetUrl(contentUrl);
        qzone.setShareImage(image);
        mController.setShareMedia(qzone);
    }

    public void setQQContent(String msg, String contentUrl, UMImage image) {
        QQShareContent qq = new QQShareContent();
        qq.setShareContent(msg);
        qq.setTargetUrl(contentUrl);
        qq.setShareImage(image);
        mController.setShareMedia(qq);
    }

    public void setShareAll(String title, String desc, String contentUrl, UMImage image) {
        if (title == null) {
            title = "";
        }
        setSinaContent(title, desc, contentUrl, image);
        setWeixinContent(title, desc, contentUrl, image);
        setCircleContent(title, desc, contentUrl, image);
        setQZoneContent(title + desc, contentUrl, image);
        setQQContent(title + desc, contentUrl, image);
    }

    public void openControllerOrder() {
        mController.openShare(mActivity, false);
    }

    private void shareSuccessfully() {
        OneApi.putUserBehaviorShare(mActivity, new JsonCallback(mActivity) {
            public void ok(JSONObject object) {
                super.ok(object);
                Helper.showToast((int) R.string.m2);
            }
        });
    }
}
