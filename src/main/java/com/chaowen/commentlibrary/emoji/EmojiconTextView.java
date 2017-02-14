package com.chaowen.commentlibrary.emoji;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.chaowen.commentlibrary.R;

public class EmojiconTextView extends TextView {
    private int mEmojiconSize;
    private int     mTextLength       = -1;
    private int     mTextStart        = 0;
    private boolean mUseSystemDefault = false;

    public EmojiconTextView(Context context) {
        super(context);
        init(null);
    }

    public EmojiconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public EmojiconTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs == null) {
            this.mEmojiconSize = (int) getTextSize();
        } else {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Emojicon);
            this.mEmojiconSize = (int) a.getDimension(R.styleable.Emojicon_emojiconSize,
                    getTextSize());
            this.mTextStart = a.getInteger(R.styleable.Emojicon_emojiconTextStart, 0);
            this.mTextLength = a.getInteger(R.styleable.Emojicon_emojiconTextLength, -1);
            this.mUseSystemDefault = a.getBoolean(R.styleable.Emojicon_emojiconUseSystemDefault,
                    false);
            a.recycle();
        }
        setText(getText());
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } catch (IndexOutOfBoundsException e) {
            setText(getText().toString());
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public void setGravity(int gravity) {
        try {
            super.setGravity(gravity);
        } catch (IndexOutOfBoundsException e) {
            setText(getText().toString());
            super.setGravity(gravity);
        }
    }

    public void setText(CharSequence text, BufferType type) {
        try {
            if (!TextUtils.isEmpty(text)) {
                SpannableStringBuilder builder = new SpannableStringBuilder(text);
                EmojiconHandler.addEmojis(getContext(), builder, this.mEmojiconSize, this
                        .mTextStart, this.mTextLength, this.mUseSystemDefault);
                text = builder;
            }
            super.setText(text, type);
        } catch (IndexOutOfBoundsException e) {
            setText(text.toString());
        }
    }

    public void setEmojiconSize(int pixels) {
        this.mEmojiconSize = pixels;
        super.setText(getText());
    }

    public void setUseSystemDefault(boolean useSystemDefault) {
        this.mUseSystemDefault = useSystemDefault;
    }
}
