package kankan.wheel.widget;

import android.view.View;
import android.widget.LinearLayout;
import java.util.LinkedList;
import java.util.List;

public class WheelRecycle {
    private List<View> emptyItems;
    private List<View> items;
    private WheelView wheel;

    public WheelRecycle(WheelView wheel) {
        this.wheel = wheel;
    }

    public int recycleItems(LinearLayout layout, int firstItem, ItemsRange range) {
        int index = firstItem;
        int i = 0;
        while (i < layout.getChildCount()) {
            if (range.contains(index)) {
                i++;
            } else {
                recycleView(layout.getChildAt(i), index);
                layout.removeViewAt(i);
                if (i == 0) {
                    firstItem++;
                }
            }
            index++;
        }
        return firstItem;
    }

    public View getItem() {
        return getCachedView(this.items);
    }

    public View getEmptyItem() {
        return getCachedView(this.emptyItems);
    }

    public void clearAll() {
        if (this.items != null) {
            this.items.clear();
        }
        if (this.emptyItems != null) {
            this.emptyItems.clear();
        }
    }

    private List<View> addView(View view, List<View> cache) {
        if (cache == null) {
            cache = new LinkedList();
        }
        cache.add(view);
        return cache;
    }

    private void recycleView(View view, int index) {
        int count = this.wheel.getViewAdapter().getItemsCount();
        if ((index < 0 || index >= count) && !this.wheel.isCyclic()) {
            this.emptyItems = addView(view, this.emptyItems);
            return;
        }
        while (index < 0) {
            index += count;
        }
        index %= count;
        this.items = addView(view, this.items);
    }

    private View getCachedView(List<View> cache) {
        if (cache == null || cache.size() <= 0) {
            return null;
        }
        View view = (View) cache.get(0);
        cache.remove(0);
        return view;
    }
}
