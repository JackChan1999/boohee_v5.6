package com.boohee.account;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.model.User;
import com.boohee.myview.DatePickerWheelView;
import com.boohee.myview.DatePickerWheelView.OnDatePickerListener;
import com.boohee.one.R;
import com.boohee.one.ui.fragment.BaseFragment;
import com.boohee.utils.DateFormatUtils;
import com.boohee.utils.WheelUtils;

import java.util.Date;

public class ProfileInitTwoFragment extends BaseFragment {
    private static final String ARG_USER = "user";
    private Date after18;
    private Date birth;
    @InjectView(2131427911)
    TextView btnNext;
    private DatePickerWheelView datePickerWheelView;
    private Date now = new Date();
    @InjectView(2131428332)
    LinearLayout pickerLayout;
    @InjectView(2131428333)
    TextView     tvBirth;
    @InjectView(2131428334)
    TextView     tvNoUse;
    private User user;

    public static ProfileInitTwoFragment newInstance(User user) {
        ProfileInitTwoFragment fragment = new ProfileInitTwoFragment();
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
        View view = inflater.inflate(R.layout.gg, container, false);
        ButterKnife.inject((Object) this, view);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        this.datePickerWheelView = new DatePickerWheelView(getActivity(), DateFormatUtils
                .string2date(this.user.birthday(), "yyyy-MM-dd"), 1946, 2004);
        this.datePickerWheelView.setOnDatePickerListener(new OnDatePickerListener() {
            public void onDatePicker(String date) {
                if (!ProfileInitTwoFragment.this.isRemoved()) {
                    ProfileInitTwoFragment.this.updateDateView(date);
                }
            }
        });
        this.pickerLayout.addView(this.datePickerWheelView);
        updateDateView(this.user.birthday());
    }

    private void updateDateView(String date) {
        if (!TextUtils.isEmpty(date) && !isRemoved()) {
            this.after18 = DateFormatUtils.getYear(date, 18);
            if (this.now.getTime() < this.after18.getTime()) {
                this.tvNoUse.setVisibility(0);
                this.btnNext.setEnabled(false);
            } else {
                this.tvNoUse.setVisibility(4);
                this.btnNext.setEnabled(true);
            }
            this.user.birthday = date;
            this.tvBirth.setText(DateFormatUtils.string2String(date, "yyyy年M月d日"));
        }
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
        ((NewUserInitActivity) getActivity()).nextStep();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void onDetach() {
        super.onDetach();
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
