package com.boohee.one.sport;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.alibaba.fastjson.TypeReference;
import com.aspsine.multithreaddownload.Constants.HTTP;
import com.aspsine.multithreaddownload.DownloadConfiguration;
import com.aspsine.multithreaddownload.DownloadInfo;
import com.aspsine.multithreaddownload.DownloadManager;
import com.boohee.one.MyApplication;
import com.boohee.one.cache.CacheKey;
import com.boohee.one.cache.FileCache;
import com.boohee.one.sport.model.DownloadRecord;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.FileUtil;
import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class DownloadHelper {
    private static final String DOWNLOAD_RECORD     = "Sport_Download";
    public static final  String VIDEO_DOWNLOAD_PATH = ".SPORT_VIDEOS";
    public static final  String VIDEO_FORMAT        = ".mp4";
    private static volatile DownloadHelper sInstance;
    private                 boolean        hasInit;
    private                 Context        mContext;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private long              mLastTime;
    private SharedPreferences mPreferences;
    private FileNameGenerator nameGenerator = new Md5FileNameGenerator();
    private Map<String, DownloadRecord> recordMap;

    public interface VideoSizeCallback {
        void onResponse(int i);
    }

    public static DownloadHelper getInstance() {
        if (sInstance == null) {
            synchronized (DownloadHelper.class) {
                if (sInstance == null) {
                    sInstance = new DownloadHelper(MyApplication.getContext());
                }
            }
        }
        return sInstance;
    }

    private DownloadHelper(Context context) {
        this.mContext = context.getApplicationContext();
        initRecord(context);
        initDownloader();
        check(context);
    }

    private void initRecord(Context context) {
        this.mPreferences = context.getSharedPreferences(DOWNLOAD_RECORD, 0);
        this.recordMap = (Map) FastJsonUtils.fromJson(this.mPreferences.getString(CacheKey
                .DOWNLOAD_RECORD, null), new TypeReference<Map<String, DownloadRecord>>() {
        });
        if (this.recordMap == null) {
            String oldRecord = FileCache.get(context, DOWNLOAD_RECORD).getAsString(CacheKey
                    .DOWNLOAD_RECORD);
            this.recordMap = (Map) FastJsonUtils.fromJson(oldRecord, new
                    TypeReference<Map<String, DownloadRecord>>() {
            });
            if (this.recordMap == null) {
                this.recordMap = new HashMap();
                return;
            }
            Editor editor = this.mPreferences.edit();
            editor.putString(CacheKey.DOWNLOAD_RECORD, oldRecord);
            editor.apply();
        }
    }

    private void initDownloader() {
        if (!this.hasInit) {
            DownloadConfiguration configuration = new DownloadConfiguration();
            configuration.setMaxThreadNum(5);
            configuration.setThreadNum(3);
            DownloadManager.getInstance().init(this.mContext, configuration);
            this.hasInit = true;
        }
    }

    public DownloadRecord getRecord(String videoUrl) {
        if (TextUtils.isEmpty(videoUrl)) {
            return null;
        }
        return (DownloadRecord) this.recordMap.get(videoUrl);
    }

    public boolean hasDownload(String videoUrl) {
        DownloadRecord record = getRecord(videoUrl);
        return record != null && record.inComplete();
    }

    public List<DownloadRecord> getRecordsList() {
        List<DownloadRecord> recordList = new ArrayList(this.recordMap.values());
        Collections.sort(recordList, new Comparator<DownloadRecord>() {
            public int compare(DownloadRecord r1, DownloadRecord r2) {
                long lhs = r1.createTime;
                long rhs = r2.createTime;
                int result = lhs < rhs ? -1 : lhs == rhs ? 0 : 1;
                return -result;
            }
        });
        return recordList;
    }

    public void deleteDownload(Set<String> videoLists) {
        if (videoLists != null && videoLists.size() != 0) {
            final Set<String> copy = new HashSet();
            copy.addAll(videoLists);
            final List<File> fileList = new ArrayList();
            for (String videoUrl : copy) {
                fileList.add(new File(((DownloadRecord) this.recordMap.get(videoUrl)).savedPath));
                this.recordMap.remove(videoUrl);
            }
            new Thread(new Runnable() {
                public void run() {
                    for (String videoUrl : copy) {
                        if (!DownloadManager.getInstance().cancel(videoUrl)) {
                            DownloadManager.getInstance().clearDb(videoUrl);
                        }
                    }
                    for (File file : fileList) {
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                }
            }).start();
            save();
        }
    }

    private void check(Context context) {
        for (DownloadRecord record : this.recordMap.values()) {
            if (record.videoSize <= 0) {
                DownloadInfo info = DownloadManager.getInstance().getDownloadProgress(record
                        .sport.video_url);
                if (info != null) {
                    record.videoSize = (int) (info.getLength() / 1048576);
                }
            }
            if (record.inDownloading()) {
                record.downloadStatus = 4;
            } else if (record.inWaitDownload() || record.inConnecting()) {
                record.downloadStatus = 4;
                if (!TextUtils.isEmpty(record.savedPath)) {
                    File file = new File(record.savedPath);
                    if (!file.exists()) {
                        try {
                            file.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        updateRecord();
        removeUnusedFile(context);
    }

    private void updateRecord() {
        boolean hasRemove = false;
        Iterator<Entry<String, DownloadRecord>> it = this.recordMap.entrySet().iterator();
        while (it.hasNext()) {
            DownloadRecord record = (DownloadRecord) ((Entry) it.next()).getValue();
            if (record != null) {
                if (TextUtils.isEmpty(record.savedPath)) {
                    hasRemove = true;
                    it.remove();
                } else if (!(record.inWaitDownload() || new File(record.savedPath).exists())) {
                    if (hasVideoUrl(record)) {
                        DownloadManager.getInstance().clearDb(record.sport.video_url);
                    }
                    it.remove();
                    hasRemove = true;
                }
            }
        }
        if (hasRemove) {
            save();
        }
    }

    private void removeUnusedFile(Context context) {
        File cacheDir = null;
        if ("mounted".equals(Environment.getExternalStorageState()) && FileUtil
                .hasExternalStoragePermission(context)) {
            cacheDir = new File(context.getExternalFilesDir(null), VIDEO_DOWNLOAD_PATH);
        }
        if (cacheDir != null && cacheDir.exists()) {
            File[] videoFile = cacheDir.listFiles();
            final List<File> deleteList = new ArrayList();
            if (videoFile != null) {
                for (File file : videoFile) {
                    if (!containsFile(file.getAbsolutePath())) {
                        deleteList.add(file);
                    }
                }
            }
            new Thread(new Runnable() {
                public void run() {
                    for (File file : deleteList) {
                        file.delete();
                    }
                }
            }).start();
        }
    }

    private boolean containsFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        for (DownloadRecord record : this.recordMap.values()) {
            if (path.equals(record.savedPath)) {
                return true;
            }
        }
        return false;
    }

    public String getVideoName(String videoUrl) {
        return this.nameGenerator.generate(videoUrl) + ".mp4";
    }

    public void createRecord(DownloadRecord record) {
        if (record != null && hasVideoUrl(record)) {
            this.recordMap.put(record.sport.video_url, record);
            save();
        }
    }

    public void updateRecord(DownloadRecord record) {
        if (record != null && hasVideoUrl(record) && this.recordMap.containsKey(record.sport
                .video_url)) {
            this.recordMap.put(record.sport.video_url, record);
            save();
        }
    }

    public void updateProgress(DownloadRecord record) {
        if (record != null && hasVideoUrl(record) && this.recordMap.containsKey(record.sport
                .video_url)) {
            this.recordMap.put(record.sport.video_url, record);
            savePeriod();
        }
    }

    public void deleteRecord(String videoUrl) {
        if (videoUrl != null) {
            this.recordMap.remove(videoUrl);
            save();
        }
    }

    private void save() {
        Editor editor = this.mPreferences.edit();
        editor.putString(CacheKey.DOWNLOAD_RECORD, FastJsonUtils.toJson(this.recordMap));
        editor.apply();
    }

    private void savePeriod() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - this.mLastTime > 3000) {
            save();
            this.mLastTime = currentTime;
        }
    }

    public boolean hasVideoUrl(DownloadRecord record) {
        if (record == null || record.sport == null || TextUtils.isEmpty(record.sport.video_url)) {
            return false;
        }
        return true;
    }

    public void getVideoSize(final String videoUrl, final VideoSizeCallback callback) {
        new Thread(new Runnable() {
            public void run() {
                URL url;
                MalformedURLException e;
                Throwable th;
                IOException e2;
                HttpURLConnection httpConnection = null;
                try {
                    URL url2 = new URL(videoUrl);
                    try {
                        httpConnection = (HttpURLConnection) url2.openConnection();
                        httpConnection.setConnectTimeout(15000);
                        httpConnection.setReadTimeout(15000);
                        httpConnection.setRequestMethod(HTTP.HEAD);
                        httpConnection.setRequestProperty("Range", "bytes=0-");
                        int responseCode = httpConnection.getResponseCode();
                        if (responseCode == 200 || responseCode == 206) {
                            final int size = httpConnection.getContentLength();
                            if (size > 0) {
                                DownloadHelper.this.mHandler.post(new Runnable() {
                                    public void run() {
                                        callback.onResponse(size);
                                    }
                                });
                            }
                        }
                        if (httpConnection != null) {
                            httpConnection.disconnect();
                            url = url2;
                            return;
                        }
                    } catch (MalformedURLException e3) {
                        e = e3;
                        url = url2;
                        try {
                            e.printStackTrace();
                            if (httpConnection != null) {
                                httpConnection.disconnect();
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            if (httpConnection != null) {
                                httpConnection.disconnect();
                            }
                            throw th;
                        }
                    } catch (IOException e4) {
                        e2 = e4;
                        url = url2;
                        e2.printStackTrace();
                        if (httpConnection != null) {
                            httpConnection.disconnect();
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        url = url2;
                        if (httpConnection != null) {
                            httpConnection.disconnect();
                        }
                        throw th;
                    }
                } catch (MalformedURLException e5) {
                    e = e5;
                    e.printStackTrace();
                    if (httpConnection != null) {
                        httpConnection.disconnect();
                    }
                } catch (IOException e6) {
                    e2 = e6;
                    e2.printStackTrace();
                    if (httpConnection != null) {
                        httpConnection.disconnect();
                    }
                }
            }
        }).start();
    }
}
