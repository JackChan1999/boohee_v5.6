package com.umeng.socialize.media;

import android.os.Parcel;
import android.text.TextUtils;

import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.media.UMediaObject.FetchMediaDataListener;
import com.umeng.socialize.media.UMediaObject.MediaType;
import com.umeng.socialize.utils.Log;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseShareContent extends SimpleShareContent {
    protected String mTargetUrl = "";
    protected String mTitle     = "";

    public BaseShareContent(String str) {
        this.mShareContent = str;
    }

    public BaseShareContent(UMediaObject uMediaObject) {
        this.mShareMedia = uMediaObject;
    }

    public void setShareMedia(UMediaObject uMediaObject) {
        this.mShareMedia = uMediaObject;
    }

    public UMediaObject getShareMedia() {
        Log.v("10.12", "mShareMedia= " + this.mShareMedia);
        return this.mShareMedia;
    }

    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(this.mTitle);
        parcel.writeString(this.mTargetUrl);
    }

    protected BaseShareContent(Parcel parcel) {
        super(parcel);
        if (parcel != null) {
            this.mTitle = parcel.readString();
            this.mTargetUrl = parcel.readString();
        }
    }

    public Map<String, Object> toUrlExtraParams() {
        if (this.mShareMedia != null) {
            return this.mShareMedia.toUrlExtraParams();
        }
        return new HashMap();
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
        if (TextUtils.isEmpty(this.mShareContent)) {
            return null;
        }
        return MediaType.TEXT;
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

    public String getTitle() {
        return this.mTitle;
    }

    public void setTitle(String str) {
        this.mTitle = str;
    }

    public String getTargetUrl() {
        return this.mTargetUrl;
    }

    public void setTargetUrl(String str) {
        this.mTargetUrl = str;
    }

    public void setAppWebSite(String str) {
        SocializeEntity.setAppWebSite(getTargetPlatform(), str);
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String toString() {
        return "BaseShareContent [mShareContent=" + this.mShareContent + ", mShareMedia=" + this
                .mShareMedia + "]";
    }
}
