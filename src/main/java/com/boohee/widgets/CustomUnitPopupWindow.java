package com.boohee.widgets;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.boohee.model.Sport;
import com.boohee.one.R;
import com.boohee.utils.ViewFinder;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

public class CustomUnitPopupWindow implements OnClickListener, OnWheelChangedListener {
    static final String TAG = CustomUnitPopupWindow.class.getName();
    private static CustomUnitPopupWindow regionsPopwindow;
    private        View                  contentView;
    private        ViewFinder            finder;
    private        Animation             inAnim;
    private        Context               mContext;
    private        String                mCurrentUnitName;
    private        OnChangeListener      mOnChangeListener;
    private        WheelView             mUnit;
    private String[] mUnits = new String[]{Sport.UNIT_NAME, "公里", "次", "组", "套"};
    private View         mask;
    private LinearLayout popLayout;
    private PopupWindow  popWindow;

    public interface OnChangeListener {
        void onChange(String str);
    }

    public static CustomUnitPopupWindow getInstance() {
        if (regionsPopwindow == null) {
            regionsPopwindow = new CustomUnitPopupWindow();
        }
        return regionsPopwindow;
    }

    private void createPopWindow(Context context, String unit_name) {
        if (this.mContext != context) {
            this.mContext = context;
            this.mCurrentUnitName = unit_name;
            this.popWindow = new PopupWindow(createContentView(), -1, -2, true);
            this.popWindow.setBackgroundDrawable(new BitmapDrawable());
            this.popWindow.setFocusable(true);
            this.popWindow.setTouchable(true);
            this.popWindow.setOutsideTouchable(true);
            this.inAnim = AnimationUtils.loadAnimation(this.mContext, R.anim.s);
        }
    }

    private View createContentView() {
        this.contentView = View.inflate(this.mContext, R.layout.ls, null);
        this.finder = new ViewFinder(this.contentView);
        findView();
        locationToRegions();
        return this.contentView;
    }

    private void findView() {
        this.popLayout = (LinearLayout) this.finder.find(R.id.popLayout);
        this.mask = this.finder.find(R.id.mask);
        this.mUnit = (WheelView) this.finder.find(R.id.wheel_unit);
        this.mUnit.setVisibleItems(3);
        this.mask.setOnClickListener(this);
    }

    private void locationToRegions() {
        this.mUnit.setViewAdapter(new ArrayWheelAdapter(this.mContext, this.mUnits));
        int index = getUnitIndex();
        this.mCurrentUnitName = this.mUnits[index];
        this.mUnit.setCurrentItem(index);
        if (this.mOnChangeListener != null) {
            this.mOnChangeListener.onChange(this.mCurrentUnitName);
        }
        this.mUnit.addChangingListener(this);
    }

    private int getUnitIndex() {
        for (int i = 0; i < this.mUnits.length; i++) {
            if (TextUtils.equals(this.mCurrentUnitName, this.mUnits[i])) {
                return i;
            }
        }
        return 0;
    }

    private void show(Context context) {
        if (this.popWindow != null && !this.popWindow.isShowing()) {
            this.popWindow.showAtLocation(new View(context), 48, 0, 0);
            this.popLayout.startAnimation(this.inAnim);
        }
    }

    public synchronized boolean isShowing() {
        boolean z;
        z = this.popWindow != null && this.popWindow.isShowing();
        return z;
    }

    public synchronized void dismiss() {
        if (this.popWindow != null && this.popWindow.isShowing()) {
            this.popWindow.dismiss();
        }
    }

    public synchronized void showPopWindow(Context context, String unit_name) {
        createPopWindow(context, unit_name);
        show(context);
    }

    public void setOnChangeListener(OnChangeListener onChangeListener) {
        this.mOnChangeListener = onChangeListener;
    }

    public void onClick(View v) {
        dismiss();
    }

    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        this.mCurrentUnitName = this.mUnits[newValue];
        if (this.mOnChangeListener != null) {
            this.mOnChangeListener.onChange(this.mCurrentUnitName);
        }
    }
}
