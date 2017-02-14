package com.boohee.one.video.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.alipay.sdk.sys.a;
import com.boohee.one.R;
import com.boohee.one.video.entity.Mention;
import com.boohee.one.video.ui.MentionPreviewActivity;
import com.boohee.utils.WheelUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class MentionRecyclerAdapter extends Adapter<ViewHolder> {
    private Context       context;
    private List<Mention> mentionList;

    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        @InjectView(2131428466)
        ImageView ivMention;
        @InjectView(2131428465)
        View      layout;
        @InjectView(2131428468)
        TextView  tvMentionInfo;
        @InjectView(2131428467)
        TextView  tvMentionName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject((Object) this, itemView);
        }
    }

    public MentionRecyclerAdapter(Context context, List<Mention> mentionList) {
        this.context = context;
        this.mentionList = mentionList;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i9,
                parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, final int position) {
        Mention mention = (Mention) this.mentionList.get(position);
        ImageLoader.getInstance().displayImage(mention.thumbnail, holder.ivMention);
        holder.tvMentionName.setText(mention.name);
        holder.tvMentionInfo.setText(mention.group_count + "x" + mention.number + (mention
                .is_times ? "" : a.e));
        holder.layout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (!WheelUtils.isFastDoubleClick()) {
                    MentionPreviewActivity.comeOnBaby(MentionRecyclerAdapter.this.context,
                            MentionRecyclerAdapter.this.mentionList, position);
                }
            }
        });
    }

    public int getItemCount() {
        return this.mentionList.size();
    }
}
