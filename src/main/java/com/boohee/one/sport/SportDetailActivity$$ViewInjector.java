package com.boohee.one.sport;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;
import com.boohee.one.player.ExVideoView;

public class SportDetailActivity$$ViewInjector<T extends SportDetailActivity> implements
        Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.videoView = (ExVideoView) finder.castView((View) finder.findRequiredView(source, R
                .id.video, "field 'videoView'"), R.id.video, "field 'videoView'");
        target.tvName = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .sport_name, "field 'tvName'"), R.id.sport_name, "field 'tvName'");
        target.tvDescription = (TextView) finder.castView((View) finder.findRequiredView(source,
                R.id.sport_description, "field 'tvDescription'"), R.id.sport_description, "field " +
                "'tvDescription'");
        target.tvSportInfo = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.sport_info, "field 'tvSportInfo'"), R.id.sport_info, "field 'tvSportInfo'");
        target.tvCalorie = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .calorie, "field 'tvCalorie'"), R.id.calorie, "field 'tvCalorie'");
        target.tvDuration = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.duration, "field 'tvDuration'"), R.id.duration, "field 'tvDuration'");
        target.tvTick = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tick, "field 'tvTick'"), R.id.tick, "field 'tvTick'");
        target.tvShare = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .share, "field 'tvShare'"), R.id.share, "field 'tvShare'");
        target.tickArea = (View) finder.findRequiredView(source, R.id.tick_area, "field " +
                "'tickArea'");
        target.shareArea = (View) finder.findRequiredView(source, R.id.share_area, "field " +
                "'shareArea'");
        target.llTick = (LinearLayout) finder.castView((View) finder.findRequiredView(source, R
                .id.tick_line, "field 'llTick'"), R.id.tick_line, "field 'llTick'");
        target.tvHint = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .use_hint, "field 'tvHint'"), R.id.use_hint, "field 'tvHint'");
    }

    public void reset(T target) {
        target.videoView = null;
        target.tvName = null;
        target.tvDescription = null;
        target.tvSportInfo = null;
        target.tvCalorie = null;
        target.tvDuration = null;
        target.tvTick = null;
        target.tvShare = null;
        target.tickArea = null;
        target.shareArea = null;
        target.llTick = null;
        target.tvHint = null;
    }
}
