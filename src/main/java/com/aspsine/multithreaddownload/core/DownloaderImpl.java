package com.aspsine.multithreaddownload.core;

import com.aspsine.multithreaddownload.DownloadConfiguration;
import com.aspsine.multithreaddownload.DownloadException;
import com.aspsine.multithreaddownload.DownloadInfo;
import com.aspsine.multithreaddownload.DownloadRequest;
import com.aspsine.multithreaddownload.architecture.ConnectTask;
import com.aspsine.multithreaddownload.architecture.ConnectTask.OnConnectListener;
import com.aspsine.multithreaddownload.architecture.DownloadResponse;
import com.aspsine.multithreaddownload.architecture.DownloadTask;
import com.aspsine.multithreaddownload.architecture.DownloadTask.OnDownloadListener;
import com.aspsine.multithreaddownload.architecture.Downloader;
import com.aspsine.multithreaddownload.architecture.Downloader.OnDownloaderDestroyedListener;
import com.aspsine.multithreaddownload.db.DataBaseManager;
import com.aspsine.multithreaddownload.db.ThreadInfo;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;

public class DownloaderImpl implements Downloader, OnConnectListener, OnDownloadListener {
    private final DownloadConfiguration mConfig;
    private ConnectTask mConnectTask;
    private final DataBaseManager mDBManager;
    private final DownloadInfo mDownloadInfo = new DownloadInfo(this.mRequest.getTitle().toString(), this.mRequest.getUri(), this.mRequest.getFolder());
    private final List<DownloadTask> mDownloadTasks = new LinkedList();
    private final Executor mExecutor;
    private final OnDownloaderDestroyedListener mListener;
    private final DownloadRequest mRequest;
    private final DownloadResponse mResponse;
    private volatile int mStatus;
    private final String mTag;

    public DownloaderImpl(DownloadRequest request, DownloadResponse response, Executor executor, DataBaseManager dbManager, String key, DownloadConfiguration config, OnDownloaderDestroyedListener listener) {
        this.mRequest = request;
        this.mResponse = response;
        this.mExecutor = executor;
        this.mDBManager = dbManager;
        this.mTag = key;
        this.mConfig = config;
        this.mListener = listener;
    }

    public boolean isRunning() {
        return this.mStatus == 101 || this.mStatus == 102 || this.mStatus == 103 || this.mStatus == 104;
    }

    public void start() {
        this.mStatus = 101;
        this.mResponse.onStarted();
        connect();
    }

    public void pause() {
        if (this.mConnectTask != null) {
            this.mConnectTask.cancel();
        }
        for (DownloadTask task : this.mDownloadTasks) {
            task.pause();
        }
    }

    public void cancel() {
        if (this.mConnectTask != null) {
            this.mConnectTask.cancel();
        }
        for (DownloadTask task : this.mDownloadTasks) {
            task.cancel();
        }
    }

    public void onDestroy() {
        this.mListener.onDestroyed(this.mTag, this);
    }

    public void onConnecting() {
        this.mStatus = 102;
        this.mResponse.onConnecting();
    }

    public void onConnected(long time, long length, boolean isAcceptRanges) {
        this.mStatus = 103;
        this.mResponse.onConnected(time, length, isAcceptRanges);
        this.mDownloadInfo.setLength(length);
        download(length, isAcceptRanges);
    }

    public void onConnectFailed(DownloadException de) {
        this.mStatus = 108;
        onDestroy();
        this.mResponse.onConnectFailed(de);
    }

    public void onConnectCanceled() {
        this.mStatus = 107;
        onDestroy();
        this.mResponse.onConnectCanceled();
    }

    public void onDownloadConnecting() {
    }

    public void onDownloadProgress(long finished, long length) {
        this.mStatus = 104;
        this.mResponse.onDownloadProgress(finished, length, (int) ((100 * finished) / length));
    }

