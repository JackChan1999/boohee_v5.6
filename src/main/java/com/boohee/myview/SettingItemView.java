package com.boohee.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.boohee.one.R;

public class SettingItemView extends MaterialRippleLayout {
    @InjectView(2131427481)
    ImageView iv_icon;
    private boolean  mBottomDivider;
    private Drawable mIcon;
    private String   mSubTitle;
    private String   mTextIndicator;
    private String   mTitle;
    private boolean  mTopDivider;
    private View     mView;
    @InjectView(2131429280)
    TextView  tv_indicate;
    @InjectView(2131429279)
    TextView  tv_subtitle;
    @InjectView(2131428175)
    TextView  tv_title;
    @InjectView(2131429425)
    View      view_bottom_divider;
    @InjectView(2131429281)
    ImageView view_indicate;
    @InjectView(2131429424)
    View      view_top_divider;

    public SettingItemView(Context context) {
        this(context, null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SettingItemView);
        this.mIcon = typedArray.getDrawable(1);
        this.mTitle = typedArray.getString(2);
        this.mSubTitle = typedArray.getString(3);
        this.mTextIndicator = typedArray.getString(4);
        this.mTopDivider = typedArray.getBoolean(0, false);
        this.mBottomDivider = typedArray.getBoolean(5, false);
        typedArray.recycle();
        this.mView = LayoutInflater.from(getContext()).inflate(R.layout.qp, null);
        addView(this.mView);
        ButterKnife.inject((View) this);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mView.setId(getId());
        setIcon(this.mIcon);
        setTitle(this.mTitle);
        setSubTitle(this.mSubTitle);
        setIndicateText(this.mTextIndicator);
        isShowTopDivider(this.mTopDivider);
        setBottomDivider(this.mBottomDivider);
    }

    private void setBottomDivider(boolean bottomDivider) {
        if (bottomDivider) {
            ((LayoutParams) this.view_bottom_divider.getLayoutParams()).leftMargin = 0;
        }
    }

    private void isShowTopDivider(boolean isShow) {
        this.view_top_divider.setVisibility(isShow ? 0 : 8);
    }

    public void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            this.tv_title.setText(title);
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

    public void setIndicateText(String indicate) {
        if (!TextUtils.isEmpty(indicate)) {
            this.view_indicate.setVisibility(8);
            this.tv_indicate.setVisibility(0);
            this.tv_indicate.setText(indicate);
        }
    }

    public void setIndicateVisibility(boolean visibility) {
        int i = 8;
        this.tv_indicate.setVisibility(8);
        ImageView imageView = this.view_indicate;
        if (visibility) {
            i = 0;
        }
        imageView.setVisibility(i);
    }

    public void setIcon(Drawable drawable) {
        if (drawable == null) {
            ((LayoutParams) this.view_bottom_divider.getLayoutParams()).leftMargin = 0;
            this.iv_icon.setVisibility(8);
            return;
        }
        this.iv_icon.setImageDrawable(drawable);
        this.iv_icon.setVisibility(0);
    }
}
