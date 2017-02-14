package com.boohee.one.video.manager;

import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.text.TextUtils;
import android.widget.VideoView;

public class VideoPlayerManager {
    public static void initPlayer(VideoView videoView, OnPreparedListener onPreparedListener,
                                  OnCompletionListener onCompletionListener, OnErrorListener
                                          onErrorListener) {
        if (onPreparedListener != null) {
            videoView.setOnPreparedListener(onPreparedListener);
        }
        if (onCompletionListener != null) {
            videoView.setOnCompletionListener(onCompletionListener);
        }
        if (onErrorListener != null) {
            videoView.setOnErrorListener(onErrorListener);
        }
    }

    public static void playVideo(VideoView videoView, String videoSource) {
        if (!TextUtils.isEmpty(videoSource)) {
            videoView.setVideoPath(videoSource);
            videoView.start();
        }
    }

    public static void pauseVideo(VideoView videoView) {
        videoView.pause();
    }

    public static void stopVideo(VideoView videoView) {
        videoView.stopPlayback();
    }
}
