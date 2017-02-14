package com.boohee.one.sport.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class CourseItemInfo implements Parcelable {
    public static final Creator<CourseItemInfo> CREATOR = new Creator<CourseItemInfo>() {
        public CourseItemInfo createFromParcel(Parcel in) {
            return new CourseItemInfo(in);
        }

        public CourseItemInfo[] newArray(int size) {
            return new CourseItemInfo[size];
        }
    };
    public String  date;
    public boolean download;
    public int     id;
    public int     index;
    public boolean is_rest;
    public String  link_to;
    public String  name;
    public boolean today;
    public String  video_url;

    protected CourseItemInfo(Parcel in) {
        boolean z;
        boolean z2 = true;
        this.id = in.readInt();
        this.index = in.readInt();
        this.name = in.readString();
        this.video_url = in.readString();
        this.date = in.readString();
        this.link_to = in.readString();
        if (in.readByte() != (byte) 0) {
            z = true;
        } else {
            z = false;
        }
        this.is_rest = z;
        if (in.readByte() != (byte) 0) {
            z = true;
        } else {
            z = false;
        }
        this.download = z;
        if (in.readByte() == (byte) 0) {
            z2 = false;
        }
        this.today = z2;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        int i;
        int i2 = 1;
        dest.writeInt(this.id);
        dest.writeInt(this.index);
        dest.writeString(this.name);
        dest.writeString(this.video_url);
        dest.writeString(this.date);
        dest.writeString(this.link_to);
        if (this.is_rest) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeByte((byte) i);
        if (this.download) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeByte((byte) i);
        if (!this.today) {
            i2 = 0;
        }
        dest.writeByte((byte) i2);
    }
}
