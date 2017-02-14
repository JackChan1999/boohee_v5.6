package com.boohee.one.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.boohee.model.Recommend;
import com.boohee.one.ui.fragment.RecommendImageFragment;

import java.util.List;

public class RecommendImagePagerAdapter extends FragmentPagerAdapter {
    private List<Recommend> mRecommends;

    public RecommendImagePagerAdapter(FragmentManager fm, List<Recommend> recommends) {
        super(fm);
        this.mRecommends = recommends;
    }

    public Fragment getItem(int arg0) {
        return RecommendImageFragment.newInstance((Recommend) this.mRecommends.get(arg0));
    }

    public int getCount() {
        return this.mRecommends == null ? 0 : this.mRecommends.size();
    }

    public Object instantiateItem(ViewGroup container, int position) {
        RecommendImageFragment f = (RecommendImageFragment) super.instantiateItem(container,
                position);
        f.setmRecommend((Recommend) this.mRecommends.get(position));
        return f;
    }

    public int getItemPosition(Object object) {
        return -2;
    }
}
