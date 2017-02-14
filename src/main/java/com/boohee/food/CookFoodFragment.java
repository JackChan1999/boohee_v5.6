package com.boohee.food;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.api.FoodApi;
import com.boohee.food.adapter.AddCustomCookAdapter;
import com.boohee.model.CustomCookItem;
import com.boohee.model.RecordFood;
import com.boohee.one.R;
import com.boohee.one.event.MyFoodEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.ui.fragment.AddCustomDietFragment;
import com.boohee.one.ui.fragment.BaseFragment;
import com.boohee.utility.TimeLinePatterns;
import com.boohee.utils.FastJsonUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import de.greenrobot.event.EventBus;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class CookFoodFragment extends BaseFragment {
    private boolean hasMore      = true;
    public  boolean isFirstLoad  = true;
    private int     mCurrentPage = 1;
    private int     mPage        = 1;
    ListView listView;
    private AddCustomCookAdapter mAdapter;
    private List<CustomCookItem> mFoodList = new ArrayList();
    @InjectView(2131427552)
    PullToRefreshListView mPullRefreshListView;
    public int mTimeType = -1;
    public String record_on;

    public static CookFoodFragment newInstance(int time_type, String record_on) {
        CookFoodFragment fragment = new CookFoodFragment();
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
        EventBus.getDefault().register(this);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.listView = (ListView) this.mPullRefreshListView.getRefreshableView();
        this.listView.addHeaderView(LayoutInflater.from(getActivity()).inflate(R.layout.h4, null));
        this.mAdapter = new AddCustomCookAdapter(getActivity(), this.mFoodList);
        this.listView.setAdapter(this.mAdapter);
        this.mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                CookFoodFragment.this.mPage = 1;
                CookFoodFragment.this.mCurrentPage = CookFoodFragment.this.mPage;
                CookFoodFragment.this.hasMore = true;
                CookFoodFragment.this.loadData();
            }
        });
        this.mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
            public void onLastItemVisible() {
                if (CookFoodFragment.this.mPage > CookFoodFragment.this.mCurrentPage) {
                    CookFoodFragment.this.mCurrentPage = CookFoodFragment.this.mPage;
                    CookFoodFragment.this.loadData();
                }
            }
        });
        this.listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!CookFoodFragment.this.isRemoved()) {
                    if (position == 1) {
                        CustomCookListActivity.comeOnBaby(CookFoodFragment.this.getActivity());
                    }
                    CustomCookItem customCook = (CustomCookItem) parent.getAdapter().getItem
                            (position);
                    if (customCook != null) {
                        RecordFood recordFood = new RecordFood();
                        recordFood.time_type = CookFoodFragment.this.mTimeType;
                        recordFood.record_on = CookFoodFragment.this.record_on;
                        recordFood.amount = 1.0f;
                        recordFood.calory = customCook.calory;
                        recordFood.food_name = customCook.name;
                        recordFood.unit_name = "份";
                        if (!TextUtils.isEmpty(customCook.photo)) {
                            if (customCook.photo.startsWith("http")) {
                                recordFood.thumb_img_url = customCook.photo;
                            } else {
                                recordFood.thumb_img_url = TimeLinePatterns.WEB_SCHEME +
                                        customCook.photo;
                            }
                        }
                        AddCustomDietFragment.newInstance(0, recordFood).show(CookFoodFragment
                                .this.getFragmentManager(), "addCustomDietFragment");
                    }
                }
            }
        });
    }

    public void firstLoad() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (CookFoodFragment.this.isAdded()) {
                    CookFoodFragment.this.mPullRefreshListView.setRefreshing();
                    CookFoodFragment.this.isFirstLoad = false;
                }
            }
        }, 500);
    }

    private void loadData() {
        if (this.hasMore) {
            FoodApi.getCustomMenus(getActivity(), this.mPage, new JsonCallback(getActivity()) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    CookFoodFragment.this.refreshData(object);
                }

                public void onFinish() {
                    super.onFinish();
                    CookFoodFragment.this.mPullRefreshListView.onRefreshComplete();
                }
            });
        }
    }

    private void refreshData(JSONObject object) {
        if (this.mPage == 1) {
            this.mFoodList.clear();
        }
        List<CustomCookItem> foodList = FastJsonUtils.parseList(object.optString("menus"),
                CustomCookItem.class);
        if (foodList == null || foodList.size() <= 0) {
            this.hasMore = false;
        } else {
            this.mFoodList.addAll(foodList);
            this.mPage++;
        }
        this.mAdapter.notifyDataSetChanged();
    }

    public void onEventMainThread(MyFoodEvent myFoodEvent) {
        if (myFoodEvent != null) {
            switch (myFoodEvent.getFlag()) {
                case 3:
                    this.mPage = 1;
                    loadData();
                    return;
                default:
                    return;
            }
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        EventBus.getDefault().unregister(this);
    }
}
