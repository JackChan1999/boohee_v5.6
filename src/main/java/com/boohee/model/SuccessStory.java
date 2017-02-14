package com.boohee.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import java.util.List;

public class SuccessStory {
    public List<ItemsEntity>   items;
    public List<SlidersEntity> sliders;
    public List<Tag>           tags;

    public static class ItemsEntity {
        public String       id;
        public String       pic;
        public List<String> tags;
        public String       title;
        public String       url;
    }

    public static class SlidersEntity {
        public String pic_url;
        public String title;
        public String url;
    }

    public static class Tag implements Parcelable {
        public static final Creator<Tag> CREATOR = new Creator<Tag>() {
            public Tag createFromParcel(Parcel source) {
                return new Tag(source);
            }

            public Tag[] newArray(int size) {
                return new Tag[size];
            }
        };
        public String       category;
        public List<String> items;

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.category);
            dest.writeStringList(this.items);
        }

        protected Tag(Parcel in) {
            this.category = in.readString();
            this.items = in.createStringArrayList();
        }
    }
}
