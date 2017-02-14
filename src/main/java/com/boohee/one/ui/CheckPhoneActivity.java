package com.boohee.one.ui;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boohee.api.PassportApi;
import com.boohee.database.UserPreference;
import com.boohee.main.GestureActivity;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.utils.Helper;

import org.json.JSONException;
import org.json.JSONObject;

public class CheckPhoneActivity extends GestureActivity {
    public static final  int    ALREADY_CHECK     = 1;
    public static final  int    CHECK_PHONE       = 2;
    public static final  String KEY               = "KEY";
    public static final  String KEY_PHONE         = "Phone";
    public static final  int    REQ_CODE_REGISTER = 11;
    private static final int    RIGHT_ACTION      = 0;
    private EditText     accountEdit;
    private LinearLayout accountLayout;
    private LinearLayout alreadyCheckLayout;
    private Button       changePhoneBtn;
    private TextView     checkedAccountText;
    private int force = 0;
    private Button       getAuthCodeBtn;
    private Menu         mMenu;
    private View         maskView;
    private EditText     passWordEdit;
    private LinearLayout passWordLayout;
    private TextView     skipTipText;
    private TimeThread   timeThread;
    private TextView     tipText;

    private class TimeThread extends Thread {
        private volatile int time = 0;

        public TimeThread(int seconds) {
            this.time = seconds;
        }

        public void run() {
            while (true) {
                int i = this.time;
                this.time = i - 1;
                if (i > 0) {
                    setTime(this.time);
                    CheckPhoneActivity.this.delay(1000);
                } else {
                    revertUI();
                    return;
                }
            }
        }

        private void revertUI() {
            if (CheckPhoneActivity.this.activity != null) {
                CheckPhoneActivity.this.activity.runOnUiThread(new Runnable() {
                    public void run() {
                        CheckPhoneActivity.this.maskView.setVisibility(8);
                        CheckPhoneActivity.this.getAuthCodeBtn.setEnabled(true);
                        CheckPhoneActivity.this.getAuthCodeBtn.setText("获取验证码");
                        TimeThread.this.time = 0;
                        CheckPhoneActivity.this.timeThread = null;
                    }
                });
            }
        }

        public void stopTimer() {
            this.time = 0;
            interrupt();
        }

        private void setTime(final int time) {
            if (CheckPhoneActivity.this.activity != null) {
                CheckPhoneActivity.this.activity.runOnUiThread(new Runnable() {
                    public void run() {
                        CheckPhoneActivity.this.getAuthCodeBtn.setText(time + "秒后重新获取");
                    }
                });
            }
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("手机号验证");
        setContentView(R.layout.aq);
        findView();
        addListener();
        init();
    }

    private void findView() {
        this.accountEdit = (EditText) this.finder.find(R.id.account);
        this.passWordEdit = (EditText) this.finder.find(R.id.passWord);
        this.getAuthCodeBtn = (Button) this.finder.find(R.id.getAuthCodeBtn);
        this.changePhoneBtn = (Button) this.finder.find(R.id.changePhoneBtn);
        this.maskView = this.finder.find(R.id.maskView);
        this.skipTipText = (TextView) this.finder.find(R.id.skipTipText);
        this.tipText = (TextView) this.finder.find(R.id.tipText);
        this.checkedAccountText = (TextView) this.finder.find(R.id.checkedAccountText);
        this.accountLayout = (LinearLayout) this.finder.find(R.id.accountLayout);
        this.passWordLayout = (LinearLayout) this.finder.find(R.id.passWordLayout);
        this.alreadyCheckLayout = (LinearLayout) this.finder.find(R.id.alreadyCheckLayout);
    }

