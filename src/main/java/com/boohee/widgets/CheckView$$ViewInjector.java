package com.boohee.widgets;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

public class CheckView$$ViewInjector<T extends CheckView> implements Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.txtTitle = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .txt_title, "field 'txtTitle'"), R.id.txt_title, "field 'txtTitle'");
        target.txtMsg = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .txt_msg, "field 'txtMsg'"), R.id.txt_msg, "field 'txtMsg'");
        View view = (View) finder.findRequiredView(source, R.id.btn_pos, "field 'btnPos' and " +
                "method 'onClick'");
        target.btnPos = (Button) finder.castView(view, R.id.btn_pos, "field 'btnPos'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.txtTitle = null;
        target.txtMsg = null;
        target.btnPos = null;
    }
}
