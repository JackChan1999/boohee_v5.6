package com.boohee.one.sport;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;
import com.boohee.one.sport.DownloadAdapter.DownloadVH;
import com.boohee.widgets.ProgressWheel;

public class DownloadAdapter$DownloadVH$$ViewInjector<T extends DownloadVH> implements Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.cover = (ImageView) finder.castView((View) finder.findRequiredView(source, R.id
                .cover, "field 'cover'"), R.id.cover, "field 'cover'");
        target.name = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .name, "field 'name'"), R.id.name, "field 'name'");
        target.duration = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .duration, "field 'duration'"), R.id.duration, "field 'duration'");
        target.select = (CheckBox) finder.castView((View) finder.findRequiredView(source, R.id
                .select, "field 'select'"), R.id.select, "field 'select'");
        target.progress = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .progress, "field 'progress'"), R.id.progress, "field 'progress'");
        target.coverArea = (View) finder.findRequiredView(source, R.id.cover_area, "field " +
                "'coverArea'");
        target.shadow = (View) finder.findRequiredView(source, R.id.shadow, "field 'shadow'");
        target.wheel = (ProgressWheel) finder.castView((View) finder.findRequiredView(source, R
                .id.wheel, "field 'wheel'"), R.id.wheel, "field 'wheel'");
        target.continueDownload = (View) finder.findRequiredView(source, R.id.continue_download,
                "field 'continueDownload'");
        target.arrow = (ImageView) finder.castView((View) finder.findRequiredView(source, R.id
                .arrow, "field 'arrow'"), R.id.arrow, "field 'arrow'");
        target.item = (RelativeLayout) finder.castView((View) finder.findRequiredView(source, R
                .id.item, "field 'item'"), R.id.item, "field 'item'");
        target.wait = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .wait, "field 'wait'"), R.id.wait, "field 'wait'");
        target.divider = (View) finder.findRequiredView(source, R.id.divider, "field 'divider'");
    }

    public void reset(T target) {
        target.cover = null;
        target.name = null;
        target.duration = null;
        target.select = null;
        target.progress = null;
        target.coverArea = null;
        target.shadow = null;
        target.wheel = null;
        target.continueDownload = null;
        target.arrow = null;
        target.item = null;
        target.wait = null;
        target.divider = null;
    }
}
