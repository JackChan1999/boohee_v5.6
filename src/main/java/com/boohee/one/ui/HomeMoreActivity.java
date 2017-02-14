package com.boohee.one.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.boohee.api.StatusApi;
import com.boohee.main.GestureActivity;
import com.boohee.model.Event;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.ui.adapter.HomeMoreListAdapter;
import com.boohee.utility.BooheeScheme;
import com.boohee.utils.Helper;
import com.boohee.utils.TextUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

public class HomeMoreActivity extends GestureActivity {
    private static final String KEY_MORE_URL = "key_more_url";
    private static final String KEY_TITLE    = "key_title";
    private HomeMoreListAdapter mAdapter;
    private int         mCurrentPage = 1;
    private List<Event> mDataList    = new ArrayList();
    private String                mMoreUrl;
    private PullToRefreshListView mPullRefreshListView;
    private String                mTitle;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bm);
        this.mMoreUrl = getIntent().getStringExtra(KEY_MORE_URL);
        this.mTitle = getIntent().getStringExtra(KEY_TITLE);
        if (!TextUtil.isEmpty(this.mTitle)) {
            setTitle(this.mTitle);
        }
        initView();
    }

    private void initView() {
        this.mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.listview);
        this.mPullRefreshListView.setMode(Mode.BOTH);
        this.mPullRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                HomeMoreActivity.this.loadData(true);
            }

            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                HomeMoreActivity.this.loadData(false);
            }
        });
        this.mPullRefreshListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event event = (Event) parent.getAdapter().getItem(position);
                BooheeScheme.handleUrl(HomeMoreActivity.this, event.url, event.title);
            }
        });
        ((ListView) this.mPullRefreshListView.getRefreshableView()).setDividerHeight(0);
        this.mAdapter = new HomeMoreListAdapter(this, this.mDataList);
        this.mPullRefreshListView.setAdapter(this.mAdapter);
        this.mPullRefreshListView.postDelayed(new Runnable() {
            public void run() {
                HomeMoreActivity.this.mPullRefreshListView.setRefreshing(true);
            }
        }, 500);
    }

    private void loadData(boolean isFirst) {
        if (isFirst) {
            this.mCurrentPage = 1;
            this.mDataList.clear();
        }
        StatusApi.getMoreCategory(this.mMoreUrl, this.mCurrentPage, this.activity, new
                JsonCallback(this.activity) {
            public void ok(JSONArray array) {
                List<Event> tmpData = Event.parseEvents(array.toString());
                if (tmpData == null || tmpData.size() <= 0) {
                    Helper.showToast((CharSequence) "没有更多了");
                    return;
                }
                HomeMoreActivity.this.mDataList.addAll(tmpData);
                HomeMoreActivity.this.mAdapter.notifyDataSetChanged();
                HomeMoreActivity.this.mCurrentPage = HomeMoreActivity.this.mCurrentPage + 1;
            }

            public void onFinish() {
                HomeMoreActivity.this.mPullRefreshListView.onRefreshComplete();
            }
        });
    }

    public static void comeOnBaby(Context context, String moreUrl, String title) {
        if (context != null) {
            if (!TextUtil.isEmpty(moreUrl)) {
                Intent intent = new Intent(context, HomeMoreActivity.class);
                intent.putExtra(KEY_MORE_URL, moreUrl);
                intent.putExtra(KEY_TITLE, title);
                context.startActivity(intent);
            }
        }
    }
}
