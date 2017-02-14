package com.meiqia.meiqiasdk.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.meiqia.meiqiasdk.R;
import com.meiqia.meiqiasdk.util.MQAudioPlayerManager;
import com.meiqia.meiqiasdk.util.MQAudioRecorderManager;
import com.meiqia.meiqiasdk.util.MQUtils;

import java.io.File;

public class MQRecorderKeyboardLayout extends MQBaseCustomCompositeView implements com.meiqia
        .meiqiasdk.util.MQAudioRecorderManager.Callback, OnTouchListener {
    private static final int RECORDER_MAX_TIME    = 60;
    private static final int STATE_NORMAL         = 1;
    private static final int STATE_RECORDING      = 2;
    private static final int STATE_WANT_CANCEL    = 3;
    private static final int VOICE_LEVEL_COUNT    = 9;
    private static final int WHAT_AUDIO_PREPARED  = 1;
    private static final int WHAT_HANDLE_OVERTIME = 3;
    private static final int WHAT_VOICE_CHANGED   = 2;
    private ImageView              mAnimIv;
    private MQAudioRecorderManager mAudioRecorderManager;
    private Callback               mCallback;
    private int mCurrentState = 1;
    private int mDistanceCancel;
    private Runnable mGetVoiceLevelRunnable = new Runnable() {
        public void run() {
            while (MQRecorderKeyboardLayout.this.mIsRecording) {
                try {
                    Thread.sleep(100);
                    MQRecorderKeyboardLayout.this.mTime = MQRecorderKeyboardLayout.this.mTime + 0
                    .1f;
                    if (MQRecorderKeyboardLayout.this.mTime <= 60.0f) {
                        MQRecorderKeyboardLayout.this.mHandler.sendEmptyMessage(2);
                    } else {
                        MQRecorderKeyboardLayout.this.mHandler.sendEmptyMessage(3);
                        return;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private Handler  mHandler               = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    MQRecorderKeyboardLayout.this.mIsRecording = true;
                    new Thread(MQRecorderKeyboardLayout.this.mGetVoiceLevelRunnable).start();
                    return;
                case 2:
                    if (MQRecorderKeyboardLayout.this.mCurrentState == 2) {
                        MQRecorderKeyboardLayout.this.mAnimIv.setImageResource
                                (MQRecorderKeyboardLayout.this.getContext().getResources()
                                        .getIdentifier("mq_voice_level" +
                                                MQRecorderKeyboardLayout.this
                                                        .mAudioRecorderManager.getVoiceLevel(9),
                                                "drawable", MQRecorderKeyboardLayout.this
                                                        .getContext().getPackageName()));
                        MQRecorderKeyboardLayout.this.mAnimIv.setColorFilter
                                (MQRecorderKeyboardLayout.this.getResources().getColor(R.color
                                        .mq_chat_audio_recorder_icon));
                        if (Math.round(60.0f - MQRecorderKeyboardLayout.this.mTime) <= 10) {
                            MQRecorderKeyboardLayout.this.mStatusTv.setText
                                    (MQRecorderKeyboardLayout.this.getContext().getString(R
                                            .string.mq_recorder_remaining_time, new
                                            Object[]{Integer.valueOf(remainingTime)}));
                            return;
                        }
                        return;
                    }
                    return;
                case 3:
                    MQRecorderKeyboardLayout.this.mIsOvertime = true;
                    MQRecorderKeyboardLayout.this.handleActionUp();
                    return;
                default:
                    return;
            }
        }
    };
    private boolean  mHasPermission         = false;
    private boolean  mIsOvertime            = false;
    private boolean  mIsRecording;
    private long     mLastTipTooShortTime;
    private TextView mStatusTv;
    private float    mTime;

    public interface Callback {
        void onAudioRecorderFinish(int i, String str);

        void onAudioRecorderNoPermission();

        void onAudioRecorderTooShort();
    }

    public MQRecorderKeyboardLayout(Context context) {
        super(context);
    }

    public MQRecorderKeyboardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MQRecorderKeyboardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected int getLayoutId() {
        return R.layout.mq_layout_recorder_keyboard;
    }

    protected void initView() {
        this.mStatusTv = (TextView) getViewById(R.id.tv_recorder_keyboard_status);
        this.mAnimIv = (ImageView) getViewById(R.id.iv_recorder_keyboard_anim);
    }

    protected void setListener() {
        this.mAnimIv.setOnTouchListener(this);
    }

    protected int[] getAttrs() {
        return new int[0];
    }

    protected void initAttr(int attr, TypedArray typedArray) {
    }

    protected void processLogic() {
        this.mAnimIv.setColorFilter(getResources().getColor(R.color.mq_chat_audio_recorder_icon));
        this.mDistanceCancel = MQUtils.dip2px(getContext(), 10.0f);
        this.mAudioRecorderManager = MQAudioRecorderManager.getInstance(getContext());
        this.mAudioRecorderManager.setCallback(this);
    }

    public void wellPrepared() {
        this.mHandler.sendEmptyMessage(1);
    }

    public void onAudioRecorderNoPermission() {
        endRecorder();
        reset();
    }

    private void changeState(int status) {
        if (this.mCurrentState != status) {
            this.mCurrentState = status;
            switch (this.mCurrentState) {
                case 1:
                    this.mStatusTv.setText(R.string.mq_audio_status_normal);
                    this.mAnimIv.setImageResource(R.drawable.mq_voice_level1);
                    this.mAnimIv.setColorFilter(getResources().getColor(R.color
                            .mq_chat_audio_recorder_icon));
                    return;
                case 2:
                    this.mStatusTv.setText(R.string.mq_audio_status_recording);
                    return;
                case 3:
                    this.mStatusTv.setText(R.string.mq_audio_status_want_cancel);
                    this.mAnimIv.setImageResource(R.drawable.mq_voice_want_cancel);
                    this.mAnimIv.clearColorFilter();
                    return;
                default:
                    return;
            }
        }
    }

    private boolean isWantCancel(int x, int y) {
        if (y < (-this.mDistanceCancel)) {
            return true;
        }
        return false;
    }

    public boolean onTouch(View v, MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case 0:
                this.mIsOvertime = false;
                this.mHasPermission = true;
                changeState(2);
                this.mAudioRecorderManager.prepareAudio();
                break;
            case 1:
                handleActionUp();
                break;
            case 2:
                if (!this.mIsOvertime && this.mIsRecording && this.mHasPermission) {
                    if (!isWantCancel(x, y)) {
                        changeState(2);
                        break;
                    }
                    changeState(3);
                    break;
                }
            case 3:
                this.mAudioRecorderManager.cancel();
                reset();
                break;
        }
        return true;
    }

    private void handleActionUp() {
        if (this.mIsOvertime || !this.mHasPermission) {
            if (this.mIsRecording) {
                endRecorder();
            }
        } else if (!this.mIsRecording || this.mTime < 1.0f) {
            this.mAudioRecorderManager.cancel();
            if (System.currentTimeMillis() - this.mLastTipTooShortTime > 1000) {
                this.mLastTipTooShortTime = System.currentTimeMillis();
                this.mCallback.onAudioRecorderTooShort();
            }
        } else if (this.mCurrentState == 2) {
            endRecorder();
        } else if (this.mCurrentState == 3) {
            this.mAudioRecorderManager.cancel();
        }
        reset();
    }

    private void endRecorder() {
        this.mAudioRecorderManager.release();
        if (this.mCallback != null) {
            String currentFilePath = this.mAudioRecorderManager.getCurrenFilePath();
            if (!TextUtils.isEmpty(currentFilePath)) {
                File currentFile = new File(currentFilePath);
                if (!currentFile.exists() || currentFile.length() <= 6) {
                    this.mAudioRecorderManager.cancel();
                    this.mCallback.onAudioRecorderNoPermission();
                    return;
                }
                this.mCallback.onAudioRecorderFinish(MQAudioPlayerManager.getDurationByFilePath
                        (getContext(), currentFile.getAbsolutePath()), currentFile
                        .getAbsolutePath());
            }
        }
    }

    private void reset() {
        this.mIsRecording = false;
        this.mHasPermission = false;
        this.mTime = 0.0f;
        changeState(1);
    }

    public boolean isRecording() {
        return this.mCurrentState != 1;
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }
}
