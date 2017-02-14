package com.kitnew.ble;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.umeng.socialize.net.utils.SocializeProtocolConstants;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class QNUser implements Parcelable {
    public static final Creator<QNUser> CREATOR = new Creator<QNUser>() {
        public QNUser createFromParcel(Parcel source) {
            return new QNUser(source);
        }

        public QNUser[] newArray(int size) {
            return new QNUser[size];
        }
    };
    Date birthday;
    int  gender;
    int  height;
    final String id;
    int   resistance;
    float weight;

    public QNUser(String id) {
        this.id = id;
    }

    public QNUser(String id, int height, int gender, Date birthday) {
        this.id = id;
        this.height = height;
        this.birthday = birthday;
        this.gender = gender;
    }

    boolean allEqual(QNUser user) {
        if (user != null && this.id.equals(user.id) && this.height == user.height && this.gender
                == user.gender && this.birthday.getTime() == user.birthday.getTime()) {
            return true;
        }
        return false;
    }

    int calcAge() {
        return new Date().getYear() - this.birthday.getYear();
    }

    String formatString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", this.id);
            jsonObject.put("height", this.height);
            jsonObject.put(SocializeProtocolConstants.PROTOCOL_KEY_GENDER, this.gender);
            if (this.birthday != null) {
                jsonObject.put("birthday", this.birthday.getTime());
            }
            jsonObject.put("weight", (double) this.weight);
            jsonObject.put("resistance", this.resistance);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    static QNUser newInstance(String jsonString) {
        if (jsonString == null) {
            return null;
        }
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            QNUser user = new QNUser(jsonObject.getString("id"));
            user.height = jsonObject.getInt("height");
            user.gender = jsonObject.getInt(SocializeProtocolConstants.PROTOCOL_KEY_GENDER);
            if (jsonObject.has("birthday")) {
                user.birthday = new Date(jsonObject.getLong("birthday"));
            }
            user.weight = (float) jsonObject.getDouble("weight");
            user.resistance = jsonObject.getInt("resistance");
            return user;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getId() {
        return this.id;
    }

    public int getHeight() {
        return this.height;
    }

    public Date getBirthday() {
        return this.birthday;
    }

    public int getGender() {
        return this.gender;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeInt(this.height);
        dest.writeLong(this.birthday != null ? this.birthday.getTime() : -1);
        dest.writeInt(this.gender);
        dest.writeFloat(this.weight);
        dest.writeInt(this.resistance);
    }

    protected QNUser(Parcel in) {
        this.id = in.readString();
        this.height = in.readInt();
        long tmpBirthday = in.readLong();
        this.birthday = tmpBirthday == -1 ? null : new Date(tmpBirthday);
        this.gender = in.readInt();
        this.weight = in.readFloat();
        this.resistance = in.readInt();
    }
}
