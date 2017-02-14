package com.boohee.one.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog.Builder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.api.RecordApi;
import com.boohee.database.OnePreference;
import com.boohee.main.GestureActivity;
import com.boohee.model.BindingDevice;
import com.boohee.model.WeightScale;
import com.boohee.one.R;
import com.boohee.one.event.BandTypeEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.pedometer.manager.StepManagerFactory;
import com.boohee.utils.BleUtil;
import com.boohee.utils.FastJsonUtils;

import de.greenrobot.event.EventBus;

import java.util.List;

import org.json.JSONObject;

public class HardwareActivity extends GestureActivity {
    private static final String CLING = "cling";
    private static final String SCALE = "yolanda";
    private List<BindingDevice> binds;
    private BindingDevice       clingBind;
    @InjectView(2131427703)
    ImageView      ivCling;
    @InjectView(2131427699)
    ImageView      ivScale;
    @InjectView(2131427698)
    RelativeLayout rlScale;
    private BindingDevice scaleBind;
    @InjectView(2131427706)
    TextView tvClingGo;
    @InjectView(2131427704)
    TextView tvClingName;
    @InjectView(2131427705)
    TextView tvClingStatus;
    @InjectView(2131427702)
    TextView tvScaleGo;
    @InjectView(2131427700)
    TextView tvScaleName;
    @InjectView(2131427701)
    TextView tvScaleStatus;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, HardwareActivity.class));
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bg);
        ButterKnife.inject((Activity) this);
        refreshWeightScale();
        loadBinds();
    }

    private void loadBinds() {
        showLoading();
        RecordApi.getBands(this, new JsonCallback(this) {
            public void ok(JSONObject object) {
                super.ok(object);
                HardwareActivity.this.binds = FastJsonUtils.parseList(object.optString("bands"),
                        BindingDevice.class);
                HardwareActivity.this.refreshView(HardwareActivity.this.binds);
            }

            public void onFinish() {
                super.onFinish();
                HardwareActivity.this.dismissLoading();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            refreshWeightScale();
            loadBinds();
        }
    }

    private void refreshView(List<BindingDevice> bindingDevices) {
        if (bindingDevices != null) {
            for (BindingDevice bindingDevice : bindingDevices) {
                if ("cling".equals(bindingDevice.provider)) {
                    this.clingBind = bindingDevice;
                    refreshClingView(bindingDevice);
                } else if (SCALE.equals(bindingDevice.provider)) {
                    this.scaleBind = bindingDevice;
                }
            }
            refreshWeightScale();
        }
    }

    private void refreshWeightScale() {
        if (BleUtil.hasBleFeature(this)) {
            WeightScale scale = OnePreference.getWeightScale();
            if (scale == null) {
                this.tvScaleName.setText("云康宝系列体脂称");
                this.tvScaleGo.setText("去查看");
                this.tvScaleStatus.setText("未连接");
                this.tvScaleGo.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        if (HardwareActivity.this.scaleBind != null) {
                            HardwareIntroActivity.startActivity(HardwareActivity.this,
                                    HardwareActivity.this.scaleBind.web_url, HardwareActivity
                                            .this.scaleBind.buy_url, 0);
                        }
                    }
                });
                return;
            }
            this.tvScaleName.setText(scale.showName());
            this.tvScaleGo.setText("解绑");
            this.tvScaleStatus.setText("已连接");
            this.tvScaleGo.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    HardwareActivity.this.showUnbindScaleDialog();
                }
            });
            return;
        }
        this.rlScale.setVisibility(8);
    }

    private void showUnbindScaleDialog() {
        new Builder(this).setMessage((CharSequence) "确定解绑体重秤？").setPositiveButton((CharSequence)
                "确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                OnePreference.clearWeightScale();
                HardwareActivity.this.refreshWeightScale();
            }
        }).setNegativeButton((CharSequence) "取消", null).show();
    }

    private void refreshClingView(BindingDevice bindingDevice) {
        if (bindingDevice != null) {
            if (bindingDevice.is_bind) {
                this.tvClingStatus.setText("已连接");
                this.tvClingGo.setText("解绑");
                this.tvClingGo.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        HardwareActivity.this.showUnbindClingDialog();
                    }
                });
                return;
            }
            this.tvClingStatus.setText("未连接");
            this.tvClingGo.setText("去查看");
            this.tvClingGo.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (HardwareActivity.this.clingBind != null) {
                        HardwareIntroActivity.startActivity(HardwareActivity.this,
                                HardwareActivity.this.clingBind.web_url, HardwareActivity.this
                                        .clingBind.buy_url, 1);
                    }
                }
            });
        }
    }

    private void showUnbindClingDialog() {
        new Builder(this).setMessage((CharSequence) "确定解绑Cling手表/手环").setPositiveButton(
                (CharSequence) "确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                HardwareActivity.this.unbindCling();
            }
        }).setNegativeButton((CharSequence) "取消", null).show();
    }

    private void unbindCling() {
        showLoading();
        RecordApi.unbindBand(this, this.clingBind.id, "cling", new JsonCallback(this) {
            public void ok(String response) {
                super.ok(response);
                if (HardwareActivity.this.clingBind != null) {
                    HardwareActivity.this.clingBind.is_bind = false;
                    HardwareActivity.this.refreshClingView(HardwareActivity.this.clingBind);
                    HardwareActivity.this.unBindClingSuccess();
                }
            }

            public void onFinish() {
                super.onFinish();
                HardwareActivity.this.dismissLoading();
            }
        });
    }

    private void unBindClingSuccess() {
        BandTypeEvent event = new BandTypeEvent();
        event.bandType = StepManagerFactory.STEP_TYPE_PEDOMETER;
        EventBus.getDefault().post(event);
    }
}
