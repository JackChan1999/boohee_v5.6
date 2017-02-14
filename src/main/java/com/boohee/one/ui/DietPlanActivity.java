package com.boohee.one.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.api.PlanApi;
import com.boohee.food.FoodDetailActivity;
import com.boohee.model.DayPlan;
import com.boohee.model.DietPlan;
import com.boohee.model.DietPlanItem;
import com.boohee.myview.WeekView;
import com.boohee.myview.WeekView.OnSelectListener;
import com.boohee.myview.swipeback.SwipeBackActivity;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.utility.Event;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.Helper;
import com.umeng.analytics.MobclickAgent;

import java.util.Date;
import java.util.List;

import org.json.JSONObject;

public class DietPlanActivity extends SwipeBackActivity {
    @InjectView(2131427479)
    View         divider;
    @InjectView(2131427609)
    LinearLayout llBottom;
    @InjectView(2131427608)
    LinearLayout llPlan;
    @InjectView(2131427607)
    WeekView     llWeek;
    private int           orderOfToday;
    private List<DayPlan> planList;
    @InjectView(2131427612)
    View rippleCopy;
    @InjectView(2131427610)
    View rippleReset;
    private int selectDay;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, DietPlanActivity.class));
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.b4);
        ButterKnife.inject((Activity) this);
        MobclickAgent.onEvent(this, Event.bingo_clickNewDiet);
        initViews();
        initData();
    }

    private void initViews() {
        this.llWeek.setSelectListener(new OnSelectListener() {
            public void onSelect(int dayOfOrder) {
                if (DietPlanActivity.this.planList != null && DietPlanActivity.this.planList.size
                        () == 7) {
                    DietPlanActivity.this.refreshPlanView((DayPlan) DietPlanActivity.this
                            .planList.get(dayOfOrder));
                }
                DietPlanActivity.this.updateBottomLine(dayOfOrder);
                DietPlanActivity.this.selectDay = dayOfOrder;
            }
        });
        this.llWeek.setCurrentDay(new Date());
        this.orderOfToday = this.llWeek.getOrderOfToday();
        this.llWeek.select(this.orderOfToday);
        this.rippleReset.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MobclickAgent.onEvent(DietPlanActivity.this, Event.bingo_clickChangeDiet);
                DietPlanActivity.this.showLoading();
                PlanApi.resetDiet(DietPlanActivity.this.llWeek.getSelectDay(), new JsonCallback
                        (DietPlanActivity.this) {
                    public void ok(JSONObject object) {
                        if (object != null) {
                            DietPlanActivity.this.updateDayPlan((DayPlan) FastJsonUtils.fromJson
                                    (object.optString("data"), DayPlan.class));
                        }
                    }

                    public void onFinish() {
                        super.onFinish();
                        DietPlanActivity.this.dismissLoading();
                    }
                }, DietPlanActivity.this);
            }
        });
        this.rippleCopy.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MobclickAgent.onEvent(DietPlanActivity.this, Event.bingo_clickCopyDiet);
                DietPlanActivity.this.showLoading();
                PlanApi.copyDiet(new JsonCallback(DietPlanActivity.this) {
                    public void ok(JSONObject object) {
                        Helper.showToast((CharSequence) "已成功复制到饮食记录");
                    }

                    public void onFinish() {
                        DietPlanActivity.this.dismissLoading();
                    }
                }, DietPlanActivity.this);
            }
        });
    }

    private void initData() {
        showLoading();
        PlanApi.getDiet(new JsonCallback(this) {
            public void ok(JSONObject object) {
                if (object != null) {
                    List<DayPlan> plans = FastJsonUtils.parseList(object.optString("data"),
                            DayPlan.class);
                    if (plans != null && plans.size() > DietPlanActivity.this.selectDay) {
                        DietPlanActivity.this.planList = plans;
                        DietPlanActivity.this.refreshPlanView((DayPlan) plans.get
                                (DietPlanActivity.this.selectDay));
                    }
                }
            }

            public void onFinish() {
                super.onFinish();
                DietPlanActivity.this.dismissLoading();
            }
        }, this);
    }

    private void refreshPlanView(DayPlan dayPlan) {
        this.llPlan.removeAllViews();
        if (dayPlan != null && dayPlan.data != null) {
            float totalCalory = 0.0f;
            for (DietPlan plan : dayPlan.data) {
                totalCalory += plan.calory;
                View cardView = generateDietCard(plan);
                if (cardView != null) {
                    this.llPlan.addView(cardView);
                }
            }
            TextView tvTotalCalory = (TextView) LayoutInflater.from(this).inflate(R.layout.or,
                    this.llPlan, false);
            this.llPlan.addView(tvTotalCalory);
            tvTotalCalory.setText(String.format("总计：%d 千卡", new Object[]{Integer.valueOf(Math
                    .round(totalCalory))}));
        }
    }

    private View generateDietCard(DietPlan plan) {
        if (plan == null || plan.detail == null) {
            return null;
        }
        View periodView = LayoutInflater.from(this).inflate(R.layout.ob, this.llPlan, false);
        LinearLayout itemGroup = (LinearLayout) periodView.findViewById(R.id.ll_list);
        TextView tvCalory = (TextView) periodView.findViewById(R.id.tv_calory);
        ((TextView) periodView.findViewById(R.id.tv_name)).setText(plan.name);
        tvCalory.setText(Math.round(plan.calory) + "千卡");
        for (int i = 0; i < plan.detail.size(); i++) {
            boolean z;
            DietPlanItem dietPlanItem = (DietPlanItem) plan.detail.get(i);
            ViewGroup viewGroup = this.llPlan;
            if (i == plan.detail.size() - 1) {
                z = true;
            } else {
                z = false;
            }
            itemGroup.addView(generatePlanItem(dietPlanItem, viewGroup, z));
        }
        return periodView;
    }

    private View generatePlanItem(final DietPlanItem item, ViewGroup viewGroup, boolean isLast) {
        View itemView = LayoutInflater.from(this).inflate(R.layout.oc, viewGroup, false);
        ImageView ivAvatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
        TextView tvName = (TextView) itemView.findViewById(R.id.txt_name);
        TextView tvCount = (TextView) itemView.findViewById(R.id.txt_count);
        TextView tvCalory = (TextView) itemView.findViewById(R.id.txt_calory);
        View view_divide = itemView.findViewById(R.id.view_divide);
        if (isLast) {
            view_divide.setVisibility(8);
        }
        this.imageLoader.displayImage(item.image_url, ivAvatar, ImageLoaderOptions.global((int) R
                .drawable.aa2));
        tvName.setText(item.name);
        tvCount.setText(item.amount + item.unit);
        tvCalory.setText(Math.round(item.calory) + "千卡");
        itemView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                FoodDetailActivity.comeOnBaby(DietPlanActivity.this, item.code, true);
            }
        });
        return itemView;
    }

    private void updateBottomLine(int selectDay) {
        if (selectDay > this.orderOfToday) {
            this.llBottom.setVisibility(0);
            this.divider.setVisibility(0);
            this.rippleReset.setVisibility(0);
            this.rippleCopy.setVisibility(8);
        } else if (selectDay == this.orderOfToday) {
            this.llBottom.setVisibility(0);
            this.divider.setVisibility(0);
            this.rippleReset.setVisibility(0);
            this.rippleCopy.setVisibility(0);
        } else {
            this.llBottom.setVisibility(8);
            this.divider.setVisibility(8);
        }
    }

    private void updateDayPlan(DayPlan plan) {
        if (plan != null) {
            if (this.planList != null && this.planList.size() == 7) {
                this.planList.set(this.selectDay, plan);
            }
            refreshPlanView(plan);
        }
    }
}
