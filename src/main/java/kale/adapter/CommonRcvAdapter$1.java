package kale.adapter;

import android.databinding.ObservableList;
import android.databinding.ObservableList.OnListChangedCallback;

class CommonRcvAdapter$1 extends OnListChangedCallback<ObservableList<T>> {
    final /* synthetic */ CommonRcvAdapter this$0;

    CommonRcvAdapter$1(CommonRcvAdapter commonRcvAdapter) {
        this.this$0 = commonRcvAdapter;
    }

    public void onChanged(ObservableList<T> observableList) {
        this.this$0.notifyDataSetChanged();
    }

    public void onItemRangeChanged(ObservableList<T> observableList, int positionStart, int itemCount) {
        this.this$0.notifyItemRangeChanged(positionStart, itemCount);
    }

    public void onItemRangeInserted(ObservableList<T> observableList, int positionStart, int itemCount) {
        this.this$0.notifyItemRangeInserted(positionStart, itemCount);
        this.this$0.notifyItemRangeChanged(positionStart, itemCount);
    }

    public void onItemRangeRemoved(ObservableList<T> observableList, int positionStart, int itemCount) {
        this.this$0.notifyItemRangeRemoved(positionStart, itemCount);
        this.this$0.notifyItemRangeChanged(positionStart, itemCount);
    }

    public void onItemRangeMoved(ObservableList<T> observableList, int fromPosition, int toPosition, int itemCount) {
        this.this$0.notifyItemMoved(fromPosition, toPosition);
        this.this$0.notifyDataSetChanged();
    }
}
