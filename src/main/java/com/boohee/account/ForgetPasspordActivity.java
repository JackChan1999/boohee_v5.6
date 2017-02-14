package com.boohee.account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.api.PassportApi;
import com.boohee.main.GestureActivity;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.utils.Helper;
import com.boohee.utils.Keyboard;
import com.boohee.utils.TextUtil;

import org.json.JSONObject;

public class ForgetPasspordActivity extends GestureActivity {
    static final         String TAG         = ForgetPasspordActivity.class.getName();
    private static final int    UPDATE_TIME = 272;
    @InjectView(2131427794)
    EditText accountEdit;
    private String accountStr;
    @InjectView(2131427795)
    Button btCaptcha;
    private String  code;
    private Captcha mCaptcha;
    private Handler mCheckHandler;
    private int mNumber = 60;
    private String newPassword;
    @InjectView(2131428149)
    EditText tvCaptcha;
    @InjectView(2131428148)
    EditText tvPassword;
    @InjectView(2131427797)
    TextView tvVoiceCaptcha;

    enum Captcha {
        sms,
        voice
    }

    class CheckTimeHandler extends Handler {
        CheckTimeHandler() {
        }

        public void handleMessage(Message msg) {
            ForgetPasspordActivity.this.mNumber = ForgetPasspordActivity.this.mNumber - 1;
            ForgetPasspordActivity.this.refreshTimeView();
        }
    }

    public void onCreate(Bundle outState) {
        super.onCreate(outState);
        setContentView(R.layout.f8);
        ButterKnife.inject((Activity) this);
        setTitle(R.string.mq);
        this.mCheckHandler = new CheckTimeHandler();
    }

    @OnClick({2131427795, 2131427797})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_captcha:
                this.mCaptcha = Captcha.sms;
                requestCaptcha();
                return;
            case R.id.tv_voice_captcha:
                this.mCaptcha = Captcha.voice;
                requestCaptcha();
                return;
            default:
                return;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, R.string.a2e).setShowAsAction(2);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            corfirmResetPassWord();
        }
        return super.onOptionsItemSelected(item);
    }

    private void requestCaptcha() {
        this.accountStr = this.accountEdit.getText().toString().trim();
        if (TextUtil.isNull(this.accountStr) || !TextUtil.isPhoneNumber(this.accountStr)) {
            Helper.showLong((CharSequence) "请正确填写手机号码");
            return;
        }
        showLoading();
        runTimer();
        PassportApi.forgetPasswordByPhone(this.accountStr, this.mCaptcha.name(), this, new
                JsonCallback(this) {
            public void ok(JSONObject object) {
                super.ok(object);
                Helper.showToast((CharSequence) "验证码已发送，请注意查收");
            }

            public void onFinish() {
                super.onFinish();
                ForgetPasspordActivity.this.dismissLoading();
            }
        });
    }

    private void corfirmResetPassWord() {
        this.code = this.tvCaptcha.getText().toString().trim();
        this.newPassword = this.tvPassword.getText().toString().trim();
        if (TextUtil.isNull(this.code)) {
            Helper.showToast((CharSequence) "请输入验证码!");
            return;
        }
        if (TextUtil.isNull(this.newPassword)) {
            Helper.showToast((CharSequence) "请输入密码!");
        }
        if (this.newPassword.length() < 6 || this.newPassword.length() > 32) {
            Helper.showToast((CharSequence) "密码必须为6-32位哦~");
        } else {
            resetPassword();
        }
    }

    private void resetPassword() {
        showLoading();
        PassportApi.finishResetPassword(this.newPassword, this.code, this, new JsonCallback(this) {
            public void ok(JSONObject object) {
                super.ok(object);
                Helper.showToast((CharSequence) "已经成功重置密码,请登录");
                Keyboard.closeAll(ForgetPasspordActivity.this.ctx);
                ForgetPasspordActivity.this.finish();
            }

            public void onFinish() {
                super.onFinish();
                ForgetPasspordActivity.this.dismissLoading();
            }
        });
    }

    private void refreshTimeView() {
        if (this.mNumber <= 0) {
            this.btCaptcha.setText(R.string.u2);
            this.btCaptcha.setEnabled(true);
            this.tvVoiceCaptcha.setText(R.string.u5);
            this.tvVoiceCaptcha.setEnabled(true);
            this.mCheckHandler.removeMessages(UPDATE_TIME);
            return;
        }
        this.btCaptcha.setTextColor(getResources().getColor(R.color.ju));
        this.btCaptcha.setText(String.valueOf(this.mNumber));
        this.tvVoiceCaptcha.setEnabled(false);
        this.mCheckHandler.sendEmptyMessageDelayed(UPDATE_TIME, 1000);
    }

    private void runTimer() {
        this.mNumber = 60;
        this.btCaptcha.setTextColor(getResources().getColor(R.color.ju));
        this.btCaptcha.setText(String.valueOf(this.mNumber));
        this.btCaptcha.setEnabled(false);
        this.mCheckHandler.sendEmptyMessageDelayed(UPDATE_TIME, 1000);
    }

    public void onBackPressed() {
        Keyboard.closeAll(this.ctx);
        finish();
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.mCheckHandler != null) {
            this.mCheckHandler.removeMessages(UPDATE_TIME);
        }
    }

    public static void comeOnBaby(Context context) {
        if (context != null) {
            context.startActivity(new Intent(context, ForgetPasspordActivity.class));
        }
    }
}
