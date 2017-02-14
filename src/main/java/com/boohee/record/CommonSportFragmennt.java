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
import com.boohee.database.OnePreference;
import com.boohee.model.RecordSport;
import com.boohee.model.Sport;
import com.boohee.one.R;
import com.boohee.one.cache.CacheKey;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.ui.adapter.CommonSportListAdapter;
import com.boohee.one.ui.fragment.AddSportFragment;
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

public class CommonSportFragmennt extends BaseFragment {
    private boolean hasMore     = true;
    public  boolean isFirstLoad = true;
    ListView listView;
    private CommonSportListAdapter mCommonSportListAdapter;
    private int mCurrentPage = 1;
    private int mPage        = 1;
    @InjectView(2131427552)
    PullToRefreshListView mPullRefreshListView;
    private List<Sport> mSports = new ArrayList();
    public String record_on;

    public static CommonSportFragmennt newInstance(String record_on) {
        CommonSportFragmennt fragment = new CommonSportFragmennt();
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

    private float getlatestWeight() {
        if (OnePreference.getLatestWeight() > 0.0f) {
            return OnePreference.getLatestWeight();
        }
        return 55.0f;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.listView = (ListView) this.mPullRefreshListView.getRefreshableView();
        this.mCommonSportListAdapter = new CommonSportListAdapter(getActivity(), this.mSports,
                getlatestWeight());
        this.listView.setAdapter(this.mCommonSportListAdapter);
        this.mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                CommonSportFragmennt.this.mPage = 1;
                CommonSportFragmennt.this.mCurrentPage = CommonSportFragmennt.this.mPage;
                CommonSportFragmennt.this.hasMore = true;
                CommonSportFragmennt.this.loadData();
            }
        });
        this.mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
            public void onLastItemVisible() {
                if (CommonSportFragmennt.this.mPage > CommonSportFragmennt.this.mCurrentPage) {
                    CommonSportFragmennt.this.mCurrentPage = CommonSportFragmennt.this.mPage;
                    CommonSportFragmennt.this.loadData();
                }
            }
        });
        this.listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (CommonSportFragmennt.this.mSports != null && CommonSportFragmennt.this
                        .mSports.size() != 0 && position >= 1) {
                    Sport sport = (Sport) CommonSportFragmennt.this.mSports.get(position - 1);
                    RecordSport recordSport = new RecordSport();
                    recordSport.mets = Float.parseFloat(sport.mets);
                    recordSport.activity_name = sport.name;
                    recordSport.activity_id = sport.id;
                    recordSport.unit_name = Sport.UNIT_NAME;
                    recordSport.thumb_img_url = sport.big_photo_url;
                    recordSport.record_on = CommonSportFragmennt.this.record_on;
                    AddSportFragment.newInstance(0, recordSport).show(CommonSportFragmennt.this
                            .getChildFragmentManager(), "addSportFragment");
                }
            }
        });
        refreshData(this.mCache.getAsJSONObject(CacheKey.COMMON_SPORT));
        firstLoad();
    }

    public void firstLoad() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (CommonSportFragmennt.this.isAdded()) {
                    CommonSportFragmennt.this.mPullRefreshListView.setRefreshing();
                    CommonSportFragmennt.this.isFirstLoad = false;
                }
            }
        }, 500);
    }

    private void loadData() {
        if (this.hasMore) {
            RecordApi.getActivitiesHot(getActivity(), this.mPage, new JsonCallback(getActivity()) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    CommonSportFragmennt.this.refreshData(object);
                    CommonSportFragmennt.this.mCache.put(CacheKey.COMMON_SPORT, object);
                }

                public void onFinish() {
                    super.onFinish();
                    if (CommonSportFragmennt.this.mPullRefreshListView != null) {
                        CommonSportFragmennt.this.mPullRefreshListView.onRefreshComplete();
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
                this.mSports.clear();
            }
            List<Sport> sports = FastJsonUtils.parseList(object.optString("activities"), Sport
                    .class);
            if (sports == null || sports.size() <= 0) {
                this.hasMore = false;
            } else {
                this.mSports.addAll(sports);
                this.mPage++;
            }
            this.mCommonSportListAdapter.notifyDataSetChanged();
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
