package com.boohee.record;

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

import com.boohee.api.RecordApi;
import com.boohee.model.CommonFood;
import com.boohee.one.R;
import com.boohee.one.cache.CacheKey;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.ui.fragment.AddDietFragment;
import com.boohee.one.ui.fragment.BaseFragment;
import com.boohee.utils.FastJsonUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import de.greenrobot.event.EventBus;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class CommonFoodFragmennt extends BaseFragment {
    private boolean hasMore     = true;
    public  boolean isFirstLoad = true;
    ListView listView;
    private CommonFoodListAdapter mAddFoodListAdapter;
    private int              mCurrentPage = 1;
    private List<CommonFood> mFoodList    = new ArrayList();
    private int              mPage        = 1;
    @InjectView(2131427552)
    PullToRefreshListView mPullRefreshListView;
    public int mTimeType = -1;
    public String record_on;

    public static CommonFoodFragmennt newInstance(int time_type, String record_on) {
        CommonFoodFragmennt fragment = new CommonFoodFragmennt();
        fragment.mTimeType = time_type;
        fragment.record_on = record_on;
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
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
        this.mAddFoodListAdapter = new CommonFoodListAdapter(getActivity(), this.mFoodList);
        this.listView.setAdapter(this.mAddFoodListAdapter);
        this.mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                CommonFoodFragmennt.this.mPage = 1;
                CommonFoodFragmennt.this.mCurrentPage = CommonFoodFragmennt.this.mPage;
                CommonFoodFragmennt.this.hasMore = true;
                CommonFoodFragmennt.this.loadData();
            }
        });
        this.mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
            public void onLastItemVisible() {
                if (CommonFoodFragmennt.this.mPage > CommonFoodFragmennt.this.mCurrentPage) {
                    CommonFoodFragmennt.this.mCurrentPage = CommonFoodFragmennt.this.mPage;
                    CommonFoodFragmennt.this.loadData();
                }
            }
        });
        this.listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (CommonFoodFragmennt.this.mFoodList != null && CommonFoodFragmennt.this
                        .mFoodList.size() != 0 && position >= 1 && CommonFoodFragmennt.this
                        .isAdded() && !CommonFoodFragmennt.this.isDetached()) {
                    AddDietFragment.newInstance(CommonFoodFragmennt.this.mTimeType,
                            CommonFoodFragmennt.this.record_on, ((CommonFood) CommonFoodFragmennt
                                    .this.mFoodList.get(position - 1)).code).show
                            (CommonFoodFragmennt.this.getChildFragmentManager(), "addDietFragment");
                }
            }
        });
        refreshData(this.mCache.getAsJSONObject(CacheKey.COMMON_FOOD));
        firstLoad();
    }

    public void firstLoad() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (CommonFoodFragmennt.this.isAdded()) {
                    CommonFoodFragmennt.this.mPullRefreshListView.setRefreshing();
                    CommonFoodFragmennt.this.isFirstLoad = false;
                }
            }
        }, 500);
    }

    private void loadData() {
        if (this.hasMore) {
            RecordApi.getEatingsHot(getActivity(), this.mPage, new JsonCallback(getActivity()) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    CommonFoodFragmennt.this.refreshData(object);
                    CommonFoodFragmennt.this.mCache.put(CacheKey.COMMON_FOOD, object);
                }

                public void onFinish() {
                    super.onFinish();
                    if (CommonFoodFragmennt.this.mPullRefreshListView != null) {
                        CommonFoodFragmennt.this.mPullRefreshListView.onRefreshComplete();
                    }
                }

                public void fail(String message) {
                }
            });
        }
    }

    private void refreshData(JSONObject object) {
        if (object != null) {
            if (this.mPage == 1) {
                this.mFoodList.clear();
            }
            List<CommonFood> foodList = FastJsonUtils.parseList(object.optString("foods"),
                    CommonFood.class);
            if (foodList == null || foodList.size() <= 0) {
                this.hasMore = false;
            } else {
                this.mFoodList.addAll(foodList);
                this.mPage++;
            }
            this.mAddFoodListAdapter.notifyDataSetChanged();
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(String s) {
    }
}
