package com.zxinsight.share.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.Toast;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX.Req;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage.IMediaObject;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zxinsight.common.base.a;
import com.zxinsight.common.util.c;
import com.zxinsight.common.util.m;
import com.zxinsight.common.util.o;
import com.zxinsight.share.domain.BMPlatform;
import com.zxinsight.share.domain.b;

public class MWWXEntryActivity implements a {
    private static final int THUMB_SIZE = 150;
    public static b          shareData;
    private       Bitmap     bitmap;
    private       boolean    fromShare;
    private       Activity   mActivity;
    private       IWXAPI     mIWXAPI;
    private       BMPlatform platform;

    public MWWXEntryActivity(Activity activity) {
        this.mActivity = activity;
    }

    public void onCreate() {
        this.platform = (BMPlatform) this.mActivity.getIntent().getExtras().get("platform");
        this.fromShare = this.mActivity.getIntent().getExtras().getBoolean("fromShare");
        String r = m.a().r();
        c.b("MWWXEntryActivity is right， WeChatAppId = " + r);
        if (this.platform == BMPlatform.PLATFORM_WXTIMELINE) {
            this.mIWXAPI = WXAPIFactory.createWXAPI(this.mActivity, r, false);
            this.mIWXAPI.registerApp(r);
        } else {
            this.mIWXAPI = WXAPIFactory.createWXAPI(this.mActivity, r, false);
            this.mIWXAPI.registerApp(r);
        }
        if (this.mIWXAPI.isWXAppInstalled() && this.mIWXAPI.isWXAppSupportAPI()) {
            shareToWx();
            return;
        }
        Toast.makeText(this.mActivity, o.a("未安装微信或者微信版本过低。", "There is no WeChat or the version " +
                "is too low."), 0).show();
        c.d(o.a("未安装微信或者微信版本过低。", "There is no WeChat or the version is too low."));
        this.mActivity.finish();
    }

    protected void shareToWx() {
        if (shareData != null) {
            Bitmap createScaledBitmap;
            WXMediaMessage wXMediaMessage = new WXMediaMessage();
            if (!TextUtils.isEmpty(shareData.c())) {
                this.bitmap = BitmapFactory.decodeFile(shareData.c());
            }
            wXMediaMessage.title = shareData.a();
            wXMediaMessage.description = shareData.b();
            if (this.bitmap != null) {
                createScaledBitmap = Bitmap.createScaledBitmap(this.bitmap, 150, 150, true);
            } else {
                createScaledBitmap = Bitmap.createScaledBitmap(Bitmap.createBitmap(150, 150,
                        Config.ARGB_8888), 150, 150, true);
            }
            IMediaObject wXTextObject;
            if (TextUtils.isEmpty(shareData.c()) && TextUtils.isEmpty(shareData.d()) && TextUtils
                    .isEmpty(shareData.e())) {
                wXTextObject = new WXTextObject();
                wXTextObject.text = shareData.b();
                wXMediaMessage.mediaObject = wXTextObject;
            } else if (!TextUtils.isEmpty(shareData.c()) && TextUtils.isEmpty(shareData.d())) {
                r2 = new WXImageObject();
                r2.imagePath = shareData.c();
                wXMediaMessage.mediaObject = r2;
                wXMediaMessage.setThumbImage(createScaledBitmap);
            } else if (!TextUtils.isEmpty(shareData.d()) && TextUtils.isEmpty(shareData.e())) {
                r2 = new WXWebpageObject();
                r2.webpageUrl = shareData.d();
                wXMediaMessage.mediaObject = r2;
                wXMediaMessage.setThumbImage(createScaledBitmap);
            } else if (!TextUtils.isEmpty(shareData.e()) && TextUtils.isEmpty(shareData.c()) &&
                    TextUtils.isEmpty(shareData.d())) {
                wXTextObject = new WXWebpageObject();
                wXTextObject.webpageUrl = shareData.e();
                wXMediaMessage.mediaObject = wXTextObject;
            } else {
                wXMediaMessage.setThumbImage(createScaledBitmap);
                wXTextObject = new WXWebpageObject();
                wXTextObject.webpageUrl = shareData.e();
                wXMediaMessage.mediaObject = wXTextObject;
            }
            BaseReq req = new Req();
            req.transaction = buildTransaction("magic_window");
            req.message = wXMediaMessage;
            if (this.fromShare) {
                if (this.platform == BMPlatform.PLATFORM_WXTIMELINE) {
                    req.scene = 1;
                } else {
                    req.scene = 0;
                }
                this.mIWXAPI.sendReq(req);
                this.mActivity.finish();
            }
        }
    }

    public void onNewIntent(Intent intent) {
    }

    public void onActivityResult(int i, int i2, Intent intent) {
    }

    protected String buildTransaction(String str) {
        return str == null ? String.valueOf(System.currentTimeMillis()) : str;
    }

    public void onRestart() {
        o.d();
        this.mActivity.finish();
    }

    public void onBackPressed() {
    }

    public void onPause() {
    }

    public void onResume() {
    }

    public void onDestroy() {
        shareData = null;
    }
}
