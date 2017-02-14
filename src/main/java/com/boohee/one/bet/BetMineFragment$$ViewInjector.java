package com.boohee.one.bet;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

public class BetMineFragment$$ViewInjector<T extends BetMineFragment> extends
        BetListFragment$$ViewInjector<T> {
    public void inject(Finder finder, final T target, Object source) {
        super.inject(finder, (BetListFragment) target, source);
        target.msgView = (RelativeLayout) finder.castView((View) finder.findRequiredView(source,
                R.id.msg_view, "field 'msgView'"), R.id.msg_view, "field 'msgView'");
        target.tvMsg = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_msg, "field 'tvMsg'"), R.id.tv_msg, "field 'tvMsg'");
        target.tvTime = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_time, "field 'tvTime'"), R.id.tv_time, "field 'tvTime'");
        View view = (View) finder.findRequiredView(source, R.id.btn_msg, "field 'btnMsg' and " +
                "method 'onClick'");
        target.btnMsg = (TextView) finder.castView(view, R.id.btn_msg, "field 'btnMsg'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        super.reset((BetListFragment) target);
        target.msgView = null;
        target.tvMsg = null;
        target.tvTime = null;
        target.btnMsg = null;
    }
}
