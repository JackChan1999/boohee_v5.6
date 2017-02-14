package com.boohee.status;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.boohee.api.StatusApi;
import com.boohee.main.FragmentAdapter;
import com.boohee.main.GestureActivity;
import com.boohee.model.status.StatusUser;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.utility.BuilderIntent;
import com.boohee.utility.Event;
import com.boohee.utils.Helper;
import com.boohee.utils.KeyBoardUtils;
import com.boohee.utils.Keyboard;
import com.boohee.widgets.PagerSlidingTabStrip;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class SearchFriendsActivity extends GestureActivity {
    static final String TAG = SearchFriendsActivity.class.getName();
    private ImageView            closeBtn;
    public  PagerSlidingTabStrip indicator;
    private boolean isLastVisible       = false;
    private boolean isSearchListVisible = false;
    private ListView mListView;
    private int mPage = 1;
    private PullToRefreshListView mPullRefreshListView;
    private String                mQueryString;
    private FriendsAdapter        mSearchAdapter;
    private List<StatusUser> mSearchUsers = new ArrayList();
    private LinearLayout mainLayout;
    private EditText     searchText;
    private TextView     txt_search;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cv);
        setTitle(R.string.lw);
        MobclickAgent.onEvent(this.ctx, Event.STATUS_VIEW_FIND_FRIEND_PAGE);
        init();
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), getFrags(), getTitles()));
        ((PagerSlidingTabStrip) findViewById(R.id.indicator)).setViewPager(pager);
    }

    private ArrayList<Fragment> getFrags() {
        ArrayList<Fragment> frags = new ArrayList();
        frags.add(SearchFriendsFragment.newInstance(0));
        frags.add(SearchFriendsFragment.newInstance(1));
        frags.add(SearchFriendsFragment.newInstance(2));
        return frags;
    }

    private String[] getTitles() {
        return getResources().getStringArray(R.array.c);
    }

    private void init() {
        this.closeBtn = (ImageView) findViewById(R.id.search_close_btn);
        this.closeBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SearchFriendsActivity.this.searchText.setText("");
            }
        });
        this.searchText = (EditText) findViewById(R.id.search_text);
        this.searchText.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Keyboard.close(SearchFriendsActivity.this.ctx, SearchFriendsActivity.this
                        .searchText);
                SearchFriendsActivity.this.searchRemote();
                return false;
            }
        });
        this.searchText.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    SearchFriendsActivity.this.closeBtn.setVisibility(8);
                } else {
                    SearchFriendsActivity.this.closeBtn.setVisibility(0);
                }
            }

            public void afterTextChanged(Editable s) {
            }
        });
        this.txt_search = (TextView) findViewById(R.id.txt_search);
        this.txt_search.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (SearchFriendsActivity.this.isSearchListVisible) {
                    SearchFriendsActivity.this.mPullRefreshListView.setVisibility(8);
                    SearchFriendsActivity.this.mainLayout.setVisibility(0);
                    SearchFriendsActivity.this.txt_search.setText(R.string.a38);
                    SearchFriendsActivity.this.isSearchListVisible = false;
                    SearchFriendsActivity.this.searchText.setText("");
                    return;
                }
                Keyboard.close(SearchFriendsActivity.this.ctx, SearchFriendsActivity.this
                        .searchText);
                SearchFriendsActivity.this.searchRemote();
            }
        });
        this.mainLayout = (LinearLayout) findViewById(R.id.main_layout);
        this.mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.listview);
        this.mListView = (ListView) this.mPullRefreshListView.getRefreshableView();
        this.mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                SearchFriendsActivity.this.searchRemote();
            }
        });
        this.mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
            public void onLastItemVisible() {
                if (!SearchFriendsActivity.this.isLastVisible) {
                    SearchFriendsActivity.this.searchNext();
                }
            }
        });
        this.mListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                new BuilderIntent(SearchFriendsActivity.this.ctx, UserTimelineActivity.class)
                        .putExtra(UserTimelineActivity.NICK_NAME, ((StatusUser)
                                SearchFriendsActivity.this.mSearchUsers.get(position - 1))
                                .nickname).startActivity();
            }
        });
    }

    private void searchRemote() {
        this.mPage = 1;
        this.mQueryString = this.searchText.getText().toString().trim();
        if (TextUtils.isEmpty(this.mQueryString)) {
            this.mPullRefreshListView.onRefreshComplete();
            Helper.showToast((int) R.string.ex);
            return;
        }
        showLoading();
        StatusApi.getUserSearch(this, this.mQueryString, this.mPage, new JsonCallback(this) {
            public void ok(JSONObject object) {
                super.ok(object);
                try {
                    SearchFriendsActivity.this.mSearchUsers = StatusUser.parseUsers(object
                            .getJSONArray("users").toString());
                    if (SearchFriendsActivity.this.mSearchUsers.size() == 0) {
                        Helper.showToast((int) R.string.xx);
                    }
                    SearchFriendsActivity.this.mSearchAdapter = new FriendsAdapter
                            (SearchFriendsActivity.this.ctx, SearchFriendsActivity.this
                                    .mSearchUsers, "follower");
                    SearchFriendsActivity.this.mListView.setAdapter(SearchFriendsActivity.this
                            .mSearchAdapter);
                    SearchFriendsActivity.this.mPullRefreshListView.setVisibility(0);
                    SearchFriendsActivity.this.mainLayout.setVisibility(8);
                    SearchFriendsActivity.this.txt_search.setText(R.string.eq);
                    SearchFriendsActivity.this.isSearchListVisible = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onFinish() {
                super.onFinish();
                SearchFriendsActivity.this.dismissLoading();
                SearchFriendsActivity.this.mPullRefreshListView.onRefreshComplete();
                KeyBoardUtils.closeKeybord(SearchFriendsActivity.this.ctx, SearchFriendsActivity
                        .this.searchText);
            }
        });
    }

    private void searchNext() {
        this.isLastVisible = true;
        String str = this.mQueryString;
        int i = this.mPage + 1;
        this.mPage = i;
        StatusApi.getUserSearch(this, str, i, new JsonCallback(this) {
            public void ok(JSONObject object) {
                super.ok(object);
                try {
                    SearchFriendsActivity.this.mSearchUsers.addAll(StatusUser.parseUsers(object
                            .getJSONArray("users").toString()));
                    SearchFriendsActivity.this.mSearchAdapter.notifyDataSetChanged();
                    SearchFriendsActivity.this.isLastVisible = false;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onFinish() {
                super.onFinish();
                KeyBoardUtils.closeKeybord(SearchFriendsActivity.this.ctx, SearchFriendsActivity
                        .this.searchText);
            }
        });
    }

    public static void comeOnBaby(Context context) {
        if (context != null) {
            context.startActivity(new Intent(context, SearchFriendsActivity.class));
        }
    }
}
