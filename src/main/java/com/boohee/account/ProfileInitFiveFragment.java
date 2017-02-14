package com.boohee.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.baidu.location.aj;
import com.boohee.model.User;
import com.boohee.myview.BooheeRulerView;
import com.boohee.myview.BooheeRulerView.OnValueChangeListener;
import com.boohee.one.R;
import com.boohee.one.ui.fragment.BaseFragment;
import com.boohee.utils.DateFormatUtils;
import com.boohee.utils.WheelUtils;

import java.util.Date;

import lecho.lib.hellocharts.gesture.ChartZoomer;

public class ProfileInitFiveFragment extends BaseFragment {
    private static final String ARG_USER = "user";
    public static final  String FEMALE   = "2";
    public static final  String MALE     = "1";
    @InjectView(2131427911)
    TextView btnNext;
    @InjectView(2131428310)
    TextView date;
    private float losePerWeek;
    @InjectView(2131428312)
    BooheeRulerView rvTargetWeight;
    private String targetDate;
    @InjectView(2131428311)
    TextView tvLose;
    @InjectView(2131428313)
    TextView tvTooLow;
    @InjectView(2131428309)
    TextView tvWeek;
    private User user;
    private int  week;

    public static ProfileInitFiveFragment newInstance(User user) {
        ProfileInitFiveFragment fragment = new ProfileInitFiveFragment();
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
        View view = inflater.inflate(R.layout.gc, container, false);
        ButterKnife.inject((Object) this, view);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        this.rvTargetWeight.init(0.1f, 1.0f, ChartZoomer.ZOOM_AMOUNT, 0.1f, 2, new
                OnValueChangeListener() {
            public void onValueChange(float value) {
                if (!ProfileInitFiveFragment.this.isRemoved()) {
                    ProfileInitFiveFragment.this.losePerWeek = value;
                    ProfileInitFiveFragment.this.refreshView();
                }
            }
        });
    }

    private void refreshView() {
        if (((double) ((this.losePerWeek * aj.hA) / this.user.beginWeight())) > 0.04d) {
            this.tvTooLow.setVisibility(0);
        } else {
            this.tvTooLow.setVisibility(4);
        }
        this.tvLose.setText(String.valueOf(this.losePerWeek) + "公斤");
        this.week = (int) Math.ceil((double) ((this.user.beginWeight() - this.user.targetWeight()
        ) / this.losePerWeek));
        this.tvWeek.setText(this.week + "周");
        this.targetDate = DateFormatUtils.date2string(DateFormatUtils.adjustDateByDay
                (DateFormatUtils.date2string(new Date(), "yyyy-MM-dd"), this.week * 7),
                "yyyy-MM-dd");
        this.date.setText(this.targetDate + "，" + (this.week * 7) + "天之后");
    }

    @OnClick({2131427911})
    public void onClick(View view) {
        if (!isRemoved() && !WheelUtils.isFastDoubleClick()) {
            switch (view.getId()) {
                case R.id.btn_next:
                    onChangeProfile();
                    return;
                default:
                    return;
            }
        }
    }

    public void onChangeProfile() {
        this.user.target_date = this.targetDate;
        ((NewUserInitActivity) getActivity()).savaChange();
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
