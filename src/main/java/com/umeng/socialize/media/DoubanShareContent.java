package com.umeng.socialize.media;

import android.os.Parcel;
import android.os.Parcelable.Creator;

import com.umeng.socialize.bean.SHARE_MEDIA;

public class DoubanShareContent extends BaseShareContent {
    public static final Creator<DoubanShareContent> CREATOR = new Creator<DoubanShareContent>() {
        public DoubanShareContent createFromParcel(Parcel parcel) {
            return new DoubanShareContent(parcel);
        }

        public DoubanShareContent[] newArray(int i) {
            return new DoubanShareContent[i];
        }
    };

    public DoubanShareContent(String str) {
        super(str);
    }

    public DoubanShareContent(UMediaObject uMediaObject) {
        super(uMediaObject);
    }

    protected DoubanShareContent(Parcel parcel) {
        super(parcel);
    }

    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
    }

    public String toString() {
        return super.toString() + "DoubanShareContent";
    }

    public SHARE_MEDIA getTargetPlatform() {
        return SHARE_MEDIA.DOUBAN;
    }

    public int describeContents() {
        return 0;
    }
}
