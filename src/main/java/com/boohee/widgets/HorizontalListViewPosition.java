package com.boohee.widgets;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListAdapter;
import android.widget.Scroller;

import java.util.LinkedList;
import java.util.Queue;

public class HorizontalListViewPosition extends AdapterView<ListAdapter> {
    static final int LAYOUT_FREEZE   = 8;
    static final int LAYOUT_NORMAL   = 0;
    static final int LAYOUT_SPECIFIC = 4;
    protected ListAdapter mAdapter;
    public boolean mAlwaysOverrideTouch = true;
    protected int mCurrentX;
    private boolean         mDataChanged   = false;
    private DataSetObserver mDataObserver  = new DataSetObserver() {
        public void onChanged() {
            synchronized (HorizontalListViewPosition.this) {
                HorizontalListViewPosition.this.mDataChanged = true;
            }
            HorizontalListViewPosition.this.invalidate();
            HorizontalListViewPosition.this.requestLayout();
        }

        public void onInvalidated() {
            HorizontalListViewPosition.this.reset();
            HorizontalListViewPosition.this.invalidate();
            HorizontalListViewPosition.this.requestLayout();
        }
    };
    private int             mDisplayOffset = 0;
    private int             mFirstPosition = 0;
    private View mFreezeChild;
    private int mFreezePosInAdapter = -1;
    private GestureDetector mGesture;
    private boolean         mIsCancelOrUp;
    private boolean         mIsLayoutDirty;
    int mLayoutMode = 0;
    private int mLeftViewIndex = -1;
    private int mMaxX          = ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
    private int mMinX          = Integer.MIN_VALUE;
    protected int mNextX;
    private OnGestureListener mOnGesture = new SimpleOnGestureListener() {
        public boolean onDown(MotionEvent e) {
            return HorizontalListViewPosition.this.onDown(e);
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return HorizontalListViewPosition.this.onFling(e1, e2, velocityX, velocityY);
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return HorizontalListViewPosition.this.onScroll(e1, e2, distanceX, distanceY);
        }

        public boolean onSingleTapConfirmed(MotionEvent e) {
            for (int i = 0; i < HorizontalListViewPosition.this.getChildCount(); i++) {
                View child = HorizontalListViewPosition.this.getChildAt(i);
                if (isEventWithinView(e, child)) {
                    if (HorizontalListViewPosition.this.mOnItemClicked != null) {
                        HorizontalListViewPosition.this.mOnItemClicked.onItemClick
                                (HorizontalListViewPosition.this, child,
                                        (HorizontalListViewPosition.this.mLeftViewIndex + 1) + i,
                                        HorizontalListViewPosition.this.mAdapter.getItemId(
                                                (HorizontalListViewPosition.this.mLeftViewIndex +
                                                        1) + i));
                    }
                    if (HorizontalListViewPosition.this.mOnItemSelected != null) {
                        HorizontalListViewPosition.this.mOnItemSelected.onItemSelected
                                (HorizontalListViewPosition.this, child,
                                        (HorizontalListViewPosition.this.mLeftViewIndex + 1) + i,
                                        HorizontalListViewPosition.this.mAdapter.getItemId(
                                                (HorizontalListViewPosition.this.mLeftViewIndex +
                                                        1) + i));
                    }
                    return true;
                }
            }
            return true;
        }

        public void onLongPress(MotionEvent e) {
            int childCount = HorizontalListViewPosition.this.getChildCount();
            int i = 0;
            while (i < childCount) {
                View child = HorizontalListViewPosition.this.getChildAt(i);
                if (!isEventWithinView(e, child)) {
                    i++;
                } else if (HorizontalListViewPosition.this.mOnItemLongClicked != null) {
                    HorizontalListViewPosition.this.mOnItemLongClicked.onItemLongClick
                            (HorizontalListViewPosition.this, child, (HorizontalListViewPosition
                                    .this.mLeftViewIndex + 1) + i, HorizontalListViewPosition
                                    .this.mAdapter.getItemId((HorizontalListViewPosition.this
                                    .mLeftViewIndex + 1) + i));
                    return;
                } else {
                    return;
                }
            }
        }

        private boolean isEventWithinView(MotionEvent e, View child) {
            Rect viewRect = new Rect();
            int[] childPosition = new int[2];
            child.getLocationOnScreen(childPosition);
            int left = childPosition[0];
            int right = left + child.getWidth();
            int top = childPosition[1];
            viewRect.set(left, top, right, top + child.getHeight());
            return viewRect.contains((int) e.getRawX(), (int) e.getRawY());
        }
    };
    private OnItemClickListener     mOnItemClicked;
    private OnItemLongClickListener mOnItemLongClicked;
    private OnItemSelectedListener  mOnItemSelected;
    private OnScrollListener        mOnScrolled;
    private Queue<View> mRemovedViewQueue = new LinkedList();
    private int         mRightViewIndex   = 0;
    private   int      mScrollStatus;
    protected Scroller mScroller;
    private   int      mSpecificLeft;
    private   int      mSpecificOldLeft;
    private   int      mSpecificOldPosition;
    private   int      mSpecificPosition;

