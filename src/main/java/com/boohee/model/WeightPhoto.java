package com.boohee.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class WeightPhoto implements Parcelable {
    public static final Creator<WeightPhoto> CREATOR = new Creator<WeightPhoto>() {
        public WeightPhoto createFromParcel(Parcel source) {
            return new WeightPhoto(source);
        }

        public WeightPhoto[] newArray(int size) {
            return new WeightPhoto[size];
        }
    };
    public int    id;
    public String photo_url;
    public String record_on;
    public String thumb_photo_url;
    public float  weight;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.record_on);
        dest.writeFloat(this.weight);
        dest.writeString(this.photo_url);
        dest.writeString(this.thumb_photo_url);
    }

    protected WeightPhoto(Parcel in) {
        this.id = in.readInt();
        this.record_on = in.readString();
        this.weight = in.readFloat();
        this.photo_url = in.readString();
        this.thumb_photo_url = in.readString();
    }
}
