package com.boohee.one.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.alibaba.fastjson.TypeReference;
import com.boohee.account.NewUserInitActivity;
import com.boohee.api.ApiUrls;
import com.boohee.api.RecordApi;
import com.boohee.api.StatusApi;
import com.boohee.database.OnePreference;
import com.boohee.database.StepsPreference;
import com.boohee.database.UserPreference;
import com.boohee.model.BindBand;
import com.boohee.model.LocalCalorieDistribution;
import com.boohee.model.LocalWeightRecord;
import com.boohee.model.ModelName;
import com.boohee.model.User;
import com.boohee.model.home.Home;
import com.boohee.model.mine.Measure.MeasureType;
import com.boohee.model.mine.WeightRecord;
import com.boohee.modeldao.UserDao;
import com.boohee.myview.ArcProgressView;
import com.boohee.myview.DietRecordBar;
import com.boohee.myview.HomePopView;
import com.boohee.myview.IntFloatWheelView;
import com.boohee.myview.LineGraph;
import com.boohee.myview.LineGraph.Point;
import com.boohee.myview.ProgressLine;
import com.boohee.myview.PullToZoomScrollView;
import com.boohee.myview.PullToZoomScrollView.OnPullToZoomListener;
import com.boohee.myview.highlight.HighLight;
import com.boohee.myview.highlight.HighLight.MarginInfo;
import com.boohee.myview.highlight.HighLight.OnHighLightClickListener;
import com.boohee.myview.highlight.HighLight.OnPosCallback;
import com.boohee.one.BuildConfig;
import com.boohee.one.R;
import com.boohee.one.cache.CacheKey;
import com.boohee.one.event.BandTypeEvent;
import com.boohee.one.event.CanCaloryEvent;
import com.boohee.one.event.LatestWeightEvent;
import com.boohee.one.event.MeasureEvent;
import com.boohee.one.event.PeriodEvent;
import com.boohee.one.event.RefreshWeightEvent;
import com.boohee.one.event.UserIntEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.one.pedometer.StepModel;
import com.boohee.one.pedometer.manager.AbstractStepManager;
import com.boohee.one.pedometer.manager.StepListener;
import com.boohee.one.pedometer.manager.StepManagerFactory;
import com.boohee.one.pedometer.v2.StepMainActivity;
import com.boohee.one.ui.DiamondSignActivity;
import com.boohee.one.ui.JumpBrowserActivity;
import com.boohee.one.ui.PeriodCalendarActivity;
import com.boohee.record.DietSportCalendarActivity;
import com.boohee.record.DimensionRecordActivity;
import com.boohee.record.WeightRecordActivity;
import com.boohee.utility.Event;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.AccountUtils;
import com.boohee.utils.AccountUtils.OnGetUserProfile;
import com.boohee.utils.DateHelper;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.FormulaUtils;
import com.boohee.utils.Helper;
import com.boohee.utils.NumberUtils;
import com.boohee.utils.ViewUtils;
import com.boohee.utils.WheelUtils;
import com.boohee.widgets.BooheeRippleLayout;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeNewFragment extends BaseFragment implements StepListener {
    public static final String HOME_ITEMS             = "/api/v1/home/items";
    public static final String REFRESH_ONE_KEY_STATUS = "refresh_one_key_status";
    public static final String TAG                    = HomeNewFragment.class.getSimpleName();
    @InjectView(2131428227)
    ArcProgressView arcProgress;
    private List<BindBand> bandList;
    @InjectView(2131428257)
    BooheeRippleLayout brlCheckinStatus;
    private JsonCallback callback;
    private int          checkInCount;
    private DecimalFormat dFormat = new DecimalFormat(BuildConfig.ONE_PATCH_VERSION);
    @InjectView(2131428240)
    DietRecordBar dietBar;
    @InjectView(2131428253)
    View          dividerPeriod;
    private Home home;
    HomePopView homePopView;
    private boolean isCheckin = false;
    private boolean isCurrentRunningForeground;
    private boolean isFirstLoad = true;
    @InjectView(2131427735)
    ImageView            ivTop;
    @InjectView(2131428224)
    PullToZoomScrollView layoutPullDown;
    @InjectView(2131428225)
    RelativeLayout       layoutTop;
    private String         loadDataTime;
    private ObjectAnimator mAnimator;
    private int[] mCheckIn = new int[]{R.id.cb_checkin_0, R.id.cb_checkin_1, R.id.cb_checkin_2, R
            .id.cb_checkin_3, R.id.cb_checkin_4, R.id.cb_checkin_5, R.id.cb_checkin_6};
    private View mParentView;
    private User mUser;
    @InjectView(2131428249)
    ProgressLine       progressStep;
    @InjectView(2131428232)
    RelativeLayout     rlCheckIn;
    @InjectView(2131428254)
    View               rlPeriod;
    @InjectView(2131428226)
    View               rlProgress;
    @InjectView(2131428246)
    View               rlStep;
    @InjectView(2131428231)
    BooheeRippleLayout rpCheckIn;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private AbstractStepManager stepManager;
    @InjectView(2131428239)
    TextView  tvCalorie;
    @InjectView(2131428238)
    TextView  tvCalorieStatus;
    @InjectView(2131428234)
    TextView  tvCheckIn;
    @InjectView(2131428262)
    TextView  tvCheckInStatusAlert;
    @InjectView(2131428228)
    TextView  tvDes;
    @InjectView(2131428244)
    TextView  tvLastWeight;
    @InjectView(2131428256)
    TextView  tvPeroidDistance;
    @InjectView(2131427747)
    TextView  tvProgress;
    @InjectView(2131427944)
    TextView  tvStep;
    @InjectView(2131427945)
    TextView  tvTargetStep;
    @InjectView(2131427845)
    TextView  tvTips;
    @InjectView(2131427651)
    TextView  tvWeight;
    @InjectView(2131428270)
    View      viewHeaderBG;
    @InjectView(2131428245)
    LineGraph weightGraph;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        this.mParentView = inflater.inflate(R.layout.fy, container, false);
        ButterKnife.inject((Object) this, this.mParentView);
        return this.mParentView;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        initView();
        requestData();
    }

    private void checkGuide() {
        if (!OnePreference.isHomeMyPlanGuide()) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    HomeNewFragment.this.showHighLight();
                }
            }, 500);
        }
    }

    private void showHighLight() {
        try {
            HighLight highLight = new HighLight(getActivity()).addHighLight((int) R.id.tv_plan,
                    (int) R.layout.pc, new OnPosCallback() {
                public void getPos(float rightMargin, float bottomMargin, RectF rectF, MarginInfo
                        marginInfo) {
                    marginInfo.topMargin = rectF.bottom + ((float) ViewUtils.dip2px
                            (HomeNewFragment.this.getActivity(), 16.0f));
                }
            });
            highLight.show();
            highLight.setOnHighLightClickListener(new OnHighLightClickListener() {
                public void onClick() {
                    OnePreference.setHomeMyPlanGuide(true);
                }
            });
        } catch (Exception e) {
        }
    }

    private void initView() {
        showLoading();
        initDataCallBack();
        this.tvCheckIn.setEnabled(false);
        this.layoutPullDown.setOnPullToZoomListener(new OnPullToZoomListener() {
            public void onPull() {
                MobclickAgent.onEvent(HomeNewFragment.this.getActivity(), Event.HOME_OPEN_PICTURE);
                try {
                    HomeNewFragment.this.tvTips.setText("");
                    if (HomeNewFragment.this.homePopView != null && !HomeNewFragment.this
                            .homePopView.isAdded() && HomeNewFragment.this.home != null) {
                        HomeNewFragment.this.homePopView.show(HomeNewFragment.this
                                .getChildFragmentManager(), "showPop");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onPreparePull() {
                HomeNewFragment.this.tvTips.setText(R.string.a8g);
            }

            public void onCancelPull() {
                HomeNewFragment.this.tvTips.setText("");
            }

            public void onStartDrag() {
                HomeNewFragment.this.tvTips.setText(R.string.a8h);
            }

            public void onScrollChanged(int vertical) {
                if (vertical <= 16) {
                    HomeNewFragment.this.viewHeaderBG.setBackgroundColor(Color.parseColor
                            ("#00000000"));
                    return;
                }
                if (vertical > 100) {
                    vertical = 100;
                }
                HomeNewFragment.this.viewHeaderBG.setBackgroundColor(Color.parseColor(String
                        .format("#%s000000", new Object[]{Integer.toHexString(vertical)})));
            }
        });
    }

    private void requestData() {
        loadUserData();
        requestCheckIn();
        requestStepManagerData();
    }

    private void initMc() {
        if (this.mUser != null && !this.mUser.isMale()) {
            RecordApi.getMcPeriodsLatest(getActivity(), new JsonCallback(getActivity()) {
                public void ok(JSONObject object) {
                    JSONObject mcLatest = object.optJSONObject("mc_latest");
                    if (mcLatest != null) {
                        int latest = mcLatest.optInt("mc_distance");
                        HomeNewFragment.this.tvPeroidDistance.setText(String.format("还有 %d 天",
                                new Object[]{Integer.valueOf(latest)}));
                    }
                }
            });
        }
    }

    private void requestStepManagerData() {
        BooheeClient.build("record").get(RecordApi.URL_BINDS, new JsonCallback(getActivity()) {
            public void ok(JSONObject object) {
                super.ok(object);
                HomeNewFragment.this.mCache.put(CacheKey.BAND_TYPE, object);
            }

            public void onFinish() {
                super.onFinish();
                JSONObject object = HomeNewFragment.this.mCache.getAsJSONObject(CacheKey.BAND_TYPE);
                String type = StepManagerFactory.STEP_TYPE_PEDOMETER;
                if (object != null) {
                    try {
                        HomeNewFragment.this.bandList = FastJsonUtils.parseList(object.getString
                                ("bands"), BindBand.class);
                        for (BindBand band : HomeNewFragment.this.bandList) {
                            if (TextUtils.equals(band.provider, StepManagerFactory
                                    .STEP_TYPE_CLING) && band.is_bind) {
                                type = band.provider;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                HomeNewFragment.this.initStepManager(type);
                HomeNewFragment.this.refreshStepsView();
            }
        }, getActivity());
    }

    private void loadUserData() {
        if (getActivity() != null) {
            AccountUtils.getUserProfile(getActivity(), new OnGetUserProfile() {
                public void onGetUserProfile(User user) {
                    if (HomeNewFragment.this.getActivity() != null) {
                        HomeNewFragment.this.mUser = user;
                        if (HomeNewFragment.this.mUser == null || !HomeNewFragment.this.mUser
                                .hasProfile()) {
                            HomeNewFragment.this.startActivity(new Intent(HomeNewFragment.this
                                    .getActivity(), NewUserInitActivity.class));
                        } else {
                            HomeNewFragment.this.initMc();
                        }
                    }
                }

                public void onGetUserProfileFinish() {
                    if (HomeNewFragment.this.mUser == null) {
                        HomeNewFragment.this.mUser = new UserDao(HomeNewFragment.this.getActivity
                                ()).queryWithToken(UserPreference.getToken(HomeNewFragment.this
                                .getActivity()));
                    }
                    if (!HomeNewFragment.this.isRemoved()) {
                        HomeNewFragment.this.requestHomeData();
                        HomeNewFragment.this.refreshPeriod();
                        HomeNewFragment.this.refreshWeightRecord();
                        HomeNewFragment.this.refreshMeasureRecord();
                        HomeNewFragment.this.refreshCaloryRecord();
                        HomeNewFragment.this.refreshWeightProgress();
                    }
                }
            });
        }
    }

    private void refreshPeriod() {
        if (this.mUser != null && !isRemoved()) {
            if (this.mUser.isMale()) {
                this.rlPeriod.setVisibility(8);
                this.dividerPeriod.setVisibility(8);
                return;
            }
            this.rlPeriod.setVisibility(0);
            this.dividerPeriod.setVisibility(0);
        }
    }

    private void refreshWeightRecord() {
        if (!isRemoved()) {
            this.tvLastWeight.setText(NumberUtils.safeToString(this.dFormat, (double)
                    getLatestWeight()));
            refreshWeightGraph((List) FastJsonUtils.fromJson(this.mCache.getAsString(CacheKey
                    .HOME_RECENT_WEIGHT), new TypeReference<List<WeightRecord>>() {
            }));
            RecordApi.getRecentWeight(getActivity(), new JsonCallback(getActivity()) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    if (object != null && !HomeNewFragment.this.isRemoved()) {
                        List<WeightRecord> records = FastJsonUtils.parseList(object.optString
                                ("recent"), WeightRecord.class);
                        HomeNewFragment.this.saveRecentWeight(records);
                        HomeNewFragment.this.refreshWeightGraph(records);
                    }
                }
            });
        }
    }

    private void saveRecentWeight(List<WeightRecord> records) {
        if (records != null && records.size() > 0) {
            WeightRecord latestRecord = (WeightRecord) records.get(0);
            LocalWeightRecord weightRecord = new LocalWeightRecord(latestRecord.weight,
                    latestRecord.record_on);
            if (this.mUser != null) {
                this.mUser.latest_weight_at = latestRecord.record_on;
                this.mUser.latest_weight = Float.parseFloat(latestRecord.weight);
            }
            this.tvLastWeight.setText(latestRecord.weight);
            this.mCache.put(CacheKey.HOME_RECENT_WEIGHT, FastJsonUtils.toJson(records));
            this.mCache.put("latest_weight", FastJsonUtils.toJson(weightRecord));
            OnePreference.setLatestWeight(Float.parseFloat(latestRecord.weight));
            refreshWeightProgress();
        }
    }

    private void refreshWeightGraph(List<WeightRecord> records) {
        if (!isRemoved()) {
            List<Point> points = new ArrayList();
            if (records != null) {
                for (WeightRecord record : records) {
                    points.add(0, new Point(NumberUtils.safeParseFloat(record.weight), false));
                    if (points.size() >= 7) {
                        break;
                    }
                }
            }
            int currentSize = points.size();
            if (points.size() < 7) {
                Point fakePoint;
                if (points.size() > 0) {
                    fakePoint = new Point(((Point) points.get(0)).value, true);
                } else {
                    fakePoint = new Point(IntFloatWheelView.DEFAULT_VALUE, true);
                }
                for (int i = 0; i < 7 - currentSize; i++) {
                    points.add(0, fakePoint);
                }
            }
            this.weightGraph.setPoints(points);
        }
    }

    private void refreshMeasureRecord() {
    }

    private void refreshCaloryRecord() {
        if (this.mUser != null && !isRemoved()) {
            LocalCalorieDistribution distribution = (LocalCalorieDistribution) FastJsonUtils
                    .fromJson(this.mCache.getAsString(CacheKey.HOME_CALORIE_DISTRUBUTION),
                            LocalCalorieDistribution.class);
            if (distribution == null || !DateHelper.today().equals(distribution.record_on)) {
                this.dietBar.showEmpty();
                this.tvCalorie.setText(String.valueOf(this.mUser.target_calory));
                return;
            }
            int sub = (int) FormulaUtils.needCalorie((float) this.mUser.target_calory, (
                    (distribution.breakfastCalory + distribution.lunchCalory) + distribution
                            .dinnerCalory) + distribution.snackCalory, distribution.sportCalory);
            this.tvCalorie.setText(String.valueOf(Math.abs(sub)));
            if (sub >= 0) {
                this.tvCalorieStatus.setText("还可以吃");
            } else {
                this.tvCalorieStatus.setText("多吃了");
            }
            this.dietBar.setData(distribution, this.mUser);
        }
    }

    @OnClick({2131428234, 2131428258, 2131428236, 2131428242, 2131428251, 2131428254, 2131427940,
            2131427616})
    public void onClick(View v) {
        if (!isDetached() && !WheelUtils.isFastDoubleClick() && getActivity() != null) {
            switch (v.getId()) {
                case R.id.tv_plan:
                    MobclickAgent.onEvent(getActivity(), Event.MINE_CLICKHEALTHREPORT);
                    JumpBrowserActivity.comeOnBaby(getActivity(), getString(R.string.t_),
                            BooheeClient.build(BooheeClient.BINGO).getDefaultURL(ApiUrls
                                    .REPORT_URL));
                    return;
                case R.id.rl_step:
                    MobclickAgent.onEvent(getActivity(), Event.bingo_clickSteps);
                    startActivity(new Intent(getActivity(), StepMainActivity.class));
                    return;
                case R.id.tv_check_in:
                case R.id.rl_checkin_status:
                    if (this.isCheckin) {
                        MobclickAgent.onEvent(getActivity(), Event.HOME_CHECK_IN);
                        DiamondSignActivity.comeOnBaby(getActivity());
                        return;
                    }
                    MobclickAgent.onEvent(getActivity(), Event.HOME_CHECK_IN_CLICK);
                    this.tvCheckIn.setEnabled(false);
                    postCheckIn();
                    return;
                case R.id.rl_diet_sport:
                    MobclickAgent.onEvent(getActivity(), Event.TOOL_FOOD_AND_SPORT);
                    User user = new UserDao(getActivity()).queryWithToken(UserPreference.getToken
                            (getActivity()));
                    if (user == null || !user.hasProfile()) {
                        new Builder(getActivity()).setMessage(R.string.a9q).setPositiveButton(R
                                .string.a9s, new OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                HomeNewFragment.this.startActivity(new Intent(HomeNewFragment
                                        .this.getActivity(), NewUserInitActivity.class));
                            }
                        }).setNegativeButton(R.string.a9r, null).show();
                        return;
                    } else {
                        startActivity(new Intent(getActivity(), DietSportCalendarActivity.class));
                        return;
                    }
                case R.id.rl_weight:
                    MobclickAgent.onEvent(getActivity(), Event.bingo_clickWeight);
                    startActivity(new Intent(getActivity(), WeightRecordActivity.class).putExtra
                            ("key_record_type", MeasureType.WEIGHT.getType()));
                    return;
                case R.id.rl_measure:
                    MobclickAgent.onEvent(getActivity(), Event.TOOL_GRIRTH);
                    startActivity(new Intent(getActivity(), DimensionRecordActivity.class)
                            .putExtra("key_record_type", MeasureType.WAIST.getType()));
                    return;
                case R.id.rl_peroid:
                    MobclickAgent.onEvent(getActivity(), Event.TOOL_PERIED);
                    startActivity(new Intent(getActivity(), PeriodCalendarActivity.class));
                    return;
                default:
                    return;
            }
        }
    }

    private void requestHomeData() {
        BooheeClient.build(BooheeClient.BINGO).get(HOME_ITEMS, this.callback, getActivity());
    }

    private void requestCheckIn() {
        StatusApi.getCheckIn(getActivity(), new JsonCallback(getActivity()) {
            public void ok(JSONObject object) {
                super.ok(object);
                HomeNewFragment.this.isCheckin = object.optBoolean("checked");
                HomeNewFragment.this.checkInCount = object.optInt("checkin_days");
            }

            public void onFinish() {
                super.onFinish();
                if (HomeNewFragment.this.getActivity() != null && !HomeNewFragment.this
                        .isDetached()) {
                    HomeNewFragment.this.refreshCheckInView();
                }
            }
        });
    }

    private void postCheckIn() {
        StatusApi.checkIn(getActivity(), new JsonCallback(getActivity()) {
            public void ok(JSONObject object) {
                super.ok(object);
                CharSequence message = object.optString("message");
                if (!TextUtils.isEmpty(message)) {
                    Helper.showToast(message);
                }
                OnePreference.setPrefSignRecord();
                HomeNewFragment.this.checkInCount = object.optInt("checkin_days");
                HomeNewFragment.this.isCheckin = object.optBoolean("checked");
                HomeNewFragment.this.setTranslateAnim();
            }

            public void ok(JSONObject object, boolean hasError) {
                super.ok(object, hasError);
                JSONArray array = object.optJSONArray("errors");
                if (array != null) {
                    JSONObject error = array.optJSONObject(0);
                    if (error != null && !TextUtils.isEmpty(error.optString("messages"))) {
                        Helper.showToast(error.optString("messages"));
                        HomeNewFragment.this.requestCheckIn();
                    }
                }
            }

            public void onFinish() {
                super.onFinish();
            }
        });
    }

    private void getCheckInWeek() {
        if (this.isCheckin) {
            this.rpCheckIn.setVisibility(8);
            this.brlCheckinStatus.setVisibility(0);
            StatusApi.getCheckInWeek(getActivity(), new JsonCallback(getActivity()) {
                public void ok(JSONObject object) {
                    try {
                        JSONArray weekly = object.optJSONArray("weekly");
                        for (int i = 0; i < weekly.length(); i++) {
                            TextView item = (TextView) HomeNewFragment.this.mParentView
                                    .findViewById(HomeNewFragment.this.mCheckIn[i]);
                            if (((Boolean) weekly.get(i)).booleanValue()) {
                                item.setTextColor(HomeNewFragment.this.getResources().getColor(R
                                        .color.dn));
                                item.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable
                                        .a2b);
                            } else {
                                item.setTextColor(HomeNewFragment.this.getResources().getColor(R
                                        .color.e4));
                                item.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable
                                        .a2c);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                public void onFinish() {
                }
            });
            return;
        }
        this.rpCheckIn.setVisibility(0);
        this.brlCheckinStatus.setVisibility(8);
    }

    private void refreshCheckInView() {
        this.tvCheckIn.setEnabled(true);
        if (this.isCheckin) {
            this.rpCheckIn.setVisibility(8);
            this.brlCheckinStatus.setVisibility(0);
            this.tvCheckInStatusAlert.setText(String.format("已坚持 %d 天", new Object[]{Integer
                    .valueOf(this.checkInCount)}));
        } else {
            this.rpCheckIn.setVisibility(0);
            this.brlCheckinStatus.setVisibility(8);
        }
        getCheckInWeek();
    }

    private void initDataCallBack() {
        this.callback = new JsonCallback(getActivity()) {
            public void onFinish() {
                super.onFinish();
                HomeNewFragment.this.dismissLoading();
            }

            public void fail(String message) {
                HomeNewFragment.this.handleHomeData(HomeNewFragment.this.mCache.getAsJSONObject
                        (CacheKey.NEW_HOME));
            }

            public void ok(JSONObject object) {
                super.ok(object);
                try {
                    HomeNewFragment.this.handleHomeData(object);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void handleHomeData(JSONObject object) {
        if (object != null) {
            try {
                this.home = (Home) FastJsonUtils.fromJson(object, Home.class);
                this.mCache.put(CacheKey.NEW_HOME, object);
                this.loadDataTime = this.home.welcome_img.date;
                refreshWallpaper();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void refreshWallpaper() {
        if (this.home != null && !isRemoved()) {
            try {
                this.imageLoader.displayImage(this.home.welcome_img.back_img_small, this.ivTop,
                        ImageLoaderOptions.custom(R.drawable.wr));
                this.homePopView = HomePopView.newInstance(this.home.welcome_img.week_images);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onEvent(String event) {
        if (TextUtils.equals(event, REFRESH_ONE_KEY_STATUS)) {
            requestData();
        }
    }

    public void onEventMainThread(UserIntEvent mUserIntEvent) {
        loadUserData();
    }

    public void onEventMainThread(PeriodEvent periodEvent) {
        refreshPeriod();
    }

    public void onEventMainThread(CanCaloryEvent calory) {
        OnePreference.getInstance(getActivity()).setCanCalory(calory.getCalory());
    }

    public void onEventMainThread(LatestWeightEvent latestWeightEvent) {
        refreshWeightProgress();
        refreshWeightRecord();
    }

    public void onEventMainThread(RefreshWeightEvent refreshWeightEvent) {
        refreshWeightProgress();
        refreshWeightRecord();
    }

    public void onEventMainThread(MeasureEvent measureEvent) {
        refreshMeasureRecord();
    }

    public void onEventMainThread(BandTypeEvent event) {
        if (!isRemoved()) {
            this.stepManager = StepManagerFactory.getInstance().changeStepManager(getActivity(),
                    event, this.stepManager);
            this.stepManager.setListener(this);
            refreshStepsView();
        }
    }

    public void onResume() {
        super.onResume();
        if (this.isFirstLoad) {
            this.isFirstLoad = false;
        } else {
            if (!TextUtils.equals(this.loadDataTime, this.simpleDateFormat.format(new Date()))) {
                requestData();
            }
        }
        this.tvTargetStep.setText(StepsPreference.getStepsTarget() + "");
        refreshCaloryRecord();
    }

    public void onStart() {
        super.onStart();
        if (!this.isCurrentRunningForeground) {
            Helper.showLog(TAG, "切换到前台");
            this.isCurrentRunningForeground = true;
            refreshStepsView();
        }
    }

    public void onStop() {
        super.onStop();
        this.isCurrentRunningForeground = isRunningForeground();
        if (!this.isCurrentRunningForeground) {
            Helper.showLog(TAG, "切换到后台");
        }
    }

    public boolean isRunningForeground() {
        for (RunningAppProcessInfo appProcessInfo : ((ActivityManager) getActivity()
                .getSystemService(ModelName.ACTIVITY)).getRunningAppProcesses()) {
            if (appProcessInfo.processName.equals("com.boohee.one") && appProcessInfo.importance
                    == 100 && appProcessInfo.processName.equals(getActivity().getApplicationInfo
                    ().processName)) {
                Helper.showLog(TAG, "isRunningForeGround");
                return true;
            }
        }
        Helper.showLog(TAG, "isRunningBackGround");
        return false;
    }

    private void initStepManager(String type) {
        if (this.stepManager == null) {
            this.stepManager = StepManagerFactory.getInstance().setType(type).createStepManager
                    (getActivity());
            this.stepManager.setListener(this);
        }
    }

    public void onGetCurrentStep(StepModel stepModel, boolean isError) {
        if (!isRemoved()) {
            if (stepModel != null) {
                this.tvStep.setText(stepModel.step + "");
                int target = StepsPreference.getStepsTarget();
                if (target > 0) {
                    this.progressStep.setPercent((((float) stepModel.step) * 1.0f) / ((float)
                            target));
                    this.tvTargetStep.setText(StepsPreference.getStepsTarget() + "");
                    return;
                }
                return;
            }
            this.tvStep.setText("0");
        }
    }

    private void refreshStepsView() {
        if (!isRemoved() && this.stepManager != null) {
            if (!this.stepManager.isSurpportStepCount() || (this.stepManager.isPedometer() &&
                    !StepsPreference.isStepOpen())) {
                this.rlStep.setVisibility(8);
                return;
            }
            this.rlStep.setVisibility(0);
            this.tvTargetStep.setText(StepsPreference.getStepsTarget() + "");
            this.stepManager.getCurrentStepAsyncs();
        }
    }

    private void refreshWeightProgress() {
        if (this.mUser != null && !isRemoved()) {
            float latestWeight = getLatestWeight();
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(1);
            drawable.setColor(ViewCompat.MEASURED_SIZE_MASK);
            float delta;
            if (this.mUser.target_weight == -1.0f) {
                this.tvProgress.setText("保持/塑形");
                delta = latestWeight - this.mUser.begin_weight;
                if (delta <= 0.0f) {
                    drawable.setStroke(5, getResources().getColor(R.color.hb));
                    this.tvDes.setText("比上次轻");
                } else {
                    drawable.setStroke(5, getResources().getColor(R.color.jr));
                    this.tvDes.setText("比上次重");
                }
                this.tvWeight.setText(NumberUtils.safeToString(this.dFormat, (double) Math.abs
                        (delta)));
                this.arcProgress.setVisibility(8);
            } else {
                float progress;
                drawable.setStroke(1, 2113929215);
                delta = latestWeight - this.mUser.begin_weight;
                this.tvWeight.setText(NumberUtils.safeToString(this.dFormat, (double) Math.abs
                        (delta)));
                if (delta <= 0.0f) {
                    this.tvDes.setText("已减重");
                } else {
                    this.tvDes.setText("已增重");
                }
                if (this.mUser.begin_weight - latestWeight < 0.0f) {
                    progress = -1.0f;
                } else if (this.mUser.begin_weight - this.mUser.target_weight <= 0.0f) {
                    progress = 0.0f;
                } else {
                    progress = (this.mUser.begin_weight - latestWeight) / (this.mUser
                            .begin_weight - this.mUser.target_weight);
                }
                this.arcProgress.setVisibility(0);
                this.arcProgress.setProgress(progress);
                this.tvWeight.setText(NumberUtils.safeToString(this.dFormat, (double) Math.abs
                        (delta)));
                this.tvProgress.setText(String.format("目标完成%d%%", new Object[]{Integer.valueOf
                        (Math.min(Math.max((int) (100.0f * progress), 0), 100))}));
            }
            this.rlProgress.setBackgroundDrawable(drawable);
        }
    }

    private float getLatestWeight() {
        LocalWeightRecord record = (LocalWeightRecord) FastJsonUtils.fromJson(this.mCache
                .getAsString("latest_weight"), LocalWeightRecord.class);
        if (this.mUser == null) {
            return record != null ? Float.parseFloat(record.weight) : IntFloatWheelView
                    .DEFAULT_VALUE;
        } else {
            if (record == null || TextUtils.isEmpty(record.record_on)) {
                return this.mUser.latest_weight;
            }
            if (TextUtils.isEmpty(this.mUser.latest_weight_at) || record.record_on.compareTo(this
                    .mUser.latest_weight_at) < 0) {
                return this.mUser.latest_weight;
            }
            return Float.parseFloat(record.weight);
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        EventBus.getDefault().unregister(this);
        if (this.stepManager != null) {
            this.stepManager.onDestroy();
        }
    }

    private void setTranslateAnim() {
        if (this.mAnimator == null) {
            float[] values = new float[10];
            for (int i = 0; i < 10; i++) {
                values[i] = ((float) i) * 100.0f;
            }
            PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("translationY", values);
            PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", new float[]{1.1f});
            PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", new float[]{1.1f});
            this.mAnimator = ObjectAnimator.ofPropertyValuesHolder(this.rpCheckIn, new
                    PropertyValuesHolder[]{pvhY, scaleX, scaleY}).setDuration(500);
            this.mAnimator.setInterpolator(new LinearInterpolator());
            this.mAnimator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    HomeNewFragment.this.rpCheckIn.bringToFront();
                }

                public void onAnimationCancel(Animator animation) {
                }

                public void onAnimationEnd(Animator animation) {
                    HomeNewFragment.this.layoutPullDown.scrollBy(0, HomeNewFragment.this
                            .layoutPullDown.getHeight());
                    HomeNewFragment.this.requestCheckIn();
                }
            });
        }
        if (!this.mAnimator.isRunning()) {
            this.mAnimator.start();
        }
    }
}
