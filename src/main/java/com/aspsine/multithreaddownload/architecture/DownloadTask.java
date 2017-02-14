package com.aspsine.multithreaddownload.architecture;

import com.aspsine.multithreaddownload.DownloadException;

public interface DownloadTask extends Runnable {

    public interface OnDownloadListener {
        void onDownloadCanceled();

        void onDownloadCompleted();

        void onDownloadConnecting();

        void onDownloadFailed(DownloadException downloadException);

        void onDownloadPaused();

        void onDownloadProgress(long j, long j2);
    }

    void cancel();

    boolean isCanceled();

    boolean isComplete();

    boolean isDownloading();

    boolean isFailed();

    boolean isPaused();

    void pause();

    void run();
}
