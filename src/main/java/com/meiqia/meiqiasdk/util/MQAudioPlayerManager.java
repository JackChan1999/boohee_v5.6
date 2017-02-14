package com.meiqia.meiqiasdk.util;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;

import java.io.IOException;

public class MQAudioPlayerManager {
    private static boolean     sIsPause;
    private static MediaPlayer sMediaPlayer;

    public interface Callback {
        void onCompletion();

        void onError();
    }

    public static void playSound(String path, final Callback callback) {
        try {
            if (sMediaPlayer == null) {
                sMediaPlayer = new MediaPlayer();
            } else {
                sMediaPlayer.reset();
            }
            sMediaPlayer.setAudioStreamType(3);
            sMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    if (callback != null) {
                        callback.onCompletion();
                    }
                }
            });
            sMediaPlayer.setOnErrorListener(new OnErrorListener() {
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    MQAudioPlayerManager.sMediaPlayer.reset();
                    if (callback != null) {
                        callback.onError();
                    }
                    return false;
                }
            });
            sMediaPlayer.setDataSource(path);
            sMediaPlayer.prepare();
            sMediaPlayer.start();
        } catch (IOException e) {
            if (callback != null) {
                callback.onError();
            }
        }
    }

    public static void resume() {
        if (sMediaPlayer != null && sIsPause) {
            sMediaPlayer.start();
            sIsPause = false;
        }
    }

    public static void pause() {
        if (isPlaying()) {
            sMediaPlayer.pause();
            sIsPause = true;
        }
    }

    public static void stop() {
        if (isPlaying()) {
            sMediaPlayer.stop();
        }
    }

    public static boolean isPlaying() {
        return sMediaPlayer != null && sMediaPlayer.isPlaying();
    }

    public static void release() {
        stop();
        if (sMediaPlayer != null) {
            sMediaPlayer.release();
            sMediaPlayer = null;
        }
    }

    public static int getCurrentPosition() {
        if (!isPlaying()) {
            return 0;
        }
        int position = sMediaPlayer.getCurrentPosition();
        if (position == 0) {
            return 1;
        }
        return position;
    }

    public static int getCurrentDuration() {
        if (!isPlaying()) {
            return 0;
        }
        int duration = sMediaPlayer.getDuration();
        if (duration == 0) {
            return 1;
        }
        return duration;
    }

    public static int getDurationByFilePath(Context context, String audioFilePath) {
        try {
            int duration = MediaPlayer.create(context, Uri.parse(audioFilePath)).getDuration() /
                    1000;
            if (duration == 0) {
                return 1;
            }
            return duration;
        } catch (Exception e) {
            return 0;
        }
    }
}
