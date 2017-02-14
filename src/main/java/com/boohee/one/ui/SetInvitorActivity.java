package com.boohee.one.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.boohee.api.PassportApi;
import com.boohee.database.UserPreference;
import com.boohee.main.GestureActivity;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.utils.Helper;
import com.boohee.utils.TextUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class SetInvitorActivity extends GestureActivity {
    private EditText friendPhoneNumber;
    private TextView skipTipText;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("邀请与验证");
        setContentView(R.layout.bt);
        findView();
        addListener();
    }

    private void findView() {
        this.skipTipText = (TextView) this.finder.find(R.id.skipTipText);
        this.friendPhoneNumber = (EditText) this.finder.find(R.id.friendPhoneNumber);
    }

    private void addListener() {
        this.skipTipText.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SetInvitorActivity.this.goDirect();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, R.string.x4).setShowAsAction(2);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            rightAction();
            return true;
        }
        if (item.getItemId() == 16908332) {
            leftAction();
        }
        return super.onOptionsItemSelected(item);
    }

    public void leftAction() {
        goDirect();
    }

    private void rightAction() {
        String phone = this.friendPhoneNumber.getText().toString().trim();
        if (TextUtil.isPhoneNumber(phone) && phone.length() == 11) {
            PassportApi.createReferrer(phone, this, new JsonCallback(this) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    SetInvitorActivity.this.goDirect();
                }
            });
        } else {
            Helper.showToast((CharSequence) "手机号填写有误~~");
        }
    }

    private JSONObject createParams(String phone) {
        JSONObject params = new JSONObject();
        try {
            params.put("token", UserPreference.getToken(this.ctx));
            params.put("user_key", UserPreference.getUserKey(this.ctx));
            params.put("referrer_cellphone", phone);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }

    private void goDirect() {
        Intent intent = new Intent(this.ctx, MainActivity.class);
        intent.setFlags(268435456);
        intent.setFlags(67108864);
        startActivity(intent);
        finish();
    }
}
