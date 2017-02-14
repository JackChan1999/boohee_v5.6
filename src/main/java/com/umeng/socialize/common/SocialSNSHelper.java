package com.umeng.socialize.common;

import android.content.Context;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SnsPlatform;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.common.ResContainer.ResType;
import com.umeng.socialize.utils.LoginInfoHelp;
import com.umeng.socialize.utils.OauthHelper;

import java.util.ArrayList;
import java.util.List;

public class SocialSNSHelper {
    public static final String SOCIALIZE_DOUBAN_KEY          = "douban";
    public static final String SOCIALIZE_EMAIL_KEY           = "email";
    public static final String SOCIALIZE_EVERNOTE_KEY        = "evernote";
    public static final String SOCIALIZE_FACEBOOS_KEY        = "facebook";
    public static final String SOCIALIZE_FLICKR_KEY          = "flickr";
    public static final String SOCIALIZE_FOUR_SQUARE_KEY     = "foursquare";
    public static final String SOCIALIZE_GENERIC_KEY         = "generic";
    public static final String SOCIALIZE_GOOGLE_PLUS_KEY     = "google+";
    public static final String SOCIALIZE_INSTAGRAM_KEY       = "instagram";
    public static final String SOCIALIZE_KAKAO_KEY           = "kakao";
    public static final String SOCIALIZE_LAIWANG_DYNAMIC_KEY = "laiwang_dynamic";
    public static final String SOCIALIZE_LAIWANG_KEY         = "laiwang";
    public static final String SOCIALIZE_LINE_KEY            = "line";
    public static final String SOCIALIZE_LINKED_IN_KEY       = "linkedin";
    public static final String SOCIALIZE_PINTERREST_KEY      = "pinterest";
    public static final String SOCIALIZE_POCKET_KEY          = "pocket";
    public static final String SOCIALIZE_QQ_KEY              = "qq";
    public static final String SOCIALIZE_QZONE_KEY           = "qzone";
    public static final String SOCIALIZE_RENREN_KEY          = "renren";
    public static final String SOCIALIZE_SINA_KEY            = "sina";
    public static final String SOCIALIZE_SMS_KEY             = "sms";
    public static final String SOCIALIZE_TENCENT_KEY         = "tencent";
    public static final String SOCIALIZE_TUMBLR_KEY          = "tumblr";
    public static final String SOCIALIZE_TWITTER_KEY         = "twitter";
    public static final String SOCIALIZE_WEIXIN_CIRCLE_KEY   = "weixin_circle";
    public static final String SOCIALIZE_WEIXIN_KEY          = "weixin";
    public static final String SOCIALIZE_WHATSAPP_KEY        = "whatsapp";
    public static final String SOCIALIZE_YIXIN_CIRCLE_KEY    = "yixin_circle";
    public static final String SOCIALIZE_YIXIN_KEY           = "yixin";
    public static final String SOCIALIZE_YNOTE_KEY           = "ynote";

    public static List<SnsPlatform> getSupprotCloudPlatforms(Context context, SocializeConfig
            socializeConfig) {
        List<SnsPlatform> arrayList = new ArrayList();
        SHARE_MEDIA loginInfo = LoginInfoHelp.getLoginInfo(context);
        int i = 0;
        for (SHARE_MEDIA share_media : socializeConfig.getPlatforms()) {
            SnsPlatform snsPlatform = (SnsPlatform) socializeConfig.getPlatformMap().get
                    (share_media.toString());
            snsPlatform.mIcon = getIconId(context, share_media);
            snsPlatform.mGrayIcon = getGrayIconId(context, share_media);
            snsPlatform.mShowWord = getShowWord(context, share_media);
            try {
                if (OauthHelper.isAuthenticated(context, share_media)) {
                    snsPlatform.mOauth = true;
                    snsPlatform.mUsid = OauthHelper.getUsid(context, share_media);
                }
                if (loginInfo != null && loginInfo == share_media) {
                    snsPlatform.mBind = true;
                }
            } catch (Exception e) {
            }
            int i2 = i + 1;
            snsPlatform.mIndex = i;
            arrayList.add(snsPlatform);
            i = i2;
        }
        return arrayList;
    }

    public static String getShowWord(Context context, SHARE_MEDIA share_media) {
        switch (a.a[share_media.ordinal()]) {
            case 1:
                return context.getString(ResContainer.getResourceId(context, ResType.STRING,
                        "umeng_socialize_text_tencent_key"));
            case 2:
                return context.getString(ResContainer.getResourceId(context, ResType.STRING,
                        "umeng_socialize_text_sina_key"));
            case 3:
                return context.getString(ResContainer.getResourceId(context, ResType.STRING,
                        "umeng_socialize_text_renren_key"));
            case 4:
                return context.getString(ResContainer.getResourceId(context, ResType.STRING,
                        "umeng_socialize_text_douban_key"));
            case 5:
                return context.getString(ResContainer.getResourceId(context, ResType.STRING,
                        "umeng_socialize_text_qq_zone_key"));
            case 6:
                return SOCIALIZE_FACEBOOS_KEY;
            default:
                return "";
        }
    }

    private static int getIconId(Context context, SHARE_MEDIA share_media) {
        switch (a.a[share_media.ordinal()]) {
            case 1:
                return ResContainer.getResourceId(context, ResType.DRAWABLE,
                        "umeng_socialize_tx_on");
            case 2:
                return ResContainer.getResourceId(context, ResType.DRAWABLE,
                        "umeng_socialize_sina_on");
            case 3:
                return ResContainer.getResourceId(context, ResType.DRAWABLE,
                        "umeng_socialize_renren_on");
            case 4:
                return ResContainer.getResourceId(context, ResType.DRAWABLE,
                        "umeng_socialize_douban_on");
            case 5:
                return ResContainer.getResourceId(context, ResType.DRAWABLE,
                        "umeng_socialize_qzone_on");
            default:
                return -1;
        }
    }

    private static int getGrayIconId(Context context, SHARE_MEDIA share_media) {
        switch (a.a[share_media.ordinal()]) {
            case 1:
                return ResContainer.getResourceId(context, ResType.DRAWABLE,
                        "umeng_socialize_tx_off");
            case 2:
                return ResContainer.getResourceId(context, ResType.DRAWABLE,
                        "umeng_socialize_sina_off");
            case 3:
                return ResContainer.getResourceId(context, ResType.DRAWABLE,
                        "umeng_socialize_renren_off");
            case 4:
                return ResContainer.getResourceId(context, ResType.DRAWABLE,
                        "umeng_socialize_douban_off");
            case 5:
                return ResContainer.getResourceId(context, ResType.DRAWABLE,
                        "umeng_socialize_qzone_off");
            default:
                return -1;
        }
    }

    public static String getKeywordByPlatform(SHARE_MEDIA share_media) {
        switch (a.a[share_media.ordinal()]) {
            case 1:
                return "tencent";
            case 2:
                return "sina";
            case 3:
                return SOCIALIZE_RENREN_KEY;
            case 4:
                return SOCIALIZE_DOUBAN_KEY;
            case 5:
                return "qzone";
            case 6:
                return SOCIALIZE_FACEBOOS_KEY;
            case 7:
                return SOCIALIZE_QQ_KEY;
            case 8:
                return "weixin";
            case 9:
                return SOCIALIZE_WEIXIN_CIRCLE_KEY;
            case 10:
                return SOCIALIZE_POCKET_KEY;
            case 11:
                return SOCIALIZE_LINKED_IN_KEY;
            case 12:
                return SOCIALIZE_FOUR_SQUARE_KEY;
            default:
                return null;
        }
    }
}
