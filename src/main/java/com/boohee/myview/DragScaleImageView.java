package com.boohee.myview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

import com.baidu.location.aj;
import com.boohee.one.R;

@SuppressLint({"NewApi"})
public class DragScaleImageView extends ImageView implements OnTouchListener {
    private static final int    CENTER      = 25;
    private static final int    LEFT_BOTTOM = 19;
    private static final int    RIGHT_TOP   = 18;
    static final         String TAG         = "DragScaleImageView";
    private   Bitmap               closeBmp;
    private   int                  defaultWidth;
    private   int                  dragDirection;
    private   boolean              isFocus;
    protected int                  lastX;
    protected int                  lastY;
    private   int                  mCalory;
    private   OnDeleteListener     mDeleteListener;
    private   OnSingleTapListener  mOnSingleTabListener;
    private   OnSizeChangeListener mSizeChangeListener;
    private   int                  offset;
    private   int                  oriBottom;
    private   int                  oriLeft;
    private   int                  oriRight;
    private   int                  oriTop;
    protected Paint                paint;
    private   Bitmap               resizeBmp;
    protected int                  screenHeight;
    protected int                  screenWidth;
    private   int                  touchRegion;

    public interface OnDeleteListener {
        void onDelete();
    }

    public interface OnSingleTapListener {
        void onSingleTab();
    }

    public interface OnSizeChangeListener {
        void onSizeChanged();
    }

    protected void initScreenWidthHeight() {
        this.screenHeight = getResources().getDisplayMetrics().heightPixels;
        this.screenWidth = getResources().getDisplayMetrics().widthPixels;
    }

    public DragScaleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.offset = 0;
        this.paint = new Paint();
        this.isFocus = true;
        init();
    }

    public DragScaleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragScaleImageView(Context context) {
        this(context, null);
    }

    public DragScaleImageView(Context context, int calory) {
        this(context, null);
        this.mCalory = calory;
    }

    private void init() {
        int width;
        setClickable(true);
        setOnTouchListener(this);
        initScreenWidthHeight();
        this.resizeBmp = BitmapFactory.decodeResource(getResources(), R.drawable.vg);
        this.closeBmp = BitmapFactory.decodeResource(getResources(), R.drawable.vf);
        if (this.resizeBmp.getWidth() > this.resizeBmp.getHeight()) {
            width = this.resizeBmp.getWidth();
        } else {
            width = this.resizeBmp.getHeight();
        }
        this.touchRegion = width;
        setPadding(this.touchRegion / 2, this.touchRegion / 2, this.touchRegion / 2, this
                .touchRegion / 2);
        this.paint.setColor(-1);
        this.paint.setStrokeWidth(aj.hA);
        this.paint.setStyle(Style.STROKE);
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (VERSION.SDK_INT >= 11 && this.oriLeft != 0) {
            setLeft(this.oriLeft);
            setRight(this.oriRight);
            setTop(this.oriTop);
            setBottom(this.oriBottom);
        }
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.defaultWidth = getMeasuredWidth();
        if (this.mSizeChangeListener != null) {
            this.mSizeChangeListener.onSizeChanged();
        }
        if (this.oriLeft != 0) {
            layout(this.oriLeft, this.oriTop, this.oriRight, this.oriBottom);
        }
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.isFocus) {
            canvas.drawRect((float) (this.offset + getPaddingLeft()), (float) (this.offset +
                    getPaddingTop()), (float) ((getWidth() - this.offset) - getPaddingRight()),
                    (float) ((getHeight() - this.offset) - getPaddingBottom()), this.paint);
            canvas.drawBitmap(this.resizeBmp, (float) (getWidth() - this.resizeBmp.getWidth()), 0
            .0f, this.paint);
            canvas.drawBitmap(this.closeBmp, 0.0f, (float) (getHeight() - this.closeBmp.getHeight
                    ()), this.paint);
        }
    }

    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        if (action == 0) {
            this.oriLeft = v.getLeft();
            this.oriRight = v.getRight();
            this.oriTop = v.getTop();
            this.oriBottom = v.getBottom();
            this.lastY = (int) event.getRawY();
            this.lastX = (int) event.getRawX();
            this.dragDirection = getDirection(v, (int) event.getX(), (int) event.getY());
            if (this.dragDirection == 19) {
                setVisibility(4);
                if (this.mDeleteListener != null) {
                    this.mDeleteListener.onDelete();
                }
                return false;
            }
            if (this.mOnSingleTabListener != null) {
                this.mOnSingleTabListener.onSingleTab();
            }
            this.isFocus = true;
        }
        delDrag(v, event, action);
        invalidate();
        return false;
    }

    protected void delDrag(View v, MotionEvent event, int action) {
        switch (action) {
            case 1:
                this.dragDirection = 0;
                return;
            case 2:
                int dx = ((int) event.getRawX()) - this.lastX;
                int dy = ((int) event.getRawY()) - this.lastY;
                switch (this.dragDirection) {
                    case 18:
                        topRight(v, dx);
                        v.layout(this.oriLeft, this.oriTop, this.oriRight, this.oriBottom);
                        break;
                    case 25:
                        center(v, dx, dy);
                        break;
                }
                this.lastX = (int) event.getRawX();
                this.lastY = (int) event.getRawY();
                return;
            default:
                return;
        }
    }

    private void center(View v, int dx, int dy) {
        int left = v.getLeft() + dx;
        int top = v.getTop() + dy;
        int right = v.getRight() + dx;
        int bottom = v.getBottom() + dy;
        this.oriLeft = left;
        this.oriTop = top;
        this.oriRight = right;
        this.oriBottom = bottom;
        v.layout(left, top, right, bottom);
    }

    private void topRight(View v, int dx) {
        if (this.oriTop - dx > 0 && this.oriRight + dx < this.screenWidth) {
            int minWidth = this.defaultWidth / 2;
            this.oriRight += dx;
            if (this.oriRight > this.screenWidth + this.offset) {
                this.oriRight = this.screenWidth + this.offset;
            }
            this.oriTop -= dx;
            if (this.oriTop < (-this.offset)) {
                this.oriTop = -this.offset;
            }
            if ((this.oriRight - this.oriLeft) - (this.offset * 2) < minWidth) {
                this.oriRight = (this.oriLeft + (this.offset * 2)) + minWidth;
            }
            if ((this.oriBottom - this.oriTop) - (this.offset * 2) < minWidth) {
                this.oriTop = (this.oriBottom - (this.offset * 2)) - minWidth;
            }
        }
    }

    protected int getDirection(View v, int x, int y) {
        int left = v.getLeft();
        int right = v.getRight();
        int bottom = v.getBottom();
        int top = v.getTop();
        if (y < this.touchRegion && (right - left) - x < this.touchRegion) {
            return 18;
        }
        if (x >= this.touchRegion || (bottom - top) - y >= this.touchRegion) {
            return 25;
        }
        return 19;
    }

    public void setFocusable(boolean focus) {
        this.isFocus = focus;
        invalidate();
    }

    public boolean getFocusable() {
        return this.isFocus;
    }

    public int getZoom() {
        return getWidth() - this.defaultWidth;
    }

    public void setCalory(int calory) {
        this.mCalory = calory;
    }

    public int getCalory() {
        return getZoom() + this.mCalory;
    }

    public void setOnSizeChangedListener(OnSizeChangeListener listener) {
        this.mSizeChangeListener = listener;
    }

    public void setOnSingleTabListener(OnSingleTapListener listener) {
        this.mOnSingleTabListener = listener;
    }

    public void setOnDeleteListener(OnDeleteListener listener) {
        this.mDeleteListener = listener;
    }
}