    public interface OnScrollListener {
        public static final int SCROLL_FLING        = 2;
        public static final int SCROLL_IDLE         = 0;
        public static final int SCROLL_TOUCH_SCROLL = 1;

        void onScroll(AdapterView<?> adapterView, int i, int i2, int i3);

        void onScrollStateChanged(AdapterView<?> adapterView, int i);
    }

    public HorizontalListViewPosition(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private synchronized void initView() {
        this.mLeftViewIndex = -1;
        this.mRightViewIndex = 0;
        this.mDisplayOffset = 0;
        this.mCurrentX = 0;
        this.mNextX = 0;
        this.mFirstPosition = 0;
        this.mSpecificPosition = 0;
        this.mSpecificLeft = 0;
        this.mMaxX = ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
        this.mMinX = Integer.MIN_VALUE;
        this.mScroller = new Scroller(getContext());
        if (!isInEditMode()) {
            this.mGesture = new GestureDetector(getContext(), this.mOnGesture);
        }
    }

    private synchronized void initViewForSpecific() {
        this.mLeftViewIndex = this.mSpecificPosition - 1;
        this.mRightViewIndex = this.mSpecificPosition + 1;
        this.mFirstPosition = this.mSpecificPosition;
        this.mDisplayOffset = 0;
        this.mCurrentX = 0;
        this.mNextX = 0;
        this.mMaxX = ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
        this.mMinX = Integer.MIN_VALUE;
        if (!isInEditMode()) {
            this.mGesture = new GestureDetector(getContext(), this.mOnGesture);
        }
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.mOnItemSelected = listener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClicked = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mOnItemLongClicked = listener;
    }

    public void setOnScrollListener(OnScrollListener listener) {
        this.mOnScrolled = listener;
    }

    public ListAdapter getAdapter() {
        return this.mAdapter;
    }

    public View getSelectedView() {
        return getChildAt(this.mSpecificPosition - getFirstVisiblePosition());
    }

    public void setAdapter(ListAdapter adapter) {
        if (this.mAdapter != null) {
            this.mAdapter.unregisterDataSetObserver(this.mDataObserver);
        }
        this.mAdapter = adapter;
        this.mAdapter.registerDataSetObserver(this.mDataObserver);
        this.mDataChanged = true;
        requestLayout();
    }

    private synchronized void reset() {
        initView();
        removeAllViewsInLayout();
        requestLayout();
    }

    public int getFirstVisiblePosition() {
        return this.mFirstPosition;
    }

    public int getLastVisiblePosition() {
        return (this.mFirstPosition + getChildCount()) - 1;
    }

    public void setSelection(int position) {
        setSelectionFromLeft(position, 0);
    }

    public void setSelectionFromLeft(int position, int x) {
        if (setSelectionFrom(position, x) >= 0) {
            requestLayout();
        }
    }

    private int setSelectionFrom(int position, int x) {
        if (this.mAdapter == null || position < 0 || position >= this.mAdapter.getCount()) {
            return -1;
        }
        if (!isInTouchMode()) {
            position = lookForSelectablePosition(position, true);
        }
        if (position >= 0) {
            this.mLayoutMode |= 4;
            this.mSpecificPosition = position;
            this.mSpecificLeft = getPaddingLeft() + x;
        }
        return position;
    }

    private boolean isAllowSelectionOnShown(int position, int delta) {
        int deltaPos = position - getFirstVisiblePosition();
        if (getChildCount() == 0 || deltaPos < 0 || deltaPos >= getChildCount()) {
            return false;
        }
        View posView = getChildAt(position - getFirstVisiblePosition());
        if (posView.getRight() + delta < 0 || posView.getLeft() + delta > getMeasuredWidth()) {
            return false;
        }
        return true;
    }

    public boolean isLayoutRequestedBySelection() {
        return Util.isFlagContain(this.mLayoutMode, 4);
    }

    public void requestLayout() {
        this.mIsLayoutDirty = true;
        super.requestLayout();
    }

    public void requestChildFreeze(View child, int posInAdapter) {
        this.mLayoutMode |= 8;
        if (this.mIsLayoutDirty) {
            this.mFreezeChild = child;
            this.mFreezePosInAdapter = posInAdapter;
            return;
        }
        setSelectionFromLeft(posInAdapter, child.getLeft());
    }

    public boolean isLayoutRequestByFreeze() {
        return Util.isFlagContain(this.mLayoutMode, 8);
    }

    private int lookForSelectablePosition(int position, boolean lookDown) {
        ListAdapter adapter = this.mAdapter;
        if (adapter == null || isInTouchMode()) {
            return -1;
        }
        int count = adapter.getCount();
        if (!adapter.areAllItemsEnabled()) {
            if (lookDown) {
                position = Math.max(0, position);
                while (position < count && !adapter.isEnabled(position)) {
                    position++;
                }
            } else {
                position = Math.min(position, count - 1);
                while (position >= 0 && !adapter.isEnabled(position)) {
                    position--;
                }
            }
            if (position < 0 || position >= count) {
                return -1;
            }
            return position;
        } else if (position < 0 || position >= count) {
            return -1;
        } else {
            return position;
        }
    }

    private void addAndMeasureChild(View child, int viewPos) {
        int childWidthSpec;
        LayoutParams params = child.getLayoutParams();
        if (params == null) {
            params = new LayoutParams(-2, -1);
        }
        addViewInLayout(child, viewPos, params, true);
        int childHeightSpec = ViewGroup.getChildMeasureSpec(MeasureSpec.makeMeasureSpec
                (getMeasuredHeight(), 1073741824), getPaddingTop() + getPaddingBottom(), params
                .height);
        if (params.width == -1) {
            childWidthSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
        } else if (params.width == -2) {
            childWidthSpec = MeasureSpec.makeMeasureSpec(0, 0);
        } else {
            childWidthSpec = MeasureSpec.makeMeasureSpec(params.width, 1073741824);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (this.mAdapter != null) {
            if (isLayoutRequestByFreeze()) {
                this.mLayoutMode &= -9;
                if (this.mIsLayoutDirty) {
                    int i;
                    this.mFirstPosition = this.mFreezePosInAdapter;
                    Util.v("Freeze pos = " + this.mFreezePosInAdapter);
                    Util.v("Freeze left = " + (this.mFreezeChild == null ? 0 : this.mFreezeChild
                            .getLeft()));
                    int i2 = this.mFreezePosInAdapter;
                    if (this.mFreezeChild == null) {
                        i = 0;
                    } else {
                        i = this.mFreezeChild.getLeft();
                    }
                    setSelectionFrom(i2, i);
                }
            }
            if (this.mIsLayoutDirty) {
                this.mIsLayoutDirty = false;
            }
            if (this.mDataChanged) {
                if (isLayoutRequestedBySelection()) {
                    initViewForSpecific();
                } else {
                    int oldCurrentX = this.mCurrentX;
                    initView();
                    removeAllViewsInLayout();
                    this.mNextX = oldCurrentX;
                }
                this.mDataChanged = false;
            }
            if (this.mScroller.computeScrollOffset()) {
                this.mNextX = this.mScroller.getCurrX();
            }
            if (this.mNextX <= this.mMinX) {
                this.mNextX = this.mMinX;
                this.mScroller.forceFinished(true);
            }
            if (this.mNextX >= this.mMaxX) {
                this.mNextX = this.mMaxX;
                this.mScroller.forceFinished(true);
            }
            if (Util.isFlagContain(this.mLayoutMode, 4)) {
                removeAllViewsInLayout();
                initViewForSpecific();
                fillSpecificV2(this.mSpecificPosition, this.mSpecificLeft);
                positionItems(this.mSpecificLeft);
                if (this.mScroller.computeScrollOffset()) {
                    this.mScroller.setFinalX(Math.max(Math.min(this.mScroller.getFinalX(), this
                            .mMaxX), this.mMinX));
                }
                this.mLayoutMode &= -5;
            } else {
                int dx = this.mCurrentX - this.mNextX;
                removeNonVisibleItems(dx);
                fillList(dx);
                positionItems(dx);
                if (this.mMinX == 0 || this.mMaxX == 0) {
                    this.mNextX = this.mCurrentX;
                    if (!this.mScroller.isFinished()) {
                        this.mScroller.forceFinished(true);
                    }
                }
            }
            this.mCurrentX = this.mNextX;
            if (this.mScroller.isFinished()) {
                reportScroll(0);
                reportScrollState(0);
                return;
            }
            post(new Runnable() {
                public void run() {
                    HorizontalListViewPosition.this.requestLayout();
                }
            });
        }
    }

    private void fillList(int dx) {
        int edge = 0;
        View child = getChildAt(getChildCount() - 1);
        if (child != null) {
            edge = child.getRight();
        }
        fillListRight(edge, dx);
        edge = 0;
        child = getChildAt(0);
        if (child != null) {
            edge = child.getLeft();
        }
        fillListLeft(edge, dx);
    }

    private void fillSpecificV2(int position, int delta) {
        View child = this.mAdapter.getView(position, (View) this.mRemovedViewQueue.poll(), this);
        if (child != null) {
            addAndMeasureChild(child, -1);
            if (child != null) {
                int leftEdge = delta;
                int rightEdge = delta + child.getMeasuredWidth();
                if (child.getMeasuredWidth() + leftEdge < 0 || rightEdge > getMeasuredWidth()) {
                    this.mSpecificLeft = 0;
                    leftEdge = 0;
                    rightEdge = child.getMeasuredWidth();
                }
                fillListRight(rightEdge, 0);
                int widthDelta = (getMeasuredWidth() - getChildrenWidth(0, getChildCount())) -
                        leftEdge;
                int childCountAfterFillRight = getChildCount();
                if (widthDelta > 0) {
                    leftEdge += widthDelta;
                    this.mSpecificLeft += widthDelta;
                }
                fillListLeft(leftEdge, 0);
                widthDelta = leftEdge - getChildrenWidth(0, getChildCount() -
                        childCountAfterFillRight);
                if (widthDelta > 0) {
                    this.mSpecificLeft -= widthDelta;
                }
            }
        }
    }

    int getChildrenWidth(int start, int end) {
        int allWidth = 0;
        for (int i = start; i < end; i++) {
            allWidth += getChildAt(i).getMeasuredWidth();
        }
        return allWidth;
    }

    private void fillListRight(int rightEdge, int dx) {
        if (this.mRightViewIndex >= this.mAdapter.getCount()) {
            this.mMaxX = (this.mCurrentX + rightEdge) - getWidth();
        }
        while (rightEdge + dx < getWidth() && this.mRightViewIndex < this.mAdapter.getCount()) {
            View child = this.mAdapter.getView(this.mRightViewIndex, (View) this
                    .mRemovedViewQueue.poll(), this);
            addAndMeasureChild(child, -1);
            rightEdge += child.getMeasuredWidth();
            if (this.mRightViewIndex == this.mAdapter.getCount() - 1) {
                this.mMaxX = (this.mCurrentX + rightEdge) - getWidth();
            }
            this.mRightViewIndex++;
        }
        if (this.mMaxX < 0) {
            this.mMaxX = 0;
        }
    }

    private void fillListLeft(int leftEdge, int dx) {
        if (this.mLeftViewIndex < 0) {
            this.mMinX = this.mCurrentX + leftEdge;
        }
        while (leftEdge + dx > 0 && this.mLeftViewIndex >= 0) {
            View child = this.mAdapter.getView(this.mLeftViewIndex, (View) this.mRemovedViewQueue
                    .poll(), this);
            addAndMeasureChild(child, 0);
            leftEdge -= child.getMeasuredWidth();
            if (this.mLeftViewIndex == 0) {
                this.mMinX = this.mCurrentX + leftEdge;
            }
            this.mLeftViewIndex--;
            this.mDisplayOffset -= child.getMeasuredWidth();
        }
        if (this.mMinX > 0) {
            this.mMinX = 0;
        }
        this.mFirstPosition = this.mLeftViewIndex + 1;
    }

    private void removeNonVisibleItems(int dx) {
        View child = getChildAt(0);
        while (child != null && child.getRight() + dx <= 0) {
            this.mDisplayOffset += child.getMeasuredWidth();
            this.mRemovedViewQueue.offer(child);
            removeViewInLayout(child);
            this.mLeftViewIndex++;
            child = getChildAt(0);
        }
        child = getChildAt(getChildCount() - 1);
        while (child != null && child.getLeft() + dx >= getWidth()) {
            this.mRemovedViewQueue.offer(child);
            removeViewInLayout(child);
            this.mRightViewIndex--;
            child = getChildAt(getChildCount() - 1);
        }
    }

    private void positionItems(int dx) {
        if (getChildCount() > 0) {
            this.mDisplayOffset += dx;
            int left = this.mDisplayOffset;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                int childWidth = child.getMeasuredWidth();
                child.layout(left, 0, left + childWidth, child.getMeasuredHeight());
                left += childWidth;
            }
        }
    }

    public synchronized void scrollTo(int x) {
        this.mScroller.startScroll(this.mNextX, 0, x - this.mNextX, 0);
        requestLayout();
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean z = true;
        boolean handled = super.dispatchTouchEvent(ev) | this.mGesture.onTouchEvent(ev);
        if (!(ev.getAction() == 3 || ev.getAction() == 1)) {
            z = false;
        }
        this.mIsCancelOrUp = z;
        return handled;
    }

    public boolean isScrollFinish() {
        return this.mScroller.isFinished() && this.mIsCancelOrUp;
    }

    public boolean isCancelOrUpNow() {
        return this.mIsCancelOrUp;
    }

    void reportScroll(int status) {
        if (!Util.isNull(this.mOnScrolled) && status != this.mScrollStatus) {
            int first = getFirstVisiblePosition();
            this.mOnScrolled.onScroll(this, first, getLastVisiblePosition() - first, this
                    .mAdapter.getCount());
        }
    }

    void reportScrollState(int status) {
        if (!Util.isNull(this.mOnScrolled) && status != this.mScrollStatus) {
            this.mScrollStatus = status;
            this.mOnScrolled.onScrollStateChanged(this, status);
        }
    }

    protected boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        synchronized (this) {
            reportScrollState(2);
            this.mScroller.fling(this.mNextX, 0, (int) (-velocityX), 0, this.mMinX, this.mMaxX,
                    0, 0);
            this.mScroller.computeScrollOffset();
        }
        requestLayout();
        return true;
    }

    protected boolean onDown(MotionEvent e) {
        this.mScroller.forceFinished(true);
        reportScrollState(0);
        return true;
    }

    protected boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        synchronized (this) {
            reportScrollState(1);
            if (Math.abs((int) distanceX) > 30) {
                this.mNextX = (distanceX > 0.0f ? 1 : -1) + this.mNextX;
            } else {
                this.mNextX += (int) distanceX;
            }
        }
        requestLayout();
        return true;
    }
}
