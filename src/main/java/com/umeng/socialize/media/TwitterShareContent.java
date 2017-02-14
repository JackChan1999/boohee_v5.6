package com.umeng.socialize.media;

import android.os.Parcel;
import android.os.Parcelable.Creator;

import com.umeng.socialize.bean.SHARE_MEDIA;

public class TwitterShareContent extends BaseShareContent {
    public static final Creator<TwitterShareContent> CREATOR = new Creator<TwitterShareContent>() {
        public TwitterShareContent createFromParcel(Parcel parcel) {
            return new TwitterShareContent(parcel);
        }

        public TwitterShareContent[] newArray(int i) {
            return new TwitterShareContent[i];
        }
    };

    public TwitterShareContent(String str) {
        super(str);
    }

    public TwitterShareContent(UMImage uMImage) {
        super((UMediaObject) uMImage);
    }

    protected TwitterShareContent(Parcel parcel) {
        super(parcel);
    }

    public SHARE_MEDIA getTargetPlatform() {
        return SHARE_MEDIA.TWITTER;
    }
}
