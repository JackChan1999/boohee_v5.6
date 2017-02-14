package kankan.wheel.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import java.util.LinkedList;
import java.util.List;
import kankan.wheel.widget.WheelScroller.ScrollingListener;
import kankan.wheel.widget.adapters.WheelViewAdapter;

public class WheelView extends View {
    private static final int DEF_VISIBLE_ITEMS = 3;
    private static final int ITEM_OFFSET_PERCENT = 0;
    private static final int PADDING = 10;
    private int[] SHADOWS_COLORS = new int[]{-15658735, 11184810, 11184810};
    private GradientDrawable bottomShadow;
    private Drawable centerDrawable;
    private List<OnWheelChangedListener> changingListeners = new LinkedList();
    private List<OnWheelClickedListener> clickingListeners = new LinkedList();
    private int currentItem = 0;
    private DataSetObserver dataObserver = new DataSetObserver() {
        public void onChanged() {
            WheelView.this.invalidateWheel(false);
        }

        public void onInvalidated() {
            WheelView.this.invalidateWheel(true);
        }
    };
    private boolean drawShadows = false;
    private int firstItem;
    boolean isCyclic = false;
    private boolean isScrollingPerformed;
    private int itemHeight = 0;
    private LinearLayout itemsLayout;
    private WheelRecycle recycle = new WheelRecycle(this);
    private WheelScroller scroller;
    ScrollingListener scrollingListener = new ScrollingListener() {
        public void onStarted() {
            WheelView.this.isScrollingPerformed = true;
            WheelView.this.notifyScrollingListenersAboutStart();
        }

        public void onScroll(int distance) {
            WheelView.this.doScroll(distance);
            int height = WheelView.this.getHeight();
            if (WheelView.this.scrollingOffset > height) {
                WheelView.this.scrollingOffset = height;
                WheelView.this.scroller.stopScrolling();
            } else if (WheelView.this.scrollingOffset < (-height)) {
                WheelView.this.scrollingOffset = -height;
                WheelView.this.scroller.stopScrolling();
            }
        }

        public void onFinished() {
            if (WheelView.this.isScrollingPerformed) {
                WheelView.this.notifyScrollingListenersAboutEnd();
                WheelView.this.isScrollingPerformed = false;
            }
            WheelView.this.scrollingOffset = 0;
            WheelView.this.invalidate();
        }

        public void onJustify() {
            if (Math.abs(WheelView.this.scrollingOffset) > 1) {
                WheelView.this.scroller.scroll(WheelView.this.scrollingOffset, 0);
            }
        }
    };
    private List<OnWheelScrollListener> scrollingListeners = new LinkedList();
    private int scrollingOffset;
    private GradientDrawable topShadow;
    private WheelViewAdapter viewAdapter;
    private int visibleItems = 3;
    private int wheelBackground = R.drawable.wheel_bg;
    private int wheelForeground = R.drawable.wheel_val;

