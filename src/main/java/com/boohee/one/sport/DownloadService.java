package com.boohee.one.sport;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.aspsine.multithreaddownload.CallBack;
import com.aspsine.multithreaddownload.DownloadException;
import com.aspsine.multithreaddownload.DownloadManager;
import com.aspsine.multithreaddownload.DownloadRequest;
import com.aspsine.multithreaddownload.DownloadRequest.Builder;
import com.boohee.one.event.DownloadEvent;
import com.boohee.one.sport.model.DownloadRecord;
import com.boohee.one.sport.model.SportDetail;
import com.boohee.utility.Event;
import com.boohee.utils.FileUtil;
import com.boohee.utils.Helper;
import com.boohee.utils.HttpUtils;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

import java.io.File;

public class DownloadService extends Service {
    public static final  String ACTION_CANCEL              = "com.boohee.one.service:action_cancel";
    public static final  String ACTION_DOWNLOAD            = "com.boohee.one" +
            ".service:action_download";
    public static final  String ACTION_DOWNLOAD_BROAD_CAST = "com.boohee.one" +
            ".service:action_download_broad_cast";
    public static final  String ACTION_PAUSE               = "com.boohee.one.service:action_pause";
    public static final  String ACTION_PAUSE_ALL           = "com.boohee.one" +
            ".service:action_pause_all";
    public static final  String DOWNLOAD_INFO              = "sport_info";
    public static final  String EXTRA_TAG                  = "tag";
    private static final int    RETRY_LIMIT                = 10;
    private static int             retryTime;
    private        DownloadManager mDownloadManager;

    private static class DownloadCallback implements CallBack {
        private static final int INTERVAL = 500;
        private Context        mContext;
        private long           mLastTime;
        private DownloadRecord mRecord;
        private int            preProgress;

        public DownloadCallback(DownloadRecord record, Context context) {
            this.mRecord = record;
            this.mContext = context;
        }

        public void onStarted() {
            this.mRecord.downloadStatus = 7;
            EventBus.getDefault().post(new DownloadEvent(this.mRecord));
            DownloadHelper.getInstance().createRecord(this.mRecord);
        }

        public void onConnecting() {
            this.mRecord.downloadStatus = 1;
            EventBus.getDefault().post(new DownloadEvent(this.mRecord));
            DownloadHelper.getInstance().updateRecord(this.mRecord);
        }

        public void onConnected(long total, boolean isRangeSupport) {
        }

