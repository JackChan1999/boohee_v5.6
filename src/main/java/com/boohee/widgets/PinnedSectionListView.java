package com.boohee.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;

public class PinnedSectionListView extends ListView {
    private OnScrollListener mDelegateOnScrollListener;
    private OnScrollListener mOnScrollListener = new OnScrollListener() {
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (PinnedSectionListView.this.mDelegateOnScrollListener != null) {
                PinnedSectionListView.this.mDelegateOnScrollListener.onScrollStateChanged(view,
                        scrollState);
            }
        }

        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int
                totalItemCount) {
            if (PinnedSectionListView.this.mDelegateOnScrollListener != null) {
                PinnedSectionListView.this.mDelegateOnScrollListener.onScroll(view,
                        firstVisibleItem, visibleItemCount, totalItemCount);
            }
            if (((PinnedSectionListAdapter) view.getAdapter()) != null && visibleItemCount != 0) {
                int visibleSectionPosition = PinnedSectionListView.this
                        .findFirstVisibleSectionPosition(firstVisibleItem, visibleItemCount);
                if (visibleSectionPosition == -1) {
                    int currentSectionPosition = PinnedSectionListView.this
                            .findCurrentSectionPosition(firstVisibleItem);
                    if (currentSectionPosition != -1) {
                        if (PinnedSectionListView.this.mPinnedShadow != null) {
                            if (PinnedSectionListView.this.mPinnedShadow.position ==
                                    currentSectionPosition) {
                                PinnedSectionListView.this.mTranslateY = 0;
                                return;
                            }
                            PinnedSectionListView.this.destroyPinnedShadow();
                        }
                        PinnedSectionListView.this.createPinnedShadow(currentSectionPosition);
                        return;
                    }
                    return;
                }
                int visibleSectionTop = view.getChildAt(visibleSectionPosition -
                        firstVisibleItem).getTop();
                int topBorder = PinnedSectionListView.this.getListPaddingTop();
                if (PinnedSectionListView.this.mPinnedShadow == null) {
                    if (visibleSectionTop < topBorder) {
                        PinnedSectionListView.this.createPinnedShadow(visibleSectionPosition);
                    }
                } else if (visibleSectionPosition != PinnedSectionListView.this.mPinnedShadow
                        .position) {
                    int pinnedSectionBottom = topBorder + PinnedSectionListView.this
                            .mPinnedShadow.view.getHeight();
                    if (visibleSectionTop >= pinnedSectionBottom) {
                        PinnedSectionListView.this.mTranslateY = 0;
                    } else if (visibleSectionTop < topBorder) {
                        PinnedSectionListView.this.destroyPinnedShadow();
                        PinnedSectionListView.this.createPinnedShadow(visibleSectionPosition);
                    } else {
                        PinnedSectionListView.this.mTranslateY = visibleSectionTop -
                                pinnedSectionBottom;
                    }
                } else if (visibleSectionTop > topBorder) {
                    PinnedSectionListView.this.destroyPinnedShadow();
                    visibleSectionPosition = PinnedSectionListView.this
                            .findCurrentSectionPosition(visibleSectionPosition - 1);
                    if (visibleSectionPosition > -1) {
                        PinnedSectionListView.this.createPinnedShadow(visibleSectionPosition);
                        int translateY = (visibleSectionTop - topBorder) - PinnedSectionListView
                                .this.mPinnedShadow.view.getHeight();
                        if (translateY > 0) {
                            translateY = 0;
                        }
                        PinnedSectionListView.this.mTranslateY = translateY;
                    }
                }
            }
        }
    };
    PinnedViewShadow mPinnedShadow;
    PinnedViewShadow mRecycleShadow;
    int              mTranslateY;

    public interface PinnedSectionListAdapter extends ListAdapter {
        boolean isItemViewTypePinned(int i);
    }

    static class PinnedViewShadow {
        public int  position;
        public View view;

        PinnedViewShadow() {
        }
    }

    public PinnedSectionListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public PinnedSectionListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void createPinnedShadow(int position) {
        int heightMode;
        int heightSize;
        PinnedViewShadow pinnedShadow = this.mRecycleShadow;
        View recycleView = pinnedShadow == null ? null : pinnedShadow.view;
        this.mRecycleShadow = null;
        View pinnedView = getAdapter().getView(position, recycleView, this);
        LayoutParams layoutParams = (LayoutParams) pinnedView.getLayoutParams();
        if (layoutParams == null) {
            heightMode = Integer.MIN_VALUE;
            heightSize = getHeight();
        } else {
            heightMode = MeasureSpec.getMode(layoutParams.height);
            heightSize = MeasureSpec.getSize(layoutParams.height);
        }
        if (heightMode == 0) {
            heightMode = 1073741824;
        }
        int maxHeight = (getHeight() - getListPaddingTop()) - getListPaddingBottom();
        if (heightSize > maxHeight) {
            heightSize = maxHeight;
        }
        pinnedView.measure(MeasureSpec.makeMeasureSpec((getWidth() - getListPaddingLeft()) -
                getListPaddingRight(), 1073741824), MeasureSpec.makeMeasureSpec(heightSize,
                heightMode));
        pinnedView.layout(0, 0, pinnedView.getMeasuredWidth(), pinnedView.getMeasuredHeight());
        this.mTranslateY = 0;
        if (pinnedShadow == null) {
            pinnedShadow = new PinnedViewShadow();
        }
        pinnedShadow.position = position;
        pinnedShadow.view = pinnedView;
        this.mPinnedShadow = pinnedShadow;
    }

    private void destroyPinnedShadow() {
        this.mRecycleShadow = this.mPinnedShadow;
        this.mPinnedShadow = null;
    }

    private int findFirstVisibleSectionPosition(int firstVisibleItem, int visibleItemCount) {
        PinnedSectionListAdapter adapter = (PinnedSectionListAdapter) getAdapter();
        for (int childIndex = 0; childIndex < visibleItemCount; childIndex++) {
            int position = firstVisibleItem + childIndex;
            if (adapter.isItemViewTypePinned(adapter.getItemViewType(position))) {
                return position;
            }
        }
        return -1;
    }

    private int findCurrentSectionPosition(int fromPosition) {
        PinnedSectionListAdapter adapter = (PinnedSectionListAdapter) getAdapter();
        if (adapter instanceof SectionIndexer) {
            SectionIndexer indexer = (SectionIndexer) adapter;
            int itemPosition = indexer.getPositionForSection(indexer.getSectionForPosition
                    (fromPosition));
            if (adapter.isItemViewTypePinned(adapter.getItemViewType(itemPosition))) {
                return itemPosition;
            }
        }
        for (int position = fromPosition; position >= 0; position--) {
            if (adapter.isItemViewTypePinned(adapter.getItemViewType(position))) {
                return position;
            }
        }
        return -1;
    }

    private void initView() {
        setOnScrollListener(this.mOnScrollListener);
    }

    public void setOnScrollListener(OnScrollListener listener) {
        if (listener == this.mOnScrollListener) {
            super.setOnScrollListener(listener);
        } else {
            this.mDelegateOnScrollListener = listener;
        }
    }

    public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        post(new Runnable() {
            public void run() {
                if (PinnedSectionListView.this.getAdapter() != null) {
                    int firstVisiblePosition = PinnedSectionListView.this.getFirstVisiblePosition();
                    int position = PinnedSectionListView.this.findCurrentSectionPosition
                            (firstVisiblePosition);
                    if (position == -1) {
                        return;
                    }
                    if (firstVisiblePosition == position) {
                        PinnedSectionListView.this.createPinnedShadow(firstVisiblePosition);
                        View childView = PinnedSectionListView.this.getChildAt
                                (firstVisiblePosition);
                        PinnedSectionListView.this.mTranslateY = childView == null ? 0 :
                                -childView.getTop();
                        return;
                    }
                    PinnedSectionListView.this.createPinnedShadow(position);
                }
            }
        });
    }

    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
    }

    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (this.mPinnedShadow != null) {
            int pLeft = getListPaddingLeft();
            int pTop = getListPaddingTop();
            View view = this.mPinnedShadow.view;
            canvas.save();
            canvas.clipRect(pLeft, pTop, view.getWidth() + pLeft, view.getHeight() + pTop);
            canvas.translate((float) pLeft, (float) (this.mTranslateY + pTop));
            drawChild(canvas, this.mPinnedShadow.view, getDrawingTime());
            canvas.restore();
        }
    }
}
