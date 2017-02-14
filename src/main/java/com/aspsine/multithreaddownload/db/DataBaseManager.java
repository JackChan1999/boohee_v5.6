package com.aspsine.multithreaddownload.db;

import android.content.Context;
import java.util.List;

public class DataBaseManager {
    private static DataBaseManager sDataBaseManager;
    private final ThreadInfoDao mThreadInfoDao;

    public static DataBaseManager getInstance(Context context) {
        if (sDataBaseManager == null) {
            sDataBaseManager = new DataBaseManager(context);
        }
        return sDataBaseManager;
    }

    private DataBaseManager(Context context) {
        this.mThreadInfoDao = new ThreadInfoDao(context);
    }

    public synchronized void insert(ThreadInfo threadInfo) {
        this.mThreadInfoDao.insert(threadInfo);
    }

    public synchronized void delete(String tag) {
        this.mThreadInfoDao.delete(tag);
    }

    public synchronized void update(String tag, int threadId, long finished) {
        this.mThreadInfoDao.update(tag, threadId, finished);
    }

    public List<ThreadInfo> getThreadInfos(String tag) {
        return this.mThreadInfoDao.getThreadInfos(tag);
    }

    public boolean exists(String tag, int threadId) {
        return this.mThreadInfoDao.exists(tag, threadId);
    }
}
