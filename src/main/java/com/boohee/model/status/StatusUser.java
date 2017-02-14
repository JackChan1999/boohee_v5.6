package com.boohee.model.status;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.boohee.one.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import org.json.JSONObject;

public class StatusUser implements Parcelable {
    public static final Creator<StatusUser> CREATOR     = new Creator<StatusUser>() {
        public StatusUser createFromParcel(Parcel source) {
            return new StatusUser(source);
        }

        public StatusUser[] newArray(int size) {
            return new StatusUser[size];
        }
    };
    public static final String              DONOR       = "donor";
    public static final String              EVENT       = "event";
    public static final String              EVENT_DONOR = "event_donor";
    public static final String              MODERATOR   = "moderator";
    public static final String              OFFICIAL    = "official";
    public static final String              VALIDATED   = "validated";
    public String  address;
    public String  avatar_url;
    public float   bmi;
    public String  description;
    public int     envious_count;
    public int     follower_count;
    public boolean following;
    public int     following_count;
    public String  gender;
    public int     hateful_count;
    public int     id;
    public boolean invited;
    public int     jealous_count;
    public boolean light;
    public float[] location;
    public String  nickname;
    public int     post_count;
    public String  realname;
    public String  title;

    public StatusUser(int id, String nickname, String avatar_url) {
        this.id = id;
        this.nickname = nickname;
        this.avatar_url = avatar_url;
    }

    public static StatusUser parseUser(JSONObject object) {
        if (object == null) {
            return null;
        }
        return (StatusUser) new Gson().fromJson(object.toString(), StatusUser.class);
    }

    public static ArrayList<StatusUser> parseUsers(String str) {
        return (ArrayList) new Gson().fromJson(str, new TypeToken<ArrayList<StatusUser>>() {
        }.getType());
    }

    public static String displayCount(int count) {
        if (count <= 10000) {
            return String.valueOf(count);
        }
        return String.format("%.1f万", new Object[]{Float.valueOf(((float) count) / 10000.0f)});
    }

    public float getBmi() {
        return ((float) Math.round(this.bmi * 10.0f)) / 10.0f;
    }

    public int getAvatarTagResource() {
        if (OFFICIAL.equals(this.title)) {
            return R.drawable.wa;
        }
        if (DONOR.equals(this.title)) {
            return R.drawable.wb;
        }
        if (VALIDATED.equals(this.title)) {
            return R.drawable.wc;
        }
        if (MODERATOR.equals(this.title)) {
            return R.drawable.rh;
        }
        if ("event".equals(this.title)) {
            return R.drawable.w9;
        }
        if (EVENT_DONOR.equals(this.title)) {
            return R.drawable.w_;
        }
        return 0;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        byte b;
        byte b2 = (byte) 1;
        dest.writeInt(this.id);
        dest.writeString(this.nickname);
        dest.writeString(this.realname);
        dest.writeFloatArray(this.location);
        dest.writeString(this.address);
        dest.writeFloat(this.bmi);
        dest.writeString(this.description);
        dest.writeString(this.avatar_url);
        dest.writeString(this.gender);
        dest.writeInt(this.follower_count);
        dest.writeInt(this.following_count);
        dest.writeInt(this.post_count);
        dest.writeInt(this.envious_count);
        dest.writeInt(this.jealous_count);
        dest.writeInt(this.hateful_count);
        if (this.following) {
            b = (byte) 1;
        } else {
            b = (byte) 0;
        }
        dest.writeByte(b);
        if (this.invited) {
            b = (byte) 1;
        } else {
            b = (byte) 0;
        }
        dest.writeByte(b);
        dest.writeString(this.title);
        if (!this.light) {
            b2 = (byte) 0;
        }
        dest.writeByte(b2);
    }

    private StatusUser(Parcel in) {
        boolean z;
        boolean z2 = true;
        this.id = in.readInt();
        this.nickname = in.readString();
        this.realname = in.readString();
        this.location = in.createFloatArray();
        this.address = in.readString();
        this.bmi = in.readFloat();
        this.description = in.readString();
        this.avatar_url = in.readString();
        this.gender = in.readString();
        this.follower_count = in.readInt();
        this.following_count = in.readInt();
        this.post_count = in.readInt();
        this.envious_count = in.readInt();
        this.jealous_count = in.readInt();
        this.hateful_count = in.readInt();
        if (in.readByte() != (byte) 0) {
            z = true;
        } else {
            z = false;
        }
        this.following = z;
        if (in.readByte() != (byte) 0) {
            z = true;
        } else {
            z = false;
        }
        this.invited = z;
        this.title = in.readString();
        if (in.readByte() == (byte) 0) {
            z2 = false;
        }
        this.light = z2;
    }
}
