package com.boohee.status;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.baidu.location.BDGeofence;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.boohee.api.StatusApi;
import com.boohee.model.status.StatusUser;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.ui.fragment.BaseFragment;
import com.boohee.utils.DateHelper;
import com.boohee.utils.Helper;
import com.boohee.widgets.LightAlertDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class SearchFriendsFragment extends BaseFragment {
    static final String TAG = SearchFriendsFragment.class.getName();
    private LinearLayout          clearGpsBar;
    private int                   id;
    private FriendsAdapter        mFriendsAdapter;
    private ArrayList<StatusUser> mFriendsUsers;
    private ListView              mListView;
    private LocationClient mLocationClient = null;
    private PullToRefreshListView mPullRefreshListView;
    private BDLocationListener    myListener;

    public class MyLocationListener implements BDLocationListener {
        public void onReceiveLocation(BDLocation location) {
            if (location != null && location.getLatitude() != Double.MIN_VALUE && location
                    .getLongitude() != Double.MIN_VALUE) {
                Helper.showLog(SearchFriendsFragment.TAG, "baidu location requestLocation success" +
                        ". ");
                Helper.showLog(SearchFriendsFragment.TAG, "location update: <city>" + location
                        .getCity() + "</city><cityCode>" + location.getCityCode() +
                        "</cityCode><lat>" + location.getLatitude() + "</lat></lng>" + location
                        .getLongitude() + "</lng><time>" + DateHelper.getCurrentDateTime() +
                        "</time>");
                SearchFriendsFragment.this.SerchNearByFriends(location);
            } else if (SearchFriendsFragment.this.getActivity() != null) {
                Helper.showLog(SearchFriendsFragment.TAG, "baidu location failed");
                Helper.showToast(SearchFriendsFragment.this.getActivity(), (CharSequence)
                        "定位失败，请检查网络");
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    public static SearchFriendsFragment newInstance(int id) {
        SearchFriendsFragment fragment = new SearchFriendsFragment();
        fragment.id = id;
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.id == 1) {
            initLocationClient();
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fv, null);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews();
        initUI();
    }

    private void findViews() {
        this.clearGpsBar = (LinearLayout) getView().findViewById(R.id.clear_gps_bar);
        ((Button) getView().findViewById(R.id.clear_gps)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                LightAlertDialog.create(SearchFriendsFragment.this.getActivity(),
                        SearchFriendsFragment.this.getString(R.string.o_), SearchFriendsFragment
                                .this.getString(R.string.fm)).setNegativeButton
                        (SearchFriendsFragment.this.getString(R.string.eq), null)
                        .setPositiveButton(SearchFriendsFragment.this.getString(R.string.gn), new
                                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SearchFriendsFragment.this.clearGps();
                    }
                }).show();
            }
        });
        this.mPullRefreshListView = (PullToRefreshListView) getView().findViewById(R.id.listview);
        this.mListView = (ListView) this.mPullRefreshListView.getRefreshableView();
        this.mPullRefreshListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                Intent intent = new Intent(SearchFriendsFragment.this.getActivity(),
                        UserTimelineActivity.class);
                intent.putExtra(UserTimelineActivity.NICK_NAME, SearchFriendsFragment.this
                        .mFriendsAdapter.getItem(arg2 - 1).nickname);
                SearchFriendsFragment.this.startActivity(intent);
            }
        });
        this.mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                SearchFriendsFragment.this.initUI();
            }
        });
    }

    protected void initUI() {
        if (this.id == 0) {
            getRecommendFriends();
        } else if (this.id == 1) {
            requestLocation();
            this.clearGpsBar.setVisibility(0);
        } else if (this.id == 2) {
            getAlikeFriends();
        }
    }

    private void SerchNearByFriends(BDLocation bdLocation) {
        StatusApi.getUserNearNby(getActivity(), "中国", bdLocation.getProvince(), bdLocation
                .getCity(), bdLocation.getDistrict(), bdLocation.getLongitude() + "", bdLocation
                .getLatitude() + "", new JsonCallback(getActivity()) {
            public void ok(JSONObject object) {
                super.ok(object);
                try {
                    SearchFriendsFragment.this.mFriendsUsers = StatusUser.parseUsers(object
                            .getJSONArray("users").toString());
                    if (!SearchFriendsFragment.this.mFriendsUsers.equals("[]")) {
                        SearchFriendsFragment.this.mFriendsAdapter = new FriendsAdapter
                                (SearchFriendsFragment.this.getActivity(), SearchFriendsFragment
                                        .this.mFriendsUsers, "follower");
                        SearchFriendsFragment.this.mListView.setAdapter(SearchFriendsFragment
                                .this.mFriendsAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onFinish() {
                super.onFinish();
                SearchFriendsFragment.this.mPullRefreshListView.onRefreshComplete();
            }
        });
    }

    private void clearGps() {
        StatusApi.getClearNearNby(getActivity(), "", "", "", new JsonCallback(getActivity()) {
            public void ok(JSONObject object) {
                super.ok(object);
                if (SearchFriendsFragment.this.mFriendsUsers != null && SearchFriendsFragment
                        .this.mFriendsAdapter != null) {
                    SearchFriendsFragment.this.mFriendsUsers.clear();
                    SearchFriendsFragment.this.mFriendsAdapter.notifyDataSetChanged();
                    Helper.showToast(SearchFriendsFragment.this.getActivity(), (int) R.string.fo);
                    SearchFriendsFragment.this.clearGpsBar.setVisibility(8);
                }
            }
        });
    }

    private void getRecommendFriends() {
        StatusApi.getUserRecommended(getActivity(), new JsonCallback(getActivity()) {
            public void ok(JSONObject object) {
                super.ok(object);
                try {
                    SearchFriendsFragment.this.mFriendsUsers = StatusUser.parseUsers(object
                            .getJSONArray("users").toString());
                    SearchFriendsFragment.this.mFriendsAdapter = new FriendsAdapter
                            (SearchFriendsFragment.this.getActivity(), SearchFriendsFragment.this
                                    .mFriendsUsers, "follower");
                    SearchFriendsFragment.this.mListView.setAdapter(SearchFriendsFragment.this
                            .mFriendsAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onFinish() {
                super.onFinish();
                SearchFriendsFragment.this.mPullRefreshListView.onRefreshComplete();
            }
        });
    }

    private void getAlikeFriends() {
        StatusApi.getUserAlike(getActivity(), new JsonCallback(getActivity()) {
            public void ok(JSONObject object) {
                super.ok(object);
                try {
                    SearchFriendsFragment.this.mFriendsUsers = StatusUser.parseUsers(object
                            .getJSONArray("users").toString());
                    SearchFriendsFragment.this.mFriendsAdapter = new FriendsAdapter
                            (SearchFriendsFragment.this.getActivity(), SearchFriendsFragment.this
                                    .mFriendsUsers, "follower");
                    SearchFriendsFragment.this.mListView.setAdapter(SearchFriendsFragment.this
                            .mFriendsAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onFinish() {
                super.onFinish();
                SearchFriendsFragment.this.mPullRefreshListView.onRefreshComplete();
            }
        });
    }

    protected void initLocationClient() {
        this.myListener = new MyLocationListener();
        this.mLocationClient = new LocationClient(getActivity());
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setCoorType(BDGeofence.COORD_TYPE_BD09LL);
        option.setScanSpan(900000);
        option.setLocationMode(LocationMode.Hight_Accuracy);
        option.setIsNeedAddress(true);
        this.mLocationClient.setLocOption(option);
        this.mLocationClient.registerLocationListener(this.myListener);
    }

    protected synchronized void requestLocation() {
        if (this.mLocationClient != null && this.mLocationClient.isStarted()) {
            Helper.showLog(TAG, "requestLocation");
            this.mLocationClient.requestLocation();
        } else if (this.mLocationClient.isStarted()) {
            Helper.showLog(TAG, "mLocationClient is not started!");
        } else {
            this.mLocationClient.start();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.id == 1) {
            this.mLocationClient.unRegisterLocationListener(this.myListener);
            this.mLocationClient.stop();
            Helper.showLog(TAG, "Exit BhLocationService");
        }
    }
}
