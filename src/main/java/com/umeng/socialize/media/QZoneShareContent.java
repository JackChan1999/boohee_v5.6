package com.umeng.socialize.media;

import android.os.Parcel;
import android.os.Parcelable.Creator;

import com.umeng.socialize.bean.SHARE_MEDIA;

public class QZoneShareContent extends BaseShareContent {
    public static final Creator<QZoneShareContent> CREATOR = new Creator<QZoneShareContent>() {
        public QZoneShareContent createFromParcel(Parcel in) {
            return new QZoneShareContent(in);
        }

        public QZoneShareContent[] newArray(int size) {
            return new QZoneShareContent[size];
        }
    };

    public QZoneShareContent(String text) {
        super(text);
    }

    public QZoneShareContent(UMImage image) {
        super((UMediaObject) image);
    }

    public QZoneShareContent(UMusic music) {
        super((UMediaObject) music);
    }

    public QZoneShareContent(UMVideo video) {
        super((UMediaObject) video);
    }

    protected QZoneShareContent(Parcel in) {
        super(in);
    }

    public String toString() {
        return super.toString() + "[QZoneShareMedia]";
    }

    public SHARE_MEDIA getTargetPlatform() {
        return SHARE_MEDIA.QZONE;
    }
}
