package com.boohee.food;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.main.GestureActivity;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.JsonParams;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.utils.Helper;
import com.boohee.utils.Utils;

import org.json.JSONObject;

public class FoodErrorActivity extends GestureActivity {
    @InjectView(2131427679)
    LinearLayout activityFoodError;
    @InjectView(2131427681)
    TextView     btn;
    private String code;
    @InjectView(2131427680)
    EditText edit;

    public static void comeOn(Context context, String code) {
        Intent intent = new Intent(context, FoodErrorActivity.class);
        intent.putExtra("code", code);
        context.startActivity(intent);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ba);
        ButterKnife.inject((Activity) this);
        this.code = getIntent().getStringExtra("code");
        this.btn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String str = FoodErrorActivity.this.edit.getText().toString().trim();
                if (TextUtils.isEmpty(str)) {
                    Helper.showToast((CharSequence) "请输入要反馈的问题");
                    return;
                }
                JsonParams params = new JsonParams();
                params.put(Utils.RESPONSE_CONTENT, str);
                BooheeClient.build(BooheeClient.FOOD).post(String.format
                        ("/fb/v1/foods/%s/revision", new Object[]{FoodErrorActivity.this.code}),
                        params, new JsonCallback(FoodErrorActivity.this.ctx) {
                    public void ok(JSONObject object) {
                        super.ok(object);
                        FoodErrorActivity.this.finish();
                    }
                }, FoodErrorActivity.this.ctx);
            }
        });
    }
}
