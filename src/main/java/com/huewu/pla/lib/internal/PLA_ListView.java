package com.huewu.pla.lib.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ListAdapter;

import com.huewu.pla.R;
import com.huewu.pla.lib.DebugUtil;
import com.huewu.pla.lib.internal.PLA_AbsListView.LayoutParams;
import com.huewu.pla.lib.internal.PLA_AbsListView.RecycleBin;
import com.huewu.pla.lib.internal.PLA_AdapterView.AdapterDataSetObserver;

import java.util.ArrayList;
import java.util.Iterator;

public class PLA_ListView extends PLA_AbsListView {
    private static final float MAX_SCROLL_FACTOR = 0.33f;
    static final         int   NO_POSITION       = -1;
    private boolean mAreAllItemsSelectable;
    private boolean mClipDivider;
    private Paint   mContentPaint;
    Drawable mDivider;
    int      mDividerHeight;
    private boolean                  mDividerIsOpaque;
    private Paint                    mDividerPaint;
    private boolean                  mFooterDividersEnabled;
    private ArrayList<FixedViewInfo> mFooterViewInfos;
    private boolean                  mHeaderDividersEnabled;
    private ArrayList<FixedViewInfo> mHeaderViewInfos;
    private boolean                  mIsCacheColorOpaque;
    private boolean                  mItemsCanFocus;
    Drawable mOverScrollFooter;
    Drawable mOverScrollHeader;
    private final Rect mTempRect;

    public PLA_ListView(Context context) {
        this(context, null);
    }

