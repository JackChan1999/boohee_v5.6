package com.boohee.one.video.manager;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;

import com.boohee.one.video.download.VideoDownloadHelper;

import java.io.File;
import java.io.IOException;

public class DesPlayerManager {
    private static MediaPlayer    desMediaPlayer;
    private        OnPrepareError onPrepareError;

    private static class DesPlayerManagerHodler {
        private static final DesPlayerManager INSTANCE = new DesPlayerManager();

        private DesPlayerManagerHodler() {
        }
    }

    public interface OnPrepareError {
        void onPrepareError();
    }

    private DesPlayerManager() {
    }

    public static DesPlayerManager getInstance() {
        DesPlayerManager playerManager = DesPlayerManagerHodler.INSTANCE;
        if (desMediaPlayer == null) {
            desMediaPlayer = new MediaPlayer();
        }
        desMediaPlayer.setOnErrorListener(new OnErrorListener() {
            public boolean onError(MediaPlayer mp, int what, int extra) {
                DesPlayerManager.desMediaPlayer.reset();
                return true;
            }
        });
        return playerManager;
    }

    public void setOnPrepareError(OnPrepareError onPrepareError) {
        this.onPrepareError = onPrepareError;
    }

    public void playDesMusic(Context context, String name) {
        playDesMusic(context, name, null);
    }

    public void playDesMusic(Context context, String name, OnCompletionListener
            onCompletionListener) {
        try {
            desMediaPlayer.reset();
            if (name.contains(VideoDownloadHelper.AUDIO_NAME)) {
                String path = VideoDownloadHelper.getInstance().getAudioDownloadPath(context) +
                        name;
                File file = new File(path);
                if ((file == null || !file.exists() || file.length() == 0) && this.onPrepareError
                        != null) {
                    this.onPrepareError.onPrepareError();
                    return;
                }
                desMediaPlayer.setDataSource(path);
            } else {
                AssetFileDescriptor descriptor = context.getAssets().openFd("sport_audio/" + name
                        + VideoDownloadHelper.AUDIO_FORMAT);
                desMediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor
                        .getStartOffset(), descriptor.getLength());
                descriptor.close();
            }
            desMediaPlayer.prepare();
            desMediaPlayer.start();
            desMediaPlayer.setOnCompletionListener(onCompletionListener);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public void pausePlayback() {
        try {
            if (desMediaPlayer.isPlaying()) {
                desMediaPlayer.pause();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void resumePlayback() {
        try {
            if (!desMediaPlayer.isPlaying() && desMediaPlayer.getCurrentPosition() > 0) {
                desMediaPlayer.start();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void stopPlayback() {
        try {
            desMediaPlayer.stop();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void release() {
        try {
            desMediaPlayer.release();
            desMediaPlayer = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
