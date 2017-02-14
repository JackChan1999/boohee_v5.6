package com.aspsine.multithreaddownload.architecture;

import com.aspsine.multithreaddownload.DownloadException;

public interface DownloadResponse {
    void onConnectCanceled();

    void onConnectFailed(DownloadException downloadException);

    void onConnected(long j, long j2, boolean z);

    void onConnecting();

    void onDownloadCanceled();

    void onDownloadCompleted();

    void onDownloadFailed(DownloadException downloadException);

    void onDownloadPaused();

    void onDownloadProgress(long j, long j2, int i);

    void onStarted();
}
