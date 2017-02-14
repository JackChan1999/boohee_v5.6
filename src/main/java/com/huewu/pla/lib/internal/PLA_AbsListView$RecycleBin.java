package com.huewu.pla.lib.internal;

import android.view.View;

import com.huewu.pla.lib.DebugUtil;
import com.huewu.pla.lib.internal.PLA_AbsListView.LayoutParams;

import java.util.List;
import java.util.Stack;

class PLA_AbsListView$RecycleBin {
    private View[] mActiveViews = new View[0];
    private               Stack<View>                      mCurrentScrap;
    private               int                              mFirstActivePosition;
    private               PLA_AbsListView$RecyclerListener mRecyclerListener;
    private               Stack<View>[]                    mScrapViews;
    private               int                              mViewTypeCount;
    final /* synthetic */ PLA_AbsListView                  this$0;

    PLA_AbsListView$RecycleBin(PLA_AbsListView this$0) {
        this.this$0 = this$0;
    }

    public void setViewTypeCount(int viewTypeCount) {
        if (viewTypeCount < 1) {
            throw new IllegalArgumentException("Can't have a viewTypeCount < 1");
        }
        Stack<View>[] scrapViews = new Stack[viewTypeCount];
        for (int i = 0; i < viewTypeCount; i++) {
            scrapViews[i] = new Stack();
        }
        this.mViewTypeCount = viewTypeCount;
        this.mCurrentScrap = scrapViews[0];
        this.mScrapViews = scrapViews;
    }

    public void markChildrenDirty() {
        Stack<View> scrap;
        int scrapCount;
        int i;
        if (this.mViewTypeCount == 1) {
            scrap = this.mCurrentScrap;
            scrapCount = scrap.size();
            for (i = 0; i < scrapCount; i++) {
                ((View) scrap.get(i)).forceLayout();
            }
            return;
        }
        int typeCount = this.mViewTypeCount;
        for (i = 0; i < typeCount; i++) {
            scrap = this.mScrapViews[i];
            scrapCount = scrap.size();
            for (int j = 0; j < scrapCount; j++) {
                ((View) scrap.get(j)).forceLayout();
            }
        }
    }

    public boolean shouldRecycleViewType(int viewType) {
        return viewType >= 0;
    }

    void clear() {
        Stack<View> scrap;
        int scrapCount;
        int i;
        if (this.mViewTypeCount == 1) {
            scrap = this.mCurrentScrap;
            scrapCount = scrap.size();
            for (i = 0; i < scrapCount; i++) {
                PLA_AbsListView.access$900(this.this$0, (View) scrap.remove((scrapCount - 1) - i)
                        , false);
            }
            return;
        }
        int typeCount = this.mViewTypeCount;
        for (i = 0; i < typeCount; i++) {
            scrap = this.mScrapViews[i];
            scrapCount = scrap.size();
            for (int j = 0; j < scrapCount; j++) {
                PLA_AbsListView.access$1000(this.this$0, (View) scrap.remove((scrapCount - 1) -
                        j), false);
            }
        }
    }

    void fillActiveViews(int childCount, int firstActivePosition) {
        if (this.mActiveViews.length < childCount) {
            this.mActiveViews = new View[childCount];
        }
        this.mFirstActivePosition = firstActivePosition;
        View[] activeViews = this.mActiveViews;
        for (int i = 0; i < childCount; i++) {
            View child = this.this$0.getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if (!(lp == null || lp.viewType == -2)) {
                activeViews[i] = child;
            }
        }
    }

    View getActiveView(int position) {
        int index = position - this.mFirstActivePosition;
        View[] activeViews = this.mActiveViews;
        if (index < 0 || index >= activeViews.length) {
            return null;
        }
        View match = activeViews[index];
        activeViews[index] = null;
        return match;
    }

    View getScrapView(int position) {
        DebugUtil.i("getFromScrap: " + position);
        if (this.this$0.getHeaderViewsCount() > position) {
            return null;
        }
        Stack<View> scrapViews;
        if (this.mViewTypeCount == 1) {
            scrapViews = this.mCurrentScrap;
        } else {
            int whichScrap = this.this$0.mAdapter.getItemViewType(position);
            if (whichScrap < 0 || whichScrap >= this.mScrapViews.length) {
                return null;
            }
            scrapViews = this.mScrapViews[whichScrap];
        }
        int size = scrapViews.size();
        for (int i = size - 1; i >= 0; i--) {
            if (((LayoutParams) ((View) scrapViews.get(i)).getLayoutParams())
                    .scrappedFromPosition == position) {
                return (View) scrapViews.remove(i);
            }
        }
        return size > 0 ? (View) scrapViews.remove(0) : null;
    }

