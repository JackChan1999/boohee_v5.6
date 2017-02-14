package com.boohee.uchoice;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

public class IdCardInfoActivity$$ViewInjector<T extends IdCardInfoActivity> implements Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.editName = (EditText) finder.castView((View) finder.findRequiredView(source, R.id
                .edit_name, "field 'editName'"), R.id.edit_name, "field 'editName'");
        target.editIdcard = (EditText) finder.castView((View) finder.findRequiredView(source, R
                .id.edit_idcard, "field 'editIdcard'"), R.id.edit_idcard, "field 'editIdcard'");
        View view = (View) finder.findRequiredView(source, R.id.iv_card1, "field 'ivCard1' and " +
                "method 'onClick'");
        target.ivCard1 = (ImageView) finder.castView(view, R.id.iv_card1, "field 'ivCard1'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        view = (View) finder.findRequiredView(source, R.id.iv_card2, "field 'ivCard2' and method " +
                "'onClick'");
        target.ivCard2 = (ImageView) finder.castView(view, R.id.iv_card2, "field 'ivCard2'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.activityPostIdCard = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.activity_post_id_card, "field 'activityPostIdCard'"), R.id
                .activity_post_id_card, "field 'activityPostIdCard'");
    }

    public void reset(T target) {
        target.editName = null;
        target.editIdcard = null;
        target.ivCard1 = null;
        target.ivCard2 = null;
        target.activityPostIdCard = null;
    }
}
