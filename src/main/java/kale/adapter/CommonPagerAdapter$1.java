package kale.adapter;

import android.databinding.ObservableList;
import android.databinding.ObservableList.OnListChangedCallback;

class CommonPagerAdapter$1 extends OnListChangedCallback<ObservableList<T>> {
    final /* synthetic */ CommonPagerAdapter this$0;

    CommonPagerAdapter$1(CommonPagerAdapter commonPagerAdapter) {
        this.this$0 = commonPagerAdapter;
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