    void addScrapView(View scrap) {
        DebugUtil.i("addToScrap");
        LayoutParams lp = (LayoutParams) scrap.getLayoutParams();
        if (lp != null) {
            int viewType = lp.viewType;
            if (shouldRecycleViewType(viewType)) {
                if (this.mViewTypeCount == 1) {
                    PLA_AbsListView.access$1200(this.this$0, scrap);
                    this.mCurrentScrap.add(scrap);
                } else {
                    PLA_AbsListView.access$1200(this.this$0, scrap);
                    this.mScrapViews[viewType].push(scrap);
                }
                if (this.mRecyclerListener != null) {
                    this.mRecyclerListener.onMovedToScrapHeap(scrap);
                }
            } else if (viewType != -2) {
                PLA_AbsListView.access$1100(this.this$0, scrap, false);
            }
        }
    }

    void scrapActiveViews() {
        boolean hasListener;
        boolean multipleScraps;
        View[] activeViews = this.mActiveViews;
        if (this.mRecyclerListener != null) {
            hasListener = true;
        } else {
            hasListener = false;
        }
        if (this.mViewTypeCount > 1) {
            multipleScraps = true;
        } else {
            multipleScraps = false;
        }
        Stack<View> scrapViews = this.mCurrentScrap;
        for (int i = activeViews.length - 1; i >= 0; i--) {
            View victim = activeViews[i];
            if (victim != null) {
                int whichScrap = ((LayoutParams) victim.getLayoutParams()).viewType;
                activeViews[i] = null;
                if (shouldRecycleViewType(whichScrap)) {
                    if (multipleScraps) {
                        scrapViews = this.mScrapViews[whichScrap];
                    }
                    PLA_AbsListView.access$1200(this.this$0, victim);
                    DebugUtil.i("addToScrap from scrapActiveViews");
                    scrapViews.add(victim);
                    if (hasListener) {
                        this.mRecyclerListener.onMovedToScrapHeap(victim);
                    }
                } else if (whichScrap != -2) {
                    PLA_AbsListView.access$1300(this.this$0, victim, false);
                }
            }
        }
        pruneScrapViews();
    }

    private void pruneScrapViews() {
        int maxViews = this.mActiveViews.length;
        int viewTypeCount = this.mViewTypeCount;
        Stack<View>[] scrapViews = this.mScrapViews;
        for (int i = 0; i < viewTypeCount; i++) {
            Stack<View> scrapPile = scrapViews[i];
            int size = scrapPile.size();
            int extras = size - maxViews;
            int j = 0;
            int size2 = size - 1;
            while (j < extras) {
                DebugUtil.i("remove scarp views from pruneScrapViews");
                size = size2 - 1;
                PLA_AbsListView.access$1400(this.this$0, (View) scrapPile.remove(size2), false);
                j++;
                size2 = size;
            }
        }
    }

    void reclaimScrapViews(List<View> views) {
        if (this.mViewTypeCount == 1) {
            views.addAll(this.mCurrentScrap);
            return;
        }
        int viewTypeCount = this.mViewTypeCount;
        Stack<View>[] scrapViews = this.mScrapViews;
        for (int i = 0; i < viewTypeCount; i++) {
            Stack<View> scrapPile = scrapViews[i];
            DebugUtil.i("add scarp views from reclaimScrapViews");
            views.addAll(scrapPile);
        }
    }

    void setCacheColorHint(int color) {
        int i;
        Stack<View> scrap;
        int scrapCount;
        if (this.mViewTypeCount == 1) {
            scrap = this.mCurrentScrap;
            scrapCount = scrap.size();
            for (i = 0; i < scrapCount; i++) {
                ((View) scrap.get(i)).setDrawingCacheBackgroundColor(color);
            }
        } else {
            int typeCount = this.mViewTypeCount;
            for (i = 0; i < typeCount; i++) {
                scrap = this.mScrapViews[i];
                scrapCount = scrap.size();
                for (int j = 0; j < scrapCount; j++) {
                    ((View) scrap.get(i)).setDrawingCacheBackgroundColor(color);
                }
            }
        }
        for (View victim : this.mActiveViews) {
            if (victim != null) {
                victim.setDrawingCacheBackgroundColor(color);
            }
        }
    }
}
