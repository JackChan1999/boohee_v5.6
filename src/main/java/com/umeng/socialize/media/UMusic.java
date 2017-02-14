package com.umeng.socialize.media;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;

import com.umeng.socialize.media.UMediaObject.FetchMediaDataListener;
import com.umeng.socialize.media.UMediaObject.MediaType;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;

import java.util.HashMap;
import java.util.Map;

public class UMusic extends BaseMediaObject {
    public static final Creator<UMusic> CREATOR = new Creator<UMusic>() {
        public UMusic createFromParcel(Parcel parcel) {
            return new UMusic(parcel);
        }

        public UMusic[] newArray(int i) {
            return new UMusic[i];
        }
    };
    private             String          h       = "未知";
    private             String          i       = "未知";
    private UMImage j;

    public UMusic(String str) {
        super(str);
    }

    public MediaType getMediaType() {
        return MediaType.MUSIC;
    }

    public void toByte(FetchMediaDataListener fetchMediaDataListener) {
    }

    protected UMusic(Parcel parcel) {
        super(parcel);
        this.h = parcel.readString();
        this.i = parcel.readString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(this.h);
        parcel.writeString(this.i);
    }

    public final Map<String, Object> toUrlExtraParams() {
        Map<String, Object> hashMap = new HashMap();
        if (isUrlMedia()) {
            hashMap.put(SocializeProtocolConstants.PROTOCOL_KEY_FURL, this.a);
            hashMap.put(SocializeProtocolConstants.PROTOCOL_KEY_FTYPE, getMediaType());
            hashMap.put(SocializeProtocolConstants.PROTOCOL_KEY_TITLE, this.h);
            hashMap.put(SocializeProtocolConstants.PROTOCOL_KEY_AUTHOR, this.i);
        }
        return hashMap;
    }

    public void setTitle(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.h = str;
        }
    }

    public void setAuthor(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.i = str;
        }
    }

    public String getTitle() {
        return this.h;
    }

    public String getAuthor() {
        return this.i;
    }

    public void setThumb(UMImage uMImage) {
        this.j = uMImage;
    }

    public byte[] toByte() {
        if (this.j != null) {
            return this.j.toByte();
        }
        return null;
    }

    public String toString() {
        return "UMusic [title=" + this.h + ", author=" + this.i + "media_url=" + this.a + ", " +
                "qzone_title=" + this.b + ", qzone_thumb=" + this.c + "]";
    }

    public boolean isMultiMedia() {
        return true;
    }

    public UMImage getThumbImage() {
        return this.j;
    }
}
