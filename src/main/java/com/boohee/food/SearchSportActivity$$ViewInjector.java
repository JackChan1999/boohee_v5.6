package com.boohee.food;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

public class SearchSportActivity$$ViewInjector<T extends SearchSportActivity> implements
        Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.ll_history = (LinearLayout) finder.castView((View) finder.findRequiredView(source,
                R.id.ll_history, "field 'll_history'"), R.id.ll_history, "field 'll_history'");
        target.ll_result = (LinearLayout) finder.castView((View) finder.findRequiredView(source,
                R.id.ll_result, "field 'll_result'"), R.id.ll_result, "field 'll_result'");
        target.ll_history_content = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.ll_history_content, "field 'll_history_content'"), R.id
                .ll_history_content, "field 'll_history_content'");
        target.lv_result = (ListView) finder.castView((View) finder.findRequiredView(source, R.id
                .lv_result, "field 'lv_result'"), R.id.lv_result, "field 'lv_result'");
        target.tv_null = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_null, "field 'tv_null'"), R.id.tv_null, "field 'tv_null'");
        target.sv_search = (ScrollView) finder.castView((View) finder.findRequiredView(source, R
                .id.sv_search, "field 'sv_search'"), R.id.sv_search, "field 'sv_search'");
        ((View) finder.findRequiredView(source, R.id.rl_history_clear, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.tv_search_null, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.ll_history = null;
        target.ll_result = null;
        target.ll_history_content = null;
        target.lv_result = null;
        target.tv_null = null;
        target.sv_search = null;
    }
}
