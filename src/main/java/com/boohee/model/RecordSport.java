package com.boohee.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import org.json.JSONObject;

public class RecordSport implements Parcelable {
    public static final Creator<RecordSport> CREATOR = new Creator<RecordSport>() {
        public RecordSport createFromParcel(Parcel source) {
            return new RecordSport(source);
        }

        public RecordSport[] newArray(int size) {
            return new RecordSport[size];
        }
    };
    public int    activity_id;
    public String activity_name;
    public float  calory;
    public float  duration;
    public int    id;
    public boolean isChecked = true;
    public float  mets;
    public String record_on;
    public String thumb_img_url;
    public String unit_name;
    public String user_key;

    public RecordSport(int id, String record_on, float duration, int activity_id, String
            activity_name, float calory, String unit_name, float mets, String user_key) {
        this.id = id;
        this.record_on = record_on;
        this.duration = duration;
        this.activity_id = activity_id;
        this.activity_name = activity_name;
        this.calory = calory;
        this.unit_name = unit_name;
        this.mets = mets;
        this.user_key = user_key;
    }

    public int calcCalory(float weight) {
        return Math.round((float) ((1.34d * ((double) (this.mets - 1.0f))) * ((double) weight)));
    }

    public int caloryWithDuration(float duration) {
        return Math.round((duration / 60.0f) * this.calory);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.record_on);
        dest.writeFloat(this.duration);
        dest.writeInt(this.activity_id);
        dest.writeString(this.activity_name);
        dest.writeFloat(this.calory);
        dest.writeString(this.thumb_img_url);
        dest.writeString(this.unit_name);
        dest.writeFloat(this.mets);
        dest.writeByte(this.isChecked ? (byte) 1 : (byte) 0);
    }

    protected RecordSport(Parcel in) {
        boolean z = true;
        this.id = in.readInt();
        this.record_on = in.readString();
        this.duration = in.readFloat();
        this.activity_id = in.readInt();
        this.activity_name = in.readString();
        this.calory = in.readFloat();
        this.thumb_img_url = in.readString();
        this.unit_name = in.readString();
        this.mets = in.readFloat();
        if (in.readByte() == (byte) 0) {
            z = false;
        }
        this.isChecked = z;
    }

    public static RecordSport parse(JSONObject object) {
        try {
            return (RecordSport) new Gson().fromJson(object.toString(), RecordSport.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<RecordSport> parseList(JSONObject res, String key) {
        try {
            return (ArrayList) new Gson().fromJson(res.getString(key).toString(), new
                    TypeToken<ArrayList<RecordSport>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
