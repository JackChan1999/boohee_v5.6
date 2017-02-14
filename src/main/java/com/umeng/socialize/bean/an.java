package com.umeng.socialize.bean;

import android.os.Parcel;
import android.os.Parcelable.Creator;

/* compiled from: UMComment */
final class an implements Creator<UMComment> {
    an() {
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return a(i);
    }

    public UMComment a(Parcel parcel) {
        return new UMComment(parcel);
    }

    public UMComment[] a(int i) {
        return new UMComment[i];
    }
}
