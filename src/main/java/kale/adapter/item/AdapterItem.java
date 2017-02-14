package kale.adapter.item;

import android.support.annotation.LayoutRes;
import android.view.View;

public interface AdapterItem<T> {
    void bindViews(View view);

    @LayoutRes
    int getLayoutResId();

    void handleData(T t, int i);

    void setViews();
}
