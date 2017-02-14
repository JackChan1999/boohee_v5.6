package com.umeng.socialize.sso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.Toast;

import com.baidu.location.l.b;
import com.umeng.socialize.bean.CustomPlatform;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.ShareType;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMWebPage;
import com.umeng.socialize.media.UMediaObject;
import com.umeng.socialize.media.UMediaObject.MediaType;
import com.umeng.socialize.media.UMusic;
import com.umeng.socialize.utils.BitmapUtils;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.SocializeUtils;
import com.umeng.socialize.utils.StatisticsDataUtils;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

public abstract class CustomHandler extends UMSsoHandler {
    protected String          TAG;
    protected boolean         isToCircle;
    protected String          mAppId;
    protected int             mDescriptionLimit;
    protected int             mGrayIcon;
    protected int             mIcon;
    protected String          mKeyWord;
    protected MediaType       mMediaType;
    protected int             mPlatformOpId;
    protected String          mShareContent;
    protected UMediaObject    mShareMedia;
    protected String          mShowWord;
    protected SnsPostListener mSnsPostListener;
    protected int             mThumbLimit;
    protected int             mThumbSize;
    protected String          mTitle;
    protected int             mTitleLimit;

    protected abstract UMediaObject buildMediaObject(UMediaObject uMediaObject);

    protected abstract boolean doShare(Object obj, MediaType mediaType);

    protected abstract Object getShareImage(UMediaObject uMediaObject);

    protected abstract Object getShareMusic(UMediaObject uMediaObject, String str);

    protected abstract Object getShareText(String str);

    protected abstract Object getShareTextAndImage(String str, UMediaObject uMediaObject);

    protected abstract Object getShareVideo(UMediaObject uMediaObject, String str);

    protected abstract void initPlatformConfig();

    public abstract boolean isClientInstalled();

    protected abstract void setSelectedPlatform();

    public CustomHandler(Context context) {
        this(context, "");
    }

    public CustomHandler(Context context, String str) {
        super(context);
        this.mSnsPostListener = null;
        this.mShareContent = "";
        this.mShareMedia = null;
        this.mAppId = "";
        this.mIcon = 0;
        this.mGrayIcon = 0;
        this.mKeyWord = "";
        this.mShowWord = "";
        this.isToCircle = false;
        this.mTitle = "";
        this.mTitleLimit = 512;
        this.mThumbSize = 150;
        this.mThumbLimit = 32768;
        this.mDescriptionLimit = 1024;
        this.mMediaType = null;
        this.TAG = getClass().getSimpleName();
        this.mPlatformOpId = -1;
        this.mAppId = str;
    }

    public String getAppId() {
        return this.mAppId;
    }

    public void setAppId(String str) {
        this.mAppId = str;
    }

    public boolean isToCircle() {
        return this.isToCircle;
    }

    public void setToCircle(boolean z) {
        this.isToCircle = z;
    }

    protected void setPlatformOpId(int i) {
        this.mPlatformOpId = i;
    }

    protected CustomPlatform createNewPlatform() {
        initPlatformConfig();
        this.mCustomPlatform = new CustomPlatform(this.mKeyWord, this.mShowWord, this.mIcon);
        this.mCustomPlatform.mGrayIcon = this.mGrayIcon;
        this.mCustomPlatform.mClickListener = new a(this);
        return this.mCustomPlatform;
    }

    protected void handleOnClick(CustomPlatform customPlatform, SocializeEntity socializeEntity,
                                 SnsPostListener snsPostListener) {
        this.mSnsPostListener = snsPostListener;
        setSelectedPlatform();
        getShareMsg(socializeEntity);
        if (this.mShareMedia == null && TextUtils.isEmpty(this.mShareContent)) {
            Toast.makeText(this.mContext, "请设置" + this.mCustomPlatform.mShowWord + "的分享内容...", 0)
                    .show();
            return;
        }
        listenerCallback(customPlatform, socializeEntity, snsPostListener);
        this.mShareMedia = buildMediaObject(this.mShareMedia);
        prepare(this.mShareContent, this.mShareMedia);
    }

