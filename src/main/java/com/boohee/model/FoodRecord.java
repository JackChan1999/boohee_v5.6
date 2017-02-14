package com.boohee.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class FoodRecord extends ModelBase implements Parcelable {
    public static final String[]            CATEGORY_NAMES = new String[]{"早餐", "上午加餐", "午餐",
            "下午加餐", "晚餐", "晚上加餐"};
    public static final Creator<FoodRecord> CREATOR        = new Creator<FoodRecord>() {
        public FoodRecord createFromParcel(Parcel source) {
            return new FoodRecord(source);
        }

        public FoodRecord[] newArray(int size) {
            return new FoodRecord[size];
        }
    };
    public static final int[]               TIME_TYPES     = new int[]{1, 6, 2, 7, 3, 8};
    public float  amount;
    public float  calory;
    public String code;
    public String food_name;
    public int    food_unit_id;
    public int    health_light;
    public boolean isChecked = true;
    public String record_on;
    public String thumb_img_url;
    public int    time_type;
    public String unit_name;
    public String user_key;

    public FoodRecord(int id, String user_key, String food_name, int time_type, String record_on,
                      String code, float amount, int food_unit_id, String unit_name, float calory) {
        this.id = id;
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

    public String getMealName() {
        String mealName = "";
        switch (this.time_type) {
            case 1:
                return "早餐";
            case 2:
                return "午餐";
            case 3:
                return "晚餐";
            case 6:
                return "上午加餐";
            case 7:
                return "下午加餐";
            case 8:
                return "晚上加餐";
            default:
                return "加餐";
        }
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

    protected FoodRecord(Parcel in) {
        boolean z = true;
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
}
