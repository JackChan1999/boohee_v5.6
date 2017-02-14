package com.boohee.one.video.manager;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;

import com.boohee.one.video.download.VideoDownloadHelper;

import java.io.IOException;

public class BGMPlayerManager {
    private static MediaPlayer bgmPlayer;
    private static boolean initialed = false;
    private static String path;

    private static class BGMPlayerManagerHodler {
        private static final BGMPlayerManager INSTANCE = new BGMPlayerManager();

        private BGMPlayerManagerHodler() {
        }
    }

    private BGMPlayerManager() {
    }

    public static BGMPlayerManager getInstance() {
        init();
        return BGMPlayerManagerHodler.INSTANCE;
    }

    private static void init() {
        bgmPlayer = new MediaPlayer();
        bgmPlayer.setOnPreparedListener(new OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        bgmPlayer.setOnErrorListener(new OnErrorListener() {
            public boolean onError(MediaPlayer mp, int what, int extra) {
                BGMPlayerManager.bgmPlayer.reset();
                return false;
            }
        });
    }

    public void startBgm(Context context, String name) {
        try {
            bgmPlayer.reset();
            path = "sport_audio/" + name + VideoDownloadHelper.AUDIO_FORMAT;
            AssetFileDescriptor descriptor = context.getAssets().openFd(path);
            bgmPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(),
                    descriptor.getLength());
            descriptor.close();
            bgmPlayer.prepare();
            bgmPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e2) {
            e2.printStackTrace();
        }
    }

    public void pauseBgm() {
        try {
            if (bgmPlayer.isPlaying()) {
                bgmPlayer.pause();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void resumeBgm() {
        try {
            if (!bgmPlayer.isPlaying() && bgmPlayer.getCurrentPosition() > 0) {
                bgmPlayer.start();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void stopBgm() {
        try {
            bgmPlayer.stop();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void release() {
        try {
            bgmPlayer.release();
            bgmPlayer = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
