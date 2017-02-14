package com.umeng.socialize.media;

import android.os.Parcel;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMediaObject.FetchMediaDataListener;
import com.umeng.socialize.media.UMediaObject.MediaType;

import java.util.Map;

public abstract class SimpleShareContent implements UMediaObject {
    protected String       TAG           = getClass().getName();
    protected String       mShareContent = "";
    protected UMediaObject mShareMedia   = null;

    public abstract SHARE_MEDIA getTargetPlatform();

    public SimpleShareContent(String str) {
        this.mShareContent = str;
    }

    public SimpleShareContent(UMImage uMImage) {
        this.mShareMedia = uMImage;
    }

    public String getShareContent() {
        return this.mShareContent;
    }

    public void setShareContent(String str) {
        this.mShareContent = str;
    }

    public UMImage getShareImage() {
        if (this.mShareMedia instanceof UMImage) {
            return (UMImage) this.mShareMedia;
        }
        return null;
    }

    public void setShareVideo(UMVideo uMVideo) {
        this.mShareMedia = uMVideo;
    }

    public UMVideo getShareVideo() {
        if (this.mShareMedia == null || !(this.mShareMedia instanceof UMVideo)) {
            return null;
        }
        return (UMVideo) this.mShareMedia;
    }

    public void setShareImage(UMImage uMImage) {
        this.mShareMedia = uMImage;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mShareContent);
        parcel.writeParcelable(this.mShareMedia, 0);
    }

    protected SimpleShareContent(Parcel parcel) {
        if (parcel != null) {
            this.mShareContent = parcel.readString();
            this.mShareMedia = (UMediaObject) parcel.readParcelable(UMediaObject.class
                    .getClassLoader());
        }
    }

    public Map<String, Object> toUrlExtraParams() {
        if (this.mShareMedia != null) {
            return this.mShareMedia.toUrlExtraParams();
        }
        return null;
    }

    public void toByte(FetchMediaDataListener fetchMediaDataListener) {
        if (this.mShareMedia != null) {
            this.mShareMedia.toByte(fetchMediaDataListener);
        }
    }

    public byte[] toByte() {
        if (this.mShareMedia != null) {
            return this.mShareMedia.toByte();
        }
        return null;
    }

    public MediaType getMediaType() {
        if (this.mShareMedia != null) {
            return this.mShareMedia.getMediaType();
        }
        return null;
    }

    public boolean isMultiMedia() {
        return true;
    }

    public boolean isUrlMedia() {
        if (this.mShareMedia != null) {
            return this.mShareMedia.isUrlMedia();
        }
        return false;
    }

    public String toUrl() {
        if (this.mShareMedia != null) {
            return this.mShareMedia.toUrl();
        }
        return "";
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String toString() {
        return "SimplaShareContent [mShareContent=" + this.mShareContent + ", mShareImage=" +
                this.mShareMedia + "]";
    }

    public int describeContents() {
        return 0;
    }
}
