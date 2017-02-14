package com.boohee.food;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import com.boohee.api.FoodApi;
import com.boohee.database.OnePreference;
import com.boohee.main.GestureActivity;
import com.boohee.model.FoodCompare;
import com.boohee.model.FoodInfo;
import com.boohee.model.FoodInfoUnit;
import com.boohee.model.IngredientInfo;
import com.boohee.more.EstimateFoodActivity;
import com.boohee.myview.highlight.HighLight;
import com.boohee.myview.highlight.HighLight.MarginInfo;
import com.boohee.myview.highlight.HighLight.OnHighLightClickListener;
import com.boohee.myview.highlight.HighLight.OnPosCallback;
import com.boohee.one.R;
import com.boohee.one.event.FoodCollectEvent;
import com.boohee.one.event.MyFoodEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.ui.fragment.AddDietFragment;
import com.boohee.status.LargeImageActivity;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.AppUtils;
import com.boohee.utils.FoodUtils;
import com.boohee.utils.Helper;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.greenrobot.event.EventBus;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class FoodDetailActivity extends GestureActivity {
    public static final String DOUGUO_RECIPE = "douguo";
    public static final String KEY_FOOD_CODE = "key_food_code";
    public static final String KEY_IS_RECORD = "key_is_record";
    public static final int    MIN_UNIT_SIZE = 2;
    public static final String OWN_RECIPE    = "own";
    @InjectView(2131427669)
    CheckBox cbUnits;
    private boolean isRecord;
    @InjectView(2131427663)
    ImageView      ivCompare;
    @InjectView(2131427648)
    ImageView      ivFoodIcon;
    @InjectView(2131427656)
    ImageView      ivHealthLight;
    @InjectView(2131427659)
    LinearLayout   llCompare;
    @InjectView(2131427661)
    LinearLayout   llCompareContent;
    @InjectView(2131427658)
    LinearLayout   llRecipe;
    @InjectView(2131427677)
    RelativeLayout llRecord;
    @InjectView(2131427666)
    LinearLayout   llUnits;
    @InjectView(2131427668)
    LinearLayout   llUnitsCheckbox;
    @InjectView(2131427667)
    LinearLayout   llUnitsItem;
    @InjectView(2131427674)
    LinearLayout   llUploader;
    @InjectView(2131427670)
    ListView       lvMain;
    private IngredientInfoAdapter mAdapter;
    private String                mCode;
    private List<IngredientInfo> mDataList = new ArrayList();
    private FoodInfo mFoodInfo;
    private Menu     mMenu;
    @InjectView(2131427654)
    RadioButton rbCalory;
    @InjectView(2131427655)
    RadioButton rbKjoule;
    @InjectView(2131427653)
    RadioGroup  rgUnit;
    private List<IngredientInfo> showList = new ArrayList();
    @InjectView(2131427657)
    TextView tvAppraise;
    @InjectView(2131427461)
    TextView tvCalory;
    @InjectView(2131427665)
    TextView tvCompareInfo;
    @InjectView(2131427664)
    TextView tvCompateAmount;
    @InjectView(2131427649)
    TextView tvFoodName;
    @InjectView(2131427675)
    TextView tvUploader;
    @InjectView(2131427651)
    TextView tvWeight;
    @InjectView(2131427673)
    View     vBottom;

    @OnClick({2131427648, 2131427656, 2131427671, 2131427658, 2131427660, 2131427677, 2131427676,
            2131427672})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_food_icon:
                if (this.mFoodInfo != null) {
                    LargeImageActivity.start(this, this.mFoodInfo.large_image_url);
                    return;
                }
                return;
            case R.id.iv_light:
                LightIntroduceActivity.comeOnBaby(this);
                return;
            case R.id.ll_recipe:
                if (this.mFoodInfo == null) {
                    return;
                }
                if (OWN_RECIPE.equals(this.mFoodInfo.recipe_type)) {
                    BooheeRecipeActivity.comeOnBaby(this, this.mFoodInfo.code);
                    return;
                } else if (DOUGUO_RECIPE.equals(this.mFoodInfo.recipe_type)) {
                    RecipeActivity.comeOnBaby(this, this.mFoodInfo.code);
                    return;
                } else {
                    return;
                }
            case R.id.tv_how_assessment:
                EstimateFoodActivity.comeOnBaby(this.activity);
                return;
            case R.id.tv_see_more:
                IngredientInfoActivity.comeOnBaby(this, this.mDataList);
                return;
            case R.id.tv_error:
                FoodErrorActivity.comeOn(this.ctx, this.mCode);
                return;
            case R.id.iv_food_app:
                AppUtils.launchFood(this.ctx);
                return;
            case R.id.ll_food_record:
                Fragment addDietFragment = AddDietFragment.newInstance(this.mCode);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(addDietFragment, "searchAddDietFragment");
                transaction.commitAllowingStateLoss();
                return;
            default:
                return;
        }
    }

    @OnCheckedChanged({2131427669})
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        this.cbUnits.setText(isChecked ? R.string.im : R.string.il);
        changeUnits();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.b_);
        ButterKnife.inject((Activity) this);
        EventBus.getDefault().register(this);
        this.mCode = getIntent().getStringExtra("key_food_code");
        this.isRecord = getIntent().getBooleanExtra(KEY_IS_RECORD, false);
        initView();
        requestData();
    }

    private void initView() {
        this.lvMain.setFocusable(false);
        this.lvMain.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        this.mAdapter = new IngredientInfoAdapter(this, this.showList);
        this.lvMain.setAdapter(this.mAdapter);
        if (this.isRecord) {
            this.llRecord.setVisibility(0);
            this.vBottom.setVisibility(0);
        }
    }

    private void initData() {
        int i;
        setTitle(this.mFoodInfo.name);
        ImageLoader.getInstance().displayImage(this.mFoodInfo.thumb_image_url, this.ivFoodIcon,
                ImageLoaderOptions.global((int) R.drawable.aa2));
        boolean isShowCalory = OnePreference.isShowCaloryUnit();
        this.rbCalory.setChecked(isShowCalory);
        this.rbKjoule.setChecked(!isShowCalory);
        this.rgUnit.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FoodUtils.changeShowUnit();
                FoodDetailActivity.this.tvCalory.setText(FoodUtils.showUnitValue
                        (FoodDetailActivity.this.mFoodInfo.calory, true));
                FoodDetailActivity.this.tvWeight.setText(FoodUtils.changeUnitAndWeight
                        (FoodDetailActivity.this.mFoodInfo.weight, FoodDetailActivity.this
                                .mFoodInfo.is_liquid, true));
                FoodDetailActivity.this.changeUnitInDataList();
            }
        });
        this.tvFoodName.setText(this.mFoodInfo.name);
        this.tvCalory.setText(FoodUtils.showUnitValue(this.mFoodInfo.calory, true));
        this.tvWeight.setText(FoodUtils.changeUnitAndWeight(this.mFoodInfo.weight, this.mFoodInfo
                .is_liquid, true));
        FoodUtils.switchToLightWithText(this.mFoodInfo.health_light, this.ivHealthLight);
        if (TextUtils.isEmpty(this.mFoodInfo.appraise)) {
            this.tvAppraise.setText(getString(R.string.ig));
        } else {
            this.tvAppraise.setText(this.mFoodInfo.appraise);
        }
        LinearLayout linearLayout = this.llRecipe;
        if (OWN_RECIPE.equals(this.mFoodInfo.recipe_type) || DOUGUO_RECIPE.equals(this.mFoodInfo
                .recipe_type)) {
            i = 0;
        } else {
            i = 8;
        }
        linearLayout.setVisibility(i);
        initCalory();
        initIngredient();
        if (TextUtils.isEmpty(this.mFoodInfo.uploader)) {
            this.llUploader.setVisibility(8);
            return;
        }
        this.llUploader.setVisibility(0);
        this.tvUploader.setText(String.format(getString(R.string.it), new Object[]{this.mFoodInfo
                .uploader}));
    }

    private void initCalory() {
        boolean compare;
        if (this.mFoodInfo.compare == null || TextUtils.isEmpty(this.mFoodInfo.compare
                .target_name)) {
            compare = true;
        } else {
            compare = false;
        }
        boolean units;
        if (this.mFoodInfo.units == null || this.mFoodInfo.units.size() == 0) {
            units = true;
        } else {
            units = false;
        }
        if (compare && units) {
            this.llCompare.setVisibility(8);
            return;
        }
        initCompare();
        initUnits();
    }

    private void initCompare() {
        FoodCompare compare = this.mFoodInfo.compare;
        if (compare == null || TextUtils.isEmpty(compare.target_name)) {
            this.llCompareContent.setVisibility(8);
            return;
        }
        ImageLoader.getInstance().displayImage(compare.target_image_url, this.ivCompare,
                ImageLoaderOptions.global((int) R.drawable.aa2));
        this.tvCompateAmount.setText(compare.amount1);
        StringBuilder builder = new StringBuilder();
        builder.append(compare.amount0);
        builder.append(compare.unit0);
        builder.append(this.mFoodInfo.name);
        builder.append(" ≈ ");
        builder.append(compare.amount1);
        builder.append(compare.unit1);
        builder.append(compare.target_name);
        this.tvCompareInfo.setText(builder.toString());
    }

    private void initUnits() {
        List<FoodInfoUnit> units = this.mFoodInfo.units;
        if (units == null || units.size() <= 0) {
            this.llUnits.setVisibility(8);
            return;
        }
        int i;
        int size = units.size();
        LinearLayout linearLayout = this.llUnitsCheckbox;
        if (size > 2) {
            i = 0;
        } else {
            i = 8;
        }
        linearLayout.setVisibility(i);
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i2 = 0; i2 < size; i2++) {
            FoodInfoUnit unit = (FoodInfoUnit) units.get(i2);
            View item = inflater.inflate(R.layout.hy, null);
            ((TextView) item.findViewById(R.id.tv_unit)).setText(String.format(getString(R.string
                    .in), new Object[]{stringToInt(unit.unit)}));
            ((TextView) item.findViewById(R.id.tv_weight)).setText(String.format(getString(R
                    .string.io), new Object[]{stringToInt(unit.weight)}));
            ((TextView) item.findViewById(R.id.tv_calory)).setText(String.format(getString(R
                    .string.ik), new Object[]{stringToInt(unit.calory)}));
            if (i2 >= 2) {
                item.setVisibility(8);
            }
            this.llUnitsItem.addView(item);
        }
    }

    private String stringToInt(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        String result = str;
        if (result.contains(".")) {
            return str.substring(0, str.indexOf("."));
        }
        return result;
    }

    private void changeUnits() {
        int childCount = this.llUnitsItem.getChildCount();
        if (childCount <= 2) {
            this.llUnitsCheckbox.setVisibility(8);
            return;
        }
        boolean isShow = this.cbUnits.isChecked();
        for (int i = 2; i < childCount; i++) {
            View item = this.llUnitsItem.getChildAt(i);
            if (item != null) {
                int i2;
                if (isShow) {
                    i2 = 0;
                } else {
                    i2 = 8;
                }
                item.setVisibility(i2);
            }
        }
    }

    private void initIngredient() {
        FoodUtils.initIngredient(this.mDataList, this.mFoodInfo.ingredient);
        FoodUtils.setIngredientInfo(this.mDataList, this.mFoodInfo.lights);
        FoodUtils.sortIngredientInfo(this.mDataList);
        FoodUtils.filterIngredientInfo(this.showList, this.mDataList);
        this.mAdapter.notifyDataSetChanged();
    }

    private void requestData() {
        if (TextUtils.isEmpty(this.mCode)) {
            finish();
            return;
        }
        showLoading();
        FoodApi.getFoodDetail(this.mCode, this.activity, new JsonCallback(this.activity) {
            public void ok(JSONObject obj) {
                FoodDetailActivity.this.mFoodInfo = FoodInfo.parse(obj.toString());
                if (FoodDetailActivity.this.mFoodInfo != null) {
                    FoodDetailActivity.this.initData();
                    FoodDetailActivity.this.changeUnitInDataList();
                    FoodDetailActivity.this.isFavorite(FoodDetailActivity.this.mCode);
                    FoodDetailActivity.this.checkGuide();
                    return;
                }
                Helper.showLong((CharSequence) "Data error!");
            }

            public void onFinish() {
                FoodDetailActivity.this.dismissLoading();
            }
        });
    }

    private void checkGuide() {
        if (!OnePreference.isEstimateFoodGuide()) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    FoodDetailActivity.this.showHighLight();
                }
            }, 500);
        }
    }

    private void showHighLight() {
        HighLight highLight = new HighLight(this.activity).addHighLight((int) R.id
                .tv_how_assessment, (int) R.layout.p1, new OnPosCallback() {
            public void getPos(float rightMargin, float bottomMargin, RectF rectF, MarginInfo
                    marginInfo) {
                marginInfo.rightMargin = rectF.width() / 2.0f;
                marginInfo.bottomMargin = rectF.height() + bottomMargin;
            }
        });
        highLight.show();
        highLight.setOnHighLightClickListener(new OnHighLightClickListener() {
            public void onClick() {
                OnePreference.setEstimateFoodGuide(true);
            }
        });
    }

    private void isFavorite(String mCode) {
        showLoading();
        FoodApi.isFavorite(mCode, this.activity, new JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                if (object != null && object.has("status")) {
                    boolean whetherLiked = object.optBoolean("status");
                    if (FoodDetailActivity.this.mMenu != null) {
                        MenuItem item = FoodDetailActivity.this.mMenu.getItem(0);
                        if (whetherLiked) {
                            item.setTitle(R.string.ia);
                            item.setIcon(R.drawable.a3a);
                            return;
                        }
                        item.setTitle(R.string.is);
                        item.setIcon(R.drawable.a3b);
                    }
                }
            }

            public void onFinish() {
                super.onFinish();
                FoodDetailActivity.this.dismissLoading();
            }
        });
    }

    private void deleteFavorite(String code, final MenuItem item) {
        showLoading();
        item.setEnabled(false);
        FoodApi.deleteFavorite(code, this.activity, new JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                item.setTitle(R.string.is);
                item.setIcon(R.drawable.a3b);
                EventBus.getDefault().post(new FoodCollectEvent().setIsCollect(false));
                EventBus.getDefault().post(new MyFoodEvent().setFlag(0));
            }

            public void onFinish() {
                super.onFinish();
                FoodDetailActivity.this.dismissLoading();
                if (item != null) {
                    item.setEnabled(true);
                }
            }
        });
    }

    private void addFavorite(String code, final MenuItem item) {
        showLoading();
        item.setEnabled(false);
        FoodApi.addFavorite(code, this.activity, new JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                item.setTitle(R.string.ia);
                item.setIcon(R.drawable.a3a);
                EventBus.getDefault().post(new FoodCollectEvent().setIsCollect(true));
            }

            public void onFinish() {
                super.onFinish();
                FoodDetailActivity.this.dismissLoading();
                if (item != null) {
                    item.setEnabled(true);
                }
            }
        });
    }

    private void changeUnitInDataList() {
        for (IngredientInfo info : this.mDataList) {
            if (!FoodUtils.isShowCaloryUnit() || !getString(R.string.aao).equals(info.unit)) {
                if (!FoodUtils.isShowCaloryUnit() && getString(R.string.aak).equals(info.unit)) {
                    info.unit = getString(R.string.aao);
                    info.value = String.valueOf(FoodUtils.showUnitValue(this.mFoodInfo.calory,
                            true));
                    break;
                }
            } else {
                info.unit = getString(R.string.aak);
                info.value = String.valueOf(FoodUtils.showUnitValue(this.mFoodInfo.calory, true));
                break;
            }
        }
        this.mAdapter.notifyDataSetChanged();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        this.mMenu = menu;
        getMenuInflater().inflate(R.menu.k, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                onBackPressed();
                return true;
            case R.id.action_collection:
                if (getString(R.string.ia).equals(item.getTitle())) {
                    deleteFavorite(this.mCode, item);
                    return true;
                }
                addFavorite(this.mCode, item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static void comeOnBaby(Context context, String foodCode, boolean isRecord) {
        if (TextUtils.isEmpty(foodCode)) {
            Helper.showLong(context.getString(R.string.kg));
            return;
        }
        Intent intent = new Intent(context, FoodDetailActivity.class);
        intent.putExtra("key_food_code", foodCode);
        intent.putExtra(KEY_IS_RECORD, isRecord);
        context.startActivity(intent);
    }

    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (this.mDataList != null) {
            this.mDataList.clear();
        }
    }

    public void onEventMainThread(String result) {
    }
}