    private void addListener() {
        this.getAuthCodeBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                CheckPhoneActivity.this.getCode();
            }
        });
        this.changePhoneBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                CheckPhoneActivity.this.accountLayout.setVisibility(0);
                CheckPhoneActivity.this.passWordLayout.setVisibility(0);
                CheckPhoneActivity.this.tipText.setVisibility(0);
                CheckPhoneActivity.this.skipTipText.setVisibility(0);
                CheckPhoneActivity.this.alreadyCheckLayout.setVisibility(8);
                if (CheckPhoneActivity.this.mMenu != null) {
                    CheckPhoneActivity.this.mMenu.getItem(0).setEnabled(true);
                    CheckPhoneActivity.this.mMenu.getItem(0).setTitle("确定");
                }
            }
        });
        this.skipTipText.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                CheckPhoneActivity.this.setResult(-1);
                CheckPhoneActivity.this.finish();
            }
        });
    }

    private void init() {
        String phone = getIntent().getStringExtra(KEY_PHONE);
        if (!TextUtils.isEmpty(phone)) {
            this.accountEdit.append(phone.substring(0, 11));
            this.checkedAccountText.setText(phone);
        }
    }

    private void setUpView() {
        switch (getIntent().getIntExtra(KEY, 2)) {
            case 1:
                this.accountLayout.setVisibility(8);
                this.passWordLayout.setVisibility(8);
                this.tipText.setVisibility(8);
                this.skipTipText.setVisibility(8);
                this.mMenu.getItem(0).setEnabled(false);
                this.mMenu.getItem(0).setTitle("");
                this.alreadyCheckLayout.setVisibility(0);
                return;
            case 2:
                this.accountLayout.setVisibility(0);
                this.passWordLayout.setVisibility(0);
                this.tipText.setVisibility(0);
                this.skipTipText.setVisibility(0);
                this.mMenu.getItem(0).setEnabled(true);
                this.mMenu.getItem(0).setTitle("确定");
                this.alreadyCheckLayout.setVisibility(8);
                return;
            default:
                return;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        this.mMenu = menu;
        menu.add(0, 0, 0, "确认").setShowAsAction(2);
        setUpView();
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 16908332) {
            leftAction();
            return true;
        } else if (item.getItemId() != 0) {
            return super.onOptionsItemSelected(item);
        } else {
            rightAction();
            return true;
        }
    }

    private void leftAction() {
        setResult(-1);
        finish();
    }

    private void rightAction() {
        String cellPhone = this.accountEdit.getText().toString().trim();
        if (TextUtils.isEmpty(cellPhone) || cellPhone.length() != 11) {
            Helper.showToast(this.activity, (CharSequence) "请输入正确的号码~~");
            return;
        }
        String code = this.passWordEdit.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            Helper.showToast(this.activity, (CharSequence) "请输入正确的验证码~~");
            return;
        }
        showLoading();
        PassportApi.verifyCellphone(cellPhone, code, this.force, this, new JsonCallback(this) {
            public void ok(JSONObject object) {
                super.ok(object);
                Helper.showToast((CharSequence) "验证成功！");
                CheckPhoneActivity.this.stopTimer();
                CheckPhoneActivity.this.setResult(-1);
                CheckPhoneActivity.this.finish();
            }

            public void onFinish() {
                super.onFinish();
                CheckPhoneActivity.this.dismissLoading();
            }
        });
    }

    private void getCode() {
        String cellPhone = this.accountEdit.getText().toString().trim();
        if (TextUtils.isEmpty(cellPhone) || cellPhone.length() != 11) {
            Helper.showToast((CharSequence) "请输入正确的号码~~");
            return;
        }
        startTimer(180);
        PassportApi.sendCellphoneVerification(cellPhone, this.force, this, new JsonCallback(this) {
            public void ok(JSONObject object, boolean hasError) {
                Helper.showToast((CharSequence) "验证码获取成功，请稍后！");
            }

            public void fail(String message, boolean hasError, int errorCode) {
                if (errorCode == 107) {
                    CheckPhoneActivity.this.showAlertDialog();
                    CheckPhoneActivity.this.stopTimer();
                    return;
                }
                CheckPhoneActivity.this.stopTimer();
            }
        });
    }

    private JSONObject createJSONParams(String cellPhone, String code) {
        JSONObject param = new JSONObject();
        try {
            param.put("cellphone", cellPhone);
            param.put("force", this.force);
            param.put("code", code);
            param.put("token", UserPreference.getToken(this.ctx));
            param.put("user_key", UserPreference.getUserKey(this.ctx));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return param;
    }

    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
    }

    private void startTimer(int seconds) {
        if (this.timeThread == null) {
            synchronized (this) {
                if (this.timeThread == null) {
                    this.timeThread = createTimeThread(seconds);
                }
            }
        }
        this.timeThread.start();
        this.maskView.setVisibility(0);
        this.getAuthCodeBtn.setEnabled(false);
    }

    private void stopTimer() {
        if (this.timeThread != null) {
            synchronized (this) {
                if (this.timeThread != null) {
                    this.timeThread.stopTimer();
                }
                this.timeThread = null;
            }
        }
        this.getAuthCodeBtn.setText("获取验证码");
        this.getAuthCodeBtn.setEnabled(true);
        this.maskView.setVisibility(8);
    }

    private TimeThread createTimeThread(int seconds) {
        return new TimeThread(seconds);
    }

    private void delay(int millseconds) {
        try {
            Thread.sleep((long) millseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void showAlertDialog() {
        Builder builder = new Builder(this.ctx);
        builder.setTitle("提示").setMessage("此手机号已与其他账号绑定，是否继续？");
        builder.setCancelable(false);
        builder.setPositiveButton("继续", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                CheckPhoneActivity.this.force = 1;
                CheckPhoneActivity.this.getCode();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
