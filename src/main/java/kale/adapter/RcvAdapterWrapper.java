package kale.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.GridLayoutManager.SpanSizeLookup;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.AdapterDataObserver;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams;
import android.view.View;
import android.view.ViewGroup;

public class RcvAdapterWrapper extends Adapter<ViewHolder> {
    public static final int TYPE_FOOTER = 99931;
    public static final int TYPE_HEADER = 99930;
    private View footerView = null;
    private View headerView = null;
    private LayoutManager layoutManager;
    private Adapter mWrapped;

    private static class SimpleViewHolder extends ViewHolder {
        public SimpleViewHolder(View itemView) {
            super(itemView);
        }
    }

    public LayoutManager getLayoutManager() {
        return this.layoutManager;
    }

    public View getHeaderView() {
        return this.headerView;
    }

    public View getFooterView() {
        return this.footerView;
    }

    public RcvAdapterWrapper(@NonNull Adapter adapter, @NonNull LayoutManager layoutManager) {
        this.mWrapped = adapter;
        this.mWrapped.registerAdapterDataObserver(new AdapterDataObserver() {
            public void onChanged() {
                RcvAdapterWrapper.this.notifyDataSetChanged();
            }

            public void onItemRangeChanged(int positionStart, int itemCount) {
                RcvAdapterWrapper.this.notifyItemRangeChanged(RcvAdapterWrapper.this.getHeaderCount() + positionStart, itemCount);
            }

            public void onItemRangeInserted(int positionStart, int itemCount) {
                RcvAdapterWrapper.this.notifyItemRangeInserted(RcvAdapterWrapper.this.getHeaderCount() + positionStart, itemCount);
            }

            public void onItemRangeRemoved(int positionStart, int itemCount) {
                RcvAdapterWrapper.this.notifyItemRangeRemoved(RcvAdapterWrapper.this.getHeaderCount() + positionStart, itemCount);
                if (RcvAdapterWrapper.this.getFooterCount() != 0 && (RcvAdapterWrapper.this.getFooterCount() + positionStart) + 1 == RcvAdapterWrapper.this.getItemCount()) {
                    RcvAdapterWrapper.this.notifyDataSetChanged();
                }
            }

            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                RcvAdapterWrapper.this.notifyItemMoved(RcvAdapterWrapper.this.getHeaderCount() + fromPosition, RcvAdapterWrapper.this.getHeaderCount() + toPosition);
            }
        });
        this.layoutManager = layoutManager;
        if (this.layoutManager instanceof GridLayoutManager) {
            setSpanSizeLookup(this, (GridLayoutManager) this.layoutManager);
        }
    }

    public int getItemCount() {
        int offset = 0;
        if (this.headerView != null) {
            offset = 0 + 1;
        }
        if (this.footerView != null) {
            offset++;
        }
        return this.mWrapped.getItemCount() + offset;
    }

    public int getItemViewType(int position) {
        if (this.headerView != null && position == 0) {
            return TYPE_HEADER;
        }
        if (this.footerView == null || position != getItemCount() - 1) {
            return this.mWrapped.getItemViewType(position - getHeaderCount());
        }
        return TYPE_FOOTER;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return new SimpleViewHolder(this.headerView);
        }
        if (viewType == TYPE_FOOTER) {
            return new SimpleViewHolder(this.footerView);
        }
        return this.mWrapped.onCreateViewHolder(parent, viewType);
    }

    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        int type = getItemViewType(position);
        if (type != TYPE_HEADER && type != TYPE_FOOTER) {
            this.mWrapped.onBindViewHolder(viewHolder, position - getHeaderCount());
        }
    }

    public void setLayoutManager(LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        if (this.headerView != null) {
            setFullSpan(this.headerView, layoutManager);
        }
        if (this.footerView != null) {
            setFullSpan(this.footerView, layoutManager);
        }
    }

    public void setHeaderView(@NonNull View headerView) {
        this.headerView = headerView;
        setFullSpan(headerView, this.layoutManager);
    }

    public void setFooterView(@NonNull View footerView) {
        this.footerView = footerView;
        setFullSpan(footerView, this.layoutManager);
    }

    private void setFullSpan(View view, LayoutManager layoutManager) {
        int itemHeight = view.getLayoutParams() != null ? view.getLayoutParams().height : -2;
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            LayoutParams layoutParams = new LayoutParams(-1, itemHeight);
            layoutParams.setFullSpan(true);
            view.setLayoutParams(layoutParams);
        } else if (layoutManager instanceof GridLayoutManager) {
            view.setLayoutParams(new ViewGroup.LayoutParams(-1, itemHeight));
        }
        notifyDataSetChanged();
    }

    public void removeHeaderView() {
        this.headerView = null;
        notifyDataSetChanged();
    }

    public void removeFooterView() {
        this.footerView = null;
        notifyItemRemoved(getItemCount());
    }

    public Adapter getWrappedAdapter() {
        return this.mWrapped;
    }

    public int getHeaderCount() {
        return this.headerView != null ? 1 : 0;
    }

    public int getFooterCount() {
        return this.footerView != null ? 1 : 0;
    }

    static void setSpanSizeLookup(final Adapter adapter, final GridLayoutManager layoutManager) {
        layoutManager.setSpanSizeLookup(new SpanSizeLookup() {
            public int getSpanSize(int position) {
                int type = adapter.getItemViewType(position);
                if (type == RcvAdapterWrapper.TYPE_HEADER || type == RcvAdapterWrapper.TYPE_FOOTER) {
                    return layoutManager.getSpanCount();
                }
                return 1;
            }
        });
    }
}
