package com.boohee.record;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar.LayoutParams;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.api.RecordApi;
import com.boohee.database.OnePreference;
import com.boohee.database.UserPreference;
import com.boohee.food.AddFoodListActivity;
import com.boohee.food.AddSportListActivity;
import com.boohee.food.EditCameraRecordActivity;
import com.boohee.food.FoodListActivity;
import com.boohee.food.SummaryActivity;
import com.boohee.main.GestureActivity;
import com.boohee.model.LocalCalorieDistribution;
import com.boohee.model.RecordFood;
import com.boohee.model.RecordPhoto;
import com.boohee.model.RecordSport;
import com.boohee.model.VideoSportRecord;
import com.boohee.modeldao.FoodRecordDao;
import com.boohee.modeldao.SportRecordDao;
import com.boohee.modeldao.UserDao;
import com.boohee.myview.DietPopView;
import com.boohee.myview.DietPopView.OnDateClickListener;
import com.boohee.myview.MineHeadViewFactory;
import com.boohee.myview.highlight.HighLight;
import com.boohee.myview.highlight.HighLight.MarginInfo;
import com.boohee.myview.highlight.HighLight.OnHighLightClickListener;
import com.boohee.myview.highlight.HighLight.OnPosCallback;
import com.boohee.one.R;
import com.boohee.one.cache.CacheKey;
import com.boohee.one.cache.FileCache;
import com.boohee.one.event.CanCaloryEvent;
import com.boohee.one.event.ConstEvent;
import com.boohee.one.event.DietEvent;
import com.boohee.one.event.PhotoDietEvent;
import com.boohee.one.event.RefreshCalorieEvent;
import com.boohee.one.event.RefreshDietEvent;
import com.boohee.one.event.RefreshSportEvent;
import com.boohee.one.event.SportEvent;
import com.boohee.one.event.TimeTypeDietEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.pedometer.StepCounterUtil;
import com.boohee.one.sync.SyncHelper;
import com.boohee.one.ui.DietShareActivity;
import com.boohee.one.ui.fragment.AddCustomDietFragment;
import com.boohee.one.ui.fragment.AddCustomSportFragment;
import com.boohee.one.ui.fragment.AddSportFragment;
import com.boohee.one.ui.fragment.EditDietFragment;
import com.boohee.one.ui.fragment.TodayAnalysisActivity;
import com.boohee.utility.Event;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.DateFormatUtils;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.FoodUtils;
import com.boohee.utils.FormulaUtils;
import com.boohee.utils.HttpUtils;
import com.boohee.utils.ViewUtils;
import com.boohee.widgets.LightAlertDialog;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.json.JSONObject;

public class DietSportCalendarActivity extends GestureActivity implements OnClickListener {
    public static final int                    ANIM_DURATION              = 500;
    public static final int                    TIME_TYPE_BREAKFAST        = 1;
    public static final int                    TIME_TYPE_DINNER           = 3;
    public static final int                    TIME_TYPE_LUNCH            = 2;
    public static final int                    TIME_TYPE_SNACKS_BREAKFAST = 6;
    public static final int                    TIME_TYPE_SNACKS_DINNER    = 8;
    public static final int                    TIME_TYPE_SNACKS_LUNCH     = 7;
    private             ArrayList<RecordFood>  breakfastList              = new ArrayList();
    private             ArrayList<RecordPhoto> breakfastPhotoList         = new ArrayList();
    @InjectView(2131427604)
    Button btnTodayAnalysis;
    private float budgetCalory = 0.0f;
    private int     canCalory;
    private boolean canShare;
    @InjectView(2131427606)
    DietPopView dietPopView;
    private ArrayList<RecordFood>  dinnerList      = new ArrayList();
    private ArrayList<RecordPhoto> dinnerPhotoList = new ArrayList();
    private MineHeadViewFactory factory;
    ImageView iv_next;
    ImageView iv_previous;
    @InjectView(2131427601)
    LinearLayout ll_calory_distribute;
    @InjectView(2131427575)
    LinearLayout ll_card_breakfast;
    @InjectView(2131427579)
    LinearLayout ll_card_dinner;
    @InjectView(2131427577)
    LinearLayout ll_card_lunch;
    @InjectView(2131427576)
    LinearLayout ll_card_snacks_breakfast;
    @InjectView(2131427580)
    LinearLayout ll_card_snacks_dinner;
    @InjectView(2131427578)
    LinearLayout ll_card_snacks_lunch;
    @InjectView(2131427581)
    LinearLayout ll_card_sport;
    @InjectView(2131428793)
    LinearLayout ll_nav_snacks;
    @InjectView(2131427602)
    LinearLayout ll_record;
    @InjectView(2131427605)
    LinearLayout ll_record_null;
    @InjectView(2131427603)
    LinearLayout ll_today_analysis;
    @InjectView(2131427600)
    LinearLayout ll_top_layout;
    private ArrayList<RecordFood>  lunchList      = new ArrayList();
    private ArrayList<RecordPhoto> lunchPhotoList = new ArrayList();
    protected FileCache mCache;
    private OnClickListener mOnIndicator = new OnClickListener() {
        public void onClick(View v) {
            int adjust = 0;
            switch (v.getId()) {
                case R.id.iv_previous:
                    adjust = -1;
                    break;
                case R.id.iv_next:
                    adjust = 1;
                    break;
            }
            DietSportCalendarActivity.this.refreshDietAndSport(DateFormatUtils.adjustDateByDay
                    (DietSportCalendarActivity.this.record_on, adjust));
        }
    };
    PopupWindow mPopupWindow;
    private String record_on;
    private ArrayList<RecordFood>  snacksBreakfastList        = new ArrayList();
    private ArrayList<RecordPhoto> snacksBreakfastPhotoList   = new ArrayList();
    private ArrayList<RecordFood>  snacksDinnerList           = new ArrayList();
    private ArrayList<RecordPhoto> snacksDinnerPhotoList      = new ArrayList();
    private ArrayList<RecordFood>  snacksLunchList            = new ArrayList();
    private ArrayList<RecordPhoto> snacksLunchPhotoList       = new ArrayList();
    private ArrayList<RecordSport> sportList                  = new ArrayList();
    private float                  totalBreakfastCalory       = 0.0f;
    private float                  totalDinnerCalory          = 0.0f;
    private float                  totalLunchCalory           = 0.0f;
    private float                  totalSnacksBreakfastCalory = 0.0f;
    private float                  totalSnacksDinnerCalory    = 0.0f;
    private float                  totalSnacksLunchCalory     = 0.0f;
    private float                  totalSportCalory           = 0.0f;
    TextView tv_date;
    private ArrayList<VideoSportRecord> videoSportRecords = new ArrayList();

