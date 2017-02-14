package com.boohee.one.video.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class MentionInfo implements Parcelable {
    public static final Creator<MentionInfo> CREATOR = new Creator<MentionInfo>() {
        public MentionInfo createFromParcel(Parcel source) {
            return new MentionInfo(source);
        }

        public MentionInfo[] newArray(int size) {
            return new MentionInfo[size];
        }
    };
    public String breath;
    public String common_errors;
    public String summary1;
    public String summary2;
    public String summary3;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.common_errors);
        dest.writeString(this.summary1);
        dest.writeString(this.summary2);
        dest.writeString(this.summary3);
        dest.writeString(this.breath);
    }

    protected MentionInfo(Parcel in) {
        this.common_errors = in.readString();
        this.summary1 = in.readString();
        this.summary2 = in.readString();
        this.summary3 = in.readString();
        this.breath = in.readString();
    }
}
