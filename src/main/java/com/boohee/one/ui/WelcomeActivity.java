package com.boohee.one.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.boohee.model.club.Story;
import com.boohee.one.R;
import com.boohee.one.http.RequestManager;
import com.boohee.one.ui.adapter.SimpleBaseAdapter;
import com.boohee.one.ui.adapter.SimpleBaseAdapter.ViewHolder;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.Helper;
import com.huewu.pla.lib.MultiColumnListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends BaseNoToolbarActivity implements OnClickListener,
        OnRefreshListener {
    private StoryAdapter mAdapter;
    private List<Story> mDataList = new ArrayList();
    private MultiColumnListView mclvStory;
    private SwipeRefreshLayout  viewSwipeRefresh;

    class StoryAdapter extends SimpleBaseAdapter<Story> {
        private ImageLoader mLoader = ImageLoader.getInstance();

        public StoryAdapter(Context context, List<Story> dataList) {
            super(context, dataList);
        }

        public int getItemResource() {
            return R.layout.jb;
        }

        public View getItemView(int position, View convertView, ViewHolder holder) {
            final Story story = (Story) getItem(position);
            if (story != null) {
                this.mLoader.displayImage(story.pic, (ImageView) holder.getView(R.id.iv_story),
                        ImageLoaderOptions.color(R.color.ju));
                ((TextView) holder.getView(R.id.tv_title)).setText(story.title);
                convertView.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        BrowserSuccessHistoryActivity.comeOnBaby(StoryAdapter.this.context, story
                                .title, story.url);
                    }
                });
            }
            return convertView;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.e2);
        this.viewSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.view_swiperefresh);
        this.viewSwipeRefresh.setOnRefreshListener(this);
        this.viewSwipeRefresh.setColorSchemeColors(getResources().getColor(R.color.hk));
        this.mclvStory = (MultiColumnListView) findViewById(R.id.mclv_story);
        this.mAdapter = new StoryAdapter(this, this.mDataList);
        this.mclvStory.setAdapter(this.mAdapter);
        requestJson();
    }

    private void requestJson() {
        RequestManager.addRequest(new StringRequest("http://shop.boohee" +
                ".com/store/pages/story_json", new Listener<String>() {
            public void onResponse(String response) {
                try {
                    List<Story> stories = Story.parseStory(response);
                    if (stories != null && stories.size() > 0) {
                        WelcomeActivity.this.mDataList.clear();
                        WelcomeActivity.this.mDataList.addAll(stories);
                        WelcomeActivity.this.mAdapter.notifyDataSetChanged();
                        stories.clear();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                WelcomeActivity.this.setRefresh(false);
            }
        }, new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Helper.showToast(error.toString());
                WelcomeActivity.this.setRefresh(false);
            }
        }), this);
        setRefresh(true);
    }

    protected void onDestroy() {
        super.onDestroy();
        this.mDataList.clear();
    }

    public void onRefresh() {
        requestJson();
    }

    private void setRefresh(boolean refreshing) {
        if (refreshing) {
            if (!this.viewSwipeRefresh.isRefreshing()) {
                this.viewSwipeRefresh.setRefreshing(true);
            }
        } else if (this.viewSwipeRefresh.isRefreshing()) {
            this.viewSwipeRefresh.setRefreshing(false);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_regster:
                NewLoginAndRegisterActivity.comeOnBaby(false, this);
                return;
            case R.id.bt_login:
                NewLoginAndRegisterActivity.comeOnBaby(true, this);
                return;
            default:
                return;
        }
    }

    public static void comeOnBaby(Context context) {
        if (context != null) {
            context.startActivity(new Intent(context, WelcomeActivity.class));
        }
    }

    public void onBackPressed() {
        finish();
    }
}
