package com.aspsine.multithreaddownload.core;

import com.aspsine.multithreaddownload.DownloadInfo;
import com.aspsine.multithreaddownload.architecture.DownloadTask.OnDownloadListener;
import com.aspsine.multithreaddownload.db.ThreadInfo;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Map;

public class SingleDownloadTask extends DownloadTaskImpl {
    public SingleDownloadTask(DownloadInfo mDownloadInfo, ThreadInfo mThreadInfo, OnDownloadListener mOnDownloadListener) {
        super(mDownloadInfo, mThreadInfo, mOnDownloadListener);
    }

    protected void insertIntoDB(ThreadInfo info) {
    }

    protected int getResponseCode() {
        return 200;
    }

    protected void updateDB(ThreadInfo info) {
    }

    protected Map<String, String> getHttpHeaders(ThreadInfo info) {
        return null;
    }

    protected RandomAccessFile getFile(File dir, String name, long offset) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(new File(dir, name), "rwd");
        raf.seek(0);
        return raf;
    }

    protected String getTag() {
        return getClass().getSimpleName();
    }
}
