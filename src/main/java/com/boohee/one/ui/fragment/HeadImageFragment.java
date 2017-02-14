package com.boohee.one.ui.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.boohee.one.R;
import com.boohee.utility.ImageLoaderOptions;

public class HeadImageFragment extends BaseFragment {
    private ImageView mImageView;
    private String    mUrl;

    public static HeadImageFragment newInstance(String url) {
        HeadImageFragment fragment = new HeadImageFragment();
        fragment.mUrl = url;
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.g0, null);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mImageView = (ImageView) getView().findViewById(R.id.image);
        if (!TextUtils.isEmpty(this.mUrl)) {
            this.imageLoader.displayImage(this.mUrl, this.mImageView, ImageLoaderOptions.global
                    (new ColorDrawable(getActivity().getResources().getColor(R.color.ju))));
        }
    }
}
