package com.boohee.more;

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
import butterknife.OnClick;

import com.boohee.main.GestureActivity;
import com.boohee.one.R;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.ViewUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class EstimateFoodActivity extends GestureActivity {
    private static List<EstimateFood> baoziList   = new ArrayList();
    private static List<EstimateFood> breadList   = new ArrayList();
    private static List<EstimateFood> cerealList  = new ArrayList();
    private static List<EstimateFood> cookedList  = new ArrayList();
    private static List<EstimateFood> mantouList  = new ArrayList();
    private static List<EstimateFood> noodlesList = new ArrayList();
    private static List<EstimateFood> riceList    = new ArrayList();
    @InjectView(2131427647)
    LinearLayout llContent;

    static class EstimateFood {
        int imgResource;
        int strResource;

        public EstimateFood(int img, int str) {
            this.imgResource = img;
            this.strResource = str;
        }
    }

    static {
        riceList.add(new EstimateFood(R.drawable.adg, R.string.l6));
        riceList.add(new EstimateFood(R.drawable.adh, R.string.l6));
        riceList.add(new EstimateFood(R.drawable.adi, R.string.l7));
        noodlesList.add(new EstimateFood(R.drawable.add, R.string.l3));
        noodlesList.add(new EstimateFood(R.drawable.ade, R.string.l4));
        noodlesList.add(new EstimateFood(R.drawable.adf, R.string.l5));
        mantouList.add(new EstimateFood(R.drawable.adk, R.string.l2));
        baoziList.add(new EstimateFood(R.drawable.ad8, R.string.ki));
        breadList.add(new EstimateFood(R.drawable.ad7, R.string.kj));
        cerealList.add(new EstimateFood(R.drawable.ad9, R.string.kk));
        cerealList.add(new EstimateFood(R.drawable.ad_, R.string.kl));
        cookedList.add(new EstimateFood(R.drawable.adc, R.string.kn));
        cookedList.add(new EstimateFood(R.drawable.ada, R.string.kp));
        cookedList.add(new EstimateFood(R.drawable.adl, R.string.kr));
        cookedList.add(new EstimateFood(R.drawable.adj, R.string.ks));
        cookedList.add(new EstimateFood(R.drawable.adb, R.string.kt));
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.b9);
        ButterKnife.inject((Activity) this);
        initView();
    }

    private void initView() {
        for (EstimateFood food : cookedList) {
            initItem(food.imgResource, food.strResource);
        }
    }

    @OnClick({2131427646, 2131427641, 2131427642, 2131427643, 2131427644, 2131427645, 2131427640})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rbtn_cooked:
                refresh(cookedList);
                return;
            case R.id.rbtn_noodles:
                refresh(noodlesList);
                return;
            case R.id.rbtn_mantou:
                refresh(mantouList);
                return;
            case R.id.rbtn_baozi:
                refresh(baoziList);
                return;
            case R.id.rbtn_bread:
                refresh(breadList);
                return;
            case R.id.rbtn_cereal:
                refresh(cerealList);
                return;
            case R.id.rbtn_rice:
                refresh(riceList);
                return;
            default:
                return;
        }
    }

    private void refresh(List<EstimateFood> list) {
        this.llContent.removeAllViews();
        for (EstimateFood food : list) {
            initItem(food.imgResource, food.strResource);
        }
    }

    private void initItem(int imgResource, int strResource) {
        View view = LayoutInflater.from(this.activity).inflate(R.layout.p0, null);
        ImageView foodImg = (ImageView) view.findViewById(R.id.food_img);
        int width = ((getResources().getDisplayMetrics().widthPixels / 5) * 4) - (ViewUtils
                .dip2px(this.activity, 10.0f) * 2);
        int height = (int) (((double) width) * 0.56d);
        foodImg.getLayoutParams().width = width;
        foodImg.getLayoutParams().height = height;
        ImageLoader.getInstance().displayImage("", foodImg, ImageLoaderOptions.global(imgResource));
        ((TextView) view.findViewById(R.id.food_des)).setText(strResource);
        this.llContent.addView(view);
    }

    public static void comeOnBaby(Context context) {
        if (context != null) {
            context.startActivity(new Intent(context, EstimateFoodActivity.class));
        }
    }
}
