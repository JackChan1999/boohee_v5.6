package com.boohee.record;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar.LayoutParams;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.api.RecordApi;
import com.boohee.main.GestureActivity;
import com.boohee.model.CopyFood;
import com.boohee.model.CopySport;
import com.boohee.model.RecordFood;
import com.boohee.model.RecordSport;
import com.boohee.myview.NewBadgeView;
import com.boohee.one.R;
import com.boohee.one.event.ConstEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.utility.Event;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.FoodUtils;
import com.boohee.utils.Helper;
import com.boohee.utils.ViewUtils;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class CopyRecordActivity extends GestureActivity {
    private static final String                KEY_COPY_DATE  = "key_copy_date";
    private static final String                KEY_DATE       = "key_date";
    private              int                   breakfastCount = 0;
    private              ArrayList<RecordFood> breakfastList  = new ArrayList();
    private              int                   copyCount      = 0;
    private              ArrayList<RecordFood> copyDietList   = new ArrayList();
    private              int                   copyFlag       = 0;
    private String copy_date;
    private int                   dinnerCount = 0;
    private ArrayList<RecordFood> dinnerList  = new ArrayList();
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
    private int                   lunchCount = 0;
    private ArrayList<RecordFood> lunchList  = new ArrayList();
    private NewBadgeView mMessageBadge;
    private String       record_on;
    private int                    requestFlag          = 0;
    private int                    snacksBreakfastCount = 0;
    private ArrayList<RecordFood>  snacksBreakfastList  = new ArrayList();
    private int                    snacksDinnerCount    = 0;
    private ArrayList<RecordFood>  snacksDinnerList     = new ArrayList();
    private int                    snacksLunchCount     = 0;
    private ArrayList<RecordFood>  snacksLunchList      = new ArrayList();
    private int                    sportCount           = 0;
    private ArrayList<RecordSport> sportList            = new ArrayList();
    @InjectView(2131427583)
    ToggleButton tb_check_all;

    public static void start(Context context, String record_on, String copy_date) {
        Intent starter = new Intent(context, CopyRecordActivity.class);
        starter.putExtra("key_date", record_on);
        starter.putExtra(KEY_COPY_DATE, copy_date);
        context.startActivity(starter);
    }

    private void handleIntent() {
        this.record_on = getStringExtra("key_date");
        this.copy_date = getStringExtra(KEY_COPY_DATE);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ay);
        ButterKnife.inject((Activity) this);
        handleIntent();
        initToolsBar();
        EventBus.getDefault().register(this);
        intBadgeView();
        refreshCount();
        getEatings();
        getActivities();
    }

    private void initToolsBar() {
        View toolbar_diet_sport = LayoutInflater.from(this).inflate(R.layout.n5, null);
        ((TextView) toolbar_diet_sport.findViewById(R.id.txt_date)).setText(TextUtils.isEmpty
                (this.record_on) ? "" : this.record_on);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutParams layoutParams = new LayoutParams(-1, -1);
        layoutParams.setMargins(0, 0, ViewUtils.dip2px(this.activity, 48.0f), 0);
        getSupportActionBar().setCustomView(toolbar_diet_sport, layoutParams);
    }

    private void intBadgeView() {
        this.mMessageBadge = new NewBadgeView(this.activity);
        this.mMessageBadge.setTargetView(findViewById(R.id.tv_copy_count));
        this.mMessageBadge.setBadgeGravity(17);
        this.mMessageBadge.setTextColor(-1);
        this.mMessageBadge.setBackground(10, getResources().getColor(R.color.he));
    }

    private void getEatings() {
        if (!TextUtils.isEmpty(this.record_on)) {
            showLoading();
            RecordApi.getEatings(this.record_on, this, new JsonCallback(this) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    CopyRecordActivity.this.initDietData(object);
                    CopyRecordActivity.this.initDietCardView(1, CopyRecordActivity.this
                            .breakfastList, CopyRecordActivity.this.ll_card_breakfast);
                    CopyRecordActivity.this.initDietCardView(2, CopyRecordActivity.this
                            .lunchList, CopyRecordActivity.this.ll_card_lunch);
                    CopyRecordActivity.this.initDietCardView(3, CopyRecordActivity.this
                            .dinnerList, CopyRecordActivity.this.ll_card_dinner);
                    CopyRecordActivity.this.initDietCardView(6, CopyRecordActivity.this
                            .snacksBreakfastList, CopyRecordActivity.this.ll_card_snacks_breakfast);
                    CopyRecordActivity.this.initDietCardView(7, CopyRecordActivity.this
                            .snacksLunchList, CopyRecordActivity.this.ll_card_snacks_lunch);
                    CopyRecordActivity.this.initDietCardView(8, CopyRecordActivity.this
                            .snacksDinnerList, CopyRecordActivity.this.ll_card_snacks_dinner);
                }

                public void onFinish() {
                    super.onFinish();
                    CopyRecordActivity.this.requestFinish();
                }
            });
        }
    }

    private void getActivities() {
        if (!TextUtils.isEmpty(this.record_on)) {
            showLoading();
            RecordApi.getActivities(this.record_on, this, new JsonCallback(this) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    CopyRecordActivity.this.initSportData(object);
                    CopyRecordActivity.this.initSportCardView(CopyRecordActivity.this.sportList,
                            CopyRecordActivity.this.ll_card_sport);
                }

                public void onFinish() {
                    super.onFinish();
                    CopyRecordActivity.this.requestFinish();
                }
            });
        }
    }

    private void initDietData(JSONObject object) {
        List<RecordFood> records = FastJsonUtils.parseList(object.optString("data"), RecordFood
                .class);
        if (records != null && records.size() > 0) {
            for (int i = 0; i < records.size(); i++) {
                RecordFood recordFood = (RecordFood) records.get(i);
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
    }

    private void initSportData(JSONObject object) {
        this.sportList.clear();
        List<RecordSport> records = RecordSport.parseList(object, "data");
        if (records != null && records.size() > 0) {
            for (int i = 0; i < records.size(); i++) {
                this.sportList.add((RecordSport) records.get(i));
            }
        }
    }

    private void initDietCardView(int time_type, ArrayList<RecordFood> foodRcordList,
                                  LinearLayout ll_card) {
        RelativeLayout rl_meal = (RelativeLayout) ll_card.findViewById(R.id.rl_time_type);
        final ToggleButton tb_time_type = (ToggleButton) ll_card.findViewById(R.id.tb_time_type);
        TextView tv_time_type = (TextView) ll_card.findViewById(R.id.tv_time_type);
        LinearLayout ll_list = (LinearLayout) ll_card.findViewById(R.id.ll_list);
        if (foodRcordList == null || foodRcordList.size() == 0) {
            ll_card.setVisibility(8);
            if (time_type == 1) {
                this.breakfastCount = 0;
            } else if (time_type == 2) {
                this.lunchCount = 0;
            } else if (time_type == 3) {
                this.dinnerCount = 0;
            } else if (time_type == 6) {
                this.snacksBreakfastCount = 0;
            } else if (time_type == 7) {
                this.snacksLunchCount = 0;
            } else if (time_type == 8) {
                this.snacksDinnerCount = 0;
            }
        } else {
            ll_card.setVisibility(0);
            ll_list.removeAllViews();
            tb_time_type.setChecked(isDietChecked(foodRcordList));
            tv_time_type.setText(FoodUtils.getDietName(this, time_type));
            int totalCount = 0;
            for (int i = 0; i < foodRcordList.size(); i++) {
                if (((RecordFood) foodRcordList.get(i)).isChecked) {
                    totalCount++;
                }
                View view = getDietItemView(time_type, foodRcordList, ll_card, i);
                if (view != null) {
                    ll_list.addView(view);
                }
            }
            if (time_type == 1) {
                this.breakfastCount = totalCount;
            } else if (time_type == 2) {
                this.lunchCount = totalCount;
            } else if (time_type == 3) {
                this.dinnerCount = totalCount;
            } else if (time_type == 6) {
                this.snacksBreakfastCount = totalCount;
            } else if (time_type == 7) {
                this.snacksLunchCount = totalCount;
            } else if (time_type == 8) {
                this.snacksDinnerCount = totalCount;
            }
        }
        refreshCount();
        final ArrayList<RecordFood> arrayList = foodRcordList;
        final int i2 = time_type;
        final LinearLayout linearLayout = ll_card;
        rl_meal.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                tb_time_type.setChecked(!tb_time_type.isChecked());
                CopyRecordActivity.this.setDietAllIsChecked(arrayList, tb_time_type.isChecked());
                CopyRecordActivity.this.initDietCardView(i2, arrayList, linearLayout);
                if (tb_time_type.isChecked()) {
                    CopyRecordActivity.this.tb_check_all.setChecked(CopyRecordActivity.this
                            .isAllChecked());
                } else {
                    CopyRecordActivity.this.tb_check_all.setChecked(false);
                }
            }
        });
    }

    private View getDietItemView(int time_type, ArrayList<RecordFood> foodRcordList, LinearLayout
            ll_card, int index) {
        View itemView = null;
        final RecordFood recordFood = (RecordFood) foodRcordList.get(index);
        if (recordFood != null) {
            itemView = LayoutInflater.from(this).inflate(R.layout.hn, null);
            final ToggleButton tb_meal = (ToggleButton) itemView.findViewById(R.id.tb_meal);
            TextView tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            TextView tv_calory = (TextView) itemView.findViewById(R.id.tv_calory);
            View view_divide = itemView.findViewById(R.id.view_divide);
            if (index == foodRcordList.size() - 1) {
                view_divide.setVisibility(8);
            }
            tv_name.setText(recordFood.food_name);
            tv_calory.setText(Math.round(recordFood.calory) + "千卡");
            tb_meal.setChecked(recordFood.isChecked);
            final int i = time_type;
            final ArrayList<RecordFood> arrayList = foodRcordList;
            final LinearLayout linearLayout = ll_card;
            itemView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    boolean z = true;
                    tb_meal.setChecked(!tb_meal.isChecked());
                    RecordFood recordFood = recordFood;
                    if (recordFood.isChecked) {
                        z = false;
                    }
                    recordFood.isChecked = z;
                    CopyRecordActivity.this.initDietCardView(i, arrayList, linearLayout);
                    if (tb_meal.isChecked()) {
                        CopyRecordActivity.this.tb_check_all.setChecked(CopyRecordActivity.this
                                .isAllChecked());
                    } else {
                        CopyRecordActivity.this.tb_check_all.setChecked(false);
                    }
                }
            });
        }
        return itemView;
    }

    private void setDietAllIsChecked(ArrayList<RecordFood> foodRcordList, boolean isChecked) {
        if (foodRcordList != null && foodRcordList.size() > 0) {
            for (int i = 0; i < foodRcordList.size(); i++) {
                ((RecordFood) foodRcordList.get(i)).isChecked = isChecked;
            }
        }
    }

    private boolean isDietChecked(ArrayList<RecordFood> foodRcordList) {
        if (foodRcordList != null && foodRcordList.size() > 0) {
            for (int i = 0; i < foodRcordList.size(); i++) {
                if (!((RecordFood) foodRcordList.get(i)).isChecked) {
                    return false;
                }
            }
        }
        return true;
    }

    private void initSportCardView(final ArrayList<RecordSport> recordSportList, final
    LinearLayout ll_card) {
        RelativeLayout rl_time_type = (RelativeLayout) ll_card.findViewById(R.id.rl_time_type);
        TextView tv_time_type = (TextView) ll_card.findViewById(R.id.tv_time_type);
        final ToggleButton tb_time_type = (ToggleButton) ll_card.findViewById(R.id.tb_time_type);
        LinearLayout ll_list = (LinearLayout) ll_card.findViewById(R.id.ll_list);
        if (recordSportList == null || recordSportList.size() == 0) {
            ll_card.setVisibility(8);
            this.sportCount = 0;
        } else {
            ll_card.setVisibility(0);
            ll_list.removeAllViews();
            tb_time_type.setChecked(isSportChecked(recordSportList));
            tv_time_type.setText("运动");
            int totalCount = 0;
            for (int i = 0; i < recordSportList.size(); i++) {
                if (((RecordSport) recordSportList.get(i)).isChecked) {
                    totalCount++;
                }
                View view = getSportItemView(recordSportList, ll_card, i);
                if (view != null) {
                    ll_list.addView(view);
                }
            }
            this.sportCount = totalCount;
        }
        refreshCount();
        rl_time_type.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                tb_time_type.setChecked(!tb_time_type.isChecked());
                CopyRecordActivity.this.setSportAllIsChecked(recordSportList, tb_time_type
                        .isChecked());
                CopyRecordActivity.this.initSportCardView(recordSportList, ll_card);
                if (tb_time_type.isChecked()) {
                    CopyRecordActivity.this.tb_check_all.setChecked(CopyRecordActivity.this
                            .isAllChecked());
                } else {
                    CopyRecordActivity.this.tb_check_all.setChecked(false);
                }
            }
        });
    }

    private View getSportItemView(ArrayList<RecordSport> sportRecordList, LinearLayout ll_card,
                                  int index) {
        View itemView = null;
        final RecordSport recordSport = (RecordSport) sportRecordList.get(index);
        if (recordSport != null) {
            itemView = LayoutInflater.from(this).inflate(R.layout.hn, null);
            TextView tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            final ToggleButton tb_meal = (ToggleButton) itemView.findViewById(R.id.tb_meal);
            tb_meal.setChecked(recordSport.isChecked);
            TextView tv_calory = (TextView) itemView.findViewById(R.id.tv_calory);
            View view_divide = itemView.findViewById(R.id.view_divide);
            if (index == sportRecordList.size() - 1) {
                view_divide.setVisibility(8);
            }
            tv_name.setText(recordSport.activity_name);
            tv_calory.setText(Math.round(recordSport.calory) + "千卡");
            final ArrayList<RecordSport> arrayList = sportRecordList;
            final LinearLayout linearLayout = ll_card;
            itemView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    boolean z = true;
                    tb_meal.setChecked(!tb_meal.isChecked());
                    RecordSport recordSport = recordSport;
                    if (recordSport.isChecked) {
                        z = false;
                    }
                    recordSport.isChecked = z;
                    CopyRecordActivity.this.initSportCardView(arrayList, linearLayout);
                    if (tb_meal.isChecked()) {
                        CopyRecordActivity.this.tb_check_all.setChecked(CopyRecordActivity.this
                                .isAllChecked());
                    } else {
                        CopyRecordActivity.this.tb_check_all.setChecked(false);
                    }
                }
            });
        }
        return itemView;
    }

    private void setSportAllIsChecked(ArrayList<RecordSport> sportRecordList, boolean isChecked) {
        if (sportRecordList != null && sportRecordList.size() > 0) {
            for (int i = 0; i < sportRecordList.size(); i++) {
                ((RecordSport) sportRecordList.get(i)).isChecked = isChecked;
            }
        }
    }

    private boolean isSportChecked(ArrayList<RecordSport> sportList) {
        for (int i = 0; i < sportList.size(); i++) {
            if (!((RecordSport) sportList.get(i)).isChecked) {
                return false;
            }
        }
        return true;
    }

    private void refreshCount() {
        this.copyCount = (((((this.breakfastCount + this.lunchCount) + this.dinnerCount) + this
                .snacksBreakfastCount) + this.snacksLunchCount) + this.snacksDinnerCount) + this
                .sportCount;
        this.mMessageBadge.setBadgeCount(this.copyCount);
        this.mMessageBadge.setVisibility(this.copyCount > 0 ? 0 : 8);
    }

    @OnClick({2131427582, 2131427584})
    public void onClick(View v) {
        boolean z = false;
        switch (v.getId()) {
            case R.id.ll_check_all:
                ToggleButton toggleButton = this.tb_check_all;
                if (!this.tb_check_all.isChecked()) {
                    z = true;
                }
                toggleButton.setChecked(z);
                checkAll(this.tb_check_all.isChecked());
                return;
            case R.id.rl_copy:
                if (this.copyCount == 0) {
                    Helper.showToast((CharSequence) "请选择复制内容");
                    return;
                }
                this.copyFlag = 0;
                copyEating();
                copyActivity();
                return;
            default:
                return;
        }
    }

    private void addToCopyDietList(ArrayList<RecordFood> foodList) {
        if (foodList != null && foodList.size() > 0) {
            this.copyDietList.addAll(foodList);
        }
    }

    private ArrayList<CopyFood> recordFoodToCopyFood(ArrayList<RecordFood> foodList) {
        ArrayList<CopyFood> copyList = new ArrayList();
        for (int i = 0; i < foodList.size(); i++) {
            if (((RecordFood) foodList.get(i)).isChecked) {
                CopyFood copyFood = new CopyFood();
                copyFood.record_on = this.copy_date;
                copyFood.code = ((RecordFood) foodList.get(i)).code;
                copyFood.food_name = ((RecordFood) foodList.get(i)).food_name;
                copyFood.food_unit_id = ((RecordFood) foodList.get(i)).food_unit_id;
                copyFood.amount = ((RecordFood) foodList.get(i)).amount;
                copyFood.calory = ((RecordFood) foodList.get(i)).calory;
                copyFood.unit_name = ((RecordFood) foodList.get(i)).unit_name;
                copyFood.time_type = ((RecordFood) foodList.get(i)).time_type;
                copyList.add(copyFood);
            }
        }
        return copyList;
    }

    private ArrayList<CopySport> recordSportToCopySport(ArrayList<RecordSport> recordList) {
        ArrayList<CopySport> copyList = new ArrayList();
        for (int i = 0; i < recordList.size(); i++) {
            if (((RecordSport) recordList.get(i)).isChecked) {
                CopySport copySport = new CopySport();
                copySport.record_on = this.copy_date;
                copySport.activity_id = ((RecordSport) recordList.get(i)).activity_id;
                copySport.duration = ((RecordSport) recordList.get(i)).duration;
                copySport.activity_name = ((RecordSport) recordList.get(i)).activity_name;
                copySport.calory = ((RecordSport) recordList.get(i)).calory;
                copySport.unit_name = ((RecordSport) recordList.get(i)).unit_name;
                copyList.add(copySport);
            }
        }
        return copyList;
    }

    private void checkAll(boolean isCheckAll) {
        setDietAllIsChecked(this.breakfastList, isCheckAll);
        setDietAllIsChecked(this.lunchList, isCheckAll);
        setDietAllIsChecked(this.dinnerList, isCheckAll);
        setDietAllIsChecked(this.snacksBreakfastList, isCheckAll);
        setDietAllIsChecked(this.snacksLunchList, isCheckAll);
        setDietAllIsChecked(this.snacksDinnerList, isCheckAll);
        setSportAllIsChecked(this.sportList, isCheckAll);
        initDietCardView(1, this.breakfastList, this.ll_card_breakfast);
        initDietCardView(2, this.lunchList, this.ll_card_lunch);
        initDietCardView(3, this.dinnerList, this.ll_card_dinner);
        initDietCardView(6, this.snacksBreakfastList, this.ll_card_snacks_breakfast);
        initDietCardView(7, this.snacksLunchList, this.ll_card_snacks_lunch);
        initDietCardView(8, this.snacksDinnerList, this.ll_card_snacks_dinner);
        initSportCardView(this.sportList, this.ll_card_sport);
    }

    private boolean isAllChecked() {
        if (isDietChecked(this.breakfastList) && isDietChecked(this.lunchList) && isDietChecked
                (this.dinnerList) && isDietChecked(this.snacksBreakfastList) && isDietChecked
                (this.snacksLunchList) && isDietChecked(this.snacksDinnerList) && isSportChecked
                (this.sportList)) {
            return true;
        }
        return false;
    }

    private void copyEating() {
        this.copyDietList.clear();
        addToCopyDietList(this.breakfastList);
        addToCopyDietList(this.lunchList);
        addToCopyDietList(this.dinnerList);
        addToCopyDietList(this.snacksBreakfastList);
        addToCopyDietList(this.snacksLunchList);
        addToCopyDietList(this.snacksDinnerList);
        if (this.copyDietList == null || this.copyDietList.size() == 0) {
            this.copyFlag++;
            return;
        }
        ArrayList<CopyFood> copyLists = recordFoodToCopyFood(this.copyDietList);
        if (copyLists == null || copyLists.size() == 0) {
            this.copyFlag++;
            return;
        }
        showLoading();
        RecordApi.batchCopyEating(copyLists, this.activity, new JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                CopyRecordActivity.this.copySuccess();
            }

            public void fail(String message) {
                super.fail(message);
                CopyRecordActivity.this.dismissLoading();
                Helper.showToast((CharSequence) "网络异常，饮食复制失败");
            }

            public void onFinish() {
                super.onFinish();
                if (CopyRecordActivity.this.copyFlag == 2) {
                    CopyRecordActivity.this.dismissLoading();
                }
            }
        });
    }

    private void copyActivity() {
        if (this.sportList == null || this.sportList.size() == 0) {
            this.copyFlag++;
            return;
        }
        ArrayList<CopySport> copyLists = recordSportToCopySport(this.sportList);
        if (copyLists == null || copyLists.size() == 0) {
            this.copyFlag++;
            return;
        }
        showLoading();
        RecordApi.batchCopyActivity(copyLists, this.activity, new JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                CopyRecordActivity.this.copySuccess();
            }

            public void fail(String message) {
                super.fail(message);
                CopyRecordActivity.this.dismissLoading();
                Helper.showToast((CharSequence) "网络异常，运动复制失败");
            }

            public void onFinish() {
                super.onFinish();
                if (CopyRecordActivity.this.copyFlag == 2) {
                    CopyRecordActivity.this.dismissLoading();
                    MobclickAgent.onEvent(CopyRecordActivity.this.activity, Event
                            .TOOL_FOODANDSPORT_COPYDONE);
                }
            }
        });
    }

    private void copySuccess() {
        this.copyFlag++;
        if (this.copyFlag == 2) {
            dismissLoading();
            EventBus.getDefault().post(new ConstEvent().setFlag(1));
            Intent intent = new Intent(this.activity, DietSportCalendarActivity.class);
            intent.addFlags(603979776);
            this.activity.startActivity(intent);
            this.activity.finish();
        }
    }

    private void requestFinish() {
        this.requestFlag++;
        if (this.requestFlag == 2) {
            dismissLoading();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(ConstEvent constEvent) {
    }
}
