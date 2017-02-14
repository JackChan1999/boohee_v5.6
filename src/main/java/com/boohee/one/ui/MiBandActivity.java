package com.boohee.one.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.main.GestureActivity;
import com.boohee.one.R;
import com.boohee.utils.Helper;
import com.boohee.utils.MiBandHelper;
import com.xiaomi.account.openauth.AuthorizeActivity;

public class MiBandActivity extends GestureActivity {
    private static final int MI_REQ_CODE = 2333;
    @InjectView(2131427775)
    Button       viewBand;
    @InjectView(2131427776)
    LinearLayout viewStatus;
    @InjectView(2131427777)
    TextView     viewUnband;

    @OnClick({2131427775, 2131427777})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_band:
                MiBandHelper.startAuthenticate(this, null, MI_REQ_CODE);
                return;
            case R.id.view_unband:
                unBindingMiBand();
                return;
            default:
                return;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c2);
        ButterKnife.inject((Activity) this);
        miBandStatus();
    }

    private void miBandStatus() {
    }

    private void bindingMiBand() {
    }

    private void unBindingMiBand() {
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == MI_REQ_CODE) {
            Bundle bundle = data.getExtras();
            if (AuthorizeActivity.RESULT_SUCCESS == resultCode) {
                String accessToken = bundle.getString("access_token");
                bindingMiBand();
            } else if (AuthorizeActivity.RESULT_FAIL == resultCode) {
                CharSequence errorDescription = bundle.getString("error_description");
                if (TextUtils.isEmpty(errorDescription)) {
                    Helper.showToast((CharSequence) "授权失败，请重试~~");
                } else {
                    Helper.showToast(errorDescription);
                }
            }
        }
    }

    public static void comeOnBaby(Context context) {
        if (context != null) {
            context.startActivity(new Intent(context, MiBandActivity.class));
        }
    }
}
