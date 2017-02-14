package com.boohee.account;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.model.User;
import com.boohee.myview.BooheeRulerView;
import com.boohee.myview.BooheeRulerView.OnValueChangeListener;
import com.boohee.myview.DatePickerWheelView;
import com.boohee.myview.DatePickerWheelView.OnDatePickerListener;
import com.boohee.one.R;
import com.boohee.one.ui.fragment.BaseFragment;
import com.boohee.utils.DateFormatUtils;
import com.boohee.utils.DateHelper;
import com.boohee.utils.Utils;
import com.boohee.utils.WheelUtils;

import java.util.Calendar;

public class ProfileInitThreeFragment extends BaseFragment {
    private static final String ARG_USER = "user";
    @InjectView(2131427911)
    TextView btnNext;
    private DatePickerWheelView datePickerWheelView;
    private float               defaultWeight;
    private Handler handler = new Handler();
    private boolean isDatePickerOpened;
    @InjectView(2131428332)
    LinearLayout    pickerLayout;
    @InjectView(2131428330)
    RadioButton     rbTargetKeep;
    @InjectView(2131428329)
    RadioButton     rbTargetLoseWeight;
    @InjectView(2131428328)
    RadioGroup      rgTarget;
    @InjectView(2131428324)
    RelativeLayout  rlDate;
    @InjectView(2131428327)
    BooheeRulerView rvWeight;
    private float target;
    @InjectView(2131428325)
    TextView tvBeginDate;
    @InjectView(2131428331)
    TextView tvSuggest;
    @InjectView(2131427651)
    TextView tvWeight;
    private User user;

    public static ProfileInitThreeFragment newInstance(User user) {
        ProfileInitThreeFragment fragment = new ProfileInitThreeFragment();
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
        View view = inflater.inflate(R.layout.gf, container, false);
        ButterKnife.inject((Object) this, view);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        float calWeightWithBmiAndHeigt;
        if (this.user.begin_weight == 0.0f) {
            calWeightWithBmiAndHeigt = Utils.calWeightWithBmiAndHeigt(22.0f, this.user.height);
        } else {
            calWeightWithBmiAndHeigt = this.user.begin_weight;
        }
        this.defaultWeight = calWeightWithBmiAndHeigt;
        updateTargetView(this.user.targetWeight());
        this.rvWeight.init(Utils.calWeightWithBmiAndHeigt(15.5f, this.user.height()), 200.0f,
                this.defaultWeight, 1.0f, 10, new OnValueChangeListener() {
            public void onValueChange(float value) {
                if (!ProfileInitThreeFragment.this.isRemoved()) {
                    ProfileInitThreeFragment.this.updateWeightView(value);
                }
            }
        });
        this.rgTarget.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                ProfileInitThreeFragment.this.target = Float.valueOf(String.valueOf(group
                        .findViewById(checkedId).getTag())).floatValue();
                ProfileInitThreeFragment.this.updateTargetView(ProfileInitThreeFragment.this
                        .target);
            }
        });
        if (this.user.target_weight > 0.0f) {
            this.rgTarget.check(R.id.rb_target_lose_weight);
        } else {
            this.rgTarget.check(R.id.rb_target_keep);
        }
        updateDateView(this.user.beginDate());
    }

    private void updateWeightView(float value) {
        this.tvWeight.setText(String.valueOf(value) + "公斤");
        this.user.begin_weight = value;
        updateSuggestView();
    }

    private void updateSuggestView() {
        float value = this.user.beginWeight();
        float upper = Utils.calWeightWithBmiAndHeigt(24.0f, this.user.height);
        if (value > Utils.calWeightWithBmiAndHeigt(18.5f, this.user.height) && value < upper) {
            this.tvSuggest.setVisibility(4);
        } else if (value >= upper) {
            if (this.target == -1.0f) {
                this.tvSuggest.setVisibility(0);
                this.tvSuggest.setText("你当前体重偏重，建议你减重");
                return;
            }
            this.tvSuggest.setVisibility(4);
        } else if (this.target >= 0.0f) {
            this.tvSuggest.setVisibility(0);
            this.tvSuggest.setText("你当前体重适中，建议你保持");
        } else {
            this.tvSuggest.setVisibility(4);
        }
    }

    private void updateTargetView(float target) {
        if (target == -1.0f) {
            this.btnNext.setText(R.string.ge);
            this.user.target_weight = -1.0f;
        } else {
            this.btnNext.setText(R.string.x4);
            if (this.user.target_weight <= 0.0f) {
                this.user.target_weight = 0.0f;
            }
        }
        updateSuggestView();
    }

    @OnClick({2131427911, 2131427647, 2131428326})
    public void onClick(View view) {
        if (!isRemoved() && !WheelUtils.isFastDoubleClick()) {
            switch (view.getId()) {
                case R.id.ll_content:
                    if (this.isDatePickerOpened) {
                        toggleDatePicker();
                        return;
                    }
                    return;
                case R.id.btn_next:
                    onChangeProfile();
                    return;
                case R.id.btn_pick_date:
                    toggleDatePicker();
                    return;
                default:
                    return;
            }
        }
    }

    private void toggleDatePicker() {
        if (this.user.targetWeight() != -1.0f) {
            if (this.isDatePickerOpened) {
                this.isDatePickerOpened = false;
                this.pickerLayout.removeAllViews();
                this.pickerLayout.setVisibility(8);
                return;
            }
            this.isDatePickerOpened = true;
            this.pickerLayout.setVisibility(0);
            int maxYear = Calendar.getInstance().get(1);
            this.datePickerWheelView = new DatePickerWheelView(getActivity(), DateFormatUtils
                    .string2date(this.user.beginDate(), "yyyy-MM-dd"), maxYear - 2, maxYear);
            this.datePickerWheelView.setOnDatePickerListener(new OnDatePickerListener() {
                public void onDatePicker(String date) {
                    if (!ProfileInitThreeFragment.this.isRemoved()) {
                        ProfileInitThreeFragment.this.updateDateView(date);
                    }
                }
            });
            this.pickerLayout.addView(this.datePickerWheelView);
        }
    }

    private void updateDateView(String date) {
        if (this.user.targetWeight() == -1.0f) {
            this.rlDate.setVisibility(8);
            return;
        }
        this.rlDate.setVisibility(8);
        this.user.begin_date = DateHelper.today();
    }

    public void onChangeProfile() {
        if (this.user.targetWeight() == -1.0f) {
            ((NewUserInitActivity) getActivity()).savaChange();
        } else {
            ((NewUserInitActivity) getActivity()).nextStep();
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
