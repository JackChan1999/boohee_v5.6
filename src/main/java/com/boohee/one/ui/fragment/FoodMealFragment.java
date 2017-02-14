package com.boohee.one.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boohee.model.FoodMealBean;
import com.boohee.one.R;
import com.boohee.one.ui.SelectStatusActivity;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.widgets.RoundedCornersImage;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class FoodMealFragment extends BaseFragment {
    private static final String[] descs  = new String[]{"一日之计在于晨", "满足、轻盈两不误", "晚餐适量更易瘦"};
    private static final String[] meals  = new String[]{"breakfast", "lunch",
            SelectStatusActivity.VAL_SUPPER};
    private static final String[] titles = new String[]{"早餐", "午餐", "晚餐"};
    private TextView desc;
    private List<RoundedCornersImage> imageList = new ArrayList();
    private FoodMealBean mBean;
    private int          mType;
    private TextView     title;

    public static FoodMealFragment newInstance(FoodMealBean bean, int type) {
        FoodMealFragment instance = new FoodMealFragment();
        instance.mBean = bean;
        instance.mType = type;
        return instance;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fu, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView();
        addListener();
    }

    private void findView() {
        this.imageList.clear();
        this.title = (TextView) getView().findViewById(R.id.title);
        this.desc = (TextView) getView().findViewById(R.id.desc);
        this.imageList.add((RoundedCornersImage) getView().findViewById(R.id.image1));
        this.imageList.add((RoundedCornersImage) getView().findViewById(R.id.image2));
        this.imageList.add((RoundedCornersImage) getView().findViewById(R.id.image3));
    }

    private void addListener() {
        getView().setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(FoodMealFragment.this.getActivity(),
                        SelectStatusActivity.class);
                intent.putExtra("meal_type", FoodMealFragment.meals[FoodMealFragment.this.mType]);
                FoodMealFragment.this.startActivity(intent);
            }
        });
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        if (this.mBean != null) {
            this.title.setText(titles[this.mType]);
            this.desc.setText(descs[this.mType]);
            switch (this.mType) {
                case 0:
                    loadImage(this.mBean.getBreakfast());
                    return;
                case 1:
                    loadImage(this.mBean.getLunch());
                    return;
                case 2:
                    loadImage(this.mBean.getSupper());
                    return;
                default:
                    return;
            }
        }
    }

    private void loadImage(List<String> imageUrls) {
        int i = 0;
        while (i < this.imageList.size() && i < imageUrls.size()) {
            ImageLoader.getInstance().displayImage((String) imageUrls.get(i), (ImageView) this
                    .imageList.get(i), ImageLoaderOptions.global((int) R.drawable.l9));
            i++;
        }
    }
}
