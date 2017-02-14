package com.boohee.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class RecordPhoto implements Parcelable {
    public static final Creator<RecordPhoto> CREATOR = new Creator<RecordPhoto>() {
        public RecordPhoto createFromParcel(Parcel source) {
            return new RecordPhoto(source);
        }

        public RecordPhoto[] newArray(int size) {
            return new RecordPhoto[size];
        }
    };
    public float  calory;
    public String comment;
    public String consultor_name;
    public int    id;
    public String name;
    public String photo_url;
    public String record_on;
    public int    status;
    public int    time_type;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.record_on);
        dest.writeString(this.photo_url);
        dest.writeInt(this.status);
        dest.writeInt(this.time_type);
        dest.writeFloat(this.calory);
        dest.writeString(this.consultor_name);
        dest.writeString(this.comment);
    }

    protected RecordPhoto(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.record_on = in.readString();
        this.photo_url = in.readString();
        this.status = in.readInt();
        this.time_type = in.readInt();
        this.calory = in.readFloat();
        this.consultor_name = in.readString();
        this.comment = in.readString();
    }
}