    public PLA_ListView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.listViewStyle);
    }

    public PLA_ListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mHeaderViewInfos = new ArrayList();
        this.mFooterViewInfos = new ArrayList();
        this.mAreAllItemsSelectable = true;
        this.mItemsCanFocus = false;
        this.mTempRect = new Rect();
        this.mContentPaint = new Paint();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ListView, defStyle, 0);
        Drawable osHeader = a.getDrawable(R.styleable.ListView_overScrollHeader);
        if (osHeader != null) {
            setOverscrollHeader(osHeader);
        }
        Drawable osFooter = a.getDrawable(R.styleable.ListView_overScrollFooter);
        if (osFooter != null) {
            setOverscrollFooter(osFooter);
        }
        int dividerHeight = a.getDimensionPixelSize(R.styleable.ListView_dividerHeight, 0);
        if (dividerHeight != 0) {
            setDividerHeight(dividerHeight);
        }
        this.mHeaderDividersEnabled = a.getBoolean(R.styleable.ListView_headerDividersEnabled,
                true);
        this.mFooterDividersEnabled = a.getBoolean(R.styleable.ListView_footerDividersEnabled,
                true);
        if (a.hasValue(R.styleable.ListView_plaContentBackground)) {
            int contentBackgroundColor = a.getColor(R.styleable.ListView_plaContentBackground, 0);
            this.mContentPaint = new Paint();
            this.mContentPaint.setColor(contentBackgroundColor);
        }
        a.recycle();
    }

    public int getMaxScrollAmount() {
        return (int) (MAX_SCROLL_FACTOR * ((float) (getBottom() - getTop())));
    }

    private void adjustViewsUpOrDown() {
        int childCount = getChildCount();
        if (childCount > 0) {
            int delta;
            if (this.mStackFromBottom) {
                delta = getScrollChildBottom() - (getHeight() - this.mListPadding.bottom);
                if (this.mFirstPosition + childCount < this.mItemCount) {
                    delta += this.mDividerHeight;
                }
                if (delta > 0) {
                    delta = 0;
                }
            } else {
                delta = getScrollChildTop() - this.mListPadding.top;
                if (this.mFirstPosition != 0) {
                    delta -= this.mDividerHeight;
                }
                if (delta < 0) {
                    delta = 0;
                }
            }
            if (delta != 0) {
                tryOffsetChildrenTopAndBottom(-delta);
            }
        }
    }

    public void addHeaderView(View v, Object data, boolean isSelectable) {
        if (this.mAdapter != null) {
            throw new IllegalStateException("Cannot add header view to list -- setAdapter has " +
                    "already been called.");
        }
        FixedViewInfo info = new FixedViewInfo(this);
        info.view = v;
        info.data = data;
        info.isSelectable = isSelectable;
        this.mHeaderViewInfos.add(info);
    }

    public void addHeaderView(View v) {
        addHeaderView(v, null, true);
    }

    public int getHeaderViewsCount() {
        return this.mHeaderViewInfos.size();
    }

    public boolean isFixedView(View v) {
        int i;
        ArrayList<FixedViewInfo> where = this.mHeaderViewInfos;
        int len = where.size();
        for (i = 0; i < len; i++) {
            if (((FixedViewInfo) where.get(i)).view == v) {
                return true;
            }
        }
        where = this.mFooterViewInfos;
        len = where.size();
        for (i = 0; i < len; i++) {
            if (((FixedViewInfo) where.get(i)).view == v) {
                return true;
            }
        }
        return false;
    }

    public boolean removeHeaderView(View v) {
        if (this.mHeaderViewInfos.size() <= 0) {
            return false;
        }
        boolean result = false;
        if (this.mAdapter != null && ((PLA_HeaderViewListAdapter) this.mAdapter).removeHeader(v)) {
            this.mDataSetObserver.onChanged();
            result = true;
        }
        removeFixedViewInfo(v, this.mHeaderViewInfos);
        return result;
    }

    private void removeFixedViewInfo(View v, ArrayList<FixedViewInfo> where) {
        int len = where.size();
        for (int i = 0; i < len; i++) {
            if (((FixedViewInfo) where.get(i)).view == v) {
                where.remove(i);
                return;
            }
        }
    }

    public void addFooterView(View v, Object data, boolean isSelectable) {
        FixedViewInfo info = new FixedViewInfo(this);
        info.view = v;
        info.data = data;
        info.isSelectable = isSelectable;
        this.mFooterViewInfos.add(info);
        if (this.mDataSetObserver != null) {
            this.mDataSetObserver.onChanged();
        }
    }

    public void addFooterView(View v) {
        addFooterView(v, null, true);
    }

    public int getFooterViewsCount() {
        return this.mFooterViewInfos.size();
    }

    public boolean removeFooterView(View v) {
        if (this.mFooterViewInfos.size() <= 0) {
            return false;
        }
        boolean result = false;
        if (this.mAdapter != null && ((PLA_HeaderViewListAdapter) this.mAdapter).removeFooter(v)) {
            this.mDataSetObserver.onChanged();
            result = true;
        }
        removeFixedViewInfo(v, this.mFooterViewInfos);
        return result;
    }

    public ListAdapter getAdapter() {
        return this.mAdapter;
    }

    public void setAdapter(ListAdapter adapter) {
        if (this.mAdapter != null) {
            this.mAdapter.unregisterDataSetObserver(this.mDataSetObserver);
        }
        resetList();
        this.mRecycler.clear();
        if (this.mHeaderViewInfos.size() > 0 || this.mFooterViewInfos.size() > 0) {
            this.mAdapter = new PLA_HeaderViewListAdapter(this.mHeaderViewInfos, this
                    .mFooterViewInfos, adapter);
        } else {
            this.mAdapter = adapter;
        }
        this.mOldSelectedPosition = -1;
        this.mOldSelectedRowId = Long.MIN_VALUE;
        if (this.mAdapter != null) {
            this.mAreAllItemsSelectable = this.mAdapter.areAllItemsEnabled();
            this.mOldItemCount = this.mItemCount;
            this.mItemCount = this.mAdapter.getCount();
            this.mDataSetObserver = new AdapterDataSetObserver(this);
            this.mAdapter.registerDataSetObserver(this.mDataSetObserver);
            this.mRecycler.setViewTypeCount(this.mAdapter.getViewTypeCount());
        } else {
            this.mAreAllItemsSelectable = true;
        }
        requestLayout();
    }

    public int getFirstVisiblePosition() {
        return Math.max(0, this.mFirstPosition - getHeaderViewsCount());
    }

    public int getLastVisiblePosition() {
        return Math.min((this.mFirstPosition + getChildCount()) - 1, this.mAdapter.getCount() - 1);
    }

    void resetList() {
        clearRecycledState(this.mHeaderViewInfos);
        clearRecycledState(this.mFooterViewInfos);
        super.resetList();
        this.mLayoutMode = 0;
    }

    private void clearRecycledState(ArrayList<FixedViewInfo> infos) {
        if (infos != null) {
            int count = infos.size();
            for (int i = 0; i < count; i++) {
                LayoutParams p = (LayoutParams) ((FixedViewInfo) infos.get(i)).view
                        .getLayoutParams();
                if (p != null) {
                    p.recycledHeaderFooter = false;
                }
            }
        }
    }

    private boolean showingTopFadingEdge() {
        int listTop = getScrollY() + this.mListPadding.top;
        if (this.mFirstPosition > 0 || getChildAt(0).getTop() > listTop) {
            return true;
        }
        return false;
    }

    private boolean showingBottomFadingEdge() {
        int childCount = getChildCount();
        return (this.mFirstPosition + childCount) + -1 < this.mItemCount + -1 || getChildAt
                (childCount - 1).getBottom() < (getScrollY() + getHeight()) - this.mListPadding
                .bottom;
    }

    public boolean requestChildRectangleOnScreen(View child, Rect rect, boolean immediate) {
        int rectTopWithinChild = rect.top;
        rect.offset(child.getLeft(), child.getTop());
        rect.offset(-child.getScrollX(), -child.getScrollY());
        int height = getHeight();
        int listUnfadedTop = getScrollY();
        int listUnfadedBottom = listUnfadedTop + height;
        int fadingEdge = getVerticalFadingEdgeLength();
        if (showingTopFadingEdge() && rectTopWithinChild > fadingEdge) {
            listUnfadedTop += fadingEdge;
        }
        int bottomOfBottomChild = getChildAt(getChildCount() - 1).getBottom();
        if (showingBottomFadingEdge() && rect.bottom < bottomOfBottomChild - fadingEdge) {
            listUnfadedBottom -= fadingEdge;
        }
        int scrollYDelta = 0;
        if (rect.bottom > listUnfadedBottom && rect.top > listUnfadedTop) {
            if (rect.height() > height) {
                scrollYDelta = 0 + (rect.top - listUnfadedTop);
            } else {
                scrollYDelta = 0 + (rect.bottom - listUnfadedBottom);
            }
            scrollYDelta = Math.min(scrollYDelta, bottomOfBottomChild - listUnfadedBottom);
        } else if (rect.top < listUnfadedTop && rect.bottom < listUnfadedBottom) {
            if (rect.height() > height) {
                scrollYDelta = 0 - (listUnfadedBottom - rect.bottom);
            } else {
                scrollYDelta = 0 - (listUnfadedTop - rect.top);
            }
            scrollYDelta = Math.max(scrollYDelta, getChildAt(0).getTop() - listUnfadedTop);
        }
        boolean scroll = scrollYDelta != 0;
        if (scroll) {
            scrollListItemsBy(-scrollYDelta);
            positionSelector(child);
            this.mSelectedTop = child.getTop();
            invalidate();
        }
        return scroll;
    }

    protected int getItemLeft(int pos) {
        return this.mListPadding.left;
    }

    protected int getItemTop(int pos) {
        int count = getChildCount();
        return count > 0 ? getChildAt(count - 1).getBottom() + this.mDividerHeight :
                getListPaddingTop();
    }

    protected int getItemBottom(int pos) {
        return getChildCount() > 0 ? getChildAt(0).getTop() - this.mDividerHeight : getHeight() -
                getListPaddingBottom();
    }

    protected void fillGap(boolean down) {
        int count = getChildCount();
        if (down) {
            fillDown(this.mFirstPosition + count, getFillChildBottom() + this.mDividerHeight);
        } else {
            fillUp(this.mFirstPosition - 1, getFillChildTop());
        }
        onAdjustChildViews(down);
    }

    private View fillDown(int pos, int nextTop) {
        DebugUtil.i("fill down: " + pos);
        int end = (getBottom() - getTop()) - this.mListPadding.bottom;
        while (nextTop < end && pos < this.mItemCount) {
            makeAndAddView(pos, getItemTop(pos), true, false);
            pos++;
            nextTop = getFillChildBottom() + this.mDividerHeight;
        }
        return null;
    }

    private View fillUp(int pos, int nextBottom) {
        DebugUtil.i("fill up: " + pos);
        int end = this.mListPadding.top;
        while (nextBottom > end && pos >= 0) {
            makeAndAddView(pos, getItemBottom(pos), false, false);
            pos--;
            nextBottom = getItemBottom(pos);
        }
        this.mFirstPosition = pos + 1;
        return null;
    }

    private View fillFromTop(int nextTop) {
        this.mFirstPosition = Math.min(this.mFirstPosition, -1);
        this.mFirstPosition = Math.min(this.mFirstPosition, this.mItemCount - 1);
        if (this.mFirstPosition < 0) {
            this.mFirstPosition = 0;
        }
        return fillDown(this.mFirstPosition, nextTop);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int childWidth = 0;
        int childHeight = 0;
        this.mItemCount = this.mAdapter == null ? 0 : this.mAdapter.getCount();
        if (this.mItemCount > 0 && (widthMode == 0 || heightMode == 0)) {
            View child = obtainView(0, this.mIsScrap);
            measureScrapChild(child, 0, widthMeasureSpec);
            childWidth = child.getMeasuredWidth();
            childHeight = child.getMeasuredHeight();
            if (recycleOnMeasure() && this.mRecycler.shouldRecycleViewType(((LayoutParams) child
                    .getLayoutParams()).viewType)) {
                this.mRecycler.addScrapView(child);
            }
        }
        if (widthMode == 0) {
            widthSize = ((this.mListPadding.left + this.mListPadding.right) + childWidth) +
                    getVerticalScrollbarWidth();
        }
        if (heightMode == 0) {
            heightSize = ((this.mListPadding.top + this.mListPadding.bottom) + childHeight) +
                    (getVerticalFadingEdgeLength() * 2);
        }
        if (heightMode == Integer.MIN_VALUE) {
            heightSize = measureHeightOfChildren(widthMeasureSpec, 0, -1, heightSize, -1);
        }
        setMeasuredDimension(widthSize, heightSize);
        this.mWidthMeasureSpec = widthMeasureSpec;
    }

    private void measureScrapChild(View child, int position, int widthMeasureSpec) {
        int childHeightSpec;
        LayoutParams p = (LayoutParams) child.getLayoutParams();
        if (p == null) {
            p = new LayoutParams(-1, -2, 0);
            child.setLayoutParams(p);
        }
        p.viewType = this.mAdapter.getItemViewType(position);
        p.forceAdd = true;
        int childWidthSpec = ViewGroup.getChildMeasureSpec(widthMeasureSpec, this.mListPadding
                .left + this.mListPadding.right, p.width);
        int lpHeight = p.height;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, 1073741824);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, 0);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    @ExportedProperty(category = "list")
    protected boolean recycleOnMeasure() {
        return true;
    }

    final int measureHeightOfChildren(int widthMeasureSpec, int startPosition, int endPosition,
                                      int maxHeight, int disallowPartialChildPosition) {
        ListAdapter adapter = this.mAdapter;
        if (adapter == null) {
            return this.mListPadding.top + this.mListPadding.bottom;
        }
        int returnedHeight = this.mListPadding.top + this.mListPadding.bottom;
        int dividerHeight = (this.mDividerHeight <= 0 || this.mDivider == null) ? 0 : this
                .mDividerHeight;
        int prevHeightWithoutPartialChild = 0;
        if (endPosition == -1) {
            endPosition = adapter.getCount() - 1;
        }
        RecycleBin recycleBin = this.mRecycler;
        boolean recyle = recycleOnMeasure();
        boolean[] isScrap = this.mIsScrap;
        int i = startPosition;
        while (i <= endPosition) {
            DebugUtil.i("measureHeightOfChildren:" + i);
            View child = obtainView(i, isScrap);
            measureScrapChild(child, i, widthMeasureSpec);
            if (i > 0) {
                returnedHeight += dividerHeight;
            }
            if (recyle && recycleBin.shouldRecycleViewType(((LayoutParams) child.getLayoutParams
                    ()).viewType)) {
                DebugUtil.i("measureHeightOfChildren");
                recycleBin.addScrapView(child);
            }
            returnedHeight += child.getMeasuredHeight();
            if (returnedHeight < maxHeight) {
                if (disallowPartialChildPosition >= 0 && i >= disallowPartialChildPosition) {
                    prevHeightWithoutPartialChild = returnedHeight;
                }
                i++;
            } else if (disallowPartialChildPosition < 0 || i <= disallowPartialChildPosition ||
                    prevHeightWithoutPartialChild <= 0 || returnedHeight == maxHeight) {
                return maxHeight;
            } else {
                return prevHeightWithoutPartialChild;
            }
        }
        return returnedHeight;
    }

    int findMotionRow(int y) {
        int childCount = getChildCount();
        if (childCount > 0) {
            int i;
            if (this.mStackFromBottom) {
                for (i = childCount - 1; i >= 0; i--) {
                    if (y >= getChildAt(i).getTop()) {
                        return this.mFirstPosition + i;
                    }
                }
            } else {
                for (i = 0; i < childCount; i++) {
                    if (y <= getChildAt(i).getBottom()) {
                        return this.mFirstPosition + i;
                    }
                }
            }
        }
        return -1;
    }

    private View fillSpecific(int position, int top) {
        if (this.mDataChanged) {
            DebugUtil.i("fill specific: " + position + ":" + top);
        }
        View temp = makeAndAddView(position, top, true, false);
        this.mFirstPosition = position;
        int dividerHeight = this.mDividerHeight;
        int childCount;
        if (this.mStackFromBottom) {
            fillDown(position + 1, temp.getBottom() + dividerHeight);
            adjustViewsUpOrDown();
            fillUp(position - 1, getFillChildTop());
            childCount = getChildCount();
            if (childCount > 0) {
                correctTooLow(childCount);
            }
        } else {
            fillUp(position - 1, getFillChildTop());
            adjustViewsUpOrDown();
            fillDown(position + 1, getFillChildBottom() + this.mDividerHeight);
            childCount = getChildCount();
            if (childCount > 0) {
                correctTooHigh(childCount);
            }
        }
        return null;
    }

    private void correctTooHigh(int childCount) {
        if ((this.mFirstPosition + childCount) - 1 == this.mItemCount - 1 && childCount > 0) {
            int bottomOffset = ((getBottom() - getTop()) - this.mListPadding.bottom) -
                    getScrollChildBottom();
            int firstTop = getScrollChildTop();
            if (bottomOffset <= 0) {
                return;
            }
            if (this.mFirstPosition > 0 || firstTop < this.mListPadding.top) {
                if (this.mFirstPosition == 0) {
                    bottomOffset = Math.min(bottomOffset, this.mListPadding.top - firstTop);
                }
                tryOffsetChildrenTopAndBottom(bottomOffset);
                if (this.mFirstPosition > 0) {
                    int newFirstTop = getScrollChildTop();
                    fillUp(this.mFirstPosition - 1, getFillChildTop());
                    adjustViewsUpOrDown();
                }
            }
        }
    }

    private void correctTooLow(int childCount) {
        if (this.mFirstPosition == 0 && childCount > 0) {
            int end = (getBottom() - getTop()) - this.mListPadding.bottom;
            int topOffset = getScrollChildTop() - this.mListPadding.top;
            int lastBottom = getScrollChildBottom();
            int lastPosition = (this.mFirstPosition + childCount) - 1;
            if (topOffset <= 0) {
                return;
            }
            if (lastPosition < this.mItemCount - 1 || lastBottom > end) {
                if (lastPosition == this.mItemCount - 1) {
                    topOffset = Math.min(topOffset, lastBottom - end);
                }
                tryOffsetChildrenTopAndBottom(-topOffset);
                if (lastPosition < this.mItemCount - 1) {
                    fillDown(lastPosition + 1, getFillChildBottom() + this.mDividerHeight);
                    adjustViewsUpOrDown();
                }
            } else if (lastPosition == this.mItemCount - 1) {
                adjustViewsUpOrDown();
            }
        }
    }

    protected void layoutChildren() {
        boolean blockLayoutRequests = this.mBlockLayoutRequests;
        if (!blockLayoutRequests) {
            this.mBlockLayoutRequests = true;
            try {
                invalidate();
                if (this.mAdapter == null) {
                    resetList();
                    invokeOnItemScrollListener();
                    return;
                }
                int childrenTop = getFillChildBottom() + this.mDividerHeight;
                int childrenBottom = getFillChildTop();
                int childCount = getChildCount();
                View oldFirst = null;
                switch (this.mLayoutMode) {
                    case 1:
                    case 3:
                    case 4:
                    case 5:
                        break;
                    default:
                        oldFirst = getChildAt(0);
                        break;
                }
                boolean dataChanged = this.mDataChanged;
                if (dataChanged) {
                    handleDataChanged();
                }
                if (this.mItemCount == 0) {
                    resetList();
                    invokeOnItemScrollListener();
                    if (!blockLayoutRequests) {
                        this.mBlockLayoutRequests = false;
                    }
                } else if (this.mItemCount != this.mAdapter.getCount()) {
                    throw new IllegalStateException("The content of the adapter has changed but " +
                            "ListView did not receive a notification. Make sure the content of " +
                            "your adapter is not modified from a background thread, but only from" +
                            " the UI thread. [in ListView(" + getId() + ", " + getClass() + ") " +
                            "with Adapter(" + this.mAdapter.getClass() + ")]");
                } else {
                    int firstPosition = this.mFirstPosition;
                    RecycleBin recycleBin = this.mRecycler;
                    if (dataChanged) {
                        for (int i = childCount - 1; i >= 0; i--) {
                            recycleBin.addScrapView(getChildAt(i));
                        }
                    } else {
                        recycleBin.fillActiveViews(childCount, firstPosition);
                    }
                    switch (this.mLayoutMode) {
                        case 1:
                            detachAllViewsFromParent();
                            this.mFirstPosition = 0;
                            fillFromTop(childrenTop);
                            adjustViewsUpOrDown();
                            break;
                        case 3:
                            detachAllViewsFromParent();
                            fillUp(this.mItemCount - 1, childrenBottom);
                            adjustViewsUpOrDown();
                            break;
                        case 4:
                            detachAllViewsFromParent();
                            fillSpecific(reconcileSelectedPosition(), this.mSpecificTop);
                            break;
                        case 5:
                            onLayoutSync(this.mSyncPosition);
                            detachAllViewsFromParent();
                            if (this.mSpecificTops != null) {
                                fillSynced(this.mSyncPosition, this.mSpecificTops);
                                this.mSpecificTops = null;
                            } else {
                                fillSpecific(this.mSyncPosition, this.mSpecificTop);
                            }
                            onLayoutSyncFinished(this.mSyncPosition);
                            break;
                        default:
                            if (childCount != 0) {
                                if (this.mFirstPosition >= this.mItemCount) {
                                    onLayoutSync(0);
                                    detachAllViewsFromParent();
                                    fillSpecific(0, childrenTop);
                                    onLayoutSyncFinished(0);
                                    break;
                                }
                                onLayoutSync(this.mFirstPosition);
                                detachAllViewsFromParent();
                                int i2 = this.mFirstPosition;
                                if (oldFirst != null) {
                                    childrenTop = oldFirst.getTop();
                                }
                                fillSpecific(i2, childrenTop);
                                onLayoutSyncFinished(this.mFirstPosition);
                                break;
                            }
                            detachAllViewsFromParent();
                            if (!this.mStackFromBottom) {
                                fillFromTop(childrenTop);
                                break;
                            } else {
                                fillUp(this.mItemCount - 1, childrenBottom);
                                break;
                            }
                    }
                    recycleBin.scrapActiveViews();
                    if (this.mTouchMode <= 0 || this.mTouchMode >= 3) {
                        this.mSelectedTop = 0;
                        this.mSelectorRect.setEmpty();
                    } else {
                        View child = getChildAt(this.mMotionPosition - this.mFirstPosition);
                        if (child != null) {
                            positionSelector(child);
                        }
                    }
                    this.mLayoutMode = 0;
                    this.mDataChanged = false;
                    this.mNeedSync = false;
                    if (this.mItemCount > 0) {
                        checkSelectionChanged();
                    }
                    invokeOnItemScrollListener();
                    if (!blockLayoutRequests) {
                        this.mBlockLayoutRequests = false;
                    }
                }
            } finally {
                if (!blockLayoutRequests) {
                    this.mBlockLayoutRequests = false;
                }
            }
        }
    }

    private void fillSynced(int position, int[] specificTops) {
        for (int i = 0; i < specificTops.length; i++) {
            makeAndAddView(position + i, specificTops[i], true, false);
            adjustViewsUpOrDown();
        }
        this.mFirstPosition = position;
    }

    private View makeAndAddView(int position, int childrenBottomOrTop, boolean flow, boolean
            selected) {
        View child;
        if (!this.mDataChanged) {
            child = this.mRecycler.getActiveView(position);
            if (child != null) {
                setupChild(child, position, childrenBottomOrTop, flow, getItemLeft(position),
                        selected, true);
                return child;
            }
        }
        onItemAddedToList(position, flow);
        int childrenLeft = getItemLeft(position);
        DebugUtil.i("makeAndAddView:" + position);
        child = obtainView(position, this.mIsScrap);
        setupChild(child, position, childrenBottomOrTop, flow, childrenLeft, selected, this
                .mIsScrap[0]);
        return child;
    }

    protected void onItemAddedToList(int position, boolean flow) {
    }

    private void setupChild(View child, int position, int y, boolean flowDown, int childrenLeft,
                            boolean selected, boolean recycled) {
        boolean isSelected = selected && shouldShowSelector();
        boolean updateChildSelected = isSelected != child.isSelected();
        int mode = this.mTouchMode;
        boolean isPressed = mode > 0 && mode < 3 && this.mMotionPosition == position;
        boolean updateChildPressed = isPressed != child.isPressed();
        boolean needToMeasure = !recycled || updateChildSelected || child.isLayoutRequested();
        ViewGroup.LayoutParams p = (LayoutParams) child.getLayoutParams();
        if (p == null) {
            ViewGroup.LayoutParams layoutParams = new LayoutParams(-1, -2, 0);
        }
        p.viewType = this.mAdapter.getItemViewType(position);
        p.scrappedFromPosition = position;
        if ((!recycled || p.forceAdd) && !(p.recycledHeaderFooter && p.viewType == -2)) {
            p.forceAdd = false;
            if (p.viewType == -2) {
                p.recycledHeaderFooter = true;
            }
            addViewInLayout(child, flowDown ? -1 : 0, p, true);
        } else {
            attachViewToParent(child, flowDown ? -1 : 0, p);
        }
        if (updateChildSelected) {
            child.setSelected(isSelected);
        }
        if (updateChildPressed) {
            child.setPressed(isPressed);
        }
        if (needToMeasure) {
            int childHeightSpec;
            int childWidthSpec = ViewGroup.getChildMeasureSpec(this.mWidthMeasureSpec, this
                    .mListPadding.left + this.mListPadding.right, p.width);
            int lpHeight = p.height;
            if (lpHeight > 0) {
                childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, 1073741824);
            } else {
                childHeightSpec = MeasureSpec.makeMeasureSpec(0, 0);
            }
            onMeasureChild(child, position, childWidthSpec, childHeightSpec);
        } else {
            cleanupLayoutState(child);
        }
        int w = child.getMeasuredWidth();
        int h = child.getMeasuredHeight();
        int childTop = flowDown ? y : y - h;
        if (needToMeasure) {
            onLayoutChild(child, position, childrenLeft, childTop, childrenLeft + w, childTop + h);
        } else {
            onOffsetChild(child, position, childrenLeft - child.getLeft(), childTop - child
                    .getTop());
        }
        if (this.mCachingStarted && !child.isDrawingCacheEnabled()) {
            child.setDrawingCacheEnabled(true);
        }
    }

    protected void onOffsetChild(View child, int position, int offsetLeft, int offsetTop) {
        child.offsetLeftAndRight(offsetLeft);
        child.offsetTopAndBottom(offsetTop);
    }

    protected void onLayoutChild(View child, int position, int l, int t, int r, int b) {
        child.layout(l, t, r, b);
    }

    protected void onMeasureChild(View child, int position, int widthMeasureSpec, int
            heightMeasureSpec) {
        child.measure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onAdjustChildViews(boolean down) {
        if (down) {
            correctTooHigh(getChildCount());
        } else {
            correctTooLow(getChildCount());
        }
    }

    protected boolean canAnimate() {
        return super.canAnimate() && this.mItemCount > 0;
    }

    public void setSelection(int position) {
    }

    public void setSelectionFromTop(int position, int y) {
        this.mLayoutMode = 4;
        this.mSpecificTop = this.mListPadding.top + y;
        if (this.mNeedSync) {
            this.mSyncPosition = position;
            this.mSyncRowId = this.mAdapter.getItemId(position);
        }
        if (this.mPositionScroller != null) {
            this.mPositionScroller.stop();
        }
        requestLayout();
    }

    int lookForSelectablePosition(int position, boolean lookDown) {
        ListAdapter adapter = this.mAdapter;
        if (adapter == null || isInTouchMode()) {
            return -1;
        }
        int count = adapter.getCount();
        if (!this.mAreAllItemsSelectable) {
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

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        boolean populated = super.dispatchPopulateAccessibilityEvent(event);
        if (!populated) {
            int itemCount = 0;
            int currentItemIndex = getSelectedItemPosition();
            ListAdapter adapter = getAdapter();
            if (adapter != null) {
                int count = adapter.getCount();
                if (count < 15) {
                    for (int i = 0; i < count; i++) {
                        if (adapter.isEnabled(i)) {
                            itemCount++;
                        } else if (i <= currentItemIndex) {
                            currentItemIndex--;
                        }
                    }
                } else {
                    itemCount = count;
                }
            }
            event.setItemCount(itemCount);
            event.setCurrentItemIndex(currentItemIndex);
        }
        return populated;
    }

    public boolean fullScroll(int direction) {
        boolean moved = false;
        if (direction == 33) {
            if (lookForSelectablePosition(0, true) >= 0) {
                this.mLayoutMode = 1;
                invokeOnItemScrollListener();
                moved = true;
            }
        } else if (direction == 130) {
            if (lookForSelectablePosition(this.mItemCount - 1, true) >= 0) {
                this.mLayoutMode = 3;
                invokeOnItemScrollListener();
            }
            moved = true;
        }
        if (moved && !awakenScrollBars()) {
            awakenScrollBars();
            invalidate();
        }
        return moved;
    }

    private void scrollListItemsBy(int amount) {
        tryOffsetChildrenTopAndBottom(amount);
        int listBottom = getHeight() - this.mListPadding.bottom;
        int listTop = this.mListPadding.top;
        RecycleBin recycleBin = this.mRecycler;
        View last;
        View first;
        if (amount < 0) {
            last = getLastChild();
            int numChildren = getChildCount();
            while (last.getBottom() < listBottom) {
                int lastVisiblePosition = (this.mFirstPosition + numChildren) - 1;
                if (lastVisiblePosition >= this.mItemCount - 1) {
                    break;
                }
                addViewBelow(last, lastVisiblePosition);
                last = getLastChild();
                numChildren++;
            }
            if (last.getBottom() < listBottom) {
                tryOffsetChildrenTopAndBottom(listBottom - last.getBottom());
            }
            first = getChildAt(0);
            while (first.getBottom() < listTop) {
                if (recycleBin.shouldRecycleViewType(((LayoutParams) first.getLayoutParams())
                        .viewType)) {
                    detachViewFromParent(first);
                    recycleBin.addScrapView(first);
                } else {
                    removeViewInLayout(first);
                }
                first = getChildAt(0);
                this.mFirstPosition++;
            }
            return;
        }
        first = getChildAt(0);
        while (first.getTop() > listTop && this.mFirstPosition > 0) {
            first = addViewAbove(first, this.mFirstPosition);
            this.mFirstPosition--;
        }
        if (first.getTop() > listTop) {
            tryOffsetChildrenTopAndBottom(listTop - first.getTop());
        }
        int lastIndex = getChildCount() - 1;
        last = getChildAt(lastIndex);
        while (last.getTop() > listBottom) {
            if (recycleBin.shouldRecycleViewType(((LayoutParams) last.getLayoutParams())
                    .viewType)) {
                detachViewFromParent(last);
                recycleBin.addScrapView(last);
            } else {
                removeViewInLayout(last);
            }
            lastIndex--;
            last = getChildAt(lastIndex);
        }
    }

    protected View getLastChild() {
        return getChildAt(getChildCount() - 1);
    }

    private View addViewAbove(View theView, int position) {
        int abovePosition = position - 1;
        DebugUtil.i("addViewAbove:" + position);
        View view = obtainView(abovePosition, this.mIsScrap);
        setupChild(view, abovePosition, theView.getTop() - this.mDividerHeight, false, this
                .mListPadding.left, false, this.mIsScrap[0]);
        return view;
    }

    private View addViewBelow(View theView, int position) {
        int belowPosition = position + 1;
        DebugUtil.i("addViewBelow:" + position);
        View view = obtainView(belowPosition, this.mIsScrap);
        setupChild(view, belowPosition, theView.getBottom() + this.mDividerHeight, true, this
                .mListPadding.left, false, this.mIsScrap[0]);
        return view;
    }

    public void setItemsCanFocus(boolean itemsCanFocus) {
        this.mItemsCanFocus = itemsCanFocus;
        if (!itemsCanFocus) {
            setDescendantFocusability(393216);
        }
    }

    public boolean getItemsCanFocus() {
        return this.mItemsCanFocus;
    }

    public boolean isOpaque() {
        return (this.mCachingStarted && this.mIsCacheColorOpaque && this.mDividerIsOpaque) ||
                super.isOpaque();
    }

    public void setCacheColorHint(int color) {
        boolean opaque = (color >>> 24) == 255;
        this.mIsCacheColorOpaque = opaque;
        if (opaque) {
            if (this.mDividerPaint == null) {
                this.mDividerPaint = new Paint();
            }
            this.mDividerPaint.setColor(color);
        }
        super.setCacheColorHint(color);
    }

    void drawOverscrollHeader(Canvas canvas, Drawable drawable, Rect bounds) {
        int height = drawable.getMinimumHeight();
        canvas.save();
        canvas.clipRect(bounds);
        if (bounds.bottom - bounds.top < height) {
            bounds.top = bounds.bottom - height;
        }
        drawable.setBounds(bounds);
        drawable.draw(canvas);
        canvas.restore();
    }

    void drawOverscrollFooter(Canvas canvas, Drawable drawable, Rect bounds) {
        int height = drawable.getMinimumHeight();
        canvas.save();
        canvas.clipRect(bounds);
        if (bounds.bottom - bounds.top < height) {
            bounds.bottom = bounds.top + height;
        }
        drawable.setBounds(bounds);
        drawable.draw(canvas);
        canvas.restore();
    }

    protected void dispatchDraw(Canvas canvas) {
        int dividerHeight = this.mDividerHeight;
        Drawable overscrollHeader = this.mOverScrollHeader;
        Drawable overscrollFooter = this.mOverScrollFooter;
        boolean drawOverscrollHeader = overscrollHeader != null;
        boolean drawOverscrollFooter = overscrollFooter != null;
        boolean drawDividers = dividerHeight > 0 && this.mDivider != null;
        drawContentBackground(canvas);
        if (drawDividers || drawOverscrollHeader || drawOverscrollFooter) {
            Rect bounds = this.mTempRect;
            bounds.left = getPaddingLeft();
            bounds.right = (getRight() - getLeft()) - getPaddingRight();
            int count = getChildCount();
            int headerCount = this.mHeaderViewInfos.size();
            int itemCount = this.mItemCount;
            int footerLimit = (itemCount - this.mFooterViewInfos.size()) - 1;
            boolean headerDividers = this.mHeaderDividersEnabled;
            boolean footerDividers = this.mFooterDividersEnabled;
            int first = this.mFirstPosition;
            boolean areAllItemsSelectable = this.mAreAllItemsSelectable;
            ListAdapter adapter = this.mAdapter;
            boolean fillForMissingDividers = drawDividers && isOpaque() && !super.isOpaque();
            if (fillForMissingDividers && this.mDividerPaint == null && this.mIsCacheColorOpaque) {
                this.mDividerPaint = new Paint();
                this.mDividerPaint.setColor(getCacheColorHint());
            }
            Paint paint = this.mDividerPaint;
            int listBottom = ((getBottom() - getTop()) - this.mListPadding.bottom) + getScrollY();
            int scrollY;
            int i;
            if (this.mStackFromBottom) {
                int listTop = this.mListPadding.top;
                scrollY = getScrollY();
                if (count > 0 && drawOverscrollHeader) {
                    bounds.top = scrollY;
                    bounds.bottom = getChildAt(0).getTop();
                    drawOverscrollHeader(canvas, overscrollHeader, bounds);
                }
                i = drawOverscrollHeader ? 1 : 0;
                while (i < count) {
                    if ((headerDividers || first + i >= headerCount) && (footerDividers || first
                            + i < footerLimit)) {
                        int top = getChildAt(i).getTop();
                        if (drawDividers && top > listTop) {
                            if (areAllItemsSelectable || (adapter.isEnabled(first + i) && (i ==
                                    count - 1 || adapter.isEnabled((first + i) + 1)))) {
                                bounds.top = top - dividerHeight;
                                bounds.bottom = top;
                                drawDivider(canvas, bounds, i - 1);
                            } else if (fillForMissingDividers) {
                                bounds.top = top - dividerHeight;
                                bounds.bottom = top;
                                canvas.drawRect(bounds, paint);
                            }
                        }
                    }
                    i++;
                }
                if (count > 0 && scrollY > 0) {
                    if (drawOverscrollFooter) {
                        int absListBottom = getBottom();
                        bounds.top = absListBottom;
                        bounds.bottom = absListBottom + scrollY;
                        drawOverscrollFooter(canvas, overscrollFooter, bounds);
                    } else if (drawDividers) {
                        bounds.top = listBottom;
                        bounds.bottom = listBottom + dividerHeight;
                        drawDivider(canvas, bounds, -1);
                    }
                }
            } else {
                int bottom = 0;
                scrollY = getScrollY();
                if (count > 0 && scrollY < 0) {
                    if (drawOverscrollHeader) {
                        bounds.bottom = 0;
                        bounds.top = scrollY;
                        drawOverscrollHeader(canvas, overscrollHeader, bounds);
                    } else if (drawDividers) {
                        bounds.bottom = 0;
                        bounds.top = -dividerHeight;
                        drawDivider(canvas, bounds, -1);
                    }
                }
                i = 0;
                while (i < count) {
                    if ((headerDividers || first + i >= headerCount) && (footerDividers || first
                            + i < footerLimit)) {
                        bottom = getChildAt(i).getBottom();
                        if (drawDividers && bottom < listBottom && !(drawOverscrollFooter && i ==
                                count - 1)) {
                            if (areAllItemsSelectable || (adapter.isEnabled(first + i) && (i ==
                                    count - 1 || adapter.isEnabled((first + i) + 1)))) {
                                bounds.top = bottom;
                                bounds.bottom = bottom + dividerHeight;
                                drawDivider(canvas, bounds, i);
                            } else if (fillForMissingDividers) {
                                bounds.top = bottom;
                                bounds.bottom = bottom + dividerHeight;
                                canvas.drawRect(bounds, paint);
                            }
                        }
                    }
                    i++;
                }
                int overFooterBottom = getBottom() + getScrollY();
                if (drawOverscrollFooter && first + count == itemCount && overFooterBottom >
                        bottom) {
                    bounds.top = bottom;
                    bounds.bottom = overFooterBottom;
                    drawOverscrollFooter(canvas, overscrollFooter, bounds);
                }
            }
        }
        super.dispatchDraw(canvas);
    }

    private void drawContentBackground(Canvas canvas) {
        if (getHeaderViewsCount() > 0) {
            Rect rect = this.mTempRect;
            rect.left = getLeft();
            rect.right = getRight();
            if (isHeaderView(getChildAt(getFirstVisiblePosition()))) {
                rect.top = ((FixedViewInfo) this.mHeaderViewInfos.get(this.mHeaderViewInfos.size
                        () - 1)).view.getBottom();
            } else {
                rect.top = 0;
            }
            rect.bottom = getBottom();
            canvas.drawRect(rect, this.mContentPaint);
        }
    }

    private boolean isHeaderView(View view) {
        Iterator it = this.mHeaderViewInfos.iterator();
        while (it.hasNext()) {
            if (((FixedViewInfo) it.next()).view == view) {
                return true;
            }
        }
        return false;
    }

    void drawDivider(Canvas canvas, Rect bounds, int childIndex) {
        Drawable divider = this.mDivider;
        boolean clipDivider = this.mClipDivider;
        if (clipDivider) {
            canvas.save();
            canvas.clipRect(bounds);
        } else {
            divider.setBounds(bounds);
        }
        divider.draw(canvas);
        if (clipDivider) {
            canvas.restore();
        }
    }

    public Drawable getDivider() {
        return this.mDivider;
    }

    public void setDivider(Drawable divider) {
        boolean z = false;
        if (divider != null) {
            this.mDividerHeight = divider.getIntrinsicHeight();
            this.mClipDivider = divider instanceof ColorDrawable;
        } else {
            this.mDividerHeight = 0;
            this.mClipDivider = false;
        }
        this.mDivider = divider;
        if (divider == null || divider.getOpacity() == -1) {
            z = true;
        }
        this.mDividerIsOpaque = z;
        requestLayoutIfNecessary();
    }

    public int getDividerHeight() {
        return this.mDividerHeight;
    }

    public void setDividerHeight(int height) {
        this.mDividerHeight = height;
        requestLayoutIfNecessary();
    }

    public void setHeaderDividersEnabled(boolean headerDividersEnabled) {
        this.mHeaderDividersEnabled = headerDividersEnabled;
        invalidate();
    }

    public void setFooterDividersEnabled(boolean footerDividersEnabled) {
        this.mFooterDividersEnabled = footerDividersEnabled;
        invalidate();
    }

    public void setOverscrollHeader(Drawable header) {
        this.mOverScrollHeader = header;
        if (getScrollY() < 0) {
            invalidate();
        }
    }

    public Drawable getOverscrollHeader() {
        return this.mOverScrollHeader;
    }

    public void setOverscrollFooter(Drawable footer) {
        this.mOverScrollFooter = footer;
        invalidate();
    }

    public Drawable getOverscrollFooter() {
        return this.mOverScrollFooter;
    }

    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        int closetChildIndex = -1;
        if (gainFocus && previouslyFocusedRect != null) {
            previouslyFocusedRect.offset(getScrollX(), getScrollY());
            ListAdapter adapter = this.mAdapter;
            if (adapter.getCount() < getChildCount() + this.mFirstPosition) {
                this.mLayoutMode = 0;
                layoutChildren();
            }
            Rect otherRect = this.mTempRect;
            int minDistance = ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
            int childCount = getChildCount();
            int firstPosition = this.mFirstPosition;
            for (int i = 0; i < childCount; i++) {
                if (adapter.isEnabled(firstPosition + i)) {
                    View other = getChildAt(i);
                    other.getDrawingRect(otherRect);
                    offsetDescendantRectToMyCoords(other, otherRect);
                    int distance = PLA_AbsListView.getDistance(previouslyFocusedRect, otherRect,
                            direction);
                    if (distance < minDistance) {
                        minDistance = distance;
                        closetChildIndex = i;
                    }
                }
            }
        }
        if (closetChildIndex >= 0) {
            setSelection(this.mFirstPosition + closetChildIndex);
        } else {
            requestLayout();
        }
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        int count = getChildCount();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                addHeaderView(getChildAt(i));
            }
            removeAllViews();
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (this.mItemsCanFocus && ev.getAction() == 0 && ev.getEdgeFlags() != 0) {
            return false;
        }
        return super.onTouchEvent(ev);
    }

    public boolean performItemClick(View view, int position, long id) {
        return false | super.performItemClick(view, position, id);
    }

    public void setItemChecked(int position, boolean value) {
    }

    public boolean isItemChecked(int position) {
        return false;
    }

    public int getCheckedItemPosition() {
        return -1;
    }

    public SparseBooleanArray getCheckedItemPositions() {
        return null;
    }

    @Deprecated
    public long[] getCheckItemIds() {
        if (this.mAdapter == null || !this.mAdapter.hasStableIds()) {
            return new long[0];
        }
        return getCheckedItemIds();
    }

    public long[] getCheckedItemIds() {
        return new long[0];
    }

    public void clearChoices() {
    }
}
