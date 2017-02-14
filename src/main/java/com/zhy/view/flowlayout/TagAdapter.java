package com.zhy.view.flowlayout;

import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public abstract class TagAdapter<T> {
    private HashSet<Integer> mCheckedPosList = new HashSet();
    private OnDataChangedListener mOnDataChangedListener;
    private List<T>               mTagDatas;

    interface OnDataChangedListener {
        void onChanged();
    }

    public abstract View getView(FlowLayout flowLayout, int i, T t);

    public TagAdapter(List<T> datas) {
        this.mTagDatas = datas;
    }

    public TagAdapter(T[] datas) {
        this.mTagDatas = new ArrayList(Arrays.asList(datas));
    }

    void setOnDataChangedListener(OnDataChangedListener listener) {
        this.mOnDataChangedListener = listener;
    }

    public void setSelectedList(int... pos) {
        for (int valueOf : pos) {
            this.mCheckedPosList.add(Integer.valueOf(valueOf));
        }
        notifyDataChanged();
    }

    HashSet<Integer> getPreCheckedList() {
        return this.mCheckedPosList;
    }

    public int getCount() {
        return this.mTagDatas == null ? 0 : this.mTagDatas.size();
    }

    public void notifyDataChanged() {
        this.mOnDataChangedListener.onChanged();
    }

    public T getItem(int position) {
        return this.mTagDatas.get(position);
    }
}