    public WheelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initData(context);
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData(context);
    }

    public WheelView(Context context) {
        super(context);
        initData(context);
    }

    private void initData(Context context) {
        this.scroller = new WheelScroller(getContext(), this.scrollingListener);
    }

    public void setInterpolator(Interpolator interpolator) {
        this.scroller.setInterpolator(interpolator);
    }

    public int getVisibleItems() {
        return this.visibleItems;
    }

    public void setVisibleItems(int count) {
        this.visibleItems = count;
    }

    public WheelViewAdapter getViewAdapter() {
        return this.viewAdapter;
    }

    public void setViewAdapter(WheelViewAdapter viewAdapter) {
        if (this.viewAdapter != null) {
            this.viewAdapter.unregisterDataSetObserver(this.dataObserver);
        }
        this.viewAdapter = viewAdapter;
        if (this.viewAdapter != null) {
            this.viewAdapter.registerDataSetObserver(this.dataObserver);
        }
        invalidateWheel(true);
    }

    public void addChangingListener(OnWheelChangedListener listener) {
        this.changingListeners.add(listener);
    }

    public void removeChangingListener(OnWheelChangedListener listener) {
        this.changingListeners.remove(listener);
    }

    protected void notifyChangingListeners(int oldValue, int newValue) {
        for (OnWheelChangedListener listener : this.changingListeners) {
            listener.onChanged(this, oldValue, newValue);
        }
    }

    public void addScrollingListener(OnWheelScrollListener listener) {
        this.scrollingListeners.add(listener);
    }

    public void removeScrollingListener(OnWheelScrollListener listener) {
        this.scrollingListeners.remove(listener);
    }

    protected void notifyScrollingListenersAboutStart() {
        for (OnWheelScrollListener listener : this.scrollingListeners) {
            listener.onScrollingStarted(this);
        }
    }

    protected void notifyScrollingListenersAboutEnd() {
        for (OnWheelScrollListener listener : this.scrollingListeners) {
            listener.onScrollingFinished(this);
        }
    }

    public void addClickingListener(OnWheelClickedListener listener) {
        this.clickingListeners.add(listener);
    }

    public void removeClickingListener(OnWheelClickedListener listener) {
        this.clickingListeners.remove(listener);
    }

    protected void notifyClickListenersAboutClick(int item) {
        for (OnWheelClickedListener listener : this.clickingListeners) {
            listener.onItemClicked(this, item);
        }
    }

    public int getCurrentItem() {
        return this.currentItem;
    }

    public void setCurrentItem(int index, boolean animated) {
        if (this.viewAdapter != null && this.viewAdapter.getItemsCount() != 0) {
            int itemCount = this.viewAdapter.getItemsCount();
            if (index < 0 || index >= itemCount) {
                if (this.isCyclic) {
                    while (index < 0) {
                        index += itemCount;
                    }
                    index %= itemCount;
                } else {
                    return;
                }
            }
            if (index == this.currentItem) {
                return;
            }
            if (animated) {
                int itemsToScroll = index - this.currentItem;
                if (this.isCyclic) {
                    int scroll = (Math.min(index, this.currentItem) + itemCount) - Math.max(index, this.currentItem);
                    if (scroll < Math.abs(itemsToScroll)) {
                        itemsToScroll = itemsToScroll < 0 ? scroll : -scroll;
                    }
                }
                scroll(itemsToScroll, 0);
                return;
            }
            this.scrollingOffset = 0;
            int old = this.currentItem;
            this.currentItem = index;
            notifyChangingListeners(old, this.currentItem);
            invalidate();
        }
    }

    public void setCurrentItem(int index) {
        setCurrentItem(index, false);
    }

    public boolean isCyclic() {
        return this.isCyclic;
    }

    public void setCyclic(boolean isCyclic) {
        this.isCyclic = isCyclic;
        invalidateWheel(false);
    }

    public boolean drawShadows() {
        return this.drawShadows;
    }

    public void setDrawShadows(boolean drawShadows) {
        this.drawShadows = drawShadows;
    }

    public void setShadowColor(int start, int middle, int end) {
        this.SHADOWS_COLORS = new int[]{start, middle, end};
    }

    public void setWheelBackground(int resource) {
        this.wheelBackground = resource;
        setBackgroundResource(this.wheelBackground);
    }

    public void setWheelForeground(int resource) {
        this.wheelForeground = resource;
        this.centerDrawable = getContext().getResources().getDrawable(this.wheelForeground);
    }

    public void invalidateWheel(boolean clearCaches) {
        if (clearCaches) {
            this.recycle.clearAll();
            if (this.itemsLayout != null) {
                this.itemsLayout.removeAllViews();
            }
            this.scrollingOffset = 0;
        } else if (this.itemsLayout != null) {
            this.recycle.recycleItems(this.itemsLayout, this.firstItem, new ItemsRange());
        }
        invalidate();
    }

    private void initResourcesIfNecessary() {
        if (this.centerDrawable == null) {
            this.centerDrawable = getContext().getResources().getDrawable(this.wheelForeground);
        }
        if (this.topShadow == null) {
            this.topShadow = new GradientDrawable(Orientation.TOP_BOTTOM, this.SHADOWS_COLORS);
        }
        if (this.bottomShadow == null) {
            this.bottomShadow = new GradientDrawable(Orientation.BOTTOM_TOP, this.SHADOWS_COLORS);
        }
        setBackgroundResource(this.wheelBackground);
    }

    private int getDesiredHeight(LinearLayout layout) {
        if (!(layout == null || layout.getChildAt(0) == null)) {
            this.itemHeight = layout.getChildAt(0).getMeasuredHeight();
        }
        return Math.max((this.itemHeight * this.visibleItems) - ((this.itemHeight * 0) / 50), getSuggestedMinimumHeight());
    }

    private int getItemHeight() {
        if (this.itemHeight != 0) {
            return this.itemHeight;
        }
        if (this.itemsLayout == null || this.itemsLayout.getChildAt(0) == null) {
            return getHeight() / this.visibleItems;
        }
        this.itemHeight = this.itemsLayout.getChildAt(0).getHeight();
        return this.itemHeight;
    }

    private int calculateLayoutWidth(int widthSize, int mode) {
        initResourcesIfNecessary();
        this.itemsLayout.setLayoutParams(new LayoutParams(-2, -2));
        this.itemsLayout.measure(MeasureSpec.makeMeasureSpec(widthSize, 0), MeasureSpec.makeMeasureSpec(0, 0));
        int width = this.itemsLayout.getMeasuredWidth();
        if (mode == 1073741824) {
            width = widthSize;
        } else {
            width = Math.max(width + 20, getSuggestedMinimumWidth());
            if (mode == Integer.MIN_VALUE && widthSize < width) {
                width = widthSize;
            }
        }
        this.itemsLayout.measure(MeasureSpec.makeMeasureSpec(width - 20, 1073741824), MeasureSpec.makeMeasureSpec(0, 0));
        return width;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        buildViewForMeasuring();
        int width = calculateLayoutWidth(widthSize, widthMode);
        if (heightMode == 1073741824) {
            height = heightSize;
        } else {
            height = getDesiredHeight(this.itemsLayout);
            if (heightMode == Integer.MIN_VALUE) {
                height = Math.min(height, heightSize);
            }
        }
        setMeasuredDimension(width, height);
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layout(r - l, b - t);
    }

    private void layout(int width, int height) {
        this.itemsLayout.layout(0, 0, width - 20, height);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.viewAdapter != null && this.viewAdapter.getItemsCount() > 0) {
            updateView();
            drawItems(canvas);
            drawCenterRect(canvas);
        }
        if (this.drawShadows) {
            drawShadows(canvas);
        }
    }

    private void drawShadows(Canvas canvas) {
        int height = (int) (1.5d * ((double) getItemHeight()));
        this.topShadow.setBounds(0, 0, getWidth(), height);
        this.topShadow.draw(canvas);
        this.bottomShadow.setBounds(0, getHeight() - height, getWidth(), getHeight());
        this.bottomShadow.draw(canvas);
    }

    private void drawItems(Canvas canvas) {
        canvas.save();
        canvas.translate(10.0f, (float) ((-(((this.currentItem - this.firstItem) * getItemHeight()) + ((getItemHeight() - getHeight()) / 2))) + this.scrollingOffset));
        this.itemsLayout.draw(canvas);
        canvas.restore();
    }

    private void drawCenterRect(Canvas canvas) {
        int center = getHeight() / 2;
        int offset = (int) (((double) (getItemHeight() / 2)) * 1.2d);
        this.centerDrawable.setBounds(0, center - offset, getWidth(), center + offset);
        this.centerDrawable.draw(canvas);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled() || getViewAdapter() == null) {
            return true;
        }
        switch (event.getAction()) {
            case 1:
                if (!this.isScrollingPerformed) {
                    int distance = ((int) event.getY()) - (getHeight() / 2);
                    if (distance > 0) {
                        distance += getItemHeight() / 2;
                    } else {
                        distance -= getItemHeight() / 2;
                    }
                    int items = distance / getItemHeight();
                    if (items != 0 && isValidItemIndex(this.currentItem + items)) {
                        notifyClickListenersAboutClick(this.currentItem + items);
                        break;
                    }
                }
                break;
            case 2:
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    break;
                }
                break;
        }
        return this.scroller.onTouchEvent(event);
    }

    private void doScroll(int delta) {
        this.scrollingOffset += delta;
        int itemHeight = getItemHeight();
        int count = this.scrollingOffset / itemHeight;
        int pos = this.currentItem - count;
        int itemCount = this.viewAdapter.getItemsCount();
        int fixPos = this.scrollingOffset % itemHeight;
        if (Math.abs(fixPos) <= itemHeight / 2) {
            fixPos = 0;
        }
        if (this.isCyclic && itemCount > 0) {
            if (fixPos > 0) {
                pos--;
                count++;
            } else if (fixPos < 0) {
                pos++;
                count--;
            }
            while (pos < 0) {
                pos += itemCount;
            }
            pos %= itemCount;
        } else if (pos < 0) {
            count = this.currentItem;
            pos = 0;
        } else if (pos >= itemCount) {
            count = (this.currentItem - itemCount) + 1;
            pos = itemCount - 1;
        } else if (pos > 0 && fixPos > 0) {
            pos--;
            count++;
        } else if (pos < itemCount - 1 && fixPos < 0) {
            pos++;
            count--;
        }
        int offset = this.scrollingOffset;
        if (pos != this.currentItem) {
            setCurrentItem(pos, false);
        } else {
            invalidate();
        }
        this.scrollingOffset = offset - (count * itemHeight);
        if (this.scrollingOffset > getHeight()) {
            this.scrollingOffset = (this.scrollingOffset % getHeight()) + getHeight();
        }
    }

    public void scroll(int itemsToScroll, int time) {
        this.scroller.scroll((getItemHeight() * itemsToScroll) - this.scrollingOffset, time);
    }

    private ItemsRange getItemsRange() {
        if (getItemHeight() == 0) {
            return null;
        }
        int first = this.currentItem;
        int count = 1;
        while (getItemHeight() * count < getHeight()) {
            first--;
            count += 2;
        }
        if (this.scrollingOffset != 0) {
            if (this.scrollingOffset > 0) {
                first--;
            }
            int emptyItems = this.scrollingOffset / getItemHeight();
            first -= emptyItems;
            count = (int) (((double) (count + 1)) + Math.asin((double) emptyItems));
        }
        return new ItemsRange(first, count);
    }

    private boolean rebuildItems() {
        int first;
        boolean updated;
        int i;
        ItemsRange range = getItemsRange();
        if (this.itemsLayout != null) {
            first = this.recycle.recycleItems(this.itemsLayout, this.firstItem, range);
            if (this.firstItem != first) {
                updated = true;
            } else {
                updated = false;
            }
            this.firstItem = first;
        } else {
            createItemsLayout();
            updated = true;
        }
        if (!updated) {
            if (this.firstItem == range.getFirst() && this.itemsLayout.getChildCount() == range.getCount()) {
                updated = false;
            } else {
                updated = true;
            }
        }
        if (this.firstItem <= range.getFirst() || this.firstItem > range.getLast()) {
            this.firstItem = range.getFirst();
        } else {
            i = this.firstItem - 1;
            while (i >= range.getFirst() && addViewItem(i, true)) {
                this.firstItem = i;
                i--;
            }
        }
        first = this.firstItem;
        for (i = this.itemsLayout.getChildCount(); i < range.getCount(); i++) {
            if (!addViewItem(this.firstItem + i, false) && this.itemsLayout.getChildCount() == 0) {
                first++;
            }
        }
        this.firstItem = first;
        return updated;
    }

    private void updateView() {
        if (rebuildItems()) {
            calculateLayoutWidth(getWidth(), 1073741824);
            layout(getWidth(), getHeight());
        }
    }

    private void createItemsLayout() {
        if (this.itemsLayout == null) {
            this.itemsLayout = new LinearLayout(getContext());
            this.itemsLayout.setOrientation(1);
        }
    }

    private void buildViewForMeasuring() {
        if (this.itemsLayout != null) {
            this.recycle.recycleItems(this.itemsLayout, this.firstItem, new ItemsRange());
        } else {
            createItemsLayout();
        }
        int addItems = this.visibleItems / 2;
        for (int i = this.currentItem + addItems; i >= this.currentItem - addItems; i--) {
            if (addViewItem(i, true)) {
                this.firstItem = i;
            }
        }
    }

    private boolean addViewItem(int index, boolean first) {
        View view = getItemView(index);
        if (view == null) {
            return false;
        }
        if (first) {
            this.itemsLayout.addView(view, 0);
        } else {
            this.itemsLayout.addView(view);
        }
        return true;
    }

    private boolean isValidItemIndex(int index) {
        return this.viewAdapter != null && this.viewAdapter.getItemsCount() > 0 && (this.isCyclic || (index >= 0 && index < this.viewAdapter.getItemsCount()));
    }

    private View getItemView(int index) {
        if (this.viewAdapter == null || this.viewAdapter.getItemsCount() == 0) {
            return null;
        }
        int count = this.viewAdapter.getItemsCount();
        if (!isValidItemIndex(index)) {
            return this.viewAdapter.getEmptyItem(this.recycle.getEmptyItem(), this.itemsLayout);
        }
        while (index < 0) {
            index += count;
        }
        return this.viewAdapter.getItem(index % count, this.recycle.getItem(), this.itemsLayout);
    }

    public void stopScrolling() {
        this.scroller.stopScrolling();
    }
}
