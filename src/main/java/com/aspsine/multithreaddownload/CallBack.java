package com.aspsine.multithreaddownload;

public interface CallBack {
    void onCompleted();

    void onConnected(long j, boolean z);

    void onConnecting();

    void onDownloadCanceled();

    void onDownloadPaused();

    void onFailed(DownloadException downloadException);

    void onProgress(long j, long j2, int i);

    void onStarted();
}
