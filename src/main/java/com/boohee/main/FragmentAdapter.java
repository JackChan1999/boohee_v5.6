package com.boohee.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class FragmentAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> frags;
    private String[]            titles;

    public FragmentAdapter(FragmentManager fm, ArrayList<Fragment> frags, String[] titles) {
        super(fm);
        this.frags = frags;
        this.titles = titles;
    }

    public Fragment getItem(int position) {
        return (Fragment) this.frags.get(position);
    }

    public int getCount() {
        return this.frags.size();
    }

    public CharSequence getPageTitle(int position) {
        if (this.titles == null) {
            return "";
        }
        return this.titles[position];
    }
}
