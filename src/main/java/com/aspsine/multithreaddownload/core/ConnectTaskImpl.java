package com.aspsine.multithreaddownload.core;

import android.os.Process;
import android.text.TextUtils;
import com.aspsine.multithreaddownload.DownloadException;
import com.aspsine.multithreaddownload.architecture.ConnectTask;
import com.aspsine.multithreaddownload.architecture.ConnectTask.OnConnectListener;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class ConnectTaskImpl implements ConnectTask {
    private final OnConnectListener mOnConnectListener;
    private volatile long mStartTime;
    private volatile int mStatus;
    private final String mUri;

    public ConnectTaskImpl(String uri, OnConnectListener listener) {
        this.mUri = uri;
        this.mOnConnectListener = listener;
    }

    public void cancel() {
        this.mStatus = 107;
    }

    public boolean isConnecting() {
        return this.mStatus == 102;
    }

    public boolean isConnected() {
        return this.mStatus == 103;
    }

    public boolean isCanceled() {
        return this.mStatus == 107;
    }

    public boolean isFailed() {
        return this.mStatus == 108;
    }

    public void run() {
        Process.setThreadPriority(10);
        this.mStatus = 102;
        this.mOnConnectListener.onConnecting();
        try {
            executeConnection();
        } catch (DownloadException e) {
            handleDownloadException(e);
        }
    }

    private void executeConnection() throws DownloadException {
        this.mStartTime = System.currentTimeMillis();
        HttpURLConnection httpConnection = null;
        try {
            try {
                httpConnection = (HttpURLConnection) new URL(this.mUri).openConnection();
                httpConnection.setConnectTimeout(15000);
                httpConnection.setReadTimeout(15000);
                httpConnection.setRequestMethod("GET");
                httpConnection.setRequestProperty("Range", "bytes=0-");
                int responseCode = httpConnection.getResponseCode();
                if (responseCode == 200) {
                    parseResponse(httpConnection, false);
                } else if (responseCode == 206) {
                    parseResponse(httpConnection, true);
                } else {
                    throw new DownloadException(108, "UnSupported response code:" + responseCode);
                }
                if (httpConnection != null) {
                    httpConnection.disconnect();
                }
            } catch (ProtocolException e) {
                throw new DownloadException(108, "Protocol error", e);
            } catch (IOException e2) {
                throw new DownloadException(108, "IO error", e2);
            } catch (Throwable th) {
                if (httpConnection != null) {
                    httpConnection.disconnect();
                }
            }
        } catch (MalformedURLException e3) {
            throw new DownloadException(108, "Bad url.", e3);
        }
    }

    private void parseResponse(HttpURLConnection httpConnection, boolean isAcceptRanges) throws DownloadException {
        long length;
        String contentLength = httpConnection.getHeaderField("Content-Length");
        if (TextUtils.isEmpty(contentLength) || contentLength.equals("0") || contentLength.equals("-1")) {
            length = (long) httpConnection.getContentLength();
        } else {
            length = Long.parseLong(contentLength);
        }
        if (length <= 0) {
            throw new DownloadException(108, "length <= 0");
        }
        checkCanceled();
        this.mStatus = 103;
        this.mOnConnectListener.onConnected(System.currentTimeMillis() - this.mStartTime, length, isAcceptRanges);
    }

    private void checkCanceled() throws DownloadException {
        if (isCanceled()) {
            throw new DownloadException(107, "Download paused!");
        }
    }

    private void handleDownloadException(DownloadException e) {
        switch (e.getErrorCode()) {
            case 107:
                synchronized (this.mOnConnectListener) {
                    this.mStatus = 107;
                    this.mOnConnectListener.onConnectCanceled();
                }
                return;
            case 108:
                synchronized (this.mOnConnectListener) {
                    this.mStatus = 108;
                    this.mOnConnectListener.onConnectFailed(e);
                }
                return;
            default:
                throw new IllegalArgumentException("Unknown state");
        }
    }
}