        public void onProgress(long finished, long total, int progress) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - this.mLastTime > 500 && this.preProgress != progress) {
                this.mRecord.progress = progress;
                this.mRecord.downloadStatus = 3;
                EventBus.getDefault().post(new DownloadEvent(this.mRecord));
                DownloadHelper.getInstance().updateProgress(this.mRecord);
                this.mLastTime = currentTime;
                this.preProgress = progress;
            }
        }

        public void onCompleted() {
            this.mRecord.downloadStatus = 6;
            this.mRecord.progress = 100;
            EventBus.getDefault().post(new DownloadEvent(this.mRecord));
            DownloadHelper.getInstance().updateRecord(this.mRecord);
        }

        public void onDownloadPaused() {
            this.mRecord.downloadStatus = 4;
            EventBus.getDefault().post(new DownloadEvent(this.mRecord));
            DownloadHelper.getInstance().updateRecord(this.mRecord);
        }

        public void onDownloadCanceled() {
        }

        public void onFailed(DownloadException e) {
            if (e.getErrorMessage().contains("404")) {
                Helper.showToast((CharSequence) "无法找到该视频");
                this.mRecord.downloadStatus = 0;
            } else {
                this.mRecord.downloadStatus = 5;
                if (e.getErrorMessage().contains("inputStream") && HttpUtils.isWifiConnection
                        (this.mContext) && DownloadService.retryTime < 10) {
                    DownloadService.access$008();
                    DownloadService.resumeDownload(this.mContext, this.mRecord);
                }
            }
            EventBus.getDefault().post(new DownloadEvent(this.mRecord));
            DownloadHelper.getInstance().updateRecord(this.mRecord);
        }
    }

    static /* synthetic */ int access$008() {
        int i = retryTime;
        retryTime = i + 1;
        return i;
    }

    public void onCreate() {
        super.onCreate();
        this.mDownloadManager = DownloadManager.getInstance();
        DownloadHelper.getInstance();
    }

    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void intentDownload(Context context, DownloadRecord record) {
        retryTime = 0;
        resumeDownload(context, record);
        MobclickAgent.onEvent(context, Event.bingo_clickCourseDownload);
    }

    private static void resumeDownload(Context context, DownloadRecord record) {
        if (record != null && record.sport != null) {
            Intent intent = new Intent(context, DownloadService.class);
            intent.setAction(ACTION_DOWNLOAD);
            intent.putExtra(EXTRA_TAG, record.sport.video_url);
            intent.putExtra(DOWNLOAD_INFO, record);
            context.startService(intent);
        }
    }

    public static void intentPause(Context context, String tag) {
        if (!TextUtils.isEmpty(tag)) {
            Intent intent = new Intent(context, DownloadService.class);
            intent.setAction(ACTION_PAUSE);
            intent.putExtra(EXTRA_TAG, tag);
            context.startService(intent);
        }
    }

    public static void intentPauseAll(Context context) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.setAction(ACTION_PAUSE_ALL);
        context.startService(intent);
    }

    public static void intentCancel(Context context, String tag) {
        if (!TextUtils.isEmpty(tag)) {
            Intent intent = new Intent(context, DownloadService.class);
            intent.setAction(ACTION_CANCEL);
            intent.putExtra(EXTRA_TAG, tag);
            context.startService(intent);
        }
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            DownloadRecord record = (DownloadRecord) intent.getParcelableExtra(DOWNLOAD_INFO);
            String tag = intent.getStringExtra(EXTRA_TAG);
            if (!TextUtils.isEmpty(action)) {
                Object obj = -1;
                switch (action.hashCode()) {
                    case -2028264777:
                        if (action.equals(ACTION_PAUSE_ALL)) {
                            obj = 2;
                            break;
                        }
                        break;
                    case -1250513835:
                        if (action.equals(ACTION_PAUSE)) {
                            obj = 1;
                            break;
                        }
                        break;
                    case -483625989:
                        if (action.equals(ACTION_CANCEL)) {
                            obj = 3;
                            break;
                        }
                        break;
                    case 642888137:
                        if (action.equals(ACTION_DOWNLOAD)) {
                            obj = null;
                            break;
                        }
                        break;
                }
                switch (obj) {
                    case null:
                        download(record, tag);
                        break;
                    case 1:
                        pause(tag);
                        break;
                    case 2:
                        pauseAll();
                        break;
                    case 3:
                        cancel(tag);
                        break;
                }
            }
        }
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    private void download(DownloadRecord record, String tag) {
        if (!TextUtils.isEmpty(tag)) {
            String title;
            File folder;
            SportDetail sport = record.sport;
            if (TextUtils.isEmpty(record.savedPath)) {
                title = DownloadHelper.getInstance().getVideoName(sport.video_url);
                folder = FileUtil.getVideoCacheDir(this);
                record.savedPath = new File(folder, title).getAbsolutePath();
            } else {
                File savedFile = new File(record.savedPath);
                folder = savedFile.getParentFile();
                title = savedFile.getName();
            }
            DownloadRequest request = new Builder().setFolder(folder).setTitle(title).setUri
                    (sport.video_url).build();
            record.sport = sport;
            DownloadManager.getInstance().download(request, tag, new DownloadCallback(record,
                    this));
        }
    }

    private void pause(String tag) {
        if (!TextUtils.isEmpty(tag) && !this.mDownloadManager.pause(tag)) {
            DownloadRecord record = DownloadHelper.getInstance().getRecord(tag);
            if (record != null && record.inConnectAndDownload()) {
                record.downloadStatus = 4;
                DownloadHelper.getInstance().updateRecord(record);
            }
        }
    }

    private void pauseAll() {
        this.mDownloadManager.pauseAll();
    }

    private void cancel(String tag) {
        if (!TextUtils.isEmpty(tag)) {
            if (!this.mDownloadManager.cancel(tag)) {
                DownloadManager.getInstance().clearDb(tag);
                DownloadRecord record = DownloadHelper.getInstance().getRecord(tag);
                if (!(record == null || TextUtils.isEmpty(record.savedPath))) {
                    File file = new File(record.savedPath);
                    if (file.exists()) {
                        file.delete();
                    }
                }
            }
            DownloadHelper.getInstance().deleteRecord(tag);
        }
    }
}
