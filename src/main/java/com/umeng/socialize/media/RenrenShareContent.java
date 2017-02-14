package com.umeng.socialize.media;

import android.os.Parcel;
import android.os.Parcelable.Creator;

import com.umeng.socialize.bean.SHARE_MEDIA;

public class RenrenShareContent extends BaseShareContent {
    public static final Creator<RenrenShareContent> CREATOR = new Creator<RenrenShareContent>() {
        public RenrenShareContent createFromParcel(Parcel parcel) {
            return new RenrenShareContent(parcel);
        }

        public RenrenShareContent[] newArray(int i) {
            return new RenrenShareContent[i];
        }
    };

    public RenrenShareContent(String str) {
        super(str);
    }

    public RenrenShareContent(UMediaObject uMediaObject) {
        super(uMediaObject);
    }

    protected RenrenShareContent(Parcel parcel) {
        super(parcel);
    }

    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
    }

    public String toString() {
        return super.toString() + "RenrenShareMedia";
    }

    public SHARE_MEDIA getTargetPlatform() {
        return SHARE_MEDIA.RENREN;
    }
}