    public void onDownloadCompleted() {
        if (isAllComplete()) {
            deleteFromDB();
            this.mStatus = 105;
            onDestroy();
            this.mResponse.onDownloadCompleted();
        }
    }

    public void onDownloadPaused() {
        if (isAllPaused()) {
            this.mStatus = 106;
            onDestroy();
            this.mResponse.onDownloadPaused();
        }
    }

    public void onDownloadCanceled() {
        if (isAllCanceled()) {
            deleteFromDB();
            File file = new File(this.mDownloadInfo.getDir(), this.mDownloadInfo.getName());
            if (file.exists()) {
                file.delete();
            }
            this.mStatus = 107;
            onDestroy();
            this.mResponse.onDownloadCanceled();
        }
    }

    public void onDownloadFailed(DownloadException de) {
        if (isAllFailed()) {
            this.mStatus = 108;
            onDestroy();
            this.mResponse.onDownloadFailed(de);
        }
    }

    private void connect() {
        this.mConnectTask = new ConnectTaskImpl(this.mRequest.getUri(), this);
        this.mExecutor.execute(this.mConnectTask);
    }

    private void download(long length, boolean acceptRanges) {
        initDownloadTasks(length, acceptRanges);
        for (DownloadTask downloadTask : this.mDownloadTasks) {
            this.mExecutor.execute(downloadTask);
        }
    }

    private void initDownloadTasks(long length, boolean acceptRanges) {
        this.mDownloadTasks.clear();
        if (acceptRanges) {
            List<ThreadInfo> threadInfos = getMultiThreadInfos(length);
            int finished = 0;
            for (ThreadInfo threadInfo : threadInfos) {
                finished = (int) (((long) finished) + threadInfo.getFinished());
            }
            this.mDownloadInfo.setFinished((long) finished);
            for (ThreadInfo info : threadInfos) {
                this.mDownloadTasks.add(new MultiDownloadTask(this.mDownloadInfo, info, this.mDBManager, this));
            }
            return;
        }
        this.mDownloadTasks.add(new SingleDownloadTask(this.mDownloadInfo, getSingleThreadInfo(), this));
    }

    private List<ThreadInfo> getMultiThreadInfos(long length) {
        List<ThreadInfo> threadInfos = this.mDBManager.getThreadInfos(this.mTag);
        File file = new File(this.mDownloadInfo.getDir(), this.mDownloadInfo.getName());
        if (!(threadInfos.isEmpty() || file.exists())) {
            threadInfos.clear();
            this.mDBManager.delete(this.mTag);
        }
        if (threadInfos.isEmpty()) {
            int threadNum = this.mConfig.getThreadNum();
            for (int i = 0; i < threadNum; i++) {
                long end;
                long average = length / ((long) threadNum);
                long start = average * ((long) i);
                if (i == threadNum - 1) {
                    end = length;
                } else {
                    end = (start + average) - 1;
                }
                threadInfos.add(new ThreadInfo(i, this.mTag, this.mRequest.getUri(), start, end, 0));
            }
        }
        return threadInfos;
    }

    private ThreadInfo getSingleThreadInfo() {
        return new ThreadInfo(0, this.mTag, this.mRequest.getUri(), 0);
    }

    private boolean isAllComplete() {
        for (DownloadTask task : this.mDownloadTasks) {
            if (!task.isComplete()) {
                return false;
            }
        }
        return true;
    }

    private boolean isAllFailed() {
        for (DownloadTask task : this.mDownloadTasks) {
            if (task.isDownloading()) {
                return false;
            }
        }
        return true;
    }

    private boolean isAllPaused() {
        for (DownloadTask task : this.mDownloadTasks) {
            if (task.isDownloading()) {
                return false;
            }
        }
        return true;
    }

    private boolean isAllCanceled() {
        for (DownloadTask task : this.mDownloadTasks) {
            if (task.isDownloading()) {
                return false;
            }
        }
        return true;
    }

    private void deleteFromDB() {
        this.mDBManager.delete(this.mTag);
    }
}
