package com.aspsine.multithreaddownload.core;

import android.os.Process;
import com.aspsine.multithreaddownload.DownloadException;
import com.aspsine.multithreaddownload.DownloadInfo;
import com.aspsine.multithreaddownload.architecture.DownloadTask;
import com.aspsine.multithreaddownload.architecture.DownloadTask.OnDownloadListener;
import com.aspsine.multithreaddownload.db.ThreadInfo;
import com.aspsine.multithreaddownload.util.IOCloseUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public abstract class DownloadTaskImpl implements DownloadTask {
    private volatile int mCommend = 0;
    private final DownloadInfo mDownloadInfo;
    private long mLastTime;
    private final OnDownloadListener mOnDownloadListener;
    private volatile int mStatus;
    private final ThreadInfo mThreadInfo;

    protected abstract RandomAccessFile getFile(File file, String str, long j) throws IOException;

    protected abstract Map<String, String> getHttpHeaders(ThreadInfo threadInfo);

    protected abstract int getResponseCode();

    protected abstract String getTag();

    protected abstract void insertIntoDB(ThreadInfo threadInfo);

    protected abstract void updateDB(ThreadInfo threadInfo);

    public DownloadTaskImpl(DownloadInfo downloadInfo, ThreadInfo threadInfo, OnDownloadListener listener) {
        this.mDownloadInfo = downloadInfo;
        this.mThreadInfo = threadInfo;
        this.mOnDownloadListener = listener;
    }

    public void cancel() {
        this.mCommend = 107;
    }

    public void pause() {
        this.mCommend = 106;
    }

    public boolean isDownloading() {
        return this.mStatus == 104;
    }

    public boolean isComplete() {
        return this.mStatus == 105;
    }

    public boolean isPaused() {
        return this.mStatus == 106;
    }

    public boolean isCanceled() {
        return this.mStatus == 107;
    }

    public boolean isFailed() {
        return this.mStatus == 108;
    }

    public void run() {
        Process.setThreadPriority(10);
        insertIntoDB(this.mThreadInfo);
        try {
            this.mStatus = 104;
            executeDownload();
            synchronized (this.mOnDownloadListener) {
                this.mStatus = 105;
                this.mOnDownloadListener.onDownloadCompleted();
            }
        } catch (DownloadException e) {
            handleDownloadException(e);
        }
    }

    private void handleDownloadException(DownloadException e) {
        updateDB(this.mThreadInfo);
        if (isPaused()) {
            synchronized (this.mOnDownloadListener) {
                this.mStatus = 106;
                this.mOnDownloadListener.onDownloadPaused();
            }
        } else if (isCanceled()) {
            synchronized (this.mOnDownloadListener) {
                this.mStatus = 107;
                this.mOnDownloadListener.onDownloadCanceled();
            }
        } else {
            switch (e.getErrorCode()) {
                case 106:
                    synchronized (this.mOnDownloadListener) {
                        this.mStatus = 106;
                        this.mOnDownloadListener.onDownloadPaused();
                    }
                    return;
                case 107:
                    synchronized (this.mOnDownloadListener) {
                        this.mStatus = 107;
                        this.mOnDownloadListener.onDownloadCanceled();
                    }
                    return;
                case 108:
                    synchronized (this.mOnDownloadListener) {
                        this.mStatus = 108;
                        this.mOnDownloadListener.onDownloadFailed(e);
                    }
                    return;
                default:
                    throw new IllegalArgumentException("Unknown state");
            }
        }
    }

    private void executeDownload() throws DownloadException {
        try {
            HttpURLConnection httpURLConnection = null;
            try {
                httpURLConnection = (HttpURLConnection) new URL(this.mThreadInfo.getUri()).openConnection();
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setReadTimeout(15000);
                httpURLConnection.setRequestMethod("GET");
                setHttpHeader(getHttpHeaders(this.mThreadInfo), httpURLConnection);
                int responseCode = httpURLConnection.getResponseCode();
                if (responseCode == getResponseCode()) {
                    transferData(httpURLConnection);
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                        return;
                    }
                    return;
                }
                throw new DownloadException(108, "UnSupported response code:" + responseCode);
            } catch (ProtocolException e) {
                throw new DownloadException(108, "Protocol error", e);
            } catch (IOException e2) {
                throw new DownloadException(108, "IO error", e2);
            } catch (Throwable th) {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }
        } catch (MalformedURLException e3) {
            throw new DownloadException(108, "Bad url.", e3);
        }
    }

    private void setHttpHeader(Map<String, String> headers, URLConnection connection) {
        if (headers != null) {
            for (String key : headers.keySet()) {
                connection.setRequestProperty(key, (String) headers.get(key));
            }
        }
    }

    private void transferData(HttpURLConnection httpConnection) throws DownloadException {
        InputStream inputStream = null;
        RandomAccessFile raf = null;
        try {
            inputStream = httpConnection.getInputStream();
            raf = getFile(this.mDownloadInfo.getDir(), this.mDownloadInfo.getName(), this.mThreadInfo.getStart() + this.mThreadInfo.getFinished());
            transferData(inputStream, raf);
            try {
                IOCloseUtils.close(inputStream);
                IOCloseUtils.close(raf);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e2) {
            throw new DownloadException(108, "File error", e2);
        } catch (IOException e22) {
            throw new DownloadException(108, "http get inputStream error", e22);
        } catch (Throwable th) {
            try {
                IOCloseUtils.close(inputStream);
                IOCloseUtils.close(raf);
            } catch (IOException e222) {
                e222.printStackTrace();
            }
        }
    }

    private void transferData(InputStream inputStream, RandomAccessFile raf) throws DownloadException {
        byte[] buffer = new byte[16384];
        while (true) {
            checkPausedOrCanceled();
            try {
                int len = inputStream.read(buffer);
                if (len != -1) {
                    try {
                        checkPausedOrCanceled();
                        raf.write(buffer, 0, len);
                        this.mThreadInfo.setFinished(this.mThreadInfo.getFinished() + ((long) len));
                        savePeriod();
                        synchronized (this.mOnDownloadListener) {
                            this.mDownloadInfo.setFinished(this.mDownloadInfo.getFinished() + ((long) len));
                            this.mOnDownloadListener.onDownloadProgress(this.mDownloadInfo.getFinished(), this.mDownloadInfo.getLength());
                        }
                    } catch (IOException e) {
                        throw new DownloadException(108, "Fail write buffer to file", e);
                    }
                }
                return;
            } catch (IOException e2) {
                throw new DownloadException(108, "Http inputStream read error", e2);
            }
        }
    }

    private void checkPausedOrCanceled() throws DownloadException {
        if (this.mCommend == 107) {
            throw new DownloadException(107, "Download canceled!");
        } else if (this.mCommend == 106) {
            updateDB(this.mThreadInfo);
            throw new DownloadException(106, "Download paused!");
        }
    }

    private void savePeriod() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - this.mLastTime > 3000) {
            updateDB(this.mThreadInfo);
            this.mLastTime = currentTime;
        }
    }
}
