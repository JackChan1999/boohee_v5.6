package com.boohee.account;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.boohee.utils.WheelUtils;

public class ProfileInitOneFragment extends BaseFragment {
    private static final String ARG_USER = "user";
    public static final  String FEMALE   = "2";
    public static final  String MALE     = "1";
    private float height;
    @InjectView(2131428320)
    ImageView       ivFemale;
    @InjectView(2131428317)
    ImageView       ivMale;
    @InjectView(2131428319)
    LinearLayout    llFemale;
    @InjectView(2131428316)
    LinearLayout    llMale;
    @InjectView(2131428323)
    BooheeRulerView rvHeight;
    private String sexType;
    @InjectView(2131428321)
    TextView tvFemale;
    @InjectView(2131428322)
    TextView tvHeight;
    @InjectView(2131428318)
    TextView tvMale;
    private User user;

    public static ProfileInitOneFragment newInstance(User user) {
        ProfileInitOneFragment fragment = new ProfileInitOneFragment();
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
        View view = inflater.inflate(R.layout.ProfileInitOneFragment, container, false);
        ButterKnife.inject((Object) this, view);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        updateGenderView(this.user.sexType());
    }

    private void initView() {
        this.rvHeight.init(100.0f, 230.0f, this.user.height(), 10.0f, 10, new
                OnValueChangeListener() {
            public void onValueChange(float value) {
                if (!ProfileInitOneFragment.this.isRemoved()) {
                    ProfileInitOneFragment.this.height = value;
                    ProfileInitOneFragment.this.tvHeight.setText(String.valueOf(value) + "厘米");
                }
            }
        });
    }

    private void updateGenderView(final String sex_type) {
        new Handler().post(new Runnable() {
            public void run() {
                if (sex_type.equalsIgnoreCase("1")) {
                    ProfileInitOneFragment.this.sexType = "1";
                    ProfileInitOneFragment.this.llMale.setBackgroundResource(R.drawable.eg);
                    ProfileInitOneFragment.this.ivMale.setImageResource(R.drawable.a6q);
                    ProfileInitOneFragment.this.tvMale.setTextColor(ContextCompat.getColor
                            (ProfileInitOneFragment.this.getActivity(), R.color.hb));
                    ProfileInitOneFragment.this.tvMale.setText("我是男生");
                    ProfileInitOneFragment.this.llFemale.setBackgroundResource(R.drawable.eh);
                    ProfileInitOneFragment.this.ivFemale.setImageResource(R.drawable.a6n);
                    ProfileInitOneFragment.this.tvFemale.setTextColor(ContextCompat.getColor
                            (ProfileInitOneFragment.this.getActivity(), R.color.du));
                    ProfileInitOneFragment.this.tvFemale.setText(R.string.lm);
                } else if (sex_type.equalsIgnoreCase("2")) {
                    ProfileInitOneFragment.this.sexType = "2";
                    ProfileInitOneFragment.this.llMale.setBackgroundResource(R.drawable.ef);
                    ProfileInitOneFragment.this.ivMale.setImageResource(R.drawable.a6p);
                    ProfileInitOneFragment.this.tvMale.setTextColor(ContextCompat.getColor
                            (ProfileInitOneFragment.this.getActivity(), R.color.du));
                    ProfileInitOneFragment.this.tvMale.setText(R.string.rl);
                    ProfileInitOneFragment.this.llFemale.setBackgroundResource(R.drawable.ei);
                    ProfileInitOneFragment.this.ivFemale.setImageResource(R.drawable.a6o);
                    ProfileInitOneFragment.this.tvFemale.setTextColor(ContextCompat.getColor
                            (ProfileInitOneFragment.this.getActivity(), R.color.hb));
                    ProfileInitOneFragment.this.tvFemale.setText("我是女生");
                }
            }
        });
    }

    @OnClick({2131427911, 2131428316, 2131428319})
    public void onClick(View view) {
        if (!isRemoved() && !WheelUtils.isFastDoubleClick()) {
            switch (view.getId()) {
                case R.id.btn_next:
                    if (TextUtils.isEmpty(this.sexType) || this.height <= 0.0f) {
                        Helper.showToast((int) R.string.a0b);
                        return;
                    } else {
                        onChangeProfile(this.user);
                        return;
                    }
                case R.id.ll_male:
                    updateGenderView("1");
                    return;
                case R.id.ll_female:
                    updateGenderView("2");
                    return;
                default:
                    return;
            }
        }
    }

    public void onChangeProfile(User user) {
        user.height = this.height;
        user.sex_type = this.sexType;
        ((NewUserInitActivity) getActivity()).nextStep();
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
