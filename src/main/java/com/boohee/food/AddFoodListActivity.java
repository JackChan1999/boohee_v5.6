package com.boohee.food;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.api.FoodApi;
import com.boohee.database.OnePreference;
import com.boohee.database.UserPreference;
import com.boohee.main.GestureActivity;
import com.boohee.model.FoodInfo;
import com.boohee.model.RecordFood;
import com.boohee.model.RecordPhoto;
import com.boohee.modeldao.UserDao;
import com.boohee.myview.NewBadgeView;
import com.boohee.myview.highlight.HighLight;
import com.boohee.myview.highlight.HighLight.MarginInfo;
import com.boohee.myview.highlight.HighLight.OnHighLightClickListener;
import com.boohee.myview.highlight.HighLight.OnPosCallback;
import com.boohee.one.R;
import com.boohee.one.event.AddFinishAnimEvent;
import com.boohee.one.event.DietEvent;
import com.boohee.one.event.RefreshCalorieEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.ui.ScannerActivity;
import com.boohee.one.ui.adapter.ArrayPagerAdapter;
import com.boohee.one.ui.fragment.AddDietFragment;
import com.boohee.record.CommonFoodFragmennt;
import com.boohee.record.DietSportCalendarActivity;
import com.boohee.utility.Event;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utility.TimeLinePatterns;
import com.boohee.utils.DateFormatUtils;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.FoodUtils;
import com.boohee.utils.FormulaUtils;
import com.boohee.utils.ResolutionUtils;
import com.boohee.utils.ViewUtils;
import com.boohee.widgets.LightAlertDialog;
import com.boohee.widgets.PagerSlidingTabStrip;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

import org.json.JSONObject;

public class AddFoodListActivity extends GestureActivity {
    private static final String KEY_CALORIE   = "key_calorie";
    private static final String KEY_DATE      = "key_date";
    private static final String KEY_TIME_TYPE = "key_time_type";
    private static final int    REQUEST_IMAGE = 0;
    private int addCount;
    @InjectView(2131427471)
    ImageView iv_diet_cart;
    private float               mAddCalorie;
    private CommonFoodFragmennt mCommomFragment;
    private List<Fragment> mContentFragments = new ArrayList();
    private CookFoodFragment    mCookFoodFragment;
    private CustomFoodFragment  mCustomFoodFragmennt;
    private FavourFoodFragmennt mFavoriteFragment;
    private String              mImagePath;
    private NewBadgeView        mMessageBadge;
    @InjectView(2131427462)
    PagerSlidingTabStrip mSlidingTab;
    private float mTargetCalorie;
    private int   mTimeType;
    private String[] mTitles = new String[]{"常见", "收藏", "自定义", "我的上传", "我的菜肴"};
    private UploadFoodFragment mUploadFoodFragment;
    @InjectView(2131427463)
    ViewPager mViewPager;
    private String record_on;
    @InjectView(2131427466)
    View     rlCalorie;
    @InjectView(2131427464)
    View     rlCamera;
    @InjectView(2131427468)
    TextView tvCalorie;
    @InjectView(2131427470)
    TextView tvCalorieAdd;
    @InjectView(2131427467)
    TextView tvSuggest;

