package com.boohee.one.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.boohee.model.status.Photo;
import com.boohee.one.ui.fragment.PhotoImageFragment;

import java.util.List;

public class PhotoImagePagerAdapter extends FragmentPagerAdapter {
    private List<Photo> photoList;

    public PhotoImagePagerAdapter(FragmentManager fm, List<Photo> photoList) {
        super(fm);
        this.photoList = photoList;
    }

    public Fragment getItem(int position) {
        return PhotoImageFragment.newInstance((Photo) this.photoList.get(position));
    }

    public int getCount() {
        return this.photoList == null ? 0 : this.photoList.size();
    }

    public Object instantiateItem(ViewGroup container, int position) {
        PhotoImageFragment f = (PhotoImageFragment) super.instantiateItem(container, position);
        f.setPhoto((Photo) this.photoList.get(position));
        return f;
    }

    public int getItemPosition(Object object) {
        return -2;
    }
}
