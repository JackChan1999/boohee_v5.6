package com.boohee.one.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.api.PassportApi;
import com.boohee.main.GestureActivity;
import com.boohee.model.User;
import com.boohee.one.MyApplication;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.utils.AccountUtils;
import com.boohee.utils.Helper;
import com.boohee.utils.TextUtil;

import org.json.JSONObject;

public class CompleteRegisterActivity extends GestureActivity {
    public static final String CELLPHONE = "CELLPHONE";
    public static final String TOKEN     = "TOKEN";
    private String cellPhone;
    @InjectView(2131427561)
    CheckBox checkBox;
    @InjectView(2131427560)
    EditText etName;
    @InjectView(2131427538)
    EditText etPassWord;
    @InjectView(2131427563)
    Button   loginBtn;
    private User   mUser;
    private String token;
    @InjectView(2131427562)
    TextView userDeal;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.a23);
        setContentView(R.layout.au);
        ButterKnife.inject((Activity) this);
        handleIntent();
        addListener();
    }

    private void handleIntent() {
        this.cellPhone = getIntent().getStringExtra(CELLPHONE);
        this.token = getIntent().getStringExtra(TOKEN);
    }

    private void addListener() {
        this.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CompleteRegisterActivity.this.loginBtn.setEnabled(isChecked);
            }
        });
    }

    @OnClick({2131427562, 2131427563})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.userDeal:
                BrowserActivity.comeOnBaby(this.activity, getResources().getString(R.string.abq),
                        "http://shop.boohee.com/store/pages/boohee_privacy");
                return;
            case R.id.loginBtn:
                register();
                return;
            default:
                return;
        }
    }

    private void register() {
        if (TextUtil.isEmpty(this.etName.getText().toString().trim())) {
            Helper.showToast((int) R.string.abs);
            return;
        }
        String passWord = this.etPassWord.getText().toString().trim();
        if (TextUtils.isEmpty(passWord)) {
            Helper.showToast((int) R.string.ys);
            return;
        }
        showLoading();
        this.loginBtn.setEnabled(false);
        PassportApi.register(this.activity, userName, passWord, this.cellPhone, this.token, new
                JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                CompleteRegisterActivity.this.mUser = User.parsePassportUser(object);
                AccountUtils.saveTokenAndUserKey(MyApplication.getContext(),
                        CompleteRegisterActivity.this.mUser);
                AccountUtils.setUserTypeBoohee(MyApplication.getContext());
                AccountUtils.login(CompleteRegisterActivity.this.activity);
            }

            public void onFinish() {
                super.onFinish();
                CompleteRegisterActivity.this.dismissLoading();
                CompleteRegisterActivity.this.loginBtn.setEnabled(true);
            }
        });
    }

    public static void comeOnBaby(Context context, String cellphone, String token) {
        if (context != null && cellphone != null && token != null) {
            Intent intent = new Intent(context, CompleteRegisterActivity.class);
            intent.putExtra(CELLPHONE, cellphone);
            intent.putExtra(TOKEN, token);
            context.startActivity(intent);
        }
    }
}
