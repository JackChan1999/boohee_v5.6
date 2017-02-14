package com.boohee.one.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.api.FoodApi;
import com.boohee.food.FoodDetailActivity;
import com.boohee.food.LightIntroduceActivity;
import com.boohee.model.CustomCook;
import com.boohee.model.FoodWithUnit;
import com.boohee.more.EstimateFoodActivity;
import com.boohee.one.R;
import com.boohee.one.cache.CacheKey;
import com.boohee.one.event.CustomCookEvent;
import com.boohee.one.event.FoodCollectEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.utility.Event;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utility.TimeLinePatterns;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.FoodUtils;
import com.boohee.utils.Helper;
import com.booheee.view.keyboard.DietKeyboard;
import com.booheee.view.keyboard.OnDietResultListener;
import com.booheee.view.keyboard.Unit;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class AddMaterialFragment extends BaseDialogFragment {
    @InjectView(2131428150)
    CircleImageView civ_title;
    @InjectView(2131428167)
    DietKeyboard    diet_keyboard;
    @InjectView(2131427656)
    ImageView       iv_light;
    @InjectView(2131428164)
    LinearLayout    ll_collect;
    private float  mAmount;
    private float  mCalory;
    private String mCode;
    private int    mFoodId;
    private String mFoodName;
    private int    mFoodUnitId;
    private String mUnitName;
    private String thumb_image_name;
    @InjectView(2131428165)
    ToggleButton toggle_collect;
    @InjectView(2131428166)
    TextView     tv_collect;
    @InjectView(2131428158)
    TextView     tv_light;
    @InjectView(2131428152)
    TextView     txt_calory;
    @InjectView(2131428151)
    TextView     txt_name;
    @InjectView(2131429207)
    TextView     txt_title;

    public static AddMaterialFragment newInstance(String code) {
        AddMaterialFragment addDietFragment = new AddMaterialFragment();
        addDietFragment.mCode = code;
        return addDietFragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    private void initTitle() {
        this.txt_title.setText("添加食材");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fb, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject((Object) this, view);
        initTitle();
        initUI(this.mCache.getAsJSONObject(String.format(CacheKey.FOOD_DETAIL, new Object[]{this
                .mCode})));
        getFoodShowWithLight(this.mCode);
    }

    @OnClick({2131429414, 2131429415, 2131428157, 2131428164, 2131427693, 2131428161})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_detail:
                MobclickAgent.onEvent(getActivity(), Event.tool_foodandsport_fooddetail);
                FoodDetailActivity.comeOnBaby(getActivity(), this.mCode, false);
                return;
            case R.id.ll_light:
                startActivity(new Intent(getActivity(), LightIntroduceActivity.class));
                return;
            case R.id.ll_estimate:
                EstimateFoodActivity.comeOnBaby(getActivity());
                return;
            case R.id.ll_collect:
                MobclickAgent.onEvent(getActivity(), Event.tool_foodandsport_favoritefood);
                this.ll_collect.setEnabled(false);
                if (this.toggle_collect.isChecked()) {
                    deleteFavorite(this.mCode);
                    return;
                } else {
                    addFavorite(this.mCode);
                    return;
                }
            case R.id.txt_cancel:
                MobclickAgent.onEvent(getActivity(), Event.tool_searchfood_canceladd);
                dismissAllowingStateLoss();
                return;
            case R.id.txt_commit:
                if (this.mAmount <= 0.0f) {
                    Helper.showToast((CharSequence) "输入不能为零");
                    return;
                } else {
                    createMaterial();
                    return;
                }
            default:
                return;
        }
    }

    private void initDietKeyboard(FoodWithUnit food) {
        this.mCalory = food.calory;
        this.mCode = food.code;
        this.mFoodName = food.name;
        this.mFoodId = food.food_id;
        List<Unit> units = new ArrayList();
        if (food.units == null || food.units.size() == 0) {
            Unit foodUnit = new Unit();
            this.mUnitName = "克";
            foodUnit.unit_name = this.mUnitName;
            foodUnit.eat_weight = "1.0";
            units.add(foodUnit);
        } else {
            units.addAll(food.units);
        }
        this.diet_keyboard.setOnResultListener(new OnDietResultListener() {
            public void onResult(float amount, float calory, int unit_id, String unit_name) {
                AddMaterialFragment.this.mAmount = amount;
                AddMaterialFragment.this.mCalory = calory;
                AddMaterialFragment.this.mFoodUnitId = unit_id;
                AddMaterialFragment.this.mUnitName = unit_name;
            }
        });
        this.diet_keyboard.init(100.0f, this.mCalory / 100.0f, (Unit) units.get(0), units);
        this.txt_name.setText(food.name);
        this.txt_calory.setText(Math.round(food.calory) + "");
        if (!TextUtils.isEmpty(food.thumb_image_name)) {
            this.thumb_image_name = food.thumb_image_name;
            ImageLoader.getInstance().displayImage(TimeLinePatterns.WEB_SCHEME + food
                    .thumb_image_name, this.civ_title, ImageLoaderOptions.global((int) R.drawable
                    .aa2));
        }
        isFavorite(food.code);
        FoodUtils.switchToLight(food.health_light, this.iv_light, this.tv_light);
    }

    private void isFavorite(String mCode) {
        showLoading();
        FoodApi.isFavorite(mCode, getActivity(), new JsonCallback(getActivity()) {
            public void ok(JSONObject object) {
                super.ok(object);
                if (object == null || !object.has("status") || !AddMaterialFragment.this.isAdded
                        ()) {
                    return;
                }
                if (object.optBoolean("status")) {
                    AddMaterialFragment.this.toggle_collect.setChecked(true);
                    AddMaterialFragment.this.tv_collect.setText("已收藏");
                    return;
                }
                AddMaterialFragment.this.toggle_collect.setChecked(false);
                AddMaterialFragment.this.tv_collect.setText("未收藏");
            }

            public void onFinish() {
                super.onFinish();
                AddMaterialFragment.this.dismissLoading();
            }

            public void fail(String message) {
            }
        });
    }

    private void deleteFavorite(String mCode) {
        showLoading();
        FoodApi.deleteFavorite(mCode, getActivity(), new JsonCallback(getActivity()) {
            public void ok(JSONObject object) {
                super.ok(object);
                if (!AddMaterialFragment.this.isDetached()) {
                    AddMaterialFragment.this.toggle_collect.setChecked(false);
                    AddMaterialFragment.this.tv_collect.setText("未收藏");
                }
            }

            public void onFinish() {
                super.onFinish();
                AddMaterialFragment.this.dismissLoading();
                if (AddMaterialFragment.this.ll_collect != null) {
                    AddMaterialFragment.this.ll_collect.setEnabled(true);
                }
            }
        });
    }

    private void addFavorite(String mCode) {
        showLoading();
        FoodApi.addFavorite(mCode, getActivity(), new JsonCallback(getActivity()) {
            public void ok(JSONObject object) {
                super.ok(object);
                if (AddMaterialFragment.this.isAdded()) {
                    AddMaterialFragment.this.toggle_collect.setChecked(true);
                    AddMaterialFragment.this.tv_collect.setText("已收藏");
                }
            }

            public void onFinish() {
                super.onFinish();
                AddMaterialFragment.this.dismissLoading();
                if (AddMaterialFragment.this.ll_collect != null) {
                    AddMaterialFragment.this.ll_collect.setEnabled(true);
                }
            }

            public void fail(String message) {
            }
        });
    }

    private void createMaterial() {
        EventBus.getDefault().post(new CustomCookEvent().setCustomCook(new CustomCook(this
                .mFoodName, this.mCode, this.mAmount, this.mCalory, this.mFoodUnitId, this
                .mUnitName, this.mFoodId)));
        dismissAllowingStateLoss();
    }

    private void getFoodShowWithLight(final String code) {
        if (!TextUtils.isEmpty(code)) {
            FoodApi.getFoodShowWithLight(code, getActivity(), new JsonCallback(getActivity()) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    AddMaterialFragment.this.initUI(object);
                    AddMaterialFragment.this.mCache.put(String.format(CacheKey.FOOD_DETAIL, new
                            Object[]{code}), object);
                }

                public void fail(String message) {
                }
            });
        }
    }

    private void initUI(JSONObject object) {
        if (object != null) {
            FoodWithUnit food = (FoodWithUnit) FastJsonUtils.fromJson(object, FoodWithUnit.class);
            if (food != null && isAdded()) {
                initDietKeyboard(food);
            }
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(FoodCollectEvent collectEvent) {
        if (collectEvent != null && this.toggle_collect != null) {
            if (collectEvent.isCollect()) {
                this.toggle_collect.setChecked(true);
            } else {
                this.toggle_collect.setChecked(false);
            }
        }
    }

    public void onEventMainThread(CustomCookEvent cookEvent) {
    }
}
