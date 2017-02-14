package com.umeng.socialize.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;

import com.umeng.socialize.media.UMediaObject;
import com.umeng.socialize.utils.Log;

public class BaseMsg implements Parcelable {
    public static final Creator<BaseMsg> CREATOR = new a();
    protected           UMediaObject     a       = null;
    public UMLocation mLocation;
    public String mText = "";

    public void setMediaData(UMediaObject uMediaObject) {
        this.a = uMediaObject;
    }

    public UMediaObject getMedia() {
        return this.a;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mText);
        parcel.writeParcelable(this.mLocation, 1);
        parcel.writeString(this.a == null ? "" : this.a.getClass().getName());
        parcel.writeParcelable(this.a, 1);
    }

    protected BaseMsg(Parcel parcel) {
        this.mText = parcel.readString();
        this.mLocation = (UMLocation) parcel.readParcelable(UMLocation.class.getClassLoader());
        Object readString = parcel.readString();
        if (TextUtils.isEmpty(readString)) {
            parcel.readString();
            return;
        }
        try {
            this.a = (UMediaObject) parcel.readParcelable(Class.forName(readString)
                    .getClassLoader());
        } catch (ClassNotFoundException e) {
            Log.d(e.toString());
        }
    }
}
