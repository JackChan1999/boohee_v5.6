package com.boohee.one.pedometer;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;
import com.boohee.widgets.AnimCheckBox;

public class StepSettingActivity$$ViewInjector<T extends StepSettingActivity> implements
        Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.tvStep = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_step, "field 'tvStep'"), R.id.tv_step, "field 'tvStep'");
        target.checkBoxStep = (AnimCheckBox) finder.castView((View) finder.findRequiredView
                (source, R.id.check_box_step, "field 'checkBoxStep'"), R.id.check_box_step,
                "field 'checkBoxStep'");
        target.checkBoxNotification = (AnimCheckBox) finder.castView((View) finder
                .findRequiredView(source, R.id.check_box_notification, "field " +
                        "'checkBoxNotification'"), R.id.check_box_notification, "field " +
                "'checkBoxNotification'");
        target.tvPermission = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_permission, "field 'tvPermission'"), R.id.tv_permission, "field " +
                "'tvPermission'");
        target.llContent = (LinearLayout) finder.castView((View) finder.findRequiredView(source,
                R.id.ll_content, "field 'llContent'"), R.id.ll_content, "field 'llContent'");
        target.checkBoxNotificationPermission = (AnimCheckBox) finder.castView((View) finder
                .findRequiredView(source, R.id.check_box_notification_permission, "field " +
                        "'checkBoxNotificationPermission'"), R.id
                .check_box_notification_permission, "field 'checkBoxNotificationPermission'");
        ((View) finder.findRequiredView(source, R.id.ll_notifi_permission, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.tvStep = null;
        target.checkBoxStep = null;
        target.checkBoxNotification = null;
        target.tvPermission = null;
        target.llContent = null;
        target.checkBoxNotificationPermission = null;
    }
}
