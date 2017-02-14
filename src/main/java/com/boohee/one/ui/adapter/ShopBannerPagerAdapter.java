package com.boohee.one.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.boohee.model.Showcase;
import com.boohee.one.ui.fragment.ShopBannerFragment;

import java.util.List;

public class ShopBannerPagerAdapter extends FragmentPagerAdapter {
    private List<Showcase> showcases;

    public ShopBannerPagerAdapter(FragmentManager fm, List<Showcase> showcases) {
        super(fm);
        this.showcases = showcases;
    }

    public Fragment getItem(int arg0) {
        return ShopBannerFragment.newInstance((Showcase) this.showcases.get(arg0));
    }

    public int getCount() {
        return this.showcases == null ? 0 : this.showcases.size();
    }

    public Object instantiateItem(ViewGroup container, int position) {
        ShopBannerFragment f = (ShopBannerFragment) super.instantiateItem(container, position);
        f.setShowcase((Showcase) this.showcases.get(position));
        return f;
    }

    public int getItemPosition(Object object) {
        return -2;
    }
}
