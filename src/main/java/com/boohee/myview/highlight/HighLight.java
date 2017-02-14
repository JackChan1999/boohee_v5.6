package com.boohee.myview.highlight;

import android.app.Activity;
import android.content.Context;
import android.graphics.RectF;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class HighLight {
    private boolean intercept = true;
    private OnHighLightClickListener lightClickListener;
    private View                     mAnchor;
    private Context                  mContext;
    private HightLightView           mHightLightView;
    private List<ViewPosInfo>        mViewRects;
    private int     maskColor = -872415232;
    private boolean shadow    = true;

    public interface OnPosCallback {
        void getPos(float f, float f2, RectF rectF, MarginInfo marginInfo);
    }

    public interface OnHighLightClickListener {
        void onClick();
    }

    public static class MarginInfo {
        public float bottomMargin;
        public float leftMargin;
        public float rightMargin;
        public float topMargin;
    }

    public static class ViewPosInfo {
        public int layoutId = -1;
        public MarginInfo    marginInfo;
        public OnPosCallback onPosCallback;
        public RectF         rectF;
        public View          view;
    }

    public HighLight(Context context) {
        this.mContext = context;
        this.mViewRects = new ArrayList();
        this.mAnchor = ((Activity) this.mContext).findViewById(16908290);
    }

    public HighLight anchor(View anchor) {
        this.mAnchor = anchor;
        return this;
    }

    public HighLight intercept(boolean intercept) {
        this.intercept = intercept;
        return this;
    }

    public HighLight shadow(boolean shadow) {
        this.shadow = shadow;
        return this;
    }

    public HighLight maskColor(int maskColor) {
        this.maskColor = maskColor;
        return this;
    }

    public HighLight addHighLight(int viewId, int decorLayoutId, OnPosCallback onPosCallback) {
        addHighLight(this.mAnchor.findViewById(viewId), decorLayoutId, onPosCallback);
        return this;
    }

    public void updateInfo() {
        ViewGroup parent = this.mAnchor;
        for (ViewPosInfo viewPosInfo : this.mViewRects) {
            RectF rect = new RectF(ViewUtils.getLocationInView(parent, viewPosInfo.view));
            viewPosInfo.rectF = rect;
            viewPosInfo.onPosCallback.getPos(((float) parent.getWidth()) - rect.right, ((float)
                    parent.getHeight()) - rect.bottom, rect, viewPosInfo.marginInfo);
        }
    }

    public HighLight addHighLight(View view, int decorLayoutId, OnPosCallback onPosCallback) {
        ViewGroup parent = this.mAnchor;
        RectF rect = new RectF(ViewUtils.getLocationInView(parent, view));
        ViewPosInfo viewPosInfo = new ViewPosInfo();
        viewPosInfo.layoutId = decorLayoutId;
        viewPosInfo.rectF = rect;
        viewPosInfo.view = view;
        if (onPosCallback != null || decorLayoutId == -1) {
            MarginInfo marginInfo = new MarginInfo();
            onPosCallback.getPos(((float) parent.getWidth()) - rect.right, ((float) parent
                    .getHeight()) - rect.bottom, rect, marginInfo);
            viewPosInfo.marginInfo = marginInfo;
            viewPosInfo.onPosCallback = onPosCallback;
            this.mViewRects.add(viewPosInfo);
            return this;
        }
        throw new IllegalArgumentException("onPosCallback can not be null.");
    }

    public void show() {
        if (this.mHightLightView == null) {
            HightLightView hightLightView = new HightLightView(this.mContext, this, this
                    .maskColor, this.shadow, this.mViewRects);
            if (this.mAnchor instanceof FrameLayout) {
                ((ViewGroup) this.mAnchor).addView(hightLightView, ((ViewGroup) this.mAnchor)
                        .getChildCount(), new LayoutParams(-1, -1));
            } else {
                FrameLayout frameLayout = new FrameLayout(this.mContext);
                ViewGroup parent = (ViewGroup) this.mAnchor.getParent();
                parent.removeView(this.mAnchor);
                parent.addView(frameLayout, this.mAnchor.getLayoutParams());
                frameLayout.addView(this.mAnchor, new LayoutParams(-1, -1));
                frameLayout.addView(hightLightView);
            }
            if (this.intercept) {
                hightLightView.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        if (HighLight.this.lightClickListener != null) {
                            HighLight.this.lightClickListener.onClick();
                        }
                        HighLight.this.remove();
                    }
                });
            }
            this.mHightLightView = hightLightView;
        }
    }

    public void remove() {
        if (this.mHightLightView != null) {
            ViewGroup parent = (ViewGroup) this.mHightLightView.getParent();
            if ((parent instanceof RelativeLayout) || (parent instanceof FrameLayout)) {
                parent.removeView(this.mHightLightView);
            } else {
                parent.removeView(this.mHightLightView);
                View origin = parent.getChildAt(0);
                ViewGroup graParent = (ViewGroup) parent.getParent();
                graParent.removeView(parent);
                graParent.addView(origin, parent.getLayoutParams());
            }
            this.mHightLightView = null;
        }
    }

    public void setOnHighLightClickListener(OnHighLightClickListener listener) {
        this.lightClickListener = listener;
    }
}
