package com.boohee.one.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class ArrayPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments;
    private String[]       mTitles;

    public ArrayPagerAdapter(FragmentManager fm, List<Fragment> fragments, String[] titles) {
        super(fm);
        this.mFragments = fragments;
        this.mTitles = titles;
    }

    public Fragment getItem(int position) {
        return (Fragment) this.mFragments.get(position);
    }

    public int getCount() {
        return this.mFragments.size();
    }

    public CharSequence getPageTitle(int position) {
        return this.mTitles[position];
    }
}
