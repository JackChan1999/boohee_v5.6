package com.boohee.one.sport.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.boohee.one.R;

public class SportProgress extends LinearLayout {
    private int       mIndicateWidth;
    private int       mMax;
    private int       mProgress;
    private Animation mProgressLeftIn;
    private View      view_indicate;
    private View      view_progress;

    public SportProgress(Context context) {
        this(context, null);
    }

    public SportProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SportProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayoutParams(new LayoutParams(-1, -2));
        LayoutInflater.from(context).inflate(R.layout.qx, this);
        this.view_progress = findViewById(R.id.view_progress);
        this.view_indicate = findViewById(R.id.view_indicate);
        this.mProgressLeftIn = AnimationUtils.loadAnimation(context, R.anim.ah);
    }

    public void setMax(int max) {
        this.mMax = max;
        resetLayout();
    }

    public void setProgress(int progress) {
        if (progress >= this.mMax) {
            progress = this.mMax;
        }
        this.mProgress = progress;
        resetLayout();
    }

    private void resetLayout() {
        this.mIndicateWidth = getHeight() / 2;
        int space = (getWidth() - this.mIndicateWidth) / this.mMax;
        FrameLayout.LayoutParams paramsProgress = (FrameLayout.LayoutParams) this.view_progress
                .getLayoutParams();
        paramsProgress.width = this.mProgress * space;
        this.view_progress.setLayoutParams(paramsProgress);
        FrameLayout.LayoutParams indicateProgress = (FrameLayout.LayoutParams) this.view_indicate
                .getLayoutParams();
        if (this.mProgress == 0) {
            indicateProgress.leftMargin = 0;
        } else {
            indicateProgress.leftMargin = (this.mProgress * space) - this.mIndicateWidth;
        }
        this.view_indicate.setLayoutParams(indicateProgress);
    }
}
