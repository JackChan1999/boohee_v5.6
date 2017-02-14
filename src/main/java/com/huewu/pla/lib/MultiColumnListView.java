package com.huewu.pla.lib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.ActivityChooserView.ActivityChooserViewAdapter;
import android.util.AttributeSet;
import android.view.View;

import com.huewu.pla.R;
import com.huewu.pla.lib.internal.PLA_AbsListView;
import com.huewu.pla.lib.internal.PLA_AbsListView$OnScrollListener;
import com.huewu.pla.lib.internal.PLA_ListView;

public class MultiColumnListView extends PLA_ListView {
    private static final int    DEFAULT_COLUMN_NUMBER = 2;
    private static final String TAG                   = "MultiColumnListView";
    OnLoadMoreListener loadMoreListener;
    private boolean                          loadingMoreComplete = true;
    private int                              mColumnNumber       = 2;
    private int                              mColumnPaddingLeft  = 0;
    private int                              mColumnPaddingRight = 0;
    private Column[]                         mColumns            = null;
    private Column                           mFixedColumn        = null;
    private Rect                             mFrameRect          = new Rect();
    private ParcelableSparseIntArray         mItems              = new ParcelableSparseIntArray();
    public  PLA_AbsListView$OnScrollListener scroller            = new
            PLA_AbsListView$OnScrollListener() {
        private static final int OFFSET = 2;
        private int visibleLastIndex = 0;

        public void onScrollStateChanged(PLA_AbsListView view, int scrollState) {
            int lastIndex = MultiColumnListView.this.getAdapter().getCount() - 2;
            if (scrollState == 0 && this.visibleLastIndex == lastIndex && MultiColumnListView
                    .this.loadingMoreComplete) {
                MultiColumnListView.this.loadMoreListener.onLoadMore();
                MultiColumnListView.this.loadingMoreComplete = false;
            }
        }

        public void onScroll(PLA_AbsListView view, int firstVisibleItem, int visibleItemCount,
                             int totalItemCount) {
            this.visibleLastIndex = (firstVisibleItem + visibleItemCount) - 2;
        }
    };

    private class Column {
        private int mColumnLeft;
        private int mColumnWidth;
        private int mIndex;
        private int mSynchedBottom = 0;
        private int mSynchedTop    = 0;

        public Column(int index) {
            this.mIndex = index;
        }

        public int getColumnLeft() {
            return this.mColumnLeft;
        }

        public int getColumnWidth() {
            return this.mColumnWidth;
        }

        public int getIndex() {
            return this.mIndex;
        }

        public int getBottom() {
            int bottom = Integer.MIN_VALUE;
            int childCount = MultiColumnListView.this.getChildCount();
            for (int index = 0; index < childCount; index++) {
                View v = MultiColumnListView.this.getChildAt(index);
                if (v.getLeft() == this.mColumnLeft || MultiColumnListView.this.isFixedView(v)) {
                    if (bottom < v.getBottom()) {
                        bottom = v.getBottom();
                    }
                }
            }
            if (bottom == Integer.MIN_VALUE) {
                return this.mSynchedBottom;
            }
            return bottom;
        }

        public void offsetTopAndBottom(int offset) {
            if (offset != 0) {
                int childCount = MultiColumnListView.this.getChildCount();
                for (int index = 0; index < childCount; index++) {
                    View v = MultiColumnListView.this.getChildAt(index);
                    if (v.getLeft() == this.mColumnLeft || MultiColumnListView.this.isFixedView
                            (v)) {
                        v.offsetTopAndBottom(offset);
                    }
                }
            }
        }

        public int getTop() {
            int top = ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
            int childCount = MultiColumnListView.this.getChildCount();
            for (int index = 0; index < childCount; index++) {
                View v = MultiColumnListView.this.getChildAt(index);
                if (v.getLeft() == this.mColumnLeft || MultiColumnListView.this.isFixedView(v)) {
                    top = Math.min(top, v.getTop());
                }
            }
            if (top == ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED) {
                return this.mSynchedTop;
            }
            return top;
        }

