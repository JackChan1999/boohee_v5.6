package com.boohee.model.status;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class Photo implements Parcelable {
    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        public Photo createFromParcel(Parcel source) {
            return new Photo(source);
        }

        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };
    public String big_url;
    public String id;
    public String middle_url;
    public String original_url;
    public int    preview_height;
    public int    preview_width;
    public String small_url;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.small_url);
        dest.writeString(this.middle_url);
        dest.writeString(this.big_url);
        dest.writeString(this.original_url);
        dest.writeInt(this.preview_width);
        dest.writeInt(this.preview_height);
    }

    private Photo(Parcel in) {
        this.id = in.readString();
        this.small_url = in.readString();
        this.middle_url = in.readString();
        this.big_url = in.readString();
        this.original_url = in.readString();
        this.preview_width = in.readInt();
        this.preview_height = in.readInt();
    }
}
