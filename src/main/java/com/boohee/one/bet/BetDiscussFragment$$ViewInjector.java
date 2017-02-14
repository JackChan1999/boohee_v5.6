package com.boohee.one.bet;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;
import com.boohee.one.ui.fragment.GoodsPostsFragment;
import com.boohee.one.ui.fragment.GoodsPostsFragment$$ViewInjector;

public class BetDiscussFragment$$ViewInjector<T extends BetDiscussFragment> extends
        GoodsPostsFragment$$ViewInjector<T> {
    public void inject(Finder finder, final T target, Object source) {
        super.inject(finder, (GoodsPostsFragment) target, source);
        target.tvMsg = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_msg, "field 'tvMsg'"), R.id.tv_msg, "field 'tvMsg'");
        View view = (View) finder.findRequiredView(source, R.id.ll_msg, "field 'llMsg' and method" +
                " 'onClick'");
        target.llMsg = (LinearLayout) finder.castView(view, R.id.ll_msg, "field 'llMsg'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.fab_button, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        super.reset((GoodsPostsFragment) target);
        target.tvMsg = null;
        target.llMsg = null;
    }
}
