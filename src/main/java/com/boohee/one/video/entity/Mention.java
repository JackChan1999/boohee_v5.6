package com.boohee.one.video.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class Mention implements Parcelable {
    public static final Creator<Mention> CREATOR = new Creator<Mention>() {
        public Mention createFromParcel(Parcel source) {
            return new Mention(source);
        }

        public Mention[] newArray(int size) {
            return new Mention[size];
        }
    };
    public String      audio_url;
    public int         group_count;
    public int         id;
    public MentionInfo info;
    public boolean     is_times;
    public String      name;
    public int         number;
    public float       rate;
    public int         rest;
    public String      thumbnail;
    public String      training_part;
    public String      video_url;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.is_times ? (byte) 1 : (byte) 0);
        dest.writeInt(this.id);
        dest.writeFloat(this.rate);
        dest.writeString(this.video_url);
        dest.writeParcelable(this.info, 0);
        dest.writeString(this.name);
        dest.writeInt(this.number);
        dest.writeInt(this.rest);
        dest.writeInt(this.group_count);
        dest.writeString(this.training_part);
        dest.writeString(this.audio_url);
        dest.writeString(this.thumbnail);
    }

    protected Mention(Parcel in) {
        this.is_times = in.readByte() != (byte) 0;
        this.id = in.readInt();
        this.rate = in.readFloat();
        this.video_url = in.readString();
        this.info = (MentionInfo) in.readParcelable(MentionInfo.class.getClassLoader());
        this.name = in.readString();
        this.number = in.readInt();
        this.rest = in.readInt();
        this.group_count = in.readInt();
        this.training_part = in.readString();
        this.audio_url = in.readString();
        this.thumbnail = in.readString();
    }
}
