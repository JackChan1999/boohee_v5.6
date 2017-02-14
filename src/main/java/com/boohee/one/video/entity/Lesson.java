package com.boohee.one.video.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import java.util.List;

public class Lesson implements Parcelable {
    public static final Creator<Lesson> CREATOR = new Creator<Lesson>() {
        public Lesson createFromParcel(Parcel source) {
            return new Lesson(source);
        }

        public Lesson[] newArray(int size) {
            return new Lesson[size];
        }
    };
    public String        banner_url;
    public int           basic_calorie;
    public int           beat;
    public int           calorie;
    public String        description;
    public String        difficulty;
    public int           envious_count;
    public int           id;
    public int           level;
    public String        level_pic;
    public List<Mention> mentions;
    public int           mentions_count;
    public List<Mention> mentions_train;
    public List<Mention> mentions_warm;
    public String        name;
    public int           next_lesson_id;
    public String        pic_url;
    public int           progress;
    public float         stream;
    public int           today_calorie;
    public int           total_progress;
    public int           total_time;
    public int           unfinish_count;
    public UserProgress  user_progress;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.progress);
        dest.writeInt(this.total_progress);
        dest.writeInt(this.total_time);
        dest.writeInt(this.mentions_count);
        dest.writeFloat(this.stream);
        dest.writeInt(this.beat);
        dest.writeInt(this.next_lesson_id);
        dest.writeString(this.description);
        dest.writeString(this.name);
        dest.writeTypedList(this.mentions);
        dest.writeInt(this.calorie);
        dest.writeString(this.pic_url);
        dest.writeString(this.banner_url);
        dest.writeParcelable(this.user_progress, 0);
        dest.writeInt(this.unfinish_count);
        dest.writeInt(this.today_calorie);
        dest.writeInt(this.basic_calorie);
        dest.writeString(this.level_pic);
        dest.writeString(this.difficulty);
        dest.writeInt(this.level);
        dest.writeInt(this.envious_count);
        dest.writeTypedList(this.mentions_warm);
        dest.writeTypedList(this.mentions_train);
    }

    protected Lesson(Parcel in) {
        this.id = in.readInt();
        this.progress = in.readInt();
        this.total_progress = in.readInt();
        this.total_time = in.readInt();
        this.mentions_count = in.readInt();
        this.stream = in.readFloat();
        this.beat = in.readInt();
        this.next_lesson_id = in.readInt();
        this.description = in.readString();
        this.name = in.readString();
        this.mentions = in.createTypedArrayList(Mention.CREATOR);
        this.calorie = in.readInt();
        this.pic_url = in.readString();
        this.banner_url = in.readString();
        this.user_progress = (UserProgress) in.readParcelable(UserProgress.class.getClassLoader());
        this.unfinish_count = in.readInt();
        this.today_calorie = in.readInt();
        this.basic_calorie = in.readInt();
        this.level_pic = in.readString();
        this.difficulty = in.readString();
        this.level = in.readInt();
        this.envious_count = in.readInt();
        this.mentions_warm = in.createTypedArrayList(Mention.CREATOR);
        this.mentions_train = in.createTypedArrayList(Mention.CREATOR);
    }
}
