package me.nereo.multi_image_selector.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.List;
import me.nereo.multi_image_selector.MultiImagePreviewFragment;
import me.nereo.multi_image_selector.bean.Image;

public class MultiImagePreviewAdapter extends FragmentPagerAdapter {
    List<Image> mImageList;

    public MultiImagePreviewAdapter(FragmentManager fm, List<Image> imageList) {
        super(fm);
        this.mImageList = imageList;
    }

    public Fragment getItem(int position) {
        return MultiImagePreviewFragment.newInstance((Image) this.mImageList.get(position));
    }

    public int getCount() {
        return this.mImageList == null ? 0 : this.mImageList.size();
    }
}
