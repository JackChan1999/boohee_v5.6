package com.boohee.food;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.api.FoodApi;
import com.boohee.api.RecordApi;
import com.boohee.database.UserPreference;
import com.boohee.main.GestureActivity;
import com.boohee.model.CustomSport;
import com.boohee.model.RecordSport;
import com.boohee.modeldao.FoodRecordDao;
import com.boohee.modeldao.SportRecordDao;
import com.boohee.one.R;
import com.boohee.one.event.AddFinishAnimEvent;
import com.boohee.one.event.CustomSportEvent;
import com.boohee.one.event.SportEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.JsonParams;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.FoodUtils;
import com.boohee.utils.Helper;
import com.boohee.utils.KeyBoardUtils;
import com.boohee.widgets.CustomUnitPopupWindow;
import com.boohee.widgets.CustomUnitPopupWindow.OnChangeListener;

import de.greenrobot.event.EventBus;

import org.json.JSONObject;

public class AddCustomSportActivity extends GestureActivity {
    private static final String KEY_DATE = "key_date";
    @InjectView(2131427442)
    EditText et_food_calory;
    @InjectView(2131427439)
    EditText et_food_name;
    @InjectView(2131427440)
    EditText et_food_num;
    private float  mAmount;
    private float  mCalory;
    private String mSportName;
    private String mUnitName;
    private String record_on;
    @InjectView(2131427451)
    TextView tv_food_unit;

    public static void start(Context context, String record_on) {
        Intent starter = new Intent(context, AddCustomSportActivity.class);
        starter.putExtra("key_date", record_on);
        context.startActivity(starter);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a9);
        setTitle(R.string.hq);
        ButterKnife.inject((Activity) this);
        handleIntent();
        addListener();
    }

    private void handleIntent() {
        this.record_on = getStringExtra("key_date");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 1, 1, R.string.y8).setShowAsAction(2);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                addTolocal();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addListener() {
        this.et_food_num.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                String temp = s.toString();
                int posDot = temp.indexOf(".");
                if (posDot > 0 && (temp.length() - posDot) - 1 > 1) {
                    s.delete(posDot + 2, posDot + 3);
                }
            }
        });
        this.tv_food_unit.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                KeyBoardUtils.closeAll(AddCustomSportActivity.this.activity);
                CustomUnitPopupWindow popwindow = CustomUnitPopupWindow.getInstance();
                popwindow.setOnChangeListener(new OnChangeListener() {
                    public void onChange(String unit_name) {
                        AddCustomSportActivity.this.mUnitName = unit_name;
                        AddCustomSportActivity.this.tv_food_unit.setText(AddCustomSportActivity
                                .this.mUnitName);
                    }
                });
                popwindow.showPopWindow(AddCustomSportActivity.this.ctx, AddCustomSportActivity
                        .this.mUnitName);
            }
        });
    }

    private void addTolocal() {
        this.mSportName = this.et_food_name.getText().toString();
        this.mUnitName = this.tv_food_unit.getText().toString();
        String food_amount = this.et_food_num.getText().toString();
        String food_calory = this.et_food_calory.getText().toString();
        if (TextUtils.isEmpty(this.mSportName)) {
            Helper.showToast((CharSequence) "运动名称不能为空！");
            this.et_food_name.requestFocus();
        } else if (this.mSportName.length() > 60) {
            Helper.showToast((CharSequence) "超出字数限制");
        } else if (TextUtils.isEmpty(food_amount)) {
            Helper.showToast((CharSequence) "数量不能为空");
            this.et_food_num.requestFocus();
        } else {
            try {
                this.mAmount = Float.parseFloat(food_amount);
                if (this.mAmount > 999.0f) {
                    Helper.showToast((CharSequence) "数量不能超过999");
                    this.et_food_num.requestFocus();
                    return;
                }
                if (!FoodUtils.isKM(this.mUnitName)) {
                    this.mAmount = (float) (Math.round(this.mAmount * 10.0f) / 10);
                }
                if (TextUtils.isEmpty(this.mUnitName)) {
                    Helper.showToast((CharSequence) "单位不能为空");
                } else if (this.mSportName.length() > 60) {
                    Helper.showToast((CharSequence) "超出字数限制");
                } else if (TextUtils.isEmpty(food_calory)) {
                    Helper.showToast((CharSequence) "总热量不能为空");
                    this.et_food_calory.requestFocus();
                } else {
                    try {
                        this.mCalory = (float) Long.parseLong(food_calory);
                        if (this.mCalory > 10000.0f) {
                            Helper.showToast((CharSequence) "总热量不能超过10000");
                            this.et_food_calory.requestFocus();
                            return;
                        }
                        createCustomSport(this.mSportName, this.mUnitName, this.mAmount, this
                                .mCalory);
                    } catch (Exception e) {
                        Helper.showToast((CharSequence) "总热量不能超过10000");
                        this.et_food_calory.requestFocus();
                    }
                }
            } catch (Exception e2) {
                Helper.showToast((CharSequence) "数量不能超过999");
                this.et_food_num.requestFocus();
            }
        }
    }

    private void createCustomSport(String foodName, String unitName, float amount, float calory) {
        showLoading();
        FoodApi.createCustomActivities(params4CustomSport(foodName, unitName, amount, calory),
                this.activity, new JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                CustomSport customSport = (CustomSport) FastJsonUtils.fromJson(object,
                        CustomSport.class);
                if (customSport != null) {
                    EventBus.getDefault().post(new CustomSportEvent().setCustomSport(customSport));
                    AddCustomSportActivity.this.createActivity(customSport);
                }
            }

            public void onFinish() {
                super.onFinish();
                AddCustomSportActivity.this.dismissLoading();
            }
        });
    }

    private void createActivity(CustomSport customSport) {
        showLoading();
        RecordApi.createCustomActivity(paramsWithCustomSport(customSport), this.activity, new
                JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                Helper.showToast((CharSequence) "添加成功");
                RecordSport recordSport = RecordSport.parse(object);
                if (recordSport != null) {
                    EventBus.getDefault().post(new SportEvent().setRecordSport(recordSport)
                            .setEditType(1));
                    EventBus.getDefault().post(new AddFinishAnimEvent());
                }
                AddCustomSportActivity.this.finish();
            }

            public void onFinish() {
                super.onFinish();
                AddCustomSportActivity.this.dismissLoading();
            }
        });
    }

    private RecordSport paramsWithCustomSport(CustomSport customSport) {
        RecordSport recordSport = new RecordSport();
        recordSport.record_on = this.record_on;
        recordSport.duration = Float.parseFloat(customSport.amount);
        recordSport.activity_name = customSport.activity_name;
        recordSport.calory = Float.parseFloat(customSport.calory);
        recordSport.unit_name = customSport.unit_name;
        return recordSport;
    }

    private JsonParams params4CustomSport(String foodName, String unitName, float amount, float
            calory) {
        JsonParams params = new JsonParams();
        params.put("token", UserPreference.getToken(this.activity));
        params.put(SportRecordDao.ACTIVITY_NAME, foodName);
        params.put("unit_name", unitName);
        params.put(FoodRecordDao.AMOUNT, amount);
        params.put("calory", calory);
        return params;
    }
}
