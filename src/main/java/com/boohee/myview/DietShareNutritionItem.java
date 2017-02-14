package com.boohee.myview;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.myview.DietShareItem.Status;
import com.boohee.one.R;

public class DietShareNutritionItem extends DietShareItem {
    private View mView;
    @InjectView(2131427962)
    TextView  tvContent;
    @InjectView(2131429280)
    TextView  tvIndicate;
    @InjectView(2131429282)
    TextView  tvIngredient;
    @InjectView(2131429283)
    TextView  tvPercent;
    @InjectView(2131429281)
    ImageView viewIndicate;

    public DietShareNutritionItem(Context context) {
        this(context, null);
    }

    public DietShareNutritionItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DietShareNutritionItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mView = LayoutInflater.from(getContext()).inflate(R.layout.ot, null);
        addView(this.mView);
        ButterKnife.inject((View) this);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mView.setId(getId());
    }

    public void setIngredient(String ingredient) {
        if (!TextUtils.isEmpty(ingredient)) {
            this.tvIngredient.setText(ingredient);
        }
    }

    public void setShowContent(String content) {
        if (!TextUtils.isEmpty(content)) {
            this.tvContent.setText(content);
        }
    }

    public void setPercent(String percent) {
        if (!TextUtils.isEmpty(percent)) {
            this.tvPercent.setText(String.valueOf(percent));
        }
    }

    public void setIndicateText(@Status String indicate) {
        if (!TextUtils.isEmpty(indicate)) {
            this.tvIndicate.setVisibility(0);
            this.viewIndicate.setVisibility(0);
            this.tvIndicate.setText(indicate.split(",")[1]);
            this.viewIndicate.setImageResource(((Integer) mIndicator.get(indicate)).intValue());
        }
    }
}
