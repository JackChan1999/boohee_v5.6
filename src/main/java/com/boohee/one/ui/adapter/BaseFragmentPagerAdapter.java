package com.boohee.one.ui.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.boohee.one.ui.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class BaseFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private final List<String>       mFragmentTitles = new ArrayList();
    private final List<BaseFragment> mFragments      = new ArrayList();

    public void addFragment(BaseFragment fragment, String title) {
        this.mFragments.add(fragment);
        this.mFragmentTitles.add(title);
    }

    public void addFragment(BaseFragment fragment) {
        this.mFragments.add(fragment);
    }

    public BaseFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public BaseFragment getItem(int position) {
        return (BaseFragment) this.mFragments.get(position);
    }

    public int getCount() {
        return this.mFragments.size();
    }

    public CharSequence getPageTitle(int position) {
        return (CharSequence) this.mFragmentTitles.get(position);
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
