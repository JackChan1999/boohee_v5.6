package com.umeng.socialize.bean;

import android.os.Parcel;
import android.os.Parcelable.Creator;

/* compiled from: UMLocation */
final class ao implements Creator<UMLocation> {
    ao() {
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }

    public UMLocation a(Parcel parcel) {
        return new UMLocation(parcel);
    }

    public UMLocation[] a(int i) {
        return new UMLocation[i];
    }
}
