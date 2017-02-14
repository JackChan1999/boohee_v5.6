package kale.adapter.util;

import android.support.annotation.NonNull;
import java.util.List;
import kale.adapter.item.AdapterItem;

public interface IAdapter<T> {
    @NonNull
    AdapterItem createItem(Object obj);

    @NonNull
    Object getConvertedData(T t, Object obj);

    List<T> getData();

    Object getItemType(T t);

    void notifyDataSetChanged();

    void setData(@NonNull List<T> list);
}
