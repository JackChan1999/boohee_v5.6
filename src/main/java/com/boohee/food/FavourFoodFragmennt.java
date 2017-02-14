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
import com.boohee.model.FavourFood;
import com.boohee.one.R;
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

public class FavourFoodFragmennt extends BaseFragment {
    private boolean hasMore     = true;
    public  boolean isFirstLoad = true;
    ListView listView;
    private FavourFoodListAdapter mAddFoodListAdapter;
    private int              mCurrentPage = 1;
    private List<FavourFood> mFoodList    = new ArrayList();
    private int              mPage        = 1;
    @InjectView(2131427552)
    PullToRefreshListView mPullRefreshListView;
    public int mTimeType = -1;
    public String record_on;

    public static FavourFoodFragmennt newInstance(int time_type, String record_on) {
        FavourFoodFragmennt fragment = new FavourFoodFragmennt();
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
        this.mAddFoodListAdapter = new FavourFoodListAdapter(getActivity(), this.mFoodList);
        this.listView.setAdapter(this.mAddFoodListAdapter);
        this.mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                FavourFoodFragmennt.this.mPage = 1;
                FavourFoodFragmennt.this.mCurrentPage = FavourFoodFragmennt.this.mPage;
                FavourFoodFragmennt.this.hasMore = true;
                FavourFoodFragmennt.this.loadData();
            }
        });
        this.mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
            public void onLastItemVisible() {
                if (FavourFoodFragmennt.this.mPage > FavourFoodFragmennt.this.mCurrentPage) {
                    FavourFoodFragmennt.this.mCurrentPage = FavourFoodFragmennt.this.mPage;
                    FavourFoodFragmennt.this.loadData();
                }
            }
        });
        this.listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (FavourFoodFragmennt.this.mFoodList != null && FavourFoodFragmennt.this
                        .mFoodList.size() != 0 && position >= 1) {
                    AddDietFragment.newInstance(FavourFoodFragmennt.this.mTimeType,
                            FavourFoodFragmennt.this.record_on, ((FavourFood) FavourFoodFragmennt
                                    .this.mFoodList.get(position - 1)).code).show
                            (FavourFoodFragmennt.this.getChildFragmentManager(), "addDietFragment");
                }
            }
        });
    }

    public void firstLoad() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (FavourFoodFragmennt.this.isAdded()) {
                    FavourFoodFragmennt.this.mPullRefreshListView.setRefreshing();
                    FavourFoodFragmennt.this.isFirstLoad = false;
                }
            }
        }, 500);
    }

    private void loadData() {
        if (this.hasMore) {
            FoodApi.getFavoriteFoods(this.mPage, getActivity(), new JsonCallback(getActivity()) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    FavourFoodFragmennt.this.refreshData(object);
                }

                public void onFinish() {
                    super.onFinish();
                    if (FavourFoodFragmennt.this.mPullRefreshListView != null) {
                        FavourFoodFragmennt.this.mPullRefreshListView.onRefreshComplete();
                    }
                }
            });
        }
    }

    private void refreshData(JSONObject object) {
        if (this.mPage == 1) {
            this.mFoodList.clear();
        }
        List<FavourFood> foodList = FastJsonUtils.parseList(object.optString("foods"), FavourFood
                .class);
        if (foodList == null || foodList.size() <= 0) {
            this.hasMore = false;
        } else {
            this.mFoodList.addAll(foodList);
            this.mPage++;
        }
        this.mAddFoodListAdapter.notifyDataSetChanged();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(String record_on) {
    }
}
