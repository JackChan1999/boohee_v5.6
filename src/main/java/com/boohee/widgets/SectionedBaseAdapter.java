package com.boohee.widgets;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.boohee.widgets.PinnedHeaderListView.PinnedSectionedHeaderAdapter;

public abstract class SectionedBaseAdapter extends BaseAdapter implements
        PinnedSectionedHeaderAdapter {
    private static int                  HEADER_VIEW_TYPE      = 0;
    private static int                  ITEM_VIEW_TYPE        = 0;
    private        int                  mCount                = -1;
    private        SparseArray<Integer> mSectionCache         = new SparseArray();
    private        int                  mSectionCount         = -1;
    private        SparseArray<Integer> mSectionCountCache    = new SparseArray();
    private        SparseArray<Integer> mSectionPositionCache = new SparseArray();

    public abstract int getCountForSection(int i);

    public abstract Object getItem(int i, int i2);

    public abstract long getItemId(int i, int i2);

    public abstract View getItemView(int i, int i2, View view, ViewGroup viewGroup);

    public abstract int getSectionCount();

    public abstract View getSectionHeaderView(int i, View view, ViewGroup viewGroup);

    public void notifyDataSetChanged() {
        this.mSectionCache.clear();
        this.mSectionPositionCache.clear();
        this.mSectionCountCache.clear();
        this.mCount = -1;
        this.mSectionCount = -1;
        super.notifyDataSetChanged();
    }

    public void notifyDataSetInvalidated() {
        this.mSectionCache.clear();
        this.mSectionPositionCache.clear();
        this.mSectionCountCache.clear();
        this.mCount = -1;
        this.mSectionCount = -1;
        super.notifyDataSetInvalidated();
    }

    public final int getCount() {
        if (this.mCount >= 0) {
            return this.mCount;
        }
        int count = 0;
        for (int i = 0; i < internalGetSectionCount(); i++) {
            count = (count + internalGetCountForSection(i)) + 1;
        }
        this.mCount = count;
        return count;
    }

    public final Object getItem(int position) {
        return getItem(getSectionForPosition(position), getPositionInSectionForPosition(position));
    }

    public final long getItemId(int position) {
        return getItemId(getSectionForPosition(position), getPositionInSectionForPosition
                (position));
    }

    public final View getView(int position, View convertView, ViewGroup parent) {
        if (isSectionHeader(position)) {
            return getSectionHeaderView(getSectionForPosition(position), convertView, parent);
        }
        return getItemView(getSectionForPosition(position), getPositionInSectionForPosition
                (position), convertView, parent);
    }

    public final int getItemViewType(int position) {
        if (isSectionHeader(position)) {
            return getItemViewTypeCount() + getSectionHeaderViewType(getSectionForPosition
                    (position));
        }
        return getItemViewType(getSectionForPosition(position), getPositionInSectionForPosition
                (position));
    }

    public final int getViewTypeCount() {
        return getItemViewTypeCount() + getSectionHeaderViewTypeCount();
    }

    public final int getSectionForPosition(int position) {
        Integer cachedSection = (Integer) this.mSectionCache.get(position);
        if (cachedSection != null) {
            return cachedSection.intValue();
        }
        int sectionStart = 0;
        int i = 0;
        while (i < internalGetSectionCount()) {
            int sectionEnd = (sectionStart + internalGetCountForSection(i)) + 1;
            if (position < sectionStart || position >= sectionEnd) {
                sectionStart = sectionEnd;
                i++;
            } else {
                this.mSectionCache.put(position, Integer.valueOf(i));
                return i;
            }
        }
        return 0;
    }

    public int getPositionInSectionForPosition(int position) {
        Integer cachedPosition = (Integer) this.mSectionPositionCache.get(position);
        if (cachedPosition != null) {
            return cachedPosition.intValue();
        }
        int sectionStart = 0;
        int i = 0;
        while (i < internalGetSectionCount()) {
            int sectionEnd = (sectionStart + internalGetCountForSection(i)) + 1;
            if (position < sectionStart || position >= sectionEnd) {
                sectionStart = sectionEnd;
                i++;
            } else {
                int positionInSection = (position - sectionStart) - 1;
                this.mSectionPositionCache.put(position, Integer.valueOf(positionInSection));
                return positionInSection;
            }
        }
        return 0;
    }

    public final boolean isSectionHeader(int position) {
        int sectionStart = 0;
        for (int i = 0; i < internalGetSectionCount(); i++) {
            if (position == sectionStart) {
                return true;
            }
            if (position < sectionStart) {
                return false;
            }
            sectionStart += internalGetCountForSection(i) + 1;
        }
        return false;
    }

    public int getItemViewType(int section, int position) {
        return ITEM_VIEW_TYPE;
    }

    public int getItemViewTypeCount() {
        return 1;
    }

    public int getSectionHeaderViewType(int section) {
        return HEADER_VIEW_TYPE;
    }

    public int getSectionHeaderViewTypeCount() {
        return 1;
    }

    private int internalGetCountForSection(int section) {
        Integer cachedSectionCount = (Integer) this.mSectionCountCache.get(section);
        if (cachedSectionCount != null) {
            return cachedSectionCount.intValue();
        }
        int sectionCount = getCountForSection(section);
        this.mSectionCountCache.put(section, Integer.valueOf(sectionCount));
        return sectionCount;
    }

    private int internalGetSectionCount() {
        if (this.mSectionCount >= 0) {
            return this.mSectionCount;
        }
        this.mSectionCount = getSectionCount();
        return this.mSectionCount;
    }
}
