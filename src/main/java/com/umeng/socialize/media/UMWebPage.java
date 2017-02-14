package com.umeng.socialize.media;

import android.os.Parcel;

import com.umeng.socialize.media.UMediaObject.FetchMediaDataListener;
import com.umeng.socialize.media.UMediaObject.MediaType;

import java.util.Map;

public class UMWebPage extends BaseMediaObject {
    private String  h = "";
    private UMImage i = null;

    public UMWebPage(String str) {
        super(str);
        this.d = str;
    }

    protected UMWebPage(Parcel parcel) {
        super(parcel);
    }

    public void setTargetUrl(String str) {
        super.setTargetUrl(str);
        this.a = str;
    }

    public String getDescription() {
        return this.h;
    }

    public void setDescription(String str) {
        this.h = str;
    }

    public void setThumb(UMImage uMImage) {
        this.i = uMImage;
    }

    public UMImage getThumbImage() {
        return this.i;
    }

    public MediaType getMediaType() {
        return MediaType.WEBPAGE;
    }

    public Map<String, Object> toUrlExtraParams() {
        return null;
    }

    public void toByte(FetchMediaDataListener fetchMediaDataListener) {
    }

    public byte[] toByte() {
        return null;
    }

    public boolean isMultiMedia() {
        return true;
    }

    public String toString() {
        return "UMWebPage [mDescription=" + this.h + ", mMediaTitle=" + this.b + ", mMediaThumb="
                + this.c + ", mMediaTargetUrl=" + this.d + ", mLength=" + this.g + "]";
    }
}
