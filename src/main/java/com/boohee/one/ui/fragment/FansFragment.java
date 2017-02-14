package com.boohee.one.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.boohee.api.StatusApi;
import com.boohee.model.status.StatusUser;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.status.FriendsAdapter;
import com.boohee.status.UserTimelineActivity;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class FansFragment extends BaseFragment {
    private boolean isLastVisible = false;
    private FriendsAdapter        mAdapter;
    private ListView              mListView;
    private PullToRefreshListView mPullToRefreshListView;
    private ArrayList<StatusUser> mUsers;
    private int page = 1;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.gv, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListView();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                FansFragment.this.mPullToRefreshListView.setRefreshing();
            }
        }, 500);
    }

    private void initListView() {
        this.mPullToRefreshListView = (PullToRefreshListView) getView().findViewById(R.id.listview);
        this.mListView = (ListView) this.mPullToRefreshListView.getRefreshableView();
        this.mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                FansFragment.this.page = 1;
                FansFragment.this.getFans();
            }
        });
        this.mPullToRefreshListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                Intent intent = new Intent(FansFragment.this.getActivity(), UserTimelineActivity
                        .class);
                intent.putExtra(UserTimelineActivity.NICK_NAME, FansFragment.this.mAdapter
                        .getItem(arg2 - 1).nickname);
                FansFragment.this.startActivity(intent);
            }
        });
        this.mPullToRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
            public void onLastItemVisible() {
                if (!FansFragment.this.isLastVisible) {
                    FansFragment.this.getNextFans();
                }
            }
        });
    }

    public void getFans() {
        StatusApi.getFollowers(getActivity(), this.page, new JsonCallback(getActivity()) {
            public void ok(JSONObject object) {
                super.ok(object);
                try {
                    FansFragment.this.mUsers = StatusUser.parseUsers(object.getJSONArray
                            ("followers").toString());
                    FansFragment.this.mAdapter = new FriendsAdapter(FansFragment.this.getActivity
                            (), FansFragment.this.mUsers, "follower");
                    FansFragment.this.mListView.setAdapter(FansFragment.this.mAdapter);
                    FansFragment.this.page = FansFragment.this.page + 1;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onFinish() {
                super.onFinish();
                FansFragment.this.mPullToRefreshListView.onRefreshComplete();
            }
        });
    }

    public void getNextFans() {
        this.isLastVisible = true;
        StatusApi.getFollowers(getActivity(), this.page, new JsonCallback(getActivity()) {
            public void ok(JSONObject object) {
                super.ok(object);
                try {
                    FansFragment.this.mUsers.addAll(StatusUser.parseUsers(object.getJSONArray
                            ("followers").toString()));
                    FansFragment.this.mAdapter.notifyDataSetChanged();
                    FansFragment.this.isLastVisible = false;
                    FansFragment.this.page = FansFragment.this.page + 1;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onFinish() {
                super.onFinish();
            }
        });
    }
}
