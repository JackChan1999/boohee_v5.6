package com.boohee.food;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.api.FoodApi;
import com.boohee.api.RecordApi;
import com.boohee.database.UserPreference;
import com.boohee.main.GestureActivity;
import com.boohee.model.CustomFood;
import com.boohee.model.RecordFood;
import com.boohee.modeldao.FoodRecordDao;
import com.boohee.one.R;
import com.boohee.one.event.AddFinishAnimEvent;
import com.boohee.one.event.CustomFoodEvent;
import com.boohee.one.event.DietEvent;
import com.boohee.one.event.MyFoodEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.JsonParams;
import com.boohee.uploader.QiniuConfig.Prefix;
import com.boohee.uploader.QiniuModel;
import com.boohee.uploader.QiniuUploader;
import com.boohee.uploader.UploadHandler;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.Helper;
import com.boohee.widgets.RoundedCornersImage;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.greenrobot.event.EventBus;

import java.io.File;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

import org.json.JSONObject;

public class AddCustomFoodActivity extends GestureActivity {
    private static final String KEY_DATE        = "key_date";
    private static final String KEY_IS_ADD_DIET = "isAddDiet";
    private static final String KEY_TIME_TYPE   = "key_time_type";
    private static final int    REQUEST_IMAGE   = 1;
    @InjectView(2131427450)
    EditText et_carbohydrate_num;
    @InjectView(2131427449)
    EditText et_fat_num;
    @InjectView(2131427442)
    EditText et_food_calory;
    @InjectView(2131427439)
    EditText et_food_name;
    @InjectView(2131427440)
    EditText et_food_num;
    @InjectView(2131427441)
    EditText et_food_unit;
    @InjectView(2131427448)
    EditText et_protein_num;
    private String imgPath;
    private boolean isAddDiet = true;
    @InjectView(2131427447)
    ImageView           ivDelete;
    @InjectView(2131427446)
    RoundedCornersImage ivFoodImg;
    private String key;
    private float  mAmount;
    private float  mCalory;
    private float  mCarbohydrate;
    private float  mFat;
    private String mFoodName;
    private float  mProtein;
    private int    mTimeType;
    private String mUnitName;
    private String record_on;
    @InjectView(2131427445)
    RelativeLayout rlFoodImg;
    @InjectView(2131427443)
    RelativeLayout rlFoodPhoto;

    public static void start(Context context, int time_type, String record_on) {
        Intent starter = new Intent(context, AddCustomFoodActivity.class);
        starter.putExtra(KEY_TIME_TYPE, time_type);
        starter.putExtra("key_date", record_on);
        context.startActivity(starter);
    }

