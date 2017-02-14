package com.umeng.socialize.bean;

import android.text.TextUtils;

import com.umeng.socialize.common.SocialSNSHelper;

public enum SHARE_MEDIA {
    GOOGLEPLUS(SocialSNSHelper.SOCIALIZE_GOOGLE_PLUS_KEY),
    GENERIC(SocialSNSHelper.SOCIALIZE_GENERIC_KEY),
    SMS(SocialSNSHelper.SOCIALIZE_SMS_KEY),
    EMAIL("email"),
    SINA("sina"),
    QZONE("qzone"),
    QQ(SocialSNSHelper.SOCIALIZE_QQ_KEY),
    RENREN(SocialSNSHelper.SOCIALIZE_RENREN_KEY),
    WEIXIN("weixin"),
    WEIXIN_CIRCLE(SocialSNSHelper.SOCIALIZE_WEIXIN_CIRCLE_KEY),
    TENCENT("tencent"),
    DOUBAN(SocialSNSHelper.SOCIALIZE_DOUBAN_KEY),
    FACEBOOK(SocialSNSHelper.SOCIALIZE_FACEBOOS_KEY),
    TWITTER(SocialSNSHelper.SOCIALIZE_TWITTER_KEY),
    LAIWANG(SocialSNSHelper.SOCIALIZE_LAIWANG_KEY),
    LAIWANG_DYNAMIC(SocialSNSHelper.SOCIALIZE_LAIWANG_DYNAMIC_KEY),
    YIXIN(SocialSNSHelper.SOCIALIZE_YIXIN_KEY),
    YIXIN_CIRCLE(SocialSNSHelper.SOCIALIZE_YIXIN_CIRCLE_KEY),
    INSTAGRAM(SocialSNSHelper.SOCIALIZE_INSTAGRAM_KEY),
    PINTEREST(SocialSNSHelper.SOCIALIZE_PINTERREST_KEY),
    EVERNOTE(SocialSNSHelper.SOCIALIZE_EVERNOTE_KEY),
    POCKET(SocialSNSHelper.SOCIALIZE_POCKET_KEY),
    LINKEDIN(SocialSNSHelper.SOCIALIZE_LINKED_IN_KEY),
    FOURSQUARE(SocialSNSHelper.SOCIALIZE_FOUR_SQUARE_KEY),
    YNOTE(SocialSNSHelper.SOCIALIZE_YNOTE_KEY),
    WHATSAPP(SocialSNSHelper.SOCIALIZE_WHATSAPP_KEY),
    LINE(SocialSNSHelper.SOCIALIZE_LINE_KEY),
    FLICKR(SocialSNSHelper.SOCIALIZE_FLICKR_KEY),
    TUMBLR(SocialSNSHelper.SOCIALIZE_TUMBLR_KEY),
    KAKAO(SocialSNSHelper.SOCIALIZE_KAKAO_KEY);

    private String a;

    public static SHARE_MEDIA convertToEmun(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (str.equals("wxtimeline")) {
            return WEIXIN_CIRCLE;
        }
        if (str.equals("wxsession")) {
            return WEIXIN;
        }
        for (SHARE_MEDIA share_media : values()) {
            if (share_media.toString().trim().equals(str)) {
                return share_media;
            }
        }
        return null;
    }

    private SHARE_MEDIA(String str) {
        this.a = str;
    }

    public String toString() {
        return this.a;
    }

    public boolean isCustomPlatform() {
        return true;
    }

    public boolean isSupportAuthorization() {
        return false;
    }

    public int getReqCode() {
        return 0;
    }

    public static SHARE_MEDIA[] getDefaultPlatform() {
        return new SHARE_MEDIA[]{SINA, DOUBAN, QZONE, TENCENT, RENREN, SMS, EMAIL, WEIXIN};
    }

    public static SHARE_MEDIA[] getShareMultiPlatforms() {
        return new SHARE_MEDIA[]{SINA, DOUBAN, TENCENT, RENREN};
    }
}
