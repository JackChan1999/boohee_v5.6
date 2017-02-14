package com.huewu.pla.lib;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.SparseArray;
import android.util.SparseIntArray;

public class ParcelableSparseIntArray extends SparseIntArray implements Parcelable {
    public static final Creator<ParcelableSparseIntArray> CREATOR = new
            Creator<ParcelableSparseIntArray>() {
        public ParcelableSparseIntArray createFromParcel(Parcel source) {
            return new ParcelableSparseIntArray(source);
        }

        public ParcelableSparseIntArray[] newArray(int size) {
            return new ParcelableSparseIntArray[size];
        }
    };

    public ParcelableSparseIntArray(int initialCapacity) {
        super(initialCapacity);
    }

    private ParcelableSparseIntArray(Parcel in) {
        append(in.readSparseArray(ClassLoader.getSystemClassLoader()));
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSparseArray(toSparseArray());
    }

    private SparseArray<Object> toSparseArray() {
        SparseArray<Object> sparseArray = new SparseArray();
        int size = size();
        for (int i = 0; i < size; i++) {
            sparseArray.append(keyAt(i), Integer.valueOf(valueAt(i)));
        }
        return sparseArray;
    }

    private void append(SparseArray<Integer> sparseArray) {
        int size = sparseArray.size();
        for (int i = 0; i < size; i++) {
            put(sparseArray.keyAt(i), ((Integer) sparseArray.valueAt(i)).intValue());
        }
    }
}
