package com.zxinsight.share.d;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.zxinsight.CustomStyle;
import com.zxinsight.common.util.o;
import com.zxinsight.share.domain.BMPlatform;

public class c {
    public static Bitmap a(String str, Context context, String str2) {
        if (BMPlatform.NAME_WXSESSION.equals(str)) {
            if (CustomStyle.getSocialShareKit(str2) == 0) {
                return BitmapFactory.decodeStream(context.getAssets().open("mw_wxsession.png"));
            }
            return BitmapFactory.decodeStream(context.getAssets().open("mw_wxsession1.png"));
        } else if (BMPlatform.NAME_WXTIMELINE.equals(str)) {
            if (CustomStyle.getSocialShareKit(str2) == 0) {
                return BitmapFactory.decodeStream(context.getAssets().open("mw_wxtimeline.png"));
            }
            return BitmapFactory.decodeStream(context.getAssets().open("mw_wxtimeline1.png"));
        } else if (BMPlatform.NAME_SINAWEIBO.equals(str)) {
            return BitmapFactory.decodeStream(context.getAssets().open("mw_xinlangact.png"));
        } else {
            if ("QQ".equals(str)) {
                return BitmapFactory.decodeStream(context.getAssets().open("mw_qqact.png"));
            }
            if (BMPlatform.NAME_QZONE.equals(str)) {
                return BitmapFactory.decodeStream(context.getAssets().open("mw_qqkjact.png"));
            }
            if (BMPlatform.NAME_TENCENTWEIBO.equals(str)) {
                return BitmapFactory.decodeStream(context.getAssets().open("mw_tengxunact.png"));
            }
            if (BMPlatform.NAME_RENN.equals(str)) {
                return BitmapFactory.decodeStream(context.getAssets().open("mw_renrenact.png"));
            }
            if (BMPlatform.NAME_MESSAGE.equals(str)) {
                return BitmapFactory.decodeStream(context.getAssets().open("mw_messact.png"));
            }
            if (BMPlatform.NAME_EMAIL.equals(str)) {
                return BitmapFactory.decodeStream(context.getAssets().open("mw_mailact.png"));
            }
            if (BMPlatform.NAME_COPYLINK.equals(str)) {
                return BitmapFactory.decodeStream(context.getAssets().open("mw_lianjieact.png"));
            }
            if (BMPlatform.NAME_MORE_SHARE.equals(str)) {
                return BitmapFactory.decodeStream(context.getAssets().open("mw_more.png"));
            }
            return BitmapFactory.decodeStream(context.getAssets().open("mw_more.png"));
        }
    }

    public static String a(String str) {
        if (BMPlatform.NAME_WXSESSION.equals(str)) {
            return o.a("微信好友", "Friends");
        }
        if (BMPlatform.NAME_WXTIMELINE.equals(str)) {
            return o.a("微信朋友圈", "Moments");
        }
        if (BMPlatform.NAME_SINAWEIBO.equals(str)) {
            return "新浪微博";
        }
        if ("QQ".equals(str)) {
            return "QQ好友";
        }
        if (BMPlatform.NAME_QZONE.equals(str)) {
            return "QQ空间";
        }
        if (BMPlatform.NAME_TENCENTWEIBO.equals(str)) {
            return "腾讯微博";
        }
        if (BMPlatform.NAME_RENN.equals(str)) {
            return "人人网";
        }
        if (BMPlatform.NAME_MESSAGE.equals(str)) {
            return "短信";
        }
        if (BMPlatform.NAME_EMAIL.equals(str)) {
            return "邮件";
        }
        if (BMPlatform.NAME_MORE_SHARE.equals(str)) {
            return "更多";
        }
        if (BMPlatform.NAME_COPYLINK.equals(str)) {
            return "复制链接";
        }
        return "";
    }
}
