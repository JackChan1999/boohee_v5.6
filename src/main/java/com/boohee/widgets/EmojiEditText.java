package com.boohee.widgets;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.util.AttributeSet;
import android.widget.EditText;

import com.boohee.utils.EmojiUtils;

public class EmojiEditText extends EditText {
    static final String TAG = EmojiEditText.class.getSimpleName();
    private Context context;
    private ImageGetter emojiGetter = new ImageGetter() {
        public Drawable getDrawable(String source) {
            Drawable emoji = EmojiEditText.this.getResources().getDrawable(EmojiEditText.this
                    .getResources().getIdentifier(source, "drawable", EmojiEditText.this.context
                            .getPackageName()));
            emoji.setBounds(0, 0, 40, 40);
            return emoji;
        }
    };
    private String text;

    public EmojiEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    public EmojiEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public EmojiEditText(Context context) {
        super(context);
        this.context = context;
    }

    public void setEmojiText(String text) {
        this.text = text;
        setText(Html.fromHtml(EmojiUtils.convetToHtml(text), this.emojiGetter, null));
    }

    public String getString() {
        return this.text;
    }
}
