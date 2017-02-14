package com.boohee.account;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ImagePagerAdapter extends FragmentPagerAdapter {
    private int[]    mImageIds;
    private String[] urls;

    public ImagePagerAdapter(FragmentManager fm, String[] urls) {
        super(fm);
        this.urls = urls;
    }

    public ImagePagerAdapter(FragmentManager fm, int[] imageIds) {
        super(fm);
        this.mImageIds = imageIds;
    }

    public Fragment getItem(int arg0) {
        return ImageFragment.newInstance(this.mImageIds[arg0]);
    }

    public int getCount() {
        return this.mImageIds.length;
    }
}
