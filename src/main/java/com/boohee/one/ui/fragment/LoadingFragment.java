package com.boohee.one.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boohee.one.R;

public class LoadingFragment extends DialogFragment {
    public static LoadingFragment newInstance() {
        LoadingFragment fragment = new LoadingFragment();
        fragment.setStyle(2, 0);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.g1, container, false);
    }
}
