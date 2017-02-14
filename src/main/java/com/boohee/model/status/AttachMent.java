package com.boohee.model.status;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class AttachMent implements Parcelable {
    public static final Creator<AttachMent> CREATOR = new Creator<AttachMent>() {
        public AttachMent createFromParcel(Parcel source) {
            return new AttachMent(source);
        }

        public AttachMent[] newArray(int size) {
            return new AttachMent[size];
        }
    };
    public String cover;
    public String pic;
    public String title;
    public String type;
    public String url;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.pic);
        dest.writeString(this.type);
        dest.writeString(this.url);
        dest.writeString(this.cover);
    }

    protected AttachMent(Parcel in) {
        this.title = in.readString();
        this.pic = in.readString();
        this.type = in.readString();
        this.url = in.readString();
        this.cover = in.readString();
    }
}
