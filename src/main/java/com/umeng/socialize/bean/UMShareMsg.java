package com.umeng.socialize.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class UMShareMsg extends BaseMsg implements Parcelable {
    public static final Creator<UMShareMsg> CREATOR = new ap();
    public String mWeiBoId;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(this.mWeiBoId);
    }

    private UMShareMsg(Parcel parcel) {
        super(parcel);
        this.mWeiBoId = parcel.readString();
    }
}
