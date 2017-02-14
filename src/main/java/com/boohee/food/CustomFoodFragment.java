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
import com.boohee.model.CustomFood;
import com.boohee.model.RecordFood;
import com.boohee.one.R;
import com.boohee.one.event.MyFoodEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.ui.fragment.AddCustomDietFragment;
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

public class CustomFoodFragment extends BaseFragment {
    private boolean hasMore     = true;
    public  boolean isFirstLoad = true;
    ListView listView;
    private CustomFoodListAdapter mAddFoodListAdapter;
    private int              mCurrentPage = 1;
    private List<CustomFood> mFoodList    = new ArrayList();
    private int              mPage        = 1;
    @InjectView(2131427552)
    PullToRefreshListView mPullRefreshListView;
    public int mTimeType = -1;
    public String record_on;

    public static CustomFoodFragment newInstance(int time_type, String record_on) {
        CustomFoodFragment fragment = new CustomFoodFragment();
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
        this.mAddFoodListAdapter = new CustomFoodListAdapter(getActivity(), this.mFoodList);
        this.listView.setAdapter(this.mAddFoodListAdapter);
        this.mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                CustomFoodFragment.this.mPage = 1;
                CustomFoodFragment.this.mCurrentPage = CustomFoodFragment.this.mPage;
                CustomFoodFragment.this.hasMore = true;
                CustomFoodFragment.this.loadData();
            }
        });
        this.mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
            public void onLastItemVisible() {
                if (CustomFoodFragment.this.mPage > CustomFoodFragment.this.mCurrentPage) {
                    CustomFoodFragment.this.mCurrentPage = CustomFoodFragment.this.mPage;
                    CustomFoodFragment.this.loadData();
                }
            }
        });
        this.listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                float f = 0.0f;
                if (!CustomFoodFragment.this.isRemoved()) {
                    if (position == 1) {
                        CustomFoodListActivity.comeOnBaby(CustomFoodFragment.this.getActivity());
                        return;
                    }
                    CustomFood customFood = (CustomFood) parent.getAdapter().getItem(position);
                    if (customFood != null) {
                        RecordFood recordFood = new RecordFood();
                        recordFood.time_type = CustomFoodFragment.this.mTimeType;
                        recordFood.record_on = CustomFoodFragment.this.record_on;
                        recordFood.amount = TextUtils.isEmpty(customFood.amount) ? 0.0f : Float
                                .parseFloat(customFood.amount);
                        if (!TextUtils.isEmpty(customFood.calory)) {
                            f = Float.parseFloat(customFood.calory);
                        }
                        recordFood.calory = f;
                        recordFood.food_name = customFood.food_name;
                        recordFood.unit_name = customFood.unit_name;
                        recordFood.thumb_img_url = customFood.image_url;
                        AddCustomDietFragment.newInstance(0, recordFood.clone()).show
                                (CustomFoodFragment.this.getFragmentManager(),
                                        "addCustomDietFragment");
                    }
                }
            }
        });
    }

    public void firstLoad() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (CustomFoodFragment.this.isAdded()) {
                    CustomFoodFragment.this.mPullRefreshListView.setRefreshing();
                    CustomFoodFragment.this.isFirstLoad = false;
                }
            }
        }, 500);
    }

    private void loadData() {
        if (this.hasMore) {
            FoodApi.getCustomFoods(getActivity(), this.mPage, new JsonCallback(getActivity()) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    CustomFoodFragment.this.refreshData(object);
                }

                public void onFinish() {
                    super.onFinish();
                    CustomFoodFragment.this.mPullRefreshListView.onRefreshComplete();
                }
            });
        }
    }

    private void refreshData(JSONObject object) {
        if (this.mPage == 1) {
            this.mFoodList.clear();
        }
        List<CustomFood> foodList = FastJsonUtils.parseList(object.optString("custom_foods"),
                CustomFood.class);
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

    public void onEventMainThread(MyFoodEvent myFoodEvent) {
        if (myFoodEvent != null) {
            switch (myFoodEvent.getFlag()) {
                case 1:
                    this.hasMore = true;
                    this.mPage = 1;
                    loadData();
                    return;
                default:
                    return;
            }
        }
    }
}
