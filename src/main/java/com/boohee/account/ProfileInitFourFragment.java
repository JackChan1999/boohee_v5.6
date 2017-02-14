package com.boohee.account;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.model.User;
import com.boohee.myview.BooheeRulerView;
import com.boohee.myview.BooheeRulerView.OnValueChangeListener;
import com.boohee.one.R;
import com.boohee.one.ui.fragment.BaseFragment;
import com.boohee.utils.Helper;
import com.boohee.utils.Utils;
import com.boohee.utils.WheelUtils;

public class ProfileInitFourFragment extends BaseFragment {
    private static final String ARG_USER = "user";
    public static final  String FEMALE   = "2";
    public static final  String MALE     = "1";
    private float beginTargetWeight;
    @InjectView(2131427911)
    TextView btnNext;
    private float currentTargetWeight;
    private float endTargetWeight;
    private float healthTargetWeight;
    @InjectView(2131428312)
    BooheeRulerView rvTargetWeight;
    private float suggestTargetWeight;
    private float targetWeight;
    @InjectView(2131428314)
    TextView tvSuggestWeight;
    @InjectView(2131428315)
    TextView tvTargetWeight;
    @InjectView(2131428313)
    TextView tvTooLow;
    private User user;

    public static ProfileInitFourFragment newInstance(User user) {
        ProfileInitFourFragment fragment = new ProfileInitFourFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.user = (User) getArguments().getSerializable("user");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.gd, container, false);
        ButterKnife.inject((Object) this, view);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        this.beginTargetWeight = Utils.calWeightWithBmiAndHeigt(15.0f, this.user.height());
        this.endTargetWeight = this.user.beginWeight();
        this.currentTargetWeight = this.user.target_weight == 0.0f ? Utils
                .calWeightWithBmiAndHeigt(20.0f, this.user.height()) : this.user.target_weight;
        this.suggestTargetWeight = Utils.calWeightWithBmiAndHeigt(18.5f, this.user.height());
        if (TextUtils.equals(this.user.sexType(), "2")) {
            this.suggestTargetWeight -= 2.5f;
        }
        this.suggestTargetWeight = ((float) Math.round(this.suggestTargetWeight * 10.0f)) / 10.0f;
        this.tvSuggestWeight.setText(this.suggestTargetWeight + "公斤 ~ " + Utils
                .calWeightWithBmiAndHeigt(24.0f, this.user.height()) + "公斤");
        this.tvSuggestWeight.setText(String.valueOf(this.suggestTargetWeight));
        this.healthTargetWeight = (18.0f * (this.user.height() / 100.0f)) * (this.user.height() /
                100.0f);
        this.rvTargetWeight.init(this.beginTargetWeight, this.endTargetWeight, this
                .currentTargetWeight, 1.0f, 10, new OnValueChangeListener() {
            public void onValueChange(float value) {
                if (!ProfileInitFourFragment.this.isRemoved()) {
                    ProfileInitFourFragment.this.targetWeight = value;
                    if (value < ProfileInitFourFragment.this.healthTargetWeight) {
                        ProfileInitFourFragment.this.tvTooLow.setVisibility(0);
                    } else {
                        ProfileInitFourFragment.this.tvTooLow.setVisibility(4);
                    }
                    ProfileInitFourFragment.this.tvTargetWeight.setText(String.valueOf(value) +
                            "公斤");
                }
            }
        });
    }

    @OnClick({2131427911})
    public void onClick(View view) {
        if (!isRemoved() && !WheelUtils.isFastDoubleClick()) {
            switch (view.getId()) {
                case R.id.btn_next:
                    if (this.targetWeight <= 0.0f) {
                        Helper.showToast((int) R.string.a0b);
                        return;
                    } else {
                        onChangeProfile();
                        return;
                    }
                default:
                    return;
            }
        }
    }

    public void onChangeProfile() {
        this.user.target_weight = this.targetWeight;
        ((NewUserInitActivity) getActivity()).nextStep();
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