    @OnClick({2131427465, 2131429421, 2131429422})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_camera:
                showTakePhotoDialog();
                return;
            case R.id.iv_food_scan:
                ScannerActivity.startScannerForResult(this.activity, 175);
                return;
            case R.id.rl_food_search:
                SearchFoodActivity.start(this.ctx, this.mTimeType, this.record_on);
                return;
            default:
                return;
        }
    }

    public static void start(Context context, int time_type, String record_on, float addCalorie) {
        Intent starter = new Intent(context, AddFoodListActivity.class);
        starter.putExtra(KEY_TIME_TYPE, time_type);
        starter.putExtra("key_date", record_on);
        starter.putExtra(KEY_CALORIE, addCalorie);
        context.startActivity(starter);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aa);
        ButterKnife.inject((Activity) this);
        EventBus.getDefault().register(this);
        initParams();
        initViews();
        initFragments();
        initViewPager();
        checkGuide();
    }

    private void initParams() {
        this.mTimeType = getIntExtra(KEY_TIME_TYPE);
        this.record_on = getStringExtra("key_date");
        this.mAddCalorie = getIntent().getFloatExtra(KEY_CALORIE, 0.0f);
        this.mTargetCalorie = (float) new UserDao(this.ctx).queryWithToken(UserPreference
                .getToken(this.ctx)).target_calory;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.c, menu);
        new Handler().post(new Runnable() {
            public void run() {
                AddFoodListActivity.this.mMessageBadge = new NewBadgeView(AddFoodListActivity
                        .this.activity);
                AddFoodListActivity.this.mMessageBadge.setTargetView(AddFoodListActivity.this
                        .findViewById(R.id.action_finish));
                AddFoodListActivity.this.mMessageBadge.setBadgeMargin(10, 5, 5, 0);
                AddFoodListActivity.this.mMessageBadge.setBadgeGravity(53);
                AddFoodListActivity.this.mMessageBadge.setTextColor(AddFoodListActivity.this
                        .getResources().getColor(R.color.ju));
                AddFoodListActivity.this.mMessageBadge.setBackground(10, ContextCompat.getColor
                        (AddFoodListActivity.this.ctx, R.color.he));
            }
        });
        return true;
    }

    private void checkGuide() {
        if (!OnePreference.isAddFoodGuide()) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    AddFoodListActivity.this.showHighLight();
                }
            }, 500);
        }
    }

    private void showHighLight() {
        try {
            HighLight highLight = new HighLight(this.activity).addHighLight((int) R.id
                    .iv_food_scan, (int) R.layout.o4, new OnPosCallback() {
                public void getPos(float rightMargin, float bottomMargin, RectF rectF, MarginInfo
                        marginInfo) {
                    marginInfo.rightMargin = (rectF.width() / 2.0f) + rightMargin;
                    marginInfo.bottomMargin = bottomMargin - ((float) ViewUtils.dip2px
                            (AddFoodListActivity.this.activity, 40.0f));
                }
            });
            highLight.show();
            highLight.setOnHighLightClickListener(new OnHighLightClickListener() {
                public void onClick() {
                    OnePreference.setAddFoodGuide(true);
                }
            });
        } catch (Exception e) {
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_finish:
                startActivity(new Intent(this.ctx, DietSportCalendarActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initViews() {
        setTitle("添加" + FoodUtils.getDietName(this.ctx, this.mTimeType));
        if (DateFormatUtils.isFuture(this.record_on)) {
            this.rlCamera.setVisibility(8);
            this.rlCalorie.setVisibility(0);
            updateCalorieView(this.mTargetCalorie, this.mTimeType, this.mAddCalorie);
            return;
        }
        this.rlCamera.setVisibility(0);
        this.rlCalorie.setVisibility(8);
    }

    private void initFragments() {
        this.mCommomFragment = CommonFoodFragmennt.newInstance(this.mTimeType, this.record_on);
        this.mContentFragments.add(this.mCommomFragment);
        this.mFavoriteFragment = FavourFoodFragmennt.newInstance(this.mTimeType, this.record_on);
        this.mContentFragments.add(this.mFavoriteFragment);
        this.mCustomFoodFragmennt = CustomFoodFragment.newInstance(this.mTimeType, this.record_on);
        this.mContentFragments.add(this.mCustomFoodFragmennt);
        this.mUploadFoodFragment = UploadFoodFragment.newInstance(this.mTimeType, this.record_on);
        this.mContentFragments.add(this.mUploadFoodFragment);
        this.mCookFoodFragment = CookFoodFragment.newInstance(this.mTimeType, this.record_on);
        this.mContentFragments.add(this.mCookFoodFragment);
    }

    private void initViewPager() {
        this.mViewPager.setOffscreenPageLimit(5);
        this.mViewPager.setAdapter(new ArrayPagerAdapter(getSupportFragmentManager(), this
                .mContentFragments, this.mTitles));
        this.mSlidingTab.setViewPager(this.mViewPager);
        this.mSlidingTab.setOnPageChangeListener(new SimpleOnPageChangeListener() {
            public void onPageSelected(int position) {
                if (position == 1 && AddFoodListActivity.this.mFavoriteFragment.isFirstLoad) {
                    MobclickAgent.onEvent(AddFoodListActivity.this.ctx, Event
                            .TOOL_FOODANDSPORT_FAVORITETAB);
                    AddFoodListActivity.this.mFavoriteFragment.firstLoad();
                } else if (position == 2 && AddFoodListActivity.this.mCustomFoodFragmennt
                        .isFirstLoad) {
                    MobclickAgent.onEvent(AddFoodListActivity.this.ctx, Event
                            .TOOL_FOODANDSPORT_MINETAB);
                    AddFoodListActivity.this.mCustomFoodFragmennt.firstLoad();
                } else if (position == 3 && AddFoodListActivity.this.mUploadFoodFragment
                        .isFirstLoad) {
                    AddFoodListActivity.this.mUploadFoodFragment.firstLoad();
                } else if (position == 4 && AddFoodListActivity.this.mCookFoodFragment
                        .isFirstLoad) {
                    AddFoodListActivity.this.mCookFoodFragment.firstLoad();
                }
            }
        });
    }

    private void updateCalorieView(float targetCalorie, int timeType, float addCalorie) {
        if (this.mTimeType == 6 || this.mTimeType == 7 || this.mTimeType == 8) {
            this.rlCalorie.setVisibility(8);
            return;
        }
        this.tvSuggest.setText(FoodUtils.getDietName(this, timeType) + "建议");
        this.tvCalorieAdd.setText(Math.round(addCalorie) + " 千卡");
        int[] limit = FormulaUtils.calorieLimit(targetCalorie, timeType);
        this.tvCalorie.setText(String.format(Locale.getDefault(), "%d ~ %d 千卡", new
                Object[]{Integer.valueOf(limit[0]), Integer.valueOf(limit[1])}));
        if (addCalorie > ((float) limit[1])) {
            this.tvCalorieAdd.setTextColor(ContextCompat.getColor(this.ctx, R.color.he));
        } else {
            this.tvCalorieAdd.setTextColor(ContextCompat.getColor(this.ctx, R.color.e4));
        }
    }

    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void setTranslateAnim(int total) {
        this.iv_diet_cart.setVisibility(0);
        int width = ResolutionUtils.getScreenWidth(this.ctx);
        float translationX = (((float) width) / 2.0f) - ((float) ViewUtils.dip2px(this.activity,
                2.0f));
        float translationY = (float) (-((ResolutionUtils.getScreenHeight(this.ctx) / 2) -
                ViewUtils.dip2px(this.activity, 24.0f)));
        float[] fArr = new float[2];
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(this.iv_diet_cart, "scaleX", new float[]{0
                .0f, 2.0f});
        fArr = new float[2];
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(this.iv_diet_cart, "scaleY", new float[]{0
                .0f, 2.0f});
        fArr = new float[2];
        ObjectAnimator scaleX2 = ObjectAnimator.ofFloat(this.iv_diet_cart, "scaleX", new
                float[]{2.0f, 0.5f});
        fArr = new float[2];
        ObjectAnimator scaleY2 = ObjectAnimator.ofFloat(this.iv_diet_cart, "scaleY", new
                float[]{2.0f, 0.5f});
        ObjectAnimator.ofFloat(this.iv_diet_cart, "translationX", new float[]{0.0f,
                translationX}).setInterpolator(new LinearInterpolator());
        ObjectAnimator.ofFloat(this.iv_diet_cart, "translationY", new float[]{0.0f,
                translationY}).setInterpolator(new DecelerateInterpolator());
        AnimatorSet set1 = new AnimatorSet();
        set1.setInterpolator(new OvershootInterpolator());
        set1.playTogether(new Animator[]{scaleX, scaleY});
        set1.setDuration(500);
        set1.start();
        final AnimatorSet set2 = new AnimatorSet();
        set2.playTogether(new Animator[]{transX, transY, scaleX2, scaleY2});
        set2.setDuration(600);
        set1.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                set2.start();
            }
        });
        final int i = total;
        set2.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                int i = 8;
                AddFoodListActivity.this.iv_diet_cart.setVisibility(8);
                AddFoodListActivity.this.iv_diet_cart.animate().translationX(0.0f).translationY(0
                .0f).scaleX(1.0f).scaleY(1.0f);
                AddFoodListActivity.this.mMessageBadge.setBadgeCount(i);
                NewBadgeView access$000 = AddFoodListActivity.this.mMessageBadge;
                if (i > 0) {
                    i = 0;
                }
                access$000.setVisibility(i);
            }
        });
    }

    public void onEventMainThread(AddFinishAnimEvent event) {
        if (TextUtils.isEmpty(event.getThumb_image_name())) {
            ImageLoader.getInstance().displayImage("", this.iv_diet_cart, ImageLoaderOptions
                    .global((int) R.drawable.aa2));
        } else if (event.getThumb_image_name().contains("storage")) {
            ImageLoader.getInstance().displayImage("file://" + event.getThumb_image_name(), this
                    .iv_diet_cart, ImageLoaderOptions.global((int) R.drawable.aa2));
        } else {
            ImageLoader.getInstance().displayImage(TimeLinePatterns.WEB_SCHEME + event
                    .getThumb_image_name(), this.iv_diet_cart, ImageLoaderOptions.global((int) R
                    .drawable.aa2));
        }
        int i = this.addCount + 1;
        this.addCount = i;
        setTranslateAnim(i);
    }

    public void onEventMainThread(DietEvent event) {
        if (this.rlCalorie.getVisibility() == 0 && event.getEditType() == 1 && event.getTimeType
                () == this.mTimeType) {
            this.mAddCalorie += event.getRecordFood().calory;
            updateCalorieView(this.mTargetCalorie, this.mTimeType, this.mAddCalorie);
        }
    }

    public void onEventMainThread(RefreshCalorieEvent event) {
        List<RecordFood> recordFoods = event.recordFoods;
        List<RecordPhoto> recordPhotos = event.recordPhotos;
        int totalCalorie = 0;
        for (RecordFood record : recordFoods) {
            if (record.time_type == this.mTimeType) {
                totalCalorie = (int) (((float) totalCalorie) + record.calory);
            }
        }
        for (RecordPhoto record2 : recordPhotos) {
            if (record2.time_type == this.mTimeType) {
                totalCalorie = (int) (((float) totalCalorie) + record2.calory);
            }
        }
        this.mAddCalorie = (float) totalCalorie;
        updateCalorieView(this.mTargetCalorie, this.mTimeType, this.mAddCalorie);
    }

    private void showTakePhotoDialog() {
        Intent intent = new Intent(this.activity, MultiImageSelectorActivity.class);
        intent.putExtra("show_camera", true);
        intent.putExtra("max_select_count", 1);
        intent.putExtra("select_count_mode", 0);
        startActivityForResult(intent, 0);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            switch (requestCode) {
                case 0:
                    List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity
                            .EXTRA_RESULT);
                    if (path != null && path.size() > 0) {
                        this.mImagePath = (String) path.get(0);
                        AddCameraRecordActivity.start(this.activity, this.mTimeType, this
                                .record_on, this.mImagePath);
                        break;
                    }
                case 175:
                    String contents = data.getStringExtra(ScannerActivity.CODE_DATA);
                    if (contents != null) {
                        searchFoodWithCode(contents);
                        break;
                    }
                    break;
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void searchFoodWithCode(final String code) {
        if (!TextUtils.isEmpty(code)) {
            FoodApi.getFoodWithBarcode(code, this.activity, new JsonCallback(this.activity) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    List<FoodInfo> foods = FastJsonUtils.parseList(object.optString("foods"),
                            FoodInfo.class);
                    if (foods == null || foods.size() == 0) {
                        AddFoodListActivity.this.showUploadDialog(code);
                        return;
                    }
                    FoodInfo foodInfo = (FoodInfo) foods.get(0);
                    if (TextUtils.isEmpty(foodInfo.code)) {
                        AddFoodListActivity.this.showUploadDialog(code);
                    } else {
                        AddFoodListActivity.this.loadFoodWithCode(foodInfo.code);
                    }
                }
            });
        }
    }

    private void loadFoodWithCode(String code) {
        AddDietFragment.newInstance(this.mTimeType, this.record_on, code).show
                (getSupportFragmentManager(), "addDietFragment");
    }

    private void showUploadDialog(final String code) {
        LightAlertDialog.create(this.ctx, (int) R.string.ab8).setNegativeButton((int) R.string
                .ab9, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setPositiveButton((int) R.string.ab_, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                MobclickAgent.onEvent(AddFoodListActivity.this.ctx, Event
                        .tool_searchfood_assistadd);
                UploadFoodActivity.comeOnBabyWithCode(AddFoodListActivity.this.ctx, code);
            }
        }).show();
    }
}
