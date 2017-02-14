package com.aspsine.multithreaddownload.architecture;

import com.aspsine.multithreaddownload.CallBack;
import com.aspsine.multithreaddownload.DownloadException;

public class DownloadStatus {
    public static final int STATUS_CANCELED = 107;
    public static final int STATUS_COMPLETED = 105;
    public static final int STATUS_CONNECTED = 103;
    public static final int STATUS_CONNECTING = 102;
    public static final int STATUS_FAILED = 108;
    public static final int STATUS_PAUSED = 106;
    public static final int STATUS_PROGRESS = 104;
    public static final int STATUS_STARTED = 101;
    private boolean acceptRanges;
    private CallBack callBack;
    private DownloadException exception;
    private long finished;
    private long length;
    private int percent;
    private int status;
    private long time;

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getLength() {
        return this.length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public long getFinished() {
        return this.finished;
    }

    public void setFinished(long finished) {
        this.finished = finished;
    }

    public int getPercent() {
        return this.percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public boolean isAcceptRanges() {
        return this.acceptRanges;
    }

    public void setAcceptRanges(boolean acceptRanges) {
        this.acceptRanges = acceptRanges;
    }

    public Exception getException() {
        return this.exception;
    }

    public void setException(DownloadException exception) {
        this.exception = exception;
    }

    public CallBack getCallBack() {
        return this.callBack;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
}
