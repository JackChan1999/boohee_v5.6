package com.umeng.socialize.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.umeng.socialize.net.utils.SocializeProtocolConstants;

import org.json.JSONException;
import org.json.JSONObject;

public class UMComment extends BaseMsg implements Parcelable {
    public static final Creator<UMComment> CREATOR = new an();
    public long   mDt;
    public Gender mGender;
    public String mSignature;
    public String mUid;
    public String mUname;
    public String mUserIcon;

    public static UMComment parseJson(JSONObject jSONObject) {
        if (jSONObject == null) {
            return null;
        }
        UMComment uMComment = new UMComment();
        try {
            if (jSONObject.has(SocializeProtocolConstants.PROTOCOL_KEY_USER_NAME)) {
                uMComment.mUname = jSONObject.getString(SocializeProtocolConstants
                        .PROTOCOL_KEY_USER_NAME);
            }
            if (jSONObject.has(SocializeProtocolConstants.PROTOCOL_KEY_USER_ICON)) {
                uMComment.mUserIcon = jSONObject.getString(SocializeProtocolConstants
                        .PROTOCOL_KEY_USER_ICON);
            }
            if (jSONObject.has(SocializeProtocolConstants.PROTOCOL_KEY_UID)) {
                uMComment.mUid = jSONObject.getString(SocializeProtocolConstants.PROTOCOL_KEY_UID);
            }
            if (jSONObject.has(SocializeProtocolConstants.PROTOCOL_KEY_COMMENT_TEXT)) {
                uMComment.mText = jSONObject.getString(SocializeProtocolConstants
                        .PROTOCOL_KEY_COMMENT_TEXT);
            }
            if (jSONObject.has(SocializeProtocolConstants.PROTOCOL_KEY_DT)) {
                uMComment.mDt = jSONObject.getLong(SocializeProtocolConstants.PROTOCOL_KEY_DT);
            }
            if (jSONObject.has(SocializeProtocolConstants.PROTOCOL_KEY_GENDER)) {
                uMComment.mGender = Gender.convertToEmun("" + jSONObject.optInt
                        (SocializeProtocolConstants.PROTOCOL_KEY_GENDER, 0));
            }
            if (!jSONObject.has(SocializeProtocolConstants.PROTOCOL_KEY_LOCATION)) {
                return uMComment;
            }
            uMComment.mLocation = UMLocation.build(jSONObject.getString
                    (SocializeProtocolConstants.PROTOCOL_KEY_LOCATION));
            return uMComment;
        } catch (JSONException e) {
            return uMComment;
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(this.mUserIcon);
        parcel.writeString(this.mUid);
        parcel.writeString(this.mUname);
        parcel.writeString(this.mSignature);
        parcel.writeLong(this.mDt);
        parcel.writeString(this.mGender == null ? "" : this.mGender.toString());
    }

    private UMComment(Parcel parcel) {
        super(parcel);
        this.mUserIcon = parcel.readString();
        this.mUid = parcel.readString();
        this.mUname = parcel.readString();
        this.mSignature = parcel.readString();
        this.mDt = parcel.readLong();
    }

    public String toString() {
        return "UMComment [mUserIcon=" + this.mUserIcon + ", mUid=" + this.mUid + ", mUname=" +
                this.mUname + ", mSignature=" + this.mSignature + ", mDt=" + this.mDt + ", " +
                "mGender=" + this.mGender + ", mText=" + this.mText + "]";
    }
}
