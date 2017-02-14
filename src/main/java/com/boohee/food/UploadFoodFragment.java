package com.boohee.food;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.api.FoodApi;
import com.boohee.food.adapter.AddUploadFoodAdapter;
import com.boohee.model.UploadFoodItem;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.ui.fragment.AddDietFragment;
import com.boohee.one.ui.fragment.BaseFragment;
import com.boohee.utils.FastJsonUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class UploadFoodFragment extends BaseFragment {
    private boolean hasMore     = true;
    public  boolean isFirstLoad = true;
    ListView listView;
    private AddUploadFoodAdapter mAdapter;
    private int                  mCurrentPage = 1;
    private List<UploadFoodItem> mFoodList    = new ArrayList();
    private int                  mPage        = 1;
    @InjectView(2131427552)
    PullToRefreshListView mPullRefreshListView;
    public int mTimeType = -1;
    public String record_on;

    public static UploadFoodFragment newInstance(int time_type, String record_on) {
        UploadFoodFragment fragment = new UploadFoodFragment();
        fragment.mTimeType = time_type;
        fragment.record_on = record_on;
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.ft, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject((Object) this, view);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.listView = (ListView) this.mPullRefreshListView.getRefreshableView();
        this.listView.addHeaderView(LayoutInflater.from(getActivity()).inflate(R.layout.h4, null));
        this.mAdapter = new AddUploadFoodAdapter(getActivity(), this.mFoodList);
        this.listView.setAdapter(this.mAdapter);
        this.mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                UploadFoodFragment.this.mPage = 1;
                UploadFoodFragment.this.mCurrentPage = UploadFoodFragment.this.mPage;
                UploadFoodFragment.this.hasMore = true;
                UploadFoodFragment.this.loadData();
            }
        });
        this.mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
            public void onLastItemVisible() {
                if (UploadFoodFragment.this.mPage > UploadFoodFragment.this.mCurrentPage) {
                    UploadFoodFragment.this.mCurrentPage = UploadFoodFragment.this.mPage;
                    UploadFoodFragment.this.loadData();
                }
            }
        });
        this.listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!UploadFoodFragment.this.isRemoved()) {
                    if (position == 1) {
                        ListUploadActivity.comeOnBaby(UploadFoodFragment.this.getActivity());
                        return;
                    }
                    UploadFoodItem uploadFoodItem = (UploadFoodItem) parent.getAdapter().getItem
                            (position);
                    if (uploadFoodItem != null) {
                        AddDietFragment.newInstance(UploadFoodFragment.this.mTimeType,
                                UploadFoodFragment.this.record_on, uploadFoodItem.code).show
                                (UploadFoodFragment.this.getChildFragmentManager(),
                                        "addDietFragment");
                    }
                }
            }
        });
    }

    private void loadData() {
        if (this.hasMore) {
            FoodApi.getSuccessUploadFoodList(this.mPage, getActivity(), new JsonCallback
                    (getActivity()) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    UploadFoodFragment.this.refreshData(object);
                }

                public void onFinish() {
                    super.onFinish();
                    UploadFoodFragment.this.mPullRefreshListView.onRefreshComplete();
                }
            });
        }
    }

    private void refreshData(JSONObject object) {
        if (this.mPage == 1) {
            this.mFoodList.clear();
        }
        List<UploadFoodItem> foodList = FastJsonUtils.parseList(object.optString("foods"),
                UploadFoodItem.class);
        if (foodList == null || foodList.size() <= 0) {
            this.hasMore = false;
        } else {
            this.mFoodList.addAll(foodList);
            this.mPage++;
        }
        this.mAdapter.notifyDataSetChanged();
    }

    public void firstLoad() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (UploadFoodFragment.this.isAdded()) {
                    UploadFoodFragment.this.mPullRefreshListView.setRefreshing();
                    UploadFoodFragment.this.isFirstLoad = false;
                }
            }
        }, 500);
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
