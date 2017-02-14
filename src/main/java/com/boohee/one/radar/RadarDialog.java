package com.boohee.one.radar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;

import com.boohee.one.R;
import com.boohee.one.ui.fragment.BaseDialogFragment;

public class RadarDialog extends BaseDialogFragment {
    public static RadarDialog newInstance() {
        return new RadarDialog();
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f0, container, false);
        ButterKnife.inject((Object) this, view);
        return view;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({2131428129})
    public void close() {
        dismissAllowingStateLoss();
    }
}
