package com.chaowen.commentlibrary.emoji;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.chaowen.commentlibrary.R;

import java.util.ArrayList;
import java.util.List;

public class EmojiViewPagerAdapter extends RecyclingPagerAdapter {
    private LayoutInflater infalter;
    private int mChildCount = 0;
    private int                  mEmojiHeight;
    private OnClickEmojiListener mListener;
    private List<List<Emojicon>> mPagers = new ArrayList();

    public interface OnClickEmojiListener {
        void onDelete();

        void onEmojiClick(Emojicon emojicon);
    }

    static class ViewHolder {
        GridView gv;

        public ViewHolder(View v) {
            this.gv = (GridView) v.findViewById(R.id.gv_emoji);
        }
    }

    public EmojiViewPagerAdapter(Context context, List<List<Emojicon>> pager, int emojiHeight,
                                 OnClickEmojiListener listener) {
        this.infalter = LayoutInflater.from(context);
        this.mPagers = pager;
        this.mEmojiHeight = emojiHeight;
        this.mListener = listener;
    }

    public void setEmojiHeight(int emojiHeight) {
        this.mEmojiHeight = emojiHeight;
        notifyDataSetChanged();
    }

    @SuppressLint({"InflateParams"})
    public View getView(int position, View convertView, ViewGroup container) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = this.infalter.inflate(R.layout.view_pager_emoji, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        final EmojiAdapter adapter = new EmojiAdapter(container.getContext(), (List) this.mPagers
                .get(position), this.mEmojiHeight);
        vh.gv.setAdapter(adapter);
        vh.gv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (EmojiViewPagerAdapter.this.mListener != null) {
                    if (position == adapter.getCount() - 1) {
                        EmojiViewPagerAdapter.this.mListener.onDelete();
                    } else {
                        EmojiViewPagerAdapter.this.mListener.onEmojiClick((Emojicon) adapter
                                .getItem(position));
                    }
                }
            }
        });
        adapter.notifyDataSetChanged();
        return convertView;
    }

    public int getCount() {
        return this.mPagers.size();
    }

    public void notifyDataSetChanged() {
        this.mChildCount = getCount();
        super.notifyDataSetChanged();
    }

    public int getItemPosition(Object object) {
        if (this.mChildCount <= 0) {
            return super.getItemPosition(object);
        }
        this.mChildCount--;
        return -2;
    }
}
