package cn.sharesdk.framework;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.mob.tools.utils.R;

public class TitleLayout extends LinearLayout {
    private ImageView btnBack;
    private TextView btnRight;
    private TextView tvTitle;

    public TitleLayout(Context context) {
        super(context);
        init(context);
    }

    public TitleLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    private void init(Context context) {
        this.btnBack = new ImageView(context);
        this.btnBack.setBackgroundDrawable(null);
        int bitmapRes = R.getBitmapRes(context, "ssdk_back_arr");
        if (bitmapRes > 0) {
            this.btnBack.setImageResource(bitmapRes);
        }
        this.btnBack.setScaleType(ScaleType.CENTER_INSIDE);
        this.btnBack.setLayoutParams(new LayoutParams(R.dipToPx(context, 48), -1));
        addView(this.btnBack);
        View imageView = new ImageView(context);
        int dipToPx = R.dipToPx(context, 2);
        int bitmapRes2 = R.getBitmapRes(context, "ssdk_title_div");
        if (bitmapRes2 > 0) {
            imageView.setImageResource(bitmapRes2);
        }
        imageView.setLayoutParams(new LayoutParams(dipToPx, -1));
        addView(imageView);
        this.tvTitle = new TextView(context);
        bitmapRes = R.dipToPx(context, 23);
        this.tvTitle.setPadding(bitmapRes, 0, bitmapRes, 0);
        this.tvTitle.setSingleLine();
        this.tvTitle.setTextColor(-1);
        this.tvTitle.setTextSize(1, 18.0f);
        this.tvTitle.setTypeface(Typeface.DEFAULT_BOLD);
        this.tvTitle.setGravity(16);
        ViewGroup.LayoutParams layoutParams = new LayoutParams(-2, -1);
        layoutParams.weight = 1.0f;
        this.tvTitle.setLayoutParams(layoutParams);
        addView(this.tvTitle);
        imageView = new ImageView(context);
        bitmapRes2 = R.getBitmapRes(context, "ssdk_title_div");
        if (bitmapRes2 > 0) {
            imageView.setImageResource(bitmapRes2);
        }
        imageView.setLayoutParams(new LayoutParams(dipToPx, -1));
        addView(imageView);
        this.btnRight = new n(this, context, imageView);
        this.btnRight.setVisibility(4);
        this.btnRight.setBackgroundDrawable(null);
        this.btnRight.setMinWidth(R.dipToPx(context, 50));
        this.btnRight.setTextColor(-1);
        this.btnRight.setTextSize(1, 12.0f);
        this.btnRight.setGravity(17);
        this.btnRight.setLayoutParams(new LayoutParams(-2, -1));
        addView(this.btnRight);
    }

    public ImageView getBtnBack() {
        return this.btnBack;
    }

    public TextView getBtnRight() {
        return this.btnRight;
    }

    public TextView getTvTitle() {
        return this.tvTitle;
    }
}
