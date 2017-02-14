package com.boohee.one.ui.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.one.R;
import com.boohee.one.ui.fragment.BaseDietFragment.ChangDataAndTimeTypeEvent;
import com.boohee.record.DietSportCalendarActivity;
import com.boohee.utility.DensityUtil;
import com.boohee.utils.DateFormatUtils;
import com.boohee.utils.DateHelper;

import de.greenrobot.event.EventBus;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

public class ChangeDateAndTimeTypeFragment extends BaseDialogFragment {
    public static final String TAG = ChangeDateAndTimeTypeFragment.class.getSimpleName();
    private String date;
    private int    timeType;
    @InjectView(2131428210)
    TextView  tvConfirm;
    @InjectView(2131428175)
    TextView  tvTitle;
    @InjectView(2131428208)
    WheelView wheelDate;
    @InjectView(2131428209)
    WheelView wheelTimeType;

    private class DateStringAdapter extends ArrayWheelAdapter<String> {
        int currentItem;
        int currentValue;

        public DateStringAdapter(Context context, String[] items, int current) {
            super(context, items);
            this.currentValue = current;
            setTextSize(17);
        }

        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            view.setTypeface(Typeface.SANS_SERIF);
        }

        public View getItem(int index, View cachedView, ViewGroup parent) {
            this.currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }

    @OnClick({2131428210})
    public void onClick(View view) {
        if (!TextUtils.isEmpty(this.date) && this.timeType > 0) {
            EventBus.getDefault().post(new ChangDataAndTimeTypeEvent(this.date, this.timeType));
            dismissAllowingStateLoss();
        }
    }

    public static ChangeDateAndTimeTypeFragment newInstance(String date, int timeType) {
        ChangeDateAndTimeTypeFragment fragment = new ChangeDateAndTimeTypeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("date", date);
        bundle.putInt("type", timeType);
        fragment.setArguments(bundle);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fm, container, false);
        ButterKnife.inject((Object) this, view);
        getDialog().getWindow().setWindowAnimations(R.style.dg);
        return view;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            this.date = getArguments().getString("date");
            this.timeType = getArguments().getInt("type");
            initView();
        }
    }

    private void initView() {
        int i;
        initTitle();
        final String[] dates = new String[7];
        String[] datesForShow = new String[7];
        int currentDateIndex = dates.length - 1;
        String end = DateHelper.nextDay(this.date, 1);
        for (i = dates.length - 1; i >= 0; i--) {
            String day = DateHelper.previousDay(end, (i - dates.length) + 1);
            dates[i] = day;
            datesForShow[i] = DateFormatUtils.string2String(day, "M月d日");
            if (TextUtils.equals(this.date, day)) {
                currentDateIndex = i;
                datesForShow[i] = "今天";
            }
        }
        this.wheelDate.setViewAdapter(new DateStringAdapter(getActivity(), datesForShow,
                currentDateIndex));
        this.wheelDate.setCurrentItem(currentDateIndex);
        this.wheelDate.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                ChangeDateAndTimeTypeFragment.this.date = dates[newValue];
                ChangeDateAndTimeTypeFragment.this.initTitle();
            }
        });
        int currentTypeIndex = 0;
        final String[] timeTypes = new String[]{"早餐", "午餐", "晚餐", "上午加餐", "下午加餐", "晚上加餐"};
        for (i = 0; i < timeTypes.length; i++) {
            if (TextUtils.equals(timeTypes[i], DietSportCalendarActivity.getTimeTypeName(this
                    .timeType))) {
                currentTypeIndex = i;
            }
        }
        this.wheelTimeType.setViewAdapter(new DateStringAdapter(getActivity(), timeTypes,
                currentTypeIndex));
        this.wheelTimeType.setCurrentItem(currentTypeIndex);
        this.wheelTimeType.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                ChangeDateAndTimeTypeFragment.this.timeType = DietSportCalendarActivity
                        .getTimeTypeWithName(timeTypes[newValue]);
                ChangeDateAndTimeTypeFragment.this.initTitle();
            }
        });
    }

    private void initTitle() {
        this.tvTitle.setText(DateFormatUtils.string2String(this.date, "M月d日") + "/" +
                DietSportCalendarActivity.getTimeTypeName(this.timeType));
    }

    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        LayoutParams params = window.getAttributes();
        params.gravity = 80;
        params.width = -1;
        params.height = DensityUtil.dip2px(getActivity(), 480.0f);
        window.setAttributes(params);
        window.setBackgroundDrawable(new ColorDrawable(0));
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
