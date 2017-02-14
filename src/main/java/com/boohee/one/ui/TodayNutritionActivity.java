package com.boohee.one.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.api.RecordApi;
import com.boohee.main.GestureActivity;
import com.boohee.model.Nutrition;
import com.boohee.model.NutritionItem;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.FastJsonUtils;

import java.util.List;

import org.json.JSONObject;

public class TodayNutritionActivity extends GestureActivity {
    private static final String EXTRA_DATE = "extra_date";
    @InjectView(2131427958)
    LinearLayout mLlNutritionGroup;
    private String record_on;

    public static void start(Context context, String record_on) {
        Intent starter = new Intent(context, TodayNutritionActivity.class);
        starter.putExtra(EXTRA_DATE, record_on);
        context.startActivity(starter);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dm);
        ButterKnife.inject((Activity) this);
        this.record_on = getIntent().getStringExtra(EXTRA_DATE);
        requestDailyNutrition();
    }

    private void requestDailyNutrition() {
        showLoading();
        RecordApi.getDailyNutrition(this.activity, this.record_on, new JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                List<Nutrition> nutritionList = FastJsonUtils.parseList(object.optString
                        ("nuturitions"), Nutrition.class);
                if (nutritionList != null) {
                    TodayNutritionActivity.this.initNutrition(nutritionList);
                }
            }

            public void onFinish() {
                super.onFinish();
                TodayNutritionActivity.this.dismissLoading();
            }
        });
    }

    private void initNutrition(List<Nutrition> nutritionList) {
        if (nutritionList != null && nutritionList.size() != 0) {
            this.mLlNutritionGroup.removeAllViews();
            for (Nutrition nutrition : nutritionList) {
                String str;
                View nutritionGroup = LayoutInflater.from(this).inflate(R.layout.q5, this
                        .mLlNutritionGroup, false);
                TextView groupWeight = (TextView) ButterKnife.findById(nutritionGroup, (int) R.id
                        .tv_group_weight);
                LinearLayout groupItem = (LinearLayout) ButterKnife.findById(nutritionGroup,
                        (int) R.id.ll_nutrition_item);
                ((TextView) ButterKnife.findById(nutritionGroup, (int) R.id.tv_group_name))
                        .setText(nutrition.name);
                StringBuilder stringBuilder = new StringBuilder();
                if (nutrition.weight < 0.0f) {
                    str = "0";
                } else {
                    str = String.format("%.1f", new Object[]{Float.valueOf(nutrition.weight)});
                }
                groupWeight.setText(stringBuilder.append(str).append("克").toString());
                addNutritionItem(nutrition, groupItem);
                this.mLlNutritionGroup.addView(nutritionGroup);
            }
        }
    }

    private void addNutritionItem(Nutrition nutrition, LinearLayout groupItem) {
        if (nutrition.items != null) {
            for (int i = 0; i < nutrition.items.size(); i++) {
                String str;
                NutritionItem item = (NutritionItem) nutrition.items.get(i);
                View itemView = LayoutInflater.from(this).inflate(R.layout.q6, groupItem, false);
                TextView itemName = (TextView) ButterKnife.findById(itemView, (int) R.id.tv_name);
                TextView itemWeight = (TextView) ButterKnife.findById(itemView, (int) R.id
                        .tv_weight);
                TextView itemAmount = (TextView) ButterKnife.findById(itemView, (int) R.id
                        .tv_amount);
                ImageView itemPhoto = (ImageView) ButterKnife.findById(itemView, (int) R.id
                        .iv_photo);
                ((TextView) ButterKnife.findById(itemView, (int) R.id.tv_order)).setText(String
                        .valueOf(i + 1));
                itemName.setText(item.name);
                StringBuilder stringBuilder = new StringBuilder();
                if (item.weight < 0.0f) {
                    str = "0";
                } else {
                    str = String.format("%.1f", new Object[]{Float.valueOf(item.weight)});
                }
                itemWeight.setText(stringBuilder.append(str).append("克").toString());
                itemAmount.setText(item.amount);
                this.imageLoader.displayImage(item.photo_url, itemPhoto, ImageLoaderOptions
                        .global((int) R.drawable.aa2));
                if (i == nutrition.items.size() - 1) {
                    itemView.findViewById(R.id.divider).setVisibility(8);
                }
                groupItem.addView(itemView);
            }
        }
    }
}
