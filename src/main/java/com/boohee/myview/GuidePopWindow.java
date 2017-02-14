package com.boohee.myview;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.boohee.one.R;
import com.boohee.utility.ImageLoaderOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class GuidePopWindow implements OnClickListener {
    private ImageView            iv_bg;
    private Context              mContext;
    private int                  mResId;
    public  OnGuideClickListener onGuideClickListener;
    private PopupWindow          popWindow;
    private View                 view;

    public interface OnGuideClickListener {
        void onGuideClick();
    }

    public void init(Context context, int resId) {
        this.mContext = context;
        this.mResId = resId;
        initView();
    }

    private void initView() {
        if (this.mContext != null) {
            this.view = LayoutInflater.from(this.mContext).inflate(R.layout.p_, null);
            this.iv_bg = (ImageView) this.view.findViewById(R.id.iv_bg);
            this.iv_bg.setOnClickListener(this);
            this.popWindow = new PopupWindow(this.view, -1, -1, true);
            this.popWindow.setBackgroundDrawable(new BitmapDrawable());
            this.popWindow.setFocusable(true);
            if (this.mResId != 0) {
                ImageLoader.getInstance().displayImage(String.format("drawable://%d", new
                        Object[]{Integer.valueOf(this.mResId)}), this.iv_bg, ImageLoaderOptions
                        .global((int) R.color.in));
            }
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_bg:
                dismiss();
                if (this.onGuideClickListener != null) {
                    this.onGuideClickListener.onGuideClick();
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void show() {
        if (this.mContext != null && !isShowing()) {
            try {
                this.popWindow.showAtLocation(new View(this.mContext), 17, 0, 0);
            } catch (Exception e) {
            }
        }
    }

    public boolean isShowing() {
        return this.popWindow != null && this.popWindow.isShowing();
    }

    public void dismiss() {
        if (isShowing()) {
            this.popWindow.dismiss();
        }
    }

    public void setOnGuideClickListener(OnGuideClickListener onGuideClickListener) {
        this.onGuideClickListener = onGuideClickListener;
    }
}
