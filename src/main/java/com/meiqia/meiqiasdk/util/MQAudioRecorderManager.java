package com.meiqia.meiqiasdk.util;

import android.content.Context;
import android.media.MediaRecorder;
import android.support.annotation.Nullable;

import java.io.File;
import java.util.UUID;

public class MQAudioRecorderManager {
    private static MQAudioRecorderManager sInstance;
    private        Callback               mCallback;
    private        Context                mContext;
    private        File                   mCurrentFile;
    private        boolean                mIsPrepared;
    private        MediaRecorder          mMediaRecorder;

    public interface Callback {
        void onAudioRecorderNoPermission();

        void wellPrepared();
    }

    private MQAudioRecorderManager(Context context) {
        this.mContext = context;
    }

    public static MQAudioRecorderManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (MQAudioRecorderManager.class) {
                if (sInstance == null) {
                    sInstance = new MQAudioRecorderManager(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    public void prepareAudio() {
        try {
            this.mCurrentFile = new File(getVoiceCacheDir(this.mContext), UUID.randomUUID()
                    .toString());
            this.mMediaRecorder = new MediaRecorder();
            this.mMediaRecorder.setOutputFile(this.mCurrentFile.getAbsolutePath());
            this.mMediaRecorder.setAudioSource(1);
            this.mMediaRecorder.setOutputFormat(3);
            this.mMediaRecorder.setAudioEncoder(1);
            this.mMediaRecorder.prepare();
            this.mMediaRecorder.start();
            this.mIsPrepared = true;
            if (this.mCallback != null) {
                this.mCallback.wellPrepared();
            }
        } catch (Exception e) {
            if (this.mCallback != null) {
                this.mCallback.onAudioRecorderNoPermission();
            }
        }
    }

    public int getVoiceLevel(int maxLevel) {
        int i = 1;
        if (this.mIsPrepared) {
            try {
                i = Math.max(Math.min(((int) (25.0d * Math.log10((double) (this.mMediaRecorder
                        .getMaxAmplitude() / 500)))) / 4, maxLevel), 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return i;
    }

    public void release() {
        try {
            if (this.mMediaRecorder != null) {
                this.mMediaRecorder.stop();
                this.mMediaRecorder.release();
                this.mMediaRecorder = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancel() {
        release();
        if (this.mCurrentFile != null) {
            this.mCurrentFile.delete();
            this.mCurrentFile = null;
        }
    }

    @Nullable
    public String getCurrenFilePath() {
        return this.mCurrentFile == null ? null : this.mCurrentFile.getAbsolutePath();
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    public static File getVoiceCacheDir(Context context) {
        File voiceCacheDir = new File(context.getExternalCacheDir(), "voice");
        if (!voiceCacheDir.exists()) {
            voiceCacheDir.mkdirs();
        }
        return voiceCacheDir;
    }

    public static File getCachedVoiceFileByUrl(Context context, String url) {
        return new File(getVoiceCacheDir(context), url.substring(url.lastIndexOf("/") + 1));
    }

    public static String renameVoiceFilename(Context context, String path, String key) {
        File oldFile = new File(path);
        File newFile = new File(getVoiceCacheDir(context), key.replace("audio/", ""));
        oldFile.renameTo(newFile);
        return newFile.getAbsolutePath();
    }
}
