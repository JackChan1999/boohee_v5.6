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
import com.boohee.model.CustomSport;
import com.boohee.model.RecordSport;
import com.boohee.one.R;
import com.boohee.one.event.CustomSportEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.ui.fragment.AddCustomSportFragment;
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

public class CustomSportFragment extends BaseFragment {
    private boolean hasMore     = true;
    public  boolean isFirstLoad = true;
    ListView listView;
    private int mCurrentPage = 1;
    private CustomSportListAdapter mCustomSportListAdapter;
    private List<CustomSport> mCustomSports = new ArrayList();
    private int               mPage         = 1;
    @InjectView(2131427552)
    PullToRefreshListView mPullRefreshListView;
    public String record_on;

    public static CustomSportFragment newInstance(String record_on) {
        CustomSportFragment fragment = new CustomSportFragment();
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
        this.mCustomSportListAdapter = new CustomSportListAdapter(getActivity(), this
                .mCustomSports);
        this.listView.setAdapter(this.mCustomSportListAdapter);
        this.mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                CustomSportFragment.this.mPage = 1;
                CustomSportFragment.this.mCurrentPage = CustomSportFragment.this.mPage;
                CustomSportFragment.this.hasMore = true;
                CustomSportFragment.this.loadData();
            }
        });
        this.mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
            public void onLastItemVisible() {
                if (CustomSportFragment.this.mPage > CustomSportFragment.this.mCurrentPage) {
                    CustomSportFragment.this.mCurrentPage = CustomSportFragment.this.mPage;
                    CustomSportFragment.this.loadData();
                }
            }
        });
        this.listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                float f = 0.0f;
                if (CustomSportFragment.this.mCustomSports != null && CustomSportFragment.this
                        .mCustomSports.size() != 0) {
                    CustomSport customSport = (CustomSport) CustomSportFragment.this
                            .mCustomSports.get(position - 1);
                    RecordSport recordSport = new RecordSport();
                    recordSport.record_on = CustomSportFragment.this.record_on;
                    recordSport.duration = TextUtils.isEmpty(customSport.amount) ? 0.0f : Float
                            .parseFloat(customSport.amount);
                    if (!TextUtils.isEmpty(customSport.calory)) {
                        f = Float.parseFloat(customSport.calory);
                    }
                    recordSport.calory = f;
                    recordSport.activity_name = customSport.activity_name;
                    recordSport.unit_name = customSport.unit_name;
                    AddCustomSportFragment.newInstance(0, recordSport).show(CustomSportFragment
                            .this.getFragmentManager(), "addCustomSportFragment");
                }
            }
        });
    }

    public void firstLoad() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (CustomSportFragment.this.isAdded()) {
                    CustomSportFragment.this.mPullRefreshListView.setRefreshing();
                    CustomSportFragment.this.isFirstLoad = false;
                }
            }
        }, 500);
    }

    private void loadData() {
        if (this.hasMore) {
            FoodApi.getCustomActivities(getActivity(), this.mPage, new JsonCallback(getActivity()) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    CustomSportFragment.this.refreshData(object);
                }

                public void onFinish() {
                    super.onFinish();
                    CustomSportFragment.this.mPullRefreshListView.onRefreshComplete();
                }
            });
        }
    }

    private void refreshData(JSONObject object) {
        if (this.mPage == 1) {
            this.mCustomSports.clear();
        }
        List<CustomSport> customSports = FastJsonUtils.parseList(object.optString
                ("custom_activities"), CustomSport.class);
        if (customSports == null || customSports.size() <= 0) {
            this.hasMore = false;
        } else {
            this.mCustomSports.addAll(customSports);
            this.mPage++;
        }
        this.mCustomSportListAdapter.notifyDataSetChanged();
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

    public void onEventMainThread(CustomSportEvent customSportEvent) {
        if (customSportEvent != null && customSportEvent.getCustomSport() != null) {
            this.mCustomSports.add(customSportEvent.getCustomSport());
            this.mCustomSportListAdapter.notifyDataSetChanged();
        }
    }
}
