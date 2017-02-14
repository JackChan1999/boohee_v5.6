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
import com.boohee.utility.Event;
import com.boohee.utils.Helper;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class FriendFragment extends BaseFragment {
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
                FriendFragment.this.mPullToRefreshListView.setRefreshing();
            }
        }, 500);
    }

    private void initListView() {
        this.mPullToRefreshListView = (PullToRefreshListView) getView().findViewById(R.id.listview);
        this.mListView = (ListView) this.mPullToRefreshListView.getRefreshableView();
        this.mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                FriendFragment.this.getFriends();
            }
        });
        this.mPullToRefreshListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                Intent intent = new Intent(FriendFragment.this.getActivity(),
                        UserTimelineActivity.class);
                intent.putExtra(UserTimelineActivity.NICK_NAME, FriendFragment.this.mAdapter
                        .getItem(arg2 - 1).nickname);
                FriendFragment.this.startActivity(intent);
            }
        });
        this.mPullToRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
            public void onLastItemVisible() {
                if (!FriendFragment.this.isLastVisible) {
                    FriendFragment.this.getNextFriends();
                }
            }
        });
    }

    public void getFriends() {
        this.page = 1;
        StatusApi.getFollowings(getActivity(), this.page, new JsonCallback(getActivity()) {
            public void ok(JSONObject object) {
                super.ok(object);
                Helper.showLog(BaseFragment.TAG, object.toString());
                MobclickAgent.onEvent(FriendFragment.this.getActivity(), Event
                        .STATUS_VIEW_FRIEND_PAGE);
                try {
                    FriendFragment.this.mUsers = StatusUser.parseUsers(object.getJSONArray
                            ("followings").toString());
                    FriendFragment.this.mAdapter = new FriendsAdapter(FriendFragment.this
                            .getActivity(), FriendFragment.this.mUsers);
                    FriendFragment.this.mListView.setAdapter(FriendFragment.this.mAdapter);
                    FriendFragment.this.page = FriendFragment.this.page + 1;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onFinish() {
                super.onFinish();
                FriendFragment.this.mPullToRefreshListView.onRefreshComplete();
            }
        });
    }

    public void getNextFriends() {
        this.isLastVisible = true;
        StatusApi.getFollowings(getActivity(), this.page, new JsonCallback(getActivity()) {
            public void ok(JSONObject object) {
                super.ok(object);
                try {
                    FriendFragment.this.mUsers.addAll(StatusUser.parseUsers(object.getJSONArray
                            ("followings").toString()));
                    FriendFragment.this.mAdapter.notifyDataSetChanged();
                    FriendFragment.this.isLastVisible = false;
                    FriendFragment.this.page = FriendFragment.this.page + 1;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
