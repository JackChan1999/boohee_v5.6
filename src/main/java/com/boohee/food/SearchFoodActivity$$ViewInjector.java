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
import com.boohee.widgets.ExpandGridView;

public class SearchFoodActivity$$ViewInjector<T extends SearchFoodActivity> implements Injector<T> {
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
        View view = (View) finder.findRequiredView(source, R.id.view_load_more, "field " +
                "'viewLoadMore' and method 'onClick'");
        target.viewLoadMore = (TextView) finder.castView(view, R.id.view_load_more, "field " +
                "'viewLoadMore'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.tv_null = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_null, "field 'tv_null'"), R.id.tv_null, "field 'tv_null'");
        target.sv_search = (ScrollView) finder.castView((View) finder.findRequiredView(source, R
                .id.sv_search, "field 'sv_search'"), R.id.sv_search, "field 'sv_search'");
        target.gvHot = (ExpandGridView) finder.castView((View) finder.findRequiredView(source, R
                .id.gv_hot, "field 'gvHot'"), R.id.gv_hot, "field 'gvHot'");
        view = (View) finder.findRequiredView(source, R.id.search_tip, "field 'tv_search_tip' and" +
                " method 'onClick'");
        target.tv_search_tip = (TextView) finder.castView(view, R.id.search_tip, "field " +
                "'tv_search_tip'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
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
        target.viewLoadMore = null;
        target.tv_null = null;
        target.sv_search = null;
        target.gvHot = null;
        target.tv_search_tip = null;
    }
}
