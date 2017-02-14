package com.boohee.one.ui;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

public class CompleteRegisterActivity$$ViewInjector<T extends CompleteRegisterActivity>
        implements Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.checkBox = (CheckBox) finder.castView((View) finder.findRequiredView(source, R.id
                .checkBox, "field 'checkBox'"), R.id.checkBox, "field 'checkBox'");
        View view = (View) finder.findRequiredView(source, R.id.userDeal, "field 'userDeal' and " +
                "method 'OnClick'");
        target.userDeal = (TextView) finder.castView(view, R.id.userDeal, "field 'userDeal'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.OnClick(p0);
            }
        });
        view = (View) finder.findRequiredView(source, R.id.loginBtn, "field 'loginBtn' and method" +
                " 'OnClick'");
        target.loginBtn = (Button) finder.castView(view, R.id.loginBtn, "field 'loginBtn'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.OnClick(p0);
            }
        });
        target.etName = (EditText) finder.castView((View) finder.findRequiredView(source, R.id
                .userName, "field 'etName'"), R.id.userName, "field 'etName'");
        target.etPassWord = (EditText) finder.castView((View) finder.findRequiredView(source, R
                .id.passWord, "field 'etPassWord'"), R.id.passWord, "field 'etPassWord'");
    }

    public void reset(T target) {
        target.checkBox = null;
        target.userDeal = null;
        target.loginBtn = null;
        target.etName = null;
        target.etPassWord = null;
    }
}
