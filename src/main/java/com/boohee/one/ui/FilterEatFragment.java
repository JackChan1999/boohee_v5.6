package com.boohee.one.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.boohee.database.UserPreference;
import com.boohee.model.FilterSyncBean;
import com.boohee.model.FilterSyncFoodBean;
import com.boohee.myview.DragScaleImageView;
import com.boohee.myview.DragScaleImageView.OnDeleteListener;
import com.boohee.myview.DragScaleImageView.OnSingleTapListener;
import com.boohee.myview.DragScaleImageView.OnSizeChangeListener;
import com.boohee.one.R;
import com.boohee.one.ui.adapter.HorizontalIconListAdapter;
import com.boohee.one.ui.adapter.HorizontalIconListAdapter.DataSet;
import com.boohee.utility.DensityUtil;
import com.boohee.utils.DateHelper;
import com.boohee.utils.ViewUtils;
import com.boohee.widgets.HorizontalListView;

import java.util.ArrayList;
import java.util.Iterator;

public class FilterEatFragment extends Fragment {
    private static final int[]    EAT_BLACK_RES_IDS = new int[]{R.drawable.vi, R.drawable.tj, R
            .drawable.we, R.drawable.tn, R.drawable.ot, R.drawable.tl, R.drawable.nz, R.drawable
            .o5};
    private static final float[]  EAT_DATAS         = new float[]{200.0f, 300.0f, 120.0f, 210.0f,
            110.0f, 120.0f, 240.0f, 130.0f};
    private static final int[]    EAT_RES_IDS       = new int[]{R.drawable.vh, R.drawable.ti, R
            .drawable.wd, R.drawable.tm, R.drawable.os, R.drawable.tk, R.drawable.ny, R.drawable
            .o4};
    private static final String[] EAT_RES_TEXTS     = new String[]{"谷薯", "荤菜", "素菜", "荤素搭配",
            "水果", "奶制品", "甜点", "饮料"};
    private HorizontalIconListAdapter adapter;
    private TextView                  caloryText;
    private ArrayList<DragScaleImageView> dragViewList = new ArrayList();
    private HorizontalListView iconListView;
    private Context            mContext;
    private Uri                mUri;
    private View               markView;
    private FrameLayout        parentLayout;
    private ImageView          preImage;
    private UserPreference     preference;
    private FilterSyncFoodBean syncBean;

    private class ItemClickListener implements OnItemClickListener {
        private ItemClickListener() {
        }

        public void onItemClick(AdapterView<?> adapterView, View itemView, int position, long id) {
            final DragScaleImageView dragView = new DragScaleImageView(FilterEatFragment.this
                    .mContext);
            dragView.setImageResource(FilterEatFragment.EAT_BLACK_RES_IDS[position]);
            LayoutParams lp = new LayoutParams(DensityUtil.dip2px(FilterEatFragment.this
                    .mContext, 108.0f), DensityUtil.dip2px(FilterEatFragment.this.mContext, 108
            .0f));
            lp.gravity = 17;
            dragView.setLayoutParams(lp);
            dragView.setCalory((int) FilterEatFragment.EAT_DATAS[position]);
            dragView.setOnSizeChangedListener(new OnSizeChangeListener() {
                public void onSizeChanged() {
                    int sumCalory = ItemClickListener.this.calSumCalory();
                    FilterEatFragment.this.caloryText.setText(sumCalory + "");
                    FilterEatFragment.this.syncBean.setCalory((float) sumCalory);
                }
            });
            dragView.setOnSingleTabListener(new OnSingleTapListener() {
                public void onSingleTab() {
                    Iterator it = FilterEatFragment.this.dragViewList.iterator();
                    while (it.hasNext()) {
                        ((DragScaleImageView) it.next()).setFocusable(false);
                    }
                }
            });
            dragView.setOnDeleteListener(new OnDeleteListener() {
                public void onDelete() {
                    FilterEatFragment.this.dragViewList.remove(dragView);
                    FilterEatFragment.this.caloryText.setText(ItemClickListener.this.calSumCalory
                            () + "");
                }
            });
            FilterEatFragment.this.dragViewList.add(dragView);
            FilterEatFragment.this.caloryText.setText(calSumCalory() + "");
            FilterEatFragment.this.parentLayout.addView(dragView);
            FilterEatFragment.this.saveTagName();
            FilterEatFragment.this.syncBean = new FilterSyncFoodBean();
            FilterEatFragment.this.syncBean.setTag(DateHelper.getTimeFiled());
        }

        private int calSumCalory() {
            int sumCalory = 0;
            Iterator it = FilterEatFragment.this.dragViewList.iterator();
            while (it.hasNext()) {
                sumCalory += ((DragScaleImageView) it.next()).getCalory();
            }
            return sumCalory > 9999 ? 9999 : sumCalory;
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fp, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView(view);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mContext = getActivity();
        this.mUri = getActivity().getIntent().getData();
        this.preference = UserPreference.getInstance(this.mContext);
        ViewUtils.initImageView(getActivity(), this.mUri, this.preImage);
        initListView();
    }

    public View getView() {
        Iterator it = this.dragViewList.iterator();
        while (it.hasNext()) {
            ((DragScaleImageView) it.next()).setVisibility(8);
        }
        this.dragViewList.clear();
        return this.parentLayout;
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.parentLayout.removeAllViews();
        this.dragViewList.clear();
    }

    private void findView(View view) {
        this.parentLayout = (FrameLayout) view.findViewById(R.id.filter_parent);
        this.preImage = (ImageView) view.findViewById(R.id.filterImageView);
        this.iconListView = (HorizontalListView) view.findViewById(R.id.filterIconListView);
        this.markView = view.findViewById(R.id.markView);
        this.caloryText = (TextView) this.markView.findViewById(R.id.upMiddleText);
    }

    private void initListView() {
        this.adapter = new HorizontalIconListAdapter(this.mContext, new DataSet(EAT_RES_IDS,
                EAT_RES_TEXTS));
        this.iconListView.setAdapter(this.adapter);
        this.iconListView.setOnItemClickListener(new ItemClickListener());
    }

    private void saveTagName() {
        this.preference.putString(ImageFilterActivity.KEY_POST_TAG, DateHelper.getTimeFiled());
    }

    public void saveSyncData(FilterSyncBean syncData) {
        if (this.syncBean != null && this.syncBean.getCalory() > 1.0f) {
            syncData.setFood(this.syncBean);
        }
    }
}
