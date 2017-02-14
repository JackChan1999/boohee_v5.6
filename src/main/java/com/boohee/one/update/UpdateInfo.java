package com.boohee.one.update;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class UpdateInfo implements Parcelable {
    public static final Creator<UpdateInfo> CREATOR = new Creator<UpdateInfo>() {
        public UpdateInfo createFromParcel(Parcel source) {
            return new UpdateInfo(source);
        }

        public UpdateInfo[] newArray(int size) {
            return new UpdateInfo[size];
        }
    };
    public String  apk_url;
    public String  new_md5;
    public String  new_version;
    public int     target_size;
    public boolean update;
    public String  update_log;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.update ? (byte) 1 : (byte) 0);
        dest.writeString(this.new_version);
        dest.writeString(this.apk_url);
        dest.writeString(this.update_log);
        dest.writeString(this.new_md5);
        dest.writeInt(this.target_size);
    }

    protected UpdateInfo(Parcel in) {
        this.update = in.readByte() != (byte) 0;
        this.new_version = in.readString();
        this.apk_url = in.readString();
        this.update_log = in.readString();
        this.new_md5 = in.readString();
        this.target_size = in.readInt();
    }
}
