package kale.adapter;

import android.databinding.ObservableList;
import android.databinding.ObservableList.OnListChangedCallback;

class CommonAdapter$1 extends OnListChangedCallback<ObservableList<T>> {
    final /* synthetic */ CommonAdapter this$0;

    CommonAdapter$1(CommonAdapter commonAdapter) {
        this.this$0 = commonAdapter;
    }

    public void onChanged(ObservableList<T> observableList) {
        this.this$0.notifyDataSetChanged();
    }

    public void onItemRangeChanged(ObservableList<T> observableList, int positionStart, int itemCount) {
        this.this$0.notifyDataSetChanged();
    }

    public void onItemRangeInserted(ObservableList<T> observableList, int positionStart, int itemCount) {
        this.this$0.notifyDataSetChanged();
    }

    public void onItemRangeMoved(ObservableList<T> observableList, int fromPosition, int toPosition, int itemCount) {
        this.this$0.notifyDataSetChanged();
    }

    public void onItemRangeRemoved(ObservableList<T> observableList, int positionStart, int itemCount) {
        this.this$0.notifyDataSetChanged();
    }
}
