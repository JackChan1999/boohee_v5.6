package com.boohee.food;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.api.FoodApi;
import com.boohee.main.GestureActivity;
import com.boohee.model.CustomCook;
import com.boohee.model.CustomCookItem;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utility.TimeLinePatterns;
import com.boohee.utils.FastJsonUtils;
import com.boohee.widgets.RoundedCornersImage;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

public class CustomCookActivity extends GestureActivity {
    private static final String CUSTOM_COOK = "CUSTOM_COOK";
    private int            cookId;
    private CustomCookItem cookItem;
    @InjectView(2131427457)
    RoundedCornersImage ivCookImg;
    @InjectView(2131427459)
    LinearLayout        llFoodMaterial;
    @InjectView(2131427454)
    RelativeLayout      rlCookPhoto;
    @InjectView(2131427461)
    TextView            tvCalory;
    @InjectView(2131427453)
    TextView            tvCookName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.b0);
        ButterKnife.inject((Activity) this);
        this.cookId = getIntExtra(CUSTOM_COOK);
        if (this.cookId > 0) {
            loadData();
        } else {
            finish();
        }
    }

    private void loadData() {
        showLoading();
        FoodApi.getCustomCookDetail(this.activity, this.cookId, new JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                CustomCookActivity.this.cookItem = (CustomCookItem) FastJsonUtils.fromJson
                        (object, CustomCookItem.class);
                if (CustomCookActivity.this.cookItem != null) {
                    CustomCookActivity.this.initView();
                }
            }

            public void onFinish() {
                super.onFinish();
                CustomCookActivity.this.dismissLoading();
            }
        });
    }

    private void initView() {
        if (this.cookItem != null) {
            this.tvCookName.setText(this.cookItem.name);
            if (TextUtils.isEmpty(this.cookItem.photo)) {
                this.rlCookPhoto.setVisibility(8);
            } else {
                ImageLoader.getInstance().displayImage(this.cookItem.photo.contains
                        (TimeLinePatterns.WEB_SCHEME) ? this.cookItem.photo : TimeLinePatterns
                        .WEB_SCHEME + this.cookItem.photo, this.ivCookImg, ImageLoaderOptions
                        .global((int) R.drawable.aa2));
            }
            this.tvCalory.setText(String.format("%.1f", new Object[]{Float.valueOf(this.cookItem
                    .calory)}));
            if (this.cookItem.materials != null && this.cookItem.materials.size() > 0) {
                for (CustomCook cook : this.cookItem.materials) {
                    addFoodMaterial(cook);
                }
            }
        }
    }

    private void addFoodMaterial(CustomCook customCook) {
        View item = LayoutInflater.from(this.activity).inflate(R.layout.ht, null);
        ((TextView) item.findViewById(R.id.tv_name)).setText(customCook.food_name);
        ((TextView) item.findViewById(R.id.tv_amount)).setText(customCook.amount + customCook
                .unit_name);
        this.llFoodMaterial.addView(item);
    }

    public static void comeOnBaby(Context context, int cookId) {
        Intent intent = new Intent(context, CustomCookActivity.class);
        intent.putExtra(CUSTOM_COOK, cookId);
        context.startActivity(intent);
    }
}
