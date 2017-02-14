package me.nereo.multi_image_selector.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.List;

public class Folder implements Parcelable {
    public static final Creator<Folder> CREATOR = new Creator<Folder>() {
        public Folder createFromParcel(Parcel source) {
            return new Folder(source);
        }

        public Folder[] newArray(int size) {
            return new Folder[size];
        }
    };
    public Image cover;
    public List<Image> images;
    public String name;
    public String path;

    public boolean equals(Object o) {
        try {
            return this.path.equalsIgnoreCase(((Folder) o).path);
        } catch (ClassCastException e) {
            e.printStackTrace();
            return super.equals(o);
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.path);
        dest.writeParcelable(this.cover, 0);
        dest.writeTypedList(this.images);
    }

    private Folder(Parcel in) {
        this.name = in.readString();
        this.path = in.readString();
        this.cover = (Image) in.readParcelable(Image.class.getClassLoader());
        in.readTypedList(this.images, Image.CREATOR);
    }
}
