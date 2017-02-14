package com.boohee.food;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.api.FoodApi;
import com.boohee.main.GestureActivity;
import com.boohee.model.Recipe;
import com.boohee.model.RecipeCondiment;
import com.boohee.model.RecipeStep;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.Helper;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import org.json.JSONObject;

public class RecipeActivity extends GestureActivity {
    public static final String KEY_FOOD_CODE = "key_food_code";
    @InjectView(2131427843)
    ImageView    ivRecipe;
    @InjectView(2131427844)
    LinearLayout llCondiments;
    @InjectView(2131427512)
    LinearLayout llSteps;
    private LayoutInflater mInflater;
    @InjectView(2131427845)
    TextView tvTips;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cl);
        ButterKnife.inject((Activity) this);
        this.mInflater = LayoutInflater.from(this);
        requestData();
    }

    private void initView(Recipe recipe) {
        getSupportActionBar().setTitle(recipe.name);
        ImageLoader.getInstance().displayImage(recipe.image_url, this.ivRecipe,
                ImageLoaderOptions.color(R.color.ju));
        if (!TextUtils.isEmpty(recipe.tips)) {
            this.tvTips.setText(recipe.tips.trim());
        }
        initCondiment(recipe.condiments);
        initSteps(recipe.steps);
    }

    private void initCondiment(List<RecipeCondiment> condiments) {
        if (condiments != null && condiments.size() != 0) {
            int size = condiments.size();
            for (int i = 0; i < size; i++) {
                RecipeCondiment condiment = (RecipeCondiment) condiments.get(i);
                this.mInflater.inflate(R.layout.ox, this.llCondiments);
                View item = this.mInflater.inflate(R.layout.ik, null);
                ((TextView) item.findViewById(R.id.tv_name)).setText(condiment.name);
                ((TextView) item.findViewById(R.id.tv_amount)).setText(condiment.amount);
                this.llCondiments.addView(item);
            }
        }
    }

    private void initSteps(List<RecipeStep> steps) {
        if (steps != null && steps.size() != 0) {
            int size = steps.size();
            for (int i = 0; i < size; i++) {
                RecipeStep step = (RecipeStep) steps.get(i);
                this.mInflater.inflate(R.layout.ox, this.llSteps);
                View item = this.mInflater.inflate(R.layout.il, null);
                ((TextView) item.findViewById(R.id.tv_position)).setText(String.valueOf(step
                        .position));
                ((TextView) item.findViewById(R.id.tv_description)).setText(step.desc);
                ImageView ivShow = (ImageView) item.findViewById(R.id.iv_show);
                if (TextUtils.isEmpty(step.image_url)) {
                    ivShow.setVisibility(8);
                } else {
                    String nullImageUrl = "http://i1.douguo.net/img/200_cookdefault.jpg";
                    if (TextUtils.isEmpty(step.image_url) || nullImageUrl.equals(step.image_url)) {
                        ivShow.setVisibility(8);
                    } else {
                        ImageLoader.getInstance().displayImage(step.image_url, ivShow,
                                ImageLoaderOptions.color(R.color.ju));
                    }
                }
                this.llSteps.addView(item);
            }
        }
    }

    private void requestData() {
        String foodCode = getIntent().getStringExtra("key_food_code");
        if (TextUtils.isEmpty(foodCode)) {
            Helper.showLong(getString(R.string.kg));
            finish();
            return;
        }
        showLoading();
        FoodApi.getFoodsRecipe(foodCode, this.activity, new JsonCallback(this.activity) {
            public void ok(JSONObject obj) {
                Recipe recipe = Recipe.parse(obj.toString());
                if (recipe != null) {
                    RecipeActivity.this.initView(recipe);
                    return;
                }
                Helper.showLong((CharSequence) "Data error!");
                RecipeActivity.this.finish();
            }

            public void onFinish() {
                RecipeActivity.this.dismissLoading();
            }
        });
    }

    public static void comeOnBaby(Context context, String foodCode) {
        if (TextUtils.isEmpty(foodCode)) {
            Helper.showLong(context.getString(R.string.kg));
            return;
        }
        Intent intent = new Intent(context, RecipeActivity.class);
        intent.putExtra("key_food_code", foodCode);
        context.startActivity(intent);
    }
}
