package com.aspsine.multithreaddownload;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import com.aspsine.multithreaddownload.architecture.DownloadStatusDelivery;
import com.aspsine.multithreaddownload.architecture.Downloader;
import com.aspsine.multithreaddownload.architecture.Downloader.OnDownloaderDestroyedListener;
import com.aspsine.multithreaddownload.core.DownloadResponseImpl;
import com.aspsine.multithreaddownload.core.DownloadStatusDeliveryImpl;
import com.aspsine.multithreaddownload.core.DownloaderImpl;
import com.aspsine.multithreaddownload.db.DataBaseManager;
import com.aspsine.multithreaddownload.db.ThreadInfo;
import com.aspsine.multithreaddownload.util.L;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DownloadManager implements OnDownloaderDestroyedListener {
    public static final String TAG = DownloadManager.class.getSimpleName();
    private static volatile DownloadManager sDownloadManager;
    private DownloadConfiguration mConfig;
    private DataBaseManager mDBManager;
    private DownloadStatusDelivery mDelivery;
    private final Map<String, Downloader> mDownloaderMap = new ConcurrentHashMap();
    private ExecutorService mExecutorService;

    public static DownloadManager getInstance() {
        if (sDownloadManager == null) {
            synchronized (DownloadManager.class) {
                if (sDownloadManager == null) {
                    sDownloadManager = new DownloadManager();
                }
            }
        }
        return sDownloadManager;
    }

    private DownloadManager() {
    }

    public void init(Context context) {
        init(context, new DownloadConfiguration());
    }

    public void init(Context context, @NonNull DownloadConfiguration config) {
        if (this.mConfig != null) {
            L.w("DownloadManger has already been initialized before");
        } else if (config.getThreadNum() > config.getMaxThreadNum()) {
            throw new IllegalArgumentException("thread num must < max thread num");
        } else {
            this.mConfig = config;
            this.mDBManager = DataBaseManager.getInstance(context);
            ThreadPoolExecutor executor = new ThreadPoolExecutor(this.mConfig.getMaxThreadNum(), this.mConfig.getMaxThreadNum(), 60, TimeUnit.SECONDS, new LinkedBlockingQueue());
            executor.allowCoreThreadTimeOut(true);
            this.mExecutorService = executor;
            this.mDelivery = new DownloadStatusDeliveryImpl(new Handler(Looper.getMainLooper()));
        }
    }

    public void onDestroyed(String key, Downloader downloader) {
        if (this.mDownloaderMap.containsKey(key)) {
            this.mDownloaderMap.remove(key);
        }
    }

    public void download(DownloadRequest request, String tag, CallBack callBack) {
        String key = createKey(tag);
        if (check(key)) {
            Downloader downloader = new DownloaderImpl(request, new DownloadResponseImpl(this.mDelivery, callBack), this.mExecutorService, this.mDBManager, key, this.mConfig, this);
            this.mDownloaderMap.put(key, downloader);
            downloader.start();
        }
    }

    public boolean pause(String tag) {
        String key = createKey(tag);
        if (!this.mDownloaderMap.containsKey(key)) {
            return false;
        }
        Downloader downloader = (Downloader) this.mDownloaderMap.get(key);
        if (downloader != null && downloader.isRunning()) {
            downloader.pause();
        }
        this.mDownloaderMap.remove(key);
        return true;
    }

    public boolean cancel(String tag) {
        String key = createKey(tag);
        if (!this.mDownloaderMap.containsKey(key)) {
            return false;
        }
        Downloader downloader = (Downloader) this.mDownloaderMap.get(key);
        if (downloader != null) {
            downloader.cancel();
        }
        this.mDownloaderMap.remove(key);
        return true;
    }

    public void pauseAll() {
        for (Downloader downloader : this.mDownloaderMap.values()) {
            if (downloader != null && downloader.isRunning()) {
                downloader.pause();
            }
        }
        this.mDownloaderMap.clear();
    }

    public void cancelAll() {
        for (Downloader downloader : this.mDownloaderMap.values()) {
            if (downloader != null && downloader.isRunning()) {
                downloader.cancel();
            }
        }
        this.mDownloaderMap.clear();
    }

    public void clearDb(String tag) {
        this.mDBManager.delete(createKey(tag));
    }

    public DownloadInfo getDownloadProgress(String tag) {
        List<ThreadInfo> threadInfos = this.mDBManager.getThreadInfos(createKey(tag));
        if (threadInfos.isEmpty()) {
            return null;
        }
        int finished = 0;
        int total = 0;
        for (ThreadInfo info : threadInfos) {
            finished = (int) (((long) finished) + info.getFinished());
            total = (int) (((long) total) + (info.getEnd() - info.getStart()));
        }
        int progress = (int) ((((long) finished) * 100) / ((long) total));
        DownloadInfo downloadInfo = new DownloadInfo();
        downloadInfo.setFinished((long) finished);
        downloadInfo.setLength((long) total);
        downloadInfo.setProgress(progress);
        return downloadInfo;
    }

    private boolean check(String key) {
        if (!this.mDownloaderMap.containsKey(key)) {
            return true;
        }
        Downloader downloader = (Downloader) this.mDownloaderMap.get(key);
        if (downloader == null) {
            return true;
        }
        if (downloader.isRunning()) {
            L.w("Task has been started!");
            return false;
        }
        L.w("Downloader instance with same tag has not been destroyed!");
        this.mDownloaderMap.remove(key);
        return true;
    }

    private static String createKey(String tag) {
        if (tag != null) {
            return String.valueOf(tag.hashCode());
        }
        throw new NullPointerException("Tag can't be null!");
    }
}
