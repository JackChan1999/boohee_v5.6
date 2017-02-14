package com.aspsine.multithreaddownload.core;

import com.aspsine.multithreaddownload.CallBack;
import com.aspsine.multithreaddownload.DownloadException;
import com.aspsine.multithreaddownload.architecture.DownloadResponse;
import com.aspsine.multithreaddownload.architecture.DownloadStatus;
import com.aspsine.multithreaddownload.architecture.DownloadStatusDelivery;

public class DownloadResponseImpl implements DownloadResponse {
    private DownloadStatusDelivery mDelivery;
    private DownloadStatus mDownloadStatus = new DownloadStatus();

    public DownloadResponseImpl(DownloadStatusDelivery delivery, CallBack callBack) {
        this.mDelivery = delivery;
        this.mDownloadStatus.setCallBack(callBack);
    }

    public void onStarted() {
        this.mDownloadStatus.setStatus(101);
        this.mDownloadStatus.getCallBack().onStarted();
    }

    public void onConnecting() {
        this.mDownloadStatus.setStatus(102);
        this.mDelivery.post(this.mDownloadStatus);
    }

    public void onConnected(long time, long length, boolean acceptRanges) {
        this.mDownloadStatus.setTime(time);
        this.mDownloadStatus.setAcceptRanges(acceptRanges);
        this.mDownloadStatus.setStatus(103);
        this.mDelivery.post(this.mDownloadStatus);
    }

    public void onConnectFailed(DownloadException e) {
        this.mDownloadStatus.setException(e);
        this.mDownloadStatus.setStatus(108);
        this.mDelivery.post(this.mDownloadStatus);
    }

    public void onConnectCanceled() {
        this.mDownloadStatus.setStatus(107);
        this.mDelivery.post(this.mDownloadStatus);
    }

    public void onDownloadProgress(long finished, long length, int percent) {
        this.mDownloadStatus.setFinished(finished);
        this.mDownloadStatus.setLength(length);
        this.mDownloadStatus.setPercent(percent);
        this.mDownloadStatus.setStatus(104);
        this.mDelivery.post(this.mDownloadStatus);
    }

    public void onDownloadCompleted() {
        this.mDownloadStatus.setStatus(105);
        this.mDelivery.post(this.mDownloadStatus);
    }

    public void onDownloadPaused() {
        this.mDownloadStatus.setStatus(106);
        this.mDelivery.post(this.mDownloadStatus);
    }

    public void onDownloadCanceled() {
        this.mDownloadStatus.setStatus(107);
        this.mDelivery.post(this.mDownloadStatus);
    }

    public void onDownloadFailed(DownloadException e) {
        this.mDownloadStatus.setException(e);
        this.mDownloadStatus.setStatus(108);
        this.mDelivery.post(this.mDownloadStatus);
    }
}
