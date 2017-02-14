package com.boohee.one.video.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;
import com.boohee.one.video.adapter.MentionRecyclerAdapter.ViewHolder;

public class MentionRecyclerAdapter$ViewHolder$$ViewInjector<T extends ViewHolder> implements
        Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.layout = (View) finder.findRequiredView(source, R.id.item_mention_layout, "field " +
                "'layout'");
        target.ivMention = (ImageView) finder.castView((View) finder.findRequiredView(source, R
                .id.iv_mention, "field 'ivMention'"), R.id.iv_mention, "field 'ivMention'");
        target.tvMentionName = (TextView) finder.castView((View) finder.findRequiredView(source,
                R.id.tv_mention_name, "field 'tvMentionName'"), R.id.tv_mention_name, "field " +
                "'tvMentionName'");
        target.tvMentionInfo = (TextView) finder.castView((View) finder.findRequiredView(source,
                R.id.tv_mention_info, "field 'tvMentionInfo'"), R.id.tv_mention_info, "field " +
                "'tvMentionInfo'");
    }

    public void reset(T target) {
        target.layout = null;
        target.ivMention = null;
        target.tvMentionName = null;
        target.tvMentionInfo = null;
    }
}
