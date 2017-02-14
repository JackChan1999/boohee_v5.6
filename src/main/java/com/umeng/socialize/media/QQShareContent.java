package com.umeng.socialize.media;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.net.utils.SocializeNetUtils;
import com.umeng.socialize.utils.Log;

public class QQShareContent extends BaseShareContent {
    public static final Creator<QQShareContent> CREATOR = new Creator<QQShareContent>() {
        public QQShareContent createFromParcel(Parcel in) {
            return new QQShareContent(in);
        }

        public QQShareContent[] newArray(int size) {
            return new QQShareContent[size];
        }
    };

    public QQShareContent(String text) {
        super(text);
    }

    public QQShareContent(UMImage image) {
        super((UMediaObject) image);
    }

    public QQShareContent(UMusic music) {
        super((UMediaObject) music);
    }

    public QQShareContent(UMVideo video) {
        super((UMediaObject) video);
    }

    protected QQShareContent(Parcel in) {
        super(in);
    }

    public void setTargetUrl(String targetUrl) {
        if (TextUtils.isEmpty(targetUrl) || !SocializeNetUtils.startWithHttp(targetUrl)) {
            Log.e(this.TAG, "### QQ的targetUrl必须以http://或者https://开头");
        } else {
            this.mTargetUrl = targetUrl;
        }
    }

    public String toString() {
        return super.toString() + "QQShareContent [mTitle=" + this.mTitle + ", mTargetUrl =" +
                this.mTargetUrl + "]";
    }

    public SHARE_MEDIA getTargetPlatform() {
        return SHARE_MEDIA.QQ;
    }
}
