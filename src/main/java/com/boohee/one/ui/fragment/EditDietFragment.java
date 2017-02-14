package com.boohee.one.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.boohee.api.FoodApi;
import com.boohee.api.RecordApi;
import com.boohee.database.UserPreference;
import com.boohee.food.FoodDetailActivity;
import com.boohee.model.FoodWithUnit;
import com.boohee.model.RecordFood;
import com.boohee.modeldao.FoodRecordDao;
import com.boohee.one.cache.CacheKey;
import com.boohee.one.event.DietEvent;
import com.boohee.one.event.TimeTypeDietEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.JsonParams;
import com.boohee.utility.Event;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.Helper;
import com.boohee.utils.HttpUtils;
import com.boohee.widgets.tablayout.FoodTabUnit;
import com.boohee.widgets.tablayout.TabModelInterface;
import com.booheee.view.keyboard.Unit;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class EditDietFragment extends BaseDietFragment {
    public static final String KEY_INDEX       = "key_INDEX";
    public static final String KEY_RECORD_FOOD = "key_record_food";
    private Unit       currentUnit;
    private int        mIndex;
    private RecordFood mRecordFood;
    private int        originalTimeType;
    private String     originalUnitName;
    private List<Unit> units = new ArrayList();

    public static EditDietFragment newInstance(int time_type, int index, RecordFood recordFood) {
        EditDietFragment editDietFragment = new EditDietFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("time_type", time_type);
        bundle.putParcelable(KEY_RECORD_FOOD, recordFood);
        bundle.putInt(KEY_INDEX, index);
        editDietFragment.setArguments(bundle);
        return editDietFragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.timeType = getArguments().getInt("time_type");
            this.originalTimeType = getArguments().getInt("time_type");
            this.mRecordFood = (RecordFood) getArguments().getParcelable(KEY_RECORD_FOOD);
            this.mIndex = getArguments().getInt(KEY_INDEX);
            this.foodName = this.mRecordFood.food_name;
            this.foodCalory = this.mRecordFood.calory;
            this.caloryPer = this.foodCalory / 100.0f;
            this.healthLight = this.mRecordFood.health_light;
            this.foodImage = this.mRecordFood.thumb_img_url;
            this.originalUnitName = this.mRecordFood.unit_name;
            this.recordOn = this.mRecordFood.record_on;
            this.timeType = this.mRecordFood.time_type;
        }
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (this.mRecordFood != null) {
            refreshView();
            initTitle();
            initUI(this.mCache.getAsJSONObject(String.format(CacheKey.FOOD_DETAIL, new
                    Object[]{this.mRecordFood.code})));
            getFoodShowWithLight(this.mRecordFood.code);
            showLoading();
        }
    }

    private void initUI(JSONObject object) {
        FoodWithUnit food = (FoodWithUnit) FastJsonUtils.fromJson(object, FoodWithUnit.class);
        if (food != null && isAdded()) {
            this.foodCalory = food.calory;
            this.caloryPer = food.calory / 100.0f;
            this.foodName = food.name;
            this.foodImage = food.thumb_image_name;
            this.foodId = food.food_id;
            this.healthLight = food.health_light;
            refreshView();
            this.ivCaloryStatus.setVisibility(0);
            initUnitTab(initUnitsWithFood(food), getCurrentUnit());
        }
    }

    protected List<TabModelInterface> initUnitsWithFood(FoodWithUnit food) {
        int i;
        this.units.clear();
        if (food.units == null || food.units.size() == 0) {
            Unit foodUnit = new Unit();
            if (food.is_liquid) {
                foodUnit.unit_name = "毫升";
            } else {
                foodUnit.unit_name = "克";
            }
            foodUnit.eat_weight = "1.0";
            this.units.add(foodUnit);
        } else {
            this.units.addAll(food.units);
            for (i = 0; i < this.units.size(); i++) {
                Unit unit = (Unit) this.units.get(i);
                if (unit != null && TextUtils.equals(unit.unit_name, "克") && food.is_liquid) {
                    unit.unit_name = "毫升";
                }
            }
        }
        List<TabModelInterface> foodTabUnits = new ArrayList();
        for (i = 0; i < this.units.size(); i++) {
            foodTabUnits.add(new FoodTabUnit((Unit) this.units.get(i)));
        }
        return foodTabUnits;
    }

    private int getCurrentUnit() {
        for (int i = 0; i < this.units.size(); i++) {
            if (TextUtils.equals(this.mRecordFood.unit_name, ((Unit) this.units.get(i))
                    .unit_name)) {
                return i;
            }
        }
        return 0;
    }

    protected void confirm() {
        if (this.amount <= 0.0f) {
            Helper.showToast((CharSequence) "输入不能为0");
        } else {
            updateEating();
        }
    }

    protected boolean needHoldAmount() {
        if (!TextUtils.equals(this.currentUnitName, this.originalUnitName)) {
            return false;
        }
        this.amount = this.mRecordFood.amount;
        return true;
    }

    protected void refreshUnit(int position) {
        if (this.units != null && this.units.size() != 0) {
            this.currentUnit = (Unit) this.units.get(position);
            this.currentUnitName = this.currentUnit.unit_name;
            this.currentUnitEatWeight = this.currentUnit.eat_weight;
            this.currentFoodUnitId = this.currentUnit.food_unit_id;
            initRulerView();
        }
    }

    private void getFoodShowWithLight(String code) {
        if (!TextUtils.isEmpty(code)) {
            FoodApi.getFoodShowWithLight(code, getActivity(), new JsonCallback(getActivity()) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    EditDietFragment.this.initUI(object);
                }

                public void onFinish() {
                    super.onFinish();
                    EditDietFragment.this.dismissLoading();
                }
            });
        }
    }

    protected void dietInfo() {
        if (this.mRecordFood != null) {
            super.dietInfo();
            MobclickAgent.onEvent(getActivity(), Event.tool_foodandsport_fooddetail);
            FoodDetailActivity.comeOnBaby(getActivity(), this.mRecordFood.code, false);
        }
    }

    private void updateEating() {
        showLoading();
        RecordApi.updateEating(this.mRecordFood.id, paramsWithFoodRecord(), getActivity(), new
                JsonCallback(getActivity()) {
            public void ok(JSONObject object) {
                super.ok(object);
                EditDietFragment.this.dismissLoading();
                EditDietFragment.this.dismissAllowingStateLoss();
                RecordFood recordFood = (RecordFood) FastJsonUtils.fromJson(object, RecordFood
                        .class);
                if (recordFood == null) {
                    return;
                }
                if (EditDietFragment.this.originalTimeType == EditDietFragment.this.timeType) {
                    EventBus.getDefault().post(new DietEvent().setTimeType(EditDietFragment.this
                            .timeType).setRecordFood(recordFood).setIndex(EditDietFragment.this
                            .mIndex).setEditType(2));
                } else {
                    EventBus.getDefault().post(new TimeTypeDietEvent().setBeforeTimeType
                            (EditDietFragment.this.originalTimeType).setRecordFood(recordFood)
                            .setIndex(EditDietFragment.this.mIndex));
                }
            }

            public void onFinish() {
                super.onFinish();
                EditDietFragment.this.dismissLoading();
            }
        });
    }

    protected void deleteEating() {
        if (HttpUtils.isNetworkAvailable(getActivity())) {
            showLoading();
            RecordApi.deleteEating(this.mRecordFood.id, getActivity(), new JsonCallback
                    (getActivity()) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    EditDietFragment.this.dismissAllowingStateLoss();
                    if (EditDietFragment.this.mRecordFood != null) {
                        EventBus.getDefault().post(new DietEvent().setTimeType(EditDietFragment
                                .this.timeType).setIndex(EditDietFragment.this.mIndex)
                                .setEditType(3));
                    }
                }

                public void onFinish() {
                    super.onFinish();
                    EditDietFragment.this.dismissLoading();
                }
            });
            return;
        }
        new FoodRecordDao(getActivity()).delete(this.mRecordFood);
        dismissAllowingStateLoss();
        if (this.mRecordFood != null) {
            EventBus.getDefault().post(new DietEvent().setTimeType(this.timeType).setIndex(this
                    .mIndex).setEditType(3));
        }
    }

    private JsonParams paramsWithFoodRecord() {
        JsonParams params = new JsonParams();
        params.put("token", UserPreference.getToken(getActivity()));
        params.put("record_on", this.recordOn);
        params.put("unit_name", this.currentUnitName);
        params.put(FoodRecordDao.AMOUNT, this.amount);
        params.put("code", this.mRecordFood.code);
        params.put("food_name", this.mRecordFood.food_name);
        params.put(FoodRecordDao.FOOD_UNIT_ID, this.currentFoodUnitId);
        params.put("calory", this.mRecordFood.calory);
        params.put("time_type", this.timeType);
        return params;
    }
}
