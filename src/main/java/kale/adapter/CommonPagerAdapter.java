package kale.adapter;

import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import kale.adapter.item.AdapterItem;
import kale.adapter.util.DataBindingJudgement;
import kale.adapter.util.IAdapter;

public abstract class CommonPagerAdapter<T> extends BasePagerAdapter<View> implements IAdapter<T> {
    private List<T> mDataList;
    private LayoutInflater mInflater;
    private boolean mIsLazy;

    public CommonPagerAdapter(@Nullable List<T> data) {
        this(data, false);
    }

    public CommonPagerAdapter(@Nullable List<T> data, boolean isLazy) {
        this.mIsLazy = false;
        if (data == null) {
            data = new ArrayList();
        }
        if (DataBindingJudgement.SUPPORT_DATABINDING && (data instanceof ObservableList)) {
            ((ObservableList) data).addOnListChangedCallback(new 1(this));
        }
        this.mDataList = data;
        this.mIsLazy = isLazy;
    }

    public int getCount() {
        return this.mDataList.size();
    }

    @NonNull
    protected View getViewFromItem(View item, int pos) {
        return item;
    }

    public View instantiateItem(ViewGroup container, int position) {
        View view = (View) super.instantiateItem(container, position);
        if (!this.mIsLazy) {
            initItem(position, view);
        }
        return view;
    }

    public void setPrimaryItem(ViewGroup container, int position, @NonNull Object object) {
        if (this.mIsLazy && object != this.currentItem) {
            initItem(position, (View) object);
        }
        super.setPrimaryItem(container, position, object);
    }

    private void initItem(int position, View view) {
        ((AdapterItem) view.getTag(R.id.tag_item)).handleData(getConvertedData(this.mDataList.get(position), getItemType(position)), position);
    }

    protected View createItem(ViewGroup viewPager, int position) {
        if (this.mInflater == null) {
            this.mInflater = LayoutInflater.from(viewPager.getContext());
        }
        AdapterItem item = createItem(getItemType(position));
        View view = this.mInflater.inflate(item.getLayoutResId(), null);
        view.setTag(R.id.tag_item, item);
        item.bindViews(view);
        item.setViews();
        return view;
    }

    public void setIsLazy(boolean isLazy) {
        this.mIsLazy = isLazy;
    }

    @NonNull
    public Object getConvertedData(T data, Object type) {
        return data;
    }

    @Deprecated
    protected Object getItemType(int position) {
        if (position < this.mDataList.size()) {
            return getItemType(this.mDataList.get(position));
        }
        return null;
    }

    public Object getItemType(T t) {
        return Integer.valueOf(-1);
    }

    public void setData(@NonNull List<T> data) {
        this.mDataList = data;
    }

    public List<T> getData() {
        return this.mDataList;
    }
}