    public static void comeWithoutAddDiet(Context context) {
        Intent intent = new Intent(context, AddCustomFoodActivity.class);
        intent.putExtra(KEY_IS_ADD_DIET, false);
        context.startActivity(intent);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a8);
        setTitle(R.string.hq);
        ButterKnife.inject((Activity) this);
        handleIntent();
        addListener();
    }

    @OnClick({2131427443, 2131427447})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_food_photo:
                showTakePhotoDialog(1);
                return;
            case R.id.iv_delete:
                this.imgPath = "";
                this.rlFoodImg.setVisibility(8);
                return;
            default:
                return;
        }
    }

    private void showTakePhotoDialog(int requestCode) {
        Intent intent = new Intent(this.activity, MultiImageSelectorActivity.class);
        intent.putExtra("show_camera", true);
        intent.putExtra("max_select_count", 1);
        intent.putExtra("select_count_mode", 0);
        startActivityForResult(intent, requestCode);
    }

    private void handleIntent() {
        this.mTimeType = getIntExtra(KEY_TIME_TYPE);
        this.record_on = getStringExtra("key_date");
        this.isAddDiet = getIntent().getBooleanExtra(KEY_IS_ADD_DIET, true);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            switch (requestCode) {
                case 1:
                    List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity
                            .EXTRA_RESULT);
                    if (path != null && path.size() > 0) {
                        this.imgPath = (String) path.get(0);
                        ImageLoader.getInstance().displayImage(Uri.decode(Uri.fromFile(new File
                                (this.imgPath)).toString()), this.ivFoodImg, ImageLoaderOptions
                                .color(R.color.ju));
                        this.rlFoodImg.setVisibility(0);
                        break;
                    }
            }
            super.onActivityResult(requestCode, resultCode, data);
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
    }

    private void addTolocal() {
        this.mFoodName = this.et_food_name.getText().toString();
        this.mUnitName = this.et_food_unit.getText().toString();
        String food_amount = this.et_food_num.getText().toString();
        String food_calory = this.et_food_calory.getText().toString();
        try {
            this.mProtein = Float.parseFloat(this.et_protein_num.getText().toString());
            this.mFat = Float.parseFloat(this.et_fat_num.getText().toString());
            this.mCarbohydrate = Float.parseFloat(this.et_carbohydrate_num.getText().toString());
        } catch (Exception e) {
        }
        if (TextUtils.isEmpty(this.mFoodName)) {
            Helper.showToast((CharSequence) "食物名称不能为空！");
            this.et_food_name.requestFocus();
        } else if (this.mFoodName.length() > 60) {
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
                } else if (TextUtils.isEmpty(this.mUnitName)) {
                    Helper.showToast((CharSequence) "单位不能为空");
                    this.et_food_unit.requestFocus();
                } else if (this.mFoodName.length() > 60) {
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
                        createCustomFood();
                    } catch (Exception e2) {
                        Helper.showToast((CharSequence) "总热量不能超过10000");
                        this.et_food_calory.requestFocus();
                    }
                }
            } catch (Exception e3) {
                Helper.showToast((CharSequence) "数量不能超过999");
                this.et_food_num.requestFocus();
            }
        }
    }

    private void createCustomFood() {
        if (TextUtils.isEmpty(this.imgPath)) {
            createCustomFood(this.mFoodName, this.mUnitName, this.mAmount, this.mCalory, this
                    .key, this.mProtein, this.mFat, this.mCarbohydrate);
            return;
        }
        showLoading();
        QiniuUploader.upload(Prefix.ifood, new UploadHandler() {
            public void onSuccess(List<QiniuModel> infos) {
                QiniuModel model = (QiniuModel) infos.get(0);
                AddCustomFoodActivity.this.key = model.key;
                AddCustomFoodActivity.this.createCustomFood(AddCustomFoodActivity.this.mFoodName,
                        AddCustomFoodActivity.this.mUnitName, AddCustomFoodActivity.this.mAmount,
                        AddCustomFoodActivity.this.mCalory, model.key, AddCustomFoodActivity.this
                                .mProtein, AddCustomFoodActivity.this.mFat, AddCustomFoodActivity
                                .this.mCarbohydrate);
            }

            public void onError(String msg) {
                Helper.showToast((CharSequence) msg);
            }

            public void onFinish() {
                AddCustomFoodActivity.this.dismissLoading();
            }
        }, this.imgPath);
    }

    private void createCustomFood(String foodName, String unitName, float amount, float calory,
                                  String key, float protein, float fat, float carbohydrate) {
        FoodApi.createCustomFood(paramsWithCustomFood(foodName, unitName, amount, calory, key,
                protein, fat, carbohydrate), this.activity, new JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                CustomFood customFood = (CustomFood) FastJsonUtils.fromJson(object, CustomFood
                        .class);
                if (customFood != null) {
                    EventBus.getDefault().post(new CustomFoodEvent().setCustomFood(customFood));
                    EventBus.getDefault().post(new MyFoodEvent().setFlag(1));
                    if (AddCustomFoodActivity.this.isAddDiet) {
                        AddCustomFoodActivity.this.createEating(customFood);
                        return;
                    }
                    Helper.showToast((CharSequence) "添加成功");
                    AddCustomFoodActivity.this.finish();
                }
            }

            public void onFinish() {
                super.onFinish();
                AddCustomFoodActivity.this.dismissLoading();
            }
        });
    }

    private void createEating(CustomFood customFood) {
        showLoading();
        RecordApi.createEating(paramsWithFoodRecord(customFood), this.activity, new JsonCallback
                (this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                Helper.showToast((CharSequence) "添加成功");
                RecordFood recordFood = (RecordFood) FastJsonUtils.fromJson(object, RecordFood
                        .class);
                if (recordFood != null) {
                    EventBus.getDefault().post(new DietEvent().setTimeType(recordFood.time_type)
                            .setRecordFood(recordFood).setEditType(1));
                    EventBus.getDefault().post(new AddFinishAnimEvent());
                }
                AddCustomFoodActivity.this.finish();
            }

            public void onFinish() {
                super.onFinish();
                AddCustomFoodActivity.this.dismissLoading();
            }
        });
    }

    private JsonParams paramsWithFoodRecord(CustomFood customFood) {
        JsonParams params = new JsonParams();
        params.put("token", UserPreference.getToken(this.activity));
        params.put("record_on", this.record_on);
        params.put("time_type", this.mTimeType);
        params.put("unit_name", customFood.unit_name);
        params.put(FoodRecordDao.AMOUNT, customFood.amount);
        params.put("food_name", customFood.food_name);
        params.put("calory", customFood.calory);
        return params;
    }

    private JsonParams paramsWithCustomFood(String foodName, String unitName, float amount, float
            calory, String key, float protein, float fat, float carbohydrate) {
        JsonParams params = new JsonParams();
        params.put("token", UserPreference.getToken(this.activity));
        params.put("food_name", foodName);
        params.put("unit_name", unitName);
        params.put(FoodRecordDao.AMOUNT, amount);
        params.put("calory", calory);
        params.put("fat", fat);
        params.put("protein", protein);
        params.put("carbohydrate", carbohydrate);
        params.put("qiniu_key", key);
        return params;
    }
}
