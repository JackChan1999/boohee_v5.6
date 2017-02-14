package com.aspsine.multithreaddownload;

public class DownloadConfiguration {
    public static final int DEFAULT_MAX_THREAD_NUMBER = 10;
    public static final int DEFAULT_THREAD_NUMBER = 1;
    private int maxThreadNum = 10;
    private int threadNum = 1;

    public int getMaxThreadNum() {
        return this.maxThreadNum;
    }

    public void setMaxThreadNum(int maxThreadNum) {
        this.maxThreadNum = maxThreadNum;
    }

    public int getThreadNum() {
        return this.threadNum;
    }

    public void setThreadNum(int threadNum) {
        this.threadNum = threadNum;
    }
}
