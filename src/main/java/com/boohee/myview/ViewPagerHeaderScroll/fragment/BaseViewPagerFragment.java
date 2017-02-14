package com.boohee.myview.ViewPagerHeaderScroll.fragment;

import android.os.Bundle;

import com.boohee.myview.ViewPagerHeaderScroll.tools.ScrollableListener;
import com.boohee.one.ui.fragment.BaseFragment;

public abstract class BaseViewPagerFragment extends BaseFragment implements ScrollableListener {
    protected static final String BUNDLE_FRAGMENT_INDEX = "BaseFragment.BUNDLE_FRAGMENT_INDEX";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
