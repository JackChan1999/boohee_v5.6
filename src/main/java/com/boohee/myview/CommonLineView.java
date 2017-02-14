package com.boohee.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.baidu.location.aj;
import com.boohee.one.R;
import com.boohee.utils.ViewUtils;

public class CommonLineView extends RelativeLayout {
    private Context  context;
    private Drawable icon;
    @InjectView(2131427481)
    ImageView ivIcon;
    private String rightText;
    private String title;
    @InjectView(2131429273)
    TextView tvText;
    @InjectView(2131428175)
    TextView tvTitle;

    public CommonLineView(Context context) {
        this(context, null);
    }

    public CommonLineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.CommonLineView);
        this.icon = arr.getDrawable(0);
        this.title = arr.getString(1);
        this.rightText = arr.getString(2);
        arr.recycle();
        LayoutInflater.from(context).inflate(R.layout.ok, this);
        ButterKnife.inject((View) this);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            setBackgroundColor(getResources().getColor(R.color.ju));
            setPadding(0, ViewUtils.dip2px(this.context, aj.hA), 0, ViewUtils.dip2px(this
                    .context, aj.hA));
            setIcon(this.icon);
            setTitle(this.title);
            setRightText(this.rightText);
        }
    }

    public void setIcon(Drawable drawable) {
        if (drawable == null) {
            this.ivIcon.setVisibility(8);
            return;
        }
        this.ivIcon.setVisibility(0);
        this.ivIcon.setImageDrawable(drawable);
    }

    public void setTitle(String string) {
        if (string != null) {
            this.tvTitle.setText(string);
        }
    }

    public void setRightText(String string) {
        if (string != null) {
            this.tvText.setText(string);
        }
    }
}
