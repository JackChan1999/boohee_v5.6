package com.boohee.one.ui.fragment;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.more.EstimateFoodActivity;
import com.boohee.myview.BooheeRulerView;
import com.boohee.myview.BooheeRulerView.OnValueChangeListener;
import com.boohee.one.R;
import com.boohee.record.DietSportCalendarActivity;
import com.boohee.utility.DensityUtil;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utility.TimeLinePatterns;
import com.boohee.utils.DateFormatUtils;
import com.boohee.utils.FoodUtils;
import com.boohee.utils.FoodUtils.FoodUnitMaxAndMin;
import com.boohee.utils.Helper;
import com.boohee.widgets.BooheeRippleLayout;
import com.boohee.widgets.tablayout.BooheeTabLayout;
import com.boohee.widgets.tablayout.BooheeTabLayout.OnTabChangeListener;
import com.boohee.widgets.tablayout.TabModelInterface;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.greenrobot.event.EventBus;

import java.util.List;

public abstract class BaseDietFragment extends BaseDialogFragment implements
        OnValueChangeListener, OnTabChangeListener {
    public static final String FOOD_CODE     = "food_code";
    public static final String KEY_DATE      = "date";
    public static final String KEY_TIME_TYPE = "time_type";
    public static final String TAG           = BaseDietFragment.class.getSimpleName();
    protected           float  amount        = -1.0f;
    protected float  calory;
    protected float  caloryPer;
    protected int    currentFoodUnitId;
    protected String currentUnitEatWeight;
    protected String currentUnitName;
    @InjectView(2131428184)
    BooheeTabLayout dietUnitTab;
    protected float  foodCalory;
    protected int    foodId;
    protected String foodImage;
    protected String foodName;
    protected float  gramValue;
    protected int    healthLight;
    @InjectView(2131428180)
    ImageView        ivCaloryStatus;
    @InjectView(2131428177)
    RoundedImageView ivDiet;
    @InjectView(2131428176)
    RelativeLayout   llDietInfo;
    protected Handler mHandler = new Handler();
    protected String recordOn;
    @InjectView(2131428186)
    BooheeRippleLayout rippleCancel;
    @InjectView(2131428187)
    BooheeRippleLayout rippleSend;
    @InjectView(2131428183)
    BooheeRulerView    ruler;
    protected int timeType;
    @InjectView(2131427461)
    TextView tvCalory;
    @InjectView(2131428179)
    TextView tvCaloryPer;
    @InjectView(2131427963)
    TextView tvCancel;
    @InjectView(2131428185)
    TextView tvDelete;
    @InjectView(2131428178)
    TextView tvDietName;
    @InjectView(2131428163)
    TextView tvEstimate;
    @InjectView(2131428188)
    TextView tvSend;
    @InjectView(2131428175)
    TextView tvTitle;
    @InjectView(2131428182)
    TextView tvUnitName;
    @InjectView(2131428181)
    TextView tvUnitValue;
    @InjectView(2131427651)
    TextView tvWeight;

    public static class ChangDataAndTimeTypeEvent {
        public String date;
        public int    timeType;

        public ChangDataAndTimeTypeEvent(String date, int timeType) {
            this.date = date;
            this.timeType = timeType;
        }
    }

    protected abstract void confirm();

    protected abstract void refreshUnit(int i);

    @OnClick({2131427963, 2131428175, 2131428188, 2131428185, 2131428163, 2131428176})
    public void onClickView(View view) {
        if (!isRemoved()) {
            switch (view.getId()) {
                case R.id.tv_cancel:
                    cancel();
                    return;
                case R.id.tv_estimate:
                    EstimateFoodActivity.comeOnBaby(getActivity());
                    return;
                case R.id.tv_title:
                    ChangeDateAndTimeTypeFragment.newInstance(this.recordOn, this.timeType).show
                            (getChildFragmentManager(), "change");
                    return;
                case R.id.ll_diet_info:
                    dietInfo();
                    return;
                case R.id.tv_delete:
                    new Builder(getActivity()).setMessage("确定要删除吗？").setPositiveButton("删除", new
                            OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            BaseDietFragment.this.deleteEating();
                        }
                    }).setNegativeButton("取消", null).show();
                    return;
                case R.id.tv_send:
                    confirm();
                    return;
                default:
                    return;
            }
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.ff, container, false);
        ButterKnife.inject((Object) this, view);
        EventBus.getDefault().register(this);
        getDialog().getWindow().setWindowAnimations(R.style.de);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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

    protected void initTitle() {
        if (!TextUtils.isEmpty(this.recordOn)) {
            this.tvTitle.setText(DateFormatUtils.transferDateString(this.recordOn) + "/" +
                    DietSportCalendarActivity.getTimeTypeName(this.timeType));
        }
    }

    protected void refreshView() {
        ImageLoader.getInstance().displayImage(TimeLinePatterns.WEB_SCHEME + this.foodImage, this
                .ivDiet, ImageLoaderOptions.global((int) R.drawable.aa2));
        this.tvDietName.setText(this.foodName);
        this.tvCaloryPer.setText(Math.round(this.foodCalory) + "千卡/" + "100克");
        FoodUtils.switchToLight(this.healthLight, this.ivCaloryStatus);
    }

    protected void initUnitTab(List<TabModelInterface> foodTabUnits, final int currentIndex) {
        if (!isRemoved() && foodTabUnits.size() > 0) {
            this.dietUnitTab.setupChild(foodTabUnits);
            this.dietUnitTab.setOnTabChangeListener(this);
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    if (!BaseDietFragment.this.isRemoved()) {
                        BaseDietFragment.this.dietUnitTab.scrollToTab(currentIndex);
                    }
                }
            }, 500);
        }
    }

    protected void deleteEating() {
    }

    protected void cancel() {
        dismissAllowingStateLoss();
    }

    protected void dietInfo() {
    }

    public void onValueChange(float value) {
        this.amount = value;
        this.gramValue = Float.parseFloat(this.currentUnitEatWeight) * this.amount;
        this.calory = this.caloryPer * this.gramValue;
        this.tvUnitValue.setText(String.valueOf(value));
        this.tvUnitName.setText(this.currentUnitName);
        this.tvCalory.setText(Math.round(this.calory) + "千卡");
        this.tvWeight.setText(Math.round(this.gramValue) + "克");
    }

    public void onTabChanged(int position) {
        Helper.showLog(TAG, "onTabChanged position : " + position);
        refreshUnit(position);
    }

    protected void initRulerView() {
        if (!TextUtils.isEmpty(this.currentUnitName)) {
            FoodUnitMaxAndMin maxAndMin = FoodUtils.getMinValueWithFoodUnit(this.currentUnitName);
            if (!needHoldAmount()) {
                this.amount = maxAndMin.currentValue;
            }
            this.ruler.init(maxAndMin.minValue, maxAndMin.maxValue, this.amount, maxAndMin.unit,
                    maxAndMin.microUnit, this);
        }
    }

    protected boolean needHoldAmount() {
        return false;
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(ChangDataAndTimeTypeEvent event) {
        this.recordOn = DateFormatUtils.string2String(event.date, "yyyy-MM-dd");
        this.timeType = event.timeType;
        initTitle();
    }
}
