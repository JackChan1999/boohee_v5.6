package com.boohee.widgets.tablayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.boohee.one.R;
import com.boohee.utility.DensityUtil;
import com.boohee.utils.Helper;
import com.boohee.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

public class BooheeTabLayout extends HorizontalScrollView {
    public static final int    DEFAULT_TEXT_COLOR = 2130903347;
    public static final int    DEFAULT_TEXT_SIZE  = 17;
    public static final String TAG                = BooheeTabLayout.class.getSimpleName();
    private int                     DEFAULT_TEXTVIEW_WIDTH;
    private int                     centerViewIndex;
    private List<TabModelInterface> mTabList;
    private OnTabChangeListener     onTabChangeListener;
    private BooheeTabStrip          tabStrip;
    private int                     textColor;
    private float                   textSize;
    private int                     textViewWidth;
    private int                     width;

    public interface OnTabChangeListener {
        void onTabChanged(int i);
    }

    private class InternalTabClickListener implements OnClickListener {
        private InternalTabClickListener() {
        }

        public void onClick(View v) {
            for (int i = 0; i < BooheeTabLayout.this.tabStrip.getChildCount(); i++) {
                if (v == BooheeTabLayout.this.tabStrip.getChildAt(i)) {
                    BooheeTabLayout.this.centerViewIndex = i;
                    BooheeTabLayout.this.scrollToTab(i);
                    BooheeTabLayout.this.notifiChange();
                    return;
                }
            }
        }
    }

    public BooheeTabLayout(Context context) {
        this(context, null);
    }

    public BooheeTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BooheeTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mTabList = new ArrayList();
        this.DEFAULT_TEXTVIEW_WIDTH = 70;
        this.textSize = 17.0f * DensityUtil.getDensity(context);
        this.textColor = R.color.hb;
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable
                .BooheeTabLayout);
        if (typedArray != null) {
            this.textSize = typedArray.getDimension(1, this.textSize);
            this.textColor = typedArray.getColor(0, ContextCompat.getColor(context, R.color.he));
            typedArray.recycle();
        }
        setHorizontalScrollBarEnabled(false);
        this.tabStrip = new BooheeTabStrip(context, attrs);
        addView(this.tabStrip, -1, -1);
        this.textViewWidth = DensityUtil.dip2px(context, (float) this.DEFAULT_TEXTVIEW_WIDTH);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setCenterChild();
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        initView();
    }

    private void initView() {
        if (this.tabStrip.getChildCount() > 0) {
            View firstTab = this.tabStrip.getChildAt(0);
            View lastTab = this.tabStrip.getChildAt(getChildCount() - 1);
            int startWidth = BooheeTabUtils.getMeasuredWidth(firstTab);
            if (startWidth == 0) {
                startWidth = BooheeTabUtils.getWidth(firstTab);
            }
            int endWidth = BooheeTabUtils.getMeasuredWidth(lastTab);
            if (endWidth == 0) {
                endWidth = BooheeTabUtils.getWidth(lastTab);
            }
            int start = ((this.width - startWidth) / 2) - BooheeTabUtils.getMarginStart(firstTab);
            int end = ((this.width - endWidth) / 2) - BooheeTabUtils.getMarginEnd(lastTab);
            this.tabStrip.setMinimumWidth(this.tabStrip.getMeasuredWidth());
            Helper.showLog(TAG, "width : " + this.width + " start " + start + " end : " + end);
            ViewCompat.setPaddingRelative(this, start, getPaddingTop(), end, getPaddingBottom());
            setClipToPadding(false);
        }
        setCenterChild();
    }

    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        setCenterChild();
    }

    private void setCenterChild() {
        int windowCenterX = getResources().getDisplayMetrics().widthPixels / 2;
        int i = 0;
        while (i < this.tabStrip.getChildCount()) {
            int[] location = new int[2];
            this.tabStrip.getChildAt(i).getLocationOnScreen(location);
            int viewRight = location[0] + this.tabStrip.getChildAt(i).getWidth();
            if (location[0] > windowCenterX || viewRight < windowCenterX) {
                ((TextView) this.tabStrip.getChildAt(i)).setTextColor(getResources().getColor(R
                        .color.du));
            } else {
                ((TextView) this.tabStrip.getChildAt(i)).setTextColor(this.textColor);
                if (this.centerViewIndex == 0 || this.centerViewIndex != i) {
                    this.centerViewIndex = i;
                    notifiChange();
                }
                this.centerViewIndex = i;
            }
            i++;
        }
    }

    public void setupChild(List<TabModelInterface> list) {
        if (list != null && list.size() != 0) {
            this.mTabList.clear();
            this.mTabList.addAll(list);
            this.tabStrip.removeAllViews();
            for (TabModelInterface model : list) {
                TextView textView = new TextView(getContext());
                textView.setGravity(17);
                textView.setText(model.getTabName());
                textView.setTextColor(getResources().getColor(R.color.du));
                textView.setTextSize(0, this.textSize);
                LayoutParams params = new LayoutParams(-2, -1);
                textView.setPadding(ViewUtils.dip2px(getContext(), 16.0f), 0, ViewUtils.dip2px
                        (getContext(), 16.0f), 0);
                textView.setOnClickListener(new InternalTabClickListener());
                this.tabStrip.addView(textView, params);
                textView.measure(MeasureSpec.makeMeasureSpec(0, 0), MeasureSpec.makeMeasureSpec
                        (0, 0));
            }
            initView();
        }
    }

    public void scrollToTab(int tabIndex) {
        int tabStripChildCount = this.tabStrip.getChildCount();
        if (tabStripChildCount != 0 && tabIndex >= 0 && tabIndex < tabStripChildCount) {
            int x;
            boolean isLayoutRtl = BooheeTabUtils.isLayoutRtl(this);
            View selectedTab = this.tabStrip.getChildAt(tabIndex);
            View firstTab = this.tabStrip.getChildAt(0);
            if (isLayoutRtl) {
                x = (BooheeTabUtils.getEnd(selectedTab) - BooheeTabUtils.getMarginEnd
                        (selectedTab)) - (((BooheeTabUtils.getWidth(firstTab) + BooheeTabUtils
                        .getMarginEnd(firstTab)) - (BooheeTabUtils.getWidth(selectedTab) +
                        BooheeTabUtils.getMarginEnd(selectedTab))) / 2);
            } else {
                x = (BooheeTabUtils.getStart(selectedTab) - BooheeTabUtils.getMarginStart
                        (selectedTab)) - (((BooheeTabUtils.getWidth(firstTab) + BooheeTabUtils
                        .getMarginStart(firstTab)) - (BooheeTabUtils.getWidth(selectedTab) +
                        BooheeTabUtils.getMarginStart(selectedTab))) / 2);
            }
            if (tabIndex == 0) {
                setCenterChild();
            }
            smoothScrollTo(x, 0);
        }
    }

    public String getSelected() {
        if (this.mTabList == null || this.mTabList.size() == 0) {
            return "";
        }
        if (this.centerViewIndex <= this.mTabList.size()) {
            return ((TabModelInterface) this.mTabList.get(this.centerViewIndex)).getTabName();
        }
        return "";
    }

    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case 0:
            case 2:
                super.onTouchEvent(ev);
                break;
            case 1:
                scrollToTab(this.centerViewIndex);
                notifiChange();
                break;
        }
        return true;
    }

    private void notifiChange() {
        if (this.onTabChangeListener != null) {
            this.onTabChangeListener.onTabChanged(this.centerViewIndex);
        }
    }

    public void setOnTabChangeListener(OnTabChangeListener listener) {
        this.onTabChangeListener = listener;
    }
}
