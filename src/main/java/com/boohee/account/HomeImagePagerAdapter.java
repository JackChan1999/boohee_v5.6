package com.boohee.account;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.boohee.model.CustomSliderImage;

import java.util.List;

public class HomeImagePagerAdapter<T extends CustomSliderImage> extends FragmentPagerAdapter {
    private List<T> datas;

    public HomeImagePagerAdapter(FragmentManager fm, List<T> datas) {
        super(fm);
        this.datas = datas;
    }

    public Fragment getItem(int arg0) {
        HomeImageFragment fragment = new HomeImageFragment();
        fragment.setData((CustomSliderImage) this.datas.get(arg0));
        fragment.initUI();
        return fragment;
    }

    public int getCount() {
        return this.datas == null ? 0 : this.datas.size();
    }

    public Object instantiateItem(ViewGroup container, int position) {
        HomeImageFragment f = (HomeImageFragment) super.instantiateItem(container, position);
        f.setData((CustomSliderImage) this.datas.get(position));
        return f;
    }

    public int getItemPosition(Object object) {
        return -2;
    }
}
