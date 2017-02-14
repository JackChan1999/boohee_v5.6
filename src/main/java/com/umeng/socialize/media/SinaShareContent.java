package com.umeng.socialize.media;

import android.os.Parcel;
import android.os.Parcelable.Creator;

import com.umeng.socialize.bean.SHARE_MEDIA;

public class SinaShareContent extends BaseShareContent {
    public static final Creator<SinaShareContent> CREATOR = new Creator<SinaShareContent>() {
        public SinaShareContent createFromParcel(Parcel parcel) {
            return new SinaShareContent(parcel);
        }

        public SinaShareContent[] newArray(int i) {
            return new SinaShareContent[i];
        }
    };

    public SinaShareContent(String str) {
        super(str);
    }

    public SinaShareContent(UMediaObject uMediaObject) {
        super(uMediaObject);
    }

    protected SinaShareContent(Parcel parcel) {
        super(parcel);
    }

    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
    }

    public String toString() {
        return super.toString() + "SinaShareContent";
    }

    public SHARE_MEDIA getTargetPlatform() {
        return SHARE_MEDIA.SINA;
    }
}
