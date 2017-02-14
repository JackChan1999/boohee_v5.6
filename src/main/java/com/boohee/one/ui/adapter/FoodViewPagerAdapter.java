package com.boohee.one.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.boohee.model.FoodMealBean;
import com.boohee.one.ui.fragment.FoodMealFragment;

public class FoodViewPagerAdapter extends FragmentPagerAdapter {
    public static final int PAGE_BRAF = 0;
    public static final int PAGE_LUNC = 1;
    public static final int PAGE_SUPP = 2;
    private FoodMealBean mBean;

    public FoodViewPagerAdapter(FragmentManager fm, FoodMealBean bean) {
        super(fm);
        this.mBean = bean;
    }

    public Fragment getItem(int position) {
        return FoodMealFragment.newInstance(this.mBean, position);
    }

    public int getCount() {
        return 3;
    }
}
