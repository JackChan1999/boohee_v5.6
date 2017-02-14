package com.boohee.one.sport.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class SportDetail implements Parcelable {
    public static final Creator<SportDetail> CREATOR = new Creator<SportDetail>() {
        public SportDetail createFromParcel(Parcel source) {
            return new SportDetail(source);
        }

        public SportDetail[] newArray(int size) {
            return new SportDetail[size];
        }
    };
    public int     calory;
    public String  description;
    public int     duration;
    public int     envious;
    public String  info;
    public boolean is_finish;
    public String  name;
    public String  pic_url;
    public String  share_message;
    public String  video_url;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.pic_url);
        dest.writeString(this.video_url);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeInt(this.calory);
        dest.writeInt(this.duration);
        dest.writeByte(this.is_finish ? (byte) 1 : (byte) 0);
        dest.writeString(this.info);
        dest.writeInt(this.envious);
        dest.writeString(this.share_message);
    }

    protected SportDetail(Parcel in) {
        this.pic_url = in.readString();
        this.video_url = in.readString();
        this.name = in.readString();
        this.description = in.readString();
        this.calory = in.readInt();
        this.duration = in.readInt();
        this.is_finish = in.readByte() != (byte) 0;
        this.info = in.readString();
        this.envious = in.readInt();
        this.share_message = in.readString();
    }
}