        public void save() {
            this.mSynchedTop = 0;
            this.mSynchedBottom = getTop();
        }

        public void clear() {
            this.mSynchedTop = 0;
            this.mSynchedBottom = 0;
        }
    }

    private class FixedColumn extends Column {
        public FixedColumn() {
            super(ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED);
        }

        public int getBottom() {
            return MultiColumnListView.this.getScrollChildBottom();
        }

        public int getTop() {
            return MultiColumnListView.this.getScrollChildTop();
        }
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public MultiColumnListView(Context context) {
        super(context);
        init(null);
    }

    public MultiColumnListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MultiColumnListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        getWindowVisibleDisplayFrame(this.mFrameRect);
        if (attrs == null) {
            this.mColumnNumber = 2;
        } else {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable
                    .MultiColumnListView);
            int landColNumber = a.getInteger(R.styleable
                    .MultiColumnListView_plaLandscapeColumnNumber, -1);
            int defColNumber = a.getInteger(R.styleable.MultiColumnListView_plaColumnNumber, -1);
            if (this.mFrameRect.width() > this.mFrameRect.height() && landColNumber != -1) {
                this.mColumnNumber = landColNumber;
            } else if (defColNumber != -1) {
                this.mColumnNumber = defColNumber;
            } else {
                this.mColumnNumber = 2;
            }
            this.mColumnPaddingLeft = a.getDimensionPixelSize(R.styleable
                    .MultiColumnListView_plaColumnPaddingLeft, 0);
            this.mColumnPaddingRight = a.getDimensionPixelSize(R.styleable
                    .MultiColumnListView_plaColumnPaddingRight, 0);
            a.recycle();
        }
        this.mColumns = new Column[this.mColumnNumber];
        for (int i = 0; i < this.mColumnNumber; i++) {
            this.mColumns[i] = new Column(i);
        }
        this.mFixedColumn = new FixedColumn();
    }

    public void setColumnPaddingLeft(int columnPaddingLeft) {
        this.mColumnPaddingLeft = columnPaddingLeft;
    }

    public void setColumnPaddingRight(int columnPaddingRight) {
        this.mColumnPaddingRight = columnPaddingRight;
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = ((((getMeasuredWidth() - this.mListPadding.left) - this.mListPadding.right) -
                this.mColumnPaddingLeft) - this.mColumnPaddingRight) / this.mColumnNumber;
        for (int index = 0; index < this.mColumnNumber; index++) {
            this.mColumns[index].mColumnWidth = width;
            this.mColumns[index].mColumnLeft = (this.mListPadding.left + this.mColumnPaddingLeft)
                    + (width * index);
        }
        this.mFixedColumn.mColumnLeft = this.mListPadding.left;
        this.mFixedColumn.mColumnWidth = getMeasuredWidth();
    }

    protected void onMeasureChild(View child, int position, int widthMeasureSpec, int
            heightMeasureSpec) {
        if (isFixedView(child)) {
            child.measure(widthMeasureSpec, heightMeasureSpec);
        } else {
            child.measure(1073741824 | getColumnWidth(position), heightMeasureSpec);
        }
    }

    protected int modifyFlingInitialVelocity(int initialVelocity) {
        return initialVelocity;
    }

    protected void onItemAddedToList(int position, boolean flow) {
        super.onItemAddedToList(position, flow);
        if (!isHeaderOrFooterPosition(position)) {
            this.mItems.append(position, getNextColumn(flow, position).getIndex());
        }
    }

    protected void onLayoutSync(int syncPos) {
        for (Column c : this.mColumns) {
            c.save();
        }
    }

    protected void onLayoutSyncFinished(int syncPos) {
        for (Column c : this.mColumns) {
            c.clear();
        }
    }

    protected void onAdjustChildViews(boolean down) {
        int i = 0;
        int firstItem = getFirstVisiblePosition();
        if (!down && firstItem == 0) {
            int firstColumnTop = this.mColumns[0].getTop();
            Column[] columnArr = this.mColumns;
            int length = columnArr.length;
            while (i < length) {
                Column c = columnArr[i];
                c.offsetTopAndBottom(firstColumnTop - c.getTop());
                i++;
            }
        }
        super.onAdjustChildViews(down);
    }

    protected int getFillChildBottom() {
        int result = ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
        for (Column c : this.mColumns) {
            int bottom = c.getBottom();
            if (result > bottom) {
                result = bottom;
            }
        }
        return result;
    }

    protected int getFillChildTop() {
        int result = Integer.MIN_VALUE;
        for (Column c : this.mColumns) {
            result = Math.max(result, c.getTop());
        }
        return result;
    }

    protected int getScrollChildBottom() {
        int result = Integer.MIN_VALUE;
        for (Column c : this.mColumns) {
            int bottom = c.getBottom();
            if (result < bottom) {
                result = bottom;
            }
        }
        return result;
    }

    protected int getScrollChildTop() {
        int result = ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
        for (Column c : this.mColumns) {
            int top = c.getTop();
            if (result > top) {
                result = top;
            }
        }
        return result;
    }

    protected int getItemLeft(int pos) {
        if (isHeaderOrFooterPosition(pos)) {
            return this.mFixedColumn.getColumnLeft();
        }
        return getColumnLeft(pos);
    }

    protected int getItemTop(int pos) {
        if (isHeaderOrFooterPosition(pos)) {
            return this.mFixedColumn.getBottom();
        }
        int colIndex = this.mItems.get(pos, -1);
        if (colIndex == -1) {
            return getFillChildBottom();
        }
        return this.mColumns[colIndex].getBottom();
    }

    protected int getItemBottom(int pos) {
        if (isHeaderOrFooterPosition(pos)) {
            return this.mFixedColumn.getTop();
        }
        int colIndex = this.mItems.get(pos, -1);
        if (colIndex == -1) {
            return getFillChildTop();
        }
        return this.mColumns[colIndex].getTop();
    }

    private Column getNextColumn(boolean flow, int position) {
        int colIndex = this.mItems.get(position, -1);
        if (colIndex != -1) {
            return this.mColumns[colIndex];
        }
        int lastVisiblePos = Math.max(0, Math.max(0, position - getHeaderViewsCount()));
        if (lastVisiblePos < this.mColumnNumber) {
            return this.mColumns[lastVisiblePos];
        }
        if (flow) {
            return gettBottomColumn();
        }
        return getTopColumn();
    }

    private boolean isHeaderOrFooterPosition(int pos) {
        return this.mAdapter.getItemViewType(pos) == -2;
    }

    private Column getTopColumn() {
        int i = 0;
        Column result = this.mColumns[0];
        Column[] columnArr = this.mColumns;
        int length = columnArr.length;
        while (i < length) {
            Column c = columnArr[i];
            if (result.getTop() > c.getTop()) {
                result = c;
            }
            i++;
        }
        return result;
    }

    private Column gettBottomColumn() {
        int i = 0;
        Column result = this.mColumns[0];
        Column[] columnArr = this.mColumns;
        int length = columnArr.length;
        while (i < length) {
            Column c = columnArr[i];
            if (result.getBottom() > c.getBottom()) {
                result = c;
            }
            i++;
        }
        return result;
    }

    private int getColumnLeft(int pos) {
        int colIndex = this.mItems.get(pos, -1);
        if (colIndex == -1) {
            return 0;
        }
        return this.mColumns[colIndex].getColumnLeft();
    }

    private int getColumnWidth(int pos) {
        int colIndex = this.mItems.get(pos, -1);
        if (colIndex == -1) {
            return 0;
        }
        return this.mColumns[colIndex].getColumnWidth();
    }

    public void onLoadMoreComplete() {
        this.loadingMoreComplete = true;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        if (listener != null) {
            this.loadMoreListener = listener;
            setOnScrollListener(this.scroller);
        }
    }

    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putParcelable("items", this.mItems);
        return bundle;
    }

    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            this.mItems = (ParcelableSparseIntArray) bundle.getParcelable("items");
            state = bundle.getParcelable("instanceState");
        }
        super.onRestoreInstanceState(state);
    }
}
