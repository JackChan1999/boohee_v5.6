package com.boohee.one.video.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class UserProgress implements Parcelable {
    public static final Creator<UserProgress> CREATOR = new Creator<UserProgress>() {
        public UserProgress createFromParcel(Parcel source) {
            return new UserProgress(source);
        }

        public UserProgress[] newArray(int size) {
            return new UserProgress[size];
        }
    };
    public int continue_days;
    public int finish_section_count;
    public int total_section_count;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.continue_days);
        dest.writeInt(this.total_section_count);
        dest.writeInt(this.finish_section_count);
    }

    protected UserProgress(Parcel in) {
        this.continue_days = in.readInt();
        this.total_section_count = in.readInt();
        this.finish_section_count = in.readInt();
    }
}
