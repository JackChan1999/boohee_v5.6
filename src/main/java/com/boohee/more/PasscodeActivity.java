package com.boohee.more;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ViewFlipper;

import com.boohee.database.OnePreference;
import com.boohee.main.GestureActivity;
import com.boohee.myview.PasscodeView;
import com.boohee.myview.PasscodeView.OnPasswordCompleteListener;
import com.boohee.one.MyApplication;
import com.boohee.one.R;
import com.boohee.one.ui.MainActivity;
import com.boohee.utility.Const;
import com.boohee.utils.Helper;

public class PasscodeActivity extends GestureActivity {
    public static final String ACTION_PASSWORD_CLOSE = "com.boohee.one.action.PASSWORD_CLOSE";
    public static final String ACTION_PASSWORD_INPUT = "com.boohee.one.action.PASSWORD_INPUT";
    public static final String ACTION_PASSWORD_OPEN  = "com.boohee.one.action.PASSWORD_OPEN";
    static final        String TAG                   = PasscodeActivity.class.getSimpleName();
    private String       action;
    private PasscodeView passcodeView1;
    private PasscodeView passcodeView2;
    private ViewFlipper  viewFlipper;

    public void onCreate(Bundle outState) {
        super.onCreate(outState);
        setContentView(R.layout.lq);
        this.action = getIntent().getAction();
        if (ACTION_PASSWORD_INPUT.equals(this.action)) {
            setTitle(R.string.p2);
        } else if (ACTION_PASSWORD_OPEN.equals(this.action)) {
            setTitle(R.string.a4h);
        } else if (ACTION_PASSWORD_CLOSE.equals(this.action)) {
            setTitle(R.string.fu);
        }
        init();
    }

    private void init() {
        this.passcodeView1 = (PasscodeView) findViewById(R.id.passcode_view1);
        this.passcodeView2 = (PasscodeView) findViewById(R.id.passcode_view2);
        this.viewFlipper = (ViewFlipper) findViewById(R.id.view_flipper);
        this.passcodeView1.setOnPasswordCompleteListener(new OnPasswordCompleteListener() {
            public void onPasswordComplete(String password) {
                if (PasscodeActivity.ACTION_PASSWORD_OPEN.equals(PasscodeActivity.this.action)) {
                    PasscodeActivity.this.setPasswordOpen(password);
                } else if (PasscodeActivity.ACTION_PASSWORD_CLOSE.equals(PasscodeActivity.this
                        .action)) {
                    PasscodeActivity.this.setPasswordClose(password);
                } else if (PasscodeActivity.ACTION_PASSWORD_INPUT.equals(PasscodeActivity.this
                        .action)) {
                    PasscodeActivity.this.setPasswordInput(password);
                }
            }
        });
    }

    private void setPasswordOpen(String password) {
        this.viewFlipper.setInAnimation(this.ctx, R.anim.a_);
        this.viewFlipper.setOutAnimation(this.ctx, R.anim.aa);
        this.viewFlipper.showNext();
        this.passcodeView2.setPasscodeTitle(R.string.yt);
        this.passcodeView2.setOnPasswordCompleteListener(new OnPasswordCompleteListener() {
            public void onPasswordComplete(String password) {
                if (password.equals(PasscodeActivity.this.passcodeView1.getPassword())) {
                    PasscodeActivity.this.savePwd(password);
                    Helper.showToast(PasscodeActivity.this.ctx, (int) R.string.yy);
                    PasscodeActivity.this.finish();
                    return;
                }
                PasscodeActivity.this.passcodeView2.emptyInput();
                Helper.showToast(PasscodeActivity.this.ctx, (int) R.string.yw);
            }
        });
    }

    private void setPasswordClose(String password) {
        if (getPwd().equals(password)) {
            removePwd();
            Helper.showToast(this.ctx, (int) R.string.yu);
            finish();
            return;
        }
        this.passcodeView1.emptyInput();
        Helper.showToast(this.ctx, (int) R.string.yv);
    }

    private void setPasswordInput(String password) {
        if (getPwd().equals(password)) {
            startActivity(new Intent(this.ctx, MainActivity.class));
            finish();
            return;
        }
        this.passcodeView1.emptyInput();
        Helper.showToast(this.ctx, (int) R.string.yv);
    }

    public void savePwd(String pwd) {
        new OnePreference(this.ctx).putString(Const.PASSWORD, pwd);
    }

    private String getPwd() {
        return new OnePreference(this.ctx).getString(Const.PASSWORD);
    }

    private void removePwd() {
        new OnePreference(this.ctx).remove(Const.PASSWORD);
    }

    protected void onDestroy() {
        super.onDestroy();
        String message = getIntent().getStringExtra(Const.NOTICE_MESSAGE);
        int noticeId = getIntent().getIntExtra(Const.NOTICE_ID, 0);
        if (message != null && ACTION_PASSWORD_INPUT.equals(this.action)) {
            Intent intent = new Intent(this.ctx, ViewTipActivity.class);
            intent.putExtra(Const.NOTICE_ID, noticeId);
            intent.putExtra(Const.NOTICE_MESSAGE, message);
            startActivity(intent);
        }
    }

    public void onBackPressed() {
        if (MyApplication.getIsMainOpened() || TextUtils.isEmpty(getPwd())) {
            super.onBackPressed();
        } else {
            finish();
        }
    }
}
