package com.boohee.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.boohee.one.R;

public class ChangeProfileItemView extends MaterialRippleLayout {
    private View mView;
    String title;
    @InjectView(2131428175)
    TextView tvTitle;
    @InjectView(2131428463)
    TextView tvValue;
    String value;
    @InjectView(2131427613)
    LinearLayout viewContent;
    @InjectView(2131429266)
    ImageView    viewEdit;

    public ChangeProfileItemView(Context context) {
        this(context, null);
    }

    public ChangeProfileItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChangeProfileItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable
                .ChangeProfileItemView);
        this.title = typedArray.getString(0);
        this.value = typedArray.getString(1);
        this.mView = LayoutInflater.from(getContext()).inflate(R.layout.og, null);
        addView(this.mView);
        ButterKnife.inject((View) this);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mView.setId(getId());
        setTitle(this.title);
    }

    public void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            this.tvTitle.setText(title);
        }
    }

    public void setIconVisible(int visible) {
        this.viewEdit.setVisibility(visible);
    }

    public void setIndicateText(String indicate) {
        if (!TextUtils.isEmpty(indicate)) {
            this.tvValue.setText(indicate);
        }
    }
}