    private class OnSnacksClickListener implements OnClickListener {
        private OnSnacksClickListener() {
        }

        public void onClick(View v) {
            if (DietSportCalendarActivity.this.mPopupWindow != null && DietSportCalendarActivity
                    .this.mPopupWindow.isShowing()) {
                DietSportCalendarActivity.this.mPopupWindow.dismiss();
            }
            switch (v.getId()) {
                case R.id.tv_breakfast_snack:
                    MobclickAgent.onEvent(DietSportCalendarActivity.this.ctx, Event
                            .tool_foodandsport_extrabreakfastmeal);
                    AddFoodListActivity.start(DietSportCalendarActivity.this.activity, 6,
                            DietSportCalendarActivity.this.record_on, DietSportCalendarActivity
                                    .this.totalSnacksBreakfastCalory);
                    return;
                case R.id.tv_lunch_snack:
                    MobclickAgent.onEvent(DietSportCalendarActivity.this.ctx, Event
                            .tool_foodandsport_extralunchmeal);
                    AddFoodListActivity.start(DietSportCalendarActivity.this.activity, 7,
                            DietSportCalendarActivity.this.record_on, DietSportCalendarActivity
                                    .this.totalSnacksLunchCalory);
                    return;
                case R.id.tv_supper_snack:
                    MobclickAgent.onEvent(DietSportCalendarActivity.this.ctx, Event
                            .tool_foodandsport_extradinnermeal);
                    AddFoodListActivity.start(DietSportCalendarActivity.this.activity, 8,
                            DietSportCalendarActivity.this.record_on, DietSportCalendarActivity
                                    .this.totalSnacksDinnerCalory);
                    return;
                default:
                    return;
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.b3);
        ButterKnife.inject((Activity) this);
        EventBus.getDefault().register(this);
        this.mCache = FileCache.get(this.activity);
        initToolsBar();
        initDate();
        intBudgetCalory();
        refreshCalory();
        getEatings();
        getActivities();
        initDietView();
        SyncHelper.syncAllEatings();
        SyncHelper.syncAllSports();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.m, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                if (this.canShare) {
                    DietShareActivity.comeOnBaby(this.ctx, this.record_on);
                }
                return true;
            case R.id.action_copy_history:
                MobclickAgent.onEvent(this.activity, Event.TOOL_FOODANDSPORT_COPY);
                HistoryRecordActivity.start(this.activity, this.record_on);
                return true;
            case R.id.action_diet_chart:
                MobclickAgent.onEvent(this.ctx, Event.tool_foodandsport_barchart);
                DietChartActivity.comeOnBaby(this.activity);
                return true;
            case R.id.action_summary:
                MobclickAgent.onEvent(this.activity, Event.TOOL_FOODANDSPORT_ABSTRACT);
                SummaryActivity.start(this.activity, this.record_on, this.breakfastList, this
                        .lunchList, this.dinnerList, this.snacksBreakfastList, this
                        .snacksLunchList, this.snacksDinnerList, this.sportList, this
                        .breakfastPhotoList, this.lunchPhotoList, this.dinnerPhotoList, this
                        .snacksBreakfastPhotoList, this.snacksLunchPhotoList, this
                        .snacksDinnerPhotoList, this.videoSportRecords);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initToolsBar() {
        View toolbar_diet_sport = LayoutInflater.from(this).inflate(R.layout.ov, null);
        this.tv_date = (TextView) toolbar_diet_sport.findViewById(R.id.tv_date);
        this.iv_previous = (ImageView) toolbar_diet_sport.findViewById(R.id.iv_previous);
        this.iv_next = (ImageView) toolbar_diet_sport.findViewById(R.id.iv_next);
        this.iv_previous.setOnClickListener(this.mOnIndicator);
        this.iv_next.setOnClickListener(this.mOnIndicator);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(toolbar_diet_sport, new LayoutParams(-1, -1));
        this.tv_date.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (DietSportCalendarActivity.this.dietPopView.isShowing()) {
                    DietSportCalendarActivity.this.dietPopView.dismiss();
                    return;
                }
                MobclickAgent.onEvent(DietSportCalendarActivity.this.ctx, Event
                        .tool_foodandsport_calendar);
                DietSportCalendarActivity.this.dietPopView.show();
            }
        });
    }

    private void initDate() {
        this.record_on = DateFormatUtils.date2string(new Date(), "yyyy-MM-dd");
        this.tv_date.setText("今天");
    }

    private void intBudgetCalory() {
        this.factory = new MineHeadViewFactory(this.activity);
        this.budgetCalory = (float) new UserDao(this.ctx).queryWithToken(UserPreference.getToken
                (this.ctx)).target_calory;
    }

    private void initDietView() {
        this.dietPopView.init(this.record_on);
        this.dietPopView.setOnDateClickListener(new OnDateClickListener() {
            public void onDateClick(Date date) {
                if (date != null) {
                    DietSportCalendarActivity.this.refreshDietAndSport(date);
                }
            }

            public void onBottomClick() {
            }
        });
        this.dietPopView.bt_today.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (DietSportCalendarActivity.this.dietPopView.isShowing()) {
                    DietSportCalendarActivity.this.dietPopView.dismiss();
                }
                DietSportCalendarActivity.this.dietPopView.postDelayed(new Runnable() {
                    public void run() {
                        DietSportCalendarActivity.this.refreshDietAndSport(new Date());
                    }
                }, 500);
            }
        });
    }

    private void refreshDietAndSport(Date date) {
        this.record_on = DateFormatUtils.date2string(date, "yyyy-MM-dd");
        this.tv_date.setText(DateFormatUtils.isToday(this.record_on) ? "今天" : this.record_on);
        this.dietPopView.init(this.record_on);
        clearList();
        refreshCalory();
        getEatings();
        getActivities();
    }

    public void clearList() {
        clearDietList();
        this.sportList.clear();
    }

    private void clearDietList() {
        this.breakfastList.clear();
        this.lunchList.clear();
        this.dinnerList.clear();
        this.snacksBreakfastList.clear();
        this.snacksLunchList.clear();
        this.snacksDinnerList.clear();
        this.breakfastPhotoList.clear();
        this.lunchPhotoList.clear();
        this.dinnerPhotoList.clear();
        this.snacksBreakfastPhotoList.clear();
        this.snacksLunchPhotoList.clear();
        this.snacksDinnerPhotoList.clear();
        this.totalBreakfastCalory = 0.0f;
        this.totalLunchCalory = 0.0f;
        this.totalDinnerCalory = 0.0f;
        this.totalSnacksBreakfastCalory = 0.0f;
        this.totalSnacksLunchCalory = 0.0f;
        this.totalSnacksDinnerCalory = 0.0f;
    }

    @OnClick({2131428790, 2131428791, 2131428792, 2131428793, 2131428794, 2131427604})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_today_analysis:
                MobclickAgent.onEvent(this.activity, Event.TOOL_DAILYANALYZE);
                TodayAnalysisActivity.start(this.activity, this.record_on);
                return;
            case R.id.ll_nav_breakfast:
                MobclickAgent.onEvent(this.ctx, Event.tool_foodandsport_breakfast);
                AddFoodListActivity.start(this, 1, this.record_on, this.totalBreakfastCalory);
                return;
            case R.id.ll_nav_lunch:
                MobclickAgent.onEvent(this.ctx, Event.tool_foodandsport_lunch);
                AddFoodListActivity.start(this, 2, this.record_on, this.totalLunchCalory);
                return;
            case R.id.ll_nav_dinner:
                MobclickAgent.onEvent(this.ctx, Event.tool_foodandsport_dinner);
                AddFoodListActivity.start(this, 3, this.record_on, this.totalDinnerCalory);
                return;
            case R.id.ll_nav_snacks:
                createPopwindow(this.ll_nav_snacks);
                return;
            case R.id.ll_nav_sport:
                AddSportListActivity.start(this.ctx, this.record_on);
                return;
            default:
                return;
        }
    }

    private void createPopwindow(View view) {
        LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.lt, null);
        this.mPopupWindow = new PopupWindow(layout, -2, -2);
        this.mPopupWindow.setFocusable(true);
        this.mPopupWindow.setOutsideTouchable(true);
        this.mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        this.mPopupWindow.setAnimationStyle(R.style.kr);
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        this.mPopupWindow.showAtLocation(view, 0, location[0] + (view.getWidth() / 2),
                location[1] - 350);
        layout.findViewById(R.id.tv_breakfast_snack).setOnClickListener(new OnSnacksClickListener
                ());
        layout.findViewById(R.id.tv_lunch_snack).setOnClickListener(new OnSnacksClickListener());
        layout.findViewById(R.id.tv_supper_snack).setOnClickListener(new OnSnacksClickListener());
    }

    private void getEatings() {
        if (HttpUtils.isNetworkAvailable(this)) {
            showLoading();
            RecordApi.getEatings(this.record_on, this, new JsonCallback(this) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    DietSportCalendarActivity.this.initDietUI(FastJsonUtils.parseList(object
                            .optString("data"), RecordFood.class), FastJsonUtils.parseList(object
                            .optString("diet_photos"), RecordPhoto.class));
                }

                public void onFinish() {
                    super.onFinish();
                    DietSportCalendarActivity.this.dismissLoading();
                }
            });
            return;
        }
        initDietUI(new FoodRecordDao(this).getList(this.record_on), null);
    }

    private void initDietUI(List<RecordFood> recordFoods, List<RecordPhoto> recordPhotos) {
        initDietData(recordFoods, recordPhotos);
        initDietCardView(1, this.breakfastList, this.breakfastPhotoList, this.ll_card_breakfast);
        initDietCardView(2, this.lunchList, this.lunchPhotoList, this.ll_card_lunch);
        initDietCardView(3, this.dinnerList, this.dinnerPhotoList, this.ll_card_dinner);
        initDietCardView(6, this.snacksBreakfastList, this.snacksBreakfastPhotoList, this
                .ll_card_snacks_breakfast);
        initDietCardView(7, this.snacksLunchList, this.snacksLunchPhotoList, this
                .ll_card_snacks_lunch);
        initDietCardView(8, this.snacksDinnerList, this.snacksDinnerPhotoList, this
                .ll_card_snacks_dinner);
        if (DateFormatUtils.isToday(this.record_on)) {
            EventBus.getDefault().post(new CanCaloryEvent().setCalory(this.canCalory));
        }
    }

    private void getActivities() {
        if (!TextUtils.isEmpty(this.record_on)) {
            if (HttpUtils.isNetworkAvailable(this)) {
                showLoading();
                RecordApi.getActivities(this.record_on, this, new JsonCallback(this) {
                    public void ok(JSONObject object) {
                        super.ok(object);
                        DietSportCalendarActivity.this.initSportData(RecordSport.parseList
                                (object, "data"), FastJsonUtils.parseList(object.optString
                                ("boohee_sport_items"), VideoSportRecord.class));
                        DietSportCalendarActivity.this.initStepCountRecordToday();
                        DietSportCalendarActivity.this.initSportCardView
                                (DietSportCalendarActivity.this.sportList,
                                        DietSportCalendarActivity.this.ll_card_sport);
                    }

                    public void onFinish() {
                        super.onFinish();
                        DietSportCalendarActivity.this.dismissLoading();
                    }
                });
                return;
            }
            initSportData(new SportRecordDao(this).getList(this.record_on), null);
            initStepCountRecordToday();
            initSportCardView(this.sportList, this.ll_card_sport);
        }
    }

    private void initStepCountRecordToday() {
        if (DateFormatUtils.isToday(this.record_on)) {
            if (this.videoSportRecords.size() > 0) {
                Iterator it = this.videoSportRecords.iterator();
                while (it.hasNext()) {
                    VideoSportRecord record = (VideoSportRecord) it.next();
                    if (TextUtils.equals(record.activity_name, "走路")) {
                        this.videoSportRecords.remove(record);
                        break;
                    }
                }
            }
            VideoSportRecord videoSportRecord = StepCounterUtil.getStepToSportRecord(this.ctx,
                    this.record_on);
            if (videoSportRecord != null) {
                this.videoSportRecords.add(videoSportRecord);
            }
        }
    }

    private void initDietData(List<RecordFood> recordFoods, List<RecordPhoto> recordPhotos) {
        int i;
        if (recordFoods != null && recordFoods.size() > 0) {
            for (i = 0; i < recordFoods.size(); i++) {
                RecordFood recordFood = (RecordFood) recordFoods.get(i);
                if (recordFood.time_type == 1) {
                    this.breakfastList.add(recordFood);
                } else if (recordFood.time_type == 2) {
                    this.lunchList.add(recordFood);
                } else if (recordFood.time_type == 3) {
                    this.dinnerList.add(recordFood);
                } else if (recordFood.time_type == 6) {
                    this.snacksBreakfastList.add(recordFood);
                } else if (recordFood.time_type == 7) {
                    this.snacksLunchList.add(recordFood);
                } else if (recordFood.time_type == 8) {
                    this.snacksDinnerList.add(recordFood);
                }
            }
        }
        if (recordPhotos != null && recordPhotos.size() > 0) {
            for (i = 0; i < recordPhotos.size(); i++) {
                RecordPhoto recordPhoto = (RecordPhoto) recordPhotos.get(i);
                if (recordPhoto.time_type == 1) {
                    this.breakfastPhotoList.add(recordPhoto);
                } else if (recordPhoto.time_type == 2) {
                    this.lunchPhotoList.add(recordPhoto);
                } else if (recordPhoto.time_type == 3) {
                    this.dinnerPhotoList.add(recordPhoto);
                } else if (recordPhoto.time_type == 6) {
                    this.snacksBreakfastPhotoList.add(recordPhoto);
                } else if (recordPhoto.time_type == 7) {
                    this.snacksLunchPhotoList.add(recordPhoto);
                } else if (recordPhoto.time_type == 8) {
                    this.snacksDinnerPhotoList.add(recordPhoto);
                }
            }
        }
    }

    private void initSportData(List<RecordSport> records, List<VideoSportRecord> videoRecords) {
        this.sportList.clear();
        this.totalSportCalory = 0.0f;
        if (records != null && records.size() > 0) {
            this.ll_record.setVisibility(0);
            this.ll_record_null.setVisibility(8);
            for (int i = 0; i < records.size(); i++) {
                this.sportList.add((RecordSport) records.get(i));
            }
        }
        this.videoSportRecords.clear();
        if (videoRecords != null && videoRecords.size() > 0) {
            this.ll_record.setVisibility(0);
            this.ll_record_null.setVisibility(8);
            this.videoSportRecords.addAll(videoRecords);
        }
    }

    private void initDietCardView(int time_type, ArrayList<RecordFood> recordFoodList,
                                  ArrayList<RecordPhoto> recordPhotoList, LinearLayout ll_card) {
        RelativeLayout rl_meal = (RelativeLayout) ll_card.findViewById(R.id.rl_meal);
        TextView tv_name = (TextView) ll_card.findViewById(R.id.tv_name);
        TextView tv_suggest = (TextView) ll_card.findViewById(R.id.tv_suggest);
        TextView tv_calory = (TextView) ll_card.findViewById(R.id.tv_calory);
        int[] suggestCalorie = FormulaUtils.calorieLimit(this.budgetCalory, time_type);
        if (time_type == 1 || time_type == 2 || time_type == 3) {
            tv_suggest.setText(String.format(Locale.getDefault(), "建议：%d ~ %d 千卡", new
                    Object[]{Integer.valueOf(suggestCalorie[0]), Integer.valueOf
                    (suggestCalorie[1])}));
            tv_suggest.setVisibility(0);
        } else {
            tv_suggest.setVisibility(8);
        }
        LinearLayout ll_list = (LinearLayout) ll_card.findViewById(R.id.ll_list);
        if ((recordFoodList == null || recordFoodList.size() == 0) && (recordPhotoList == null ||
                recordPhotoList.size() == 0)) {
            ll_card.setVisibility(8);
            if (time_type == 1) {
                this.totalBreakfastCalory = 0.0f;
            } else if (time_type == 2) {
                this.totalLunchCalory = 0.0f;
            } else if (time_type == 3) {
                this.totalDinnerCalory = 0.0f;
            } else if (time_type == 6) {
                this.totalSnacksBreakfastCalory = 0.0f;
            } else if (time_type == 7) {
                this.totalSnacksLunchCalory = 0.0f;
            } else if (time_type == 8) {
                this.totalSnacksDinnerCalory = 0.0f;
            }
        } else {
            int i;
            View view;
            ll_card.setVisibility(0);
            ll_list.removeAllViews();
            tv_name.setText(FoodUtils.getDietName(this, time_type));
            float totalCalory = 0.0f;
            if (recordFoodList != null || recordFoodList.size() > 0) {
                i = 0;
                while (i < recordFoodList.size()) {
                    RecordFood recordFood = (RecordFood) recordFoodList.get(i);
                    totalCalory += recordFood.calory;
                    view = getDietItemView(i, recordFood, i == recordFoodList.size() + -1);
                    if (view != null) {
                        ll_list.addView(view);
                    }
                    i++;
                }
            }
            if (recordPhotoList != null || recordPhotoList.size() > 0) {
                i = 0;
                while (i < recordPhotoList.size()) {
                    RecordPhoto recordPhoto = (RecordPhoto) recordPhotoList.get(i);
                    if (recordPhoto.status != 1) {
                        totalCalory += recordPhoto.calory;
                    }
                    view = getPhotoDietItemView(i, recordPhoto, i == recordPhotoList.size() + -1);
                    if (view != null) {
                        ll_list.addView(view);
                    }
                    i++;
                }
            }
            tv_calory.setText(Math.round(totalCalory) + "千卡");
            if (time_type == 1) {
                this.totalBreakfastCalory = totalCalory;
            } else if (time_type == 2) {
                this.totalLunchCalory = totalCalory;
            } else if (time_type == 3) {
                this.totalDinnerCalory = totalCalory;
            } else if (time_type == 6) {
                this.totalSnacksBreakfastCalory = totalCalory;
            } else if (time_type == 7) {
                this.totalSnacksLunchCalory = totalCalory;
            } else if (time_type == 8) {
                this.totalSnacksDinnerCalory = totalCalory;
            }
            float suggestCalory = (float) FormulaUtils.calorieLimit(this.budgetCalory, time_type)
                    [1];
            if (totalCalory <= suggestCalory || suggestCalory <= 0.0f) {
                tv_calory.setTextColor(ContextCompat.getColor(this.ctx, R.color.du));
            } else {
                tv_calory.setTextColor(ContextCompat.getColor(this.ctx, R.color.he));
            }
        }
        refreshCalory();
        final int i2 = time_type;
        final ArrayList<RecordFood> arrayList = recordFoodList;
        final ArrayList<RecordPhoto> arrayList2 = recordPhotoList;
        rl_meal.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                FoodListActivity.start(DietSportCalendarActivity.this.ctx, i2, arrayList,
                        arrayList2);
            }
        });
    }

    private void initSportCardView(final ArrayList<RecordSport> recordSportList, LinearLayout
            ll_card) {
        RelativeLayout rl_meal = (RelativeLayout) ll_card.findViewById(R.id.rl_meal);
        TextView tv_name = (TextView) ll_card.findViewById(R.id.tv_name);
        ((TextView) ll_card.findViewById(R.id.tv_suggest)).setVisibility(8);
        TextView tv_calory = (TextView) ll_card.findViewById(R.id.tv_calory);
        LinearLayout ll_list = (LinearLayout) ll_card.findViewById(R.id.ll_list);
        if ((recordSportList == null || recordSportList.size() == 0) && this.videoSportRecords
                .size() == 0) {
            ll_card.setVisibility(8);
            this.totalSportCalory = 0.0f;
        } else {
            View view;
            ll_card.setVisibility(0);
            ll_list.removeAllViews();
            tv_name.setText("运动");
            float totalCalory = 0.0f;
            int i = 0;
            while (i < recordSportList.size()) {
                RecordSport recordSport = (RecordSport) recordSportList.get(i);
                totalCalory += recordSport.calory;
                view = getSportItemView(i, recordSport, i == recordSportList.size() + -1);
                if (view != null) {
                    ll_list.addView(view);
                }
                i++;
            }
            i = 0;
            while (i < this.videoSportRecords.size()) {
                VideoSportRecord record = (VideoSportRecord) this.videoSportRecords.get(i);
                totalCalory += (float) record.calory;
                view = getVideoSportItemView(i, record, i == this.videoSportRecords.size() + -1);
                if (view != null) {
                    ll_list.addView(view);
                }
                i++;
            }
            tv_calory.setText(Math.round(totalCalory) + "千卡");
            this.totalSportCalory = totalCalory;
        }
        refreshCalory();
        if (DateFormatUtils.isToday(this.record_on)) {
            EventBus.getDefault().post(new CanCaloryEvent().setCalory(this.canCalory));
        }
        rl_meal.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SportListActivity.start(DietSportCalendarActivity.this.ctx, recordSportList);
            }
        });
    }

    private View getSportItemView(final int index, final RecordSport recordSport, boolean
            isLastest) {
        View itemView = null;
        if (recordSport != null) {
            itemView = LayoutInflater.from(this).inflate(R.layout.oc, null);
            ImageView iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            TextView txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            TextView txt_count = (TextView) itemView.findViewById(R.id.txt_count);
            TextView txt_calory = (TextView) itemView.findViewById(R.id.txt_calory);
            View view_divide = itemView.findViewById(R.id.view_divide);
            if (isLastest && (this.videoSportRecords == null || this.videoSportRecords.size() ==
                    0)) {
                view_divide.setVisibility(8);
            }
            this.imageLoader.displayImage(recordSport.thumb_img_url, iv_avatar,
                    ImageLoaderOptions.global((int) R.drawable.aa5));
            txt_name.setText(recordSport.activity_name);
            StringBuilder stringBuilder = new StringBuilder();
            String str = (recordSport.activity_id == 0 && FoodUtils.isKM(recordSport.unit_name))
                    ? recordSport.duration + "" : Math.round(recordSport.duration) + "";
            txt_count.setText(stringBuilder.append(str).append(recordSport.unit_name).toString());
            txt_calory.setText(Math.round(recordSport.calory) + "千卡");
            itemView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (recordSport.activity_id != 0) {
                        AddSportFragment.newInstance(1, index, recordSport).show
                                (DietSportCalendarActivity.this.getSupportFragmentManager(),
                                        "addSportFragment");
                    } else {
                        AddCustomSportFragment.newInstance(1, index, recordSport).show
                                (DietSportCalendarActivity.this.getSupportFragmentManager(),
                                        "addCustomSportFragment");
                    }
                }
            });
        }
        return itemView;
    }

    private View getVideoSportItemView(int index, final VideoSportRecord recordSport, boolean
            isLastest) {
        View itemView = null;
        if (recordSport != null) {
            itemView = LayoutInflater.from(this).inflate(R.layout.oc, null);
            ImageView iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            TextView txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            TextView txt_count = (TextView) itemView.findViewById(R.id.txt_count);
            TextView txt_calory = (TextView) itemView.findViewById(R.id.txt_calory);
            View view_divide = itemView.findViewById(R.id.view_divide);
            if (isLastest) {
                view_divide.setVisibility(8);
            }
            this.imageLoader.displayImage(recordSport.img_url, iv_avatar, ImageLoaderOptions
                    .global((int) R.drawable.aa5));
            txt_name.setText(recordSport.activity_name);
            txt_count.setText(R.string.mx);
            txt_calory.setText(Math.round((float) recordSport.calory) + "千卡");
            itemView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    final LightAlertDialog dialog = LightAlertDialog.create
                            (DietSportCalendarActivity.this.ctx, "确定要删除吗？");
                    dialog.setPositiveButton(new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DietSportCalendarActivity.this.showLoading();
                            RecordApi.deleteVideoSportRecord(DietSportCalendarActivity.this.ctx,
                                    recordSport.id, new JsonCallback(DietSportCalendarActivity
                                            .this.ctx) {
                                public void ok(String response) {
                                    super.ok(response);
                                    DietSportCalendarActivity.this.getActivities();
                                }
                            });
                        }
                    });
                    dialog.setNegativeButton(new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            });
        }
        return itemView;
    }

    private View getDietItemView(final int index, final RecordFood recordFood, boolean isLast) {
        View itemView = null;
        if (recordFood != null) {
            itemView = LayoutInflater.from(this).inflate(R.layout.oc, null);
            ImageView iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            TextView txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            TextView txt_count = (TextView) itemView.findViewById(R.id.txt_count);
            TextView txt_calory = (TextView) itemView.findViewById(R.id.txt_calory);
            View view_divide = itemView.findViewById(R.id.view_divide);
            if (isLast) {
                view_divide.setVisibility(8);
            }
            this.imageLoader.displayImage(recordFood.thumb_img_url, iv_avatar, ImageLoaderOptions
                    .global((int) R.drawable.aa2));
            txt_name.setText(recordFood.food_name);
            txt_count.setText(recordFood.amount + recordFood.unit_name);
            txt_calory.setText(Math.round(recordFood.calory) + "千卡");
            itemView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (TextUtils.isEmpty(recordFood.code)) {
                        Fragment addCustomDietFragment = AddCustomDietFragment.newInstance(1,
                                index, recordFood.clone());
                        FragmentTransaction transaction = DietSportCalendarActivity.this
                                .getSupportFragmentManager().beginTransaction();
                        transaction.add(addCustomDietFragment, "addCustomDietFragment");
                        transaction.commitAllowingStateLoss();
                        return;
                    }
                    Fragment editDietFragment = EditDietFragment.newInstance(recordFood
                            .time_type, index, recordFood.clone());
                    transaction = DietSportCalendarActivity.this.getSupportFragmentManager()
                            .beginTransaction();
                    transaction.add(editDietFragment, "editDietFragment");
                    transaction.commitAllowingStateLoss();
                }
            });
        }
        return itemView;
    }

    private View getPhotoDietItemView(final int index, final RecordPhoto recordPhoto, boolean
            isLastest) {
        View itemView = null;
        if (recordPhoto != null) {
            itemView = LayoutInflater.from(this).inflate(R.layout.in, null);
            ImageView iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            TextView txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            TextView txt_calory = (TextView) itemView.findViewById(R.id.txt_calory);
            View view_divide = itemView.findViewById(R.id.view_divide);
            this.imageLoader.displayImage(recordPhoto.photo_url, iv_avatar, ImageLoaderOptions
                    .global((int) R.drawable.aa2));
            if (TextUtils.isEmpty(recordPhoto.name)) {
                txt_name.setText("拍照记录");
            } else {
                txt_name.setText(recordPhoto.name);
            }
            if (recordPhoto.status == 1) {
                txt_calory.setText("正在估算");
            } else if (recordPhoto.status == 4 || recordPhoto.status == 2) {
                txt_calory.setText(recordPhoto.calory > 0.0f ? Math.round(recordPhoto.calory) +
                        "千卡" : "");
            }
            itemView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    EditCameraRecordActivity.start(DietSportCalendarActivity.this.activity,
                            recordPhoto, index);
                }
            });
        }
        return itemView;
    }

    private void refreshCalory() {
        float totalDietCalory = ((((this.totalBreakfastCalory + this.totalLunchCalory) + this
                .totalDinnerCalory) + this.totalSnacksBreakfastCalory) + this
                .totalSnacksLunchCalory) + this.totalSnacksDinnerCalory;
        if (((((((((((((this.breakfastList.size() + this.lunchList.size()) + this.dinnerList.size
                ()) + this.snacksBreakfastList.size()) + this.snacksLunchList.size()) + this
                .snacksDinnerList.size()) + this.breakfastPhotoList.size()) + this.lunchPhotoList
                .size()) + this.dinnerPhotoList.size()) + this.snacksBreakfastPhotoList.size()) +
                this.snacksLunchPhotoList.size()) + this.snacksDinnerPhotoList.size()) + this
                .sportList.size()) + this.videoSportRecords.size() > 0) {
            this.ll_record.setVisibility(0);
            this.ll_record_null.setVisibility(8);
        } else {
            this.ll_record_null.setVisibility(0);
            this.ll_record.setVisibility(8);
        }
        this.canCalory = Math.round((this.budgetCalory - totalDietCalory) + FormulaUtils
                .computeSport(this.totalSportCalory));
        if (totalDietCalory > 0.0f) {
            this.ll_today_analysis.setVisibility(0);
            this.canShare = true;
        } else {
            this.ll_today_analysis.setVisibility(8);
            this.canShare = false;
        }
        this.ll_calory_distribute.removeAllViews();
        this.ll_calory_distribute.addView(this.factory.createHeadView(2, this.record_on,
                totalDietCalory, this.totalSportCalory));
        saveTodayDistribution(this.record_on, this.totalBreakfastCalory, this.totalLunchCalory,
                this.totalDinnerCalory, this.totalSnacksDinnerCalory + (this
                        .totalSnacksBreakfastCalory + this.totalSnacksLunchCalory), this
                        .totalSportCalory);
    }

    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(DietEvent eventEditDiet) {
        updateDietView(eventEditDiet);
    }

    private void updateDietView(DietEvent eventEditDiet) {
        int timeType = eventEditDiet.getTimeType();
        if (timeType == 1 && this.breakfastList != null) {
            initDietList(this.breakfastList, eventEditDiet);
            initDietCardView(timeType, this.breakfastList, this.breakfastPhotoList, this
                    .ll_card_breakfast);
        } else if (timeType == 2 && this.lunchList != null) {
            initDietList(this.lunchList, eventEditDiet);
            initDietCardView(timeType, this.lunchList, this.lunchPhotoList, this.ll_card_lunch);
        } else if (timeType == 3 && this.dinnerList != null) {
            initDietList(this.dinnerList, eventEditDiet);
            initDietCardView(timeType, this.dinnerList, this.dinnerPhotoList, this.ll_card_dinner);
        } else if (timeType == 6 && this.snacksBreakfastList != null) {
            initDietList(this.snacksBreakfastList, eventEditDiet);
            initDietCardView(timeType, this.snacksBreakfastList, this.snacksBreakfastPhotoList,
                    this.ll_card_snacks_breakfast);
        } else if (timeType == 7 && this.snacksLunchList != null) {
            initDietList(this.snacksLunchList, eventEditDiet);
            initDietCardView(timeType, this.snacksLunchList, this.snacksLunchPhotoList, this
                    .ll_card_snacks_lunch);
        } else if (timeType == 8 && this.snacksDinnerList != null) {
            initDietList(this.snacksDinnerList, eventEditDiet);
            initDietCardView(timeType, this.snacksDinnerList, this.snacksDinnerPhotoList, this
                    .ll_card_snacks_dinner);
        }
    }

    public void onEventMainThread(PhotoDietEvent eventPhotoEditDiet) {
        int timeType = eventPhotoEditDiet.getTimeType();
        if (timeType == 1 && this.breakfastPhotoList != null) {
            initPhotoDietList(this.breakfastPhotoList, eventPhotoEditDiet);
            initDietCardView(timeType, this.breakfastList, this.breakfastPhotoList, this
                    .ll_card_breakfast);
        } else if (timeType == 2 && this.lunchPhotoList != null) {
            initPhotoDietList(this.lunchPhotoList, eventPhotoEditDiet);
            initDietCardView(timeType, this.lunchList, this.lunchPhotoList, this.ll_card_lunch);
        } else if (timeType == 3 && this.dinnerPhotoList != null) {
            initPhotoDietList(this.dinnerPhotoList, eventPhotoEditDiet);
            initDietCardView(timeType, this.dinnerList, this.dinnerPhotoList, this.ll_card_dinner);
        } else if (timeType == 6 && this.snacksBreakfastPhotoList != null) {
            initPhotoDietList(this.snacksBreakfastPhotoList, eventPhotoEditDiet);
            initDietCardView(timeType, this.snacksBreakfastList, this.snacksBreakfastPhotoList,
                    this.ll_card_snacks_breakfast);
        } else if (timeType == 7 && this.snacksLunchPhotoList != null) {
            initPhotoDietList(this.snacksLunchPhotoList, eventPhotoEditDiet);
            initDietCardView(timeType, this.snacksLunchList, this.snacksLunchPhotoList, this
                    .ll_card_snacks_lunch);
        } else if (timeType == 8 && this.snacksDinnerPhotoList != null) {
            initPhotoDietList(this.snacksDinnerPhotoList, eventPhotoEditDiet);
            initDietCardView(timeType, this.snacksDinnerList, this.snacksDinnerPhotoList, this
                    .ll_card_snacks_dinner);
        }
        if (DateFormatUtils.isToday(this.record_on)) {
            EventBus.getDefault().post(new CanCaloryEvent().setCalory(this.canCalory));
        }
    }

    public void onEventMainThread(TimeTypeDietEvent event) {
        updateDietView(new DietEvent().setEditType(3).setIndex(event.getIndex()).setTimeType
                (event.getBeforeTimeType()));
        updateDietView(new DietEvent().setEditType(1).setTimeType(event.getRecordFood()
                .time_type).setRecordFood(event.getRecordFood()));
    }

    public void onEventMainThread(RefreshDietEvent refreshDietEvent) {
        RecordApi.getEatings(this.record_on, this, new JsonCallback(this) {
            public void ok(JSONObject object) {
                super.ok(object);
                List<RecordFood> recordFoods = FastJsonUtils.parseList(object.optString("data"),
                        RecordFood.class);
                List<RecordPhoto> recordPhotos = FastJsonUtils.parseList(object.optString
                        ("diet_photos"), RecordPhoto.class);
                DietSportCalendarActivity.this.clearDietList();
                DietSportCalendarActivity.this.initDietUI(recordFoods, recordPhotos);
                EventBus.getDefault().post(new RefreshCalorieEvent(recordFoods, recordPhotos));
            }

            public void onFinish() {
                super.onFinish();
            }
        });
    }

    public void onEventMainThread(RefreshSportEvent refreshSportEvent) {
        RecordApi.getActivities(this.record_on, this, new JsonCallback(this) {
            public void ok(JSONObject object) {
                super.ok(object);
                DietSportCalendarActivity.this.initSportData(RecordSport.parseList(object,
                        "data"), FastJsonUtils.parseList(object.optString("boohee_sport_items"),
                        VideoSportRecord.class));
                DietSportCalendarActivity.this.initStepCountRecordToday();
                DietSportCalendarActivity.this.initSportCardView(DietSportCalendarActivity.this
                        .sportList, DietSportCalendarActivity.this.ll_card_sport);
            }

            public void onFinish() {
                super.onFinish();
            }
        });
    }

    private void initDietList(ArrayList<RecordFood> recordFoods, DietEvent dietEvent) {
        int editType = dietEvent.getEditType();
        if (editType != 1 || dietEvent.getRecordFood() == null) {
            if (editType == 2 && dietEvent.getRecordFood() != null) {
                recordFoods.remove(dietEvent.getIndex());
                recordFoods.add(dietEvent.getIndex(), dietEvent.getRecordFood());
            } else if (editType == 3) {
                recordFoods.remove(dietEvent.getIndex());
            }
        } else if (recordFoods == null || recordFoods.size() <= 0) {
            recordFoods.add(dietEvent.getRecordFood());
        } else {
            int index = getIndexFromList((ArrayList) recordFoods, dietEvent.getRecordFood());
            if (index == -1) {
                recordFoods.add(dietEvent.getRecordFood());
            } else {
                recordFoods.set(index, dietEvent.getRecordFood());
            }
        }
    }

    private void initPhotoDietList(ArrayList<RecordPhoto> recordPhotos, PhotoDietEvent
            photoDietEvent) {
        int editType = photoDietEvent.getEditType();
        if (editType == 1 && photoDietEvent.getRecordPhoto() != null) {
            recordPhotos.add(photoDietEvent.getRecordPhoto());
        } else if (editType == 2 && photoDietEvent.getRecordPhoto() != null) {
            recordPhotos.remove(photoDietEvent.getIndex());
            recordPhotos.add(photoDietEvent.getIndex(), photoDietEvent.getRecordPhoto());
        } else if (editType == 3) {
            recordPhotos.remove(photoDietEvent.getIndex());
        }
    }

    private int getIndexFromList(ArrayList<RecordFood> recordFoods, RecordFood recordFood) {
        int i = 0;
        while (i < recordFoods.size()) {
            if (TextUtils.equals(((RecordFood) recordFoods.get(i)).food_name, recordFood
                    .food_name) && TextUtils.equals(((RecordFood) recordFoods.get(i)).code,
                    recordFood.code)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public void onEventMainThread(SportEvent sportEvent) {
        int editType = sportEvent.getEditType();
        if (editType != 1 || sportEvent.getRecordSport() == null) {
            if (editType == 2 && sportEvent.getRecordSport() != null) {
                this.sportList.remove(sportEvent.getIndex());
                this.sportList.add(sportEvent.getIndex(), sportEvent.getRecordSport());
            } else if (editType == 3) {
                this.sportList.remove(sportEvent.getIndex());
            }
        } else if (this.sportList == null || this.sportList.size() <= 0) {
            this.sportList.add(sportEvent.getRecordSport());
        } else {
            int index = getIndexFromList(this.sportList, sportEvent.getRecordSport());
            if (index == -1) {
                this.sportList.add(sportEvent.getRecordSport());
            } else {
                this.sportList.set(index, sportEvent.getRecordSport());
            }
        }
        initSportCardView(this.sportList, this.ll_card_sport);
    }

    public void onEventMainThread(ConstEvent constEvent) {
        if (constEvent != null && constEvent.getFlag() == 1) {
            clearList();
            refreshCalory();
            getEatings();
            getActivities();
        }
    }

    private int getIndexFromList(ArrayList<RecordSport> recordSports, RecordSport
            currentRecordSport) {
        int i;
        if (currentRecordSport.activity_id == 0) {
            for (i = 0; i < recordSports.size(); i++) {
                if (TextUtils.equals(((RecordSport) recordSports.get(i)).activity_name,
                        currentRecordSport.activity_name)) {
                    return i;
                }
            }
            return -1;
        }
        for (i = 0; i < recordSports.size(); i++) {
            if (currentRecordSport.activity_id == ((RecordSport) recordSports.get(i)).activity_id) {
                return i;
            }
        }
        return -1;
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            checkGuide();
        }
    }

    private void checkGuide() {
        if (!OnePreference.isDietFoodGuide()) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    DietSportCalendarActivity.this.showHighLight();
                }
            }, 500);
        }
    }

    private void showHighLight() {
        if (this.tv_date != null) {
            try {
                HighLight highLight = new HighLight(this.activity).addHighLight((int) R.id
                        .tv_date, (int) R.layout.ou, new OnPosCallback() {
                    public void getPos(float rightMargin, float bottomMargin, RectF rectF,
                                       MarginInfo marginInfo) {
                        marginInfo.leftMargin = 0.0f;
                        marginInfo.topMargin = rectF.bottom;
                    }
                }).addHighLight((int) R.id.action_more, (int) R.layout.oo, new OnPosCallback() {
                    public void getPos(float rightMargin, float bottomMargin, RectF rectF,
                                       MarginInfo marginInfo) {
                        marginInfo.rightMargin = rectF.width() / 2.0f;
                        marginInfo.bottomMargin = (float) ((DietSportCalendarActivity.this
                                .getResources().getDisplayMetrics().heightPixels / 2) + ViewUtils
                                .dip2px(DietSportCalendarActivity.this.activity, 30.0f));
                    }
                });
                highLight.show();
                highLight.setOnHighLightClickListener(new OnHighLightClickListener() {
                    public void onClick() {
                        OnePreference.setDietFoodGuide(true);
                    }
                });
            } catch (Exception e) {
            }
        }
    }

    private void saveTodayDistribution(String record_on, float breakfastCalory, float
            lunchCalory, float dinnerCalory, float snackCalory, float sportCalory) {
        if (DateFormatUtils.isToday(record_on)) {
            this.mCache.put(CacheKey.HOME_CALORIE_DISTRUBUTION, FastJsonUtils.toJson(new
                    LocalCalorieDistribution(record_on, breakfastCalory, lunchCalory,
                    dinnerCalory, snackCalory, sportCalory)), getCacheTime());
        }
    }

    private int getCacheTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(11, 24);
        calendar.set(12, 0);
        calendar.set(13, 0);
        return (int) ((calendar.getTimeInMillis() - System.currentTimeMillis()) / 1000);
    }

    protected void onSaveInstanceState(Bundle outState) {
    }

    public static String getTimeTypeName(int timeType) {
        if (timeType == 1) {
            return "早餐";
        }
        if (timeType == 2) {
            return "午餐";
        }
        if (timeType == 3) {
            return "晚餐";
        }
        if (timeType == 6) {
            return "上午加餐";
        }
        if (timeType == 7) {
            return "下午加餐";
        }
        if (timeType == 8) {
            return "晚上加餐";
        }
        return "";
    }

    public static int getTimeTypeWithName(String name) {
        if (TextUtils.equals(name, "早餐")) {
            return 1;
        }
        if (TextUtils.equals(name, "午餐")) {
            return 2;
        }
        if (TextUtils.equals(name, "晚餐")) {
            return 3;
        }
        if (TextUtils.equals(name, "上午加餐")) {
            return 6;
        }
        if (TextUtils.equals(name, "下午加餐")) {
            return 7;
        }
        if (TextUtils.equals(name, "晚上加餐")) {
            return 8;
        }
        return 0;
    }
}
