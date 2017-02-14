package cn.sharesdk.onekeyshare;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.text.TextUtils;
import cn.sharesdk.framework.CustomPlatform;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.ShareSDK;
import com.mob.tools.utils.R;
import com.zxinsight.share.domain.BMPlatform;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

public class ShareCore {
    private ShareContentCustomizeCallback customizeCallback;

    public void setShareContentCustomizeCallback(ShareContentCustomizeCallback callback) {
        this.customizeCallback = callback;
    }

    public boolean share(Platform plat, HashMap<String, Object> data) {
        if (plat == null || data == null) {
            return false;
        }
        try {
            Bitmap viewToShare = (Bitmap) data.get("viewToShare");
            if (!(!TextUtils.isEmpty((String) data.get("imagePath")) || viewToShare == null || viewToShare.isRecycled())) {
                File ss = new File(R.getCachePath(plat.getContext(), "screenshot"), String.valueOf(System.currentTimeMillis()) + ".jpg");
                FileOutputStream fos = new FileOutputStream(ss);
                viewToShare.compress(CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
                data.put("imagePath", ss.getAbsolutePath());
            }
            ShareParams sp = new ShareParams((HashMap) data);
            if (this.customizeCallback != null) {
                this.customizeCallback.onShare(plat, sp);
            }
            plat.share(sp);
            return true;
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }

    public static boolean isUseClientToShare(String platform) {
        if ("Wechat".equals(platform) || "WechatMoments".equals(platform) || "WechatFavorite".equals(platform) || BMPlatform.NAME_MESSAGE.equals(platform) || BMPlatform.NAME_EMAIL.equals(platform) || "GooglePlus".equals(platform) || "QQ".equals(platform) || "Pinterest".equals(platform) || "Instagram".equals(platform) || "Yixin".equals(platform) || "YixinMoments".equals(platform) || BMPlatform.NAME_QZONE.equals(platform) || "Mingdao".equals(platform) || "Line".equals(platform) || "KakaoStory".equals(platform) || "KakaoTalk".equals(platform) || "Bluetooth".equals(platform) || "WhatsApp".equals(platform) || "BaiduTieba".equals(platform) || "Laiwang".equals(platform) || "LaiwangMoments".equals(platform)) {
            return true;
        }
        if ("Evernote".equals(platform)) {
            if ("true".equals(ShareSDK.getPlatform(platform).getDevinfo("ShareByAppClient"))) {
                return true;
            }
        } else if (BMPlatform.NAME_SINAWEIBO.equals(platform)) {
            Platform plat = ShareSDK.getPlatform(platform);
            if ("true".equals(plat.getDevinfo("ShareByAppClient"))) {
                Intent test = new Intent("android.intent.action.SEND");
                test.setPackage("com.sina.weibo");
                test.setType("image/*");
                if (plat.getContext().getPackageManager().resolveActivity(test, 0) == null) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    public static boolean canAuthorize(Context context, String platform) {
        return !("WechatMoments".equals(platform) || "WechatFavorite".equals(platform) || BMPlatform.NAME_MESSAGE.equals(platform) || BMPlatform.NAME_EMAIL.equals(platform) || "Pinterest".equals(platform) || "Yixin".equals(platform) || "YixinMoments".equals(platform) || "Line".equals(platform) || "Bluetooth".equals(platform) || "WhatsApp".equals(platform) || "BaiduTieba".equals(platform)) || "Laiwang".equals(platform) || "LaiwangMoments".equals(platform);
    }

    public static boolean canGetUserInfo(Context context, String platform) {
        return ("WechatMoments".equals(platform) || "WechatFavorite".equals(platform) || BMPlatform.NAME_MESSAGE.equals(platform) || BMPlatform.NAME_EMAIL.equals(platform) || "Pinterest".equals(platform) || "Yixin".equals(platform) || "YixinMoments".equals(platform) || "Line".equals(platform) || "Bluetooth".equals(platform) || "WhatsApp".equals(platform) || "Pocket".equals(platform) || "BaiduTieba".equals(platform) || "Laiwang".equals(platform) || "LaiwangMoments".equals(platform)) ? false : true;
    }

    public static boolean isDirectShare(Platform platform) {
        return (platform instanceof CustomPlatform) || isUseClientToShare(platform.getName());
    }
}
