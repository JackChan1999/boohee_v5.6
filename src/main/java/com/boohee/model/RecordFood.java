package com.boohee.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class RecordFood extends ModelBase implements Parcelable, Cloneable {
    public static final Creator<RecordFood> CREATOR = new Creator<RecordFood>() {
        public RecordFood createFromParcel(Parcel source) {
            return new RecordFood(source);
        }

        public RecordFood[] newArray(int size) {
            return new RecordFood[size];
        }
    };
    public float   amount;
    public float   calory;
    public String  code;
    public String  food_name;
    public int     food_unit_id;
    public int     health_light;
    public boolean isChecked;
    public String  record_on;
    public String  thumb_img_url;
    public int     time_type;
    public String  unit_name;
    public String  user_key;

    public RecordFood() {
        this.isChecked = true;
    }

    public RecordFood(String user_key, String food_name, int time_type, String record_on, String
            code, float amount, int food_unit_id, String unit_name, float calory) {
        this.isChecked = true;
        this.user_key = user_key;
        this.food_name = food_name;
        this.time_type = time_type;
        this.record_on = record_on;
        this.code = code;
        this.amount = amount;
        this.food_unit_id = food_unit_id;
        this.unit_name = unit_name;
        this.calory = calory;
    }

    public RecordFood(int id, String user_key, String food_name, int time_type, String record_on,
                      String code, float amount, int food_unit_id, String unit_name, float calory) {
        this(user_key, food_name, time_type, record_on, code, amount, food_unit_id, unit_name,
                calory);
        this.id = id;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.food_name);
        dest.writeString(this.record_on);
        dest.writeString(this.code);
        dest.writeInt(this.time_type);
        dest.writeFloat(this.amount);
        dest.writeFloat(this.calory);
        dest.writeInt(this.food_unit_id);
        dest.writeString(this.unit_name);
        dest.writeInt(this.health_light);
        dest.writeString(this.thumb_img_url);
        dest.writeByte(this.isChecked ? (byte) 1 : (byte) 0);
    }

    protected RecordFood(Parcel in) {
        boolean z = true;
        this.isChecked = true;
        this.id = in.readInt();
        this.food_name = in.readString();
        this.record_on = in.readString();
        this.code = in.readString();
        this.time_type = in.readInt();
        this.amount = in.readFloat();
        this.calory = in.readFloat();
        this.food_unit_id = in.readInt();
        this.unit_name = in.readString();
        this.health_light = in.readInt();
        this.thumb_img_url = in.readString();
        if (in.readByte() == (byte) 0) {
            z = false;
        }
        this.isChecked = z;
    }

    public RecordFood clone() {
        Parcel parcel = Parcel.obtain();
        writeToParcel(parcel, 0);
        byte[] bytes = parcel.marshall();
        Parcel parcel2 = Parcel.obtain();
        parcel2.unmarshall(bytes, 0, bytes.length);
        parcel2.setDataPosition(0);
        return (RecordFood) CREATOR.createFromParcel(parcel2);
    }
}