    protected void prepare(String str, UMediaObject uMediaObject) {
        this.mMediaType = getMediaType(uMediaObject);
        if (this.mMediaType != null) {
            shareTo();
            return;
        }
        Toast.makeText(this.mContext, "请设置分享内容...", 0).show();
        Log.e(this.TAG, "您设置的分享内容为空,分享内容只支持文字、图片，图文、音乐、视频、网页类型...");
    }

    public void authorize(Activity activity, UMAuthListener uMAuthListener) {
    }

    public void authorizeCallBack(int i, int i2, Intent intent) {
    }

    protected MediaType getMediaType(UMediaObject uMediaObject) {
        if (uMediaObject != null) {
            MediaType mediaType = uMediaObject.getMediaType();
            if (mediaType != MediaType.IMAGE || TextUtils.isEmpty(this.mShareContent)) {
                return mediaType;
            }
            return MediaType.TEXT_IMAGE;
        } else if (TextUtils.isEmpty(this.mShareContent)) {
            return null;
        } else {
            return MediaType.TEXT;
        }
    }

    protected void getShareMsg(SocializeEntity socializeEntity) {
        if (socializeEntity != null) {
            mEntity = socializeEntity;
            if (mEntity.getShareType() == ShareType.SHAKE) {
                this.mShareContent = mEntity.getShareMsg().mText;
                this.mShareMedia = mEntity.getShareMsg().getMedia();
            } else {
                this.mShareContent = mEntity.getShareContent();
                this.mShareMedia = mEntity.getMedia();
            }
            mEntity.setShareType(ShareType.NORMAL);
        }
    }

    protected void listenerCallback(CustomPlatform customPlatform, SocializeEntity
            socializeEntity, SnsPostListener snsPostListener) {
        if (snsPostListener != null) {
            snsPostListener.onStart();
        }
        fireAllListenersOnStart();
    }

    protected Object createMessage(MediaType mediaType) {
        if (mediaType == MediaType.IMAGE) {
            return getShareImage(this.mShareMedia);
        }
        if (mediaType == MediaType.MUSIC) {
            return getShareMusic(this.mShareMedia, this.mShareContent);
        }
        if (mediaType == MediaType.TEXT) {
            return getShareText(this.mShareContent);
        }
        if (mediaType == MediaType.TEXT_IMAGE) {
            return getShareTextAndImage(this.mShareContent, this.mShareMedia);
        }
        if (mediaType == MediaType.VEDIO) {
            return getShareVideo(this.mShareMedia, this.mShareContent);
        }
        return null;
    }

