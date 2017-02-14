package com.boohee.one.sport.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class DownloadRecord implements Parcelable {
    public static final Creator<DownloadRecord> CREATOR               = new
            Creator<DownloadRecord>() {
        public DownloadRecord createFromParcel(Parcel source) {
            return new DownloadRecord(source);
        }

        public DownloadRecord[] newArray(int size) {
            return new DownloadRecord[size];
        }
    };
    public static final int                     STATUS_COMPLETE       = 6;
    public static final int                     STATUS_CONNECTING     = 1;
    public static final int                     STATUS_CONNECT_ERROR  = 2;
    public static final int                     STATUS_DOWNLOADING    = 3;
    public static final int                     STATUS_DOWNLOAD_ERROR = 5;
    public static final int                     STATUS_NOT_DOWNLOAD   = 0;
    public static final int                     STATUS_PAUSED         = 4;
    public static final int                     STATUS_WAIT_DOWNLOAD  = 7;
    public long        createTime;
    public int         downloadStatus;
    public int         progress;
    public String      savedPath;
    public SportDetail sport;
    public int         videoSize;

    public boolean canDownload() {
        return this.downloadStatus == 0 || this.downloadStatus == 4 || this.downloadStatus == 5
                || this.downloadStatus == 2;
    }

    public boolean inConnectAndDownload() {
        return this.downloadStatus == 3 || this.downloadStatus == 1;
    }

    public boolean inComplete() {
        return this.downloadStatus == 6;
    }

    public boolean inWaitDownload() {
        return this.downloadStatus == 7;
    }

    public boolean inDownloading() {
        return this.downloadStatus == 3;
    }

    public boolean inConnecting() {
        return this.downloadStatus == 1;
    }

    public DownloadRecord() {
        this.createTime = System.currentTimeMillis();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.savedPath);
        dest.writeInt(this.downloadStatus);
        dest.writeInt(this.progress);
        dest.writeInt(this.videoSize);
        dest.writeParcelable(this.sport, flags);
        dest.writeLong(this.createTime);
    }

    protected DownloadRecord(Parcel in) {
        this.savedPath = in.readString();
        this.downloadStatus = in.readInt();
        this.progress = in.readInt();
        this.videoSize = in.readInt();
        this.sport = (SportDetail) in.readParcelable(SportDetail.class.getClassLoader());
        this.createTime = in.readLong();
    }
}
