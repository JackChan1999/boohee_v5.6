package com.boohee.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class VideoSportRecord implements Parcelable {
    public static final Creator<VideoSportRecord> CREATOR = new Creator<VideoSportRecord>() {
        public VideoSportRecord createFromParcel(Parcel source) {
            return new VideoSportRecord(source);
        }

        public VideoSportRecord[] newArray(int size) {
            return new VideoSportRecord[size];
        }
    };
    public String activity_name;
    public int    amount;
    public int    calory;
    public int    id;
    public String img_url;
    public int    record_id;
    public String unit_name;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.img_url);
        dest.writeInt(this.id);
        dest.writeInt(this.amount);
        dest.writeString(this.activity_name);
        dest.writeInt(this.calory);
        dest.writeString(this.unit_name);
        dest.writeInt(this.record_id);
    }

    protected VideoSportRecord(Parcel in) {
        this.img_url = in.readString();
        this.id = in.readInt();
        this.amount = in.readInt();
        this.activity_name = in.readString();
        this.calory = in.readInt();
        this.unit_name = in.readString();
        this.record_id = in.readInt();
    }
}
