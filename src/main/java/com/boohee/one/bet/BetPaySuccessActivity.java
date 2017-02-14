package com.boohee.one.bet;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.database.UserPreference;
import com.boohee.model.User;
import com.boohee.modeldao.UserDao;
import com.boohee.one.R;
import com.boohee.one.ui.BaseActivity;
import com.boohee.one.ui.WebViewPicUploadActivity;
import com.boohee.utils.WheelUtils;

import de.hdodenhof.circleimageview.CircleImageView;

public class BetPaySuccessActivity extends BaseActivity {
    @InjectView(2131427501)
    TextView        btnUploadPic;
    @InjectView(2131427499)
    CircleImageView ivAvatar;
    private int orderId = -1;
    @InjectView(2131427500)
    TextView tvUserName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aj);
        ButterKnife.inject((Activity) this);
        if (getIntent() != null) {
            this.orderId = getIntent().getIntExtra("orderId", -1);
        }
        User user = new UserDao(this.ctx).queryWithUserKey(UserPreference.getUserKey(this.ctx));
        if (!TextUtils.isEmpty(user.user_name)) {
            this.tvUserName.setText(user.user_name);
        }
        this.imageLoader.displayImage(user.avatar_url, this.ivAvatar);
    }

    @OnClick({2131427501})
    public void onClick(View view) {
        if (!WheelUtils.isFastDoubleClick()) {
            switch (view.getId()) {
                case R.id.btn_upload_pic:
                    if (this.orderId >= 0) {
                        WebViewPicUploadActivity.startMe(this.ctx, String.format
                                ("bet_id=0&order_id=%d&type=base_upload", new Object[]{Integer
                                        .valueOf(this.orderId)}));
                        finish();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }
}
