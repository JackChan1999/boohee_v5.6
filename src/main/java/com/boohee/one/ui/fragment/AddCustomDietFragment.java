package com.boohee.one.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.boohee.api.RecordApi;
import com.boohee.database.UserPreference;
import com.boohee.model.RecordFood;
import com.boohee.modeldao.FoodRecordDao;
import com.boohee.one.event.AddFinishAnimEvent;
import com.boohee.one.event.DietEvent;
import com.boohee.one.event.TimeTypeDietEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.JsonParams;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.Helper;

import de.greenrobot.event.EventBus;

import org.json.JSONObject;

public class AddCustomDietFragment extends BaseDietFragment {
    public static final String KEY_CUSTOM_TYPE = "custom_type";
    public static final String KEY_INDEX       = "index";
    public static final String KEY_RECORD_FOOD = "record_food";
    public static final int    TYPE_ADD        = 0;
    public static final int    TYPE_EDIT       = 1;
    private int        mIndex;
    private RecordFood mRecordFood;
    private int mType = 0;

    public static AddCustomDietFragment newInstance(int type, int index, RecordFood recordFood) {
        AddCustomDietFragment addSportFragment = new AddCustomDietFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_CUSTOM_TYPE, type);
        bundle.putInt("index", index);
        bundle.putParcelable(KEY_RECORD_FOOD, recordFood);
        addSportFragment.setArguments(bundle);
        return addSportFragment;
    }

    public static AddCustomDietFragment newInstance(int type, RecordFood recordFood) {
        AddCustomDietFragment addCustomDietFragment = new AddCustomDietFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_CUSTOM_TYPE, type);
        bundle.putParcelable(KEY_RECORD_FOOD, recordFood);
        addCustomDietFragment.setArguments(bundle);
        return addCustomDietFragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.mIndex = getArguments().getInt("index");
            this.mRecordFood = (RecordFood) getArguments().getParcelable(KEY_RECORD_FOOD);
            this.mType = getArguments().getInt(KEY_CUSTOM_TYPE);
        }
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (this.mRecordFood != null) {
            this.foodName = this.mRecordFood.food_name;
            this.foodCalory = this.mRecordFood.calory;
            this.caloryPer = this.foodCalory / this.mRecordFood.amount;
            this.healthLight = this.mRecordFood.health_light;
            this.foodImage = this.mRecordFood.thumb_img_url;
            this.currentUnitName = this.mRecordFood.unit_name;
            this.currentUnitEatWeight = "1";
            this.recordOn = this.mRecordFood.record_on;
            this.timeType = this.mRecordFood.time_type;
            initTitle();
            refreshView();
            this.tvCaloryPer.setText(Math.round(this.caloryPer) + "千卡/" + "1" + this
                    .currentUnitName);
            this.ivCaloryStatus.setVisibility(8);
            if (this.mType == 0) {
                this.tvDelete.setVisibility(8);
            }
            this.tvWeight.setVisibility(8);
            initRulerView();
        }
    }

    protected boolean needHoldAmount() {
        if (this.mType == 0) {
            return false;
        }
        this.amount = this.mRecordFood.amount;
        return true;
    }

    private void createEating() {
        showLoading();
        RecordApi.createEating(paramsWithFoodRecord(), getActivity(), new JsonCallback
                (getActivity()) {
            public void ok(JSONObject object) {
                super.ok(object);
                AddCustomDietFragment.this.dismissAllowingStateLoss();
                RecordFood recordFood = (RecordFood) FastJsonUtils.fromJson(object, RecordFood
                        .class);
                if (recordFood != null) {
                    EventBus.getDefault().post(new DietEvent().setTimeType(recordFood.time_type)
                            .setRecordFood(recordFood).setEditType(1));
                    EventBus.getDefault().post(new AddFinishAnimEvent());
                }
                if (AddCustomDietFragment.this.changeListener != null) {
                    AddCustomDietFragment.this.changeListener.onFinish();
                }
            }

            public void onFinish() {
                super.onFinish();
                AddCustomDietFragment.this.dismissLoading();
            }
        });
    }

    private JsonParams paramsWithFoodRecord() {
        JsonParams params = new JsonParams();
        params.put("token", UserPreference.getToken(getActivity()));
        params.put("record_on", this.recordOn);
        params.put("unit_name", this.currentUnitName);
        params.put(FoodRecordDao.AMOUNT, this.amount);
        params.put("food_name", this.mRecordFood.food_name);
        params.put("calory", this.calory);
        params.put("time_type", this.timeType);
        return params;
    }

    private void updateEating() {
        showLoading();
        RecordApi.updateEating(this.mRecordFood.id, paramsWithFoodRecord(), getActivity(), new
                JsonCallback(getActivity()) {
            public void ok(JSONObject object) {
                super.ok(object);
                AddCustomDietFragment.this.dismissLoading();
                AddCustomDietFragment.this.dismissAllowingStateLoss();
                RecordFood recordFood = (RecordFood) FastJsonUtils.fromJson(object, RecordFood
                        .class);
                if (recordFood != null) {
                    EventBus.getDefault().post(new TimeTypeDietEvent().setBeforeTimeType
                            (AddCustomDietFragment.this.mRecordFood.time_type).setRecordFood
                            (recordFood).setIndex(AddCustomDietFragment.this.mIndex));
                }
            }

            public void onFinish() {
                super.onFinish();
                AddCustomDietFragment.this.dismissLoading();
            }
        });
    }

    protected void deleteEating() {
        showLoading();
        RecordApi.deleteEating(this.mRecordFood.id, getActivity(), new JsonCallback(getActivity()) {
            public void ok(JSONObject object) {
                super.ok(object);
                AddCustomDietFragment.this.dismissAllowingStateLoss();
                if (AddCustomDietFragment.this.mRecordFood != null) {
                    EventBus.getDefault().post(new DietEvent().setTimeType(AddCustomDietFragment
                            .this.mRecordFood.time_type).setIndex(AddCustomDietFragment.this
                            .mIndex).setEditType(3));
                }
            }

            public void onFinish() {
                super.onFinish();
                AddCustomDietFragment.this.dismissLoading();
            }
        });
    }

    protected void confirm() {
        if (this.mRecordFood != null) {
            if (this.amount <= 0.0f) {
                Helper.showToast((CharSequence) "输入不能为0");
            }
            if (this.mType == 0) {
                createEating();
            } else {
                updateEating();
            }
        }
    }

    protected void refreshUnit(int position) {
    }
}
