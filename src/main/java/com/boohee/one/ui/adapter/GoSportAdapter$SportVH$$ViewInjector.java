package com.boohee.one.ui.adapter;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;
import com.boohee.one.ui.adapter.GoSportAdapter.SportVH;

import de.hdodenhof.circleimageview.CircleImageView;

public class GoSportAdapter$SportVH$$ViewInjector<T extends SportVH> implements Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.icon = (CircleImageView) finder.castView((View) finder.findRequiredView(source, R
                .id.icon, "field 'icon'"), R.id.icon, "field 'icon'");
        target.name = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .name, "field 'name'"), R.id.name, "field 'name'");
        target.duration = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .duration, "field 'duration'"), R.id.duration, "field 'duration'");
        target.record = (Button) finder.castView((View) finder.findRequiredView(source, R.id
                .record, "field 'record'"), R.id.record, "field 'record'");
    }

    public void reset(T target) {
        target.icon = null;
        target.name = null;
        target.duration = null;
        target.record = null;
    }
}
