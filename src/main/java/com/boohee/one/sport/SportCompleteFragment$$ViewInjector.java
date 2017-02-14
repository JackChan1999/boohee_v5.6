package com.boohee.one.sport;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

public class SportCompleteFragment$$ViewInjector<T extends SportCompleteFragment> implements
        Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.tvCalorie = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .calorie, "field 'tvCalorie'"), R.id.calorie, "field 'tvCalorie'");
        target.tvDuration = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.duration, "field 'tvDuration'"), R.id.duration, "field 'tvDuration'");
        target.tvDiamond = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .diamond, "field 'tvDiamond'"), R.id.diamond, "field 'tvDiamond'");
        View view = (View) finder.findRequiredView(source, R.id.complete, "field 'btnComplete' " +
                "and method 'onClick'");
        target.btnComplete = (Button) finder.castView(view, R.id.complete, "field 'btnComplete'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        view = (View) finder.findRequiredView(source, R.id.share, "field 'btnShare' and method " +
                "'onClick'");
        target.btnShare = (Button) finder.castView(view, R.id.share, "field 'btnShare'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.tvMessage = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .message, "field 'tvMessage'"), R.id.message, "field 'tvMessage'");
        target.diamondLine = (View) finder.findRequiredView(source, R.id.diamond_line, "field " +
                "'diamondLine'");
        target.completeLine = (View) finder.findRequiredView(source, R.id.complete_line, "field " +
                "'completeLine'");
        target.closeLine = (View) finder.findRequiredView(source, R.id.close_line, "field " +
                "'closeLine'");
        ((View) finder.findRequiredView(source, R.id.close, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.tvCalorie = null;
        target.tvDuration = null;
        target.tvDiamond = null;
        target.btnComplete = null;
        target.btnShare = null;
        target.tvMessage = null;
        target.diamondLine = null;
        target.completeLine = null;
        target.closeLine = null;
    }
}
