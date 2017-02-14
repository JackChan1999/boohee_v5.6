package com.boohee.account;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.boohee.one.R;

public class ImageFragment extends Fragment {
    private ImageView mImageView;
    private int       mResId;

    public static ImageFragment newInstance(int resId) {
        ImageFragment fragment = new ImageFragment();
        fragment.mResId = resId;
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.g0, null);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initUI();
    }

    private void initUI() {
        this.mImageView = (ImageView) getView().findViewById(R.id.image);
        this.mImageView.setImageResource(this.mResId);
    }
}
