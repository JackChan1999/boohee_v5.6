package com.umeng.socialize.media;

import android.os.Parcel;
import android.os.Parcelable.Creator;

import com.umeng.socialize.bean.SHARE_MEDIA;

public class GooglePlusShareContent extends BaseShareContent {
    public static final Creator<GooglePlusShareContent> CREATOR = new
            Creator<GooglePlusShareContent>() {
        public GooglePlusShareContent createFromParcel(Parcel parcel) {
            return new GooglePlusShareContent(parcel);
        }

        public GooglePlusShareContent[] newArray(int i) {
            return new GooglePlusShareContent[i];
        }
    };

    public GooglePlusShareContent(String str) {
        super(str);
    }

    public GooglePlusShareContent(UMediaObject uMediaObject) {
        super(uMediaObject);
    }

    protected GooglePlusShareContent(Parcel parcel) {
        super(parcel);
    }

    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
    }

    public String toString() {
        return super.toString() + "GooglePlusShareContent";
    }

    public SHARE_MEDIA getTargetPlatform() {
        return SHARE_MEDIA.GOOGLEPLUS;
    }
}
