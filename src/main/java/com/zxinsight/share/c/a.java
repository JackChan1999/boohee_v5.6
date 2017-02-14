package com.zxinsight.share.c;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.Toast;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.zxinsight.common.util.c;
import com.zxinsight.common.util.o;
import com.zxinsight.share.domain.b;

public class a {
    public static void a(Context context, b bVar) {
        ShareParams shareParams = new ShareParams();
        shareParams.setShareType(4);
        Bitmap bitmap = null;
        if (!TextUtils.isEmpty(bVar.c())) {
            bitmap = BitmapFactory.decodeFile(bVar.c());
        }
        if (bitmap != null) {
            bitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
        } else {
            bitmap = Bitmap.createScaledBitmap(Bitmap.createBitmap(150, 150, Config.ARGB_8888),
                    150, 150, true);
        }
        shareParams.setImageData(bitmap);
        shareParams.setTitle(bVar.a());
        shareParams.setText(bVar.b());
        shareParams.setUrl(bVar.e());
        Platform platform = ShareSDK.getPlatform(Wechat.NAME);
        if (platform.isClientValid()) {
            platform.share(shareParams);
            return;
        }
        Toast.makeText(context, o.a("未安装微信或者微信版本过低。", "There is no WeChat or the version is too " +
                "low."), 0).show();
        c.d(o.a("未安装微信或者微信版本过低。", "There is no WeChat or the version is too low."));
    }

    public static void b(Context context, b bVar) {
        ShareParams shareParams = new ShareParams();
        shareParams.setShareType(4);
        Bitmap bitmap = null;
        if (!TextUtils.isEmpty(bVar.c())) {
            bitmap = BitmapFactory.decodeFile(bVar.c());
        }
        if (bitmap != null) {
            bitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
        } else {
            bitmap = Bitmap.createScaledBitmap(Bitmap.createBitmap(150, 150, Config.ARGB_8888),
                    150, 150, true);
        }
        shareParams.setImageData(bitmap);
        shareParams.setTitle(bVar.a());
        shareParams.setText(bVar.b());
        shareParams.setUrl(bVar.e());
        Platform platform = ShareSDK.getPlatform(WechatMoments.NAME);
        if (platform.isClientValid()) {
            platform.share(shareParams);
            return;
        }
        Toast.makeText(context, o.a("未安装微信或者微信版本过低。", "There is no WeChat or the version is too " +
                "low."), 0).show();
        c.d(o.a("未安装微信或者微信版本过低。", "There is no WeChat or the version is too low."));
    }
}
