package com.boohee.myview;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.boohee.one.R;
import com.boohee.widgets.Util.DensityUtil;

public class HorizontalDragLinearLayout extends LinearLayout {
    public static final int SCROLL_SPEED = -5;
    private int                MAX_SCROLL;
    private String             TAG;
    private boolean            canFling;
    private Context            context;
    private int                deltaScroll;
    private long               deltaTime;
    private long               downTime;
    private MarginLayoutParams headerLayoutParams;
    private LinearLayout       headerView;
    private int                hideHeaderWidth;
    private boolean            loadOnce;
    private int                mScreenWidth;
    private OnClick            onClick;
    private TextView           originTv;
    private int                touchSlop;
    private long               upTime;
    private float              upX;
    private float              upY;
    private float              xDown;
    private float              yDown;

    class HideHeaderTask extends AsyncTask<Void, Integer, Integer> {
        HideHeaderTask() {
        }

        protected Integer doInBackground(Void... params) {
            int leftMargin = HorizontalDragLinearLayout.this.headerLayoutParams.leftMargin;
            while (true) {
                leftMargin -= 5;
                if (leftMargin <= HorizontalDragLinearLayout.this.hideHeaderWidth) {
                    return Integer.valueOf(HorizontalDragLinearLayout.this.hideHeaderWidth);
                }
                publishProgress(new Integer[]{Integer.valueOf(leftMargin)});
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        protected void onProgressUpdate(Integer... leftMargin) {
            HorizontalDragLinearLayout.this.headerLayoutParams.leftMargin = leftMargin[0]
                    .intValue();
            HorizontalDragLinearLayout.this.headerView.setLayoutParams(HorizontalDragLinearLayout
                    .this.headerLayoutParams);
        }

        protected void onPostExecute(Integer leftMargin) {
            HorizontalDragLinearLayout.this.headerLayoutParams.leftMargin = leftMargin.intValue();
            HorizontalDragLinearLayout.this.headerView.setLayoutParams(HorizontalDragLinearLayout
                    .this.headerLayoutParams);
        }
    }

    public interface OnClick {
        void onClick();
    }

    public HorizontalDragLinearLayout(Context context) {
        this(context, null);
    }

    public HorizontalDragLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalDragLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.TAG = HorizontalDragLinearLayout.class.getSimpleName();
        this.deltaScroll = 10;
        this.downTime = 0;
        this.upTime = 0;
        this.upX = 0.0f;
        this.upY = 0.0f;
        setOrientation(0);
        this.context = context;
        init();
    }

    private void init() {
        this.mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        this.touchSlop = ViewConfiguration.get(this.context).getScaledTouchSlop();
        this.headerView = (LinearLayout) LayoutInflater.from(this.context).inflate(R.layout.k8,
                null);
        this.originTv = (TextView) this.headerView.findViewById(R.id.tv_origin_weight);
        LayoutParams params = new LayoutParams(-2, -2);
        params.gravity = 16;
        this.headerView.setOrientation(0);
        this.headerView.setGravity(17);
        this.headerView.setLayoutParams(params);
        addView(this.headerView, 0);
    }

    public void setHeaderText(String string) {
        if (this.originTv != null) {
            this.originTv.setText(string);
        }
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!(this.headerView == null || !changed || this.loadOnce)) {
            this.hideHeaderWidth = -this.headerView.getWidth();
            this.MAX_SCROLL = (-this.hideHeaderWidth) + DensityUtil.dip2px(this.context, (float)
                    this.deltaScroll);
            this.headerLayoutParams = (MarginLayoutParams) this.headerView.getLayoutParams();
            this.headerLayoutParams.leftMargin = this.hideHeaderWidth;
            this.headerView.setLayoutParams(this.headerLayoutParams);
            this.loadOnce = true;
        }
        if (getChildAt(1) != null) {
            getLayoutParams().width = (-this.hideHeaderWidth) + this.mScreenWidth;
            getChildAt(1).getLayoutParams().width = this.mScreenWidth;
            invalidate();
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.canFling) {
            getParent().requestDisallowInterceptTouchEvent(true);
            int distance;
            switch (motionEvent.getAction()) {
                case 0:
                    this.xDown = motionEvent.getRawX();
                    this.yDown = motionEvent.getRawY();
                    this.downTime = motionEvent.getEventTime();
                    return true;
                case 1:
                    this.upTime = motionEvent.getEventTime();
                    this.upX = motionEvent.getRawX();
                    this.upY = motionEvent.getRawY();
                    this.deltaTime = this.upTime - this.downTime;
                    distance = (int) Math.sqrt((double) ((Math.abs(this.xDown - this.upX) * Math
                            .abs(this.xDown - this.upX)) + (Math.abs(this.yDown - this.upY) *
                            Math.abs(this.yDown - this.upY))));
                    if ((this.deltaTime < 50 || distance < 5) && this.onClick != null) {
                        this.onClick.onClick();
                    }
                    new HideHeaderTask().execute(new Void[0]);
                    return true;
                case 2:
                    distance = (int) (motionEvent.getRawX() - this.xDown);
                    if (distance <= 0 && this.headerLayoutParams.leftMargin <= this
                            .hideHeaderWidth) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                        return true;
                    } else if (distance < this.touchSlop || this.headerLayoutParams.leftMargin >
                            (-this.hideHeaderWidth)) {
                        return true;
                    } else {
                        if (distance >= this.MAX_SCROLL) {
                            distance = this.MAX_SCROLL;
                        }
                        this.headerLayoutParams.leftMargin = this.hideHeaderWidth + distance;
                        this.headerView.setLayoutParams(this.headerLayoutParams);
                        return true;
                    }
                default:
                    return true;
            }
        }
        getParent().requestDisallowInterceptTouchEvent(false);
        return super.onTouchEvent(motionEvent);
    }

    public void setCanFling(boolean canFling) {
        this.canFling = canFling;
    }

    private void sleep(int i) {
        try {
            Thread.sleep((long) i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }
}
