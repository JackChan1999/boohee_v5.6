package com.aspsine.multithreaddownload.core;

import com.aspsine.multithreaddownload.DownloadInfo;
import com.aspsine.multithreaddownload.architecture.DownloadTask.OnDownloadListener;
import com.aspsine.multithreaddownload.db.DataBaseManager;
import com.aspsine.multithreaddownload.db.ThreadInfo;
import com.umeng.socialize.common.SocializeConstants;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

public class MultiDownloadTask extends DownloadTaskImpl {
    private DataBaseManager mDBManager;

    public MultiDownloadTask(DownloadInfo downloadInfo, ThreadInfo threadInfo, DataBaseManager dbManager, OnDownloadListener listener) {
        super(downloadInfo, threadInfo, listener);
        this.mDBManager = dbManager;
    }

    protected void insertIntoDB(ThreadInfo info) {
        if (!this.mDBManager.exists(info.getTag(), info.getId())) {
            this.mDBManager.insert(info);
        }
    }

    protected int getResponseCode() {
        return 206;
    }

    protected void updateDB(ThreadInfo info) {
        this.mDBManager.update(info.getTag(), info.getId(), info.getFinished());
    }

    protected Map<String, String> getHttpHeaders(ThreadInfo info) {
        Map<String, String> headers = new HashMap();
        long start = info.getStart() + info.getFinished();
        headers.put("Range", "bytes=" + start + SocializeConstants.OP_DIVIDER_MINUS + info.getEnd());
        return headers;
    }

    protected RandomAccessFile getFile(File dir, String name, long offset) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(new File(dir, name), "rwd");
        raf.seek(offset);
        return raf;
    }

    protected String getTag() {
        return getClass().getSimpleName();
    }
}
