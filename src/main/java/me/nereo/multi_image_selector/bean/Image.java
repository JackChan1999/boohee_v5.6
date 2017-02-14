package me.nereo.multi_image_selector.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class Image implements Parcelable {
    public static final Creator<Image> CREATOR = new Creator<Image>() {
        public Image createFromParcel(Parcel source) {
            return new Image(source);
        }

        public Image[] newArray(int size) {
            return new Image[size];
        }
    };
    public String id;
    public String name;
    public long originSize;
    public String path;
    public long time;

    public Image(String path, String name, long time, long originSize, String id) {
        this.path = path;
        this.name = name;
        this.time = time;
        this.originSize = originSize;
        this.id = id;
    }

    public boolean equals(Object o) {
        try {
            return this.path.equalsIgnoreCase(((Image) o).path);
        } catch (ClassCastException e) {
            e.printStackTrace();
            return super.equals(o);
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.path);
        dest.writeString(this.name);
        dest.writeLong(this.time);
        dest.writeLong(this.originSize);
        dest.writeString(this.id);
    }

    private Image(Parcel in) {
        this.path = in.readString();
        this.name = in.readString();
        this.time = in.readLong();
        this.originSize = in.readLong();
        this.id = in.readString();
    }
}
