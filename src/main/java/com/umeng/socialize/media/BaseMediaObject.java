package com.umeng.socialize.media;

import android.os.Parcel;
import android.text.TextUtils;

import com.umeng.socialize.bean.SHARE_MEDIA;

public abstract class BaseMediaObject implements UMediaObject {
    protected String a = "";
    protected String b = "";
    protected String c = "";
    protected String d = "";
    protected String e = "";
    protected String f = "";
    protected int    g = 0;

    public BaseMediaObject(String str) {
        this.a = str;
    }

    public String toUrl() {
        return this.a;
    }

    public void setMediaUrl(String str) {
        this.a = str;
    }

    public boolean isUrlMedia() {
        if (TextUtils.isEmpty(this.a)) {
            return false;
        }
        return true;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.a);
        parcel.writeString(this.b);
        parcel.writeString(this.c);
        parcel.writeString(this.d);
    }

    public String getTitle() {
        return this.b;
    }

    public void setTitle(String str) {
        this.b = str;
    }

    public String getThumb() {
        return this.c;
    }

    public void setThumb(String str) {
        this.c = str;
    }

    public void setTargetUrl(String str) {
        this.d = str;
    }

    public String getTargetUrl() {
        return this.d;
    }

    protected BaseMediaObject(Parcel parcel) {
        if (parcel != null) {
            this.a = parcel.readString();
            this.b = parcel.readString();
            this.c = parcel.readString();
            this.d = parcel.readString();
        }
    }

    public SHARE_MEDIA getTargetPlatform() {
        return SHARE_MEDIA.GENERIC;
    }

    public String toString() {
        return "BaseMediaObject [media_url=" + this.a + ", qzone_title=" + this.b + ", " +
                "qzone_thumb=" + this.c + "]";
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
