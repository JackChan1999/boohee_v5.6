package com.umeng.socialize.weixin.media;

import android.os.Parcel;
import android.os.Parcelable.Creator;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.BaseShareContent;
import com.umeng.socialize.media.UMEmoji;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMediaObject;
import com.umeng.socialize.media.UMusic;

public class WeiXinShareContent extends BaseShareContent {
    public static final Creator<WeiXinShareContent> CREATOR = new Creator<WeiXinShareContent>() {
        public WeiXinShareContent createFromParcel(Parcel in) {
            return new WeiXinShareContent(in);
        }

        public WeiXinShareContent[] newArray(int size) {
            return new WeiXinShareContent[size];
        }
    };

    public WeiXinShareContent(String text) {
        super(text);
    }

    public WeiXinShareContent(UMImage image) {
        super((UMediaObject) image);
    }

    public WeiXinShareContent(UMusic music) {
        super((UMediaObject) music);
    }

    public WeiXinShareContent(UMVideo video) {
        super((UMediaObject) video);
    }

    public WeiXinShareContent(UMEmoji emoji) {
        super((UMediaObject) emoji);
    }

    protected WeiXinShareContent(Parcel in) {
        super(in);
    }

    public String toString() {
        return super.toString() + "WeiXinShareMedia [mTitle=" + this.mTitle + ", mTargetUrl =" +
                this.mTargetUrl + "]";
    }

    public SHARE_MEDIA getTargetPlatform() {
        return SHARE_MEDIA.WEIXIN;
    }
}
