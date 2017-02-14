package com.boohee.one.player;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.MediaController.MediaPlayerControl;

import com.boohee.one.R;
import com.boohee.one.player.IRenderView.IRenderCallback;
import com.boohee.one.player.IRenderView.ISurfaceHolder;
import com.boohee.utils.Helper;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

public class PureVideoView extends FrameLayout implements MediaPlayerControl {
    public static final  int    MEDIA_ERROR_ILLEGAL_STATE = -1111;
    public static final  int    MEDIA_ERROR_IO            = -1004;
    public static final  int    MEDIA_ERROR_UNSUPPORTED   = -1010;
    public static final  int    RENDER_NONE               = 0;
    public static final  int    RENDER_SURFACE_VIEW       = 1;
    public static final  int    RENDER_TEXTURE_VIEW       = 2;
    private static final int    STATE_ERROR               = -1;
    private static final int    STATE_IDLE                = 0;
    private static final int    STATE_PAUSED              = 4;
    private static final int    STATE_PLAYBACK_COMPLETED  = 5;
    private static final int    STATE_PLAYING             = 3;
    private static final int    STATE_PREPARED            = 2;
    private static final int    STATE_PREPARING           = 1;
    private static final int[]  s_allAspectRatio          = new int[]{0, 1, 2, 4, 5};
    private              String TAG                       = "PureVideoView";
    private Context            mAppContext;
    private BufferInfoListener mBufferInfoListener;
    private OnBufferingUpdateListener mBufferingUpdateListener = new OnBufferingUpdateListener() {
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            PureVideoView.this.mCurrentBufferPercentage = percent;
        }
    };
    private boolean                   mCanPause                = true;
    private boolean                   mCanSeekBack             = true;
    private boolean                   mCanSeekForward          = true;
    private OnCompletionListener      mCompletionListener      = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mp) {
            PureVideoView.this.mCurrentState = 5;
            PureVideoView.this.mTargetState = 5;
            if (PureVideoView.this.mMediaController != null) {
                PureVideoView.this.mMediaController.hide();
            }
            if (PureVideoView.this.mOnCompletionListener != null) {
                PureVideoView.this.mOnCompletionListener.onCompletion(PureVideoView.this
                        .mMediaPlayer);
            }
        }
    };
    private int                       mCurrentAspectRatio      = s_allAspectRatio[0];
    private int                       mCurrentAspectRatioIndex = 0;
    private int mCurrentBufferPercentage;
    private int             mCurrentState  = 0;
    private OnErrorListener mErrorListener = new OnErrorListener() {
        public boolean onError(MediaPlayer mp, int framework_err, int impl_err) {
            Helper.showLog(PureVideoView.this.TAG, "Error: " + framework_err + "," + impl_err);
            PureVideoView.this.mCurrentState = -1;
            PureVideoView.this.mTargetState = -1;
            if (PureVideoView.this.mMediaController != null) {
                PureVideoView.this.mMediaController.hide();
            }
            if (PureVideoView.this.mOnErrorListener == null || !PureVideoView.this
                    .mOnErrorListener.onError(PureVideoView.this.mMediaPlayer, framework_err,
                            impl_err)) {
                int messageId;
                switch (impl_err) {
                    case PureVideoView.MEDIA_ERROR_ILLEGAL_STATE /*-1111*/:
                        messageId = R.string.i;
                        break;
                    case PureVideoView.MEDIA_ERROR_UNSUPPORTED /*-1010*/:
                        messageId = R.string.l;
                        break;
                    case -1004:
                        messageId = R.string.j;
                        break;
                    default:
                        messageId = R.string.k;
                        break;
                }
                Helper.showToast(messageId);
                PureVideoView.this.mMediaPlayer = null;
                PlayerManager.getInstance().releaseAll();
            }
            return true;
        }
    };
    private OnInfoListener  mInfoListener  = new OnInfoListener() {
        public boolean onInfo(MediaPlayer mp, int arg1, int arg2) {
            if (PureVideoView.this.mOnInfoListener != null) {
                PureVideoView.this.mOnInfoListener.onInfo(mp, arg1, arg2);
            }
            switch (arg1) {
                case 3:
                    Helper.showLog(PureVideoView.this.TAG, "MEDIA_INFO_VIDEO_RENDERING_START:");
                    break;
                case 700:
                    Helper.showLog(PureVideoView.this.TAG, "MEDIA_INFO_VIDEO_TRACK_LAGGING:");
                    break;
                case 701:
                    Helper.showLog(PureVideoView.this.TAG, "MEDIA_INFO_BUFFERING_START:");
                    if (PureVideoView.this.mBufferInfoListener != null) {
                        PureVideoView.this.mBufferInfoListener.onBufferingStart();
                        break;
                    }
                    break;
                case 702:
                    if (PureVideoView.this.mBufferInfoListener != null) {
                        PureVideoView.this.mBufferInfoListener.onBufferingEnd();
                    }
                    Helper.showLog(PureVideoView.this.TAG, "MEDIA_INFO_BUFFERING_END:");
                    break;
                case 800:
                    Helper.showLog(PureVideoView.this.TAG, "MEDIA_INFO_BAD_INTERLEAVING:");
                    break;
                case 801:
                    Helper.showLog(PureVideoView.this.TAG, "MEDIA_INFO_NOT_SEEKABLE:");
                    break;
                case 802:
                    Helper.showLog(PureVideoView.this.TAG, "MEDIA_INFO_METADATA_UPDATE:");
                    break;
                case 901:
                    Helper.showLog(PureVideoView.this.TAG, "MEDIA_INFO_UNSUPPORTED_SUBTITLE:");
                    break;
                case 902:
                    Helper.showLog(PureVideoView.this.TAG, "MEDIA_INFO_SUBTITLE_TIMED_OUT:");
                    break;
            }
            return true;
        }
    };
    private IMediaController mMediaController;
    private MediaPlayer mMediaPlayer = null;
    private OnCompletionListener mOnCompletionListener;
    private OnErrorListener      mOnErrorListener;
    private OnInfoListener       mOnInfoListener;
    private OnPreparedListener   mOnPreparedListener;
    OnPreparedListener mPreparedListener = new OnPreparedListener() {
        public void onPrepared(MediaPlayer mp) {
            PureVideoView.this.mCurrentState = 2;
            Helper.showLog(PureVideoView.this.TAG, "onPrepared");
            PureVideoView.this.prepareToTarget(mp);
        }
    };
    private IRenderView mRenderView;
    IRenderCallback mSHCallback = new IRenderCallback() {
        public void onSurfaceChanged(@NonNull ISurfaceHolder holder, int format, int w, int h) {
            if (holder.getRenderView() != PureVideoView.this.mRenderView) {
                Helper.showLog(PureVideoView.this.TAG, "onSurfaceChanged: unmatched render " +
                        "callback\n");
                return;
            }
            Helper.showLog(PureVideoView.this.TAG, "onSurfaceChanged");
            PureVideoView.this.mSurfaceWidth = w;
            PureVideoView.this.mSurfaceHeight = h;
            boolean isValidState;
            if (PureVideoView.this.mTargetState == 3) {
                isValidState = true;
            } else {
                isValidState = false;
            }
            boolean hasValidSize;
            if (!PureVideoView.this.mRenderView.shouldWaitForResize() || (PureVideoView.this
                    .mVideoWidth == w && PureVideoView.this.mVideoHeight == h)) {
                hasValidSize = true;
            } else {
                hasValidSize = false;
            }
            if (PureVideoView.this.mMediaPlayer != null && isValidState && hasValidSize) {
                if (PureVideoView.this.mSeekWhenPrepared != 0) {
                    PureVideoView.this.seekTo(PureVideoView.this.mSeekWhenPrepared);
                }
                PureVideoView.this.start();
            }
        }

        public void onSurfaceCreated(@NonNull ISurfaceHolder holder, int width, int height) {
            if (holder.getRenderView() != PureVideoView.this.mRenderView) {
                Helper.showLog(PureVideoView.this.TAG, "onSurfaceCreated: unmatched render " +
                        "callback\n");
                return;
            }
            Helper.showLog(PureVideoView.this.TAG, "onSurfaceCreated");
            PureVideoView.this.mSurfaceHolder = holder;
            if (PureVideoView.this.getVisibility() != 8) {
                PlayerManager.getInstance().onSurfaceCreated(PureVideoView.this);
                if (PureVideoView.this.mMediaPlayer != null) {
                    PureVideoView.this.bindSurfaceHolder(PureVideoView.this.mMediaPlayer, holder);
                } else {
                    PureVideoView.this.openVideo();
                }
            }
        }

        public void onSurfaceDestroyed(@NonNull ISurfaceHolder holder) {
            if (holder.getRenderView() != PureVideoView.this.mRenderView) {
                Helper.showLog(PureVideoView.this.TAG, "onSurfaceDestroyed: unmatched render " +
                        "callback\n");
                return;
            }
            PureVideoView.this.mSurfaceHolder = null;
            PlayerManager.getInstance().onSurfaceDestroyed(PureVideoView.this);
            Helper.showLog(PureVideoView.this.TAG, "onSurfaceDestroy");
            PureVideoView.this.releaseWithoutStop();
        }
    };
    private int mSeekWhenPrepared;
    OnVideoSizeChangedListener mSizeChangedListener = new OnVideoSizeChangedListener() {
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
            try {
                PureVideoView.this.mVideoWidth = mp.getVideoWidth();
                PureVideoView.this.mVideoHeight = mp.getVideoHeight();
            } catch (IllegalStateException ex) {
                PureVideoView.this.notifyError("onVideoSizeChanged", ex, PureVideoView
                        .MEDIA_ERROR_ILLEGAL_STATE);
                PureVideoView.this.mVideoWidth = 0;
                PureVideoView.this.mVideoHeight = 0;
            }
            PureVideoView.this.mVideoSarNum = 1;
            PureVideoView.this.mVideoSarDen = 1;
            if (PureVideoView.this.mVideoWidth != 0 && PureVideoView.this.mVideoHeight != 0) {
                if (PureVideoView.this.mRenderView != null) {
                    PureVideoView.this.mRenderView.setVideoSize(PureVideoView.this.mVideoWidth,
                            PureVideoView.this.mVideoHeight);
                    PureVideoView.this.mRenderView.setVideoSampleAspectRatio(PureVideoView.this
                            .mVideoSarNum, PureVideoView.this.mVideoSarDen);
                }
                PureVideoView.this.requestLayout();
            }
        }
    };
    private int mSurfaceHeight;
    private ISurfaceHolder mSurfaceHolder = null;
    private int mSurfaceWidth;
    private int mTargetState = 0;
    private Uri mUri;
    private int mVideoHeight;
    private int mVideoRotationDegree;
    private int mVideoSarDen;
    private int mVideoSarNum;
    private int mVideoWidth;

    public interface BufferInfoListener {
        void onBufferingEnd();

        void onBufferingStart();
    }

    public PureVideoView(Context context) {
        super(context);
        initVideoView(context);
    }

    public PureVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initVideoView(context);
    }

    public PureVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initVideoView(context);
    }

    private void initVideoView(Context context) {
        this.mAppContext = context.getApplicationContext();
        initRenders();
        this.mVideoWidth = 0;
        this.mVideoHeight = 0;
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
        this.mCurrentState = 0;
        this.mTargetState = 0;
    }

    private void initRenders() {
        setRender(2);
    }

    public void setRender(int render) {
        switch (render) {
            case 0:
                setRenderView(null);
                return;
            case 1:
                setRenderView(new SurfaceRenderView(getContext()));
                return;
            case 2:
                TextureRenderView renderView = new TextureRenderView(getContext());
                if (this.mMediaPlayer != null) {
                    renderView.getSurfaceHolder().bindToMediaPlayer(this.mMediaPlayer);
                    renderView.setVideoSize(this.mMediaPlayer.getVideoWidth(), this.mMediaPlayer
                            .getVideoHeight());
                    renderView.setVideoSampleAspectRatio(1, 1);
                    renderView.setAspectRatio(this.mCurrentAspectRatio);
                }
                setRenderView(renderView);
                return;
            default:
                Helper.showLog(this.TAG, String.format(Locale.getDefault(), "invalid render " +
                        "%d\n", new Object[]{Integer.valueOf(render)}));
                return;
        }
    }

    public void setRenderView(IRenderView renderView) {
        if (this.mRenderView != null) {
            if (this.mMediaPlayer != null) {
                this.mMediaPlayer.setDisplay(null);
            }
            View renderUIView = this.mRenderView.getView();
            this.mRenderView.removeRenderCallback(this.mSHCallback);
            this.mRenderView = null;
            removeView(renderUIView);
        }
        if (renderView != null) {
            this.mRenderView = renderView;
            renderView.setAspectRatio(this.mCurrentAspectRatio);
            if (this.mVideoWidth > 0 && this.mVideoHeight > 0) {
                renderView.setVideoSize(this.mVideoWidth, this.mVideoHeight);
            }
            if (this.mVideoSarNum > 0 && this.mVideoSarDen > 0) {
                renderView.setVideoSampleAspectRatio(this.mVideoSarNum, this.mVideoSarDen);
            }
            renderUIView = this.mRenderView.getView();
            renderUIView.setLayoutParams(new LayoutParams(-2, -2, 17));
            addView(renderUIView);
            this.mRenderView.addRenderCallback(this.mSHCallback);
            this.mRenderView.setVideoRotation(this.mVideoRotationDegree);
        }
    }

    public void setVideoPath(String path) {
        if (!TextUtils.isEmpty(path)) {
            setVideoURI(Uri.parse(path));
        }
    }

    public void setVideoURI(Uri uri) {
        setVideoURI(uri, null);
    }

    private void setVideoURI(Uri uri, Map<String, String> map) {
        if (uri != null) {
            this.mUri = uri;
            this.mSeekWhenPrepared = 0;
            Helper.showLog(uri.toString());
            openVideo();
            requestLayout();
            invalidate();
        }
    }

    void openVideo() {
        boolean needPrepare = false;
        if (this.mUri != null && this.mSurfaceHolder != null) {
            ((AudioManager) this.mAppContext.getSystemService("audio")).requestAudioFocus(null,
                    3, 1);
            try {
                if (!PlayerManager.getInstance().hasPrevPlayer(this.mUri)) {
                    needPrepare = true;
                }
                this.mMediaPlayer = PlayerManager.getInstance().getMediaPlayer(this.mUri);
                this.mMediaPlayer.setOnPreparedListener(this.mPreparedListener);
                this.mMediaPlayer.setOnVideoSizeChangedListener(this.mSizeChangedListener);
                this.mMediaPlayer.setOnCompletionListener(this.mCompletionListener);
                this.mMediaPlayer.setOnErrorListener(this.mErrorListener);
                this.mMediaPlayer.setOnInfoListener(this.mInfoListener);
                this.mMediaPlayer.setOnBufferingUpdateListener(this.mBufferingUpdateListener);
                this.mCurrentBufferPercentage = 0;
                bindSurfaceHolder(this.mMediaPlayer, this.mSurfaceHolder);
                attachMediaController();
                if (needPrepare) {
                    this.mMediaPlayer.setDataSource(this.mUri.toString());
                    this.mMediaPlayer.setAudioStreamType(3);
                    this.mMediaPlayer.prepareAsync();
                    this.mCurrentState = 1;
                    return;
                }
                this.mCurrentState = 4;
                this.mTargetState = 3;
                prepareToTarget(this.mMediaPlayer);
                post(new Runnable() {
                    public void run() {
                        if (!PlayerManager.getInstance().isPlayingBefore()) {
                            PureVideoView.this.pause();
                        }
                    }
                });
            } catch (IOException ex) {
                notifyError("Unable to open content: " + this.mUri, ex, -1004);
            } catch (IllegalStateException ex2) {
                notifyError("openVideo", ex2, MEDIA_ERROR_ILLEGAL_STATE);
            } catch (IllegalArgumentException ex3) {
                notifyError("openVideo", ex3, MEDIA_ERROR_UNSUPPORTED);
            } catch (SecurityException ex4) {
                notifyError("openVideo", ex4, 1);
            } catch (Exception ex5) {
                notifyError("openVideo", ex5, 1);
            }
        }
    }

    public void setMediaController(IMediaController controller) {
        if (this.mMediaController != null) {
            this.mMediaController.hide();
        }
        this.mMediaController = controller;
        attachMediaController();
    }

    private void attachMediaController() {
        if (this.mMediaPlayer != null && this.mMediaController != null) {
            this.mMediaController.setMediaPlayer(this);
            this.mMediaController.setEnabled(isInPlaybackState());
        }
    }

    private void prepareToTarget(MediaPlayer mp) {
        if (this.mOnPreparedListener != null) {
            this.mOnPreparedListener.onPrepared(this.mMediaPlayer);
        }
        if (this.mMediaController != null) {
            this.mMediaController.setEnabled(true);
        }
        this.mVideoWidth = mp.getVideoWidth();
        this.mVideoHeight = mp.getVideoHeight();
        int seekToPosition = this.mSeekWhenPrepared;
        if (seekToPosition != 0) {
            seekTo(seekToPosition);
        }
        if (this.mVideoWidth == 0 || this.mVideoHeight == 0) {
            if (this.mTargetState == 3) {
                start();
            }
        } else if (this.mRenderView != null) {
            this.mRenderView.setVideoSize(this.mVideoWidth, this.mVideoHeight);
            this.mRenderView.setVideoSampleAspectRatio(this.mVideoSarNum, this.mVideoSarDen);
            if (this.mRenderView.shouldWaitForResize() && (this.mSurfaceWidth != this.mVideoWidth
                    || this.mSurfaceHeight != this.mVideoHeight)) {
                return;
            }
            if (this.mTargetState == 3) {
                start();
                if (this.mMediaController != null) {
                    this.mMediaController.show();
                }
            } else if (!isPlaying()) {
                if ((seekToPosition != 0 || getCurrentPosition() > 0) && this.mMediaController !=
                        null) {
                    this.mMediaController.show(0);
                }
            }
        }
    }

    private void notifyError(String msg, Exception ex, int errorCode) {
        String str = this.TAG;
        if (msg == null) {
            msg = "";
        }
        Helper.showError(str, msg, ex);
        this.mCurrentState = -1;
        this.mTargetState = -1;
        this.mErrorListener.onError(this.mMediaPlayer, 1, errorCode);
    }

    public void setOnPreparedListener(OnPreparedListener l) {
        this.mOnPreparedListener = l;
    }

    public void setOnCompletionListener(OnCompletionListener l) {
        this.mOnCompletionListener = l;
    }

    public void setOnErrorListener(OnErrorListener l) {
        this.mOnErrorListener = l;
    }

    public void setOnInfoListener(OnInfoListener l) {
        this.mOnInfoListener = l;
    }

    private void bindSurfaceHolder(MediaPlayer mp, ISurfaceHolder holder) {
        if (mp != null) {
            if (holder == null) {
                mp.setDisplay(null);
                return;
            }
            holder.bindToMediaPlayer(mp);
            mp.setScreenOnWhilePlaying(true);
        }
    }

    public void releaseWithoutStop() {
        this.mMediaPlayer = null;
    }

    public void release(boolean clearTargetState) {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.reset();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
            this.mCurrentState = 0;
            if (clearTargetState) {
                this.mTargetState = 0;
            }
            ((AudioManager) this.mAppContext.getSystemService("audio")).abandonAudioFocus(null);
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (isInPlaybackState() && this.mMediaController != null) {
            toggleMediaControlsVisibility();
        }
        return false;
    }

    public boolean onTrackballEvent(MotionEvent ev) {
        if (isInPlaybackState() && this.mMediaController != null) {
            toggleMediaControlsVisibility();
        }
        return false;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean isKeyCodeSupported = (keyCode == 4 || keyCode == 24 || keyCode == 25 || keyCode
                == 164 || keyCode == 82 || keyCode == 5 || keyCode == 6) ? false : true;
        if (isInPlaybackState() && isKeyCodeSupported && this.mMediaController != null) {
            if (keyCode == 79 || keyCode == 85) {
                if (isPlaying()) {
                    pause();
                    this.mMediaController.show();
                    return true;
                }
                start();
                this.mMediaController.hide();
                return true;
            } else if (keyCode == 126) {
                if (isPlaying()) {
                    return true;
                }
                start();
                this.mMediaController.hide();
                return true;
            } else if (keyCode != 86 && keyCode != 127) {
                toggleMediaControlsVisibility();
            } else if (!isPlaying()) {
                return true;
            } else {
                pause();
                this.mMediaController.show();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void toggleMediaControlsVisibility() {
        if (this.mMediaController.isShowing()) {
            this.mMediaController.hide();
        } else {
            this.mMediaController.show();
        }
    }

    public void start() {
        if (isInPlaybackState()) {
            try {
                this.mMediaPlayer.start();
            } catch (IllegalStateException ex) {
                notifyError("start", ex, MEDIA_ERROR_ILLEGAL_STATE);
            }
            this.mCurrentState = 3;
        }
        this.mTargetState = 3;
    }

    public void pause() {
        if (isInPlaybackState()) {
            try {
                if (this.mMediaPlayer.isPlaying()) {
                    this.mMediaPlayer.pause();
                    this.mCurrentState = 4;
                }
            } catch (IllegalStateException ex) {
                notifyError("pause", ex, MEDIA_ERROR_ILLEGAL_STATE);
            }
        }
        this.mTargetState = 4;
    }

    public int getDuration() {
        if (isInPlaybackState()) {
            try {
                return this.mMediaPlayer.getDuration();
            } catch (IllegalStateException ex) {
                notifyError("getDuration", ex, MEDIA_ERROR_ILLEGAL_STATE);
            }
        }
        return -1;
    }

    public int getCurrentPosition() {
        if (isInPlaybackState()) {
            try {
                return this.mMediaPlayer.getCurrentPosition();
            } catch (IllegalStateException ex) {
                notifyError("getCurrentPosition", ex, MEDIA_ERROR_ILLEGAL_STATE);
            }
        }
        return 0;
    }

    public void seekTo(int msec) {
        if (isInPlaybackState()) {
            try {
                this.mMediaPlayer.seekTo(msec);
                this.mSeekWhenPrepared = 0;
                return;
            } catch (IllegalStateException ex) {
                notifyError("seekTo", ex, MEDIA_ERROR_ILLEGAL_STATE);
                return;
            }
        }
        this.mSeekWhenPrepared = msec;
    }

    public boolean isPlaying() {
        try {
            return isInPlaybackState() && this.mMediaPlayer.isPlaying();
        } catch (IllegalStateException ex) {
            notifyError("isPlaying", ex, MEDIA_ERROR_ILLEGAL_STATE);
            return false;
        }
    }

    public int getBufferPercentage() {
        if (this.mMediaPlayer != null) {
            return this.mCurrentBufferPercentage;
        }
        return 0;
    }

    private boolean isInPlaybackState() {
        return (this.mMediaPlayer == null || this.mCurrentState == -1 || this.mCurrentState == 0
                || this.mCurrentState == 1) ? false : true;
    }

    public boolean canPause() {
        return this.mCanPause;
    }

    public boolean canSeekBackward() {
        return this.mCanSeekBack;
    }

    public boolean canSeekForward() {
        return this.mCanSeekForward;
    }

    public int getAudioSessionId() {
        return 0;
    }

    public void stopPlayback() {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.stop();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
            this.mCurrentState = 0;
            this.mTargetState = 0;
            ((AudioManager) this.mAppContext.getSystemService("audio")).abandonAudioFocus(null);
        }
    }

    public void setBufferInfoListener(BufferInfoListener bufferInfoListener) {
        this.mBufferInfoListener = bufferInfoListener;
    }
}
