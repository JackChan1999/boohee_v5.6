package com.umeng.socialize.weixin.media;

import android.os.Parcel;
import android.os.Parcelable.Creator;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.BaseShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMediaObject;
import com.umeng.socialize.media.UMusic;

public class CircleShareContent extends BaseShareContent {
    public static final Creator<CircleShareContent> CREATOR = new Creator<CircleShareContent>() {
        public CircleShareContent createFromParcel(Parcel in) {
            return new CircleShareContent(in);
        }

        public CircleShareContent[] newArray(int size) {
            return new CircleShareContent[size];
        }
    };

    public CircleShareContent(String text) {
        super(text);
    }

    public CircleShareContent(UMImage image) {
        super((UMediaObject) image);
    }

    public CircleShareContent(UMusic music) {
        super((UMediaObject) music);
    }

    public CircleShareContent(UMVideo video) {
        super((UMediaObject) video);
    }

    protected CircleShareContent(Parcel in) {
        super(in);
    }

    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    public String toString() {
        return super.toString() + "CircleShareContent [mTitle=" + this.mTitle + ", mTargetUrl ="
                + this.mTargetUrl + "]";
    }

    public SHARE_MEDIA getTargetPlatform() {
        return SHARE_MEDIA.WEIXIN_CIRCLE;
    }
}
