package com.boohee.one.update;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;

import com.aspsine.multithreaddownload.CallBack;
import com.aspsine.multithreaddownload.DownloadConfiguration;
import com.aspsine.multithreaddownload.DownloadException;
import com.aspsine.multithreaddownload.DownloadManager;
import com.aspsine.multithreaddownload.DownloadRequest;
import com.boohee.utils.FileUtil;
import com.boohee.utils.Helper;
import com.boohee.utils.SDcard;
import com.tencent.tinker.loader.shareutil.ShareConstants;

import java.io.File;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class UpdateService extends Service {
    public static final  String ACTION_STOP_UPDATE_SERVICE = "com.boohee.one" +
            ".service:action_stop_update_service";
    public static final  String ACTION_UPDATE              = "com.boohee.one.service:action_update";
    private static final int    ID                         = 4096;
    public static final  int    REQUEST_INSTALL            = 12288;
    public static final  int    REQUEST_RESTART            = 8192;
    public static final  int    REQUEST_STOP               = 16384;
    public static final  String UPDATE_INFO                = "update_info";
    private DownloadManager sDownloadManager;

    private static class UpdateDownloadCallback implements CallBack {
        private Service    context;
        private File       downloadFile;
        private UpdateInfo info;
        private Builder    mBuilder;
        private Handler mHandler = new Handler(Looper.getMainLooper());
        private long                      mLastTime;
        private NotificationManagerCompat mNotificationManager;

        public UpdateDownloadCallback(Service context, File file, UpdateInfo info, Builder
                builder) {
            this.context = context;
            this.downloadFile = file;
            this.info = info;
            this.mNotificationManager = NotificationManagerCompat.from(context);
            this.mBuilder = builder;
        }

        public void onStarted() {
        }

        public void onConnecting() {
        }

        public void onConnected(long total, boolean isRangeSupport) {
        }

        public void onProgress(long finished, long total, int progress) {
            if (this.mLastTime == 0) {
                this.mLastTime = System.currentTimeMillis();
            }
            Helper.simpleLog(SDcard.DOWNLOAD_DIR, "onProgress");
            long currentTime = System.currentTimeMillis();
            if (currentTime - this.mLastTime > 500) {
                this.mBuilder.setSmallIcon(17301633);
                this.mBuilder.setContentText("已下载 " + progress + "%");
                this.mBuilder.setProgress(100, progress, false);
                updateNotification();
                this.mLastTime = currentTime;
            }
        }

        public void onCompleted() {
            Helper.simpleLog(SDcard.DOWNLOAD_DIR, "onCompleted");
            Observable.just(this.downloadFile).observeOn(Schedulers.io()).map(new Func1<File,
                    Boolean>() {
                public Boolean call(File file) {
                    boolean isComplete = FileUtil.checkMD5(UpdateDownloadCallback.this.info
                            .new_md5, file);
                    if (!isComplete && file.exists()) {
                        file.delete();
                    }
                    return Boolean.valueOf(isComplete);
                }
            }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Boolean>() {
                public void call(Boolean isComplete) {
                    if (isComplete.booleanValue()) {
                        UpdateDownloadCallback.this.mBuilder.setSmallIcon(17301634);
                        UpdateDownloadCallback.this.mBuilder.setContentText("下载完成，点击安装");
                        UpdateDownloadCallback.this.mBuilder.setProgress(100, 100, false);
                        UpdateDownloadCallback.this.mBuilder.setTicker("下载完成，点击安装");
                        UpdateDownloadCallback.this.mBuilder.setContentIntent
                                (UpdateDownloadCallback.this.getInstallIntent());
                        UpdateDownloadCallback.this.mBuilder.setAutoCancel(true);
                        UpdateUtil.installApk(UpdateDownloadCallback.this.context,
                                UpdateDownloadCallback.this.downloadFile);
                        UpdateDownloadCallback.this.context.stopSelf();
                        UpdateDownloadCallback.this.updateNotification();
                        return;
                    }
                    UpdateDownloadCallback.this.mBuilder.setSmallIcon(17301634);
                    UpdateDownloadCallback.this.mBuilder.setTicker("下载出错，点击重新下载");
                    UpdateDownloadCallback.this.mBuilder.setContentText("下载出错，点击重新下载");
                    UpdateDownloadCallback.this.mBuilder.setContentIntent(UpdateDownloadCallback
                            .this.getRestartIntent());
                    UpdateDownloadCallback.this.context.stopSelf();
                    UpdateDownloadCallback.this.updateNotification();
                }
            });
        }

        public void onDownloadPaused() {
            Helper.simpleLog(SDcard.DOWNLOAD_DIR, "onPause");
            this.mBuilder.setSmallIcon(17301634);
            this.mBuilder.setTicker("下载暂停，点击继续");
            this.mBuilder.setContentText("下载暂停，点击继续");
            this.mBuilder.setContentIntent(getRestartIntent());
            this.context.stopSelf();
            updateNotification();
        }

        public void onDownloadCanceled() {
            Helper.simpleLog(SDcard.DOWNLOAD_DIR, "onCanceled");
        }

        public void onFailed(DownloadException e) {
            Helper.simpleLog(SDcard.DOWNLOAD_DIR, "onFailed");
            this.mBuilder.setSmallIcon(17301634);
            this.mBuilder.setTicker("下载出错，点击继续");
            this.mBuilder.setContentText("下载出错，点击继续");
            this.mBuilder.setContentIntent(getRestartIntent());
            this.context.stopSelf();
            updateNotification();
        }

        private void updateNotification() {
            this.mNotificationManager.notify(4096, this.mBuilder.build());
        }

        private PendingIntent getRestartIntent() {
            return PendingIntent.getService(this.context.getApplicationContext(), 8192,
                    UpdateService.getDownloadIntent(this.context, this.info), 134217728);
        }

        private PendingIntent getInstallIntent() {
            return PendingIntent.getActivity(this.context.getApplicationContext(), UpdateService
                    .REQUEST_INSTALL, UpdateUtil.getInstallIntent(this.downloadFile), 134217728);
        }
    }

    public static void intentDownload(Context context, UpdateInfo info) {
        context.startService(getDownloadIntent(context, info));
    }

    private static Intent getDownloadIntent(Context context, UpdateInfo info) {
        Intent intent = new Intent(context, UpdateService.class);
        intent.setAction(ACTION_UPDATE);
        intent.putExtra("update_info", info);
        return intent;
    }

    private static Intent getStopServiceIntent(Context context) {
        Intent intent = new Intent(context, UpdateService.class);
        intent.setAction(ACTION_STOP_UPDATE_SERVICE);
        return intent;
    }

    public static void stopService(Context context) {
        context.startService(getStopServiceIntent(context));
    }

    public void onCreate() {
        super.onCreate();
        Helper.simpleLog("Update", "onCreate");
        DownloadConfiguration configuration = new DownloadConfiguration();
        configuration.setMaxThreadNum(5);
        configuration.setThreadNum(3);
        this.sDownloadManager = DownloadManager.getInstance();
        this.sDownloadManager.init(this, configuration);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Helper.simpleLog("Update", "onStartCommand");
        if (intent != null) {
            String action = intent.getAction();
            Helper.simpleLog("Update", "onStartCommand:" + action);
            if (action != null) {
                Object obj = -1;
                switch (action.hashCode()) {
                    case -1089969349:
                        if (action.equals(ACTION_STOP_UPDATE_SERVICE)) {
                            obj = 1;
                            break;
                        }
                        break;
                    case 45252170:
                        if (action.equals(ACTION_UPDATE)) {
                            obj = null;
                            break;
                        }
                        break;
                }
                switch (obj) {
                    case null:
                        download((UpdateInfo) intent.getParcelableExtra("update_info"));
                        break;
                    case 1:
                        stopSelf();
                        break;
                }
            }
        }
        return 2;
    }

    private void download(UpdateInfo info) {
        if (info == null || TextUtils.isEmpty(info.apk_url) || TextUtils.isEmpty(info.new_md5)) {
            stopSelf();
            return;
        }
        File folder = UpdateUtil.getUpdateDir(this);
        String title = info.new_md5 + ShareConstants.PATCH_SUFFIX;
        DownloadRequest request = new DownloadRequest.Builder().setTitle(title).setFolder(folder)
                .setUri(info.apk_url).build();
        File downloadFile = new File(folder, title);
        Builder builder = new Builder(this);
        builder.setSmallIcon(17301633).setContentTitle("薄荷").setContentText("开始下载").setProgress
                (100, 0, true).setTicker("正在下载薄荷");
        startForeground(4096, builder.build());
        this.sDownloadManager.download(request, info.apk_url, new UpdateDownloadCallback(this,
                downloadFile, info, builder));
    }

    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onDestroy() {
        super.onDestroy();
        Helper.simpleLog("Update", "UpdateService:onDestroy");
    }
}
