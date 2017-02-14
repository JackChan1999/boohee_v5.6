package com.umeng.socialize.bean;

import android.os.Parcel;
import android.os.Parcelable.Creator;

/* compiled from: BaseMsg */
final class a implements Creator<BaseMsg> {
    a() {
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }

    public BaseMsg a(Parcel parcel) {
        return new BaseMsg(parcel);
    }

    public BaseMsg[] a(int i) {
        return new BaseMsg[i];
    }
}
