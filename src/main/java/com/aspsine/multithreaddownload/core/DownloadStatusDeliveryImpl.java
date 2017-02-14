package com.aspsine.multithreaddownload.core;

import android.os.Handler;
import com.aspsine.multithreaddownload.CallBack;
import com.aspsine.multithreaddownload.DownloadException;
import com.aspsine.multithreaddownload.architecture.DownloadStatus;
import com.aspsine.multithreaddownload.architecture.DownloadStatusDelivery;
import java.util.concurrent.Executor;

public class DownloadStatusDeliveryImpl implements DownloadStatusDelivery {
    private Executor mDownloadStatusPoster;

    private static class DownloadStatusDeliveryRunnable implements Runnable {
        private final CallBack mCallBack = this.mDownloadStatus.getCallBack();
        private final DownloadStatus mDownloadStatus;

        public DownloadStatusDeliveryRunnable(DownloadStatus downloadStatus) {
            this.mDownloadStatus = downloadStatus;
        }

        public void run() {
            switch (this.mDownloadStatus.getStatus()) {
                case 102:
                    this.mCallBack.onConnecting();
                    return;
                case 103:
                    this.mCallBack.onConnected(this.mDownloadStatus.getLength(), this.mDownloadStatus.isAcceptRanges());
                    return;
                case 104:
                    this.mCallBack.onProgress(this.mDownloadStatus.getFinished(), this.mDownloadStatus.getLength(), this.mDownloadStatus.getPercent());
                    return;
                case 105:
                    this.mCallBack.onCompleted();
                    return;
                case 106:
                    this.mCallBack.onDownloadPaused();
                    return;
                case 107:
                    this.mCallBack.onDownloadCanceled();
                    return;
                case 108:
                    this.mCallBack.onFailed((DownloadException) this.mDownloadStatus.getException());
                    return;
                default:
                    return;
            }
        }
    }

    public DownloadStatusDeliveryImpl(final Handler handler) {
        this.mDownloadStatusPoster = new Executor() {
            public void execute(Runnable command) {
                handler.post(command);
            }
        };
    }

    public void post(DownloadStatus status) {
        this.mDownloadStatusPoster.execute(new DownloadStatusDeliveryRunnable(status));
    }
}
