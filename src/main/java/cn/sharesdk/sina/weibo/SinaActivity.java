package cn.sharesdk.sina.weibo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.authorize.AuthorizeListener;
import com.boohee.widgets.PathListView;
import com.mob.tools.utils.BitmapHelper;
import com.mob.tools.utils.Ln;
import com.mob.tools.utils.R;
import com.mob.tools.utils.UIHandler;
import com.sina.weibo.sdk.api.BaseMediaObject;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler.Response;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.utils.Utility;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class SinaActivity extends Activity implements Callback, Response, WeiboAuthListener {
    private static ShareParams f;
    private static AuthorizeListener g;
    private String a;
    private long b;
    private SsoHandler c;
    private AuthInfo d;
    private IWeiboShareAPI e;

    private int a(Bitmap bitmap, CompressFormat compressFormat) {
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(compressFormat, 100, byteArrayOutputStream);
        int size = byteArrayOutputStream.size();
        byteArrayOutputStream.close();
        return size;
    }

    private Bitmap a(Context context, Bitmap bitmap) {
        try {
            File createTempFile = File.createTempFile("bm_tmp", ".jpg");
            OutputStream fileOutputStream = new FileOutputStream(createTempFile);
            bitmap.compress(CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            return a(context, createTempFile.getAbsolutePath());
        } catch (Throwable th) {
            Ln.e(th);
            return null;
        }
    }

    private Bitmap a(Context context, String str) {
        File file = new File(str);
        if (file.exists()) {
            CompressFormat bmpFormat = BitmapHelper.getBmpFormat(str);
            int dipToPx = R.dipToPx(context, 120);
            if (CompressFormat.PNG == bmpFormat) {
                dipToPx = R.dipToPx(context, 90);
            }
            Bitmap decodeFile = BitmapFactory.decodeFile(str, new Options());
            if (file.length() > this.b) {
                Bitmap bitmap = decodeFile;
                while (dipToPx > 40 && a(bitmap, bmpFormat) > 32768) {
                    int i = dipToPx - 5;
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    double d = (height > i || width > i) ? height > width ? ((double) i) / ((double) height) : ((double) i) / ((double) width) : PathListView.NO_ZOOM;
                    bitmap = Bitmap.createScaledBitmap(bitmap, (int) (((double) width) * d), (int) (d * ((double) height)), true);
                    dipToPx = i;
                }
                OutputStream fileOutputStream = new FileOutputStream(File.createTempFile("sina_bm_tmp", "." + bmpFormat.name().toLowerCase()));
                bitmap.compress(bmpFormat, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                return bitmap;
            }
            Ln.i("sina weibo decode bitmap size ==>>" + a(decodeFile, bmpFormat), new Object[0]);
            return decodeFile;
        }
        throw new FileNotFoundException();
    }

    private void a() {
        WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();
        if (!TextUtils.isEmpty(f.getText())) {
            weiboMultiMessage.textObject = d();
        }
        if (!(f.getImageData() == null && TextUtils.isEmpty(f.getImagePath()))) {
            weiboMultiMessage.imageObject = c();
        }
        if (!TextUtils.isEmpty(f.getUrl())) {
            weiboMultiMessage.mediaObject = b();
        }
        SendMultiMessageToWeiboRequest sendMultiMessageToWeiboRequest = new SendMultiMessageToWeiboRequest();
        sendMultiMessageToWeiboRequest.transaction = String.valueOf(System.currentTimeMillis());
        sendMultiMessageToWeiboRequest.multiMessage = weiboMultiMessage;
        this.e.sendRequest(this, sendMultiMessageToWeiboRequest, this.d, this.a, this);
    }

    public static void a(ShareParams shareParams) {
        f = shareParams;
    }

    public static void a(AuthorizeListener authorizeListener) {
        g = authorizeListener;
    }

    private BaseMediaObject b() {
        WebpageObject webpageObject = new WebpageObject();
        webpageObject.identify = Utility.generateGUID();
        webpageObject.title = f.getTitle();
        webpageObject.description = f.getText();
        try {
            this.b = 32768;
            if (f.getImageData() != null) {
                webpageObject.setThumbImage(a((Context) this, f.getImageData()));
            } else if (!TextUtils.isEmpty(f.getImagePath())) {
                webpageObject.setThumbImage(a((Context) this, f.getImagePath()));
            }
        } catch (Throwable th) {
            Ln.e(th);
            webpageObject.setThumbImage(null);
        }
        webpageObject.actionUrl = f.getUrl();
        webpageObject.defaultText = f.getText();
        return webpageObject;
    }

    private ImageObject c() {
        ImageObject imageObject = new ImageObject();
        try {
            if (f.getImageData() != null) {
                this.b = 2097152;
                imageObject.setImageObject(a((Context) this, f.getImageData()));
                return imageObject;
            } else if (TextUtils.isEmpty(f.getImagePath())) {
                return imageObject;
            } else {
                this.b = 10485760;
                imageObject.setImageObject(a((Context) this, f.getImagePath()));
                return imageObject;
            }
        } catch (Throwable th) {
            Ln.e(th);
            return null;
        }
    }

    private TextObject d() {
        TextObject textObject = new TextObject();
        textObject.text = f.getText();
        return textObject;
    }

    private void e() {
        UIHandler.sendEmptyMessageDelayed(1, 200, this);
    }

    public boolean handleMessage(Message message) {
        if (message.what == 1) {
            finish();
        }
        return false;
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (this.c != null) {
            this.c.authorizeCallBack(i, i2, intent);
        }
        e();
    }

    public void onCancel() {
        e();
        if (g != null) {
            g.onCancel();
        }
    }

    public void onComplete(Bundle bundle) {
        e();
        if (g != null) {
            g.onComplete(bundle);
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle extras = getIntent().getExtras();
        int i = extras.getInt("action");
        String string = extras.getString("appkey");
        String string2 = extras.getString("redirectUrl");
        String string3 = extras.getString("permissions");
        boolean z = extras.getBoolean("isUserClient");
        if (string3 == null) {
            string3 = "";
        }
        this.d = new AuthInfo(this, string, string2, string3);
        this.c = new SsoHandler(this, this.d);
        if (i == 1) {
            if (z) {
                this.c.authorize(this);
            } else {
                this.c.authorizeWeb(this);
            }
        } else if (i == 2) {
            this.a = extras.getString("token");
            this.e = WeiboShareSDK.createWeiboAPI(this, string);
            this.e.registerApp();
            a();
        } else {
            e();
        }
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Ln.i("onNewIntent ==>>", intent.getExtras().toString());
        this.e.handleWeiboResponse(intent, this);
    }

    public void onResponse(BaseResponse baseResponse) {
        e();
        switch (baseResponse.errCode) {
            case 0:
                if (g != null) {
                    g.onComplete(null);
                    return;
                }
                return;
            case 1:
                if (g != null) {
                    g.onCancel();
                    return;
                }
                return;
            case 2:
                if (g != null) {
                    g.onError(new Throwable());
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void onWeiboException(WeiboException weiboException) {
        e();
        if (g != null) {
            g.onError(weiboException);
        }
    }
}
