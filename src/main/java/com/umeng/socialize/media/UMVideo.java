package com.umeng.socialize.media;

import android.os.Parcel;
import android.os.Parcelable.Creator;

import com.umeng.socialize.media.UMediaObject.FetchMediaDataListener;
import com.umeng.socialize.media.UMediaObject.MediaType;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;

import java.util.HashMap;
import java.util.Map;

public class UMVideo extends BaseMediaObject {
    public static final Creator<UMVideo> CREATOR = new Creator<UMVideo>() {
        public UMVideo createFromParcel(Parcel parcel) {
            return new UMVideo(parcel);
        }

        public UMVideo[] newArray(int i) {
            return new UMVideo[i];
        }
    };
    private UMImage h;

    public UMVideo(String str) {
        super(str);
    }

    public MediaType getMediaType() {
        return MediaType.VEDIO;
    }

    public void toByte(FetchMediaDataListener fetchMediaDataListener) {
    }

    protected UMVideo(Parcel parcel) {
        super(parcel);
    }

    public final Map<String, Object> toUrlExtraParams() {
        Map<String, Object> hashMap = new HashMap();
        if (isUrlMedia()) {
            hashMap.put(SocializeProtocolConstants.PROTOCOL_KEY_FURL, this.a);
            hashMap.put(SocializeProtocolConstants.PROTOCOL_KEY_FTYPE, getMediaType());
        }
        return hashMap;
    }

    public void setThumb(UMImage uMImage) {
        this.h = uMImage;
    }

    public byte[] toByte() {
        if (this.h != null) {
            return this.h.toByte();
        }
        return null;
    }

    public String toString() {
        return "UMVedio [media_url=" + this.a + ", qzone_title=" + this.b + ", qzone_thumb=" +
                this.c + "media_url=" + this.a + ", qzone_title=" + this.b + ", qzone_thumb=" +
                this.c + "]";
    }

    public boolean isMultiMedia() {
        return true;
    }

    public UMImage getThumbImage() {
        return this.h;
    }
}
