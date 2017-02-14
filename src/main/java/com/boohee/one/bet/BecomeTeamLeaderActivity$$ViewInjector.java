package com.boohee.one.bet;

import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class BecomeTeamLeaderActivity$$ViewInjector<T extends BecomeTeamLeaderActivity>
        implements Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        View view = (View) finder.findRequiredView(source, R.id.iv_icon, "field 'iv_icon' and " +
                "method 'onClick'");
        target.iv_icon = (CircleImageView) finder.castView(view, R.id.iv_icon, "field 'iv_icon'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.et_name = (EditText) finder.castView((View) finder.findRequiredView(source, R.id
                .et_name, "field 'et_name'"), R.id.et_name, "field 'et_name'");
        target.et_experience = (EditText) finder.castView((View) finder.findRequiredView(source,
                R.id.et_experience, "field 'et_experience'"), R.id.et_experience, "field " +
                "'et_experience'");
        target.et_contact = (EditText) finder.castView((View) finder.findRequiredView(source, R
                .id.et_contact, "field 'et_contact'"), R.id.et_contact, "field 'et_contact'");
        target.rg_money = (RadioGroup) finder.castView((View) finder.findRequiredView(source, R
                .id.rg_money, "field 'rg_money'"), R.id.rg_money, "field 'rg_money'");
        target.rg_model = (RadioGroup) finder.castView((View) finder.findRequiredView(source, R
                .id.rg_model, "field 'rg_model'"), R.id.rg_model, "field 'rg_model'");
        target.rg_isjoin = (RadioGroup) finder.castView((View) finder.findRequiredView(source, R
                .id.rg_isjoin, "field 'rg_isjoin'"), R.id.rg_isjoin, "field 'rg_isjoin'");
        ((View) finder.findRequiredView(source, R.id.bt_submit, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.iv_icon = null;
        target.et_name = null;
        target.et_experience = null;
        target.et_contact = null;
        target.rg_money = null;
        target.rg_model = null;
        target.rg_isjoin = null;
    }
}
