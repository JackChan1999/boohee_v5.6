package com.boohee.one.bet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boohee.one.bet.model.BetGirl;
import com.boohee.one.ui.fragment.BaseFragment;
import com.nostra13.universalimageloader.core.ImageLoader;

public class BetGirlFragment extends BaseFragment {
    private BetGirl betGirl;
    protected ImageLoader imageLoader = ImageLoader.getInstance();

    public void setBetGirl(BetGirl betGirl) {
        this.betGirl = betGirl;
    }

    public static BetGirlFragment newInstance(BetGirl betGirl) {
        BetGirlFragment fragment = new BetGirlFragment();
        fragment.betGirl = betGirl;
        return fragment;
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
    }
}
