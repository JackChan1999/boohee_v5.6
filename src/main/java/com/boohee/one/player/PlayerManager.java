package com.boohee.one.player;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import com.boohee.one.MyApplication;
import com.boohee.utils.ResolutionUtils;

import java.lang.ref.WeakReference;

public class PlayerManager {
    public static final String TAG = "PlayerManager";
    private static PlayerManager mPlayerManager;
    private        boolean       fullScreenManualQuit;
    private int id = Integer.MIN_VALUE;
    private boolean                    isHolderDestroyed;
    private boolean                    isPlaying;
    private WeakReference<ExVideoView> mBindView;
    private VideoHandler mHandler = new VideoHandler();
    private MediaPlayer mMediaPlayer;
    private Uri         mUri;
    private boolean     startFullScreen;

    private static class VideoHandler extends Handler {
        private static final int CHECK_VISIBILITY = 0;
        private static final int INTERVAL         = 500;
        private boolean                    isCheck;
        private WeakReference<ExVideoView> mVideoWeakReference;

        private VideoHandler() {
        }

        private void setViewTarget(ExVideoView videoView) {
            if (videoView == null) {
                removeMessages(0);
            } else {
                this.mVideoWeakReference = new WeakReference(videoView);
            }
        }

        private void pauseCheck() {
            this.isCheck = false;
            removeMessages(0);
        }

        private void resumeCheck() {
            this.isCheck = true;
            sendEmptyMessageDelayed(0, 500);
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (this.mVideoWeakReference != null) {
                        ExVideoView view = (ExVideoView) this.mVideoWeakReference.get();
                        if (view == null) {
                            return;
                        }
                        if (needPlay(view)) {
                            this.isCheck = true;
                            sendEmptyMessageDelayed(0, 500);
                            return;
                        }
                        this.isCheck = false;
                        PlayerManager.getInstance().clearBindMediaPlayer(view);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }

        private boolean needPlay(ExVideoView view) {
            boolean needPlay;
            if (view.getVisibility() != 0 || view.getVideoView().getVisibility() == 8) {
                needPlay = false;
            } else {
                needPlay = true;
            }
            if (!needPlay) {
                return needPlay;
            }
            int[] location = new int[2];
            view.getLocationInWindow(location);
            if (location[0] > view.getWidth() / 2 || location[0] < (-view.getWidth()) / 2 ||
                    location[1] < (-view.getHeight()) / 2 || location[1] > ResolutionUtils
                    .getScreenHeight(view.getContext()) - (view.getHeight() / 2)) {
                return false;
            }
            return needPlay;
        }
    }

    public static PlayerManager getInstance() {
        if (mPlayerManager == null) {
            synchronized (PlayerManager.class) {
                if (mPlayerManager == null) {
                    mPlayerManager = new PlayerManager();
                }
            }
        }
        return mPlayerManager;
    }

    private PlayerManager() {
    }

    public boolean hasPrevPlayer(Uri uri) {
        if (this.mUri == null || !this.mUri.equals(uri) || this.mMediaPlayer == null) {
            return false;
        }
        return true;
    }

    @NonNull
    public MediaPlayer getMediaPlayer(Uri uri) {
        if (hasPrevPlayer(uri)) {
            return this.mMediaPlayer;
        }
        releaseMediaPlayer();
        this.mMediaPlayer = createPlayer();
        this.mUri = uri;
        return this.mMediaPlayer;
    }

    private MediaPlayer createPlayer() {
        return new MediaPlayer();
    }

    public boolean isPlayingBefore() {
        return this.isPlaying;
    }

    public boolean isStartFullScreen() {
        return this.startFullScreen;
    }

    public void resetStartFullScreenState() {
        this.startFullScreen = false;
    }

    public boolean isFullScreenManualQuit() {
        return this.fullScreenManualQuit;
    }

    public void savePlayingState(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public void prepareFullScreen() {
        this.startFullScreen = true;
        pauseCheck();
    }

    public void resetManualQuitState() {
        this.fullScreenManualQuit = false;
    }

    public void prepareQuitFullScreen(boolean isPlaying) {
        this.isPlaying = isPlaying;
        this.fullScreenManualQuit = true;
    }

    public void openPrevVideo() {
        if (this.fullScreenManualQuit && !this.isHolderDestroyed && this.mBindView != null &&
                this.mBindView.get() != null) {
            ((ExVideoView) this.mBindView.get()).getVideoView().openVideo();
        }
    }

    public void onSurfaceCreated(@NonNull PureVideoView videoView) {
        if (this.mBindView != null && this.mBindView.get() != null && videoView == ((ExVideoView)
                this.mBindView.get()).getVideoView()) {
            this.isHolderDestroyed = false;
        }
    }

    public void onSurfaceDestroyed(@NonNull PureVideoView videoView) {
        if (this.mBindView != null && this.mBindView.get() != null && videoView == ((ExVideoView)
                this.mBindView.get()).getVideoView()) {
            this.isHolderDestroyed = true;
        }
    }

    public void bindView(ExVideoView view, int id) {
        clearBindViewAndPlayer();
        if (view != null) {
            this.mBindView = new WeakReference(view);
            this.mHandler.setViewTarget(view);
            this.mHandler.resumeCheck();
        }
        this.id = id;
    }

    public void updateBindView(ExVideoView view, int id) {
        if (this.id == id && !this.mHandler.isCheck) {
            view.setStatus(2);
            if (view != null && this.mBindView != null && this.mBindView.get() != view) {
                this.mBindView = new WeakReference(view);
                this.mHandler.setViewTarget(view);
            }
        }
    }

    public void clearBindMediaPlayer(ExVideoView view) {
        if (this.mBindView != null && this.mBindView.get() == view) {
            clearBindViewAndPlayer();
        }
    }

    private void clearBindViewAndPlayer() {
        if (!(this.mBindView == null || this.mBindView.get() == null)) {
            ((ExVideoView) this.mBindView.get()).setStatus(0);
            this.mBindView = null;
            this.mHandler.setViewTarget(null);
        }
        this.id = Integer.MIN_VALUE;
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer() {
        final MediaPlayer mediaPlayer = this.mMediaPlayer;
        this.mMediaPlayer = null;
        if (mediaPlayer != null) {
            new Thread(new Runnable() {
                public void run() {
                    mediaPlayer.reset();
                    mediaPlayer.release();
                }
            }).start();
        }
    }

    public void releaseAll() {
        clearBindViewAndPlayer();
        ((AudioManager) MyApplication.getContext().getSystemService("audio")).abandonAudioFocus
                (null);
    }

    public void pauseCheck() {
        this.mHandler.pauseCheck();
    }

    public void resumeCheck() {
        this.mHandler.resumeCheck();
    }
}
