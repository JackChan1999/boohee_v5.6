package com.boohee.one.pedometer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.api.RecordApi;
import com.boohee.main.GestureActivity;
import com.boohee.model.BindBand;
import com.boohee.one.R;
import com.boohee.one.event.BandTypeEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.JsonParams;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.one.pedometer.manager.StepManagerFactory;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.Helper;

import de.greenrobot.event.EventBus;

import java.util.List;

import org.json.JSONObject;

public class BindClingActivity extends GestureActivity {
    public static final int REQUEST_CLING = 18;
    private String account;
    private int bandId = -1;
    private List<BindBand> bandList;
    @InjectView(2131427503)
    TextView btnBind;
    @InjectView(2131427479)
    View     divider;
    @InjectView(2131427502)
    EditText editCling;

    public static void startActivity(Activity context) {
        context.startActivityForResult(new Intent(context, BindClingActivity.class), 18);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ak);
        ButterKnife.inject((Activity) this);
        getBindData();
        showLoading();
    }

    private void getBindData() {
        BooheeClient.build("record").get(RecordApi.URL_BINDS, new JsonCallback(this.ctx) {
            public void onFinish() {
                super.onFinish();
                BindClingActivity.this.dismissLoading();
            }

            public void ok(JSONObject object) {
                if (object != null) {
                    try {
                        BindClingActivity.this.bandList = FastJsonUtils.parseList(object
                                .getString("bands"), BindBand.class);
                        for (BindBand band : BindClingActivity.this.bandList) {
                            if (TextUtils.equals(band.provider, StepManagerFactory
                                    .STEP_TYPE_CLING) && band.is_bind) {
                                BindClingActivity.this.bandId = band.id;
                            }
                        }
                        BindClingActivity.this.initView();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, this.ctx);
    }

    private void initView() {
        if (this.bandId < 0) {
            this.editCling.setVisibility(0);
            this.divider.setVisibility(0);
            this.btnBind.setText("绑定");
            return;
        }
        this.editCling.setVisibility(8);
        this.divider.setVisibility(8);
        this.btnBind.setText("解除绑定");
    }

    @OnClick({2131427503})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_bind:
                if (this.bandId < 0) {
                    this.account = this.editCling.getText().toString().trim();
                    if (!TextUtils.isEmpty(this.account)) {
                        bindCling();
                        return;
                    }
                    return;
                }
                unBindCling();
                return;
            default:
                return;
        }
    }

    private void bindCling() {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("band_account", this.account);
        jsonParams.put("provider", StepManagerFactory.STEP_TYPE_CLING);
        BooheeClient.build("record").post(RecordApi.URL_BINDS, jsonParams, new JsonCallback(this
                .ctx) {
            public void ok(JSONObject response) {
                BindClingActivity.this.bindClingSuccess();
                BindClingActivity.this.setResult(-1);
                BindClingActivity.this.finish();
            }
        }, this.ctx);
    }

    private void unBindCling() {
        BooheeClient.build("record").delete("/api/v2/user_bands/" + this.bandId, null, new
                JsonCallback(this.ctx) {
            public void ok(String response) {
                BindClingActivity.this.unBindClingSuccess();
                BindClingActivity.this.setResult(-1);
                BindClingActivity.this.finish();
            }
        }, this.ctx);
    }

    private void bindClingSuccess() {
        BandTypeEvent event = new BandTypeEvent();
        event.bandType = StepManagerFactory.STEP_TYPE_CLING;
        EventBus.getDefault().post(event);
    }

    private void unBindClingSuccess() {
        Helper.showToast((CharSequence) "解绑成功");
        BandTypeEvent event = new BandTypeEvent();
        event.bandType = StepManagerFactory.STEP_TYPE_PEDOMETER;
        EventBus.getDefault().post(event);
    }
}
