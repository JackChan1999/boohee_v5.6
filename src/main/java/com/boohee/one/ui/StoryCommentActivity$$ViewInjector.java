package com.boohee.one.ui;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class StoryCommentActivity$$ViewInjector<T extends StoryCommentActivity> implements
        Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.mPullRefreshListView = (PullToRefreshListView) finder.castView((View) finder
                .findRequiredView(source, R.id.listview, "field 'mPullRefreshListView'"), R.id
                .listview, "field 'mPullRefreshListView'");
        target.commentEdit = (EditText) finder.castView((View) finder.findRequiredView(source, R
                .id.et_comment, "field 'commentEdit'"), R.id.et_comment, "field 'commentEdit'");
        View view = (View) finder.findRequiredView(source, R.id.btn_comment, "field 'commentBtn' " +
                "and method 'onClick'");
        target.commentBtn = (Button) finder.castView(view, R.id.btn_comment, "field 'commentBtn'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.mPullRefreshListView = null;
        target.commentEdit = null;
        target.commentBtn = null;
    }
}
