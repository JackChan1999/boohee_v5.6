package com.huewu.pla.lib.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewDebug.IntToString;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.ViewTreeObserver.OnTouchModeChangeListener;
import android.widget.ListAdapter;

import com.boohee.status.FriendShipActivity;
import com.huewu.pla.R;
import com.huewu.pla.lib.DebugUtil;
import com.huewu.pla.lib.internal.PLA_AdapterView.AdapterContextMenuInfo;
import com.huewu.pla.lib.internal.PLA_AdapterView.AdapterDataSetObserver;

import java.util.ArrayList;
import java.util.List;

public abstract class PLA_AbsListView extends PLA_AdapterView<ListAdapter> implements
        OnGlobalLayoutListener, OnTouchModeChangeListener {
    private static final   int     INVALID_POINTER               = -1;
    static final           int     LAYOUT_FORCE_BOTTOM           = 3;
    static final           int     LAYOUT_FORCE_TOP              = 1;
    static final           int     LAYOUT_MOVE_SELECTION         = 6;
    static final           int     LAYOUT_NORMAL                 = 0;
    static final           int     LAYOUT_SET_SELECTION          = 2;
    static final           int     LAYOUT_SPECIFIC               = 4;
    static final           int     LAYOUT_SYNC                   = 5;
    private static final   boolean PROFILE_FLINGING              = false;
    private static final   boolean PROFILE_SCROLLING             = false;
    private static final   String  TAG                           = "PLA_AbsListView";
    protected static final int     TOUCH_MODE_DONE_WAITING       = 2;
    protected static final int     TOUCH_MODE_DOWN               = 0;
    protected static final int     TOUCH_MODE_FLING              = 4;
    private static final   int     TOUCH_MODE_OFF                = 1;
    private static final   int     TOUCH_MODE_ON                 = 0;
    static final           int     TOUCH_MODE_REST               = -1;
    protected static final int     TOUCH_MODE_SCROLL             = 3;
    protected static final int     TOUCH_MODE_TAP                = 1;
    private static final   int     TOUCH_MODE_UNKNOWN            = -1;
    public static final    int     TRANSCRIPT_MODE_ALWAYS_SCROLL = 2;
    public static final    int     TRANSCRIPT_MODE_DISABLED      = 0;
    public static final    int     TRANSCRIPT_MODE_NORMAL        = 1;
    private   int             mActivePointerId;
    protected ListAdapter     mAdapter;
    private   int             mCacheColorHint;
    protected boolean         mCachingStarted;
    private   Runnable        mClearScrollingCache;
    private   ContextMenuInfo mContextMenuInfo;
    AdapterDataSetObserver mDataSetObserver;
    boolean                mDrawSelectorOnTop;
    private boolean       mFlingProfilingStarted;
    private FlingRunnable mFlingRunnable;
    private boolean       mIsChildViewEnabled;
    final   boolean[]     mIsScrap;
    private int           mLastScrollState;
    private int           mLastTouchMode;
    int mLastY;
    int mLayoutMode;
    protected Rect mListPadding;
    private   int  mMaximumVelocity;
    private   int  mMinimumVelocity;
    int mMotionCorrection;
    protected int mMotionPosition;
    int mMotionViewNewTop;
    int mMotionViewOriginalTop;
    int mMotionX;
    int mMotionY;
    private OnScrollListener mOnScrollListener;
    private Runnable         mPendingCheckForTap;
    private SavedState       mPendingSync;
    private PerformClick     mPerformClick;
    PositionScroller mPositionScroller;
    final RecycleBin mRecycler;
    int mResurrectToPosition;
    private boolean mScrollProfilingStarted;
    boolean  mScrollingCacheEnabled;
    int      mSelectedTop;
    int      mSelectionBottomPadding;
    int      mSelectionLeftPadding;
    int      mSelectionRightPadding;
    int      mSelectionTopPadding;
    Drawable mSelector;
    Rect     mSelectorRect;
    private boolean mSmoothScrollbarEnabled;
    boolean mStackFromBottom;
    private   Rect            mTouchFrame;
    protected int             mTouchMode;
    private   int             mTouchSlop;
    private   int             mTranscriptMode;
    private   VelocityTracker mVelocityTracker;
    protected int             mWidthMeasureSpec;

    public static class LayoutParams extends android.view.ViewGroup.LayoutParams {
        @ExportedProperty
        public boolean forceAdd;
        @ExportedProperty
        public boolean recycledHeaderFooter;
        public int     scrappedFromPosition;
        @ExportedProperty(mapping = {@IntToString(from = -1, to = "ITEM_VIEW_TYPE_IGNORE"),
                @IntToString(from = -2, to = "ITEM_VIEW_TYPE_HEADER_OR_FOOTER")})
        public int     viewType;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int w, int h) {
            super(w, h);
        }

        public LayoutParams(int w, int h, int viewType) {
            super(w, h);
            this.viewType = viewType;
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
            super(source);
        }
    }

    abstract void fillGap(boolean z);

    abstract int findMotionRow(int i);

    public PLA_AbsListView(Context context) {
        super(context);
        this.mLayoutMode = 0;
        this.mDrawSelectorOnTop = false;
        this.mSelectorRect = new Rect();
        this.mRecycler = new RecycleBin(this);
        this.mSelectionLeftPadding = 0;
        this.mSelectionTopPadding = 0;
        this.mSelectionRightPadding = 0;
        this.mSelectionBottomPadding = 0;
        this.mListPadding = new Rect();
        this.mWidthMeasureSpec = 0;
        this.mTouchMode = -1;
        this.mSelectedTop = 0;
        this.mSmoothScrollbarEnabled = true;
        this.mResurrectToPosition = -1;
        this.mContextMenuInfo = null;
        this.mLastTouchMode = -1;
        this.mScrollProfilingStarted = false;
        this.mFlingProfilingStarted = false;
        this.mLastScrollState = 0;
        this.mIsScrap = new boolean[1];
        this.mActivePointerId = -1;
        initAbsListView();
        setVerticalScrollBarEnabled(true);
        TypedArray a = context.obtainStyledAttributes(R.styleable.View);
        try {
            View.class.getDeclaredMethod("initializeScrollbars", new Class[]{TypedArray.class})
                    .invoke(this, new Object[]{a});
        } catch (Exception e) {
            e.printStackTrace();
        }
        a.recycle();
    }

    public PLA_AbsListView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.absListViewStyle);
    }

    public PLA_AbsListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mLayoutMode = 0;
        this.mDrawSelectorOnTop = false;
        this.mSelectorRect = new Rect();
        this.mRecycler = new RecycleBin(this);
        this.mSelectionLeftPadding = 0;
        this.mSelectionTopPadding = 0;
        this.mSelectionRightPadding = 0;
        this.mSelectionBottomPadding = 0;
        this.mListPadding = new Rect();
        this.mWidthMeasureSpec = 0;
        this.mTouchMode = -1;
        this.mSelectedTop = 0;
        this.mSmoothScrollbarEnabled = true;
        this.mResurrectToPosition = -1;
        this.mContextMenuInfo = null;
        this.mLastTouchMode = -1;
        this.mScrollProfilingStarted = false;
        this.mFlingProfilingStarted = false;
        this.mLastScrollState = 0;
        this.mIsScrap = new boolean[1];
        this.mActivePointerId = -1;
        initAbsListView();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AbsListView, defStyle, 0);
        Drawable d = a.getDrawable(R.styleable.AbsListView_listSelector);
        if (d != null) {
            setSelector(d);
        }
        this.mDrawSelectorOnTop = a.getBoolean(R.styleable.AbsListView_drawSelectorOnTop, false);
        setStackFromBottom(a.getBoolean(R.styleable.AbsListView_stackFromBottom, false));
        setScrollingCacheEnabled(a.getBoolean(R.styleable.AbsListView_scrollingCache, true));
        setTranscriptMode(a.getInt(R.styleable.AbsListView_transcriptMode, 0));
        setCacheColorHint(a.getColor(R.styleable.AbsListView_cacheColorHint, 0));
        setSmoothScrollbarEnabled(a.getBoolean(R.styleable.AbsListView_smoothScrollbar, true));
        a.recycle();
    }

    private void initAbsListView() {
        setClickable(true);
        setFocusableInTouchMode(true);
        setWillNotDraw(false);
        setAlwaysDrawnWithCacheEnabled(false);
        setScrollingCacheEnabled(true);
        ViewConfiguration configuration = ViewConfiguration.get(getContext());
        this.mTouchSlop = configuration.getScaledTouchSlop();
        this.mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        this.mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
    }

    public void setSmoothScrollbarEnabled(boolean enabled) {
        this.mSmoothScrollbarEnabled = enabled;
    }

    @ExportedProperty
    public boolean isSmoothScrollbarEnabled() {
        return this.mSmoothScrollbarEnabled;
    }

    public void setOnScrollListener(OnScrollListener l) {
        this.mOnScrollListener = l;
        invokeOnItemScrollListener();
    }

    void invokeOnItemScrollListener() {
        if (this.mOnScrollListener != null) {
            this.mOnScrollListener.onScroll(this, this.mFirstPosition, getChildCount(), this
                    .mItemCount);
        }
    }

    @ExportedProperty
    public boolean isScrollingCacheEnabled() {
        return this.mScrollingCacheEnabled;
    }

    public void setScrollingCacheEnabled(boolean enabled) {
        if (this.mScrollingCacheEnabled && !enabled) {
            clearScrollingCache();
        }
        this.mScrollingCacheEnabled = enabled;
    }

    public void getFocusedRect(Rect r) {
        View view = getSelectedView();
        if (view == null || view.getParent() != this) {
            super.getFocusedRect(r);
            return;
        }
        view.getFocusedRect(r);
        offsetDescendantRectToMyCoords(view, r);
    }

    private void useDefaultSelector() {
        setSelector(getResources().getDrawable(17301602));
    }

    @ExportedProperty
    public boolean isStackFromBottom() {
        return this.mStackFromBottom;
    }

    public void setStackFromBottom(boolean stackFromBottom) {
        if (this.mStackFromBottom != stackFromBottom) {
            this.mStackFromBottom = stackFromBottom;
            requestLayoutIfNecessary();
        }
    }

    void requestLayoutIfNecessary() {
        if (getChildCount() > 0) {
            resetList();
            requestLayout();
            invalidate();
        }
    }

    public void requestLayout() {
        if (!this.mBlockLayoutRequests && !this.mInLayout) {
            super.requestLayout();
        }
    }

    void resetList() {
        removeAllViewsInLayout();
        this.mFirstPosition = 0;
        this.mDataChanged = false;
        this.mNeedSync = false;
        this.mOldSelectedPosition = -1;
        this.mOldSelectedRowId = Long.MIN_VALUE;
        this.mSelectedTop = 0;
        this.mSelectorRect.setEmpty();
        invalidate();
    }

    protected int computeVerticalScrollExtent() {
        int count = getChildCount();
        if (count <= 0) {
            return 0;
        }
        if (!this.mSmoothScrollbarEnabled) {
            return 1;
        }
        int extent = count * 100;
        View view = getChildAt(0);
        int top = getFillChildTop();
        int height = view.getHeight();
        if (height > 0) {
            extent += (top * 100) / height;
        }
        view = getChildAt(count - 1);
        int bottom = getScrollChildBottom();
        height = view.getHeight();
        if (height > 0) {
            return extent - (((bottom - getHeight()) * 100) / height);
        }
        return extent;
    }

    protected int computeVerticalScrollOffset() {
        int firstPosition = this.mFirstPosition;
        int childCount = getChildCount();
        if (firstPosition < 0 || childCount <= 0) {
            return 0;
        }
        if (this.mSmoothScrollbarEnabled) {
            View view = getChildAt(0);
            int top = getFillChildTop();
            int height = view.getHeight();
            if (height > 0) {
                return Math.max(((firstPosition * 100) - ((top * 100) / height)) + ((int) ((((
                        (float) getScrollY()) / ((float) getHeight())) * ((float) this
                        .mItemCount)) * 100.0f)), 0);
            }
            return 0;
        }
        int index;
        int count = this.mItemCount;
        if (firstPosition == 0) {
            index = 0;
        } else if (firstPosition + childCount == count) {
            index = count;
        } else {
            index = firstPosition + (childCount / 2);
        }
        return (int) (((float) firstPosition) + (((float) childCount) * (((float) index) / (
                (float) count))));
    }

    protected int computeVerticalScrollRange() {
        if (this.mSmoothScrollbarEnabled) {
            return Math.max(this.mItemCount * 100, 0);
        }
        return this.mItemCount;
    }

    protected float getTopFadingEdgeStrength() {
        int count = getChildCount();
        float fadeEdge = super.getTopFadingEdgeStrength();
        if (count == 0) {
            return fadeEdge;
        }
        if (this.mFirstPosition > 0) {
            return 1.0f;
        }
        int top = getChildAt(0).getTop();
        return top < getPaddingTop() ? ((float) (-(top - getPaddingTop()))) / ((float)
                getVerticalFadingEdgeLength()) : fadeEdge;
    }

    protected float getBottomFadingEdgeStrength() {
        int count = getChildCount();
        float fadeEdge = super.getBottomFadingEdgeStrength();
        if (count == 0) {
            return fadeEdge;
        }
        if ((this.mFirstPosition + count) - 1 < this.mItemCount - 1) {
            return 1.0f;
        }
        int bottom = getChildAt(count - 1).getBottom();
        int height = getHeight();
        return bottom > height - getPaddingBottom() ? ((float) ((bottom - height) +
                getPaddingBottom())) / ((float) getVerticalFadingEdgeLength()) : fadeEdge;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.mSelector == null) {
            useDefaultSelector();
        }
        Rect listPadding = this.mListPadding;
        listPadding.left = this.mSelectionLeftPadding + getPaddingLeft();
        listPadding.top = this.mSelectionTopPadding + getPaddingTop();
        listPadding.right = this.mSelectionRightPadding + getPaddingRight();
        listPadding.bottom = this.mSelectionBottomPadding + getPaddingBottom();
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        this.mInLayout = true;
        if (changed) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                getChildAt(i).forceLayout();
            }
            this.mRecycler.markChildrenDirty();
        }
        DebugUtil.i("onLayout");
        layoutChildren();
        this.mInLayout = false;
    }

    protected void layoutChildren() {
    }

    @ExportedProperty
    public View getSelectedView() {
        return null;
    }

    public int getListPaddingTop() {
        return this.mListPadding.top;
    }

    public int getListPaddingBottom() {
        return this.mListPadding.bottom;
    }

    public int getListPaddingLeft() {
        return this.mListPadding.left;
    }

    public int getListPaddingRight() {
        return this.mListPadding.right;
    }

    View obtainView(int position, boolean[] isScrap) {
        View child;
        isScrap[0] = false;
        View scrapView = this.mRecycler.getScrapView(position);
        if (scrapView != null) {
            child = this.mAdapter.getView(position, scrapView, this);
            if (child != scrapView) {
                DebugUtil.i("obtainView");
                this.mRecycler.addScrapView(scrapView);
                if (this.mCacheColorHint != 0) {
                    child.setDrawingCacheBackgroundColor(this.mCacheColorHint);
                }
            } else {
                isScrap[0] = true;
                dispatchFinishTemporaryDetach(child);
            }
        } else {
            DebugUtil.i("makeView:" + position);
            child = this.mAdapter.getView(position, null, this);
            if (this.mCacheColorHint != 0) {
                child.setDrawingCacheBackgroundColor(this.mCacheColorHint);
            }
        }
        return child;
    }

    void positionSelector(View sel) {
        Rect selectorRect = this.mSelectorRect;
        selectorRect.set(sel.getLeft(), sel.getTop(), sel.getRight(), sel.getBottom());
        positionSelector(selectorRect.left, selectorRect.top, selectorRect.right, selectorRect
                .bottom);
        boolean isChildViewEnabled = this.mIsChildViewEnabled;
        if (sel.isEnabled() != isChildViewEnabled) {
            this.mIsChildViewEnabled = !isChildViewEnabled;
            refreshDrawableState();
        }
    }

    private void positionSelector(int l, int t, int r, int b) {
        this.mSelectorRect.set(l - this.mSelectionLeftPadding, t - this.mSelectionTopPadding,
                this.mSelectionRightPadding + r, this.mSelectionBottomPadding + b);
    }

    protected void dispatchDraw(Canvas canvas) {
        boolean drawSelectorOnTop = this.mDrawSelectorOnTop;
        if (!drawSelectorOnTop) {
            drawSelector(canvas);
        }
        super.dispatchDraw(canvas);
        if (drawSelectorOnTop) {
            drawSelector(canvas);
        }
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (getChildCount() > 0) {
            DebugUtil.LogDebug("data changed by onSizeChanged()");
            this.mDataChanged = true;
            rememberSyncState();
        }
    }

    boolean touchModeDrawsInPressedState() {
        switch (this.mTouchMode) {
            case 1:
            case 2:
                return true;
            default:
                return false;
        }
    }

    protected boolean shouldShowSelector() {
        return (hasFocus() && !isInTouchMode()) || touchModeDrawsInPressedState();
    }

    private void drawSelector(Canvas canvas) {
        if (shouldShowSelector() && this.mSelectorRect != null && !this.mSelectorRect.isEmpty()) {
            Drawable selector = this.mSelector;
            selector.setBounds(this.mSelectorRect);
            selector.draw(canvas);
        }
    }

    public void setDrawSelectorOnTop(boolean onTop) {
        this.mDrawSelectorOnTop = onTop;
    }

    public void setSelector(int resID) {
        setSelector(getResources().getDrawable(resID));
    }

    public void setSelector(Drawable sel) {
        if (this.mSelector != null) {
            this.mSelector.setCallback(null);
            unscheduleDrawable(this.mSelector);
        }
        this.mSelector = sel;
        Rect padding = new Rect();
        sel.getPadding(padding);
        this.mSelectionLeftPadding = padding.left;
        this.mSelectionTopPadding = padding.top;
        this.mSelectionRightPadding = padding.right;
        this.mSelectionBottomPadding = padding.bottom;
        sel.setCallback(this);
        sel.setState(getDrawableState());
    }

    public Drawable getSelector() {
        return this.mSelector;
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (this.mSelector != null) {
            this.mSelector.setState(getDrawableState());
        }
    }

    protected int[] onCreateDrawableState(int extraSpace) {
        if (this.mIsChildViewEnabled) {
            return super.onCreateDrawableState(extraSpace);
        }
        int enabledState = ENABLED_STATE_SET[0];
        int[] state = super.onCreateDrawableState(extraSpace + 1);
        int enabledPos = -1;
        for (int i = state.length - 1; i >= 0; i--) {
            if (state[i] == enabledState) {
                enabledPos = i;
                break;
            }
        }
        if (enabledPos < 0) {
            return state;
        }
        System.arraycopy(state, enabledPos + 1, state, enabledPos, (state.length - enabledPos) - 1);
        return state;
    }

    public boolean verifyDrawable(Drawable dr) {
        return this.mSelector == dr || super.verifyDrawable(dr);
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ViewTreeObserver treeObserver = getViewTreeObserver();
        if (treeObserver != null) {
            treeObserver.addOnTouchModeChangeListener(this);
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mRecycler.clear();
        ViewTreeObserver treeObserver = getViewTreeObserver();
        if (treeObserver != null) {
            treeObserver.removeOnTouchModeChangeListener(this);
        }
    }

    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        DebugUtil.i("onWindowFocusChanged");
        int touchMode = isInTouchMode() ? 0 : 1;
        if (!hasWindowFocus) {
            setChildrenDrawingCacheEnabled(false);
            if (this.mFlingRunnable != null) {
                removeCallbacks(this.mFlingRunnable);
                FlingRunnable.access$000(this.mFlingRunnable);
                if (getScrollY() != 0) {
                    scrollTo(getScrollX(), 0);
                    invalidate();
                }
            }
        } else if (!(touchMode == this.mLastTouchMode || this.mLastTouchMode == -1)) {
            this.mLayoutMode = 0;
            DebugUtil.i("onWindowFocusChanged");
            layoutChildren();
        }
        this.mLastTouchMode = touchMode;
    }

    ContextMenuInfo createContextMenuInfo(View view, int position, long id) {
        return new AdapterContextMenuInfo(view, position, id);
    }

    protected ContextMenuInfo getContextMenuInfo() {
        return this.mContextMenuInfo;
    }

    public boolean showContextMenuForChild(View originalView) {
        int longPressPosition = getPositionForView(originalView);
        if (longPressPosition < 0) {
            return false;
        }
        long longPressId = this.mAdapter.getItemId(longPressPosition);
        boolean handled = false;
        if (this.mOnItemLongClickListener != null) {
            handled = this.mOnItemLongClickListener.onItemLongClick(this, originalView,
                    longPressPosition, longPressId);
        }
        if (handled) {
            return handled;
        }
        this.mContextMenuInfo = createContextMenuInfo(getChildAt(longPressPosition - this
                .mFirstPosition), longPressPosition, longPressId);
        return super.showContextMenuForChild(originalView);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    protected void dispatchSetPressed(boolean pressed) {
    }

    public int pointToPosition(int x, int y) {
        Rect frame = this.mTouchFrame;
        if (frame == null) {
            this.mTouchFrame = new Rect();
            frame = this.mTouchFrame;
        }
        for (int i = getChildCount() - 1; i >= 0; i--) {
            View child = getChildAt(i);
            if (child.getVisibility() == 0) {
                child.getHitRect(frame);
                if (frame.contains(x, y)) {
                    return this.mFirstPosition + i;
                }
            }
        }
        return -1;
    }

    public long pointToRowId(int x, int y) {
        int position = pointToPosition(x, y);
        if (position >= 0) {
            return this.mAdapter.getItemId(position);
        }
        return Long.MIN_VALUE;
    }

    private boolean startScrollIfNeeded(int deltaY) {
        if (Math.abs(deltaY) <= this.mTouchSlop) {
            return false;
        }
        createScrollingCache();
        this.mTouchMode = 3;
        this.mMotionCorrection = deltaY;
        setPressed(false);
        View motionView = getChildAt(this.mMotionPosition - this.mFirstPosition);
        if (motionView != null) {
            motionView.setPressed(false);
        }
        reportScrollStateChange(1);
        requestDisallowInterceptTouchEvent(true);
        return true;
    }

    public void onTouchModeChanged(boolean isInTouchMode) {
        if (isInTouchMode && getHeight() > 0 && getChildCount() > 0) {
            layoutChildren();
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (isEnabled()) {
            int action = ev.getAction();
            if (this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            }
            this.mVelocityTracker.addMovement(ev);
            int x;
            int y;
            int motionPosition;
            switch (action & 255) {
                case 0:
                    this.mActivePointerId = ev.getPointerId(0);
                    x = (int) ev.getX();
                    y = (int) ev.getY();
                    motionPosition = pointToPosition(x, y);
                    if (!this.mDataChanged) {
                        if (this.mTouchMode != 4 && motionPosition >= 0 && ((ListAdapter)
                                getAdapter()).isEnabled(motionPosition)) {
                            this.mTouchMode = 0;
                            if (this.mPendingCheckForTap == null) {
                                this.mPendingCheckForTap = new CheckForTap(this);
                            }
                            postDelayed(this.mPendingCheckForTap, (long) ViewConfiguration
                                    .getTapTimeout());
                        } else if (ev.getEdgeFlags() != 0 && motionPosition < 0) {
                            return false;
                        } else {
                            if (this.mTouchMode == 4) {
                                createScrollingCache();
                                this.mTouchMode = 3;
                                this.mMotionCorrection = 0;
                                motionPosition = findMotionRow(y);
                                reportScrollStateChange(1);
                            }
                        }
                    }
                    if (motionPosition >= 0) {
                        this.mMotionViewOriginalTop = getChildAt(motionPosition - this
                                .mFirstPosition).getTop();
                    }
                    this.mMotionX = x;
                    this.mMotionY = y;
                    this.mMotionPosition = motionPosition;
                    this.mLastY = Integer.MIN_VALUE;
                    break;
                case 1:
                    switch (this.mTouchMode) {
                        case 0:
                        case 1:
                        case 2:
                            motionPosition = this.mMotionPosition;
                            View child = getChildAt(motionPosition - this.mFirstPosition);
                            if (!(child == null || child.hasFocusable())) {
                                if (this.mTouchMode != 0) {
                                    child.setPressed(false);
                                }
                                if (this.mPerformClick == null) {
                                    this.mPerformClick = new PerformClick(this, null);
                                }
                                PerformClick performClick = this.mPerformClick;
                                performClick.mChild = child;
                                performClick.mClickMotionPosition = motionPosition;
                                performClick.rememberWindowAttachCount();
                                this.mResurrectToPosition = motionPosition;
                                if (this.mTouchMode == 0 || this.mTouchMode == 1) {
                                    this.mLayoutMode = 0;
                                    if (this.mDataChanged || !this.mAdapter.isEnabled
                                            (motionPosition)) {
                                        this.mTouchMode = -1;
                                    } else {
                                        this.mTouchMode = 1;
                                        layoutChildren();
                                        child.setPressed(true);
                                        positionSelector(child);
                                        setPressed(true);
                                        if (this.mSelector != null) {
                                            Drawable d = this.mSelector.getCurrent();
                                            if (d != null && (d instanceof TransitionDrawable)) {
                                                ((TransitionDrawable) d).resetTransition();
                                            }
                                        }
                                        postDelayed(new 1 (this, child, performClick),
                                        (long) ViewConfiguration.getPressedStateDuration());
                                    }
                                    return true;
                                } else if (!this.mDataChanged && this.mAdapter.isEnabled
                                        (motionPosition)) {
                                    post(performClick);
                                }
                            }
                            this.mTouchMode = -1;
                            break;
                        case 3:
                            int childCount = getChildCount();
                            if (childCount <= 0) {
                                this.mTouchMode = -1;
                                reportScrollStateChange(0);
                                break;
                            }
                            int top = getFillChildTop();
                            int bottom = getFillChildBottom();
                            if (this.mFirstPosition == 0 && top >= this.mListPadding.top && this
                                    .mFirstPosition + childCount < this.mItemCount && bottom <=
                                    getHeight() - this.mListPadding.bottom) {
                                this.mTouchMode = -1;
                                reportScrollStateChange(0);
                                break;
                            }
                            VelocityTracker velocityTracker = this.mVelocityTracker;
                            velocityTracker.computeCurrentVelocity(1000, (float) this
                                    .mMaximumVelocity);
                            int initialVelocity = (int) velocityTracker.getYVelocity(this
                                    .mActivePointerId);
                            if (Math.abs(initialVelocity) <= this.mMinimumVelocity) {
                                this.mTouchMode = -1;
                                reportScrollStateChange(0);
                                break;
                            }
                            if (this.mFlingRunnable == null) {
                                this.mFlingRunnable = new FlingRunnable(this);
                            }
                            reportScrollStateChange(2);
                            this.mFlingRunnable.start(-initialVelocity);
                            break;
                        break;
                    }
                    setPressed(false);
                    invalidate();
                    if (this.mVelocityTracker != null) {
                        this.mVelocityTracker.recycle();
                        this.mVelocityTracker = null;
                    }
                    this.mActivePointerId = -1;
                    break;
                case 2:
                    y = (int) ev.getY(ev.findPointerIndex(this.mActivePointerId));
                    int deltaY = y - this.mMotionY;
                    switch (this.mTouchMode) {
                        case 0:
                        case 1:
                        case 2:
                            startScrollIfNeeded(deltaY);
                            break;
                        case 3:
                            if (y != this.mLastY) {
                                int incrementalDeltaY;
                                deltaY -= this.mMotionCorrection;
                                if (this.mLastY != Integer.MIN_VALUE) {
                                    incrementalDeltaY = y - this.mLastY;
                                } else {
                                    incrementalDeltaY = deltaY;
                                }
                                boolean atEdge = false;
                                if (incrementalDeltaY != 0) {
                                    atEdge = trackMotionScroll(deltaY, incrementalDeltaY);
                                }
                                if (atEdge && getChildCount() > 0) {
                                    motionPosition = findMotionRow(y);
                                    if (motionPosition >= 0) {
                                        this.mMotionViewOriginalTop = getChildAt(motionPosition -
                                                this.mFirstPosition).getTop();
                                    }
                                    this.mMotionY = y;
                                    this.mMotionPosition = motionPosition;
                                    invalidate();
                                }
                                this.mLastY = y;
                                break;
                            }
                            break;
                        default:
                            break;
                    }
                case 3:
                    this.mTouchMode = -1;
                    setPressed(false);
                    View motionView = getChildAt(this.mMotionPosition - this.mFirstPosition);
                    if (motionView != null) {
                        motionView.setPressed(false);
                    }
                    clearScrollingCache();
                    if (this.mVelocityTracker != null) {
                        this.mVelocityTracker.recycle();
                        this.mVelocityTracker = null;
                    }
                    this.mActivePointerId = -1;
                    break;
                case 6:
                    onSecondaryPointerUp(ev);
                    x = this.mMotionX;
                    y = this.mMotionY;
                    motionPosition = pointToPosition(x, y);
                    if (motionPosition >= 0) {
                        this.mMotionViewOriginalTop = getChildAt(motionPosition - this
                                .mFirstPosition).getTop();
                        this.mMotionPosition = motionPosition;
                    }
                    this.mLastY = y;
                    break;
            }
            return true;
        } else if (isClickable() || isLongClickable()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction() & 255) {
            case 0:
                int touchMode = this.mTouchMode;
                int x = (int) ev.getX();
                int y = (int) ev.getY();
                this.mActivePointerId = ev.getPointerId(0);
                int motionPosition = findMotionRow(y);
                if (touchMode != 4 && motionPosition >= 0) {
                    this.mMotionViewOriginalTop = getChildAt(motionPosition - this
                            .mFirstPosition).getTop();
                    this.mMotionX = x;
                    this.mMotionY = y;
                    this.mMotionPosition = motionPosition;
                    this.mTouchMode = 0;
                    clearScrollingCache();
                }
                this.mLastY = Integer.MIN_VALUE;
                if (touchMode == 4) {
                    return true;
                }
                break;
            case 1:
                this.mTouchMode = -1;
                this.mActivePointerId = -1;
                reportScrollStateChange(0);
                break;
            case 2:
                switch (this.mTouchMode) {
                    case 0:
                        if (startScrollIfNeeded(((int) ev.getY(ev.findPointerIndex(this
                                .mActivePointerId))) - this.mMotionY)) {
                            return true;
                        }
                        break;
                    default:
                        break;
                }
            case 6:
                onSecondaryPointerUp(ev);
                break;
        }
        return false;
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        int pointerIndex = (ev.getAction() & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8;
        if (ev.getPointerId(pointerIndex) == this.mActivePointerId) {
            int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            this.mMotionX = (int) ev.getX(newPointerIndex);
            this.mMotionY = (int) ev.getY(newPointerIndex);
            this.mActivePointerId = ev.getPointerId(newPointerIndex);
            if (this.mVelocityTracker != null) {
                this.mVelocityTracker.clear();
            }
        }
    }

    public void addTouchables(ArrayList<View> views) {
        int count = getChildCount();
        int firstPosition = this.mFirstPosition;
        ListAdapter adapter = this.mAdapter;
        if (adapter != null) {
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (adapter.isEnabled(firstPosition + i)) {
                    views.add(child);
                }
                child.addTouchables(views);
            }
        }
    }

    void reportScrollStateChange(int newState) {
        if (newState != this.mLastScrollState && this.mOnScrollListener != null) {
            this.mOnScrollListener.onScrollStateChanged(this, newState);
            this.mLastScrollState = newState;
        }
    }

    public void smoothScrollToPosition(int position) {
        if (this.mPositionScroller == null) {
            this.mPositionScroller = new PositionScroller(this);
        }
        this.mPositionScroller.start(position);
    }

    public void smoothScrollToPosition(int position, int boundPosition) {
        if (this.mPositionScroller == null) {
            this.mPositionScroller = new PositionScroller(this);
        }
        this.mPositionScroller.start(position, boundPosition);
    }

    public void smoothScrollBy(int distance, int duration) {
        if (this.mFlingRunnable == null) {
            this.mFlingRunnable = new FlingRunnable(this);
        } else {
            FlingRunnable.access$000(this.mFlingRunnable);
        }
        this.mFlingRunnable.startScroll(distance, duration);
    }

    private void createScrollingCache() {
        if (this.mScrollingCacheEnabled && !this.mCachingStarted) {
            setChildrenDrawnWithCacheEnabled(true);
            setChildrenDrawingCacheEnabled(true);
            this.mCachingStarted = true;
        }
    }

    private void clearScrollingCache() {
        if (this.mClearScrollingCache == null) {
            this.mClearScrollingCache = new 2 (this);
        }
        post(this.mClearScrollingCache);
    }

    boolean trackMotionScroll(int deltaY, int incrementalDeltaY) {
        int childCount = getChildCount();
        if (childCount == 0) {
            return true;
        }
        int firstTop = getScrollChildTop();
        int lastBottom = getScrollChildBottom();
        Rect listPadding = this.mListPadding;
        int end = getHeight() - listPadding.bottom;
        int spaceAbove = listPadding.top - getFillChildTop();
        int spaceBelow = getFillChildBottom() - end;
        int height = (getHeight() - getPaddingBottom()) - getPaddingTop();
        if (deltaY < 0) {
            deltaY = Math.max(-(height - 1), deltaY);
        } else {
            deltaY = Math.min(height - 1, deltaY);
        }
        if (incrementalDeltaY < 0) {
            incrementalDeltaY = Math.max((-(height - 1)) / 2, incrementalDeltaY);
        } else {
            incrementalDeltaY = Math.min((height - 1) / 2, incrementalDeltaY);
        }
        int firstPosition = this.mFirstPosition;
        if (firstPosition == 0 && firstTop >= listPadding.top && deltaY >= 0) {
            return true;
        }
        if (firstPosition + childCount == this.mItemCount && lastBottom <= end && deltaY <= 0) {
            return true;
        }
        boolean down = incrementalDeltaY < 0;
        int headerViewsCount = getHeaderViewsCount();
        int footerViewsStart = this.mItemCount - getFooterViewsCount();
        int start = 0;
        int count = 0;
        int i;
        View child;
        int position;
        if (!down) {
            int bottom = (getHeight() - listPadding.bottom) - incrementalDeltaY;
            for (i = childCount - 1; i >= 0; i--) {
                child = getChildAt(i);
                if (child.getTop() <= bottom) {
                    break;
                }
                start = i;
                count++;
                position = firstPosition + i;
                if (position >= headerViewsCount && position < footerViewsStart) {
                    this.mRecycler.addScrapView(child);
                }
            }
        } else {
            int top = listPadding.top - incrementalDeltaY;
            for (i = 0; i < childCount; i++) {
                child = getChildAt(i);
                if (child.getBottom() >= top) {
                    break;
                }
                count++;
                position = firstPosition + i;
                if (position >= headerViewsCount && position < footerViewsStart) {
                    this.mRecycler.addScrapView(child);
                }
            }
        }
        this.mMotionViewNewTop = this.mMotionViewOriginalTop + deltaY;
        this.mBlockLayoutRequests = true;
        if (count > 0) {
            detachViewsFromParent(start, count);
        }
        tryOffsetChildrenTopAndBottom(incrementalDeltaY);
        if (down) {
            this.mFirstPosition += count;
        }
        invalidate();
        int absIncrementalDeltaY = Math.abs(incrementalDeltaY);
        if (spaceAbove < absIncrementalDeltaY || spaceBelow < absIncrementalDeltaY) {
            fillGap(down);
        }
        this.mBlockLayoutRequests = false;
        invokeOnItemScrollListener();
        awakenScrollBars();
        return false;
    }

    protected void tryOffsetChildrenTopAndBottom(int offset) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).offsetTopAndBottom(offset);
        }
    }

    int getHeaderViewsCount() {
        return 0;
    }

    int getFooterViewsCount() {
        return 0;
    }

    int reconcileSelectedPosition() {
        int position = this.mSelectedPosition;
        if (position < 0) {
            position = this.mResurrectToPosition;
        }
        return Math.min(Math.max(0, position), this.mItemCount - 1);
    }

    int findClosestMotionRow(int y) {
        int childCount = getChildCount();
        if (childCount == 0) {
            return -1;
        }
        int motionRow = findMotionRow(y);
        return motionRow == -1 ? (this.mFirstPosition + childCount) - 1 : motionRow;
    }

    public void invalidateViews() {
        DebugUtil.LogDebug("data changed by invalidateViews()");
        this.mDataChanged = true;
        rememberSyncState();
        requestLayout();
        invalidate();
    }

    protected void handleDataChanged() {
        int i = 3;
        int count = this.mItemCount;
        if (count > 0) {
            int newPos;
            if (this.mNeedSync) {
                this.mNeedSync = false;
                this.mPendingSync = null;
                if (this.mTranscriptMode == 2 || (this.mTranscriptMode == 1 && this
                        .mFirstPosition + getChildCount() >= this.mOldItemCount)) {
                    this.mLayoutMode = 3;
                    return;
                }
                switch (this.mSyncMode) {
                    case 0:
                        if (isInTouchMode()) {
                            this.mLayoutMode = 5;
                            this.mSyncPosition = Math.min(Math.max(0, this.mSyncPosition), count
                                    - 1);
                            return;
                        }
                        newPos = findSyncPosition();
                        if (newPos >= 0 && lookForSelectablePosition(newPos, true) == newPos) {
                            this.mSyncPosition = newPos;
                            if (this.mSyncHeight == ((long) getHeight())) {
                                this.mLayoutMode = 5;
                                return;
                            } else {
                                this.mLayoutMode = 2;
                                return;
                            }
                        }
                    case 1:
                        this.mLayoutMode = 5;
                        this.mSyncPosition = Math.min(Math.max(0, this.mSyncPosition), count - 1);
                        return;
                }
            }
            if (!isInTouchMode()) {
                newPos = getSelectedItemPosition();
                if (newPos >= count) {
                    newPos = count - 1;
                }
                if (newPos < 0) {
                    newPos = 0;
                }
                if (lookForSelectablePosition(newPos, true) >= 0 || lookForSelectablePosition
                        (newPos, false) >= 0) {
                    return;
                }
            } else if (this.mResurrectToPosition >= 0) {
                return;
            }
        }
        if (!this.mStackFromBottom) {
            i = 1;
        }
        this.mLayoutMode = i;
        this.mSelectedPosition = -1;
        this.mSelectedRowId = Long.MIN_VALUE;
        this.mNeedSync = false;
        this.mPendingSync = null;
        checkSelectionChanged();
    }

    protected void onLayoutSync(int syncPosition) {
    }

    protected void onLayoutSyncFinished(int syncPosition) {
    }

    static int getDistance(Rect source, Rect dest, int direction) {
        int sX;
        int sY;
        int dX;
        int dY;
        switch (direction) {
            case 17:
                sX = source.left;
                sY = source.top + (source.height() / 2);
                dX = dest.right;
                dY = dest.top + (dest.height() / 2);
                break;
            case 33:
                sX = source.left + (source.width() / 2);
                sY = source.top;
                dX = dest.left + (dest.width() / 2);
                dY = dest.bottom;
                break;
            case 66:
                sX = source.right;
                sY = source.top + (source.height() / 2);
                dX = dest.left;
                dY = dest.top + (dest.height() / 2);
                break;
            case 130:
                sX = source.left + (source.width() / 2);
                sY = source.bottom;
                dX = dest.left + (dest.width() / 2);
                dY = dest.top;
                break;
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, " +
                        "FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
        int deltaX = dX - sX;
        int deltaY = dY - sY;
        return (deltaY * deltaY) + (deltaX * deltaX);
    }

    public void onGlobalLayout() {
    }

    protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup
                                                                               .LayoutParams p) {
        return new LayoutParams(p);
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    public void setTranscriptMode(int mode) {
        this.mTranscriptMode = mode;
    }

    public int getTranscriptMode() {
        return this.mTranscriptMode;
    }

    public int getSolidColor() {
        return this.mCacheColorHint;
    }

    public void setCacheColorHint(int color) {
        if (color != this.mCacheColorHint) {
            this.mCacheColorHint = color;
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                getChildAt(i).setDrawingCacheBackgroundColor(color);
            }
            this.mRecycler.setCacheColorHint(color);
        }
    }

    public int getCacheColorHint() {
        return this.mCacheColorHint;
    }

    public void reclaimViews(List<View> views) {
        int childCount = getChildCount();
        RecyclerListener listener = RecycleBin.access$800(this.mRecycler);
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if (lp != null && this.mRecycler.shouldRecycleViewType(lp.viewType)) {
                views.add(child);
                if (listener != null) {
                    listener.onMovedToScrapHeap(child);
                }
            }
        }
        this.mRecycler.reclaimScrapViews(views);
        removeAllViewsInLayout();
    }

    public void setRecyclerListener(RecyclerListener listener) {
        RecycleBin.access$802(this.mRecycler, listener);
    }

    private void dispatchFinishTemporaryDetach(View v) {
        if (v != null) {
            v.onFinishTemporaryDetach();
            if (v instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) v;
                int count = group.getChildCount();
                for (int i = 0; i < count; i++) {
                    dispatchFinishTemporaryDetach(group.getChildAt(i));
                }
            }
        }
    }

    protected int modifyFlingInitialVelocity(int initialVelocity) {
        return initialVelocity;
    }

    protected int getScrollChildTop() {
        if (getChildCount() == 0) {
            return 0;
        }
        return getChildAt(0).getTop();
    }

    protected int getFirstChildTop() {
        if (getChildCount() == 0) {
            return 0;
        }
        return getChildAt(0).getTop();
    }

    protected int getFillChildTop() {
        if (getChildCount() == 0) {
            return 0;
        }
        return getChildAt(0).getTop();
    }

    protected int getFillChildBottom() {
        int count = getChildCount();
        if (count == 0) {
            return 0;
        }
        return getChildAt(count - 1).getBottom();
    }

    protected int getScrollChildBottom() {
        int count = getChildCount();
        if (count == 0) {
            return 0;
        }
        return getChildAt(count - 1).getBottom();
    }

    public Parcelable onSaveInstanceState() {
        Bundle ss = new Bundle();
        ss.putParcelable("instanceState", super.onSaveInstanceState());
        if (this.mPendingSync != null) {
            ss.putLong("firstId", this.mPendingSync.firstId);
            ss.putInt("viewTop", this.mPendingSync.viewTop);
            ss.putIntArray("viewTops", this.mPendingSync.viewTops);
            ss.putInt(FriendShipActivity.FRIENDSHIP_POSITION, this.mPendingSync.position);
            ss.putInt("height", this.mPendingSync.height);
            ss.putInt("childCount", this.mPendingSync.childCount);
        } else {
            boolean haveChildren;
            ss.putInt("height", getHeight());
            int childCount = getChildCount();
            ss.putInt("childCount", childCount);
            if (childCount <= 0 || this.mItemCount <= 0) {
                haveChildren = false;
            } else {
                haveChildren = true;
            }
            if (!haveChildren || this.mFirstPosition <= 0) {
                ss.putInt("viewTop", 0);
                ss.putLong("firstId", -1);
                ss.putInt(FriendShipActivity.FRIENDSHIP_POSITION, 0);
                ss.putIntArray("viewTops", new int[1]);
            } else {
                int firstPos = this.mFirstPosition;
                if (firstPos >= this.mItemCount) {
                    firstPos = this.mItemCount - 1;
                }
                ss.putInt(FriendShipActivity.FRIENDSHIP_POSITION, firstPos);
                ss.putLong("firstId", this.mAdapter.getItemId(firstPos));
                ss.putInt("viewTop", getChildAt(0).getTop());
                int[] viewTops = new int[childCount];
                for (int i = 0; i < childCount; i++) {
                    viewTops[i] = getChildAt(i).getTop();
                }
                ss.putIntArray("viewTops", viewTops);
            }
        }
        return ss;
    }

    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            this.mDataChanged = true;
            this.mSyncHeight = (long) bundle.getInt("height");
            long firstId = bundle.getLong("firstId");
            if (firstId >= 0) {
                this.mNeedSync = true;
                SavedState ss = new SavedState();
                ss.firstId = firstId;
                ss.height = (int) this.mSyncHeight;
                ss.position = bundle.getInt(FriendShipActivity.FRIENDSHIP_POSITION);
                ss.viewTop = bundle.getInt("viewTop");
                ss.childCount = bundle.getInt("childCount");
                ss.viewTops = bundle.getIntArray("viewTops");
                this.mPendingSync = ss;
                this.mSyncRowId = ss.firstId;
                this.mSyncPosition = ss.position;
                this.mSpecificTop = ss.viewTop;
                this.mSpecificTops = ss.viewTops;
            }
            state = bundle.getParcelable("instanceState");
        }
        super.onRestoreInstanceState(state);
        requestLayout();
    }
}
