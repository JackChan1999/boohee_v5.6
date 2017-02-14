package com.boohee.one.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.boohee.model.mine.WeightPreviewPhoto;
import com.boohee.one.ui.fragment.WeightImageFragment;

import java.util.List;

public class WeightImagePagerAdapter extends FragmentPagerAdapter {
    private float                    mHeight;
    private List<WeightPreviewPhoto> mWeightPreviewPhotos;

    public WeightImagePagerAdapter(FragmentManager fm, List<WeightPreviewPhoto> previewPhotos,
                                   float height) {
        super(fm);
        this.mWeightPreviewPhotos = previewPhotos;
        this.mHeight = height;
    }

    public Fragment getItem(int arg0) {
        return WeightImageFragment.newInstance((WeightPreviewPhoto) this.mWeightPreviewPhotos.get
                (arg0), this.mHeight);
    }

    public int getCount() {
        return this.mWeightPreviewPhotos == null ? 0 : this.mWeightPreviewPhotos.size();
    }

    public Object instantiateItem(ViewGroup container, int position) {
        WeightImageFragment f = (WeightImageFragment) super.instantiateItem(container, position);
        f.setWeightRecord((WeightPreviewPhoto) this.mWeightPreviewPhotos.get(position));
        f.setHeight(this.mHeight);
        return f;
    }

    public int getItemPosition(Object object) {
        return -2;
    }
}
