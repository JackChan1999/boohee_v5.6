package com.boohee.food;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.api.FoodApi;
import com.boohee.main.GestureActivity;
import com.boohee.model.BooheeRecipe;
import com.boohee.model.Materials.Material;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.utils.Helper;

import java.util.Iterator;

import org.json.JSONObject;

public class BooheeRecipeActivity extends GestureActivity {
    public static final String KEY_FOOD_CODE = "key_food_code";
    @InjectView(2131427506)
    LinearLayout llMajor;
    @InjectView(2131427507)
    LinearLayout llMajorContent;
    @InjectView(2131427508)
    LinearLayout llMinor;
    @InjectView(2131427509)
    LinearLayout llMinorContent;
    @InjectView(2131427504)
    LinearLayout llRaw;
    @InjectView(2131427505)
    LinearLayout llRawContent;
    @InjectView(2131427510)
    LinearLayout llSeasoning;
    @InjectView(2131427511)
    LinearLayout llSeasoningContent;
    @InjectView(2131427512)
    LinearLayout llSteps;
    private LayoutInflater mInflater;
    @InjectView(2131427513)
    TextView tvStep;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.al);
        ButterKnife.inject((Activity) this);
        this.mInflater = LayoutInflater.from(this);
        requestData();
    }

    private void requestData() {
        String foodCode = getIntent().getStringExtra("key_food_code");
        if (TextUtils.isEmpty(foodCode)) {
            Helper.showLong(getString(R.string.kg));
            finish();
            return;
        }
        showLoading();
        FoodApi.getWholeFoodsRecipe(foodCode, this.activity, new JsonCallback(this.activity) {
            public void ok(JSONObject obj) {
                BooheeRecipe recipe = BooheeRecipe.parse(obj.toString());
                if (recipe != null) {
                    BooheeRecipeActivity.this.initView(recipe);
                }
            }

            public void onFinish() {
                BooheeRecipeActivity.this.dismissLoading();
            }
        });
    }

    private void initView(BooheeRecipe recipe) {
        if (recipe != null) {
            Iterator it;
            Material material;
            if (recipe.data.materials.raw == null || recipe.data.materials.raw.size() <= 0) {
                this.llRaw.setVisibility(8);
            } else {
                it = recipe.data.materials.raw.iterator();
                while (it.hasNext()) {
                    material = (Material) it.next();
                    addContentView(this.llRawContent, material.name, material.weight);
                }
            }
            if (recipe.data.materials.major_materials == null || recipe.data.materials
                    .major_materials.size() <= 0) {
                this.llMajor.setVisibility(8);
            } else {
                it = recipe.data.materials.major_materials.iterator();
                while (it.hasNext()) {
                    material = (Material) it.next();
                    addContentView(this.llMajorContent, material.name, material.weight);
                }
            }
            if (recipe.data.materials.minor_materials == null || recipe.data.materials
                    .minor_materials.size() <= 0) {
                this.llMinor.setVisibility(8);
            } else {
                it = recipe.data.materials.minor_materials.iterator();
                while (it.hasNext()) {
                    material = (Material) it.next();
                    addContentView(this.llMinorContent, material.name, material.weight);
                }
            }
            if (recipe.data.materials.seasoning == null || recipe.data.materials.seasoning.size()
                    <= 0) {
                this.llSeasoning.setVisibility(8);
            } else {
                it = recipe.data.materials.seasoning.iterator();
                while (it.hasNext()) {
                    material = (Material) it.next();
                    addContentView(this.llSeasoningContent, material.name, material.weight);
                }
            }
            if (TextUtils.isEmpty(recipe.data.ext)) {
                this.llSteps.setVisibility(8);
            } else {
                this.tvStep.setText(recipe.data.ext);
            }
        }
    }

    private void addContentView(ViewGroup layout, String name, float weight) {
        View item = this.mInflater.inflate(R.layout.ik, null);
        ((TextView) item.findViewById(R.id.tv_name)).setText(name);
        ((TextView) item.findViewById(R.id.tv_amount)).setText(String.format("%.1f克", new
                Object[]{Float.valueOf(weight)}));
        layout.addView(item);
        this.mInflater.inflate(R.layout.ox, layout);
    }

    public static void comeOnBaby(Context context, String foodCode) {
        if (!TextUtils.isEmpty(foodCode)) {
            Intent intent = new Intent(context, BooheeRecipeActivity.class);
            intent.putExtra("key_food_code", foodCode);
            context.startActivity(intent);
        }
    }
}
