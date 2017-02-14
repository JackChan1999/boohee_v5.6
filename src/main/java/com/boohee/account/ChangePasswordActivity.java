package com.boohee.account;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.boohee.api.PassportApi;
import com.boohee.database.UserPreference;
import com.boohee.main.GestureActivity;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.utils.Helper;

import org.json.JSONObject;

public class ChangePasswordActivity extends GestureActivity {
    static final String TAG = ChangePasswordActivity.class.getSimpleName();
    private EditText     newPwdEdit;
    private EditText     oldPwdEdit;
    private LinearLayout resetPwdLayout;
    private EditText     setPwdEdit;
    private int          user_type;

    public void onCreate(Bundle outState) {
        super.onCreate(outState);
        setTitle("修改密码");
        setContentView(R.layout.ed);
        this.resetPwdLayout = (LinearLayout) findViewById(R.id.reset_pwd_layout);
        this.oldPwdEdit = (EditText) findViewById(R.id.old_pwd);
        this.newPwdEdit = (EditText) findViewById(R.id.new_pwd);
        this.setPwdEdit = (EditText) findViewById(R.id.set_pwd);
        this.user_type = UserPreference.getInstance(this.ctx).getInt("user_type");
        Helper.showLog(TAG, "user_type:" + this.user_type);
        if (this.user_type == 0) {
            this.setPwdEdit.setVisibility(8);
        } else {
            this.resetPwdLayout.setVisibility(8);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, "完成").setShowAsAction(2);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 1) {
            return super.onOptionsItemSelected(item);
        }
        onComplete();
        return true;
    }

    public void onComplete() {
        switch (this.user_type) {
            case 0:
                String oldPassword = this.oldPwdEdit.getText().toString().trim();
                String newPassword = this.newPwdEdit.getText().toString().trim();
                if ("".equals(oldPassword) || "".equals(newPassword)) {
                    Helper.showToast(this.ctx, (int) R.string.ex);
                    return;
                } else {
                    changePwd(oldPassword, newPassword);
                    return;
                }
            default:
                String passowrd = this.setPwdEdit.getText().toString().trim();
                if ("".equals(passowrd)) {
                    Helper.showToast(this.ctx, (int) R.string.ex);
                    return;
                } else if (passowrd.length() < 6 || passowrd.length() > 32) {
                    Helper.showToast(this.ctx, (CharSequence) "密码必须为6-32位哦~");
                    return;
                } else {
                    setPassword(passowrd);
                    return;
                }
        }
    }

    private void changePwd(String oldPwd, String newPwd) {
        PassportApi.changePassword(oldPwd, newPwd, this, new JsonCallback(this) {
            public void ok(JSONObject object) {
                super.ok(object);
                Helper.showToast((CharSequence) "修改密码成功");
                ChangePasswordActivity.this.finish();
            }

            public void onFinish() {
                super.onFinish();
                ChangePasswordActivity.this.dismissLoading();
            }
        });
    }

    private void setPassword(String password) {
        PassportApi.setPassword(password, this, new JsonCallback(this) {
            public void ok(JSONObject object) {
                super.ok(object);
                UserPreference userPrefs = UserPreference.getInstance(ChangePasswordActivity.this
                        .ctx);
                userPrefs.putInt("user_type", 0);
                userPrefs.remove("identity");
                Helper.showToast((CharSequence) "设置密码成功");
                ChangePasswordActivity.this.finish();
            }

            public void onFinish() {
                super.onFinish();
                ChangePasswordActivity.this.dismissLoading();
            }
        });
    }
}
