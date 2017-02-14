package com.boohee.one.player;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.boohee.one.R;
import com.boohee.one.cache.FileCache;
import com.boohee.utils.DateFormatUtils;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;

public class MediaControllerView extends FrameLayout implements IMediaController {
    private static final int FADE_OUT        = 1;
    private static final int SHOW_PROGRESS   = 2;
    private static final int sDefaultTimeout = 3000;
    private List<View> mBindViews;
    private Context    mContext;
    private boolean    mDragging;
    private ImageView  mExpandButton;
    StringBuilder mFormatBuilder;
    Formatter     mFormatter;
    private final Handler                 mHandler;
    private final OnSeekBarChangeListener mOnSeekBarChangeListener;
    private       ImageView               mPauseButton;
    private final OnClickListener         mPauseListener;
    private       MediaPlayerControl      mPlayer;
    private       SeekBar                 mProgress;
    private       View                    mRoot;
    private       boolean                 mShowing;
    private       TextView                mTime;

    public MediaControllerView(Context context) {
        this(context, null, 0);
    }

    public MediaControllerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MediaControllerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mBindViews = new ArrayList();
        this.mPauseListener = new OnClickListener() {
            public void onClick(View v) {
                MediaControllerView.this.doPauseResume();
                MediaControllerView.this.show(3000);
            }
        };
        this.mOnSeekBarChangeListener = new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    MediaControllerView.this.mPlayer.seekTo((MediaControllerView.this.mPlayer
                            .getDuration() * progress) / 1000);
                    MediaControllerView.this.mTime.setText(String.format("%s / %s", new
                            Object[]{MediaControllerView.this.stringForTime(newPosition),
                            MediaControllerView.this.stringForTime(duration)}));
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                MediaControllerView.this.show(DateFormatUtils.HOUR);
                MediaControllerView.this.mDragging = true;
                MediaControllerView.this.mHandler.removeMessages(2);
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                MediaControllerView.this.mDragging = false;
                MediaControllerView.this.setProgress();
                MediaControllerView.this.updatePausePlay();
                MediaControllerView.this.show(3000);
                MediaControllerView.this.mHandler.sendEmptyMessage(2);
            }
        };
        this.mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        MediaControllerView.this.hide();
                        return;
                    case 2:
                        MediaControllerView.this.updatePausePlay();
                        int pos = MediaControllerView.this.setProgress();
                        if (!MediaControllerView.this.mDragging && MediaControllerView.this
                                .mShowing && MediaControllerView.this.mPlayer.isPlaying()) {
                            sendMessageDelayed(obtainMessage(2), (long) (1000 - (pos % 1000)));
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        };
        this.mRoot = this;
        this.mContext = context;
        initViews(context);
    }

    private void initViews(Context context) {
        inflate(context, R.layout.pr, this);
        updateVisibility();
        this.mPauseButton = (ImageView) findViewById(R.id.play);
        this.mExpandButton = (ImageView) findViewById(R.id.expand);
        this.mProgress = (SeekBar) findViewById(R.id.progress);
        this.mTime = (TextView) findViewById(R.id.time);
        this.mProgress.setMax(1000);
        this.mProgress.setOnSeekBarChangeListener(this.mOnSeekBarChangeListener);
        this.mFormatBuilder = new StringBuilder();
        this.mFormatter = new Formatter(this.mFormatBuilder, Locale.getDefault());
        this.mPauseButton.requestFocus();
        this.mPauseButton.setOnClickListener(this.mPauseListener);
    }

    private void doPauseResume() {
        if (this.mPlayer.isPlaying()) {
            this.mPlayer.pause();
        } else {
            this.mPlayer.start();
        }
        updatePausePlay();
    }

    public boolean onTrackballEvent(MotionEvent ev) {
        show(3000);
        return false;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (this.mPlayer == null) {
            return super.dispatchKeyEvent(event);
        }
        int keyCode = event.getKeyCode();
        boolean uniqueDown = event.getRepeatCount() == 0 && event.getAction() == 0;
        if (keyCode == 79 || keyCode == 85 || keyCode == 62) {
            if (!uniqueDown) {
                return true;
            }
            doPauseResume();
            show(3000);
            if (this.mPauseButton == null) {
                return true;
            }
            this.mPauseButton.requestFocus();
            return true;
        } else if (keyCode == 126) {
            if (!uniqueDown || this.mPlayer.isPlaying()) {
                return true;
            }
            this.mPlayer.start();
            updatePausePlay();
            show(3000);
            return true;
        } else if (keyCode == 86 || keyCode == 127) {
            if (!uniqueDown || !this.mPlayer.isPlaying()) {
                return true;
            }
            this.mPlayer.pause();
            updatePausePlay();
            show(3000);
            return true;
        } else if (keyCode == 25 || keyCode == 24 || keyCode == 164 || keyCode == 27) {
            return super.dispatchKeyEvent(event);
        } else {
            if (keyCode != 4 && keyCode != 82) {
                show(3000);
                return super.dispatchKeyEvent(event);
            } else if (!uniqueDown) {
                return true;
            } else {
                hide();
                return true;
            }
        }
    }

    private int setProgress() {
        if (this.mPlayer == null || this.mDragging) {
            return 0;
        }
        int position = this.mPlayer.getCurrentPosition();
        int duration = this.mPlayer.getDuration();
        if (duration > 0) {
            this.mProgress.setProgress((int) ((1000 * ((long) position)) / ((long) duration)));
        }
        this.mProgress.setSecondaryProgress(this.mPlayer.getBufferPercentage() * 10);
        this.mTime.setText(String.format("%s / %s", new Object[]{stringForTime(position),
                stringForTime(duration)}));
        return position;
    }

    public void setExpandState(boolean isFullScreen) {
        if (isFullScreen) {
            this.mExpandButton.setImageResource(R.drawable.rr);
        } else {
            this.mExpandButton.setImageResource(R.drawable.rl);
        }
    }

    public void setExpandListener(OnClickListener listener) {
        this.mExpandButton.setOnClickListener(listener);
    }

    public void setMediaPlayer(MediaPlayerControl player) {
        this.mPlayer = player;
        updatePausePlay();
    }

    private void updateVisibility() {
        this.mShowing = getVisibility() == 0;
    }

    private void updatePausePlay() {
        if (this.mRoot != null && this.mPauseButton != null && this.mPlayer != null) {
            if (this.mPlayer.isPlaying()) {
                this.mPauseButton.setImageResource(R.drawable.rn);
            } else {
                this.mPauseButton.setImageResource(R.drawable.rp);
            }
        }
    }

    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / FileCache.TIME_HOUR;
        this.mFormatBuilder.setLength(0);
        if (hours > 0) {
            return this.mFormatter.format("%d:%02d:%02d", new Object[]{Integer.valueOf(hours),
                    Integer.valueOf(minutes), Integer.valueOf(seconds)}).toString();
        }
        return this.mFormatter.format("%02d:%02d", new Object[]{Integer.valueOf(minutes), Integer
                .valueOf(seconds)}).toString();
    }

    private void disableUnsupportedButtons() {
        try {
            if (this.mPlayer != null) {
                if (!(this.mPauseButton == null || this.mPlayer.canPause())) {
                    this.mPauseButton.setEnabled(false);
                }
                if (this.mProgress != null && !this.mPlayer.canSeekBackward() && !this.mPlayer
                        .canSeekForward()) {
                    this.mProgress.setEnabled(false);
                }
            }
        } catch (IncompatibleClassChangeError e) {
        }
    }

    public void show(int timeout) {
        if (!(this.mShowing || this.mPlayer == null)) {
            setVisibility(0);
            handleBindViewVisibility(0);
            setProgress();
            updatePausePlay();
            this.mPauseButton.requestFocus();
            disableUnsupportedButtons();
            this.mShowing = true;
        }
        this.mHandler.sendEmptyMessage(2);
        if (timeout != 0) {
            this.mHandler.removeMessages(1);
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(1), (long) timeout);
        }
    }

    public void show() {
        show(3000);
    }

    public void hide() {
        if (this.mShowing) {
            try {
                this.mHandler.removeMessages(2);
                setVisibility(8);
                handleBindViewVisibility(8);
            } catch (IllegalArgumentException e) {
                Log.w("MediaController", "already removed");
            }
            this.mShowing = false;
        }
    }

    public boolean isShowing() {
        return this.mShowing;
    }

    public void setEnabled(boolean enabled) {
        this.mPauseButton.setEnabled(enabled);
        this.mProgress.setEnabled(enabled);
        disableUnsupportedButtons();
        super.setEnabled(enabled);
    }

    public void addBindView(View view) {
        if (view != null) {
            this.mBindViews.add(view);
        }
    }

    public void removeBindView(View view) {
        if (view != null) {
            this.mBindViews.remove(view);
        }
    }

    private void handleBindViewVisibility(int visible) {
        for (View view : this.mBindViews) {
            if (view != null) {
                view.setVisibility(visible);
            }
        }
    }

    public void hideExpandButton() {
        this.mExpandButton.setVisibility(8);
    }
}
