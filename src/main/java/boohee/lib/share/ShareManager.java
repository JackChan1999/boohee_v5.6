package boohee.lib.share;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import cn.sharesdk.framework.ShareSDK;

public class ShareManager {
    static              String DEF_IMAGE_URL = null;
    private static      String DEF_TITLE     = null;
    public static final String DEF_URL       = "http://www.boohee.com";
    private static IShare mShare;

    public static void init(Context context, String defImageUrl) {
        if (context == null) {
            throw new RuntimeException("Context 不能为null！");
        }
        DEF_IMAGE_URL = defImageUrl;
        DEF_TITLE = context.getResources().getString(R.string.share_title);
        ShareSDK.initSDK(context);
        mShare = ShareSDKHelper.newIntance();
    }

    private static boolean isInit() {
        Log.e("ShareLib", mShare == null ? "请先初始化！" : "ShareLib初始化成功！");
        return mShare != null;
    }

    public static void share(Context context, String content) {
        if (isInit()) {
            Context context2 = context;
            String str = content;
            mShare.share(context2, "分享", str, "", "", "");
        }
    }

    public static void share(Context context, String title, String content) {
        share(context, title, content, DEF_IMAGE_URL);
    }

    public static void share(Context context, String title, String content, String image) {
        if (TextUtils.isEmpty(image) || image.contains("http")) {
            Context context2 = context;
            String str = title;
            String str2 = content;
            share(context2, str, str2, "http://www.boohee.com", image, "");
            return;
        }
        context2 = context;
        str = title;
        str2 = content;
        share(context2, str, str2, "http://www.boohee.com", "", image);
    }

    public static void share(Context context, String title, String content, String url, String
            image) {
        if (TextUtils.isEmpty(image) || image.contains("http")) {
            share(context, title, content, url, image, "");
            return;
        }
        Context context2 = context;
        String str = title;
        String str2 = content;
        String str3 = url;
        share(context2, str, str2, str3, "", image);
    }

    public static void share(Context context, String title, String content, String[] picturePath) {
        share(context, title, content, "http://www.boohee.com", "", picturePath);
    }

    public static void share(Context context, String title, String content, String url, String[]
            picturePath) {
        share(context, title, content, url, "", picturePath);
    }

    public static void shareWithImage(Context context, String title, String content, String image) {
        if (TextUtils.isEmpty(image) || image.contains("http")) {
            Context context2 = context;
            String str = title;
            String str2 = content;
            shareWithImage(context2, str, str2, "", image, "");
            return;
        }
        context2 = context;
        str = title;
        str2 = content;
        shareWithImage(context2, str, str2, "", "", image);
    }

    private static void shareWithImage(Context context, String title, String content, String url,
                                       String imageUrl, String... picturePath) {
        if (isInit()) {
            if (TextUtils.isEmpty(title)) {
                title = DEF_TITLE;
            }
            if (TextUtils.isEmpty(imageUrl)) {
                imageUrl = DEF_IMAGE_URL;
            }
            mShare.share(context, title, content, url, imageUrl, picturePath);
        }
    }

    public static void share(Context context, String title, String content, String url, String
            imageUrl, String... picturePath) {
        if (isInit()) {
            if (TextUtils.isEmpty(title)) {
                title = DEF_TITLE;
            }
            if (TextUtils.isEmpty(url)) {
                url = "http://www.boohee.com";
            }
            if (TextUtils.isEmpty(imageUrl)) {
                imageUrl = DEF_IMAGE_URL;
            }
            mShare.share(context, title, content, url, imageUrl, picturePath);
        }
    }

    public static void shareLocalImage(Context context, String... picturePath) {
        if (isInit()) {
            mShare.share(context, "", "", "", "", picturePath);
        }
    }
}
