package com.boohee.one.ui;

import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;

public class ChangeEnvironmentActivity$$ViewInjector<T extends ChangeEnvironmentActivity>
        implements Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.tvPhone = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_phone, "field 'tvPhone'"), R.id.tv_phone, "field 'tvPhone'");
        target.tvNetState = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_net_state, "field 'tvNetState'"), R.id.tv_net_state, "field 'tvNetState'");
        target.tvIpState = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_ip_state, "field 'tvIpState'"), R.id.tv_ip_state, "field 'tvIpState'");
        target.rbQA = (RadioButton) finder.castView((View) finder.findRequiredView(source, R.id
                .rb_qa, "field 'rbQA'"), R.id.rb_qa, "field 'rbQA'");
        target.rbRC = (RadioButton) finder.castView((View) finder.findRequiredView(source, R.id
                .rb_rc, "field 'rbRC'"), R.id.rb_rc, "field 'rbRC'");
        target.rbPRO = (RadioButton) finder.castView((View) finder.findRequiredView(source, R.id
                .rb_pro, "field 'rbPRO'"), R.id.rb_pro, "field 'rbPRO'");
        target.rgEnvironment = (RadioGroup) finder.castView((View) finder.findRequiredView
                (source, R.id.rg_environment, "field 'rgEnvironment'"), R.id.rg_environment,
                "field 'rgEnvironment'");
        target.cbIPConnect = (CheckBox) finder.castView((View) finder.findRequiredView(source, R
                .id.cb_ip_connect, "field 'cbIPConnect'"), R.id.cb_ip_connect, "field " +
                "'cbIPConnect'");
        target.tvConnectIPs = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_connect_ips, "field 'tvConnectIPs'"), R.id.tv_connect_ips, "field " +
                "'tvConnectIPs'");
        target.tvDns = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_dns, "field 'tvDns'"), R.id.tv_dns, "field 'tvDns'");
    }

    public void reset(T target) {
        target.tvPhone = null;
        target.tvNetState = null;
        target.tvIpState = null;
        target.rbQA = null;
        target.rbRC = null;
        target.rbPRO = null;
        target.rgEnvironment = null;
        target.cbIPConnect = null;
        target.tvConnectIPs = null;
        target.tvDns = null;
    }
}
