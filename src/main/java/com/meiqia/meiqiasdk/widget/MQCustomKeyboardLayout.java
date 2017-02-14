package com.meiqia.meiqiasdk.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

import com.meiqia.meiqiasdk.R;
import com.meiqia.meiqiasdk.util.MQEmotionUtil;
import com.meiqia.meiqiasdk.util.MQUtils;

public class MQCustomKeyboardLayout extends MQBaseCustomCompositeView {
    private static final int WHAT_CHANGE_TO_EMOTION_KEYBOARD = 2;
    private static final int WHAT_CHANGE_TO_VOICE_KEYBOARD   = 3;
    private static final int WHAT_SCROLL_CONTENT_TO_BOTTOM   = 1;
    private Activity                mActivity;
    private Callback                mCallback;
    private EditText                mContentEt;
    private MQEmotionKeyboardLayout mEmotionKeyboardLayout;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    MQCustomKeyboardLayout.this.mCallback.scrollContentToBottom();
                    return;
                case 2:
                    MQCustomKeyboardLayout.this.showEmotionKeyboard();
                    return;
                case 3:
                    MQCustomKeyboardLayout.this.showVoiceKeyboard();
                    return;
                default:
                    return;
            }
        }
    };
    private MQRecorderKeyboardLayout mRecorderKeyboardLayout;

    public interface Callback {
        void onAudioRecorderFinish(int i, String str);

        void onAudioRecorderNoPermission();

        void onAudioRecorderTooShort();

        void scrollContentToBottom();
    }

    public MQCustomKeyboardLayout(Context context) {
        super(context);
    }

    public MQCustomKeyboardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MQCustomKeyboardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected int getLayoutId() {
        return R.layout.mq_layout_custom_keyboard;
    }

    protected void initView() {
        this.mEmotionKeyboardLayout = (MQEmotionKeyboardLayout) getViewById(R.id
                .emotionKeyboardLayout);
        this.mRecorderKeyboardLayout = (MQRecorderKeyboardLayout) getViewById(R.id
                .recorderKeyboardLayout);
    }

    protected void setListener() {
        this.mEmotionKeyboardLayout.setCallback(new com.meiqia.meiqiasdk.widget
                .MQEmotionKeyboardLayout.Callback() {
            public void onDelete() {
                MQCustomKeyboardLayout.this.mContentEt.dispatchKeyEvent(new KeyEvent(0, 67));
            }

            public void onInsert(String text) {
                int cursorPosition = MQCustomKeyboardLayout.this.mContentEt.getSelectionStart();
                StringBuilder sb = new StringBuilder(MQCustomKeyboardLayout.this.mContentEt
                        .getText());
                sb.insert(cursorPosition, text);
                MQCustomKeyboardLayout.this.mContentEt.setText(MQEmotionUtil.getEmotionText
                        (MQCustomKeyboardLayout.this.getContext(), sb.toString(), 20));
                MQCustomKeyboardLayout.this.mContentEt.setSelection(text.length() + cursorPosition);
            }
        });
        this.mRecorderKeyboardLayout.setCallback(new com.meiqia.meiqiasdk.widget
                .MQRecorderKeyboardLayout.Callback() {
            public void onAudioRecorderFinish(int time, String filePath) {
                if (MQCustomKeyboardLayout.this.mCallback != null) {
                    MQCustomKeyboardLayout.this.mCallback.onAudioRecorderFinish(time, filePath);
                }
            }

            public void onAudioRecorderTooShort() {
                if (MQCustomKeyboardLayout.this.mCallback != null) {
                    MQCustomKeyboardLayout.this.mCallback.onAudioRecorderTooShort();
                }
            }

            public void onAudioRecorderNoPermission() {
                if (MQCustomKeyboardLayout.this.mCallback != null) {
                    MQCustomKeyboardLayout.this.mCallback.onAudioRecorderNoPermission();
                }
            }
        });
    }

    protected int[] getAttrs() {
        return new int[0];
    }

    protected void initAttr(int attr, TypedArray typedArray) {
    }

    protected void processLogic() {
    }

    public void toggleEmotionOriginKeyboard() {
        if (isEmotionKeyboardVisible()) {
            changeToOriginalKeyboard();
        } else {
            changeToEmotionKeyboard();
        }
    }

    public void toggleVoiceOriginKeyboard() {
        if (isVoiceKeyboardVisible()) {
            changeToOriginalKeyboard();
        } else {
            changeToVoiceKeyboard();
        }
    }

    public void changeToVoiceKeyboard() {
        MQUtils.closeKeyboard(this.mActivity);
        if (isCustomKeyboardVisible()) {
            showVoiceKeyboard();
        } else {
            this.mHandler.sendEmptyMessageDelayed(3, 300);
        }
    }

    public void changeToEmotionKeyboard() {
        if (!this.mContentEt.isFocused()) {
            this.mContentEt.requestFocus();
            this.mContentEt.setSelection(this.mContentEt.getText().toString().length());
        }
        MQUtils.closeKeyboard(this.mActivity);
        if (isCustomKeyboardVisible()) {
            showEmotionKeyboard();
        } else {
            this.mHandler.sendEmptyMessageDelayed(2, 300);
        }
    }

    public void changeToOriginalKeyboard() {
        closeCustomKeyboard();
        MQUtils.openKeyboard(this.mContentEt);
        this.mHandler.sendEmptyMessageDelayed(1, 600);
    }

    private void showEmotionKeyboard() {
        this.mEmotionKeyboardLayout.setVisibility(0);
        sendScrollContentToBottomMsg();
        closeVoiceKeyboard();
    }

    private void showVoiceKeyboard() {
        this.mRecorderKeyboardLayout.setVisibility(0);
        sendScrollContentToBottomMsg();
        closeEmotionKeyboard();
    }

    private void sendScrollContentToBottomMsg() {
        this.mHandler.sendEmptyMessageDelayed(1, 300);
    }

    public void closeEmotionKeyboard() {
        this.mEmotionKeyboardLayout.setVisibility(8);
    }

    public void closeVoiceKeyboard() {
        this.mRecorderKeyboardLayout.setVisibility(8);
    }

    public void closeCustomKeyboard() {
        closeEmotionKeyboard();
        closeVoiceKeyboard();
    }

    public void closeAllKeyboard() {
        closeCustomKeyboard();
        MQUtils.closeKeyboard(this.mActivity);
    }

    public boolean isEmotionKeyboardVisible() {
        return this.mEmotionKeyboardLayout.getVisibility() == 0;
    }

    public boolean isVoiceKeyboardVisible() {
        return this.mRecorderKeyboardLayout.getVisibility() == 0;
    }

    public boolean isCustomKeyboardVisible() {
        return isEmotionKeyboardVisible() || isVoiceKeyboardVisible();
    }

    public void init(Activity activity, EditText contentEt, Callback callback) {
        if (activity == null || contentEt == null || callback == null) {
            throw new RuntimeException(MQCustomKeyboardLayout.class.getSimpleName() +
                    "的init方法的参数均不能为null");
        }
        this.mActivity = activity;
        this.mContentEt = contentEt;
        this.mCallback = callback;
        this.mContentEt.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (MQCustomKeyboardLayout.this.isCustomKeyboardVisible()) {
                    MQCustomKeyboardLayout.this.closeCustomKeyboard();
                }
                MQCustomKeyboardLayout.this.sendScrollContentToBottomMsg();
            }
        });
        this.mContentEt.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    MQCustomKeyboardLayout.this.sendScrollContentToBottomMsg();
                } else {
                    MQCustomKeyboardLayout.this.closeAllKeyboard();
                }
            }
        });
    }

    public boolean isRecording() {
        return this.mRecorderKeyboardLayout.isRecording();
    }

    protected <VT extends View> VT getViewById(@IdRes int id) {
        return findViewById(id);
    }
}