    protected Bitmap createThumb(Bitmap bitmap, float f) {
        if (bitmap == null || bitmap.isRecycled()) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float f2 = 1.0f;
        if (width < 200 || height < 200) {
            if (width < height) {
                f2 = f / ((float) width);
            } else {
                f2 = f / ((float) height);
            }
        }
        Bitmap createScaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) (((float) width) *
                f2), (int) (f2 * ((float) height)), true);
        if (createScaledBitmap == null) {
            return bitmap;
        }
        return createScaledBitmap;
    }

    protected void sendReport(boolean z) {
        SHARE_MEDIA selectedPlatfrom = SocializeConfig.getSelectedPlatfrom();
        int i = -1;
        if (z) {
            SocializeUtils.sendAnalytic(this.mContext, mEntity.mDescriptor, this.mShareContent,
                    this.mShareMedia, selectedPlatfrom.toString());
            StatisticsDataUtils.addStatisticsData(this.mContext, selectedPlatfrom, this
                    .mPlatformOpId);
            i = 200;
        }
        if (!haveCallback(this.mContext)) {
            fireAllListenersOnComplete(selectedPlatfrom, i, mEntity);
        }
    }

    protected boolean haveCallback(Context context) {
        return false;
    }

    protected byte[] compressBitmap(byte[] bArr) {
        int i = 0;
        if (bArr != null && bArr.length >= this.mThumbLimit) {
            OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Bitmap decodeByteArray = BitmapFactory.decodeByteArray(bArr, 0, bArr.length);
            int i2 = 1;
            while (i == 0 && i2 <= 10) {
                int pow = (int) (Math.pow(b. do,(double) i2)*100.0d);
                Log.d(this.TAG, "quality = " + pow);
                decodeByteArray.compress(CompressFormat.JPEG, pow, byteArrayOutputStream);
                Log.d(this.TAG, "Thumb Size = " + (byteArrayOutputStream.toByteArray().length /
                        1024) + " KB");
                if (byteArrayOutputStream == null || byteArrayOutputStream.size() >= this
                        .mThumbLimit) {
                    byteArrayOutputStream.reset();
                    i2++;
                } else {
                    i = 1;
                }
            }
            if (byteArrayOutputStream != null) {
                bArr = byteArrayOutputStream.toByteArray();
                if (!decodeByteArray.isRecycled()) {
                    decodeByteArray.recycle();
                }
                Log.d(this.TAG, "### 分享" + this.mShowWord + "的缩略图大小 : " + (bArr.length / 1024) +
                        " KB");
                if (bArr.length == 0) {
                    Log.e(this.TAG, "### 缩略图的原图太大,请设置小于64KB的缩略图");
                }
            }
        }
        return bArr;
    }

    protected byte[] getThumbByteArray(UMediaObject uMediaObject) {
        if (uMediaObject == null) {
            return null;
        }
        byte[] bArr;
        Object obj = "";
        if (uMediaObject instanceof UMusic) {
            UMusic uMusic = (UMusic) uMediaObject;
            obj = uMusic.getThumb();
            uMediaObject = uMusic.getThumbImage();
        } else if (uMediaObject instanceof UMVideo) {
            UMVideo uMVideo = (UMVideo) uMediaObject;
            obj = uMVideo.getThumb();
            uMediaObject = uMVideo.getThumbImage();
        } else if (uMediaObject instanceof UMWebPage) {
            UMWebPage uMWebPage = (UMWebPage) uMediaObject;
            obj = uMWebPage.getThumb();
            uMediaObject = uMWebPage.getThumbImage();
        } else if (uMediaObject instanceof UMImage) {
            UMImage uMImage = (UMImage) uMediaObject;
            if (!TextUtils.isEmpty(uMImage.getThumb())) {
                obj = uMImage.getThumb();
            } else if (uMImage.isUrlMedia()) {
                obj = uMImage.toUrl();
            }
        } else {
            uMediaObject = null;
        }
        if (uMediaObject != null && uMediaObject.isUrlMedia()) {
            obj = uMediaObject.toUrl();
        }
        if (!TextUtils.isEmpty(obj)) {
            Bitmap bitmapFromFile = BitmapUtils.getBitmapFromFile(obj, this.mThumbSize, this
                    .mThumbSize);
            if (!(bitmapFromFile == null || bitmapFromFile.isRecycled())) {
                byte[] bitmap2Bytes = BitmapUtils.bitmap2Bytes(bitmapFromFile);
                bitmapFromFile.recycle();
                bArr = bitmap2Bytes;
                if ((bArr == null && bArr.length != 0) || uMediaObject == null || uMediaObject
                        .isUrlMedia()) {
                    return bArr;
                }
                return uMediaObject.toByte();
            }
        }
        bArr = null;
        if (bArr == null) {
        }
        return uMediaObject.toByte();
    }

    protected String buildTransaction(String str) {
        if (str == null) {
            return String.valueOf(System.currentTimeMillis());
        }
        return str + System.currentTimeMillis();
    }

    protected void fireAllListenersOnStart() {
        this.mSocializeConfig.fireAllListenersOnStart(SnsPostListener.class);
    }

    protected void fireAllListenersOnComplete(SHARE_MEDIA share_media, int i, SocializeEntity
            socializeEntity) {
        if (this.mSnsPostListener != null) {
            this.mSnsPostListener.onComplete(share_media, i, mEntity);
        }
        this.mSocializeConfig.fireAllListenersOnComplete(SnsPostListener.class, share_media, i, mEntity);
    }
}
