package com.chaowen.commentlibrary.emoji;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout.LayoutParams;

import com.chaowen.commentlibrary.R;

import java.util.List;

class EmojiAdapter extends ArrayAdapter<Emojicon> {
    private int     mEmojiHeight;
    private boolean mUseSystemDefault;

    class ViewHolder {
        EmojiconTextView icon;

        ViewHolder() {
        }
    }

    public EmojiAdapter(Context context, List<Emojicon> data, int emojiHeight) {
        super(context, R.layout.item_emoji, data);
        this.mUseSystemDefault = false;
        this.mUseSystemDefault = false;
        this.mEmojiHeight = emojiHeight;
    }

    public EmojiAdapter(Context context, List<Emojicon> data, boolean useSystemDefault, int
            emojiHeight) {
        super(context, R.layout.item_emoji, data);
        this.mUseSystemDefault = false;
        this.mUseSystemDefault = useSystemDefault;
        this.mEmojiHeight = emojiHeight;
    }

    public EmojiAdapter(Context context, Emojicon[] data, int emojiHeight) {
        super(context, R.layout.item_emoji, data);
        this.mUseSystemDefault = false;
        this.mUseSystemDefault = false;
        this.mEmojiHeight = emojiHeight;
    }

    public EmojiAdapter(Context context, Emojicon[] data, boolean useSystemDefault, int
            emojiHeight) {
        super(context, R.layout.item_emoji, data);
        this.mUseSystemDefault = false;
        this.mUseSystemDefault = useSystemDefault;
        this.mEmojiHeight = emojiHeight;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View v = convertView;
        if (v == null) {
            v = View.inflate(getContext(), R.layout.item_emoji, null);
            holder = new ViewHolder();
            holder.icon = (EmojiconTextView) v.findViewById(R.id.emojicon_icon);
            holder.icon.setUseSystemDefault(this.mUseSystemDefault);
            v.setTag(holder);
        }
        holder = (ViewHolder) v.getTag();
        holder.icon.setText(((Emojicon) getItem(position)).getEmoji());
        if (position == 27) {
            holder.icon.setBackgroundResource(R.drawable.btn_del_nor);
        } else {
            holder.icon.setBackgroundResource(R.color.transparent);
        }
        holder.icon.setLayoutParams(new LayoutParams(-1, this.mEmojiHeight));
        return v;
    }
}
