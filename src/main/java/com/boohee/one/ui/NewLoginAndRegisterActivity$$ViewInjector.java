package com.boohee.one.ui;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.myview.SettingItemView;
import com.boohee.one.R;

public class NewLoginAndRegisterActivity$$ViewInjector<T extends NewLoginAndRegisterActivity>
        implements Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.accountEdit = (EditText) finder.castView((View) finder.findRequiredView(source, R
                .id.accountEdit, "field 'accountEdit'"), R.id.accountEdit, "field 'accountEdit'");
        target.passWordEdit = (EditText) finder.castView((View) finder.findRequiredView(source, R
                .id.passWord, "field 'passWordEdit'"), R.id.passWord, "field 'passWordEdit'");
        View view = (View) finder.findRequiredView(source, R.id.forgetPwdText, "field " +
                "'forgetPwdText' and method 'onClick'");
        target.forgetPwdText = (TextView) finder.castView(view, R.id.forgetPwdText, "field " +
                "'forgetPwdText'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        view = (View) finder.findRequiredView(source, R.id.loginBtn, "field 'loginBtn' and method" +
                " 'onClick'");
        target.loginBtn = (Button) finder.castView(view, R.id.loginBtn, "field 'loginBtn'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        view = (View) finder.findRequiredView(source, R.id.moreLoginBtn, "field 'moreLoginText' " +
                "and method 'onClick'");
        target.moreLoginText = (TextView) finder.castView(view, R.id.moreLoginBtn, "field " +
                "'moreLoginText'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        view = (View) finder.findRequiredView(source, R.id.leyuLoginBtn, "field 'leyuLoginText' " +
                "and method 'onClick'");
        target.leyuLoginText = (TextView) finder.castView(view, R.id.leyuLoginBtn, "field " +
                "'leyuLoginText'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.dealLayout = (LinearLayout) finder.castView((View) finder.findRequiredView(source,
                R.id.dealLayout, "field 'dealLayout'"), R.id.dealLayout, "field 'dealLayout'");
        view = (View) finder.findRequiredView(source, R.id.bt_captcha, "field 'btCaptcha' and " +
                "method 'onClick'");
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
        target.changeEnvironment = (SettingItemView) finder.castView((View) finder
                .findRequiredView(source, R.id.clv_change_environment, "field " +
                        "'changeEnvironment'"), R.id.clv_change_environment, "field " +
                "'changeEnvironment'");
        ((View) finder.findRequiredView(source, R.id.WeiXinLoginBtn, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.qqLoginBtn, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.weiboLoginBtn, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.accountEdit = null;
        target.passWordEdit = null;
        target.forgetPwdText = null;
        target.loginBtn = null;
        target.moreLoginText = null;
        target.leyuLoginText = null;
        target.dealLayout = null;
        target.btCaptcha = null;
        target.tvVoiceCaptcha = null;
        target.changeEnvironment = null;
    }
}
