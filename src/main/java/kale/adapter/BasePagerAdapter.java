package kale.adapter;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public abstract class BasePagerAdapter<T> extends PagerAdapter {
    protected T currentItem = null;
    private final PagerCache<T> mCache = new PagerCache();

    private static class PagerCache<T> {
        private Map<Object, Queue<T>> mCacheMap = new ArrayMap();

        public T getItem(Object type) {
            Queue<T> queue = (Queue) this.mCacheMap.get(type);
            return queue != null ? queue.poll() : null;
        }

        public void putItem(Object type, T item) {
            Queue<T> queue = (Queue) this.mCacheMap.get(type);
            if (queue == null) {
                queue = new LinkedList();
                this.mCacheMap.put(type, queue);
            }
            queue.offer(item);
        }
    }

    protected abstract T createItem(ViewGroup viewGroup, int i);

    @NonNull
    protected abstract View getViewFromItem(T t, int i);

    public boolean isViewFromObject(View view, Object obj) {
        return view == getViewFromItem(obj, 0);
    }

    public T instantiateItem(ViewGroup container, int position) {
        Object type = getItemType(position);
        T item = this.mCache.getItem(type);
        if (item == null) {
            item = createItem(container, position);
        }
        View view = getViewFromItem(item, position);
        view.setTag(R.id.tag_item_type, type);
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        container.addView(view);
        return item;
    }

    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        if (object != this.currentItem) {
            this.currentItem = object;
        }
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        T item = object;
        container.removeView(getViewFromItem(item, position));
        this.mCache.putItem(getViewFromItem(item, position).getTag(R.id.tag_item_type), item);
    }

    public int getItemPosition(Object object) {
        return -2;
    }

    protected Object getItemType(int position) {
        return Integer.valueOf(-1);
    }

    public T getCurrentItem() {
        return this.currentItem;
    }

    protected PagerCache<T> getCache() {
        return this.mCache;
    }
}
