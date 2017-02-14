package com.boohee.one.pedometer;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.database.StepsPreference;
import com.boohee.main.GestureActivity;
import com.boohee.one.R;
import com.boohee.one.event.BandTypeEvent;
import com.boohee.one.pedometer.manager.StepManagerFactory;
import com.boohee.one.service.StepCounterService;
import com.boohee.utils.Helper;
import com.boohee.widgets.AnimCheckBox;
import com.boohee.widgets.AnimCheckBox.OnCheckedChangeListener;

import de.greenrobot.event.EventBus;

public class StepSettingActivity extends GestureActivity {
    @InjectView(2131427948)
    AnimCheckBox checkBoxNotification;
    @InjectView(2131427951)
    AnimCheckBox checkBoxNotificationPermission;
    @InjectView(2131427947)
    AnimCheckBox checkBoxStep;
    private boolean isFirst = true;
    @InjectView(2131427647)
    LinearLayout llContent;
    NotificationManager notificationManager;
    @InjectView(2131427950)
    TextView tvPermission;
    @InjectView(2131427513)
    TextView tvStep;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.df);
        ButterKnife.inject((Activity) this);
        this.notificationManager = (NotificationManager) getSystemService("notification");
        initView();
        this.isFirst = false;
    }

    private void initView() {
        this.checkBoxStep.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onChange(boolean checked) {
                if (!StepSettingActivity.this.isFirst) {
                    if (StepCounterUtil.isBindCling()) {
                        Helper.showToast((CharSequence) "请解绑手环重试");
                        return;
                    }
                    StepsPreference.putStepOpen(checked);
                    StepSettingActivity.this.toogleStep(checked);
                }
            }
        });
        this.checkBoxNotification.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onChange(boolean checked) {
                if (!StepSettingActivity.this.isFirst) {
                    if (StepCounterUtil.isBindCling()) {
                        Helper.showToast((CharSequence) "请解绑手环重试");
                        return;
                    }
                    StepsPreference.putStepNotification(checked);
                    StepSettingActivity.this.setNotification(checked);
                }
            }
        });
        this.checkBoxNotification.setChecked(StepsPreference.isStepNotificationOpen());
        this.checkBoxStep.setChecked(StepsPreference.isStepOpen());
    }

    private void setNotification(boolean isOpen) {
        Intent intent = new Intent(this.activity, StepCounterService.class);
        if (isOpen) {
            intent.setAction(StepCounterService.ACTION_OPEN_NOTIFICATION);
        } else {
            intent.setAction(StepCounterService.ACTION_CLOSE_NOTIFICATION);
        }
        startService(intent);
    }

    private void toogleStep(boolean isOpen) {
        Intent intent = new Intent(this, StepCounterService.class);
        BandTypeEvent event = new BandTypeEvent();
        if (isOpen) {
            event.bandType = StepManagerFactory.STEP_TYPE_PEDOMETER;
            this.llContent.setVisibility(0);
        } else {
            event.bandType = "default";
            this.llContent.setVisibility(8);
            if (!this.isFirst && StepCounterUtil.checkNotificationPermission(this.ctx)) {
                StepCounterUtil.goNLPermission(this.ctx);
                Helper.showToast((int) R.string.a72);
            }
        }
        EventBus.getDefault().post(event);
    }

    protected void onResume() {
        super.onResume();
        if (StepCounterUtil.checkNotificationPermission(this.ctx)) {
            this.checkBoxNotificationPermission.setChecked(true);
        } else {
            this.checkBoxNotificationPermission.setChecked(false);
        }
    }

    @OnClick({2131427949})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_notifi_permission:
                StepCounterUtil.goNLPermission(this.ctx);
                Helper.showToast((int) R.string.a75);
                return;
            default:
                return;
        }
    }
}
