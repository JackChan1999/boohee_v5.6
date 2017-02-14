package com.boohee.one.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import butterknife.ButterKnife;

import com.boohee.api.FoodApi;
import com.boohee.database.UserPreference;
import com.boohee.food.FoodDetailActivity;
import com.boohee.model.FoodWithUnit;
import com.boohee.model.RecentFoodUnit;
import com.boohee.model.RecordFood;
import com.boohee.modeldao.FoodRecordDao;
import com.boohee.one.cache.CacheKey;
import com.boohee.one.event.AddFinishAnimEvent;
import com.boohee.one.event.DietEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.JsonParams;
import com.boohee.record.DietSportCalendarActivity;
import com.boohee.utility.Event;
import com.boohee.utils.DateFormatUtils;
import com.boohee.utils.DateHelper;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.FoodUtils;
import com.boohee.utils.Helper;
import com.boohee.widgets.tablayout.FoodTabUnit;
import com.boohee.widgets.tablayout.TabModelInterface;
import com.booheee.view.keyboard.Unit;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class AddDietFragment extends BaseDietFragment {
    private Unit   currentUnit;
    private String mCode;
    private float recentFoodUnitAmount = -1.0f;
    private int recentFoodUnitId;
    protected List<Unit> units = new ArrayList();

    public static AddDietFragment newInstance(int time_type, String record_on, String code) {
        AddDietFragment addDietFragment = new AddDietFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("time_type", time_type);
        bundle.putString("date", record_on);
        bundle.putString(BaseDietFragment.FOOD_CODE, code);
        addDietFragment.setArguments(bundle);
        return addDietFragment;
    }

    public static AddDietFragment newInstance(String code) {
        return newInstance(DietSportCalendarActivity.getTimeTypeWithName(DateFormatUtils
                .getTimeFiled()), DateHelper.today(), code);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject((Object) this, view);
        if (getArguments() != null) {
            this.timeType = getArguments().getInt("time_type");
            this.recordOn = getArguments().getString("date");
            this.mCode = getArguments().getString(BaseDietFragment.FOOD_CODE);
            initTitle();
            initUI(this.mCache.getAsJSONObject(String.format(CacheKey.FOOD_DETAIL, new
                    Object[]{this.mCode})));
            getFoodShowWithLight(this.mCode);
        }
    }

    protected void dietInfo() {
        super.dietInfo();
        MobclickAgent.onEvent(getActivity(), Event.tool_foodandsport_fooddetail);
        FoodDetailActivity.comeOnBaby(getActivity(), this.mCode, false);
    }

    protected void confirm() {
        if (this.amount <= 0.0f) {
            Helper.showToast((CharSequence) "输入不能为0");
        } else {
            createEating();
        }
    }

    public void onValueChange(float value) {
        super.onValueChange(value);
    }

    public void onTabChanged(int position) {
        super.onTabChanged(position);
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

    protected void cancel() {
        super.cancel();
        MobclickAgent.onEvent(getActivity(), Event.tool_searchfood_canceladd);
    }

    protected boolean needHoldAmount() {
        if (this.currentFoodUnitId != this.recentFoodUnitId || this.recentFoodUnitAmount <= 0.0f) {
            return false;
        }
        this.amount = this.recentFoodUnitAmount;
        return true;
    }

    private void getFoodShowWithLight(final String code) {
        if (!TextUtils.isEmpty(code)) {
            FoodApi.getFoodShowWithLight(code, getActivity(), new JsonCallback(getActivity()) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    AddDietFragment.this.initUI(object);
                    AddDietFragment.this.mCache.put(String.format(CacheKey.FOOD_DETAIL, new
                            Object[]{code}), object);
                }

                public void fail(String message) {
                }
            });
        }
    }

    private void initUI(JSONObject object) {
        if (!isRemoved() && object != null) {
            FoodWithUnit food = (FoodWithUnit) FastJsonUtils.fromJson(object, FoodWithUnit.class);
            if (food != null && isAdded()) {
                this.foodCalory = food.calory;
                this.caloryPer = food.calory / 100.0f;
                this.foodName = food.name;
                this.foodImage = food.thumb_image_name;
                this.foodId = food.food_id;
                this.healthLight = food.health_light;
                refreshView();
                initUnitTab(initUnitsWithFood(food), getRecentFoodUnit(food));
            }
            this.tvDelete.setVisibility(8);
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

    private int getRecentFoodUnit(FoodWithUnit food) {
        RecentFoodUnit recentFoodUnit = FoodUtils.getRecentFoodUnit(getActivity(), food.food_id);
        if (recentFoodUnit != null) {
            for (int i = 0; i < this.units.size(); i++) {
                if (((Unit) this.units.get(i)).food_unit_id == recentFoodUnit.unit_id) {
                    this.recentFoodUnitAmount = recentFoodUnit.amount;
                    this.recentFoodUnitId = recentFoodUnit.unit_id;
                    return i;
                }
            }
        }
        return 0;
    }

    private void createEating() {
        RecordFood recordFood = new FoodRecordDao(getActivity()).add(this.foodName, this
                .timeType, this.mCode, this.amount, this.foodCalory, this.currentFoodUnitId, this
                .currentUnitName, this.recordOn);
        dismissAllowingStateLoss();
        if (recordFood != null) {
            EventBus.getDefault().post(new DietEvent().setTimeType(this.timeType).setRecordFood
                    (recordFood).setEditType(1));
            EventBus.getDefault().post(new AddFinishAnimEvent().setThumb_image_name(this
                    .foodImage));
            MobclickAgent.onEvent(getActivity(), Event.tool_addDietRecordOK);
            MobclickAgent.onEvent(getActivity(), Event.tool_recordOK);
            FoodUtils.saveRecentFoodUnit(getActivity(), this.foodId, this.amount, this
                    .currentFoodUnitId);
        }
        if (this.changeListener != null) {
            this.changeListener.onFinish();
        }
    }

    private JsonParams paramsWithFoodRecord() {
        JsonParams params = new JsonParams();
        params.put("token", UserPreference.getToken(getActivity()));
        params.put("record_on", this.recordOn);
        params.put("unit_name", this.currentUnitName);
        params.put(FoodRecordDao.AMOUNT, this.amount);
        params.put("code", this.mCode);
        params.put("food_name", this.foodName);
        params.put(FoodRecordDao.FOOD_UNIT_ID, this.currentFoodUnitId);
        params.put("calory", this.calory);
        params.put("time_type", this.timeType);
        return params;
    }
}
