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

import java.util.HashMap;
import java.util.Map;

public class DietShareEatItem extends DietShareItem {
    public static final  String               BREAKFAST = "breakfast";
    public static final  String               DINNER    = "dinner";
    public static final  String               LUNCH     = "lunch";
    public static final  String               SNACK     = "snack";
    public static final  String               SPORT     = "sport";
    private static final Map<String, Integer> mItem     = new HashMap();
    @InjectView(2131427481)
    ImageView iv_icon;
    private View mView;
    @InjectView(2131429278)
    TextView  tv_consume;
    @InjectView(2131429280)
    TextView  tv_indicate;
    @InjectView(2131429279)
    TextView  tv_subtitle;
    @InjectView(2131428175)
    TextView  tv_title;
    @InjectView(2131427436)
    TextView  tv_unit;
    @InjectView(2131429281)
    ImageView view_indicate;

    public @interface Item {
    }

    public DietShareEatItem(Context context) {
        this(context, null);
    }

    public DietShareEatItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DietShareEatItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mItem.put("breakfast", Integer.valueOf(R.drawable.pr));
        mItem.put("lunch", Integer.valueOf(R.drawable.pt));
        mItem.put("dinner", Integer.valueOf(R.drawable.ps));
        mItem.put(SNACK, Integer.valueOf(R.drawable.pq));
        mItem.put("sport", Integer.valueOf(R.drawable.pu));
        this.mView = LayoutInflater.from(getContext()).inflate(R.layout.os, null);
        addView(this.mView);
        ButterKnife.inject((View) this);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mView.setId(getId());
    }

    public void setIcon(@Item String icon) {
        if (!TextUtils.isEmpty(icon)) {
            this.tv_consume.setVisibility("sport".equals(icon) ? 0 : 8);
            this.iv_icon.setImageResource(((Integer) mItem.get(icon)).intValue());
        }
    }

    public void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            this.tv_title.setText(title);
        }
    }

    public void setUnit(String unit) {
        if (!TextUtils.isEmpty(unit)) {
            this.tv_unit.setText(unit);
        }
    }

    public void setSubTitle(String subTitle) {
        if (TextUtils.isEmpty(subTitle)) {
            this.tv_subtitle.setVisibility(8);
            return;
        }
        this.tv_subtitle.setVisibility(0);
        this.tv_subtitle.setText(subTitle);
    }

    public void setIndicateText(@Status String indicate) {
        if (!TextUtils.isEmpty(indicate)) {
            this.tv_indicate.setVisibility(0);
            this.view_indicate.setVisibility(0);
            this.tv_indicate.setText(indicate.split(",")[1]);
            this.view_indicate.setImageResource(((Integer) mIndicator.get(indicate)).intValue());
        }
    }
}
