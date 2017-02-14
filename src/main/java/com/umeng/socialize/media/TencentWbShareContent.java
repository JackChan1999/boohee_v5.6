package com.umeng.socialize.media;

import android.os.Parcel;
import android.os.Parcelable.Creator;

import com.umeng.socialize.bean.SHARE_MEDIA;

public class TencentWbShareContent extends BaseShareContent {
    public static final Creator<TencentWbShareContent> CREATOR = new
            Creator<TencentWbShareContent>() {
        public TencentWbShareContent createFromParcel(Parcel parcel) {
            return new TencentWbShareContent(parcel);
        }

        public TencentWbShareContent[] newArray(int i) {
            return new TencentWbShareContent[i];
        }
    };

    public TencentWbShareContent(String str) {
        super(str);
    }

    public TencentWbShareContent(UMediaObject uMediaObject) {
        super(uMediaObject);
    }

    protected TencentWbShareContent(Parcel parcel) {
        super(parcel);
    }

    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
    }

    public String toString() {
        return super.toString() + "TencentWbShareContent";
    }

    public SHARE_MEDIA getTargetPlatform() {
        return SHARE_MEDIA.TENCENT;
    }
}
