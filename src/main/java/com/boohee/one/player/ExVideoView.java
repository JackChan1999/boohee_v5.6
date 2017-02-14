package com.boohee.one.player;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.boohee.one.R;
import com.boohee.one.player.PureVideoView.BufferInfoListener;
import com.boohee.one.sport.SportDetailActivity;
import com.boohee.utility.Event;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.Helper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

public class ExVideoView extends FrameLayout {
    public static final int    STATE_COVER   = 0;
    public static final int    STATE_LOADING = 1;
    public static final int    STATE_PLAY    = 2;
    public static final String TAG           = "ExVideoView";
    private String              cover;
    private String              downloadUrl;
    private int                 id;
    private boolean             isFullScreen;
    private ImageView           ivCover;
    private ImageView           ivPlay;
    private Context             mContext;
    private MediaControllerView mControllerView;
    private ProgressBar         mLoadingView;
    private View                mMask;
    private PureVideoView       mVideoView;
    private String              videoUrl;

    public ExVideoView(Context context) {
        this(context, null, 0);
    }

    public ExVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    private void initViews(Context context) {
        this.mContext = context;
        inflate(context, R.layout.r6, this);
        this.mVideoView = (PureVideoView) findViewById(R.id.video_view);
        this.mControllerView = (MediaControllerView) findViewById(R.id.controller);
        this.ivCover = (ImageView) findViewById(R.id.cover);
        this.ivPlay = (ImageView) findViewById(R.id.to_play);
        this.mLoadingView = (ProgressBar) findViewById(R.id.loading);
        this.mMask = findViewById(R.id.mask);
        this.mVideoView.setMediaController(this.mControllerView);
        this.mControllerView.setExpandState(this.isFullScreen);
        this.mControllerView.setExpandListener(new OnClickListener() {
            public void onClick(View v) {
                if (!ExVideoView.this.isFullScreen) {
                    PlayerManager.getInstance().savePlayingState(ExVideoView.this.mVideoView
                            .isPlaying());
                    ExVideoView.this.mVideoView.pause();
                    ContinueVideoActivity.startActivity(ExVideoView.this.mContext, ExVideoView
                            .this.videoUrl);
                } else if (ExVideoView.this.mContext instanceof Activity) {
                    ExVideoView.this.quitFullScreen();
                }
            }
        });
        this.ivPlay.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (ExVideoView.this.mContext instanceof SportDetailActivity) {
                    MobclickAgent.onEvent(ExVideoView.this.mContext, Event.bingo_clickCourseVideo);
                }
                PlayerManager.getInstance().bindView(ExVideoView.this, ExVideoView.this.id);
                ExVideoView.this.setStatus(1);
                if (!TextUtils.isEmpty(ExVideoView.this.downloadUrl)) {
                    ExVideoView.this.videoUrl = ExVideoView.this.downloadUrl;
                }
                ExVideoView.this.mVideoView.setVideoPath(ExVideoView.this.videoUrl);
                ExVideoView.this.mVideoView.start();
            }
        });
        this.mVideoView.setOnPreparedListener(new OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                ExVideoView.this.setStatus(2);
            }
        });
        this.mVideoView.setBufferInfoListener(new BufferInfoListener() {
            private boolean hasShowToast;
            private long mBufferStartTime;

            public void onBufferingStart() {
                ExVideoView.this.mLoadingView.setVisibility(0);
                this.mBufferStartTime = System.currentTimeMillis();
            }

            public void onBufferingEnd() {
                ExVideoView.this.mLoadingView.setVisibility(8);
                if (System.currentTimeMillis() - this.mBufferStartTime > 5000 && !this
                        .hasShowToast) {
                    Helper.showToast((CharSequence) "当前网速较慢，建议先暂停一会或下载观看");
                    this.hasShowToast = true;
                }
            }
        });
        setStatus(0);
    }

    public void quitFullScreen() {
        PlayerManager.getInstance().prepareQuitFullScreen(this.mVideoView.isPlaying());
        this.mVideoView.pause();
        ((Activity) this.mContext).finish();
    }

    public void setStatus(int status) {
        switch (status) {
            case 0:
                this.ivCover.setVisibility(0);
                this.mMask.setVisibility(0);
                this.mMask.setBackgroundColor(getResources().getColor(R.color.io));
                this.ivPlay.setVisibility(0);
                this.mLoadingView.setVisibility(8);
                this.mVideoView.setVisibility(8);
                this.mControllerView.hide();
                return;
            case 1:
                this.ivCover.setVisibility(8);
                this.mMask.setVisibility(0);
                this.mMask.setBackgroundColor(getResources().getColor(R.color.ax));
                this.ivPlay.setVisibility(8);
                this.mLoadingView.setVisibility(0);
                this.mVideoView.setVisibility(0);
                this.mControllerView.hide();
                return;
            case 2:
                this.ivCover.setVisibility(8);
                this.mMask.setVisibility(8);
                this.ivPlay.setVisibility(8);
                this.mLoadingView.setVisibility(8);
                this.mVideoView.setVisibility(0);
                this.mControllerView.show();
                return;
            default:
                return;
        }
    }

    public void setExpandState(boolean fullScreen) {
        this.isFullScreen = fullScreen;
        this.mControllerView.setExpandState(this.isFullScreen);
    }

    public void updateVideoUrl(String videoUrl) {
        this.downloadUrl = videoUrl;
    }

    public void setData(String cover, String videoUrl, int id) {
        this.cover = cover;
        this.videoUrl = videoUrl;
        this.id = id;
        ImageLoader.getInstance().displayImage(cover, this.ivCover, ImageLoaderOptions.color(R
                .color.in));
        setStatus(0);
        PlayerManager.getInstance().updateBindView(this, id);
    }

    public void setContinueVideo(String videoUrl) {
        this.videoUrl = videoUrl;
        this.mVideoView.setVideoPath(videoUrl);
    }

    public void setVideo(String videoUrl) {
        this.videoUrl = videoUrl;
        setStatus(1);
        this.mVideoView.setVideoPath(videoUrl);
        this.mVideoView.start();
        this.mControllerView.hideExpandButton();
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (!this.isFullScreen) {
            PlayerManager.getInstance().clearBindMediaPlayer(this);
        }
    }

    public PureVideoView getVideoView() {
        return this.mVideoView;
    }

    public MediaControllerView getControllerView() {
        return this.mControllerView;
    }

    public String getVideoUrl() {
        return this.videoUrl;
    }
}
