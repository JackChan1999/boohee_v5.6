package com.umeng.socialize.media;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable.Creator;

import com.umeng.socialize.media.UMediaObject.FetchMediaDataListener;
import com.umeng.socialize.media.UMediaObject.MediaType;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class UMEmoji extends BaseMediaObject {
    public static final Creator<UMEmoji> CREATOR = new Creator<UMEmoji>() {
        public UMEmoji createFromParcel(Parcel parcel) {
            return new UMEmoji(parcel);
        }

        public UMEmoji[] newArray(int i) {
            return new UMEmoji[i];
        }
    };
    public UMImage mSrcImage;
    public UMImage mThumb;

    public UMEmoji(Context context, String str) {
        super(str);
        this.mSrcImage = new UMImage(context, str);
    }

    public UMEmoji(Context context, File file) {
        this.mSrcImage = new UMImage(context, file);
    }

    public MediaType getMediaType() {
        return MediaType.IMAGE;
    }

    public void toByte(FetchMediaDataListener fetchMediaDataListener) {
    }

    protected UMEmoji(Parcel parcel) {
        super(parcel);
    }

    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
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
        this.mThumb = uMImage;
    }

    public byte[] toByte() {
        if (this.mThumb != null) {
            return this.mSrcImage.toByte();
        }
        return null;
    }

    public String toString() {
        return "UMEmoji [" + this.mThumb.toString() + "]";
    }

    public boolean isMultiMedia() {
        return true;
    }

    public UMImage getThumbImage() {
        return this.mThumb;
    }
}
