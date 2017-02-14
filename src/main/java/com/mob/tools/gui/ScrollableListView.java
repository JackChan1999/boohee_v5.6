package com.mob.tools.gui;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.ListView;

import com.mob.tools.gui.Scrollable.OnScrollListener;

public class ScrollableListView extends ListView implements Scrollable {
    private OnScrollListener osListener;
    private boolean          pullEnable;

    public ScrollableListView(Context context) {
        super(context);
        init(context);
    }

    public ScrollableListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public ScrollableListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    private void init(Context context) {
        setCacheColorHint(0);
        setSelector(new ColorDrawable());
        setDividerHeight(0);
        this.osListener = new OnScrollListener() {
            public void onScrollChanged(Scrollable scrollable, int i, int i2, int i3, int i4) {
                ScrollableListView scrollableListView = ScrollableListView.this;
                boolean z = i2 <= 0 && i4 <= 0;
                scrollableListView.pullEnable = z;
            }
        };
    }

    protected int computeVerticalScrollOffset() {
        int computeVerticalScrollOffset = super.computeVerticalScrollOffset();
        if (this.osListener != null) {
            this.osListener.onScrollChanged(this, 0, computeVerticalScrollOffset, 0, 0);
        }
        return computeVerticalScrollOffset;
    }

    public boolean isReadyToPull() {
        return this.pullEnable;
    }
}
