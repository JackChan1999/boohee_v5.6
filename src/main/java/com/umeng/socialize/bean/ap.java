package com.umeng.socialize.bean;

import android.os.Parcel;
import android.os.Parcelable.Creator;

/* compiled from: UMShareMsg */
final class ap implements Creator<UMShareMsg> {
    ap() {
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }

    public UMShareMsg a(Parcel parcel) {
        return new UMShareMsg(parcel);
    }

    public UMShareMsg[] a(int i) {
        return new UMShareMsg[i];
    }
}
