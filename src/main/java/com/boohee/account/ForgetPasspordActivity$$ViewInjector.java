package com.boohee.account;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

public class ForgetPasspordActivity$$ViewInjector<T extends ForgetPasspordActivity> implements
        Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.accountEdit = (EditText) finder.castView((View) finder.findRequiredView(source, R
                .id.accountEdit, "field 'accountEdit'"), R.id.accountEdit, "field 'accountEdit'");
        target.tvPassword = (EditText) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_password, "field 'tvPassword'"), R.id.tv_password, "field 'tvPassword'");
        target.tvCaptcha = (EditText) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_captcha, "field 'tvCaptcha'"), R.id.tv_captcha, "field 'tvCaptcha'");
        View view = (View) finder.findRequiredView(source, R.id.bt_captcha, "field 'btCaptcha' " +
                "and method 'onClick'");
        target.btCaptcha = (Button) finder.castView(view, R.id.bt_captcha, "field 'btCaptcha'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        view = (View) finder.findRequiredView(source, R.id.tv_voice_captcha, "field " +
                "'tvVoiceCaptcha' and method 'onClick'");
        target.tvVoiceCaptcha = (TextView) finder.castView(view, R.id.tv_voice_captcha, "field " +
                "'tvVoiceCaptcha'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.accountEdit = null;
        target.tvPassword = null;
        target.tvCaptcha = null;
        target.btCaptcha = null;
        target.tvVoiceCaptcha = null;
    }
}
